package com.openclassrooms.realestatemanager.utils

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.core.content.FileProvider
import com.openclassrooms.realestatemanager.BuildConfig
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date

object ImageSave {

    private fun uriToBitmap(context: Context, uri: Uri): Bitmap? {
        var bitmap: Bitmap? = null
        try {
            bitmap = BitmapFactory.decodeStream(context.contentResolver.openInputStream(uri))
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return bitmap
    }

    @SuppressLint("SimpleDateFormat")
    fun saveImgInCache(context: Context, uri: Uri): Uri {
        val timeStamp = SimpleDateFormat("ddMMyyyy_HHmmss").format(Date())
        val file = File(context.externalCacheDir, "$timeStamp.png")
        try {
            val f = FileOutputStream(file)
            uriToBitmap(context, uri)?.compress(Bitmap.CompressFormat.PNG, 100, f)
            f.flush()
            f.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".provider", file)
    }

    @SuppressLint("SimpleDateFormat")
    fun createImageFile(context: Context): File {
        val timeStamp = SimpleDateFormat("ddMMyyyy_HHmmss").format(Date())
        return File.createTempFile(
            "${timeStamp}_",
            ".png",
            context.externalCacheDir,
        )
    }
}
