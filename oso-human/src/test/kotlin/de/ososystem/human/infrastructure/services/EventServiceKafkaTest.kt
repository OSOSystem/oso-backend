package de.ososystem.human.infrastructure.services

import com.fasterxml.jackson.databind.ObjectMapper
import de.ososystem.human.*
import de.ososystem.human.domain.entities.Human
import de.ososystem.human.domain.events.HumanEvent
import de.ososystem.human.domain.factories.EventFactory
import de.ososystem.human.domain.repositories.EventRepository
import de.ososystem.human.domain.services.DOMAIN_HUMAN
import de.ososystem.human.domain.services.TYPE_CREATED
import de.ososystem.human.infrastructure.entities.DomainEventEntity
import de.ososystem.human.infrastructure.entities.SendState
import de.ososystem.human.infrastructure.repositories.EventRepositorySpring
import org.junit.Assert
import org.junit.jupiter.api.Test
import org.mockito.ArgumentCaptor
import org.mockito.Mockito
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.support.SendResult
import org.springframework.util.concurrent.ListenableFuture
import org.springframework.util.concurrent.ListenableFutureCallback
import java.time.ZonedDateTime
import java.util.*

class EventServiceKafkaTest {
    val eventFactory: EventFactory = mock()
    val eventRepository: EventRepository = mock()
    val eventRepositorySpring: EventRepositorySpring = mock()
    val kafkaTemplate: KafkaTemplate<String, String> = mock()
    val objectMapper: ObjectMapper = mock()

    val eventServiceKafka = EventServiceKafka(eventFactory, eventRepository, eventRepositorySpring, kafkaTemplate, objectMapper)

    @Test
    fun testFireHumanEvent_createsEventForSending() {
        val domain = DOMAIN_HUMAN
        val type = TYPE_CREATED
        val human = Human(
            UUID.fromString("88fa54d0-7036-48ff-9562-6c1455c1aa04"),
            "test",
            "test"
        )
        val event = HumanEvent(
            1,
            "1.0",
            ZonedDateTime.now(),
            domain,
            type,
            human.id.toString(),
            ByteArray(0)
        )

        Mockito.`when`(eventFactory.createHumanEvent(domain, type, human)).thenReturn(event)
        Mockito.`when`(eventRepository.saveEvent(event)).thenReturn(event)

        eventServiceKafka.fireHumanEvent(domain, type, human)

        verifyOnce(eventRepository).saveEvent(event)
    }

    @Test
    fun testFireEvent() {
        val event = DomainEventEntity(
            1,
            "1.0",
            ZonedDateTime.now(),
            DOMAIN_HUMAN,
            TYPE_CREATED,
            "key",
            ByteArray(0)
        )
        val result = mock<ListenableFuture<SendResult<String, String>>>()

        Mockito.`when`(kafkaTemplate.send(eq(event.domain), eq(event.key), any())).thenReturn(result)
        Mockito.`when`(eventRepositorySpring.save(event)).thenReturn(event)
        Mockito.doNothing().`when`(result).addCallback(any())

        eventServiceKafka.fireEvent(event)

        verifyOnce(kafkaTemplate).send(eq(event.domain), eq(event.key), any())
        verifyOnce(eventRepositorySpring).save(event)
        verifyOnce(result).addCallback(any())

        Assert.assertEquals("sendState not set", SendState.SENT, event.sendState)
        Assert.assertEquals("tries not updated", 1, event.sendTries)
        Assert.assertNotNull("sendTime not updated", event.lastSendTime)
    }

    @Test
    fun testFireEvent_onSuccess() {
        val event = DomainEventEntity(
            1,
            "1.0",
            ZonedDateTime.now(),
            DOMAIN_HUMAN,
            TYPE_CREATED,
            "key",
            ByteArray(0)
        )
        val result = mock<ListenableFuture<SendResult<String, String>>>()
        val sendResult = mock<SendResult<String, String>>()

        val captorCallback = captorForClass<ListenableFutureCallback<in SendResult<String, String>>>()

        Mockito.`when`(kafkaTemplate.send(eq(event.domain), eq(event.key), any())).thenReturn(result)
        Mockito.`when`(eventRepositorySpring.save(event)).thenReturn(event)
        Mockito.doNothing().`when`(result).addCallback(captorCallback.capture())
        Mockito.`when`(eventRepositorySpring.findById(event.id)).thenReturn(Optional.of(event))

        eventServiceKafka.fireEvent(event)

        captorCallback.value.onSuccess(sendResult)

        verifyOnce(kafkaTemplate).send(eq(event.domain), eq(event.key), any())
        verifyOnce(result).addCallback(any())
        Mockito.verify(eventRepositorySpring, Mockito.times(2)).save(event)

        Assert.assertEquals("sendState not set", SendState.SUCCESSFULL, event.sendState)
        Assert.assertEquals("tries not updated", 1, event.sendTries)
        Assert.assertNotNull("sendTime not updated", event.lastSendTime)
    }

    @Test
    fun testFireEvent_onFailure() {
        val event = DomainEventEntity(
            1,
            "1.0",
            ZonedDateTime.now(),
            DOMAIN_HUMAN,
            TYPE_CREATED,
            "key",
            ByteArray(0)
        )
        val result = mock<ListenableFuture<SendResult<String, String>>>()
        val sendResult = mock<SendResult<String, String>>()

        val captorCallback = captorForClass<ListenableFutureCallback<in SendResult<String, String>>>()

        Mockito.`when`(kafkaTemplate.send(eq(event.domain), eq(event.key), any())).thenReturn(result)
        Mockito.`when`(eventRepositorySpring.save(event)).thenReturn(event)
        Mockito.doNothing().`when`(result).addCallback(captorCallback.capture())
        Mockito.`when`(eventRepositorySpring.findById(event.id)).thenReturn(Optional.of(event))

        eventServiceKafka.fireEvent(event)

        captorCallback.value.onFailure(RuntimeException("Exception for test purposes"))

        verifyOnce(kafkaTemplate).send(eq(event.domain), eq(event.key), any())
        verifyOnce(result).addCallback(any())
        Mockito.verify(eventRepositorySpring, Mockito.times(2)).save(event)

        Assert.assertEquals("sendState not set", SendState.FAILED, event.sendState)
        Assert.assertEquals("tries not updated", 1, event.sendTries)
        Assert.assertNotNull("sendTime not updated", event.lastSendTime)
    }
}