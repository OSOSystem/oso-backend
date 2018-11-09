package org.oso.devices.reachfar

import kotlin.math.max
import kotlin.math.min

abstract class ReachFarMsgType(
        val typeString: String
) {
    abstract fun parse(msg: ReachfarMsg): Map<String, String>

    // TODO create method to create a message out of the type

    private open class Simple(
            typeString: String,
            val fieldNames: List<String>
    ) : ReachFarMsgType(typeString) {
        override fun parse(msg: ReachfarMsg): Map<String, String> {
            return parse(msg, fieldNames)
        }
    }

    protected fun parse(msg: ReachfarMsg, fieldNames: List<String>): Map<String, String> {
        return msg.params.mapIndexed { index, s -> fieldNames[index] to s }.toMap()
    }

    companion object {
        private val types = mutableMapOf<String, ReachFarMsgType>()

        fun register(type: ReachFarMsgType): ReachFarMsgType {
            types[type.typeString] = type
            return type
        }

        fun typeOf(msg: ReachfarMsg): ReachFarMsgType? {
            return types[msg.type]
        }

        // TODO create constants for missing message types
        /** general information */
        val V1: ReachFarMsgType = Simple("V1", "HHMMSS,S,latitude,D,longitude,G,speed,direction,DDMMYY,tracker_status".split(","))
        /** answer to server-cmds */
        val V4: ReachFarMsgType = Simple("V4", "CMD,hhmmss,HHMMSS,S,latitude,D,longitude,G,speed,direction,DDMMYY,tracker_status".split(","))
        /** chinese address instruction - same as V1 ??? -> Reply I1 */
        val VI1: ReachFarMsgType = Simple("VI1", "HHMMSS,S,latitude,D,longitude,G,speed,direction,DDMMYY,vehicle_status".split(","))
        /** address with other language ??? -> Reply I3 */
        val V8: ReachFarMsgType = Simple("V8", "LAG,HHMMSS,S,latitude,D,longitude,G,speed,direction,DDMMYY,vehic le_status".split(","))
        /** address with cell id code ??? -> Reply I3 */
        val VI2: ReachFarMsgType = Simple("VI2", "MCC,MNC,LAC,CID,LAC2,CID2,2,00,cn".split(","))
        /** heartbeat */
        val LINK: ReachFarMsgType = Simple("LINK", "HHMMSS,GSM,GPS,BAT,STEP,TURNOVER,DDMMYY,tracker_status".split(","))

        private const val FIELD_NUM = "NUM"
        private val preFieldNames = "HHMMSS,MCC,MNC,TA,$FIELD_NUM".split(",")
        private val postFieldNames = "DDMMYY,vehicle_status".split(",")
        private val variableFieldNames = "RXLEV,LAC,CID".split(",")
        private val IDX_NUM = preFieldNames.indexOf(FIELD_NUM)

        /** multi based station entities */
        val NBR: ReachFarMsgType = object: ReachFarMsgType("NBR") {
            override fun parse(msg: ReachfarMsg): Map<String, String> {
                val count = max(msg.params[IDX_NUM].toInt(), 1)

                val list = mutableListOf<String>()
                list.addAll(preFieldNames)
                repeat(count) { i ->
                    variableFieldNames.forEach{
                        list.add(it + i)
                    }
                }
                list.addAll(postFieldNames)

                return parse(msg, list)
            }
        }

        init {
            register(V1)
            register(V4)
            register(VI1)
            register(V8)
            register(VI2)
            register(LINK)
            register(NBR)
        }
    }
}