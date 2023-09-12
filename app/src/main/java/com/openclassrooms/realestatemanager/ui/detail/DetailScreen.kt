package com.openclassrooms.realestatemanager.ui.detail

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.openclassrooms.realestatemanager.database.Property
import com.openclassrooms.realestatemanager.theme.AppTheme

@Composable
fun DetailScreen(
    property: Property
) {
    AppTheme() {
        Column() {
            Text(property.address)
            Text(property.description)
            Text(property.uriPicture.size.toString())
        }
    }
}