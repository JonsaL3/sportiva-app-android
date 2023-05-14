package es.dao.sportiva.utils

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.os.ParcelFileDescriptor
import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.lifecycle.LifecycleCoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
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

fun Uri.getFile(context: Context): File {
    val parcelFileDescriptor: ParcelFileDescriptor? =
        context.contentResolver.openFileDescriptor(this, "r")
    val fileDescriptor: FileDescriptor = parcelFileDescriptor!!.fileDescriptor
    val file = File(context.cacheDir, "cacheFileAppeal.srl")
    val fos = FileOutputStream(file)
    val inputStream = FileInputStream(fileDescriptor)
    fos.use { output ->
        val buffer = ByteArray(4 * 1024) // or other buffer size
        var read: Int
        while (inputStream.read(buffer).also { read = it } != -1) {
            output.write(buffer, 0, read)
        }
        output.flush()
    }
    inputStream.close()
    return file
}

fun Bitmap.toBase64(): String {
    val baos = ByteArrayOutputStream()
    this.compress(Bitmap.CompressFormat.JPEG, 100, baos)
    val b = baos.toByteArray()
    return Base64.getEncoder().encodeToString(b)
}

fun String.fromBase64(): Bitmap {
    val imageBytes = Base64.getDecoder().decode(this)
    return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
}

fun Bitmap.setCircledForm(): Bitmap {
    val output = Bitmap.createBitmap(
        this.width,
        this.height, Bitmap.Config.ARGB_8888
    )
    val canvas = android.graphics.Canvas(output)
    val color = -0xbdbdbe
    val paint = android.graphics.Paint()
    val rect = android.graphics.Rect(
        0, 0, this.width,
        this.height
    )
    val rectF = android.graphics.RectF(rect)
    val roundPx = this.width.toFloat() / 2
    paint.isAntiAlias = true
    canvas.drawARGB(0, 0, 0, 0)
    paint.color = color
    canvas.drawRoundRect(rectF, roundPx, roundPx, paint)
    paint.xfermode = android.graphics.PorterDuffXfermode(android.graphics.PorterDuff.Mode.SRC_IN)
    canvas.drawBitmap(this, rect, rect, paint)
    return output
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

fun View.reduceScaleXY(
    animationduration: Long,
) {
    val scaleX = ObjectAnimator.ofFloat(this, "scaleX", 0.95f)
    val scaleY = ObjectAnimator.ofFloat(this, "scaleY", 0.95f)
    val animatorSet = AnimatorSet()
    animatorSet.playTogether(scaleX, scaleY)
    animatorSet.duration = animationduration
    animatorSet.start()
}
fun View.reduceScaleXY(
    animationduration: Long,
    lifecycleScope: LifecycleCoroutineScope,
    onCompletedAnimation: (() -> Unit)
) {
    lifecycleScope.launch {
        reduceScaleXY(animationduration)
        delay(animationduration)
    }.invokeOnCompletion {
        onCompletedAnimation.invoke()
    }
}

fun View.increaseScaleXY(animationduration: Long) {
    val scaleX = ObjectAnimator.ofFloat(this, "scaleX", 1f)
    val scaleY = ObjectAnimator.ofFloat(this, "scaleY", 1f)
    val animatorSet = AnimatorSet()
    animatorSet.playTogether(scaleX, scaleY)
    animatorSet.duration = animationduration
    animatorSet.start()
}

fun View.increaseScaleXY(
    animationduration: Long,
    lifecycleScope: LifecycleCoroutineScope,
    onCompletedAnimation: (() -> Unit)
) {
    lifecycleScope.launch {
        increaseScaleXY(animationduration)
        delay(animationduration)
    }.invokeOnCompletion {
        onCompletedAnimation.invoke()
    }
}

fun View.enableOnTouchListenerAnimation() {
    this.setOnTouchListener { v, event ->
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                v.reduceScaleXY(Constantes.ANIMACION_DURATION)
            }
            MotionEvent.ACTION_UP -> {
                v.increaseScaleXY(Constantes.ANIMACION_DURATION)
            }
        }
        false
    }
}