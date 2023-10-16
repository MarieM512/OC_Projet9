package com.openclassrooms.realestatemanager.database.utils

enum class PropertyDate(val label: String, val query: String) {
    ONE_WEEK("one week", "-7 day"),
    ONE_MONTH("one month", "-1 month"),
    THREE_MONTHS("three months", "-3 month")
}