package org.oso.core.services.impl

import org.oso.core.entities.Coordinates
import org.oso.core.services.DeviceService
import org.oso.core.services.ReachfarService
import org.oso.devices.reachfar.ReachFarMsgType
import org.oso.devices.reachfar.ReachfarMsg
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.*

@Service
class DefaultReachfarService(
    private val deviceService: DeviceService
) : ReachfarService {

    private val mapDeviceToState = mutableMapOf<String, TrackerState>()

    override fun handleNewMsg(msg: ReachfarMsg) {
        val device = deviceService.createIfMissing(
            name = msg.deviceName,
            description = "Reachfartracker maker<${msg.maker}> id${msg.id}",
            deviceType = deviceService.findTypeByName(DeviceService.DEVICE_TYPE_REACHFAR)
        )

        LOGGER.debug("reaceived msg$msg> from Reachfar<${device.name}>")

        // TODO store state data in repository somewhere
        if (!mapDeviceToState.containsKey(device.name)) {
            val state = TrackerState()
            mapDeviceToState[device.name] = state
            state.onStateChanged = { index, newState ->
                stateChanged(device.name, index, newState)
            }
        }

        val type = ReachFarMsgType.typeOf(msg)

        if (type == null) {
            LOGGER.warn("Unknown messageType")
            return
        }

        val data = type.parse(msg)

        stateParams.filter {
            data.containsKey(it)
        }.forEach {
            mapDeviceToState[device.name]!!.newState(data[it]!!)
        }

        if (data.containsKey(KEY_EFFECTIVE_MARK) && data[KEY_EFFECTIVE_MARK] == EFFECTIVE_MARK_VALID) {
            if (data.containsKey(KEY_LATITUDE) && data.containsKey(KEY_LATITUDE_MARK)
                && data.containsKey(KEY_LONGITUDE) && data.containsKey(KEY_LONGITUDE_MARK)) {
                val coordLatitude = parseCoordinate(data[KEY_LATITUDE]!!)
                val coordLongtude = parseCoordinate(data[KEY_LONGITUDE]!!)

                val coordinates = Coordinates(
                    // northern latitude is positive
                    coordLatitude.toBigDecimal(data[KEY_LATITUDE_MARK] == LATITUDE_MARK_NORTH),
                    // eastern longitude is positive
                    coordLongtude.toBigDecimal(data[KEY_LONGITUDE_MARK] == LONGITUDE_MARK_EAST)
                )

                deviceService.saveCoordinates(device, coordinates, LocalDateTime.now())
            }
        }
    }

    private fun stateChanged(deviceName: String, index: Int, state: Boolean) {
        if (TrackerState.isReserved(index)) {
            return
        }
        when (index) {
            idx(4, 1) -> { // alarm
                // TODO get current alarmstate of device
                val alarm = false

                val newAlarm = state.xor(TrackerState.isNegativeLogic(index))

                if(newAlarm != alarm) {
                    if (alarm) {
                        // TODO fire alarm
                    } else {
                        // TODO resolve alarm
                    }
                }
            }
            idx(1, 4) -> { // churger unplugged

            }
            idx(2, 1) -> { // battery low

            }
        }
    }

    private fun parseCoordinate(coordinate: String): Coordinate {
        val iDot = coordinate.indexOf('.')
        val sDegree = coordinate.substring(0, iDot - 2)
        val sMinutes = coordinate.substring(iDot - 2)

        return Coordinate(sDegree.toInt(), sMinutes.toDouble())
    }

    companion object {
        private val LOGGER = LoggerFactory.getLogger(DefaultReachfarService::class.java)
        private val stateParams = listOf("tracker_state", "vehcle_state")

        private const val KEY_EFFECTIVE_MARK = "S"
        private const val EFFECTIVE_MARK_VALID = "A"
        private const val EFFECTIVE_MARK_INVALID = "V"
        private const val KEY_LATITUDE = "latitude"
        private const val KEY_LATITUDE_MARK = "D"
        private const val LATITUDE_MARK_NORTH = "N"
        private const val LATITUDE_MARK_SOUTH = "S"
        private const val KEY_LONGITUDE = "longitude"
        private const val KEY_LONGITUDE_MARK = "G"
        private const val LONGITUDE_MARK_EAST = "E"
        private const val LONGITUDE_MARK_WEST = "W"
    }
}

internal val ReachfarMsg.deviceName: String
    get() {
        return "$maker/$id"
    }

private class TrackerState(
        state: String = "FFFFFBFD"
){
    private var set: BitSet

    var onStateChanged: ((Int, Boolean) -> Unit)? = null

    init {
        set = BitSet(SIZE)
        newState(state)
    }

    fun newState(state: String) {
        val s = BitSet.valueOf(longArrayOf(state.toLong(16)))
        // ignore unknown bits
        if (s.length() > SIZE) {
            s.clear(SIZE, s.length())
        }
        val changes = set
        set = s
        changes.xor(s)

        changes.stream().forEach {
            onStateChanged?.invoke(it, set[it])
        }
    }

    companion object {
        private const val SIZE = 4 * 8

        fun isReserved(index: Int): Boolean {
            return when(index) {
                idx(1, 1),
                idx(1, 2),
                idx(1, 3),
                idx(1, 5),
                idx(1, 6),
                idx(1, 7),
                idx(2, 1),
                idx(2, 3),
                idx(2, 4),
                idx(2, 5),
                idx(2, 6),
                idx(2, 7),
                idx(3, 1),
                idx(3, 2),
                idx(3, 3),
                idx(3, 4),
                idx(3, 5),
                idx(3, 6),
                idx(3, 7),
                idx(4, 3),
                idx(4, 5),
                idx(4, 6) -> true
                else -> false
            }
        }

        fun isNegativeLogic(index: Int): Boolean {
            return when(index) {
                idx(1, 1),
                idx(4, 0),
                idx(4, 1),
                idx(4, 2),
                idx(4, 4),
                idx(4, 7) -> true
                else -> false
            }
        }
    }
}

private data class Coordinate(val degree: Int, val minutes: Double) {
    fun toBigDecimal(positive: Boolean): BigDecimal {
        val value = degree + (minutes / 60)
        return BigDecimal(if (positive) value else -value)
    }
}

private inline fun idx(byte: Int, bit: Int) = ((byte - 1) * 8) + (7 - bit)