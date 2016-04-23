package com.attilapalfi.commons.utlis

import com.attilapalfi.commons.UDP_BUFFER_SIZE
import com.attilapalfi.commons.messages.ClientTcpMessage
import com.attilapalfi.commons.messages.ServerTcpMessage
import com.attilapalfi.commons.messages.UdpDiscoveryBroadcast
import com.attilapalfi.commons.messages.UdpSensorData
import com.esotericsoftware.kryo.Kryo
import com.esotericsoftware.kryo.io.Input
import com.esotericsoftware.kryo.io.Output
import org.apache.commons.lang3.SerializationUtils

/**
 * Created by palfi on 2016-01-15.
 */
class ServerMessageConverter {

    private val kryo = Kryo()

    fun messageToByteArray(message: ServerTcpMessage): ByteArray
            = convertToByteArray(message)

    fun messageToByteArray(message: UdpDiscoveryBroadcast): ByteArray
            = convertToByteArray(message)

    private fun convertToByteArray(message: Any): ByteArray {
        val buffer = ByteArray(UDP_BUFFER_SIZE)
        val output = Output(buffer)
        output.use {
            kryo.writeObject(output, message)
        }
        return buffer.copyOf(output.position())
    }

    fun byteArrayToTcpMessage(byteArray: ByteArray): ServerTcpMessage {
        val input = Input(byteArray)
        input.use {
            return kryo.readObject(input, ServerTcpMessage::class.java)
        }
    }

    fun byteArrayToDiscoveryBroadcast(byteArray: ByteArray): UdpDiscoveryBroadcast {
        val input = Input(byteArray)
        input.use {
            return kryo.readObject(input, UdpDiscoveryBroadcast::class.java)
        }
    }
}