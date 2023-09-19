package com.openclassrooms.realestatemanager.ui.composant.image

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.openclassrooms.realestatemanager.database.PropertyEvent

@Composable
fun DisplayImage(
    selectedImageUris: SnapshotStateList<String>,
    selectedImageTitles: SnapshotStateList<String>,
    onEvent: (PropertyEvent) -> Unit,
) {
    LazyRow {
        item {
            selectedImageUris.zip(selectedImageTitles).forEach { image ->
                Box(
                    contentAlignment = Alignment.TopEnd,
                    modifier = Modifier.padding(end = 4.dp),
                ) {
                    AsyncImage(
                        model = image.first,
                        contentDescription = null,
                        modifier = Modifier.height(120.dp),
                    )
                    SmallFloatingActionButton(
                        containerColor = MaterialTheme.colorScheme.secondary,
                        modifier = Modifier
                            .padding(4.dp)
                            .size(20.dp),
                        shape = CircleShape,
                        onClick = {
                            selectedImageUris.remove(image.first)
                            selectedImageTitles.remove(image.second)
                            onEvent(PropertyEvent.SetUriPicture(image.first))
                            onEvent(PropertyEvent.SetTitlePicture(image.second))
                        },
                    ) {
                        Icon(Icons.Filled.Clear, "Delete picture")
                    }
                    Text(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .background(MaterialTheme.colorScheme.secondary)
                            .fillMaxWidth()
                            .width(90.dp),
                        text = image.second,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        color = Color.White,
                        textAlign = TextAlign.Center,
                    )
                }
            }
        }
    }
}
