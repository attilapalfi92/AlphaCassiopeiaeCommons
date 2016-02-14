package com.attilapalfi.commons

import com.attilapalfi.commons.messages.*
import com.attilapalfi.commons.utlis.ClientMessageConverter
import java.io.OutputStream
import java.net.InetAddress
import java.net.Socket

/**
 * Created by palfi on 2016-02-14.
 */
object TestUtils {
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
        var clientMessage: TcpClientMessage = TcpClientMessage(SENSOR_DATA, "alma")
        writeToStream(clientMessage, out)

        clientMessage = TcpClientMessage(REGISTRATION, "alma")
        writeToStream(clientMessage, out)

        clientMessage = TcpClientMessage(START, "alma")
        writeToStream(clientMessage, out)

        clientMessage = TcpClientMessage(PAUSE, "alma")
        writeToStream(clientMessage, out)

        clientMessage = TcpClientMessage(RESUME, "alma")
        writeToStream(clientMessage, out)
    }

    private fun writeToStream(clientMessage: TcpClientMessage, it: OutputStream) {
        it.write(ClientMessageConverter.tcpClientMessageToByteArray(clientMessage) + MESSAGE_END)
    }
}
