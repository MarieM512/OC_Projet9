package com.openclassrooms.realestatemanager.ui.filter

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.openclassrooms.realestatemanager.database.Agent
import com.openclassrooms.realestatemanager.database.PropertyEvent
import com.openclassrooms.realestatemanager.database.PropertyState
import com.openclassrooms.realestatemanager.database.PropertyType
import com.openclassrooms.realestatemanager.database.SortType
import com.openclassrooms.realestatemanager.theme.AppTheme
import com.openclassrooms.realestatemanager.ui.composant.card.CardFilterComparison

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterScreen(
    state: PropertyState,
    onEvent: (PropertyEvent) -> Unit,
) {
    var agentExpanded by remember { mutableStateOf(false) }
    var typeExpanded by remember { mutableStateOf(false) }

    AppTheme {
        Column(
            modifier = Modifier.padding(16.dp),
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(bottom = 8.dp)
                    .fillMaxWidth(),
            ) {
                Button(
                    onClick = {
                        onEvent(PropertyEvent.SortProperty(SortType.RESET))
                        onEvent(PropertyEvent.ResetFilter)
                    },
                ) {
                    Text("Reset")
                }
                Text("Filters")
                Button(
                    onClick = {
                        onEvent(PropertyEvent.SortProperty(SortType.FILTER))
                    },
                ) {
                    Text("Apply")
                }
            }
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                item {
                    CardFilterComparison(
                        title = "Price",
                        min = state.minPrice,
                        max = state.maxPrice,
                        minEvent = { onEvent(PropertyEvent.FilterByPriceMin(it.toInt())) },
                        maxEvent = { onEvent(PropertyEvent.FilterByPriceMax(it.toInt())) },
                    )
                }
                item {
                    CardFilterComparison(
                        title = "Surface",
                        min = state.minSurface,
                        max = state.maxSurface,
                        minEvent = { onEvent(PropertyEvent.FilterBySurfaceMin(it.toInt())) },
                        maxEvent = { onEvent(PropertyEvent.FilterBySurfaceMax(it.toInt())) },
                    )
                }
                item {
                    CardFilterComparison(
                        title = "Piece",
                        min = state.minPiece,
                        max = state.maxPiece,
                        minEvent = { onEvent(PropertyEvent.FilterByPieceMin(it.toInt())) },
                        maxEvent = { onEvent(PropertyEvent.FilterByPieceMax(it.toInt())) },
                    )
                }
                item {
                    Card {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                        ) {
                            Text("Type")
                            ExposedDropdownMenuBox(
                                expanded = typeExpanded,
                                onExpandedChange = {
                                    typeExpanded = !typeExpanded
                                },
                            ) {
                                TextField(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .menuAnchor(),
                                    readOnly = true,
                                    value = state.filterType?.label ?: "",
                                    onValueChange = {},
                                    trailingIcon = {
                                        ExposedDropdownMenuDefaults.TrailingIcon(
                                            expanded = typeExpanded,
                                        )
                                    },
                                    colors = ExposedDropdownMenuDefaults.textFieldColors(),
                                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                                )
                                ExposedDropdownMenu(
                                    expanded = typeExpanded,
                                    onDismissRequest = { typeExpanded = false },
                                ) {
                                    PropertyType.values().forEach { type ->
                                        DropdownMenuItem(
                                            text = { Text(type.label) },
                                            onClick = {
                                                onEvent(PropertyEvent.FilterByType(type))
                                                typeExpanded = false
                                            },
                                            contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                        ) {
                            Text("Address")
                            TextField(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                value = state.filterAddress,
                                onValueChange = { onEvent(PropertyEvent.FilterByAddress(it)) },
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Text,
                                    imeAction = ImeAction.Done,
                                ),
                            )
                        }
                    }
                }
                item {
                    Card {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                        ) {
                            Text("Agent")
                            ExposedDropdownMenuBox(
                                expanded = agentExpanded,
                                onExpandedChange = { agentExpanded = !agentExpanded },
                            ) {
                                TextField(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .menuAnchor(),
                                    readOnly = true,
                                    value = state.filterAgent?.label ?: "",
                                    onValueChange = {},
                                    trailingIcon = {
                                        ExposedDropdownMenuDefaults.TrailingIcon(
                                            expanded = agentExpanded,
                                        )
                                    },
                                    colors = ExposedDropdownMenuDefaults.textFieldColors(),
                                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                                )
                                ExposedDropdownMenu(
                                    expanded = agentExpanded,
                                    onDismissRequest = { agentExpanded = false },
                                ) {
                                    Agent.values().forEach { agent ->
                                        DropdownMenuItem(
                                            text = { Text(agent.label) },
                                            onClick = {
                                                onEvent(PropertyEvent.FilterByAgent(agent))
                                                agentExpanded = false
                                            },
                                            contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
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
}
