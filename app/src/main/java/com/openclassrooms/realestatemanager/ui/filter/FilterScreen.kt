package com.openclassrooms.realestatemanager.ui.filter

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.openclassrooms.realestatemanager.database.PropertyEvent
import com.openclassrooms.realestatemanager.database.PropertyState
import com.openclassrooms.realestatemanager.database.SortType
import com.openclassrooms.realestatemanager.theme.AppTheme
import com.openclassrooms.realestatemanager.ui.composant.card.CardFilterComparison

@Composable
fun FilterScreen(
    state: PropertyState,
    onEvent: (PropertyEvent) -> Unit,
) {
    AppTheme {
        Column(
            modifier = Modifier.padding(16.dp),
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(bottom = 8.dp)
                    .fillMaxWidth(),
            ) {
                Button(
                    onClick = {
                        onEvent(PropertyEvent.SortProperty(SortType.RESET))
                        onEvent(PropertyEvent.ResetFilter)
                    },
                ) {
                    Text("Reset")
                }
                Text("Filters")
                Button(
                    onClick = {
                        onEvent(PropertyEvent.SortProperty(SortType.SURFACE))
                    },
                ) {
                    Text("Apply")
                }
            }
            LazyColumn {
                item {
                    CardFilterComparison(title = "Surface", min = state.minSurface, max = state.maxSurface, minEvent = { onEvent(PropertyEvent.FilterBySurfaceMin(it.toInt())) }, maxEvent = { onEvent(PropertyEvent.FilterBySurfaceMax(it.toInt())) })
                }
            }
        }
    }
}
