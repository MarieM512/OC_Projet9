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
        openMap: MutableState<Boolean>,
    ) {
        if (
            permissions.all {
                ContextCompat.checkSelfPermission(
                    context,
                    it,
                ) == PackageManager.PERMISSION_GRANTED
            }
        ) {
            openMap.value = true
        } else {
            launcher.launch(permissions)
        }
    }
}