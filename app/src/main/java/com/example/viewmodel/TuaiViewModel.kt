package com.example.viewmodel

import androidx.lifecycle.ViewModel
import com.example.data.TuaiFirebaseRepository
import java.text.NumberFormat
import java.util.Locale
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

enum class TuaiActiveModal {
    NONE,
    FORGOT_PASSWORD,
    BIOMETRIC_PROMPT,
    RECEIVE_QR,
    SWAP_ASSET,
    TOP_UP,
    NOTIFICATIONS,
    ASSET_DETAIL,
    TRANSACTION_DETAIL,
    WITHDRAW_EXCHANGE_PICKER,
    PROFILE_ACCOUNT
}

data class CryptoAssetItem(
    val id: String,
    val name: String,
    val percentageAndValue: String,
    val tokenAmount: String,
    val totalUsdValue: String,
    val iconType: AssetIconType,
    val symbol: String = when (iconType) {
        AssetIconType.ETHEREUM -> "ETH"
        AssetIconType.BINANCE -> "BNB"
        AssetIconType.TETHER -> "USDT"
    },
    val tokenNumeric: Double = when (iconType) {
        AssetIconType.ETHEREUM -> 79.006
        AssetIconType.BINANCE -> 107.70
        AssetIconType.TETHER -> 79.006
    },
    val unitPriceUsd: Double = when (iconType) {
        AssetIconType.ETHEREUM -> 1265.72
        AssetIconType.BINANCE -> 286.10
        AssetIconType.TETHER -> 1.00
    },
    val change24h: String = when (iconType) {
        AssetIconType.ETHEREUM -> "+4.82%"
        AssetIconType.BINANCE -> "+2.45%"
        AssetIconType.TETHER -> "+0.05%"
    }
)

data class WalletTransactionItem(
    val id: String,
    val title: String,
    val date: String,
    val amount: String,
    val isPositive: Boolean,
    val iconType: AssetIconType,
    val category: String = if (isPositive) "RECEIVE" else "WITHDRAW",
    val status: String = "Berhasil",
    val referenceNote: String = "Tuai Instant Settlement • Bebas Biaya Admin"
)

data class TuaiNotificationItem(
    val id: String,
    val title: String,
    val message: String,
    val time: String,
    val isRead: Boolean = false
)

data class SavingsGoalItem(
    val id: String,
    val title: String,
    val currentUsd: Double,
    val targetUsd: Double,
    val deadline: String
)

