package com.openclassrooms.realestatemanager

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.openclassrooms.realestatemanager.database.Agent
import com.openclassrooms.realestatemanager.database.InterestPoint
import com.openclassrooms.realestatemanager.database.Property
import com.openclassrooms.realestatemanager.database.PropertyDao
import com.openclassrooms.realestatemanager.database.PropertyDatabase
import com.openclassrooms.realestatemanager.database.PropertyType
import com.openclassrooms.realestatemanager.database.Status
import com.openclassrooms.realestatemanager.utils.Converters
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class PropertyDaoTest {

    private lateinit var propertyDao: PropertyDao
    private lateinit var propertyDb: PropertyDatabase

    companion object {
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
    }

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<android.content.Context>()
        propertyDb = Room
            .inMemoryDatabaseBuilder(context, PropertyDatabase::class.java)
            .allowMainThreadQueries()
            .addTypeConverter(Converters())
            .build()
        propertyDao = propertyDb.dao
    }

    @Test
    fun insertProperty_expectedProperty() = runBlocking {
        propertyDao.upsertProperty(property)

        val result = propertyDao.getAllProperties().first()[0]

        Assert.assertEquals(PropertyType.HOUSE, result.type)
        Assert.assertEquals(780, result.price)
        Assert.assertEquals(4, result.pieceNumber)
        Assert.assertEquals("Little house far from the city", result.description)
        Assert.assertEquals(listOf("content://com.openclassrooms.realestatemanager.provider/my_images/21092023_122146.png"), result.uriPicture)
        Assert.assertEquals(listOf("kitchen"), result.titlePicture)
        Assert.assertEquals("La Tancrere, La Varenne, Orée d'Anjou, PDL, France", result.address)
        Assert.assertEquals(47.3099966, result.latitude, 0.0)
        Assert.assertEquals(-1.3126905, result.longitude, 0.0)
        Assert.assertEquals(listOf(InterestPoint.SCHOOL), result.nearInterestPoint)
        Assert.assertEquals(Status.AVAILABLE, result.status)
        Assert.assertEquals("18/06/2022", result.entryDate)
        Assert.assertEquals("", result.soldDate)
        Assert.assertEquals(Agent.STEPHANE_PLAZA, result.agent)
    }
}
