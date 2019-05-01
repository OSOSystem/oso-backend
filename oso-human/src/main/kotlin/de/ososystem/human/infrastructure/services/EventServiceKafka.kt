package de.ososystem.human.infrastructure.services

import com.fasterxml.jackson.databind.ObjectMapper
import de.ososystem.human.domain.events.HumanEvent
import de.ososystem.human.domain.events.Id
import de.ososystem.human.domain.factories.EventFactory
import de.ososystem.human.domain.repositories.EventRepository
import de.ososystem.human.domain.services.impl.EventServiceBase
import org.slf4j.LoggerFactory
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.support.SendResult
import org.springframework.util.concurrent.ListenableFutureCallback
import java.lang.Exception
import java.time.ZonedDateTime

class EventServiceKafka(
    eventFactory: EventFactory,
    val eventRepository: EventRepository,
    val kafkaTemplate: KafkaTemplate<String, String>,
    val objectMapper: ObjectMapper
) : EventServiceBase(
    eventFactory
) {
    override fun fireEvent(event: HumanEvent) {
        LOGGER.info("firing of event<$event> requested")
        eventRepository.saveEvent(event)

        fireEvents()
    }

    // TODO this should be triggered frequently outside of the fireEvent-method
    // we do want to have this happen asynchronously to fireEvent
    // this shall ensure
    // 1. firing of events will be retriggered ( a certain amount of times ) until it was successfull, independent of any errors, etc
    // 2. whatever triggers fireEvent will run independent of this routine
    private fun fireEvents() {
        LOGGER.debug("checking for event to fire")
        eventRepository.findUnSentEvents().forEach { event ->
            LOGGER.info("firing event<$event>")
            val result = kafkaTemplate.send(event.topic, event.key, event.toSendString())

            //event.sentDate = ZonedDateTime.now()
            //event.sendStatus = SEND
            //event.sends++

            eventRepository.saveEvent(event)

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
        LOGGER.debug("event<$eventId> fired successfully")
        val event = eventRepository.findEventById(eventId)
        event ?: TODO("something happened to the Event in between sending and successfull sent")
        //event.sendStatus = SUCCESS
        eventRepository.saveEvent(event)
    }

    private fun onFailure(ex: Throwable, eventId: Id) {
        LOGGER.debug("event<$eventId> firing failed")
        val event = eventRepository.findEventById(eventId)

        event ?: TODO("something happened to the event in between sending and failed sent")
        //event.sendStatus = FAILURE
        eventRepository.saveEvent(event)
    }

    val HumanEvent.topic: String
        get() = domain

    fun HumanEvent.toSendString() =
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

    private fun EventRepository.findUnSentEvents(): Iterable<HumanEvent> {
        // TODO method missing currently
        return findEventWithHighestId()?.let { listOf(it) } ?: emptyList()
    }

    companion object {
        private val LOGGER = LoggerFactory.getLogger(EventServiceKafka::class.java)
    }
}