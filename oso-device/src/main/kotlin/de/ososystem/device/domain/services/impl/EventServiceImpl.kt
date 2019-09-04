package de.ososystem.device.domain.services.impl

import de.ososystem.device.domain.events.*
import de.ososystem.device.domain.mapper.HumanMapper
import de.ososystem.device.domain.services.EventService
import de.ososystem.device.domain.services.HumanService
import org.slf4j.LoggerFactory

class EventServiceImpl(
        val humanService: HumanService,
        val humanMapper: HumanMapper
): EventService {
    override fun processHumanEvent(event: HumanEvent) {
        LOGGER.debug("Processing human event id<{}> with key<{}> and data<{}>", event.id, event.key, event.payload)

        when (event.type) {
            TYPE_CREATED ->
                humanService.createHuman(event.key, getHumanMapperForVersion(event.version).read(event.payload))

            TYPE_DELETED ->
                humanService.deleteHuman(event.key)

            TYPE_CHANGED ->
                humanService.changeHuman(event.key, getHumanMapperForVersion(event.version).read(event.payload))

            else ->
                LOGGER.info("Unknown Eventtype<{}>, ignoring message", event.type)
        }
    }

    fun getHumanMapperForVersion(version: Version): HumanMapper {
        // TODO how to do this correctly?
        return humanMapper
    }

    companion object {
        private val LOGGER = LoggerFactory.getLogger(EventServiceImpl::class.java)
    }
}