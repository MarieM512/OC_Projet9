package com.openclassrooms.realestatemanager.ui.add

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.pm.PackageManager
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
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
import androidx.compose.material3.AlertDialog
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
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.FileProvider
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.openclassrooms.realestatemanager.BuildConfig
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.ViewModel.PropertyViewModel
import com.openclassrooms.realestatemanager.database.PropertyEvent
import com.openclassrooms.realestatemanager.database.PropertyState
import com.openclassrooms.realestatemanager.database.entity.Property
import com.openclassrooms.realestatemanager.database.utils.Agent
import com.openclassrooms.realestatemanager.database.utils.PropertyType
import com.openclassrooms.realestatemanager.database.utils.Status
import com.openclassrooms.realestatemanager.model.Address
import com.openclassrooms.realestatemanager.theme.AppTheme
import com.openclassrooms.realestatemanager.ui.composant.alert.DialogInformation
import com.openclassrooms.realestatemanager.ui.composant.bottomNavigation.BottomNavItem
import com.openclassrooms.realestatemanager.ui.composant.button.Pictures
import com.openclassrooms.realestatemanager.ui.composant.chip.InterestChip
import com.openclassrooms.realestatemanager.ui.composant.image.DisplayImage
import com.openclassrooms.realestatemanager.ui.composant.topbar.TopBarEdit
import com.openclassrooms.realestatemanager.utils.ImageSave
import com.openclassrooms.realestatemanager.utils.Notification
import com.openclassrooms.realestatemanager.utils.Utils
import java.text.SimpleDateFormat
import java.util.Date

