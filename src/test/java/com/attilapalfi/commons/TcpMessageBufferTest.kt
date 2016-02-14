import com.attilapalfi.commons.BUFFER_SIZE
import com.attilapalfi.commons.IntelligentTcpMessageBuffer
import com.attilapalfi.commons.TcpSignalProcessor
import com.attilapalfi.commons.TestUtils
import org.junit.Test
import org.mockito.Mockito
import java.net.InetAddress
import java.net.ServerSocket
import java.net.Socket

/**
 * Created by palfi on 2016-02-13.
 */
class TcpMessageBufferTest {

    val mockSignalProcessor: TcpSignalProcessor = Mockito.mock(TcpSignalProcessor::class.java)
    val tcpMessageBuffer = IntelligentTcpMessageBuffer(mockSignalProcessor)
    var serverPort = 6789

    @Test
    fun canReadBytes() {
        val serverThread = startServerThread()
        Thread.sleep(500)
        val clientThread = TestUtils.startClientThread(InetAddress.getByName("localhost"), serverPort)
        clientThread.join()
        serverThread.join()
        // Mockito.verify(mockSignalProcessor, Mockito.times(5)).process(Mockito.any(ByteArray::class.java))
        // cannot write proper verification because of Kotlin null safety
    }


    fun startServerThread(): Thread {
        return Thread({
            val serverSocket = ServerSocket()
            serverSocket.bind(null)
            serverPort = serverSocket.localPort
            val connection: Socket = serverSocket.accept()
            connection.keepAlive = true


            while (true) {
                val array = ByteArray(BUFFER_SIZE)
                var readBytes: Int = 0
                connection.inputStream.use {
                    while (readBytes != -1) {
                        readBytes = it.read(array)
                        if (readBytes != -1) {
                            tcpMessageBuffer.tryToProcess(array, readBytes)
                        }
                    }
                }

            }
        }).apply { start() }
    }
}