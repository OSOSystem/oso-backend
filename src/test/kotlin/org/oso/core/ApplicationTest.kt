package org.oso.core


import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.oso.Application
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit.jupiter.SpringExtension

@SpringBootTest(classes=[Application::class])
@ExtendWith(SpringExtension::class)
@ActiveProfiles("test")
class ApplicationTest {
    /**
     * Check if the application can start without any hiccups.
     */
    @Test
    fun testApplicationStart() {
    }
}