package org.oso.core.services.impl

import org.junit.Assert
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.oso.Application
import org.oso.core.entities.Coordinate
import org.oso.core.services.external.impl.OpenStreetMapsService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.math.BigDecimal

@SpringBootTest(classes=[Application::class])
@ExtendWith(SpringExtension::class)
@ActiveProfiles("test")
class OpenStreetMapsServiceTest {

    @Autowired
    lateinit var openStreetMapsService: OpenStreetMapsService

    @Test
    fun testResolveAddress() {
        val actual = openStreetMapsService.resolve(Coordinate(BigDecimal(52.5487429714954), BigDecimal(-1.81602098644987)))
        val expected = "137, Pilkington Avenue, Sutton Coldfield, Birmingham, West Midlands Combined Authority, West Midlands, England, B72 1LH, UK"
        Assert.assertEquals(expected, actual)
    }
}