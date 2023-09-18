package com.openclassrooms.realestatemanager.ui.composant.chip

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.openclassrooms.realestatemanager.database.InterestPoint
import com.openclassrooms.realestatemanager.database.PropertyEvent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InterestChip(
    chip: SnapshotStateList<InterestPoint>,
    onEvent: (PropertyEvent) -> Unit,
) {
    LazyRow {
        item {
            InterestPoint.values().forEach { interest ->
                FilterChip(
                    onClick = {
                        if (chip.contains(interest)) {
                            chip.remove(interest)
                        } else {
                            chip.add(interest)
                        }
                        onEvent(PropertyEvent.SetNearInterestPoint(interest))
                    },
                    label = { Text(interest.label) },
                    selected = chip.contains(interest),
                    leadingIcon = if (chip.contains(interest)) {
                        {
                            Icon(
                                imageVector = Icons.Filled.Done,
                                contentDescription = "Done icon",
                                modifier = Modifier.size(FilterChipDefaults.IconSize),
                            )
                        }
                    } else {
                        null
                    },
                    modifier = Modifier.padding(horizontal = 4.dp),
                )
            }
        }
    }
}
