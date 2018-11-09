package org.oso.core.services

import org.oso.devices.reachfar.ReachfarMsg

interface ReachfarService {
    fun handleNewMsg(msg: ReachfarMsg)
}