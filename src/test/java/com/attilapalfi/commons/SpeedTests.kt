package com.attilapalfi.commons

import com.attilapalfi.commons.messages.ClientTcpMessage
import com.attilapalfi.commons.messages.ClientTcpMessageType.REGISTRATION
import com.attilapalfi.commons.messages.PressedButton.None
import com.esotericsoftware.kryo.Kryo
import com.esotericsoftware.kryo.io.Input
import com.esotericsoftware.kryo.io.Output
import org.junit.Test
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.util.*

/**
 * Created by palfi on 2016-04-23.
 */
class SpeedTests {

    private val kryo = Kryo()

    @Test
    fun testSerializationSpeed() {
        val messageCount = 10000
        val iterationCount = 100
        val testList: List<ClientTcpMessage> = createMessages(messageCount)
        println("--- serialization: ---")
        measureJavaIoSerialization(iterationCount, messageCount, testList)
        println("Byte array size: ${messageToByteArrayWithJavaIO(testList[0]).size}\n")
        //measureApacheSerialization(iterationCount, messageCount, testList)
        //println("Byte array size: ${messageToByteArrayWithApache(testList[0]).size}\n")
        measureKryoSerialization(iterationCount, messageCount, testList)
        println("Byte array size: ${messageToByteArrayWithKryo(testList[0]).size}")
    }

    @Test
    fun testDeserializationSpeed() {
        val messageCount = 10000
        val iterationCount = 100
        val messages = createMessages(messageCount)
        var byteArrayList: List<ByteArray> = messages.map { messageToByteArrayWithJavaIO(it) }
        println("--- deserialization: ---")
        measureJavaIoDeserialization(byteArrayList, iterationCount, messageCount)
        //measureApacheCommonsDeserialization(byteArrayList, iterationCount, messageCount)
        byteArrayList = messages.map { messageToByteArrayWithKryo(it) }
        measureKryoDeserialization(byteArrayList, iterationCount, messageCount)
    }

    private fun measureJavaIoSerialization(iterationCount: Int, messageCount: Int,
                                           testList: List<ClientTcpMessage>) {

        measureSerialization(iterationCount, messageCount, testList, "java.io",
                { m -> messageToByteArrayWithJavaIO(m) })
    }

    /*
    private fun measureApacheSerialization(iterationCount: Int, messageCount: Int,
                                           testList: List<ClientTcpMessage>) {

        measureSerialization(iterationCount, messageCount, testList, "apache commons",
                { m -> messageToByteArrayWithApache(m) })
    }
    */

    private fun measureKryoSerialization(iterationCount: Int, messageCount: Int,
                                         testList: List<ClientTcpMessage>) {
        measureSerialization(iterationCount, messageCount, testList, "kryo",
                { m -> messageToByteArrayWithJavaIO(m) })
    }

    private fun measureSerialization(iterationCount: Int, messageCount: Int,
                                     testList: List<ClientTcpMessage>, name: String,
                                     function: (ClientTcpMessage) -> ByteArray) {
        val start = System.nanoTime()
        for (i in 1..iterationCount) {
            testList.forEach { function(it) }
        }
        val passed = System.nanoTime() - start
        println("$name: total: ${passed / 1000000}ms. " +
                "1 message: ${passed / (messageCount * iterationCount)}ns")
    }

    private fun messageToByteArrayWithJavaIO(message: ClientTcpMessage): ByteArray {
        ByteArrayOutputStream().use {
            val oos = ObjectOutputStream(it)
            oos.writeObject(message)
            val payload: ByteArray = it.toByteArray();
            return payload
        }
    }

    //private fun messageToByteArrayWithApache(message: ClientTcpMessage): ByteArray
    //        = SerializationUtils.serialize(message)

    private fun messageToByteArrayWithKryo(message: ClientTcpMessage): ByteArray {
        val buffer = ByteArray(300)
        val output = Output(buffer)
        kryo.writeObject(output, message)
        return buffer.copyOf(output.position())
    }

    private fun measureJavaIoDeserialization(byteArrayList: List<ByteArray>, iterationCount: Int,
                                             messageCount: Int) {

        measureDeserialization(byteArrayList, iterationCount, messageCount, "java.io",
                { b -> byteArrayToMessageWithJavaIO(b) })
    }

    /*
    private fun measureApacheCommonsDeserialization(byteArrayList: List<ByteArray>, iterationCount: Int,
                                                    messageCount: Int) {

        measureDeserialization(byteArrayList, iterationCount, messageCount, "apache commons",
                { b -> byteArrayToMessageWithApache(b) })
    }
    */

    private fun measureKryoDeserialization(byteArrayList: List<ByteArray>, iterationCount: Int,
                                           messageCount: Int) {

        measureDeserialization(byteArrayList, iterationCount, messageCount, "kryo",
                { b -> byteArrayToMessageWithKryo(b) })
    }

    private fun measureDeserialization(byteArrayList: List<ByteArray>, iterationCount: Int,
                                       messageCount: Int, name: String,
                                       function: (ByteArray) -> ClientTcpMessage) {

        val start = System.nanoTime()
        for (i in 1..iterationCount) {
            byteArrayList.forEach { function(it) }
        }
        val passed = System.nanoTime() - start
        println("$name: total: ${passed / 1000000}ms. " +
                "1 message: ${passed / (messageCount * iterationCount)}ns")
    }

    private fun byteArrayToMessageWithJavaIO(payload: ByteArray): ClientTcpMessage {
        ByteArrayInputStream(payload).use {
            val ois = ObjectInputStream(it)
            val receivedMessage: ClientTcpMessage = ois.readObject() as ClientTcpMessage
            return receivedMessage
        }
    }

    //private fun byteArrayToMessageWithApache(payload: ByteArray): ClientTcpMessage
    //        = SerializationUtils.deserialize(payload)

    private fun byteArrayToMessageWithKryo(payload: ByteArray): ClientTcpMessage
            = kryo.readObject(Input(payload), ClientTcpMessage::class.java)


    private fun createMessages(messageCount: Int): List<ClientTcpMessage> {
        val messageList = ArrayList<ClientTcpMessage>(messageCount)
        val random = Random().apply { setSeed(System.nanoTime()) }

        for (i in 0..messageCount - 1) {
            messageList.add(ClientTcpMessage(REGISTRATION, None, UUID.randomUUID().toString()))
        }

        return messageList
    }
}