@SuppressLint("SimpleDateFormat")
@OptIn(
    ExperimentalMaterial3Api::class,
)
@Composable
fun AddScreen(
    state: PropertyState,
    viewModel: PropertyViewModel,
    property: Property? = null,
    navController: NavController,
    addViewModel: AddViewModel = viewModel(),
) {
    val context = LocalContext.current
    val activityContext = LocalContext.current as Activity
    val addUiState by addViewModel.uiState.collectAsState()
    var uri: Uri? = null
    val descriptionImage = remember { mutableStateOf("") }
    var typeExpanded by remember { mutableStateOf(false) }
    var agentExpanded by remember { mutableStateOf(false) }
    val chip = remember { mutableStateListOf<String>() }
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
                viewModel.onEvent(
                    PropertyEvent.SetUriPicture(
                        ImageSave.saveImgInCache(context, uri).toString(),
                    ),
                )
                viewModel.onEvent(PropertyEvent.SetTitlePicture(descriptionImage.value))
            }
        },
    )

    val openDialogMissing = remember { mutableStateOf(false) }
    val openDialogInternet = remember { mutableStateOf(false) }

    // Permissions
    val openDialogPicture = remember { mutableStateOf(false) }
    val openDialogFolderPermission = remember { mutableStateOf(false) }
    val openDialogCameraPermission = remember { mutableStateOf(false) }
    val openDialogNotificationPermission = remember { mutableStateOf(false) }
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
                uri?.toString()?.let { PropertyEvent.SetUriPicture(it) }?.let { viewModel.onEvent(it) }
                viewModel.onEvent(PropertyEvent.SetTitlePicture(descriptionImage.value))
            }
        }

    LaunchedEffect(Unit) {
        if (!Utils.isInternetAvailable(context)) {
            openDialogInternet.value = true
        }
        if (property != null) {
            viewModel.getPicture(property.id).forEach { currentPicture ->
                selectedImageUris.add(currentPicture.uri)
                selectedImageTitles.add(currentPicture.title)
                viewModel.onEvent(PropertyEvent.SetUriPicture(currentPicture.uri))
                viewModel.onEvent(PropertyEvent.SetTitlePicture(currentPicture.title))
            }
            address = property.address
            viewModel.getNearInterestPoint(property.id).forEach { interestPoint ->
                viewModel.onEvent(PropertyEvent.SetNearInterestPoint(interestPoint))
            }
            chip.addAll(viewModel.getNearInterestPoint(property.id))
            viewModel.onEvent(PropertyEvent.SetType(property.type))
            viewModel.onEvent(PropertyEvent.SetPrice(property.price))
            viewModel.onEvent(PropertyEvent.SetSurface(property.surface))
            viewModel.onEvent(PropertyEvent.SetPieceNumber(property.pieceNumber))
            viewModel.onEvent(PropertyEvent.SetAgent(property.agent))
            viewModel.onEvent(PropertyEvent.SetAddress(property.address))
            viewModel.onEvent(PropertyEvent.SetDescription(property.description))
            viewModel.onEvent(PropertyEvent.SetLatitude(property.latitude))
            viewModel.onEvent(PropertyEvent.SetLongitude(property.longitude))
            viewModel.onEvent(PropertyEvent.SetEntryDate(property.entryDate))
        } else {
            selectedImageUris.clear()
            selectedImageTitles.clear()
            chip.clear()
            addViewModel.reset()
            viewModel.onEvent(PropertyEvent.Reset)
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
                        onEvent = viewModel::onEvent,
                        navController = navController,
                        id = property.id,
                        clear = { viewModel.onEvent(PropertyEvent.SetAddress("")) },
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
                                    AlertDialog(
                                        onDismissRequest = {},
                                        title = {
                                            Text(stringResource(id = R.string.picture_title))
                                        },
                                        text = {
                                            TextField(
                                                value = descriptionImage.value,
                                                onValueChange = { descriptionImage.value = it },
                                                label = { Text(stringResource(id = R.string.picture_description)) },
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
                                                Text(stringResource(id = R.string.button_confirm))
                                            }
                                        },
                                        dismissButton = {
                                            Button(
                                                onClick = {
                                                    openDialogPicture.value = false
                                                },
                                            ) {
                                                Text(stringResource(id = R.string.button_cancel))
                                            }
                                        },
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
                                viewModel::onEvent,
                            )
                        }
                        InterestChip(chip, viewModel::onEvent, false)
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
                                                viewModel.onEvent(PropertyEvent.SetType(type))
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
                                        viewModel.onEvent(PropertyEvent.SetPrice(0))
                                    } else if (it.isDigitsOnly()) {
                                        viewModel.onEvent(PropertyEvent.SetPrice(it.toInt()))
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
                                        viewModel.onEvent(PropertyEvent.SetSurface(0))
                                    } else if (it.isDigitsOnly()) {
                                        viewModel.onEvent(PropertyEvent.SetSurface(it.toInt()))
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
                                        viewModel.onEvent(PropertyEvent.SetPieceNumber(0))
                                    } else if (it.isDigitsOnly()) {
                                        viewModel.onEvent(PropertyEvent.SetPieceNumber(it.toInt()))
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
                                            viewModel.onEvent(PropertyEvent.SetAgent(agent))
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
                                            viewModel.onEvent(PropertyEvent.SetAddress(item.address))
                                            viewModel.onEvent(PropertyEvent.SetLatitude(item.lat))
                                            viewModel.onEvent(PropertyEvent.SetLongitude(item.lon))
                                            location.clear()
                                            addViewModel.reset()
                                        }),
                                )
                            }
                        }

                        TextField(
                            value = state.description,
                            onValueChange = {
                                viewModel.onEvent(PropertyEvent.SetDescription(it))
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
                                        viewModel.onEvent(PropertyEvent.SetStatus(Status.SOLD))
                                        viewModel.onEvent(
                                            PropertyEvent.SetSoldDate(
                                                SimpleDateFormat("yyyy-MM-dd").format(
                                                    Date(),
                                                ),
                                            ),
                                        )
                                        viewModel.onEvent(PropertyEvent.SaveProperty(property.id))
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
                                            if (ActivityCompat.checkSelfPermission(
                                                    activityContext,
                                                    Manifest.permission.POST_NOTIFICATIONS,
                                                ) != PackageManager.PERMISSION_GRANTED
                                            ) {
                                                openDialogNotificationPermission.value = true
                                            } else {
                                                NotificationManagerCompat.from(activityContext).notify(0, Notification.sendNotification(activityContext))
                                            }
                                            viewModel.onEvent(PropertyEvent.SaveProperty(-1))
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
                                DialogInformation(stringResource(id = R.string.missing_title), stringResource(id = R.string.missing_description), openDialogMissing)
                            }
                            if (openDialogInternet.value) {
                                DialogInformation(title = stringResource(id = R.string.internet_title), message = stringResource(id = R.string.internet_description), openDialog = openDialogInternet)
                            }
                            if (openDialogNotificationPermission.value) {
                                DialogInformation(title = stringResource(id = R.string.permission_title), message = stringResource(id = R.string.notification_permission_description), openDialog = openDialogNotificationPermission)
                            }
                        }
                    }
                }
            },
        )
    }
}
