package de.ososystem.device.domain.services

interface TcpCommService {
    fun newConnection(connId: String)
    fun receiveData(connId: String, payload: ByteArray)
    fun connectionClosed(connId: String)

    fun sendData(connId: String, payload: ByteArray)
}