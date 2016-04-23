package com.attilapalfi.commons.messages

import java.io.Serializable
import java.util.*

/**
 * Created by palfi on 2016-01-15.
 */
data class UdpSensorData(val pressedButtons: Set<PressedButton> = HashSet(),
                         val x: Float = 0f,
                         val y: Float = 0f) : Serializable

enum class PressedButton {
    A, B, X, Y, None
}

data class ClientTcpMessage(val messageType: ClientTcpMessageType = ClientTcpMessageType.REGISTRATION,
                            val pressedButton: PressedButton = PressedButton.None,
                            val deviceName: String? = null) : Serializable

enum class ClientTcpMessageType {
    REGISTRATION,
    BUTTON_PRESS,
    DISCONNECTION
}