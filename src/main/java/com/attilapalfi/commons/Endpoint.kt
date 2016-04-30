package com.attilapalfi.commons

import java.io.Serializable
import java.net.InetAddress

/**
 * Created by palfi on 2016-01-14.
 */
open class Endpoint(val IP: InetAddress,
                    val port: Int) : Serializable {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other?.javaClass != javaClass) return false

        other as Endpoint

        if (IP != other.IP) return false

        return true
    }

    override fun hashCode(): Int {
        return IP.hashCode()
    }

    override fun toString() = "IP: $IP port: $port"
}