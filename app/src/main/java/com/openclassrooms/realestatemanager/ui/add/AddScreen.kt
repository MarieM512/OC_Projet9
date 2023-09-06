package com.openclassrooms.realestatemanager.ui.add

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.FileProvider
import coil.compose.AsyncImage
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.openclassrooms.realestatemanager.BuildConfig
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.database.Agent
import com.openclassrooms.realestatemanager.database.InterestPoint
import com.openclassrooms.realestatemanager.database.PropertyEvent
import com.openclassrooms.realestatemanager.database.PropertyState
import com.openclassrooms.realestatemanager.database.PropertyType
import com.openclassrooms.realestatemanager.theme.AppTheme
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Objects

@OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalPermissionsApi::class,
)
@Composable
fun AddScreen(
    state: PropertyState,
    onEvent: (PropertyEvent) -> Unit,
) {
    var typeExpanded by remember { mutableStateOf(false) }
    var agentExpanded by remember { mutableStateOf(false) }
    val chip = remember { mutableStateListOf<InterestPoint>() }
    val selectedImageUris = remember { mutableStateListOf<Uri>() }
    val multiplePhotoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            if (uri != null) {
                selectedImageUris.add(uri)
                onEvent(PropertyEvent.SetPicture(uri))
            }
        },
    )
    val cameraPermissionState: PermissionState = rememberPermissionState(android.Manifest.permission.CAMERA)
    val context = LocalContext.current
    val file = context.createImageFile()
    val uri = FileProvider.getUriForFile(Objects.requireNonNull(context), BuildConfig.APPLICATION_ID + ".provider", file)
    val cameraLauncher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) {
        selectedImageUris.add(uri)
        onEvent(PropertyEvent.SetPicture(uri))
    }

    AppTheme {
        LazyColumn(
            verticalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxSize(),
            contentPadding = PaddingValues(all = 16.dp),
        ) {
            item {
                Row {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        Button(
                            onClick = {
                                if (cameraPermissionState.status.isGranted) {
                                    cameraLauncher.launch(uri)
                                } else {
                                    cameraPermissionState.launchPermissionRequest()
                                }
                            },
                        ) {
                            Icon(
                                painterResource(id = R.drawable.ic_picture),
                                contentDescription = "Take a picture",
                                modifier = Modifier
                                    .padding(vertical = 8.dp),
                            )
                        }
                        Button(
                            onClick = {
                                multiplePhotoPickerLauncher.launch(
                                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly),
                                )
                            },
                            modifier = Modifier
                                .padding(bottom = 8.dp),
                        ) {
                            Icon(
                                painterResource(id = R.drawable.ic_image),
                                contentDescription = "Add a picture",
                                modifier = Modifier
                                    .padding(vertical = 8.dp),
                            )
                        }
                    }
                    Divider(
                        color = MaterialTheme.colorScheme.secondary,
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .height(120.dp)
                            .width(2.dp),
                    )
                    LazyRow() {
                        item {
                            selectedImageUris.forEach { uri ->
                                Box(
                                    contentAlignment = Alignment.TopEnd,
                                    modifier = Modifier
                                        .padding(end = 4.dp),
                                ) {
                                    AsyncImage(
                                        model = uri,
                                        contentDescription = null,
                                        modifier = Modifier
                                            .height(120.dp),
                                    )
                                    SmallFloatingActionButton(
                                        containerColor = MaterialTheme.colorScheme.secondary,
                                        modifier = Modifier
                                            .padding(4.dp)
                                            .size(20.dp),
                                        shape = CircleShape,
                                        onClick = {
                                            selectedImageUris.remove(uri)
                                            onEvent(PropertyEvent.SetPicture(uri))
                                        },
                                    ) {
                                        Icon(Icons.Filled.Clear, "Delete picture")
                                    }
                                }
                            }
                        }
                    }
                }
                LazyRow {
                    item {
                        InterestPoint.values().forEach { interest ->
                            FilterChip(
                                onClick = {
                                    if (chip.contains(interest)) chip.remove(interest) else chip.add(interest)
                                    onEvent(PropertyEvent.SetNearInterestPoint(interest))
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
                            value = state.type.label,
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
                                        onEvent(PropertyEvent.SetType(type))
                                        typeExpanded = false
                                    },
                                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                                )
                            }
                        }
                    }
                    TextField(
                        value = state.price.toString(),
                        onValueChange = {
                            onEvent(PropertyEvent.SetPrice(it.toInt()))
                        },
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
                        value = state.surface.toString(),
                        onValueChange = {
                            onEvent(PropertyEvent.SetSurface(it.toInt()))
                        },
                        label = { Text(stringResource(id = R.string.surface)) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Next),
                        singleLine = true,
                        modifier = Modifier.weight(1f),
                    )
                    TextField(
                        value = state.pieceNumber.toString(),
                        onValueChange = {
                            onEvent(PropertyEvent.SetPieceNumber(it.toInt()))
                        },
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
                        value = state.agent.label,
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
                                    onEvent(PropertyEvent.SetAgent(agent))
                                    agentExpanded = false
                                },
                                contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                            )
                        }
                    }
                }
                TextField(
                    value = state.address,
                    onValueChange = {
                        onEvent(PropertyEvent.SetAddress(it))
                    },
                    label = { Text(stringResource(id = R.string.address)) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text, imeAction = ImeAction.Next),
                    modifier = Modifier
                        .padding(vertical = 8.dp)
                        .fillMaxWidth(),
                )
                TextField(
                    value = state.description,
                    onValueChange = {
                        onEvent(PropertyEvent.SetDescription(it))
                    },
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
                        onClick = {
                            selectedImageUris.clear()
                            chip.clear()
                            onEvent(PropertyEvent.SaveProperty)
                        },
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

@SuppressLint("SimpleDateFormat")
fun Context.createImageFile(): File {
    val timeStamp = SimpleDateFormat("ddMMyyyy_HHmmss").format(Date())
    val imageFileName = "JPEG_" + timeStamp + "_"
    return File.createTempFile(
        imageFileName,
        ".jpg",
        externalCacheDir,
    )
}
