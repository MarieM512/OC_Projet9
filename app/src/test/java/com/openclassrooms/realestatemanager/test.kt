package com.openclassrooms.realestatemanager

import app.cash.turbine.test
import com.openclassrooms.realestatemanager.ViewModel.PropertyViewModel
import com.openclassrooms.realestatemanager.database.Agent
import com.openclassrooms.realestatemanager.database.InterestPoint
import com.openclassrooms.realestatemanager.database.Property
import com.openclassrooms.realestatemanager.database.PropertyDao
import com.openclassrooms.realestatemanager.database.PropertyEvent
import com.openclassrooms.realestatemanager.database.PropertyState
import com.openclassrooms.realestatemanager.database.PropertyType
import com.openclassrooms.realestatemanager.database.Status
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mock
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule
import org.mockito.kotlin.doAnswer
import org.mockito.kotlin.stub

@RunWith(JUnit4::class)
class test {

    @Mock
    lateinit var propertyDao: PropertyDao

    @get:Rule
    val mockitoRule: MockitoRule = MockitoJUnit.rule()

    @Test
    fun normal() {
        val viewModel = PropertyViewModel(propertyDao)
        val state = PropertyState()
        viewModel.onEvent(PropertyEvent.SetAddress("Nantes"))
        viewModel.onEvent(PropertyEvent.SetAddress("autre"))
        val second = state.address
        viewModel.state.value.address
        val ok = "test"
        assertEquals(viewModel.state.value.address, PropertyState().address)
    }

    @Test
    fun `test2`() = runTest {
        val expectedString = "Nantes"
        propertyDao.stub {
            onBlocking { getAllProperties() } doAnswer { emptyFlow() }
        }
        val sut = PropertyViewModel(propertyDao)
        sut.state.test {
            sut.onEvent(PropertyEvent.SetAddress("Nantes"))

            val first = sut.state.value.address
            val second = PropertyState().address
            val ok = "test"
            assertEquals(sut.state.value.address, expectedString)
        }
    }

    @Test
    fun `èhfljksf`() = runTest {
        val property = Property(
            type = PropertyType.HOUSE,
            price = 780,
            surface = 100,
            pieceNumber = 4,
            description = "Little house far from the city",
            uriPicture = listOf("content://com.openclassrooms.realestatemanager.provider/my_images/21092023_122146.png"),
            titlePicture = listOf("kitchen"),
            address = "La Tancrere, La Varenne, Orée d'Anjou, PDL, France",
            latitude = 47.3099966,
            longitude = -1.3126905,
            nearInterestPoint = listOf(InterestPoint.SCHOOL),
            status = Status.AVAILABLE,
            entryDate = "18/06/2022",
            soldDate = "",
            agent = Agent.STEPHANE_PLAZA,
        )
        propertyDao.upsertProperty(property)
        propertyDao.getAllProperties().test {
            val list = awaitItem()
            assert(list.contains(property))
            cancel()
        }
    }
}