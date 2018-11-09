package org.oso.devices.reachfar

class ReachfarMsgBuffer : Iterable<ReachfarMsg> {
    private val buffer = StringBuilder()

    val hasMsg: Boolean
        get() { return ReachfarMsg.isReachfarMsg(buffer) }

    fun add(input: CharSequence) {
        buffer.append(input)
    }

    fun add(buffer: ReachfarMsgBuffer) {
        this.buffer.append(buffer.buffer)
    }

    operator fun plusAssign(input: String) = add(input)
    operator fun plusAssign(input: ReachfarMsgBuffer) = add(input)

    fun extractMsg(): ReachfarMsg {
        val msg = ReachfarMsg.extractMsg(buffer)
        if(msg.isEmpty()) {
            throw IllegalStateException("No Msg currently contained in buffer")
        }
        buffer.removeRange(0,
                buffer.indexOf(msg.toString()) + msg.length)

        return ReachfarMsg(msg)
    }

    fun clear(): Unit = buffer.setLength(0)

    override fun iterator(): Iterator<ReachfarMsg> {
        return ReachfarMsgBufferIterator(this)
    }
}

private class ReachfarMsgBufferIterator(
    private val buffer: ReachfarMsgBuffer
) : Iterator<ReachfarMsg> {

    override fun hasNext(): Boolean {
        return buffer.hasMsg
    }

    override fun next(): ReachfarMsg {
        return buffer.extractMsg()
    }
}