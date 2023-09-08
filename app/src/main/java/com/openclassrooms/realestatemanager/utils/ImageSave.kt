package com.openclassrooms.realestatemanager.utils

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Environment
import android.util.Log
import androidx.compose.ui.platform.LocalContext
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date

class Cache {

    @SuppressLint("SimpleDateFormat")
    fun saveImgToCache(bitmap: Bitmap, context: Context): File {
        val path = context.applicationContext.externalCacheDir
        val cachePath = File(path, "img")

        try {
            cachePath.mkdirs()

            val stream = FileOutputStream("$cachePath /img.png")
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
            stream.close()
        } catch (e: IOException) {
            Log.e("Cache.class", "failed to save image to cache: $e")
        }
        return cachePath
    }
}