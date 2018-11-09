package org.oso.core.services.impl

import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.oso.Application
import org.oso.core.entities.Coordinates
import org.oso.core.services.external.impl.OpenStreetMapsService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit4.SpringRunner
import java.math.BigDecimal

@SpringBootTest(classes=[Application::class])
@RunWith(SpringRunner::class)
@ActiveProfiles("test")
class OpenStreetMapsServiceTest {

    @Autowired
    lateinit var openStreetMapsService: OpenStreetMapsService

    @Test
    fun testResolveAddress() {
        val actual = openStreetMapsService.resolve(Coordinates(BigDecimal(52.5487429714954), BigDecimal(-1.81602098644987)))
        val expected = "137, Pilkington Avenue, Sutton Coldfield, Birmingham, West Midlands Combined Authority, West Midlands, England, B72 1LH, UK"
        Assert.assertEquals(expected, actual)
    }
}