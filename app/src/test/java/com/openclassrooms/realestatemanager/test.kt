package com.openclassrooms.realestatemanager

import app.cash.turbine.test
import com.openclassrooms.realestatemanager.ViewModel.PropertyViewModel
import com.openclassrooms.realestatemanager.database.utils.Agent
import com.openclassrooms.realestatemanager.database.entity.Property
import com.openclassrooms.realestatemanager.database.dao.PropertyDao
import com.openclassrooms.realestatemanager.database.PropertyEvent
import com.openclassrooms.realestatemanager.database.PropertyState
import com.openclassrooms.realestatemanager.database.utils.PropertyType
import com.openclassrooms.realestatemanager.database.utils.Status
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
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
    fun dfkljs() = runTest {
        val viewModel = PropertyViewModel(propertyDao)

        val values = mutableListOf<Any>()
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.state.collect()
        }

        viewModel.state.test {
            propertyDao.getAllProperties()
            viewModel.onEvent(PropertyEvent.SetAddress("Nantes"))
            viewModel.onEvent(PropertyEvent.SaveProperty(-1))
            assertEquals("Nantes", awaitItem())
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
//            nearInterestPoint = listOf(InterestPoint.SCHOOL),
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