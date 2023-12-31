package com.openclassrooms.realestatemanager.ui.list

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.ViewModel.PropertyViewModel
import com.openclassrooms.realestatemanager.database.PropertyState
import com.openclassrooms.realestatemanager.database.entity.Property
import com.openclassrooms.realestatemanager.theme.AppTheme
import com.openclassrooms.realestatemanager.ui.detail.DetailTabletScreen

@SuppressLint("UnrememberedMutableState")
@Composable
fun ListTabletScreen(
    state: PropertyState,
    navController: NavController,
    windowSizeClass: WindowSizeClass,
    viewModel: PropertyViewModel,
) {
    val property: MutableState<Property?> = remember { mutableStateOf(null) }
    AppTheme {
        Row {
            Column(
                modifier = Modifier
                    .weight(1f),
            ) {
                property.value = ListScreen(
                    state = state,
                    navController = navController,
                    windowSizeClass = windowSizeClass,
                    viewModel = viewModel,
                )
            }
            Column(
                modifier = Modifier
                    .weight(2f),
            ) {
                if (property.value == null) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                    ) {
                        Icon(painterResource(id = R.drawable.ic_house), contentDescription = stringResource(R.string.home))
                        Text(stringResource(R.string.select_property))
                    }
                } else {
                    property.value?.let { DetailTabletScreen(it, navController, viewModel.getNearInterestPoint(it.id), viewModel) }
                }
            }
        }
    }
}
