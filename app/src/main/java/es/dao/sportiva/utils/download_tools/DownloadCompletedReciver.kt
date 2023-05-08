package es.dao.sportiva.utils.download_tools

import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Environment
import android.util.Log
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import es.dao.sportiva.BuildConfig
import es.dao.sportiva.utils.runOnUiThread
import java.io.File

class DownloadCompletedReciver : BroadcastReceiver() {

    // private lateinit var downloadManager: DownloadManager

    override fun onReceive(context: Context?, intent: Intent?) {

        // downloadManager = context?.getSystemService(DownloadManager::class.java)!!

        if (intent?.action == DownloadManager.ACTION_DOWNLOAD_COMPLETE) {

            val id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1L)
            // val query = DownloadManager.Query().setFilterById(id) // TODO no me hace falta esto porque esta app es lo único que va a descargar...

            if (id != -1L) {

                // Una vez descargado el APK lo instalamos
                // Para ello lo primero obtenemos la carpeta de descargas
                val downloadDir = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).path)

                // Obtenemos el archivo descargado, pero antes, como puede haber varios Sportiva.APK nos quedamos con el mas reciente
                downloadDir.listFiles()?.forEach {
                    Log.w(":::DownloadCompletedReciver", "FILE: ${it.name} - ${it.lastModified()}")
                }
                val file = downloadDir.listFiles()?.filter { it.name == "sportiva.apk" }?.maxByOrNull { it.lastModified() }

                // Si no hay archivo, no podemos instalarlo
                file?.let {

                    Log.d(":::DownloadCompletedReciver", "ENCONTRADO!!!!: ${it.name} - ${it.lastModified()}")
                    val contentUri = FileProvider.getUriForFile(context!!, BuildConfig.APPLICATION_ID + ".provider", it)

                    // Creamos el intent para instalarlo
                    val miIntent = Intent(Intent.ACTION_VIEW).apply {
                        setDataAndType(contentUri, "application/vnd.android.package-archive")
                        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    }

                    context.startActivity(miIntent)
                    //context.unregisterReceiver(this)


                } ?: run {
                    Log.e(":::DownloadCompletedReciver", "ERROR ENCONTRANDO LO DESCARGADO")
                }


            } else {
                Log.e(":::DownloadCompletedReciver", "ERROR Download completed with id: $id")
            }

        }

    }

}