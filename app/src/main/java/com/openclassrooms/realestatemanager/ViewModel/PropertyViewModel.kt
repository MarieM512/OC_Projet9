package com.openclassrooms.realestatemanager.ViewModel

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.openclassrooms.realestatemanager.database.utils.Agent
import com.openclassrooms.realestatemanager.database.entity.NearInterestPoint
import com.openclassrooms.realestatemanager.database.dao.NearInterestPointDao
import com.openclassrooms.realestatemanager.database.entity.Property
import com.openclassrooms.realestatemanager.database.dao.PropertyDao
import com.openclassrooms.realestatemanager.database.PropertyEvent
import com.openclassrooms.realestatemanager.database.PropertyState
import com.openclassrooms.realestatemanager.database.utils.PropertyType
import com.openclassrooms.realestatemanager.database.SortType
import com.openclassrooms.realestatemanager.database.dao.PictureDao
import com.openclassrooms.realestatemanager.database.entity.Picture
import com.openclassrooms.realestatemanager.database.utils.PictureTuple
import com.openclassrooms.realestatemanager.database.utils.Status
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date

class PropertyViewModel(
    private val propertyDao: PropertyDao,
    private val nearDao: NearInterestPointDao,
    private val pictureDao: PictureDao,
) : ViewModel() {

    private val _sortType = MutableStateFlow(SortType.RESET)
    private val _properties = _sortType
        .flatMapLatest { sortType ->
            when (sortType) {
                SortType.RESET -> propertyDao.getAllProperties()
                SortType.FILTER -> propertyDao.getPropertyFiltered(
                    _state.value.minSurface, _state.value.maxSurface,
                    _state.value.minPrice, _state.value.maxPrice,
                    _state.value.filterAgent,
                    _state.value.filterAddress,
                    _state.value.filterType,
                    _state.value.minPiece,
                    _state.value.maxPiece,
                )
            }
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    private val _state = MutableStateFlow(PropertyState())

    val state = combine(_state, _sortType, _properties) { state, sortType, properties ->
        state.copy(
            property = properties,
            sortType = sortType,
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), PropertyState())

    @SuppressLint("SimpleDateFormat")
    fun onEvent(event: PropertyEvent) {
        when (event) {
            is PropertyEvent.FilterBySurfaceMin -> {
                _state.update {
                    it.copy(
                        minSurface = event.min,
                    )
                }
            }

            is PropertyEvent.FilterBySurfaceMax -> {
                _state.update {
                    it.copy(
                        maxSurface = event.max,
                    )
                }
            }

            is PropertyEvent.FilterByPriceMin -> {
                _state.update {
                    it.copy(
                        minPrice = event.min,
                    )
                }
            }

            is PropertyEvent.FilterByPriceMax -> {
                _state.update {
                    it.copy(
                        maxPrice = event.max,
                    )
                }
            }

            is PropertyEvent.FilterByAgent -> {
                _state.update {
                    it.copy(
                        filterAgent = event.agent,
                    )
                }
            }

            is PropertyEvent.FilterByAddress -> {
                _state.update {
                    it.copy(
                        filterAddress = event.address,
                    )
                }
            }

            is PropertyEvent.FilterByType -> {
                _state.update {
                    it.copy(
                        filterType = event.type,
                    )
                }
            }

            is PropertyEvent.FilterByPieceMin -> {
                _state.update {
                    it.copy(
                        minPiece = event.min,
                    )
                }
            }

            is PropertyEvent.FilterByPieceMax -> {
                _state.update {
                    it.copy(
                        maxPiece = event.max,
                    )
                }
            }

            PropertyEvent.ResetFilter -> {
                _state.update {
                    it.copy(
                        minSurface = 0,
                        maxSurface = 10000,
                        minPrice = 0,
                        maxPrice = 1000000000,
                        filterAgent = null,
                        filterAddress = "",
                        filterType = null,
                        minPiece = 0,
                        maxPiece = 1000,
                    )
                }
            }

            PropertyEvent.Reset -> {
                _state.update {
                    it.copy(
                        type = PropertyType.HOUSE,
                        price = 0,
                        surface = 0,
                        pieceNumber = 0,
                        description = "",
                        uriPicture = mutableListOf(),
                        titlePicture = mutableListOf(),
                        address = "",
                        latitude = 0.0,
                        longitude = 0.0,
                        nearInterestPoint = mutableListOf(),
                        status = Status.AVAILABLE,
                        entryDate = "",
                        soldDate = "",
                        agent = Agent.STEPHANE_PLAZA,
                    )
                }
            }

            is PropertyEvent.SaveProperty -> {
                val id = event.id
                val type = _state.value.type
                val price = _state.value.price
                val surface = _state.value.surface
                val pieceNumber = _state.value.pieceNumber
                val description = _state.value.description
                val uriPicture = _state.value.uriPicture
                val titlePicture = _state.value.titlePicture
                val address = _state.value.address
                val latitude = _state.value.latitude
                val longitude = _state.value.longitude
                val nearInterestPoint = _state.value.nearInterestPoint
                val status = _state.value.status
                val soldDate = _state.value.soldDate
                val agent = _state.value.agent

                val property: Property

                if (id != -1) {
                    property = Property(
                        id = id,
                        type = type,
                        price = price,
                        surface = surface,
                        pieceNumber = pieceNumber,
                        description = description,
                        address = address,
                        latitude = latitude,
                        longitude = longitude,
                        status = status,
                        entryDate = SimpleDateFormat("dd/MM/yyyy").format(Date()),
                        soldDate = soldDate,
                        agent = agent,
                    )
                } else {
                    property = Property(
                        type = type,
                        price = price,
                        surface = surface,
                        pieceNumber = pieceNumber,
                        description = description,
                        address = address,
                        latitude = latitude,
                        longitude = longitude,
                        status = status,
                        entryDate = SimpleDateFormat("dd/MM/yyyy").format(Date()),
                        soldDate = soldDate,
                        agent = agent,
                    )
                }
                viewModelScope.launch {
                    val propertyId = propertyDao.upsertProperty(property).toInt()
                    if (propertyId != -1) {
                        nearInterestPoint.forEach { nearInterestPoint ->
                            val near = NearInterestPoint(propertyId = propertyId, nearInterestPoint = nearInterestPoint)
                            nearDao.insertNearInterestPoint(near)
                        }
                        uriPicture.zip(titlePicture).forEach { currentPicture ->
                            val picture = Picture(propertyId = propertyId, uri = currentPicture.first, title = currentPicture.second)
                            pictureDao.insertPicture(picture)
                        }
                    } else {
                        val nearDb = getNearInterestPoint(id)
                        nearInterestPoint.forEach { nearInterestPoint ->
                            if (!nearDb.contains(nearInterestPoint)) {
                                val near = NearInterestPoint(propertyId = id, nearInterestPoint = nearInterestPoint)
                                nearDao.insertNearInterestPoint(near)
                            }
                        }
                        nearDb.forEach { interestPoint ->
                            if (!nearInterestPoint.contains(interestPoint)) {
                                nearDao.deleteNearInterestPoint(id, interestPoint)
                            }
                        }
                        val pictureDb = getPicture(id)
                        uriPicture.zip(titlePicture).forEach { currentPicture ->
                            val pictureTuple = PictureTuple(uri = currentPicture.first, title = currentPicture.second)
                            if (!pictureDb.contains(pictureTuple)) {
                                val picture = Picture(propertyId = id, uri = currentPicture.first, title = currentPicture.second)
                                pictureDao.insertPicture(picture)
                            }
                        }
                        pictureDb.forEach { currentPicture ->
                            if (!(uriPicture.contains(currentPicture.uri) && titlePicture.contains(currentPicture.title))) {
                                pictureDao.deletePicture(id, currentPicture.uri, currentPicture.title)
                            }
                        }
                    }
                }

                _state.update {
                    it.copy(
                        type = PropertyType.HOUSE,
                        price = 0,
                        surface = 0,
                        pieceNumber = 0,
                        description = "",
                        uriPicture = mutableListOf(),
                        titlePicture = mutableListOf(),
                        address = "",
                        latitude = 0.0,
                        longitude = 0.0,
                        nearInterestPoint = mutableListOf(),
                        status = Status.AVAILABLE,
                        entryDate = "",
                        soldDate = "",
                        agent = Agent.STEPHANE_PLAZA,
                    )
                }
            }

            is PropertyEvent.SetAddress -> {
                _state.update {
                    it.copy(
                        address = event.address,
                    )
                }
            }

            is PropertyEvent.SetAgent -> {
                _state.update {
                    it.copy(
                        agent = event.agent,
                    )
                }
            }

            is PropertyEvent.SetDescription -> {
                _state.update {
                    it.copy(
                        description = event.description,
                    )
                }
            }

            is PropertyEvent.SetLatitude -> {
                _state.update {
                    it.copy(
                        latitude = event.latitude,
                    )
                }
            }

            is PropertyEvent.SetLongitude -> {
                _state.update {
                    it.copy(
                        longitude = event.longitude,
                    )
                }
            }

            is PropertyEvent.SetNearInterestPoint -> {
                _state.update {
                    val near: MutableList<String> = it.nearInterestPoint
                    if (near.contains(event.nearInterestPoint)) {
                        near.remove(event.nearInterestPoint)
                    } else {
                        near.add(event.nearInterestPoint)
                    }
                    it.copy(nearInterestPoint = near)
                }
            }

            is PropertyEvent.SetUriPicture -> {
                _state.update {
                    val image: MutableList<String> = it.uriPicture
                    if (image.contains(event.uriPicture)) {
                        image.remove(event.uriPicture)
                    } else {
                        image.add(event.uriPicture)
                    }
                    it.copy(uriPicture = image)
                }
            }

            is PropertyEvent.SetTitlePicture -> {
                _state.update {
                    val title: MutableList<String> = it.titlePicture
                    if (title.contains(event.titlePicture)) {
                        title.remove(event.titlePicture)
                    } else {
                        title.add(event.titlePicture)
                    }
                    it.copy(titlePicture = title)
                }
            }

            is PropertyEvent.SetPieceNumber -> {
                _state.update {
                    it.copy(
                        pieceNumber = event.pieceNumber,
                    )
                }
            }

            is PropertyEvent.SetPrice -> {
                _state.update {
                    it.copy(
                        price = event.price,
                    )
                }
            }

            is PropertyEvent.SetSoldDate -> {
                _state.update {
                    it.copy(
                        soldDate = event.soldDate,
                    )
                }
            }

            is PropertyEvent.SetStatus -> {
                _state.update {
                    it.copy(
                        status = event.status,
                    )
                }
            }

            is PropertyEvent.SetSurface -> {
                _state.update {
                    it.copy(
                        surface = event.surface,
                    )
                }
            }

            is PropertyEvent.SetType -> {
                _state.update {
                    it.copy(
                        type = event.type,
                    )
                }
            }

            is PropertyEvent.SortProperty -> {
                _sortType.value = event.sortType
            }
        }
    }

    fun getNearInterestPoint(id: Int): List<String> {
        return nearDao.getNearInterestPointFromPropertyId(id)
    }

    fun getPicture(id: Int): List<PictureTuple> {
        return pictureDao.getPictureFromPropertyId(id)
    }
}
