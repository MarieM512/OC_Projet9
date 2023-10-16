package com.openclassrooms.realestatemanager

import com.openclassrooms.realestatemanager.ViewModel.PropertyViewModel
import com.openclassrooms.realestatemanager.database.PropertyEvent
import com.openclassrooms.realestatemanager.database.dao.NearInterestPointDao
import com.openclassrooms.realestatemanager.database.dao.PictureDao
import com.openclassrooms.realestatemanager.database.dao.PropertyDao
import com.openclassrooms.realestatemanager.database.entity.Property
import com.openclassrooms.realestatemanager.database.repository.NearInterestPointRepository
import com.openclassrooms.realestatemanager.database.repository.PictureRepository
import com.openclassrooms.realestatemanager.database.repository.PropertyRepository
import com.openclassrooms.realestatemanager.database.utils.Agent
import com.openclassrooms.realestatemanager.database.utils.InterestPoint
import com.openclassrooms.realestatemanager.database.utils.PropertyDate
import com.openclassrooms.realestatemanager.database.utils.PropertyType
import com.openclassrooms.realestatemanager.database.utils.Status
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito

class PropertyViewModelUnitTest {

    @Mock
    val propertyDao: PropertyDao = Mockito.mock(PropertyDao::class.java)

    @Mock
    val nearDao: NearInterestPointDao = Mockito.mock(NearInterestPointDao::class.java)

