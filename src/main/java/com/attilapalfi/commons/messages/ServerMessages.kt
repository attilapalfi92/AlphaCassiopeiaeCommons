package com.attilapalfi.commons.messages

import java.io.Serializable

/**
 * Created by palfi on 2016-01-15.
 */
data class UdpDiscoveryBroadcast(val ports: List<Int> = arrayListOf()) : Serializable

class ServerTcpMessage(val messageType: ServerTcpMessageType
                       = ServerTcpMessageType.START_SENSOR_STREAM,
                       val effectMilliSeconds: Int = 0) : Serializable

enum class ServerTcpMessageType {
    START_SENSOR_STREAM,
    STOP_SENSOR_STREAM,
    VIBRATE,
    GLOW
}