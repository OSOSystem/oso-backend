package org.oso.core.services.impl

import org.hamcrest.core.Is.`is`
import org.junit.Assert
import org.junit.Before
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.oso.any
import org.springframework.mail.MailSendException
import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class)
class DefaultMailServiceTest {

    @InjectMocks
    lateinit var defaultMailService: DefaultMailService

    @Mock
    lateinit var javaMailSender: JavaMailSender

    private val from = "oso@test.de"
    private val participants = listOf("a@gmail.com", "b@gmail.com", "c@gmail.com")
    private val subject = "Test"
    private val text = "This is a test"

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        defaultMailService = DefaultMailService(javaMailSender)
    }

    @Test
    fun testFailedDelivery() {
        Mockito
            .doNothing()
            .doThrow(MailSendException("Failed to deliver mail"))
            .doThrow(MailSendException("Failed to deliver mail"))
            .`when`(javaMailSender).send(any<SimpleMailMessage>())

        defaultMailService.send(from, participants, subject, text).let {
            Assert.assertThat(it, `is`(participants.drop(1)))
        }

        Mockito.verify(javaMailSender, Mockito.times(3))
    }
}