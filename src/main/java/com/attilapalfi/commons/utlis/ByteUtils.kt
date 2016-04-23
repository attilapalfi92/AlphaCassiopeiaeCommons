package com.attilapalfi.commons.utlis

/**
 * Created by palfi on 2016-02-14.
 */

fun byteArrayEquals(first: ByteArray, second: ByteArray): Boolean {
    if (second.size == first.size) {
        second.forEachIndexed { i, byte ->
            if (first[i] != byte) {
                return false
            }
        }
    } else {
        return false
    }
    return true
}
