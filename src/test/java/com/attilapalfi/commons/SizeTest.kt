package com.attilapalfi.commons

import com.attilapalfi.commons.messages.ClientTcpMessage
import com.attilapalfi.commons.messages.ClientTcpMessageType.REGISTRATION
import com.attilapalfi.commons.messages.PressedButton.*
import com.attilapalfi.commons.messages.UdpSensorData
import com.esotericsoftware.kryo.Kryo
import com.esotericsoftware.kryo.io.Input
import com.esotericsoftware.kryo.io.Output
import org.junit.Test

/**
 * Created by palfi on 2016-04-23.
 */
class SizeTest {
    private val kryo = Kryo()

    private val udpSensorData = UdpSensorData(hashSetOf(A, B, X), 1.12345f, 5.21421f)
    private val tcpClientMessage = ClientTcpMessage(REGISTRATION, A, "almaaa")

    @Test
    fun test() {
        var buffer = ByteArray(300)
        var output = Output(buffer)
        kryo.writeObject(output, udpSensorData)
        val sensorResult = kryo.readObject(Input(buffer), UdpSensorData::class.java)
        println(output.position())
        println(sensorResult)

        buffer = ByteArray(300)
        output = Output(buffer)
        kryo.writeObject(output, tcpClientMessage)
        val tcpMessageResult = kryo.readObject(Input(buffer), ClientTcpMessage::class.java)
        println(output.position())
        println(tcpMessageResult)
    }
}