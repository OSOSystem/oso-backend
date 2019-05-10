package de.ososystem.human.infrastructure.services

import com.fasterxml.jackson.databind.ObjectMapper
import de.ososystem.human.domain.events.HumanEvent
import de.ososystem.human.domain.events.Id
import de.ososystem.human.domain.factories.EventFactory
import de.ososystem.human.domain.repositories.EventRepository
import de.ososystem.human.domain.services.impl.EventServiceBase
import de.ososystem.human.infrastructure.entities.DomainEventEntity
import de.ososystem.human.infrastructure.entities.SendState
import de.ososystem.human.infrastructure.repositories.EventRepositorySpring
import org.slf4j.LoggerFactory
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.support.SendResult
import org.springframework.scheduling.annotation.Async
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.util.concurrent.ListenableFutureCallback
import java.lang.Exception
import java.time.ZonedDateTime

class EventServiceKafka(
    eventFactory: EventFactory,
    val eventRepository: EventRepository,
    val eventRepositorySpring: EventRepositorySpring,
    val kafkaTemplate: KafkaTemplate<String, String>,
    val objectMapper: ObjectMapper
) : EventServiceBase(
    eventFactory
) {
    override fun fireEvent(event: HumanEvent) {
        LOGGER.info("firing of event<$event> requested")
        eventRepository.saveEvent(event)
    }

    // TODO set the delay via configuration
    // TODO maybe enable triggering of this method via fireEvent
    // TODO execute this in a separate thread just for its own purpose
    @Scheduled(fixedDelay = 1000)
    fun fireEvents() {
        LOGGER.debug("checking for event to fire")
        eventRepositorySpring.findBySendStateNotIn(listOf(SendState.SENT, SendState.SUCCESSFULL)).forEach { event ->
            LOGGER.info("firing event<$event>")
            val result = kafkaTemplate.send(event.topic, event.key, event.toSendString())

            event.lastSendTime = ZonedDateTime.now()
            event.sendState = SendState.SENT
            event.sendTries++

            eventRepositorySpring.save(event)

            result.addCallback(object : ListenableFutureCallback<SendResult<String, String>> {
                override fun onSuccess(result: SendResult<String, String>?) {
                    onSuccess(result, event.id)
                }

                override fun onFailure(ex: Throwable) {
                    onFailure(ex, event.id)
                }
            })
        }
    }

    private fun onSuccess(result: SendResult<String, String>?, eventId: Id) {
        LOGGER.debug("event<$eventId> fired successfully - result<$result>")
        val event = eventRepositorySpring.findById(eventId).orElse(null)
        event ?: TODO("something happened to the Event in between sending and successfull sent")
        event.sendState = SendState.SUCCESSFULL
        eventRepositorySpring.save(event)
    }

    private fun onFailure(ex: Throwable, eventId: Id) {
        LOGGER.debug("event<$eventId> firing failed with exception<$ex>")
        val event = eventRepositorySpring.findById(eventId).orElse(null)

        event ?: TODO("something happened to the event in between sending and failed sent")
        event.sendState = SendState.FAILED
        eventRepositorySpring.save(event)
    }

    val DomainEventEntity.topic: String
        get() = domain

    fun DomainEventEntity.toSendString() =
        objectMapper.writeValueAsString(
            mutableMapOf(
                "id" to id,
                "version" to version,
                "time" to time,
                "type" to "$domain.$type",
                "key" to key,
                "payload" to payload
            )
        )

    companion object {
        private val LOGGER = LoggerFactory.getLogger(EventServiceKafka::class.java)
    }
}