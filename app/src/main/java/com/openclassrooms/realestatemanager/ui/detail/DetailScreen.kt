package com.openclassrooms.realestatemanager.ui.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.database.Property
import com.openclassrooms.realestatemanager.database.Status
import com.openclassrooms.realestatemanager.theme.AppTheme
import com.openclassrooms.realestatemanager.ui.composant.carousel.Carousel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    property: Property,
    navController: NavController,
) {
    val context = LocalContext.current

    AppTheme() {
        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = {
                        Text(
                            "${property.price} $",
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = { navController.navigateUp() }) {
                            Icon(
                                imageVector = Icons.Filled.ArrowBack,
                                contentDescription = "Back",
                            )
                        }
                    },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = MaterialTheme.colorScheme.primary),
                )
            },
            content = { innerPadding ->
                LazyColumn(
                    contentPadding = innerPadding,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    item {
                        Carousel(context = context, property = property)
                    }
                    item {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                        ) {
                            Text(
                                text = property.status.label,
                                color = if (property.status == Status.AVAILABLE) {
                                    Color.Green
                                } else {
                                    MaterialTheme.colorScheme.error
                                },
                            )
                        }
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                        ) {
                            Text("Entry: ${property.entryDate}")
                            if (property.soldDate.isNotEmpty()) {
                                Text("Sold: ${property.soldDate}")
                            }
                        }
                    }
                    item {
                        Divider()
                    }
                    item {
                        Row() {
                            Column(
                                modifier = Modifier
                                    .weight(1f),
                            ) {
                                Text("Surface: ${property.surface}")
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
                        LazyRow() {
                            item {
                                property.nearInterestPoint.forEach { interest ->
                                    FilterChip(
                                        onClick = {},
                                        label = { Text(interest.label) },
                                        selected = true,
                                    )
                                }
                            }
                        }
                    }
                    item {
                        Divider()
                    }
                    item {
                        Row() {
                            Column(
                                modifier = Modifier
                                    .weight(1f),
                            ) {
                                Text(property.description)
                            }
                            Column(
                                modifier = Modifier
                                    .weight(1f),
                                horizontalAlignment = Alignment.End,
                            ) {
                                Row() {
                                    Icon(painterResource(id = R.drawable.ic_address), contentDescription = "address")
                                    Text(property.address)
                                }
                            }
                        }
                    }
                }
            },
        )
    }
}
