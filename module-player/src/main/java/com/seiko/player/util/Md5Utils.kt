package com.seiko.player.util

import com.seiko.common.util.getMD5
import java.io.File
import java.io.RandomAccessFile

/**
 * 前 maxLength MB数据的md5
 */
fun File.getVideoMd5(maxLength: Long = 16 * 1024 * 1024): String {
    return if (length() < maxLength) {
        getMD5()
    } else {
        val r = RandomAccessFile(this, "r")
        r.seek(0)
        val bs = ByteArray(16 * 1024 * 1024)
        r.read(bs)
        bs.getMD5()
    }
}