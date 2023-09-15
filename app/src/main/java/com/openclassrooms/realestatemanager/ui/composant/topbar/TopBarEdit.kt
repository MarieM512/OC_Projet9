package com.openclassrooms.realestatemanager.ui.composant.topbar

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.navigation.NavController
import com.openclassrooms.realestatemanager.database.PropertyEvent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBarEdit(
    onEvent: (PropertyEvent) -> Unit,
    navController: NavController,
    id: Int,
) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                "Edition",
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        },
        navigationIcon = {
            IconButton(onClick = { navController.navigateUp() }) {
                Icon(
                    imageVector = Icons.Filled.Close,
                    contentDescription = "Cancel",
                )
            }
        },
        actions = {
            IconButton(onClick = {
                onEvent(PropertyEvent.SaveProperty(id))
                navController.navigateUp()
            }) {
                Icon(
                    imageVector = Icons.Filled.Edit,
                    contentDescription = "Save",
                    tint = Color.Black,
                )
            }
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = MaterialTheme.colorScheme.primary),
    )
}
