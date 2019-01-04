package org.oso

import org.oso.core.entities.Device
import org.oso.core.entities.DeviceType
import org.oso.core.repositories.DeviceRepository
import org.oso.core.repositories.DeviceTypeRepository
import org.oso.core.services.DeviceService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.stereotype.Component

@Component
class AppInitialData
    @Autowired
    constructor(
        private val deviceRepository: DeviceRepository,
        private val deviceTypeRepository: DeviceTypeRepository
    ) : ApplicationRunner {

    override fun run(args: ApplicationArguments?) {
        LOGGER.info("Creating initial data...")

        // ensure that the objects are only initialized once
        if (deviceRepository.count() > 0 && deviceTypeRepository.count() > 0) {
            return
        }

        val typeReachfar = DeviceType(DeviceService.DEVICE_TYPE_REACHFAR)
        val typeNanoTracker = DeviceType(DeviceService.DEVICE_TYPE_NANO_TRACKER)
        val typeApp = DeviceType(DeviceService.DEVICE_TYPE_APP)

        deviceTypeRepository.save(typeReachfar)
        deviceTypeRepository.save(typeNanoTracker)
        deviceTypeRepository.save(typeApp)

        val reachFarTracker = Device(
            deviceType = typeReachfar
        )
        reachFarTracker.id = "ReachFar Tracker"
        val nanoTracker = Device(
            deviceType = typeNanoTracker
        )
        nanoTracker.id = "Nano Tracker"

        deviceRepository.save(reachFarTracker)
        deviceRepository.save(nanoTracker)
    }

    companion object {
        val LOGGER: Logger = LoggerFactory.getLogger(AppInitialData::class.java)
    }
}