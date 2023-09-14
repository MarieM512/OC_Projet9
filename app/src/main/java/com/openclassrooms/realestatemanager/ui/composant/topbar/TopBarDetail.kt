package com.openclassrooms.realestatemanager.ui.composant.topbar

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
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
import com.openclassrooms.realestatemanager.database.Property
import com.openclassrooms.realestatemanager.database.Status

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBarDetail(
    property: Property,
    navController: NavController,
) {
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
        actions = {
            if (property.status != Status.SOLD) {
                IconButton(onClick = { navController.navigate("edit/$property") }) {
                    Icon(
                        imageVector = Icons.Filled.Edit,
                        contentDescription = "Edit property",
                        tint = Color.Black,
                    )
                }
            }
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = MaterialTheme.colorScheme.primary),
    )
}
