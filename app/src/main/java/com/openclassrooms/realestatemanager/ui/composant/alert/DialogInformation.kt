package com.openclassrooms.realestatemanager.ui.composant.alert

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.res.stringResource
import com.openclassrooms.realestatemanager.R

@Composable
fun DialogInformation(title: String, message: String, openDialog: MutableState<Boolean>) {
    AlertDialog(
        onDismissRequest = {},
        title = {
            Text(title)
        },
        text = {
            Text(message)
        },
        confirmButton = {
            Button(
                onClick = {
                    openDialog.value = false
                },
            ) {
                Text(stringResource(id = R.string.button_got_it))
            }
        },
    )
}
