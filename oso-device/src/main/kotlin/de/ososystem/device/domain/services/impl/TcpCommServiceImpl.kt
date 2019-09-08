package de.ososystem.device.domain.services.impl

import de.ososystem.device.domain.services.DeviceTypeService
import de.ososystem.device.domain.services.TcpCommService
import org.slf4j.LoggerFactory

class TcpCommServiceImpl(
        private val deviceTypeService: DeviceTypeService
) : TcpCommService {
    private val typeMap = mutableMapOf<String, String>()
    private val dataMap = mutableMapOf<String, ByteArray>()

    override fun newConnection(connId: String) {
        LOGGER.debug("New connection<$connId>")

        if (isConnKnown(connId)) {
            LOGGER.debug("connection already known - closing it")
            connectionClosed(connId)
        }

        dataMap[connId] = ByteArray(0)
    }

    override fun receiveData(connId: String, payload: ByteArray) {
        LOGGER.debug("received data<$payload> for connection<$connId>")
        if (typeMap.containsKey(connId)) {
            deviceTypeService.receiveData(
                    deviceTypeService.getDeviceType(typeMap[connId] ?: error("no devicetype for conn<$connId> in map")) ?: error("unknown devicetype in typeMap"),
                    connId,
                    payload
            )
        } else {
            val pl = dataMap.getOrDefault(connId, ByteArray(0)) + payload

            val deviceType = findDeviceType(pl)

            if (deviceType == null) {
                dataMap[connId] = pl
            } else {
                LOGGER.debug("found type<$deviceType>")

                typeMap[connId] = deviceType
                dataMap.remove(connId)

                deviceTypeService.receiveData(
                        deviceTypeService.getDeviceType(deviceType) ?: error("deviceType<$deviceType> found by service does not exist"),
                        connId,
                        payload
                )
            }
        }
    }

    private fun findDeviceType(payload: ByteArray): String? {
        LOGGER.debug("checking deviceType for payload<$payload>")

        val types = deviceTypeService.getPossibleDeviceTypes(payload)

        if (types.size == 1) {
            return types.first().name
        }
        return null
    }

    override fun connectionClosed(connId: String) {
        LOGGER.debug("connection<$connId> closed")
        if (!isConnKnown(connId)) {
            LOGGER.debug("connection was already unknown")
            return
        }

        dataMap.remove(connId)
        typeMap.remove(connId)
    }

    private fun isConnKnown(connId: String) = dataMap.containsKey(connId) || typeMap.containsKey(connId)

    override fun sendData(connId: String, payload: ByteArray) {
        LOGGER.debug("sending data<$payload> to connection<$connId>")
        if (!isConnKnown(connId)) {
            LOGGER.debug("connection unknown - abort")
            return
        }
        TODO("not implemented")
    }

    companion object {
        private val LOGGER = LoggerFactory.getLogger(TcpCommServiceImpl::class.java)
    }
}