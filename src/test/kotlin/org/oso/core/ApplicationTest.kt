package org.oso.core

import org.junit.Test
import org.junit.runner.RunWith
import org.oso.Application
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit4.SpringRunner

@SpringBootTest(classes=[Application::class])
@RunWith(SpringRunner::class)
@ActiveProfiles("test")
class ApplicationTest {
    /**
     * Check if the application can start without any hiccups.
     */
    @Test
    fun testApplicationStart() {
    }
}