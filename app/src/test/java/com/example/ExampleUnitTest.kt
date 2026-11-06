package com.example

import com.example.viewmodel.TuaiScreen
import com.example.viewmodel.TuaiViewModel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Unit tests for Tuai Wallet ViewModel state transitions, balance math, and transaction logging.
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun withdraw_deductsBalanceAndAppendsTransaction() {
        val viewModel = TuaiViewModel()
        assertEquals("$23,867", viewModel.uiState.value.walletBalance)

        // Default withdraw amount is "19,29.00" ($1,929.00)
        viewModel.completeWithdraw()

        val updated = viewModel.uiState.value
        assertEquals(21938.0, updated.walletBalanceNumeric, 0.01)
        assertEquals("$21,938", updated.walletBalance)
        assertEquals(TuaiScreen.WALLET, updated.currentScreen)
        assertTrue(updated.transactions.first().title.contains("Withdraw"))
    }

    @Test
    fun topUp_increasesBalanceAndLogsTransaction() {
        val viewModel = TuaiViewModel()
        viewModel.updateTopUpAmount("1000")
        viewModel.submitTopUp()

        val updated = viewModel.uiState.value
        assertEquals(24867.0, updated.walletBalanceNumeric, 0.01)
        assertEquals("$24,867", updated.walletBalance)
        assertTrue(updated.transactions.first().title.contains("Top Up"))
    }
}
