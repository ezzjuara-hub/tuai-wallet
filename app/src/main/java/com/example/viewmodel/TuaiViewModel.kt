package com.example.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

enum class TuaiScreen {
    LOGIN,
    WALLET,
    WITHDRAW
}

enum class AssetIconType {
    ETHEREUM,
    BINANCE,
    TETHER
}

data class CryptoAssetItem(
    val id: String,
    val name: String,
    val percentageAndValue: String,
    val tokenAmount: String,
    val totalUsdValue: String,
    val iconType: AssetIconType
)

data class WalletTransactionItem(
    val id: String,
    val title: String,
    val date: String,
    val amount: String,
    val isPositive: Boolean,
    val iconType: AssetIconType
)

data class TuaiUiState(
    val currentScreen: TuaiScreen = TuaiScreen.LOGIN,
    val emailOrPhone: String = "",
    val password: String = "",
    val isPasswordVisible: Boolean = false,
    val walletBalance: String = "$23,867",
    val walletId: String = "2678900085",
    val selectedAssetTab: Int = 0, // 0 = My Assets, 1 = My Transaction
    val selectedBottomNav: Int = 1, // 0 = Home, 1 = Wallet, 2 = Chart, 3 = Settings
    val withdrawAmount: String = "19,29.00",
    val isWithdrawStarred: Boolean = false,
    val assets: List<CryptoAssetItem> = listOf(
        CryptoAssetItem(
            id = "eth",
            name = "Ethereum",
            percentageAndValue = "28,74% ($28,015)",
            tokenAmount = "79.006 ETH",
            totalUsdValue = "$100,000.00",
            iconType = AssetIconType.ETHEREUM
        ),
        CryptoAssetItem(
            id = "bnb",
            name = "Binance",
            percentageAndValue = "15.96% ($28,015)",
            tokenAmount = "107.70 BNB",
            totalUsdValue = "$30,812.92",
            iconType = AssetIconType.BINANCE
        ),
        CryptoAssetItem(
            id = "usdt",
            name = "Tether usd",
            percentageAndValue = "29.74% ($26,015)",
            tokenAmount = "79.006 ETH",
            totalUsdValue = "$100,000.00",
            iconType = AssetIconType.TETHER
        )
    ),
    val transactions: List<WalletTransactionItem> = listOf(
        WalletTransactionItem(
            id = "tx1",
            title = "Withdraw Ethereum",
            date = "Today, 09:41 AM",
            amount = "-$1,929.00",
            isPositive = false,
            iconType = AssetIconType.ETHEREUM
        ),
        WalletTransactionItem(
            id = "tx2",
            title = "Received Binance",
            date = "Yesterday, 04:15 PM",
            amount = "+$4,250.00",
            isPositive = true,
            iconType = AssetIconType.BINANCE
        ),
        WalletTransactionItem(
            id = "tx3",
            title = "Tether usd Swap",
            date = "Oct 29, 11:20 AM",
            amount = "+$2,600.00",
            isPositive = true,
            iconType = AssetIconType.TETHER
        )
    ),
    val toastMessage: String? = null
)

class TuaiViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(TuaiUiState())
    val uiState: StateFlow<TuaiUiState> = _uiState.asStateFlow()

    fun navigateTo(screen: TuaiScreen) {
        _uiState.update { it.copy(currentScreen = screen) }
    }

    fun updateEmailOrPhone(value: String) {
        _uiState.update { it.copy(emailOrPhone = value) }
    }

    fun updatePassword(value: String) {
        _uiState.update { it.copy(password = value) }
    }

    fun togglePasswordVisibility() {
        _uiState.update { it.copy(isPasswordVisible = !it.isPasswordVisible) }
    }

    fun selectAssetTab(index: Int) {
        _uiState.update { it.copy(selectedAssetTab = index) }
    }

    fun selectBottomNav(index: Int) {
        _uiState.update { it.copy(selectedBottomNav = index) }
    }

    fun toggleWithdrawStar() {
        _uiState.update { it.copy(isWithdrawStarred = !it.isWithdrawStarred) }
    }

    fun onKeypadDigit(digit: String) {
        _uiState.update { state ->
            val current = state.withdrawAmount
            // If it's the initial formatted string "19,29.00", start fresh or append sensibly
            val next = if (current == "0.00" || current == "0") {
                digit
            } else if (current == "19,29.00") {
                digit
            } else if (current.length < 10) {
                current + digit
            } else {
                current
            }
            state.copy(withdrawAmount = next)
        }
    }

    fun onKeypadReset() {
        _uiState.update { it.copy(withdrawAmount = "19,29.00") }
    }

    fun onKeypadBackspace() {
        _uiState.update { state ->
            val current = state.withdrawAmount
            val next = if (current.length <= 1) {
                "0"
            } else {
                current.dropLast(1)
            }
            state.copy(withdrawAmount = next)
        }
    }

    fun showMessage(msg: String) {
        _uiState.update { it.copy(toastMessage = msg) }
    }

    fun clearMessage() {
        _uiState.update { it.copy(toastMessage = null) }
    }
}
