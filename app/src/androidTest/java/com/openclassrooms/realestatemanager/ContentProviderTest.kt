package com.openclassrooms.realestatemanager

import android.content.ContentResolver
import android.content.ContentUris
import android.content.ContentValues
import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.openclassrooms.realestatemanager.database.PropertyDatabase
import com.openclassrooms.realestatemanager.database.utils.Agent
import com.openclassrooms.realestatemanager.database.utils.PropertyType
import com.openclassrooms.realestatemanager.database.utils.Status
import com.openclassrooms.realestatemanager.provider.ContentProvider
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ContentProviderTest {
    private var contentResolver: ContentResolver? = null

    @Before
    fun setup() {
        Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getInstrumentation().context, PropertyDatabase::class.java)
            .allowMainThreadQueries()
            .build()

        contentResolver = InstrumentationRegistry.getInstrumentation().context.contentResolver
    }

    @Test
    fun getItem() {
        val propertyId = contentResolver!!.insert(ContentProvider.URI_ITEM, generateProperty())
        val cursor = contentResolver!!.query(ContentUris.withAppendedId(ContentProvider.URI_ITEM, 306), null, null, null, null)
        assertEquals(cursor?.count, 1)
        assertEquals(cursor?.moveToLast(), true)
        assertEquals(cursor?.getString(cursor.getColumnIndex("description")), "Little loft for student")
    }

    private fun generateProperty(): ContentValues {
        val values = ContentValues()
        values.put("id", 306)
        values.put("type", PropertyType.LOFT.label)
        values.put("price", 800)
        values.put("surface", 50)
        values.put("pieceNumber", 2)
        values.put("description", "Little loft for student")
        values.put("address", "Nantes")
        values.put("latitude", 41.0)
        values.put("longitude", 35.9)
        values.put("status", Status.AVAILABLE.label)
        values.put("entryDate", "2023-10-07")
        values.put("soldDate", "")
        values.put("agent", Agent.STEPHANE_PLAZA.label)
        return values
    }
}
