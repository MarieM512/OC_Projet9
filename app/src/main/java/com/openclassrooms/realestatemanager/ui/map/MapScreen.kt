package com.openclassrooms.realestatemanager.ui.map

import android.Manifest
import android.annotation.SuppressLint
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.database.PropertyState
import com.openclassrooms.realestatemanager.theme.AppTheme
import com.openclassrooms.realestatemanager.ui.composant.alert.DialogInformation
import com.openclassrooms.realestatemanager.utils.Permissions

@SuppressLint("PermissionLaunchedDuringComposition")
@Composable
fun MapScreen(
    state: PropertyState,
    navController: NavController,
    mapViewModel: MapViewModel = viewModel(),
) {
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
        position = CameraPosition.fromLatLngZoom(LatLng(0.0, 0.0), 15f)
    }
    val mapProperties by remember { mutableStateOf(MapProperties(isMyLocationEnabled = true)) }

    LaunchedEffect(Unit) {
        mapViewModel.fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
        Permissions.checkAndRequestLocationPermissions(context, multiplePermissionsState, launcher, openMap)
    }

    AppTheme {
        if (openMap.value) {
            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = cameraPositionState,
                properties = mapProperties,
            ) {
                mapViewModel.getCurrentLocation(cameraPositionState)
                state.property.forEach { property ->
                    Marker(
                        state = MarkerState(position = LatLng(property.latitude, property.longitude)),
                        onClick = {
                            navController.navigate("property/$property")
                            true
                        },
                    )
                }
            }
        }
        if (openDialogLocation.value) {
            DialogInformation(stringResource(R.string.permission_title), stringResource(id = R.string.location_permission_description), openMap)
        }
    }
}
