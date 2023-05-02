package es.dao.sportiva.utils.download_tools

import android.content.Context

interface Downloader {
    fun donwloadApkFile(url: String): Long
}