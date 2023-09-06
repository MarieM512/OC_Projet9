package com.openclassrooms.realestatemanager.ui.loan

import androidx.lifecycle.ViewModel
import com.openclassrooms.realestatemanager.utils.Utils
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlin.math.pow
import kotlin.math.roundToInt

class LoanViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(LoanUiState())
    val uiState: StateFlow<LoanUiState> = _uiState.asStateFlow()

    fun updateContribution(value: String) {
        _uiState.update {
            it.copy(contribution = value)
        }
    }

    fun updateRate(value: String) {
        _uiState.update {
            it.copy(rate = value)
        }
    }

    fun updateDuration(value: String) {
        _uiState.update {
            it.copy(duration = value)
        }
    }

    fun updateSelectedOptionText(value: String) {
        _uiState.update {
            it.copy(selectedOptionText = value)
        }
    }

    fun loanSimulate() {
        val c = if (uiState.value.selectedOptionText == "$") {
            Utils.convertEuroToDollar(uiState.value.contribution.toInt())
        } else {
            uiState.value.contribution.toInt()
        }
        val r = (uiState.value.rate.toFloat() / 100) / 12
        val d = uiState.value.duration.toInt() * 12
        _uiState.update {
            it.copy(result = ((((c * r) / (1 - (1 / ((1 + r).pow(d))))) * 100).roundToInt() / 100.0).toFloat())
        }
    }

    fun isAllFieldsAreNotEmpty(): Boolean {
        return uiState.value.contribution.isNotEmpty() && uiState.value.rate.isNotEmpty() && uiState.value.duration.isNotEmpty()
    }
}
