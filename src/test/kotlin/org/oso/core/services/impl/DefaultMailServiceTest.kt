package org.oso.core.services.impl

import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class)
class DefaultMailServiceTest {
    lateinit var defaultMailService: DefaultMailService

    @Before
    fun setup() {
        defaultMailService = Mockito.mock(DefaultMailService::class.java)
    }

    @Test
    fun testSend() {
        val participants = listOf("a@gmail.com", "b@gmail.com", "c@gmail.com")
        val subject = "Test"
        val text = "This is a test"
        defaultMailService.send(participants, subject, text)
        Mockito.verify(defaultMailService, Mockito.times(1)).send(participants, subject, text)
    }
}