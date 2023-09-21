package com.openclassrooms.realestatemanager.ui.add

import android.annotation.SuppressLint
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.database.Agent
import com.openclassrooms.realestatemanager.database.InterestPoint
import com.openclassrooms.realestatemanager.database.Property
import com.openclassrooms.realestatemanager.database.PropertyEvent
import com.openclassrooms.realestatemanager.database.PropertyState
import com.openclassrooms.realestatemanager.database.PropertyType
import com.openclassrooms.realestatemanager.database.Status
import com.openclassrooms.realestatemanager.model.Address
import com.openclassrooms.realestatemanager.theme.AppTheme
import com.openclassrooms.realestatemanager.ui.composant.alert.DialogInformation
import com.openclassrooms.realestatemanager.ui.composant.alert.DialogPicture
import com.openclassrooms.realestatemanager.ui.composant.bottomNavigation.BottomNavItem
import com.openclassrooms.realestatemanager.ui.composant.button.Pictures
import com.openclassrooms.realestatemanager.ui.composant.chip.InterestChip
import com.openclassrooms.realestatemanager.ui.composant.image.DisplayImage
import com.openclassrooms.realestatemanager.ui.composant.topbar.TopBarEdit
import com.openclassrooms.realestatemanager.utils.ImageSave
import java.text.SimpleDateFormat
import java.util.Date

