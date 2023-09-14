package com.openclassrooms.realestatemanager.ui.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.database.PropertyEvent
import com.openclassrooms.realestatemanager.database.PropertyState
import com.openclassrooms.realestatemanager.theme.AppTheme
import com.openclassrooms.realestatemanager.ui.composant.carousel.Carousel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListScreen(
    state: PropertyState,
    onEvent: (PropertyEvent) -> Unit,
    navController: NavController,
) {
    val context = LocalContext.current

    AppTheme() {
        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 300.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(all = 16.dp),
        ) {
            items(state.property) { property ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth(),
                    elevation = CardDefaults.elevatedCardElevation(),
                    shape = RoundedCornerShape(16.dp),
                    onClick = { navController.navigate("property/$property") },
                ) {
                    Column(
                        modifier = Modifier
                            .padding(18.dp),
                    ) {
                        Carousel(context, property)
                        Column(
                            modifier = Modifier
                                .padding(vertical = 8.dp)
                                .fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                        ) {
                            Text(text = property.type.label)
                            Row() {
                                Column(
                                    modifier = Modifier
                                        .weight(1f),
                                ) {
                                    Text(text = "Surface: ${property.surface}")
                                    Text(text = "Piece: ${property.pieceNumber}")
                                }
                                Column(
                                    modifier = Modifier
                                        .weight(1f),
                                    horizontalAlignment = Alignment.End,
                                ) {
                                    Text(text = "${property.price} $")
                                    Row(
                                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                                    ) {
                                        Icon(painterResource(id = R.drawable.ic_address), contentDescription = "address")
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
}
