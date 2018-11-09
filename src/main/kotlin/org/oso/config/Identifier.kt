package org.oso.config

import org.oso.AppConfig
import org.oso.devices.reachfar.ReachfarMsg
import org.slf4j.LoggerFactory
import org.springframework.integration.annotation.Router
import org.springframework.integration.annotation.ServiceActivator
import org.springframework.integration.annotation.Transformer
import org.springframework.integration.ip.IpHeaders
import org.springframework.messaging.handler.annotation.Header
import org.springframework.stereotype.Component
import java.nio.charset.StandardCharsets

@Component(AppConfig.IDENTIFY_BEAN)
class Identifier {
    val map = mutableMapOf<String, ByteArray>()
    val routeMap = mutableMapOf<String, String>()

    @Transformer
    fun concat(payload: ByteArray, @Header(IpHeaders.CONNECTION_ID) connId: String): ByteArray {
        println("received<${String(payload)}> from connection<$connId>")
        return map.getOrDefault(connId, ByteArray(0)) + payload
    }

    @Router
    fun route(@Header(IpHeaders.CONNECTION_ID) connId: String): String {
        LOGGER.debug("NEW ROUTING FOR $connId")
        return routeMap[connId] ?: AppConfig.UNKNOWN_CHANNEL
    }

    @Router
    fun identify(payload: ByteArray, @Header(IpHeaders.CONNECTION_ID) connId: String): String {
        val s = String(payload, 0, payload.size, StandardCharsets.US_ASCII)

        when {
            ReachfarMsg.isReachfarMsg(s) -> routeMap[connId] = AppConfig.REACHFAR_CHANNEL
        }

        if(routeMap.containsKey(connId)) {
            map.remove(connId)
        }

        return routeMap[connId] ?: AppConfig.REAL_UNKNOWN_CHANNEL
    }

    @ServiceActivator
    fun savePayload(payload: ByteArray, @Header(IpHeaders.CONNECTION_ID) connId: String) {
        map[connId] = payload
    }

    companion object {
        private val LOGGER = LoggerFactory.getLogger(Identifier::class.java)
    }
}