package es.dao.sportiva.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.os.ParcelFileDescriptor
import java.io.FileDescriptor
import java.io.IOException
import java.time.Instant
import java.time.LocalDateTime
import java.util.*

fun runOnUiThread(action: () -> Unit) {
    Handler(Looper.getMainLooper()).post(action)
}

fun Long.toLocalDateTime(): LocalDateTime {
    val triggerTime = LocalDateTime.ofInstant(
        Instant.ofEpochMilli(this),
        TimeZone.getDefault().toZoneId()
    )
    return triggerTime
}

fun Uri.getBitmap(context: Context): Bitmap? {
    var parcelFileDescriptor: ParcelFileDescriptor? = null
    try {
        parcelFileDescriptor = context.contentResolver.openFileDescriptor(this, "r")
        val fileDescriptor: FileDescriptor = parcelFileDescriptor!!.fileDescriptor
        val image = BitmapFactory.decodeFileDescriptor(fileDescriptor)
        parcelFileDescriptor.close()
        return image
    } catch (e: IOException) {
        e.printStackTrace()
    } finally {
        try {
            parcelFileDescriptor?.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
    return null
}