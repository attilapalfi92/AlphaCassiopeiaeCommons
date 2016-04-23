package com.attilapalfi.commons

import com.attilapalfi.commons.messages.ClientTcpMessage
import com.attilapalfi.commons.messages.ClientTcpMessageType.DISCONNECTION
import com.attilapalfi.commons.messages.ClientTcpMessageType.REGISTRATION
import com.attilapalfi.commons.messages.PressedButton.*
import com.attilapalfi.commons.utlis.ClientMessageConverter
import java.io.OutputStream
import java.net.InetAddress
import java.net.Socket

/**
 * Created by palfi on 2016-02-14.
 */
object TestUtils {

    private val converter = ClientMessageConverter()

    fun startClientThread(serverAddress: InetAddress, serverPort: Int): Thread {
        return Thread({
            val clientSocket: Socket = Socket(serverAddress, serverPort)
            clientSocket.outputStream.use {
                for (i in 1..5) {
                    writeTcpClientMessagesToStream(it)
                    Thread.sleep(200)
                }
            }
        }).apply { start() }
    }

    fun writeTcpClientMessagesToStream(out: OutputStream) {
        var clientMessage: ClientTcpMessage = ClientTcpMessage(REGISTRATION, A, "alma")
        writeToStream(clientMessage, out)

        clientMessage = ClientTcpMessage(DISCONNECTION, B, "alma")
        writeToStream(clientMessage, out)

        clientMessage = ClientTcpMessage(REGISTRATION, X, "alma")
        writeToStream(clientMessage, out)

        clientMessage = ClientTcpMessage(DISCONNECTION, Y, "alma")
        writeToStream(clientMessage, out)

        clientMessage = ClientTcpMessage(REGISTRATION, A, "alma")
        writeToStream(clientMessage, out)
    }

    private fun writeToStream(clientMessage: ClientTcpMessage, it: OutputStream) {
        it.write(converter.messageToByteArray(clientMessage) + MESSAGE_END)
    }
}
