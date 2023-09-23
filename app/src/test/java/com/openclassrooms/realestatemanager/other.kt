package com.openclassrooms.realestatemanager

import com.openclassrooms.realestatemanager.ViewModel.PropertyViewModel
import com.openclassrooms.realestatemanager.database.dao.PropertyDao
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import org.mockito.Mock

class other {

    @Mock
    lateinit var propertyDao: PropertyDao

    @Test
    fun `when creating view model, should fetch property`() = runTest {
        val viewModel = PropertyViewModel(propertyDao)
        assertEquals("", viewModel.state.value.address)
    }
}