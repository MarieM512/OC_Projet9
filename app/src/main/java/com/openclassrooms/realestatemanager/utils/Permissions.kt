package com.openclassrooms.realestatemanager.utils

import android.content.Context
import android.content.pm.PackageManager
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.core.content.ContextCompat

object Permissions {

    @Composable
    fun dialogPermission(title: String, message: String, openDialog: MutableState<Boolean>) {
        AlertDialog(
            onDismissRequest = {},
            title = {
                Text(title)
            },
            text = {
                Text(message)
            },
            confirmButton = {
                Button(
                    onClick = {
                        openDialog.value = false
                    },
                ) {
                    Text("Got it")
                }
            },
        )
    }

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