    @Mock
    val pictureDao: PictureDao = Mockito.mock(PictureDao::class.java)

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Before
    fun setup() {
        val property = Property(
            id = 1,
            type = PropertyType.HOUSE,
            price = 780,
            surface = 100,
            pieceNumber = 4,
            description = "Little house far from the city",
            address = "La Tancrere, La Varenne, Orée d'Anjou, PDL, France",
            latitude = 47.3099966,
            longitude = -1.3126905,
            status = Status.AVAILABLE,
            entryDate = "18/06/2022",
            soldDate = "",
            agent = Agent.STEPHANE_PLAZA,
        )
        Mockito.`when`(propertyDao.getAllProperties()).thenReturn(flowOf(listOf(property)))
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun setAddress() = runTest {
        val viewModel = PropertyViewModel(
            propertyRepo = PropertyRepository(propertyDao),
            nearRepo = NearInterestPointRepository(nearDao),
            pictureRepo = PictureRepository(pictureDao),
        )
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.state.collect()
        }
        val expected = "Nantes"

        assertEquals("", viewModel.state.value.address)
        viewModel.onEvent(PropertyEvent.SetAddress(expected))
        assertEquals(expected, viewModel.state.value.address)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun setAgent() = runTest {
        val viewModel = PropertyViewModel(
            propertyRepo = PropertyRepository(propertyDao),
            nearRepo = NearInterestPointRepository(nearDao),
            pictureRepo = PictureRepository(pictureDao),
        )
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.state.collect()
        }
        val expected = Agent.MARTIN_ROULEAU

        assertEquals(Agent.STEPHANE_PLAZA, viewModel.state.value.agent)
        viewModel.onEvent(PropertyEvent.SetAgent(expected))
        assertEquals(expected, viewModel.state.value.agent)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun setDescription() = runTest {
        val viewModel = PropertyViewModel(
            propertyRepo = PropertyRepository(propertyDao),
            nearRepo = NearInterestPointRepository(nearDao),
            pictureRepo = PictureRepository(pictureDao),
        )
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.state.collect()
        }
        val expected = "Little house"

        assertEquals("", viewModel.state.value.description)
        viewModel.onEvent(PropertyEvent.SetDescription(expected))
        assertEquals(expected, viewModel.state.value.description)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun setLatitude() = runTest {
        val viewModel = PropertyViewModel(
            propertyRepo = PropertyRepository(propertyDao),
            nearRepo = NearInterestPointRepository(nearDao),
            pictureRepo = PictureRepository(pictureDao),
        )
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.state.collect()
        }
        val expected = 41.2

        assertEquals(0.0, viewModel.state.value.latitude, 0.0)
        viewModel.onEvent(PropertyEvent.SetLatitude(expected))
        assertEquals(expected, viewModel.state.value.latitude, 0.0)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun setLongitude() = runTest {
        val viewModel = PropertyViewModel(
            propertyRepo = PropertyRepository(propertyDao),
            nearRepo = NearInterestPointRepository(nearDao),
            pictureRepo = PictureRepository(pictureDao),
        )
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.state.collect()
        }
        val expected = 35.6

        assertEquals(0.0, viewModel.state.value.longitude, 0.0)
        viewModel.onEvent(PropertyEvent.SetLongitude(expected))
        assertEquals(expected, viewModel.state.value.longitude, 0.0)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun setNearInterestPoint() = runTest {
        val viewModel = PropertyViewModel(
            propertyRepo = PropertyRepository(propertyDao),
            nearRepo = NearInterestPointRepository(nearDao),
            pictureRepo = PictureRepository(pictureDao),
        )
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.state.collect()
        }
        val interestToAdd = InterestPoint.SCHOOL
        val expected = arrayListOf(InterestPoint.SCHOOL.toString())

        assertEquals(emptyList<String>(), viewModel.state.value.nearInterestPoint)
        viewModel.onEvent(PropertyEvent.SetNearInterestPoint(interestToAdd.toString()))
        assertEquals(expected, viewModel.state.value.nearInterestPoint)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun setUriPicture() = runTest {
        val viewModel = PropertyViewModel(
            propertyRepo = PropertyRepository(propertyDao),
            nearRepo = NearInterestPointRepository(nearDao),
            pictureRepo = PictureRepository(pictureDao),
        )
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.state.collect()
        }
        val uriToAdd = "content://uri.picture"
        val expected = arrayListOf(uriToAdd)

        assertEquals(emptyList<String>(), viewModel.state.value.uriPicture)
        viewModel.onEvent(PropertyEvent.SetUriPicture(uriToAdd))
        assertEquals(expected, viewModel.state.value.uriPicture)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun setTitlePicture() = runTest {
        val viewModel = PropertyViewModel(
            propertyRepo = PropertyRepository(propertyDao),
            nearRepo = NearInterestPointRepository(nearDao),
            pictureRepo = PictureRepository(pictureDao),
        )
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.state.collect()
        }
        val titleToAdd = "Kitchen"
        val expected = arrayListOf(titleToAdd)

        assertEquals(emptyList<String>(), viewModel.state.value.titlePicture)
        viewModel.onEvent(PropertyEvent.SetTitlePicture(titleToAdd))
        assertEquals(expected, viewModel.state.value.titlePicture)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun setPieceNumber() = runTest {
        val viewModel = PropertyViewModel(
            propertyRepo = PropertyRepository(propertyDao),
            nearRepo = NearInterestPointRepository(nearDao),
            pictureRepo = PictureRepository(pictureDao),
        )
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.state.collect()
        }
        val expected = 4

        assertEquals(0, viewModel.state.value.pieceNumber)
        viewModel.onEvent(PropertyEvent.SetPieceNumber(expected))
        assertEquals(expected, viewModel.state.value.pieceNumber)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun setPrice() = runTest {
        val viewModel = PropertyViewModel(
            propertyRepo = PropertyRepository(propertyDao),
            nearRepo = NearInterestPointRepository(nearDao),
            pictureRepo = PictureRepository(pictureDao),
        )
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.state.collect()
        }
        val expected = 780

        assertEquals(0, viewModel.state.value.price)
        viewModel.onEvent(PropertyEvent.SetPrice(expected))
        assertEquals(expected, viewModel.state.value.price)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun setEntryDate() = runTest {
        val viewModel = PropertyViewModel(
            propertyRepo = PropertyRepository(propertyDao),
            nearRepo = NearInterestPointRepository(nearDao),
            pictureRepo = PictureRepository(pictureDao),
        )
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.state.collect()
        }
        val expected = "2023-10-05"

        assertEquals("", viewModel.state.value.entryDate)
        viewModel.onEvent(PropertyEvent.SetEntryDate(expected))
        assertEquals(expected, viewModel.state.value.entryDate)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun setSoldDate() = runTest {
        val viewModel = PropertyViewModel(
            propertyRepo = PropertyRepository(propertyDao),
            nearRepo = NearInterestPointRepository(nearDao),
            pictureRepo = PictureRepository(pictureDao),
        )
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.state.collect()
        }
        val expected = "2023-10-05"

        assertEquals("", viewModel.state.value.soldDate)
        viewModel.onEvent(PropertyEvent.SetSoldDate(expected))
        assertEquals(expected, viewModel.state.value.soldDate)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun setStatus() = runTest {
        val viewModel = PropertyViewModel(
            propertyRepo = PropertyRepository(propertyDao),
            nearRepo = NearInterestPointRepository(nearDao),
            pictureRepo = PictureRepository(pictureDao),
        )
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.state.collect()
        }
        val expected = Status.SOLD

        assertEquals(Status.AVAILABLE, viewModel.state.value.status)
        viewModel.onEvent(PropertyEvent.SetStatus(expected))
        assertEquals(expected, viewModel.state.value.status)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun setSurface() = runTest {
        val viewModel = PropertyViewModel(
            propertyRepo = PropertyRepository(propertyDao),
            nearRepo = NearInterestPointRepository(nearDao),
            pictureRepo = PictureRepository(pictureDao),
        )
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.state.collect()
        }
        val expected = 100

        assertEquals(0, viewModel.state.value.surface)
        viewModel.onEvent(PropertyEvent.SetSurface(expected))
        assertEquals(expected, viewModel.state.value.surface)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun setType() = runTest {
        val viewModel = PropertyViewModel(
            propertyRepo = PropertyRepository(propertyDao),
            nearRepo = NearInterestPointRepository(nearDao),
            pictureRepo = PictureRepository(pictureDao),
        )
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.state.collect()
        }
        val expected = PropertyType.HOTEL

        assertEquals(PropertyType.HOUSE, viewModel.state.value.type)
        viewModel.onEvent(PropertyEvent.SetType(expected))
        assertEquals(expected, viewModel.state.value.type)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun setFilterByNear() = runTest {
        val viewModel = PropertyViewModel(
            propertyRepo = PropertyRepository(propertyDao),
            nearRepo = NearInterestPointRepository(nearDao),
            pictureRepo = PictureRepository(pictureDao),
        )
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.state.collect()
        }
        val interestToAdd = InterestPoint.SCHOOL
        val expected = arrayListOf(InterestPoint.SCHOOL.toString())

        assertEquals(emptyList<String>(), viewModel.state.value.filterNear)
        viewModel.onEvent(PropertyEvent.FilterByNear(interestToAdd.toString()))
        assertEquals(expected, viewModel.state.value.filterNear)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun setFilterBySoldDate() = runTest {
        val viewModel = PropertyViewModel(
            propertyRepo = PropertyRepository(propertyDao),
            nearRepo = NearInterestPointRepository(nearDao),
            pictureRepo = PictureRepository(pictureDao),
        )
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.state.collect()
        }
        val expected = PropertyDate.ONE_MONTH

        assertEquals(null, viewModel.state.value.filterSoldDate)
        viewModel.onEvent(PropertyEvent.FilterBySoldDate(expected))
        assertEquals(expected, viewModel.state.value.filterSoldDate)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun setFilterByEntryDate() = runTest {
        val viewModel = PropertyViewModel(
            propertyRepo = PropertyRepository(propertyDao),
            nearRepo = NearInterestPointRepository(nearDao),
            pictureRepo = PictureRepository(pictureDao),
        )
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.state.collect()
        }
        val expected = PropertyDate.ONE_MONTH

        assertEquals(null, viewModel.state.value.filterEntryDate)
        viewModel.onEvent(PropertyEvent.FilterByEntryDate(expected))
        assertEquals(expected, viewModel.state.value.filterEntryDate)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun setFilterByPicture() = runTest {
        val viewModel = PropertyViewModel(
            propertyRepo = PropertyRepository(propertyDao),
            nearRepo = NearInterestPointRepository(nearDao),
            pictureRepo = PictureRepository(pictureDao),
        )
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.state.collect()
        }
        val expected = 2

        assertEquals(1, viewModel.state.value.filterPicture)
        viewModel.onEvent(PropertyEvent.FilterByPicture(expected))
        assertEquals(expected, viewModel.state.value.filterPicture)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun setFilterBySurfaceMinAndMax() = runTest {
        val viewModel = PropertyViewModel(
            propertyRepo = PropertyRepository(propertyDao),
            nearRepo = NearInterestPointRepository(nearDao),
            pictureRepo = PictureRepository(pictureDao),
        )
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.state.collect()
        }
        val expectedMin = 10
        val expectedMax = 120

        assertEquals(0, viewModel.state.value.minSurface)
        assertEquals(10000, viewModel.state.value.maxSurface)
        viewModel.onEvent(PropertyEvent.FilterBySurfaceMin(expectedMin))
        viewModel.onEvent(PropertyEvent.FilterBySurfaceMax(expectedMax))
        assertEquals(expectedMin, viewModel.state.value.minSurface)
        assertEquals(expectedMax, viewModel.state.value.maxSurface)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun setFilterByPriceMinAndMax() = runTest {
        val viewModel = PropertyViewModel(
            propertyRepo = PropertyRepository(propertyDao),
            nearRepo = NearInterestPointRepository(nearDao),
            pictureRepo = PictureRepository(pictureDao),
        )
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.state.collect()
        }
        val expectedMin = 500
        val expectedMax = 1000

        assertEquals(0, viewModel.state.value.minPrice)
        assertEquals(1000000000, viewModel.state.value.maxPrice)
        viewModel.onEvent(PropertyEvent.FilterByPriceMin(expectedMin))
        viewModel.onEvent(PropertyEvent.FilterByPriceMax(expectedMax))
        assertEquals(expectedMin, viewModel.state.value.minPrice)
        assertEquals(expectedMax, viewModel.state.value.maxPrice)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun setFilterByPieceMinAndMax() = runTest {
        val viewModel = PropertyViewModel(
            propertyRepo = PropertyRepository(propertyDao),
            nearRepo = NearInterestPointRepository(nearDao),
            pictureRepo = PictureRepository(pictureDao),
        )
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.state.collect()
        }
        val expectedMin = 1
        val expectedMax = 10

        assertEquals(0, viewModel.state.value.minPiece)
        assertEquals(1000, viewModel.state.value.maxPiece)
        viewModel.onEvent(PropertyEvent.FilterByPieceMin(expectedMin))
        viewModel.onEvent(PropertyEvent.FilterByPieceMax(expectedMax))
        assertEquals(expectedMin, viewModel.state.value.minPiece)
        assertEquals(expectedMax, viewModel.state.value.maxPiece)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun setFilterByAgent() = runTest {
        val viewModel = PropertyViewModel(
            propertyRepo = PropertyRepository(propertyDao),
            nearRepo = NearInterestPointRepository(nearDao),
            pictureRepo = PictureRepository(pictureDao),
        )
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.state.collect()
        }
        val expected = Agent.BEN_CABALLERO

        assertEquals(null, viewModel.state.value.filterAgent)
        viewModel.onEvent(PropertyEvent.FilterByAgent(expected))
        assertEquals(expected, viewModel.state.value.filterAgent)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun setFilterByAddress() = runTest {
        val viewModel = PropertyViewModel(
            propertyRepo = PropertyRepository(propertyDao),
            nearRepo = NearInterestPointRepository(nearDao),
            pictureRepo = PictureRepository(pictureDao),
        )
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.state.collect()
        }
        val expected = "Nantes"

        assertEquals("", viewModel.state.value.filterAddress)
        viewModel.onEvent(PropertyEvent.FilterByAddress(expected))
        assertEquals(expected, viewModel.state.value.filterAddress)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun setFilterByType() = runTest {
        val viewModel = PropertyViewModel(
            propertyRepo = PropertyRepository(propertyDao),
            nearRepo = NearInterestPointRepository(nearDao),
            pictureRepo = PictureRepository(pictureDao),
        )
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.state.collect()
        }
        val expected = PropertyType.HOTEL

        assertEquals(null, viewModel.state.value.filterType)
        viewModel.onEvent(PropertyEvent.FilterByType(expected))
        assertEquals(expected, viewModel.state.value.filterType)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun resetFilter() = runTest {
        val viewModel = PropertyViewModel(
            propertyRepo = PropertyRepository(propertyDao),
            nearRepo = NearInterestPointRepository(nearDao),
            pictureRepo = PictureRepository(pictureDao),
        )
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.state.collect()
        }
        val expected = PropertyType.HOTEL

        assertEquals(null, viewModel.state.value.filterType)
        viewModel.onEvent(PropertyEvent.FilterByType(expected))
        assertEquals(expected, viewModel.state.value.filterType)
        viewModel.onEvent(PropertyEvent.ResetFilter)
        assertEquals(null, viewModel.state.value.filterType)
    }
}
