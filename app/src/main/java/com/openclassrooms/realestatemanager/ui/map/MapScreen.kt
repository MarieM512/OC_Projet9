package com.openclassrooms.realestatemanager.ui.map

import android.Manifest
import android.annotation.SuppressLint
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.openclassrooms.realestatemanager.theme.AppTheme
import com.openclassrooms.realestatemanager.utils.Permissions

@SuppressLint("PermissionLaunchedDuringComposition")
@Composable
fun MapScreen() {
    val context = LocalContext.current

    val openDialogLocation = remember { mutableStateOf(false) }
    val openMap = remember { mutableStateOf(false) }
    val multiplePermissionsState = arrayOf(
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION,
    )

    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions(),
    ) { permissionsMap ->
        val areGranted = permissionsMap.values.reduce { acc, next -> acc && next }
        if (areGranted) {
            openMap.value = true
        } else {
            openDialogLocation.value = true
        }
    }

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(LatLng(44.81, 20.461), 17f)
    }

    LaunchedEffect(Unit) {
        Permissions.checkAndRequestLocationPermissions(context, multiplePermissionsState, launcher, openMap)
    }

    AppTheme {
        if (openMap.value) {
            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = cameraPositionState,
            ) {
                Marker(
                    state = MarkerState(position = LatLng(44.81, 20.461)),
                    title = "Signapour",
                )
            }
        }
        if (openDialogLocation.value) {
            Permissions.dialogPermission("Permission denied", "Please go to your settings to allow location permission in order to be able to use the map.", openDialogLocation)
        }
    }
}
