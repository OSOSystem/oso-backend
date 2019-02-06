package org.oso.core.services.impl

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.*
import org.oso.any
import org.oso.argThat
import org.oso.core.entities.Coordinate
import org.oso.core.entities.Device
import org.oso.core.entities.DeviceType
import org.oso.core.services.DeviceService
import org.oso.devices.reachfar.ReachfarMsg
import org.oso.eq
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class)
class DefaultReachfarServiceTest {

    @InjectMocks
    lateinit var defaultReachfarService: DefaultReachfarService

    @Mock
    lateinit var deviceService: DeviceService

    @Test
    fun testHandleNewMsgCreatesDeviceIfMissing() {
        val msg = ReachfarMsg(
            id = "1234567890",
            maker = "HQ",
            type = "type",
            params = listOf()
        )

        val deviceType = DeviceType(
                DeviceService.DEVICE_TYPE_REACHFAR
        )

        val device = Device(
            id = "Reachfar",
            deviceType = deviceType
        )

        Mockito.`when`(deviceService.findTypeByName(ArgumentMatchers.anyString())).thenThrow(IllegalArgumentException())
        Mockito.doReturn(deviceType).`when`(deviceService).findTypeByName(DeviceService.DEVICE_TYPE_REACHFAR)
        Mockito.`when`(deviceService.createIfMissing(ArgumentMatchers.anyString(), ArgumentMatchers.anyString(), any())).thenThrow(IllegalArgumentException())
        Mockito.doReturn(device).`when`(deviceService).createIfMissing(eq(msg.deviceName), ArgumentMatchers.anyString(), eq(deviceType))

        defaultReachfarService.handleNewMsg(msg)

        Mockito.verify(deviceService, Mockito.atLeast(1)).createIfMissing(eq(msg.deviceName), ArgumentMatchers.anyString(), eq(deviceType))
        Mockito.verify(deviceService, Mockito.never()).saveCoordinates(any(), any(), any())
    }

    @Test
    fun testHandleNewMsgWithCoordinates() {
        val coordinates = Coordinate(
            latitude = 12.57613.toBigDecimal(),
            longitude = 123.761315.toBigDecimal()
        )

        val msg = ReachfarMsg(
            "*" +
                "HQ," +
                "1234567890," +
                "V1," +
                "121314," +
                "A," +
                "1234.5678," +
                "N," +
                "12345.6789," +
                "E," +
                "0," +
                "0," +
                "010170," +
                "FFFFFBFD" +
                "#"
        )

        val deviceType = DeviceType(
                DeviceService.DEVICE_TYPE_REACHFAR
        )

        val device = Device(
                id = "Reachfar",
                deviceType = deviceType
        )

        Mockito.`when`(deviceService.findTypeByName(ArgumentMatchers.anyString())).thenThrow(IllegalArgumentException())
        Mockito.doReturn(deviceType).`when`(deviceService).findTypeByName(DeviceService.DEVICE_TYPE_REACHFAR)
        Mockito.`when`(deviceService.createIfMissing(ArgumentMatchers.anyString(), ArgumentMatchers.anyString(), any())).thenThrow(IllegalArgumentException())
        Mockito.doReturn(device).`when`(deviceService).createIfMissing(eq(msg.deviceName), ArgumentMatchers.anyString(), eq(deviceType))
        //Mockito.`when`(deviceService.saveCoordinates(any(), any(), any())).thenThrow(IllegalArgumentException())
        Mockito.doNothing().`when`(deviceService).saveCoordinates(eq(device), argThat(matches(coordinates)), any())

        defaultReachfarService.handleNewMsg(msg)

        Mockito.verify(deviceService, Mockito.times(1)).saveCoordinates(eq(device), argThat(matches(coordinates)), any())
    }

    private fun matches(coordinates: Coordinate) =
            ArgumentMatcher<Coordinate> { argument ->
                println("argument: $argument")
                println("coordinate $coordinates")
                (coordinates.latitude.max(argument.latitude) - coordinates.latitude.min(argument.latitude) < 1.toBigDecimal()
                && coordinates.longitude.max(argument.longitude) - coordinates.longitude.min(argument.longitude) < 1.toBigDecimal())
            }
}