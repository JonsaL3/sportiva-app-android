package es.dao.sportiva.utils.download_tools

import android.app.DownloadManager
import android.content.Context
import android.os.Environment
import androidx.core.net.toUri
import java.io.File

class AndroidDownloader(
    private val context: Context
) : Downloader {

    private val downloadManager = context.getSystemService(DownloadManager::class.java)

    override fun donwloadApkFile(url: String): Long {
        val request = DownloadManager.Request(url.toUri())
            .setMimeType("application/vnd.android.package-archive")
            .setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            .setTitle("sportiva.apk")
            .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "sportiva.apk")
        return downloadManager.enqueue(request)
    }

}