data class TuaiUiState(
    val currentScreen: TuaiScreen = TuaiScreen.LOGIN,
    val isRegisterMode: Boolean = false,
    val registerName: String = "",
    val emailOrPhone: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val isPasswordVisible: Boolean = false,
    val userName: String = "Alex Pratama",
    val userEmail: String = "alex.pratama@tuai.id",
    val walletBalanceNumeric: Double = 23867.0,
    val walletBalance: String = "$23,867",
    val walletId: String = "2678900085",
    val walletAddressHex: String = "0x71C9...89A4F2",
    val isBalanceHidden: Boolean = false,
    val biometricEnabled: Boolean = true,
    val notificationsEnabled: Boolean = true,
    val selectedAssetTab: Int = 0, // 0 = My Assets, 1 = My Transaction
    val selectedBottomNav: Int = 1, // 0 = Home, 1 = Wallet, 2 = Chart, 3 = Settings
    val selectedTxFilter: String = "ALL", // ALL, IN, OUT, SWAP
    val selectedChartTimeframe: String = "1M", // 24H, 1W, 1M, 1Y, ALL
    val selectedChartAssetId: String = "all", // all, eth, bnb, usdt
    val withdrawAmount: String = "19,29.00",
    val isWithdrawStarred: Boolean = false,
    val selectedWithdrawAssetId: String = "eth",
    val withdrawDestination: String = "BCA • 8820192831 (Alex Pratama)",
    val activeModal: TuaiActiveModal = TuaiActiveModal.NONE,
    val selectedAssetForModal: CryptoAssetItem? = null,
    val selectedTxForModal: WalletTransactionItem? = null,
    val swapFromAssetId: String = "eth",
    val swapToAssetId: String = "usdt",
    val swapAmountInput: String = "500",
    val topUpAmountInput: String = "1000",
    val topUpMethod: String = "BCA Virtual Account",
    val topUpTargetAssetId: String = "eth",
    val recoveryInput: String = "",
    val isFirebaseSynced: Boolean = true,
    val lastSyncedLabel: String = "Baru saja • Firebase Cloud & Local",
    val assets: List<CryptoAssetItem> = listOf(
        CryptoAssetItem(
            id = "eth",
            name = "Ethereum",
            percentageAndValue = "28,74% ($28,015)",
            tokenAmount = "79.006 ETH",
            totalUsdValue = "$100,000.00",
            iconType = AssetIconType.ETHEREUM,
            tokenNumeric = 79.006,
            unitPriceUsd = 1265.72
        ),
        CryptoAssetItem(
            id = "bnb",
            name = "Binance",
            percentageAndValue = "15.96% ($28,015)",
            tokenAmount = "107.70 BNB",
            totalUsdValue = "$30,812.92",
            iconType = AssetIconType.BINANCE,
            tokenNumeric = 107.70,
            unitPriceUsd = 286.10
        ),
        CryptoAssetItem(
            id = "usdt",
            name = "Tether usd",
            percentageAndValue = "29.74% ($26,015)",
            tokenAmount = "79.006 ETH",
            totalUsdValue = "$100,000.00",
            iconType = AssetIconType.TETHER,
            tokenNumeric = 79.006,
            unitPriceUsd = 1265.72
        )
    ),
    val transactions: List<WalletTransactionItem> = listOf(
        WalletTransactionItem(
            id = "tx1",
            title = "Withdraw Ethereum",
            date = "Today, 09:41 AM",
            amount = "-$1,929.00",
            isPositive = false,
            iconType = AssetIconType.ETHEREUM,
            category = "WITHDRAW",
            status = "Berhasil",
            referenceNote = "Transfer ke BCA • 8820192831"
        ),
        WalletTransactionItem(
            id = "tx2",
            title = "Received Binance",
            date = "Yesterday, 04:15 PM",
            amount = "+$4,250.00",
            isPositive = true,
            iconType = AssetIconType.BINANCE,
            category = "RECEIVE",
            status = "Berhasil",
            referenceNote = "Deposit Masuk dari 0x48F...91B"
        ),
        WalletTransactionItem(
            id = "tx3",
            title = "Tether usd Swap",
            date = "Oct 29, 11:20 AM",
            amount = "+$2,600.00",
            isPositive = true,
            iconType = AssetIconType.TETHER,
            category = "SWAP",
            status = "Berhasil",
            referenceNote = "Konversi Instan ETH → USDT"
        )
    ),
    val notifications: List<TuaiNotificationItem> = listOf(
        TuaiNotificationItem(
            id = "notif1",
            title = "Sinkronisasi Firebase Aktif",
            message = "Dompet Tuai #2678900085 telah terhubung dan diamankan dengan enkripsi real-time.",
            time = "Baru saja",
            isRead = false
        ),
        TuaiNotificationItem(
            id = "notif2",
            title = "Deposit Binance Diterima",
            message = "Saldo sebesar +$4,250.00 berhasil masuk ke portofolio Binance (BNB).",
            time = "Kemarin, 16:15",
            isRead = false
        ),
        TuaiNotificationItem(
            id = "notif3",
            title = "Target Panen Bulan Ini",
            message = "Portofolio Anda tumbuh +12.4% bulan ini. Saatnya panen hasil finansialmu!",
            time = "2 hari lalu",
            isRead = true
        )
    ),
    val savingsGoals: List<SavingsGoalItem> = listOf(
        SavingsGoalItem(
            id = "goal1",
            title = "Dana Panen Pensiun & Kebebasan Finansial",
            currentUsd = 18500.0,
            targetUsd = 25000.0,
            deadline = "Des 2027"
        ),
        SavingsGoalItem(
            id = "goal2",
            title = "Cadangan Likuiditas Kripto & Staking",
            currentUsd = 5367.0,
            targetUsd = 10000.0,
            deadline = "Agu 2027"
        )
    ),
    val toastMessage: String? = null
)