@SuppressLint("SimpleDateFormat")
@OptIn(
    ExperimentalMaterial3Api::class,
)
@Composable
fun AddScreen(
    state: PropertyState,
    onEvent: (PropertyEvent) -> Unit,
    property: Property? = null,
    navController: NavController,
    addViewModel: AddViewModel = viewModel(),
) {
    val context = LocalContext.current
    val addUiState by addViewModel.uiState.collectAsState()
    var uri: Uri? = null
    val descriptionImage = remember { mutableStateOf("") }
    var typeExpanded by remember { mutableStateOf(false) }
    var agentExpanded by remember { mutableStateOf(false) }
    val chip = remember { mutableStateListOf<InterestPoint>() }
    val selectedImageUris = remember { mutableStateListOf<String>() }
    val selectedImageTitles = remember { mutableStateListOf<String>() }
    val location = remember { mutableStateListOf<Address>() }
    var address by remember { mutableStateOf("") }

    val multiplePhotoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            if (uri != null) {
                selectedImageUris.add(uri.toString())
                selectedImageTitles.add(descriptionImage.value)
                onEvent(
                    PropertyEvent.SetUriPicture(
                        ImageSave.saveImgInCache(context, uri).toString(),
                    ),
                )
                onEvent(PropertyEvent.SetTitlePicture(descriptionImage.value))
            }
        },
    )

    val openDialogMissing = remember { mutableStateOf(false) }
    val openDialogSuccess = remember { mutableStateOf(false) }

    // Permissions
    val openDialogPicture = remember { mutableStateOf(false) }
    val openDialogFolderPermission = remember { mutableStateOf(false) }
    val openDialogCameraPermission = remember { mutableStateOf(false) }
    val takePicture = remember { mutableStateOf(false) }
    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission(),
    ) { isGranted ->
        if (isGranted) {
            takePicture.value = true
            openDialogPicture.value = true
        } else {
            openDialogCameraPermission.value = true
        }
    }
    val folderPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission(),
    ) { isGranted ->
        if (isGranted) {
            openDialogPicture.value = true
        } else {
            openDialogFolderPermission.value = true
        }
    }

    val cameraLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { taken ->
            if (taken) {
                uri.let { it1 -> selectedImageUris.add(it1.toString()) }
                selectedImageTitles.add(descriptionImage.value)
                uri?.toString()?.let { PropertyEvent.SetUriPicture(it) }?.let { onEvent(it) }
                onEvent(PropertyEvent.SetTitlePicture(descriptionImage.value))
            }
        }

    LaunchedEffect(Unit) {
        if (property != null) {
            property.uriPicture.let { selectedImageUris.addAll(it) }
            property.titlePicture.let { selectedImageTitles.addAll(it) }
            address = property.address
            property.nearInterestPoint.forEach { interestPoint ->
                onEvent(PropertyEvent.SetNearInterestPoint(interestPoint))
            }
            property.nearInterestPoint.let { chip.addAll(it) }
            property.uriPicture.zip(property.titlePicture).forEach { picture ->
                onEvent(PropertyEvent.SetUriPicture(picture.first))
                onEvent(PropertyEvent.SetTitlePicture(picture.second))
            }

            onEvent(PropertyEvent.SetType(property.type))
            onEvent(PropertyEvent.SetPrice(property.price))
            onEvent(PropertyEvent.SetSurface(property.surface))
            onEvent(PropertyEvent.SetPieceNumber(property.pieceNumber))
            onEvent(PropertyEvent.SetAgent(property.agent))
            onEvent(PropertyEvent.SetAddress(property.address))
            onEvent(PropertyEvent.SetDescription(property.description))
            onEvent(PropertyEvent.SetLatitude(property.latitude))
            onEvent(PropertyEvent.SetLongitude(property.longitude))
        } else {
            selectedImageUris.clear()
            selectedImageTitles.clear()
            chip.clear()
            addViewModel.reset()
            onEvent(PropertyEvent.Reset)
        }
    }

    LaunchedEffect(addUiState.addressList) {
        location.clear()
        location.addAll(addUiState.addressList)
    }

    AppTheme {
        Scaffold(
            topBar = {
                if (property != null) {
                    TopBarEdit(
                        onEvent = onEvent,
                        navController = navController,
                        id = property.id,
                        clear = { onEvent(PropertyEvent.SetAddress("")) },
                    )
                }
            },
            content = { innerPadding ->
                LazyColumn(
                    verticalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxSize(),
                    contentPadding = innerPadding,
                ) {
                    item {
                        Row {
                            Column(
                                verticalArrangement = Arrangement.spacedBy(8.dp),
                            ) {
                                Pictures(
                                    context,
                                    cameraPermissionLauncher,
                                    folderPermissionLauncher,
                                    openDialogPicture,
                                    takePicture,
                                    openDialogCameraPermission,
                                    openDialogFolderPermission,
                                )
                                if (openDialogPicture.value) {
                                    descriptionImage.value = ""
                                    DialogPicture(
                                        context,
                                        descriptionImage,
                                        takePicture,
                                        uri,
                                        cameraLauncher,
                                        multiplePhotoPickerLauncher,
                                        openDialogPicture,
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
                            DisplayImage(
                                selectedImageUris,
                                selectedImageTitles,
                                onEvent,
                            )
                        }
                        InterestChip(chip, onEvent)
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
                                    trailingIcon = {
                                        ExposedDropdownMenuDefaults.TrailingIcon(
                                            expanded = typeExpanded,
                                        )
                                    },
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
                                    if (it.isEmpty()) {
                                        onEvent(PropertyEvent.SetPrice(0))
                                    } else if (it.isDigitsOnly()) {
                                        onEvent(PropertyEvent.SetPrice(it.toInt()))
                                    }
                                },
                                label = { Text(stringResource(id = R.string.price)) },
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Number,
                                    imeAction = ImeAction.Next,
                                ),
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
                                    if (it.isEmpty()) {
                                        onEvent(PropertyEvent.SetSurface(0))
                                    } else if (it.isDigitsOnly()) {
                                        onEvent(PropertyEvent.SetSurface(it.toInt()))
                                    }
                                },
                                label = { Text(stringResource(id = R.string.surface)) },
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Number,
                                    imeAction = ImeAction.Next,
                                ),
                                singleLine = true,
                                modifier = Modifier.weight(1f),
                            )
                            TextField(
                                value = state.pieceNumber.toString(),
                                onValueChange = {
                                    if (it.isEmpty()) {
                                        onEvent(PropertyEvent.SetPieceNumber(0))
                                    } else if (it.isDigitsOnly()) {
                                        onEvent(PropertyEvent.SetPieceNumber(it.toInt()))
                                    }
                                },
                                label = { Text(stringResource(id = R.string.piece_number)) },
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Number,
                                    imeAction = ImeAction.Next,
                                ),
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
                            value = address,
                            onValueChange = {
                                address = it
                                addViewModel.updateAddress(it)
                                if (addViewModel.uiState.value.address.length >= 3) {
                                    addViewModel.getAddressList()
                                }
                            },
                            label = { Text(stringResource(id = R.string.address)) },
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Text,
                                imeAction = ImeAction.Next,
                            ),
                            modifier = Modifier
                                .padding(vertical = 8.dp)
                                .fillMaxWidth(),
                        )
                        if (location.isNotEmpty()) {
                            location.forEach { item ->
                                Text(
                                    text = item.address,
                                    modifier = Modifier
                                        .padding(4.dp)
                                        .clickable(onClick = {
                                            address = item.address
                                            onEvent(PropertyEvent.SetAddress(item.address))
                                            onEvent(PropertyEvent.SetLatitude(item.lat))
                                            onEvent(PropertyEvent.SetLongitude(item.lon))
                                            location.clear()
                                            addViewModel.reset()
                                        }),
                                )
                            }
                        }

                        TextField(
                            value = state.description,
                            onValueChange = {
                                onEvent(PropertyEvent.SetDescription(it))
                            },
                            label = { Text(stringResource(id = R.string.description)) },
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Text,
                                imeAction = ImeAction.Done,
                            ),
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
                                    if (property != null) {
                                        onEvent(PropertyEvent.SetStatus(Status.SOLD))
                                        onEvent(
                                            PropertyEvent.SetSoldDate(
                                                SimpleDateFormat("dd/MM/yyyy").format(
                                                    Date(),
                                                ),
                                            ),
                                        )
                                        onEvent(PropertyEvent.SaveProperty(property.id))
                                        navController.navigate(BottomNavItem.List.route)
                                    } else {
                                        if (state.address.isEmpty() || state.description.isEmpty() || state.uriPicture.isEmpty() || state.price == 0 || state.surface == 0 || state.pieceNumber == 0) {
                                            openDialogMissing.value = true
                                        } else {
                                            selectedImageUris.clear()
                                            selectedImageTitles.clear()
                                            chip.clear()
                                            address = ""
                                            location.clear()
                                            openDialogSuccess.value = true
                                            onEvent(PropertyEvent.SaveProperty(-1))
                                        }
                                    }
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .align(Alignment.BottomCenter)
                                    .size(50.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor =
                                    if (property != null) {
                                        MaterialTheme.colorScheme.error
                                    } else {
                                        MaterialTheme.colorScheme.primary
                                    },
                                ),
                            ) {
                                Text(
                                    text = stringResource(
                                        id =
                                        if (property != null) {
                                            R.string.sold
                                        } else {
                                            R.string.add
                                        },
                                    ),
                                    textAlign = TextAlign.Center,
                                    fontSize = 16.sp,
                                )
                            }
                            if (openDialogMissing.value) {
                                DialogInformation("Missing information", "Please be sure to fill all the information with at least one picture.", openDialogMissing)
                            }
                            if (openDialogSuccess.value) {
                                DialogInformation("Success", "You have successfully add your property.", openDialogSuccess)
                            }
                        }
                    }
                }
            },
        )
    }
}
