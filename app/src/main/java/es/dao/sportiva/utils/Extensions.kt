package es.dao.sportiva.utils

import android.os.Handler
import android.os.Looper

fun runOnUiThread(action: () -> Unit) {
    Handler(Looper.getMainLooper()).post(action)
}