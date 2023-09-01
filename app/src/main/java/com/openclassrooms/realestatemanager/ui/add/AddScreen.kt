package com.openclassrooms.realestatemanager.ui.add

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.database.Agent
import com.openclassrooms.realestatemanager.database.InterestPoint
import com.openclassrooms.realestatemanager.database.PropertyType
import com.openclassrooms.realestatemanager.theme.AppTheme

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun AddScreen(addViewModel: AddViewModel = viewModel()) {
    val addUiState by addViewModel.uiState.collectAsState()
    var typeExpanded by remember { mutableStateOf(false) }
    var agentExpanded by remember { mutableStateOf(false) }
    val chip = remember { mutableStateListOf<InterestPoint>() }

    AppTheme {
        LazyColumn(
            verticalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxSize(),
            contentPadding = PaddingValues(all = 16.dp),
        ) {
            item {
                Icon(
                    painterResource(id = R.drawable.icons8_accueil),
                    contentDescription = "image",
                    modifier = Modifier
                        .fillMaxWidth(),
                )
                LazyRow {
                    item {
                        InterestPoint.values().forEach { interest ->
                            FilterChip(
                                onClick = {
                                    if (chip.contains(interest)) chip.remove(interest) else chip.add(interest)
                                    addViewModel.updateInterestPoint(interest)
                                },
                                label = { Text(text = interest.label) },
                                selected = chip.contains(interest),
                                leadingIcon = if (chip.contains(interest)) {
                                    {
                                        Icon(
                                            imageVector = Icons.Filled.Done,
                                            contentDescription = "Done icon",
                                            modifier = Modifier.size(FilterChipDefaults.IconSize),
                                        )
                                    }
                                } else {
                                    null
                                },
                                modifier = Modifier
                                    .padding(horizontal = 4.dp),
                            )
                        }
                    }
                }
                Row(
                    modifier = Modifier
                        .padding(vertical = 8.dp)
                        .fillMaxWidth(),
                ) {
                    ExposedDropdownMenuBox(
                        expanded = typeExpanded,
                        onExpandedChange = {
                            typeExpanded = !typeExpanded
                        },
                        modifier = Modifier
                            .weight(1f),
                    ) {
                        TextField(
                            modifier = Modifier
                                .fillMaxWidth()
                                .menuAnchor(),
                            readOnly = true,
                            value = addUiState.type.label,
                            onValueChange = {},
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = typeExpanded) },
                            colors = ExposedDropdownMenuDefaults.textFieldColors(),
                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                        )
                        ExposedDropdownMenu(
                            expanded = typeExpanded,
                            onDismissRequest = { typeExpanded = false },
                        ) {
                            PropertyType.values().forEach { type ->
                                DropdownMenuItem(
                                    text = { Text(type.label) },
                                    onClick = {
                                        addViewModel.updateType(type)
                                        typeExpanded = false
                                    },
                                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                                )
                            }
                        }
                    }
                    TextField(
                        value = addUiState.price.toString(),
                        onValueChange = { addViewModel.updatePrice(it.toInt()) },
                        label = { Text(stringResource(id = R.string.price)) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Next),
                        modifier = Modifier
                            .padding(start = 8.dp)
                            .weight(1f),
                    )
                }
                Row(
                    modifier = Modifier
                        .padding(vertical = 8.dp)
                        .fillMaxWidth(),
                ) {
                    TextField(
                        value = addUiState.surface.toString(),
                        onValueChange = { addViewModel.updateSurface(it.toInt()) },
                        label = { Text(stringResource(id = R.string.surface)) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Next),
                        singleLine = true,
                        modifier = Modifier.weight(1f),
                    )
                    TextField(
                        value = addUiState.pieceNumber.toString(),
                        onValueChange = { addViewModel.updatePieceNumber(it.toInt()) },
                        label = { Text(stringResource(id = R.string.piece_number)) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Next),
                        singleLine = true,
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 8.dp),
                    )
                }
                ExposedDropdownMenuBox(
                    expanded = agentExpanded,
                    onExpandedChange = { agentExpanded = !agentExpanded },
                    modifier = Modifier.padding(vertical = 8.dp),
                ) {
                    TextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor(),
                        readOnly = true,
                        value = addUiState.agent.label,
                        onValueChange = {},
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = agentExpanded) },
                        colors = ExposedDropdownMenuDefaults.textFieldColors(),
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                    )
                    ExposedDropdownMenu(
                        expanded = agentExpanded,
                        onDismissRequest = { agentExpanded = false },
                    ) {
                        Agent.values().forEach { agent ->
                            DropdownMenuItem(
                                text = { Text(agent.label) },
                                onClick = {
                                    addViewModel.updateAgent(agent)
                                    agentExpanded = false
                                },
                                contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                            )
                        }
                    }
                }
                TextField(
                    value = addUiState.address,
                    onValueChange = { addViewModel.updateAddress(it) },
                    label = { Text(stringResource(id = R.string.address)) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text, imeAction = ImeAction.Next),
                    modifier = Modifier
                        .padding(vertical = 8.dp)
                        .fillMaxWidth(),
                )
                TextField(
                    value = addUiState.description,
                    onValueChange = { addViewModel.updateDescription(it) },
                    label = { Text(stringResource(id = R.string.description)) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text, imeAction = ImeAction.Done),
                    modifier = Modifier
                        .padding(vertical = 8.dp)
                        .height(200.dp)
                        .fillMaxWidth(),
                )
            }
            item {
                Box(
                    modifier = Modifier
                        .fillMaxHeight(),
                ) {
                    Button(
                        onClick = { addViewModel.addProperty() },
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.BottomCenter)
                            .size(50.dp),
                    ) {
                        Text(
                            text = stringResource(id = R.string.add),
                            textAlign = TextAlign.Center,
                            fontSize = 16.sp,
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ReadonlyTextField(
    value: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    label: @Composable () -> Unit,
) {
    Box {
        TextField(
            value = value,
            onValueChange = onValueChange,
            modifier = modifier,
            label = label,
        )

        Box(
            modifier = Modifier
                .matchParentSize()
                .alpha(0f)
                .clickable(onClick = onClick),
        )
    }
}

@Preview
@Composable
fun Preview() {
    AddScreen()
}
