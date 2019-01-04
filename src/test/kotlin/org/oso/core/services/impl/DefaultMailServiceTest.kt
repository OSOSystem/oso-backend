package org.oso.core.services.impl

import org.junit.Before
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
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

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        defaultMailService = DefaultMailService(javaMailSender)
    }

    @Test
    fun testSend() {
        val from = "oso@test.de"
        val participants = listOf("a@gmail.com", "b@gmail.com", "c@gmail.com")
        val subject = "Test"
        val text = "This is a test"
        defaultMailService.send(from, participants, subject, text)
    }
}