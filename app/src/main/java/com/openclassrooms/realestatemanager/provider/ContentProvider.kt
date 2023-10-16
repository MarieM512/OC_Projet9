package com.openclassrooms.realestatemanager.provider

import android.content.ContentProvider
import android.content.ContentUris
import android.content.ContentValues
import android.database.Cursor
import android.net.Uri
import com.openclassrooms.realestatemanager.database.PropertyDatabase
import com.openclassrooms.realestatemanager.database.entity.Property

class ContentProvider : ContentProvider() {

    companion object {
        val AUTHORITY = "com.openclassrooms.realestatemanager.data.provider"
        val TABLE_REAL_ESTATE = Property::class.java.simpleName
        val URI_ITEM = Uri.parse("content://$AUTHORITY/$TABLE_REAL_ESTATE")
    }

    override fun onCreate(): Boolean {
        return true
    }

    override fun query(
        p0: Uri,
        p1: Array<out String>?,
        p2: String?,
        p3: Array<out String>?,
        p4: String?,
    ): Cursor {
        if (context != null) {
            val idProperty = ContentUris.parseId(p0)
            val cursor: Cursor = PropertyDatabase.getDatabase(context!!)
                .propertyDao.getPropertiesWithCursor(idProperty)
            cursor.setNotificationUri(context!!.contentResolver, p0)
            return cursor
        }
        throw IllegalArgumentException("Failed to query row for uri -> $p0")
    }

    override fun getType(p0: Uri): String {
        return "vnd.android.cursor.item/$AUTHORITY.$TABLE_REAL_ESTATE"
    }

    override fun insert(p0: Uri, p1: ContentValues?): Uri? {
        if (context != null) {
            val id = p1?.let {
                Property.fromContentValues(
                    it,
                )
            }?.let {
                PropertyDatabase.getDatabase(context!!).propertyDao.insertPropertyForContent(it)
            }
            if (id != 0L) {
                context!!.contentResolver.notifyChange(p0, null)
                return id?.let { ContentUris.withAppendedId(p0, it) }
            }
        }
        throw IllegalArgumentException("Failed to insert row into $p0")
    }

    override fun delete(p0: Uri, p1: String?, p2: Array<out String>?): Int {
        if (context != null) {
            val count = PropertyDatabase.getDatabase(context!!).propertyDao.deletePropertyForContent(ContentUris.parseId(p0))
            context!!.contentResolver.notifyChange(p0, null)
            return count
        }
        throw IllegalArgumentException("Failed to delete row into $p0")
    }

    override fun update(p0: Uri, p1: ContentValues?, p2: String?, p3: Array<out String>?): Int {
        if (context != null && p1 != null) {
            val count = PropertyDatabase.getDatabase(context!!).propertyDao.updatePropertyForContent(Property.fromContentValues(p1))
            context!!.contentResolver.notifyChange(p0, null)
            return count
        }
        throw IllegalArgumentException("Failed to update row into $p0")
    }
}
