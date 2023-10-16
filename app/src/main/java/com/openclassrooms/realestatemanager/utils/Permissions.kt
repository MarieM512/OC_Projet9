package com.openclassrooms.realestatemanager.utils

import android.content.Context
import android.content.pm.PackageManager
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.compose.runtime.MutableState
import androidx.core.content.ContextCompat

object Permissions {

    const val cameraPermission = android.Manifest.permission.CAMERA
    const val folderPermission = android.Manifest.permission.READ_EXTERNAL_STORAGE

    fun checkAndRequestLocationPermissions(
        context: Context,
        permissions: Array<String>,
        launcher: ManagedActivityResultLauncher<Array<String>, Map<String, Boolean>>,
        openDialog: MutableState<Boolean>,
    ) {
        if (
            permissions.all {
                ContextCompat.checkSelfPermission(
                    context,
                    it,
                ) == PackageManager.PERMISSION_GRANTED
            }
        ) {
            openDialog.value = true
        } else {
            launcher.launch(permissions)
        }
    }

    fun checkAndRequestPermission(
        context: Context,
        permissions: String,
        launcher: ManagedActivityResultLauncher<String, Boolean>,
        openDialog: MutableState<Boolean>,
        openCamera: MutableState<Boolean>? = null,
    ) {
        if (
            ContextCompat.checkSelfPermission(
                context,
                permissions,
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            openDialog.value = true
            openCamera?.value = true
        } else {
            launcher.launch(permissions)
        }
    }
}
