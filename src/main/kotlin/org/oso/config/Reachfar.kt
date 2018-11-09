package org.oso.config

import org.oso.AppConfig
import org.oso.core.services.ReachfarService
import org.oso.devices.reachfar.ReachfarMsg
import org.oso.devices.reachfar.ReachfarMsgBuffer
import org.slf4j.LoggerFactory
import org.springframework.integration.annotation.ServiceActivator
import org.springframework.integration.ip.IpHeaders
import org.springframework.messaging.handler.annotation.Header
import org.springframework.stereotype.Component

@Component(AppConfig.REACHFAR_BEAN)
class Reachfar(
    private val reachfarService: ReachfarService
) {
    private val mapConnToData = mutableMapOf<String, ReachfarData>()

    @ServiceActivator
    fun handle(payload: String, @Header(IpHeaders.CONNECTION_ID) connId: String) {
        logger.debug("Received<$payload> from connection<$connId>")

        val data = with(mapConnToData) {
            if (!containsKey(connId)) {
                this[connId] = ReachfarData()
                logger.debug("New reachfarconnection<$connId>")
            }
            this[connId]!!
        }

        data.buffer.add(payload)

        while (data.buffer.hasMsg) {
            val msg = data.buffer.extractMsg()
            val msgid = msg.reachfarid

            if (data.id == null) {
                logger.debug("Identified Reachfar<$msgid> on connection<$connId>")

                trackerEntry(msgid)?.let {
                    logger.debug("Reachfar is already known on connection<${it.key}>")
                    logger.debug("Transfering Reachfar<$msgid> from connection<${it.key}> to connection<$connId>")
                    mapConnToData.remove(it.key)
                }

                data.id = msgid
            }

            if (data.id != msgid) {
                logger.error("connection<$connId> is for reachfar<${data.id} but reeived msg is supposed to be of<$msgid>")
                // TODO what to do now?
            }

            reachfarService.handleNewMsg(msg)
        }
    }

    private fun trackerEntry(id: ReachfarId) = mapConnToData.entries.find { it.value.id == id }

    companion object {
        private val logger = LoggerFactory.getLogger(Reachfar::class.java)
    }
}

private data class ReachfarId(val maker: String, val id: String)

private val ReachfarMsg.reachfarid: ReachfarId
    get() = ReachfarId(
        maker = maker,
        id = id
    )

private data class ReachfarData(
    val buffer: ReachfarMsgBuffer = ReachfarMsgBuffer(),
    var id: ReachfarId? = null
)