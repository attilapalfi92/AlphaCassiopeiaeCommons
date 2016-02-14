package com.attilapalfi.commons

import com.attilapalfi.commons.messages.MESSAGE_END
import com.attilapalfi.commons.utlis.byteArrayEquals

/**
 * Created by palfi on 2016-02-13.
 */
open class IntelligentTcpMessageBuffer(private val signalProcessor: TcpSignalProcessor) : TcpMessageBuffer {

    private val buffer = ByteArray(TCP_BUFFER_BUFFER_SIZE)
    private var currentBufferSize: Int = 0

    override fun tryToProcess(array: ByteArray, readBytes: Int) {
        appendToBuffer(array, readBytes)
        findAndProcessMessages()
    }

    private fun appendToBuffer(array: ByteArray, readBytes: Int) {
        for (i in 0..readBytes - 1) {
            buffer[currentBufferSize + i] = array[i]
        }
        currentBufferSize += readBytes
    }

    private fun findAndProcessMessages() {
        var messageEndIndex = findMessageEndIndex()
        while (messageEndIndex != -1) {
            processMessage(messageEndIndex)
            messageEndIndex = findMessageEndIndex()
        }
    }

    private fun findMessageEndIndex(): Int {
        for (i in 0..currentBufferSize - MESSAGE_END.size) {
            val bufferCopy: ByteArray = buffer.copyOfRange(i, i + MESSAGE_END.size)
            if (byteArrayEquals(bufferCopy, MESSAGE_END)) {
                return i
            }
        }
        return -1
    }

    private fun processMessage(messageEndIndex: Int) {
        val messageBytes: ByteArray = buffer.copyOfRange(0, messageEndIndex)
        truncateBuffer(messageEndIndex)
        signalProcessor.process(messageBytes)
    }

    private fun truncateBuffer(messageEnd: Int) {
        val tempArray: ByteArray = buffer.copyOfRange(messageEnd + MESSAGE_END.size, currentBufferSize)
        buffer.fill(0)
        currentBufferSize = tempArray.size
        for (i in 0..currentBufferSize - 1) {
            buffer[i] = tempArray[i]
        }
    }
}