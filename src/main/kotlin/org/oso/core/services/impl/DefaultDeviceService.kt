package org.oso.core.services.impl

import org.oso.core.entities.Coordinate
import org.oso.core.entities.Device
import org.oso.core.entities.DeviceCoordinate
import org.oso.core.entities.DeviceType
import org.oso.core.repositories.DeviceCoordinatesRepository
import org.oso.core.repositories.DeviceRepository
import org.oso.core.repositories.DeviceTypeRepository
import org.oso.core.services.DeviceService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class DefaultDeviceService
    @Autowired constructor(
        private val deviceRepository: DeviceRepository,
        private val deviceTypeRepository: DeviceTypeRepository,
        private val deviceCoordinatesRepository: DeviceCoordinatesRepository
    ) : DeviceService {

    override fun findAll(): List<Device> {
        return deviceRepository.findAll().toList()
    }

    override fun createIfMissing(name: String, description: String, deviceType: DeviceType?): Device {
        var dev = deviceRepository.findById(name).get()
        if (dev == null) {
            dev = Device(
                deviceType = deviceType
            )
            dev.id = name
            deviceRepository.save(dev)
        }
        return dev
    }

    override fun findTypeByName(name: String): DeviceType? =
        deviceTypeRepository.findByName(name)

    override fun saveCoordinates(device: Device, coordinates: Coordinate, time: LocalDateTime) {
        val devCoords = DeviceCoordinate(
            device = device,
            time = time,
            coordinates = coordinates
        )

        deviceCoordinatesRepository.save(devCoords)

        device.deviceCoordinates.add(devCoords)
    }
}