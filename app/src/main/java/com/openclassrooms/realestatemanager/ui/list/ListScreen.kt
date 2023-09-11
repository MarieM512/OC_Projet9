package com.openclassrooms.realestatemanager.ui.list

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.database.PropertyEvent
import com.openclassrooms.realestatemanager.database.PropertyState
import com.openclassrooms.realestatemanager.theme.AppTheme

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun ListScreen(
    state: PropertyState,
    onEvent: (PropertyEvent) -> Unit,
) {
    val context = LocalContext.current

    AppTheme() {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .fillMaxSize(),
            contentPadding = PaddingValues(all = 16.dp),
        ) {
            item {
                Button(
                    onClick = {
                        onEvent(PropertyEvent.DeleteAllProperty)
                    },

                ) {
                    Text("text")
                }
            }
            items(state.property) { property ->
                val pagerState = rememberPagerState(pageCount = {
                    property.uriPicture.size
                })
                Card(
                    modifier = Modifier
                        .fillMaxWidth(),
                    elevation = CardDefaults.elevatedCardElevation(),
                    shape = RoundedCornerShape(16.dp),
                    onClick = {
                        println("hey")
                    },
                ) {
                    Column(
                        modifier = Modifier
                            .padding(18.dp),
                    ) {
                        Box(
                            contentAlignment = Alignment.BottomCenter,
                            modifier = Modifier
                                .clip(RoundedCornerShape(10.dp)),
                        ) {
                            HorizontalPager(state = pagerState) { page ->
                                Box() {
                                    AsyncImage(
                                        model = ImageRequest.Builder(context)
                                            .data(property.uriPicture[page].toUri()).crossfade(true).build(),
                                        contentDescription = null,
                                        modifier = Modifier
                                            .fillParentMaxWidth(),
                                        contentScale = ContentScale.Crop,
                                    )
                                    Text(
                                        text = property.titlePicture[page],
                                        modifier = Modifier
                                            .background(MaterialTheme.colorScheme.outline)
                                            .fillMaxWidth(),
                                        textAlign = TextAlign.Center,
                                        color = MaterialTheme.colorScheme.surfaceTint,
                                        fontSize = 20.sp,
                                    )
                                }
                            }
                            Row(
                                modifier = Modifier
                                    .padding(bottom = 16.dp)
                                    .fillMaxWidth(),
                                horizontalArrangement = Arrangement.Center,
                            ) {
                                repeat(property.uriPicture.size) { iteration ->
                                    val color = if (pagerState.currentPage == iteration) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline
                                    Box(
                                        modifier = Modifier
                                            .padding(2.dp)
                                            .clip(CircleShape)
                                            .background(color)
                                            .size(10.dp),
                                    )
                                }
                            }
                        }
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
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                    ) {
                                        Text(text = "Surface: ${property.surface}")
                                        Text(text = "Piece: ${property.pieceNumber}")
                                    }
                                    Row(
                                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                                    ) {
                                        Icon(painterResource(id = R.drawable.ic_address), contentDescription = "address")
                                        Text(text = property.address)
                                    }
                                }
                                Column(
                                    modifier = Modifier
                                        .weight(1f),
                                    horizontalAlignment = Alignment.End,
                                ) {
                                    Text(text = "${property.price} $")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
