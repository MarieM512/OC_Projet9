package com.openclassrooms.realestatemanager.database.utils

enum class Status(val label: String) {
    AVAILABLE("Available"),
    SOLD("Sold"),
    ;

    companion object {
        fun getStatusByLabel(label: String): Status {
            for (value in Status.values()) {
                if (value.label == label) {
                    return value
                }
            }
            return AVAILABLE
        }
    }
}
