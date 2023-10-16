package com.openclassrooms.realestatemanager.database.utils

enum class Agent(val label: String) {
    STEPHANE_PLAZA("Stephane Plaza"),
    DOLLY_LENZ("Dolly Lenz"),
    GARY_KELLER("Gary Keller"),
    MARTIN_ROULEAU("Martin Rouleau"),
    BEN_CABALLERO("Ben Caballero"),
    ;

    companion object {
        fun getAgentByLabel(label: String): Agent {
            for (value in values()) {
                if (value.label == label) {
                    return value
                }
            }
            return STEPHANE_PLAZA
        }
    }
}
