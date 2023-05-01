package es.dao.sportiva.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.os.ParcelFileDescriptor
import android.util.Log
import okhttp3.ResponseBody
import java.io.*
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

fun Bitmap.toBase64(): String {
    val baos = ByteArrayOutputStream()
    this.compress(Bitmap.CompressFormat.JPEG, 100, baos)
    val b = baos.toByteArray()
    return Base64.getEncoder().encodeToString(b)
}

fun Uri.toBase64(context: Context): String? {
    val bitmap = this.getBitmap(context)
    return bitmap?.toBase64()
}

fun saveFile(body: ResponseBody?, pathWhereYouWantToSaveFile: String):String{
    if (body==null)
        return ""
    var input: InputStream? = null
    try {
        input = body.byteStream()
        //val file = File(getCacheDir(), "cacheFileAppeal.srl")
        val fos = FileOutputStream(pathWhereYouWantToSaveFile)
        fos.use { output ->
            val buffer = ByteArray(4 * 1024) // or other buffer size
            var read: Int
            while (input.read(buffer).also { read = it } != -1) {
                output.write(buffer, 0, read)
            }
            output.flush()
        }
        return pathWhereYouWantToSaveFile
    }catch (e:Exception){
        Log.e("saveFile error",e.toString())
    }
    finally {
        input?.close()
    }
    return ""
}