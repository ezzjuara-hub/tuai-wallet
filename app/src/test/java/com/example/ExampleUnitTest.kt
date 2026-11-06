package com.example

import com.example.viewmodel.TuaiScreen
import com.example.viewmodel.TuaiViewModel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Unit tests for Tuai Multi-Wallet & Bank Connector ViewModel state transitions,
 * plugin connection aggregation, balance updates, cross-wallet transfers, and financial conditioning.
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun initialAggregatedBalance_sumsAllConnectedWalletsAndBanks() {
        val viewModel = TuaiViewModel()
        val state = viewModel.uiState.value
        // BCA (14.5M) + Mandiri (8.25M) + BRImo (4.6M) + GoPay (1.35M) + DANA (0.85M) = 29.55M
        assertEquals(29_550_000.0, state.walletBalanceNumeric, 0.01)
        assertEquals("Rp 29.550.000", state.walletBalance)
        assertEquals(5, state.connectedPluginCount)
        // Bank = 14.5M + 8.25M + 4.6M = 27.35M; E-Wallet = 1.35M + 0.85M = 2.2M
        assertEquals(27_350_000.0, state.bankBalanceNumeric, 0.01)
        assertEquals(2_200_000.0, state.ewalletBalanceNumeric, 0.01)
    }

    @Test
    fun togglePluginConnection_updatesTotalAggregatedBalanceAndConditioningGoals() {
        val viewModel = TuaiViewModel()
        // Connect OVO (Rp 620.000)
        viewModel.togglePluginConnection("ovo")
        val updated = viewModel.uiState.value
        assertEquals(30_170_000.0, updated.walletBalanceNumeric, 0.01)
        assertEquals("Rp 30.170.000", updated.walletBalance)
        assertEquals(6, updated.connectedPluginCount)
        // E-Wallet daily operational bucket (goal2) should reflect GoPay (1.35M) + DANA (0.85M) + OVO (0.62M) = 2.82M
        val operationalGoal = updated.savingsGoals.first { it.id == "goal2" }
        assertEquals(2_820_000.0, operationalGoal.currentUsd, 0.01)
    }

    @Test
    fun withdraw_deductsBalanceFromSelectedWalletAndUpdatesTotal() {
        val viewModel = TuaiViewModel()
        // Default withdraw amount is "150.000" from GoPay
        viewModel.completeWithdraw()

        val updated = viewModel.uiState.value
        assertEquals(29_400_000.0, updated.walletBalanceNumeric, 0.01)
        assertEquals("Rp 29.400.000", updated.walletBalance)
        assertEquals(TuaiScreen.WALLET, updated.currentScreen)
        assertTrue(updated.transactions.first().title.contains("Keluar dari GoPay"))
    }

    @Test
    fun topUp_increasesBalanceAndLogsTransaction() {
        val viewModel = TuaiViewModel()
        viewModel.updateTopUpAmount("450000")
        viewModel.submitTopUp()

        val updated = viewModel.uiState.value
        assertEquals(30_000_000.0, updated.walletBalanceNumeric, 0.01)
        assertEquals("Rp 30.000.000", updated.walletBalance)
        assertTrue(updated.transactions.first().title.contains("Saldo Masuk"))
    }

    @Test
    fun saveManualWalletBalance_updatesBalanceAndAccountNumber() {
        val viewModel = TuaiViewModel()
        val gopay = viewModel.uiState.value.assets.first { it.id == "gopay" }
        viewModel.openAssetDetail(gopay)
        viewModel.updateManualBalanceInput("2.000.000")
        viewModel.updateManualAccountInput("0812-9999-8888")
        viewModel.saveManualWalletBalance("gopay")

        val updated = viewModel.uiState.value
        val updatedGopay = updated.assets.first { it.id == "gopay" }
        assertEquals(2_000_000.0, updatedGopay.tokenNumeric, 0.01)
        assertEquals("0812-9999-8888", updatedGopay.accountNumber)
        // Total increases by +650.000 (from 1.35M to 2.0M) -> 30.2M
        assertEquals(30_200_000.0, updated.walletBalanceNumeric, 0.01)
    }

    @Test
    fun addNewCustomPlugin_connectsNewWalletAndAddsToTotalBalance() {
        val viewModel = TuaiViewModel()
        viewModel.updateCustomPluginName("Bank Jago")
        viewModel.updateCustomPluginCategory("BANK")
        viewModel.updateCustomPluginAccount("1029-7712-99")
        viewModel.updateCustomPluginBalance("1450000")
        viewModel.addNewCustomPlugin()

        val updated = viewModel.uiState.value
        assertEquals(6, updated.connectedPluginCount)
        assertEquals(31_000_000.0, updated.walletBalanceNumeric, 0.01)
        assertTrue(updated.connectedAssets.any { it.name == "Bank Jago" })
    }
}
