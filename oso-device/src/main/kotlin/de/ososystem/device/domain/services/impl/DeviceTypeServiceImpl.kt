package de.ososystem.device.domain.services.impl

import de.ososystem.device.domain.entities.DeviceType
import de.ososystem.device.domain.factories.DeviceTypeCommStraFactory
import de.ososystem.device.domain.repositories.DeviceTypeRepository
import de.ososystem.device.domain.services.DeviceService
import de.ososystem.device.domain.services.DeviceTypeService

class DeviceTypeServiceImpl(
        private val deviceTypeRepository: DeviceTypeRepository,
        private val deviceTypeCommStraFactory: DeviceTypeCommStraFactory,
        private val deviceService: DeviceService
): DeviceTypeService {
    override fun getAllDeviceTypes(): Iterable<DeviceType> {
        return deviceTypeRepository.getAllDeviceTypes()
    }

    override fun getDeviceType(name: String): DeviceType? {
        return deviceTypeRepository.findDeviceTypeByName(name)
    }

    override fun getPossibleDeviceTypes(payload: ByteArray): Set<DeviceType> {
        return deviceTypeRepository.getAllDeviceTypes()
                .map { it to deviceTypeCommStraFactory.getCommStraFor(it) }
                .filter {
                    it.second?.isValidMsg(payload) ?: false
                }
                .map { it.first }
                .toSet()
    }

    override fun receiveData(deviceType: DeviceType, connId: String, payload: ByteArray) {
        val device = deviceService.findDeviceByConnId(connId)

        deviceTypeCommStraFactory.getCommStraFor(deviceType)?.receiveData(deviceType, device, connId, payload) ?: error("deviceType<$deviceType> has no communication strategy")
    }
}