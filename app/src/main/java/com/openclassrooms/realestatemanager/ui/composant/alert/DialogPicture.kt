package com.openclassrooms.realestatemanager.ui.composant.alert

import android.content.Context
import android.net.Uri
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.core.content.FileProvider
import com.openclassrooms.realestatemanager.BuildConfig
import com.openclassrooms.realestatemanager.utils.ImageSave

@Composable
fun DialogPicture(
    context: Context,
    descriptionImage: MutableState<String>,
    takePicture: MutableState<Boolean>,
    _uri: Uri?,
    cameraLauncher: ManagedActivityResultLauncher<Uri, Boolean>,
    multiplePhotoPickerLauncher: ManagedActivityResultLauncher<PickVisualMediaRequest, Uri?>,
    openDialogPicture: MutableState<Boolean>,
) {
    var uri = _uri
    AlertDialog(
        onDismissRequest = {},
        title = {
            Text(text = "Image")
        },
        text = {
            TextField(
                value = descriptionImage.value,
                onValueChange = { descriptionImage.value = it },
                label = { Text("Picture's title") },
            )
        },
        confirmButton = {
            Button(
                onClick = {
                    if (descriptionImage.value.isNotEmpty()) {
                        if (takePicture.value) {
                            uri = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".provider", ImageSave.createImageFile(context))
                            cameraLauncher.launch(uri)
                            takePicture.value = false
                        } else {
                            multiplePhotoPickerLauncher.launch(
                                PickVisualMediaRequest(
                                    ActivityResultContracts.PickVisualMedia.ImageOnly,
                                ),
                            )
                        }
                    }
                    openDialogPicture.value = false
                },
            ) {
                Text("Confirm")
            }
        },
        dismissButton = {
            Button(
                onClick = {
                    openDialogPicture.value = false
                },
            ) {
                Text("Cancel")
            }
        },
    )
}
