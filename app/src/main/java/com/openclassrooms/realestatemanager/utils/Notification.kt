package com.openclassrooms.realestatemanager.utils

import android.app.Notification
import android.content.Context
import androidx.core.app.NotificationCompat
import com.openclassrooms.realestatemanager.R

object Notification {

    fun sendNotification(context: Context): Notification {
        return NotificationCompat.Builder(context, "success_property")
            .setContentTitle(context.resources.getString(R.string.success_title))
            .setContentText(context.resources.getString(R.string.success_description))
            .setSmallIcon(R.drawable.ic_add)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()
    }
}