class TuaiViewModel(
    private val repository: TuaiFirebaseRepository = TuaiFirebaseRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(TuaiUiState())
    val uiState: StateFlow<TuaiUiState> = _uiState.asStateFlow()

    fun navigateTo(screen: TuaiScreen) {
        _uiState.update {
            it.copy(
                currentScreen = screen,
                activeModal = TuaiActiveModal.NONE
            )
        }
    }

    fun toggleRegisterMode() {
        _uiState.update {
            it.copy(
                isRegisterMode = !it.isRegisterMode,
                toastMessage = null
            )
        }
    }

    fun updateRegisterName(value: String) {
        _uiState.update { it.copy(registerName = value) }
    }

    fun updateEmailOrPhone(value: String) {
        _uiState.update { it.copy(emailOrPhone = value) }
    }

    fun updatePassword(value: String) {
        _uiState.update { it.copy(password = value) }
    }

    fun updateConfirmPassword(value: String) {
        _uiState.update { it.copy(confirmPassword = value) }
    }

    fun togglePasswordVisibility() {
        _uiState.update { it.copy(isPasswordVisible = !it.isPasswordVisible) }
    }

    fun submitAuth() {
        val state = _uiState.value
        if (state.isRegisterMode) {
            if (state.registerName.isBlank() && state.emailOrPhone.isBlank()) {
                _uiState.update {
                    it.copy(
                        isRegisterMode = false,
                        currentScreen = TuaiScreen.WALLET,
                        selectedBottomNav = 1,
                        toastMessage = "Akun baru berhasil dibuat!"
                    )
                }
                return
            }
            if (state.password.isNotEmpty() && state.password.length < 4) {
                showMessage("Kata sandi minimal 4 karakter")
                return
            }
            if (state.confirmPassword.isNotEmpty() && state.password != state.confirmPassword) {
                showMessage("Konfirmasi kata sandi tidak cocok")
                return
            }
            repository.registerWithEmail(
                name = state.registerName,
                emailOrPhone = state.emailOrPhone,
                password = state.password.ifBlank { "tuai1234" }
            ) { _, msg, _, displayName ->
                _uiState.update { current ->
                    val next = current.copy(
                        isRegisterMode = false,
                        currentScreen = TuaiScreen.WALLET,
                        selectedBottomNav = 1,
                        userName = displayName ?: current.userName,
                        userEmail = current.emailOrPhone.ifBlank { current.userEmail },
                        toastMessage = msg
                    )
                    repository.saveWalletState(next)
                    next
                }
            }
        } else {
            // Login Mode
            if (state.emailOrPhone.isBlank() && state.password.isBlank()) {
                // Mendukung akses langsung / pengujian instan tanpa kendala
                _uiState.update {
                    it.copy(
                        currentScreen = TuaiScreen.WALLET,
                        selectedBottomNav = 1
                    )
                }
                return
            }
            if (state.password.isNotEmpty() && state.password.length < 4) {
                showMessage("Kata sandi minimal 4 karakter")
                return
            }
            repository.signInWithEmail(
                emailOrPhone = state.emailOrPhone,
                password = state.password.ifBlank { "tuai1234" }
            ) { _, msg, _, displayName ->
                _uiState.update { current ->
                    val next = current.copy(
                        currentScreen = TuaiScreen.WALLET,
                        selectedBottomNav = 1,
                        userName = displayName ?: current.userName,
                        userEmail = current.emailOrPhone.ifBlank { current.userEmail },
                        toastMessage = msg
                    )
                    repository.saveWalletState(next)
                    next
                }
            }
        }
    }

    fun loginWithSocial(provider: String) {
        _uiState.update { current ->
            val next = current.copy(
                currentScreen = TuaiScreen.WALLET,
                selectedBottomNav = 1,
                toastMessage = "Berhasil masuk dengan $provider"
            )
            repository.saveWalletState(next)
            next
        }
    }

    fun openModal(modal: TuaiActiveModal) {
        _uiState.update { it.copy(activeModal = modal) }
    }

    fun closeModal() {
        _uiState.update {
            it.copy(
                activeModal = TuaiActiveModal.NONE,
                selectedAssetForModal = null,
                selectedTxForModal = null
            )
        }
    }

    fun openAssetDetail(asset: CryptoAssetItem) {
        _uiState.update {
            it.copy(
                selectedAssetForModal = asset,
                selectedWithdrawAssetId = asset.id,
                activeModal = TuaiActiveModal.ASSET_DETAIL
            )
        }
    }

    fun openTransactionDetail(tx: WalletTransactionItem) {
        _uiState.update {
            it.copy(
                selectedTxForModal = tx,
                activeModal = TuaiActiveModal.TRANSACTION_DETAIL
            )
        }
    }

    fun updateRecoveryInput(value: String) {
        _uiState.update { it.copy(recoveryInput = value) }
    }

    fun submitForgotPassword() {
        val target = _uiState.value.recoveryInput.ifBlank { _uiState.value.emailOrPhone }
        repository.sendPasswordReset(target) { message ->
            _uiState.update {
                it.copy(
                    activeModal = TuaiActiveModal.NONE,
                    toastMessage = message
                )
            }
        }
    }

    fun selectAssetTab(index: Int) {
        _uiState.update { it.copy(selectedAssetTab = index) }
    }

    fun selectBottomNav(index: Int) {
        _uiState.update { it.copy(selectedBottomNav = index, activeModal = TuaiActiveModal.NONE) }
    }

    fun selectTxFilter(filter: String) {
        _uiState.update { it.copy(selectedTxFilter = filter) }
    }

    fun selectChartTimeframe(timeframe: String) {
        _uiState.update { it.copy(selectedChartTimeframe = timeframe) }
    }

    fun selectChartAsset(assetId: String) {
        _uiState.update { it.copy(selectedChartAssetId = assetId) }
    }

    fun toggleBalancePrivacy() {
        _uiState.update { state ->
            val next = state.copy(isBalanceHidden = !state.isBalanceHidden)
            repository.saveWalletState(next)
            next
        }
    }

    fun toggleBiometricSetting() {
        _uiState.update { state ->
            val enabled = !state.biometricEnabled
            val next = state.copy(
                biometricEnabled = enabled,
                toastMessage = if (enabled) "Login Sidik Jari diaktifkan" else "Login Sidik Jari dinonaktifkan"
            )
            repository.saveWalletState(next)
            next
        }
    }

    fun toggleNotificationsSetting() {
        _uiState.update { state ->
            val enabled = !state.notificationsEnabled
            val next = state.copy(
                notificationsEnabled = enabled,
                toastMessage = if (enabled) "Notifikasi transaksi aktif" else "Notifikasi transaksi dibisukan"
            )
            repository.saveWalletState(next)
            next
        }
    }

    fun markAllNotificationsRead() {
        _uiState.update { state ->
            state.copy(
                notifications = state.notifications.map { it.copy(isRead = true) },
                toastMessage = "Semua notifikasi ditandai sudah dibaca"
            )
        }
    }

    fun toggleWithdrawStar() {
        _uiState.update { state ->
            val starred = !state.isWithdrawStarred
            val next = state.copy(
                isWithdrawStarred = starred,
                toastMessage = if (starred) {
                    "Disimpan ke penarikan favorit"
                } else {
                    "Dihapus dari penarikan favorit"
                }
            )
            repository.saveWalletState(next)
            next
        }
    }

    fun selectWithdrawAsset(assetId: String, destination: String? = null) {
        _uiState.update { state ->
            state.copy(
                selectedWithdrawAssetId = assetId,
                withdrawDestination = destination ?: state.withdrawDestination,
                activeModal = TuaiActiveModal.NONE
            )
        }
    }

    fun onKeypadDigit(digit: String) {
        _uiState.update { state ->
            val current = state.withdrawAmount
            val next = if (current == "0.00" || current == "0" || current == "19,29.00") {
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

    fun cycleQuickWithdrawAmount() {
        _uiState.update { state ->
            val presets = listOf("100.00", "500.00", "1,000.00", formatCurrencyPlain(state.walletBalanceNumeric))
            val currentIdx = presets.indexOf(state.withdrawAmount)
            val nextAmount = presets[(currentIdx + 1) % presets.size]
            state.copy(
                withdrawAmount = nextAmount,
                toastMessage = "Nominal cepat: $$nextAmount"
            )
        }
    }

    fun onKeypadBackspace() {
        _uiState.update { state ->
            val current = state.withdrawAmount
            val next = if (current.length <= 1 || current == "19,29.00") {
                "0"
            } else {
                current.dropLast(1)
            }
            state.copy(withdrawAmount = next)
        }
    }

    fun completeWithdraw() {
        val state = _uiState.value
        val numericAmount = parseWithdrawAmount(state.withdrawAmount)
        if (numericAmount <= 0.0) {
            showMessage("Masukkan nominal penarikan lebih dari $0")
            return
        }
        if (numericAmount > state.walletBalanceNumeric) {
            showMessage("Saldo tidak mencukupi (Maksimal ${state.walletBalance})")
            return
        }

        val targetAsset = state.assets.find { it.id == state.selectedWithdrawAssetId } ?: state.assets.first()
        val newBalanceNum = (state.walletBalanceNumeric - numericAmount).coerceAtLeast(0.0)
        val formattedBalance = formatHeadlineBalance(newBalanceNum)
        val formattedWithdraw = formatCurrencyWithCents(numericAmount)

        val newTx = WalletTransactionItem(
            id = "tx_${System.currentTimeMillis()}",
            title = "Withdraw ${targetAsset.name}",
            date = "Just now",
            amount = "-$$formattedWithdraw",
            isPositive = false,
            iconType = targetAsset.iconType,
            category = "WITHDRAW",
            status = "Berhasil",
            referenceNote = "Tujuan: ${state.withdrawDestination}"
        )

        val newNotif = TuaiNotificationItem(
            id = "notif_${System.currentTimeMillis()}",
            title = "Penarikan Dana Berhasil",
            message = "Withdraw $$formattedWithdraw dari ${targetAsset.name} ke ${state.withdrawDestination} berhasil diproses.",
            time = "Baru saja",
            isRead = false
        )

        _uiState.update { current ->
            val next = current.copy(
                walletBalanceNumeric = newBalanceNum,
                walletBalance = formattedBalance,
                transactions = listOf(newTx) + current.transactions,
                notifications = listOf(newNotif) + current.notifications,
                currentScreen = TuaiScreen.WALLET,
                selectedBottomNav = 1,
                withdrawAmount = "19,29.00",
                lastSyncedLabel = "Baru saja • Tersinkron ke Firebase",
                toastMessage = "Withdraw $${state.withdrawAmount} berhasil diproses!"
            )
            repository.saveWalletState(next)
            next
        }
    }

    fun updateTopUpAmount(value: String) {
        _uiState.update { it.copy(topUpAmountInput = value.filter { ch -> ch.isDigit() || ch == '.' }) }
    }

    fun updateTopUpMethod(method: String) {
        _uiState.update { it.copy(topUpMethod = method) }
    }

    fun updateTopUpTargetAsset(assetId: String) {
        _uiState.update { it.copy(topUpTargetAssetId = assetId) }
    }

    fun submitTopUp() {
        val state = _uiState.value
        val amount = state.topUpAmountInput.toDoubleOrNull() ?: 0.0
        if (amount <= 0.0) {
            showMessage("Masukkan nominal Top Up yang valid")
            return
        }
        val targetAsset = state.assets.find { it.id == state.topUpTargetAssetId } ?: state.assets.first()
        val newBalanceNum = state.walletBalanceNumeric + amount
        val formattedBalance = formatHeadlineBalance(newBalanceNum)
        val formattedAmount = formatCurrencyWithCents(amount)

        val newTx = WalletTransactionItem(
            id = "tx_${System.currentTimeMillis()}",
            title = "Top Up ${targetAsset.name}",
            date = "Just now",
            amount = "+$$formattedAmount",
            isPositive = true,
            iconType = targetAsset.iconType,
            category = "RECEIVE",
            status = "Berhasil",
            referenceNote = "Metode: ${state.topUpMethod}"
        )

        val newNotif = TuaiNotificationItem(
            id = "notif_${System.currentTimeMillis()}",
            title = "Top Up Saldo Berhasil",
            message = "Dana sebesar +$$formattedAmount via ${state.topUpMethod} telah masuk ke dompet Anda.",
            time = "Baru saja",
            isRead = false
        )

        _uiState.update { current ->
            val next = current.copy(
                walletBalanceNumeric = newBalanceNum,
                walletBalance = formattedBalance,
                transactions = listOf(newTx) + current.transactions,
                notifications = listOf(newNotif) + current.notifications,
                activeModal = TuaiActiveModal.NONE,
                lastSyncedLabel = "Baru saja • Tersinkron ke Firebase",
                toastMessage = "Top Up +$$formattedAmount berhasil ditambahkan!"
            )
            repository.saveWalletState(next)
            next
        }
    }

    fun simulateReceiveFunds(amountUsd: Double = 500.0) {
        val state = _uiState.value
        val newBalanceNum = state.walletBalanceNumeric + amountUsd
        val formattedBalance = formatHeadlineBalance(newBalanceNum)
        val formattedAmount = formatCurrencyWithCents(amountUsd)

        val newTx = WalletTransactionItem(
            id = "tx_${System.currentTimeMillis()}",
            title = "Received Ethereum",
            date = "Just now",
            amount = "+$$formattedAmount",
            isPositive = true,
            iconType = AssetIconType.ETHEREUM,
            category = "RECEIVE",
            status = "Berhasil",
            referenceNote = "Transfer Masuk via QR Wallet #${state.walletId}"
        )

        _uiState.update { current ->
            val next = current.copy(
                walletBalanceNumeric = newBalanceNum,
                walletBalance = formattedBalance,
                transactions = listOf(newTx) + current.transactions,
                activeModal = TuaiActiveModal.NONE,
                lastSyncedLabel = "Baru saja • Tersinkron ke Firebase",
                toastMessage = "Dana +$$formattedAmount berhasil diterima!"
            )
            repository.saveWalletState(next)
            next
        }
    }

    fun updateSwapAmount(value: String) {
        _uiState.update { it.copy(swapAmountInput = value.filter { ch -> ch.isDigit() || ch == '.' }) }
    }

    fun flipSwapAssets() {
        _uiState.update {
            it.copy(
                swapFromAssetId = it.swapToAssetId,
                swapToAssetId = it.swapFromAssetId
            )
        }
    }

    fun submitSwap() {
        val state = _uiState.value
        val amountUsd = state.swapAmountInput.toDoubleOrNull() ?: 0.0
        if (amountUsd <= 0.0) {
            showMessage("Masukkan nominal Swap yang valid")
            return
        }
        val fromAsset = state.assets.find { it.id == state.swapFromAssetId } ?: state.assets[0]
        val toAsset = state.assets.find { it.id == state.swapToAssetId } ?: state.assets[2]
        val formattedAmount = formatCurrencyWithCents(amountUsd)

        val newTx = WalletTransactionItem(
            id = "tx_${System.currentTimeMillis()}",
            title = "${fromAsset.symbol} → ${toAsset.symbol} Swap",
            date = "Just now",
            amount = "+$$formattedAmount",
            isPositive = true,
            iconType = toAsset.iconType,
            category = "SWAP",
            status = "Berhasil",
            referenceNote = "Konversi Instan ${fromAsset.name} ke ${toAsset.name}"
        )

        val newNotif = TuaiNotificationItem(
            id = "notif_${System.currentTimeMillis()}",
            title = "Swap Aset Berhasil",
            message = "Berhasil menukar $$formattedAmount dari ${fromAsset.name} ke ${toAsset.name}.",
            time = "Baru saja",
            isRead = false
        )

        _uiState.update { current ->
            val next = current.copy(
                transactions = listOf(newTx) + current.transactions,
                notifications = listOf(newNotif) + current.notifications,
                activeModal = TuaiActiveModal.NONE,
                selectedAssetTab = 1,
                lastSyncedLabel = "Baru saja • Tersinkron ke Firebase",
                toastMessage = "Swap $$formattedAmount (${fromAsset.symbol} → ${toAsset.symbol}) berhasil!"
            )
            repository.saveWalletState(next)
            next
        }
    }

    fun contributeToSavingsGoal(goalId: String, amountUsd: Double = 250.0) {
        _uiState.update { state ->
            val updatedGoals = state.savingsGoals.map { goal ->
                if (goal.id == goalId) {
                    goal.copy(currentUsd = (goal.currentUsd + amountUsd).coerceAtMost(goal.targetUsd))
                } else {
                    goal
                }
            }
            val next = state.copy(
                savingsGoals = updatedGoals,
                toastMessage = "Berhasil menambah panen tabungan +$${amountUsd.toInt()}!"
            )
            repository.saveWalletState(next)
            next
        }
    }

    fun syncFirebaseManual() {
        val current = _uiState.value
        repository.saveWalletState(current)
        _uiState.update {
            it.copy(
                isFirebaseSynced = true,
                lastSyncedLabel = "Baru saja • Tersinkron ke Firebase Cloud",
                toastMessage = "Data dompet berhasil disinkronkan ke Firebase!"
            )
        }
    }

    fun resetDemoData() {
        val fresh = TuaiUiState(
            currentScreen = TuaiScreen.WALLET,
            selectedBottomNav = 1,
            toastMessage = "Data dompet dikembalikan ke posisi awal ($23,867)"
        )
        repository.saveWalletState(fresh)
        _uiState.value = fresh
    }

    fun logout() {
        repository.signOut()
        _uiState.update {
            it.copy(
                currentScreen = TuaiScreen.LOGIN,
                password = "",
                confirmPassword = "",
                activeModal = TuaiActiveModal.NONE,
                toastMessage = "Anda telah keluar dari akun"
            )
        }
    }

    fun showMessage(msg: String) {
        _uiState.update { it.copy(toastMessage = msg) }
    }

    fun clearMessage() {
        _uiState.update { it.copy(toastMessage = null) }
    }

    companion object {
        fun parseWithdrawAmount(raw: String): Double {
            if (raw == "19,29.00") return 1929.00
            val cleaned = raw.replace(",", "").replace("$", "").trim()
            return cleaned.toDoubleOrNull() ?: 0.0
        }

        fun formatHeadlineBalance(amount: Double): String {
            val nf = NumberFormat.getNumberInstance(Locale.US).apply {
                maximumFractionDigits = if (amount % 1.0 == 0.0) 0 else 2
                minimumFractionDigits = if (amount % 1.0 == 0.0) 0 else 2
            }
            return "$${nf.format(amount)}"
        }

        fun formatCurrencyWithCents(amount: Double): String {
            val nf = NumberFormat.getNumberInstance(Locale.US).apply {
                minimumFractionDigits = 2
                maximumFractionDigits = 2
            }
            return nf.format(amount)
        }

        fun formatCurrencyPlain(amount: Double): String {
            val nf = NumberFormat.getNumberInstance(Locale.US).apply {
                minimumFractionDigits = 0
                maximumFractionDigits = 0
            }
            return nf.format(amount)
        }
    }
}
