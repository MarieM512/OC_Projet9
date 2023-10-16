package com.openclassrooms.realestatemanager.ui.composant.button

import android.content.Context
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.ui.composant.alert.DialogInformation
import com.openclassrooms.realestatemanager.utils.Permissions

@Composable
fun Pictures(
    context: Context,
    cameraPermissionLauncher: ManagedActivityResultLauncher<String, Boolean>,
    folderPermissionLauncher: ManagedActivityResultLauncher<String, Boolean>,
    openDialogPicture: MutableState<Boolean>,
    takePicture: MutableState<Boolean>,
    openDialogCameraPermission: MutableState<Boolean>,
    openDialogFolderPermission: MutableState<Boolean>,
) {
    Button(
        onClick = {
            Permissions.checkAndRequestPermission(
                context,
                Permissions.cameraPermission,
                cameraPermissionLauncher,
                openDialogPicture,
                takePicture,
            )
        },
    ) {
        Icon(
            painterResource(id = R.drawable.ic_picture),
            contentDescription = stringResource(id = R.string.camera),
            modifier = Modifier.padding(vertical = 8.dp),
        )
    }
    Button(
        onClick = {
            Permissions.checkAndRequestPermission(
                context,
                Permissions.folderPermission,
                folderPermissionLauncher,
                openDialogPicture,
            )
        },
        modifier = Modifier.padding(bottom = 8.dp),
    ) {
        Icon(
            painterResource(id = R.drawable.ic_image),
            contentDescription = stringResource(id = R.string.gallery),
            modifier = Modifier.padding(vertical = 8.dp),
        )
    }
    if (openDialogCameraPermission.value) {
        DialogInformation(
            stringResource(id = R.string.permission_title),
            stringResource(id = R.string.camera_permission_description),
            openDialogCameraPermission,
        )
    }
    if (openDialogFolderPermission.value) {
        DialogInformation(
            stringResource(id = R.string.permission_title),
            stringResource(id = R.string.folder_permission_description),
            openDialogFolderPermission,
        )
    }
}
