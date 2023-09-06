package com.openclassrooms.realestatemanager

import com.openclassrooms.realestatemanager.ui.loan.LoanViewModel
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class LoanUnitTest {

    private val viewModel = LoanViewModel()
    private val contribution = "100000"
    private val rate = "5"
    private val duration = "15"

    @Test
    fun updateContribution() {
        Assert.assertEquals(viewModel.uiState.value.contribution, "")
        viewModel.updateContribution(contribution)
        Assert.assertEquals(viewModel.uiState.value.contribution, contribution)
    }

    @Test
    fun updateRate() {
        Assert.assertEquals(viewModel.uiState.value.rate, "")
        viewModel.updateRate(rate)
        Assert.assertEquals(viewModel.uiState.value.rate, rate)
    }

    @Test
    fun updateDuration() {
        Assert.assertEquals(viewModel.uiState.value.duration, "")
        viewModel.updateDuration(duration)
        Assert.assertEquals(viewModel.uiState.value.duration, duration)
    }

    @Test
    fun updateSelectedOption() {
        Assert.assertEquals(viewModel.uiState.value.selectedOptionText, "$")
        viewModel.updateSelectedOptionText("€")
        Assert.assertEquals(viewModel.uiState.value.selectedOptionText, "€")
    }

    @Test
    fun simulate_Dollars() {
        viewModel.updateContribution(contribution)
        viewModel.updateRate(rate)
        viewModel.updateDuration(duration)
        viewModel.loanSimulate()
        Assert.assertEquals(viewModel.uiState.value.result, 850.31F)
    }

    @Test
    fun simulate_Euros() {
        viewModel.updateContribution(contribution)
        viewModel.updateRate(rate)
        viewModel.updateDuration(duration)
        viewModel.updateSelectedOptionText("€")
        viewModel.loanSimulate()
        Assert.assertEquals(viewModel.uiState.value.result, 790.79F)
    }

    @Test
    fun checkEmptyFields() {
        Assert.assertEquals(viewModel.uiState.value.contribution, "")
        Assert.assertEquals(viewModel.uiState.value.rate, "")
        Assert.assertEquals(viewModel.uiState.value.duration, "")
        Assert.assertFalse(viewModel.isAllFieldsAreNotEmpty())
    }

    @Test
    fun checkNotEmptyFields() {
        viewModel.updateContribution(contribution)
        viewModel.updateRate(rate)
        viewModel.updateDuration(duration)
        Assert.assertTrue(viewModel.isAllFieldsAreNotEmpty())
    }
}
