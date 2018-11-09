package org.oso.devices.reachfar

import java.util.*

class ReachfarMsg {
    val maker: String
    val id: String
    val type: String
    val params: List<String>

    constructor(maker: String, id: String, type: String, params: List<String>) {
        this.maker = maker
        this.id = id
        this.type = type
        this.params = params
    }

    constructor(msg: CharSequence) {
        if(msg.first() != startMark
            || msg.last() != endMark
            || msg.count { it == startMark || it == endMark } != 2) {
            throw IllegalArgumentException("Invalid message-string<$msg> - a message must always start with<${startMark}> and end with<${endMark}> and do not contains those any time more")
        }

        val parts = msg.subSequence(1, msg.length - 1).split("$delimiter")

        if(parts.size < 3) {
            throw IllegalArgumentException("Invalid message-string<$msg> - a message always at least needs information about maker, id and type")
        }

        maker = parts[0]
        id = parts[1]
        type = parts[2]
        params = if(parts.size == 3) Collections.emptyList() else parts.subList(3, parts.size)
    }

    companion object {
        const val startMark = '*'
        const val endMark = '#'
        const val delimiter = ','

        fun isReachfarMsg(msg: CharSequence): Boolean {
            val noDelimiter = "[^${delimiter}]"
            val aDelimiter = "[${delimiter}]"
            val start = "[${startMark}]"
            val end = "[${endMark}]"

            val useExplicitRangeChecks = false

            return !msg.isEmpty()
                && msg.matches(
                Regex(
            ".*" // any characters at the beginning
                    + "$start" // start sign
                    + noDelimiter + (if(useExplicitRangeChecks) "{2}" else "+") // marker
                    + "$aDelimiter$noDelimiter" + (if(useExplicitRangeChecks) "{10]" else "+") // id
                    + "$aDelimiter$noDelimiter+" // type
                    + "($aDelimiter$noDelimiter*)*" // any further parameters
                    + "[$end]" // end sign
                    + ".*")) // any characters at the end
        }

        fun extractMsg(msg: CharSequence): CharSequence {
            val start = msg.indexOf(startMark)
            val end = msg.indexOf(endMark, start)

            return when {
                start == -1 || end == -1 || start > end -> ""
                else -> msg.subSequence(start, end + 1)
            }
        }
    }
}