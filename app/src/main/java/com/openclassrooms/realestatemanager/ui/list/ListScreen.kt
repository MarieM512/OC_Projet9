package com.openclassrooms.realestatemanager.ui.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.ViewModel.PropertyViewModel
import com.openclassrooms.realestatemanager.database.entity.Property
import com.openclassrooms.realestatemanager.database.PropertyState
import com.openclassrooms.realestatemanager.database.utils.Status
import com.openclassrooms.realestatemanager.theme.AppTheme
import com.openclassrooms.realestatemanager.ui.composant.carousel.Carousel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListScreen(
    state: PropertyState,
    navController: NavController,
    windowSizeClass: WindowSizeClass,
    viewModel: PropertyViewModel,
): Property? {
    val context = LocalContext.current
    val propertyClick: MutableState<Property?> = remember { mutableStateOf(null) }

    AppTheme() {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(all = 16.dp),
        ) {
            items(state.property) { property ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth(),
                    elevation = CardDefaults.elevatedCardElevation(),
                    shape = RoundedCornerShape(16.dp),
                    onClick = {
                        if (windowSizeClass.widthSizeClass == WindowWidthSizeClass.Compact) {
                            navController.navigate("property/$property")
                        } else {
                            propertyClick.value = property
                        }
                    },
                    colors = CardDefaults.cardColors(
                        containerColor = if (property.status == Status.SOLD) MaterialTheme.colorScheme.errorContainer else MaterialTheme.colorScheme.primaryContainer,
                    ),
                ) {
                    Column(
                        modifier = Modifier
                            .padding(18.dp),
                    ) {
                        Carousel(context, property, viewModel)
                        Column(
                            modifier = Modifier
                                .padding(vertical = 8.dp)
                                .fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                        ) {
                            Text(text = property.type.label)
                            Row {
                                Column(
                                    modifier = Modifier
                                        .weight(1f),
                                ) {
                                    Text(text = stringResource(id = R.string.detail_surface, property.surface))
                                    Text(text = stringResource(id = R.string.detail_piece, property.pieceNumber))
                                }
                                Column(
                                    modifier = Modifier
                                        .weight(1f),
                                    horizontalAlignment = Alignment.End,
                                ) {
                                    Text(text = stringResource(id = R.string.detail_price, property.price))
                                    Row(
                                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                                    ) {
                                        Icon(painterResource(id = R.drawable.ic_address), contentDescription = stringResource(R.string.address))
                                        Text(text = property.address)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    return propertyClick.value
}
