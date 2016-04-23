package com.attilapalfi.commons.utlis

import com.attilapalfi.commons.UDP_BUFFER_SIZE
import com.attilapalfi.commons.messages.ClientTcpMessage
import com.attilapalfi.commons.messages.UdpSensorData
import com.esotericsoftware.kryo.Kryo
import com.esotericsoftware.kryo.io.Input
import com.esotericsoftware.kryo.io.Output

/**
 * Created by palfi on 2016-02-13.
 */
class ClientMessageConverter {

    private val kryo = Kryo()

    fun messageToByteArray(message: ClientTcpMessage): ByteArray
            = convertToByteArray(message)

    fun messageToByteArray(message: UdpSensorData): ByteArray
            = convertToByteArray(message)

    private fun convertToByteArray(message: Any): ByteArray {
        val buffer = ByteArray(UDP_BUFFER_SIZE)
        val output = Output(buffer)
        output.use {
            kryo.writeObject(output, message)
        }
        return buffer.copyOf(output.position())
    }

    fun byteArrayToTcpMessage(byteArray: ByteArray): ClientTcpMessage {
        val input = Input(byteArray)
        input.use {
            return kryo.readObject(input, ClientTcpMessage::class.java)
        }
    }

    fun byteArrayToUdpSensorData(byteArray: ByteArray): UdpSensorData {
        val input = Input(byteArray)
        input.use {
            return kryo.readObject(input, UdpSensorData::class.java)
        }
    }
}
