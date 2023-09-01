package com.openclassrooms.realestatemanager.ui.add

import android.net.Uri
import com.openclassrooms.realestatemanager.database.Agent
import com.openclassrooms.realestatemanager.database.InterestPoint
import com.openclassrooms.realestatemanager.database.PropertyType

data class AddUiState(
    var image: MutableList<Uri> = mutableListOf(),
    var type: PropertyType = PropertyType.HOUSE,
    var surface: Int = 0,
    var pieceNumber: Int = 0,
    var nearInterestPoint: MutableList<InterestPoint> = mutableListOf(),
    var description: String = "",
    var address: String = "",
    var price: Int = 0,
    var agent: Agent = Agent.STEPHANE_PLAZA,
)
