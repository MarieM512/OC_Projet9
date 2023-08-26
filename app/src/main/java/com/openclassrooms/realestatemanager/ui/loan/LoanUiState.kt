package com.openclassrooms.realestatemanager.ui.loan

data class LoanUiState(
    var contribution: String = "",
    var rate: String = "",
    var duration: String = "",
    var result: Float = 0.00F,
    var expanded: Boolean = false,
    val options: List<String> = listOf("$", "€"),
    var selectedOptionText: String = options[0]
)
