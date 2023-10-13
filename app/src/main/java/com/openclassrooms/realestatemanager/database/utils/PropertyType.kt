package com.openclassrooms.realestatemanager.database.utils

enum class PropertyType(val label: String) {
    HOUSE("House"),
    VILLA("Villa"),
    APARTMENT("Apartment"),
    RESIDENCE("Residence"),
    LOFT("Loft"),
    OFFICE("Office"),
    COMMERCIAL_PREMISES("Commercial premises"),
    HOTEL("Hotel"),
    MANOR("Manor"),
    ;

    companion object {
        fun getTypeByLabel(label: String): PropertyType {
            for (value in PropertyType.values()) {
                if (value.label == label) {
                    return value
                }
            }
            return HOUSE
        }
    }
}
