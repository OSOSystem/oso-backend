package de.ososystem.device.domain.services.impl

import de.ososystem.device.domain.entities.Human
import de.ososystem.device.domain.repositories.HumanRepository
import de.ososystem.device.domain.services.HumanService
import org.slf4j.LoggerFactory
import java.util.*

class HumanServiceImpl(
        val humanRepository: HumanRepository
): HumanService {
    override fun createHuman(key: String, data: Human): Human {
        LOGGER.debug("creating Human<{}> with data<{}>", key, data)

        if(humanRepository.humanExistsById(UUID.fromString(key))) {
            LOGGER.info("Human already exists, data will be updated")
        }

        return humanRepository.saveHuman(data)
    }

    override fun changeHuman(key: String, data: Human): Human {
        LOGGER.debug("data of human<{}> changing to <{}>", key, data)

        if(!humanRepository.humanExistsById(UUID.fromString(key))) {
            LOGGER.info("Human does not exist yet, it will be created")
        }

        return humanRepository.saveHuman(data)
    }

    override fun deleteHuman(key: String) {
        LOGGER.debug("deleting human<{}>", key)

        if(!humanRepository.humanExistsById(UUID.fromString(key))) {
            LOGGER.info("Human does not exist, abort")
            return
        }

        humanRepository.deleteHumanById(UUID.fromString(key))
    }

    companion object {
        private val LOGGER = LoggerFactory.getLogger(HumanServiceImpl::class.java)
    }
}