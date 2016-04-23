package com.attilapalfi.commons

/**
 * Created by palfi on 2016-01-13.
 */
val UDP_PORT = 23456
val UDP_BUFFER_SIZE = 200
val TCP_BUFFER_SIZE = 200
val TCP_BUFFER_BUFFER_SIZE = 2000
val DEFAULT_MAX_USERS = 4

val MESSAGE_END: ByteArray = ByteArray(16, { i -> i.toByte() })