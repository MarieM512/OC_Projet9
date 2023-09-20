package com.openclassrooms.realestatemanager.ui.detail

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.database.Property
import com.openclassrooms.realestatemanager.database.Status
import com.openclassrooms.realestatemanager.theme.AppTheme
import com.openclassrooms.realestatemanager.ui.composant.carousel.Carousel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailTabletScreen(
    property: Property,
    navController: NavController,
) {
    val context = LocalContext.current

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(LatLng(property.latitude, property.longitude), 15f)
    }

    AppTheme() {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            item {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxHeight()
                            .weight(1f),
                    ) {
                        Carousel(context, property)
                    }
                    Column(
                        modifier = Modifier
                            .weight(2f),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                Text(
                                    text = property.status.label,
                                    color = if (property.status == Status.AVAILABLE) {
                                        Color.Green
                                    } else {
                                        MaterialTheme.colorScheme.error
                                    },
                                )
                                IconButton(
                                    onClick = {
                                        navController.navigate("edit/$property")
                                    },
                                ) {
                                    Icon(
                                        imageVector = Icons.Filled.Edit,
                                        contentDescription = "Edit property",
                                        tint = Color.Black,
                                    )
                                }
                            }
                        }
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                        ) {
                            Text("Entry: ${property.entryDate}")
                            if (property.soldDate.isNotEmpty()) {
                                Text("Sold: ${property.soldDate}")
                            }
                        }
                        LazyRow() {
                            item {
                                property.nearInterestPoint.forEach { interest ->
                                    FilterChip(
                                        onClick = {},
                                        label = { Text(interest.label) },
                                        selected = true,
                                        modifier = Modifier
                                            .padding(horizontal = 4.dp),
                                    )
                                }
                            }
                        }
                        Divider()
                        Row() {
                            Column(
                                modifier = Modifier
                                    .weight(1f),
                            ) {
                                Text("Surface: ${property.surface} m²")
                                Text("Piece: ${property.pieceNumber}")
                            }
                            Column(
                                modifier = Modifier
                                    .weight(1f),
                                horizontalAlignment = Alignment.End,
                            ) {
                                Text(property.type.label)
                                Text(property.agent.label)
                            }
                        }
                        Divider()
                        Text(
                            text = property.description,
                            textAlign = TextAlign.Justify,
                        )
                        Divider()
                        Row() {
                            Column(
                                modifier = Modifier
                                    .weight(1f),
                            ) {
                                Row() {
                                    Icon(painterResource(id = R.drawable.ic_address), contentDescription = "address")
                                    Text(property.address)
                                }
                            }
                            Column(
                                modifier = Modifier
                                    .weight(2f),
                                horizontalAlignment = Alignment.End,
                            ) {
                                GoogleMap(
                                    modifier = Modifier
                                        .border(BorderStroke(1.dp, MaterialTheme.colorScheme.primary))
                                        .fillMaxWidth()
                                        .height(240.dp),
                                    cameraPositionState = cameraPositionState,
                                    uiSettings = MapUiSettings(rotationGesturesEnabled = false, scrollGesturesEnabled = false, scrollGesturesEnabledDuringRotateOrZoom = false, tiltGesturesEnabled = false, zoomControlsEnabled = false, zoomGesturesEnabled = false),
                                ) {
                                    Marker(
                                        state = MarkerState(position = LatLng(property.latitude, property.longitude)),
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
