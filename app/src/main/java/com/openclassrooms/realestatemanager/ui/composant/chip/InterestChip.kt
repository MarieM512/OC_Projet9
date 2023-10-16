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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.database.PropertyEvent
import com.openclassrooms.realestatemanager.database.utils.InterestPoint

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InterestChip(
    chip: SnapshotStateList<String>,
    onEvent: (PropertyEvent) -> Unit,
    isFilter: Boolean,
) {
    LazyRow {
        item {
            InterestPoint.values().forEach { interest ->
                FilterChip(
                    onClick = {
                        if (isFilter) {
                            if (chip.contains(interest.label)) {
                                chip.remove(interest.label)
                            } else if (chip.size != 3) {
                                chip.add(interest.label)
                            }
                            onEvent(PropertyEvent.FilterByNear(interest.label))
                        } else {
                            if (chip.contains(interest.label)) {
                                chip.remove(interest.label)
                            } else {
                                chip.add(interest.label)
                            }
                            onEvent(PropertyEvent.SetNearInterestPoint(interest.label))
                        }
                    },
                    label = { Text(interest.label) },
                    selected = chip.contains(interest.label),
                    leadingIcon = if (chip.contains(interest.label)) {
                        {
                            Icon(
                                imageVector = Icons.Filled.Done,
                                contentDescription = stringResource(id = R.string.done),
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
