package com.example.viewmodel

import android.content.Context
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
    BCA,
    MANDIRI,
    BRIMO,
    GOPAY,
    DANA,
    OVO,
    SHOPEEPAY,
    SEABANK,
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
    PROFILE_ACCOUNT,
    PLUGIN_MANAGER
}

data class CryptoAssetItem(
    val id: String,
    val name: String,
    val percentageAndValue: String,
    val tokenAmount: String,
    val totalUsdValue: String,
    val iconType: AssetIconType,
    val symbol: String = when (iconType) {
        AssetIconType.BCA -> "BCA"
        AssetIconType.MANDIRI -> "MANDIRI"
        AssetIconType.BRIMO -> "BRIMO"
        AssetIconType.GOPAY -> "GOPAY"
        AssetIconType.DANA -> "DANA"
        AssetIconType.OVO -> "OVO"
        AssetIconType.SHOPEEPAY -> "SHOPEEPAY"
        AssetIconType.SEABANK -> "SEABANK"
        AssetIconType.ETHEREUM -> "ETH"
        AssetIconType.BINANCE -> "BNB"
        AssetIconType.TETHER -> "USDT"
    },
    val tokenNumeric: Double = 1_000_000.0,
    val unitPriceUsd: Double = 1.0,
    val change24h: String = "Terhubung",
    val walletCategory: String = when (iconType) {
        AssetIconType.BCA,
        AssetIconType.MANDIRI,
        AssetIconType.BRIMO,
        AssetIconType.SEABANK -> "BANK"
        else -> "E-WALLET"
    },
    val accountNumber: String = "0812-3456-7890",
    val allocationRole: String = "Operasional Harian",
    val isConnected: Boolean = true,
    val packageName: String = when (iconType) {
        AssetIconType.BCA -> "com.bca"
        AssetIconType.MANDIRI -> "id.bmri.livin"
        AssetIconType.BRIMO -> "id.co.bri.brimo"
        AssetIconType.GOPAY -> "com.gojek.app"
        AssetIconType.DANA -> "id.dana"
        AssetIconType.OVO -> "ovo.id"
        AssetIconType.SHOPEEPAY -> "com.shopee.id"
        AssetIconType.SEABANK -> "id.co.bankbkemobile.digitalbank"
        else -> ""
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
    val referenceNote: String = "Sinkronisasi Otomatis Plugin Tuai"
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
    val deadline: String,
    val linkedWalletsLabel: String = "BCA & Livin' Mandiri"
)

fun defaultWalletPlugins(): List<CryptoAssetItem> {
    val rawList = listOf(
        CryptoAssetItem(
            id = "bca",
            name = "BCA (myBCA)",
            percentageAndValue = "49,1% • Tabungan & Pos Utama",
            tokenAmount = "Rp 14.500.000",
            totalUsdValue = "Bank • 8820-1928-31",
            iconType = AssetIconType.BCA,
            symbol = "BCA",
            tokenNumeric = 14_500_000.0,
            walletCategory = "BANK",
            accountNumber = "8820-1928-31",
            allocationRole = "Tabungan & Pos Utama",
            isConnected = true
        ),
        CryptoAssetItem(
            id = "mandiri",
            name = "Livin' by Mandiri",
            percentageAndValue = "27,9% • Dana Darurat & Investasi",
            tokenAmount = "Rp 8.250.000",
            totalUsdValue = "Bank • 137-00-982145",
            iconType = AssetIconType.MANDIRI,
            symbol = "MANDIRI",
            tokenNumeric = 8_250_000.0,
            walletCategory = "BANK",
            accountNumber = "137-00-982145",
            allocationRole = "Dana Darurat & Investasi",
            isConnected = true
        ),
        CryptoAssetItem(
            id = "brimo",
            name = "BRImo (Bank BRI)",
            percentageAndValue = "15,6% • Operasional & Tagihan",
            tokenAmount = "Rp 4.600.000",
            totalUsdValue = "Bank • 0341-01-029384",
            iconType = AssetIconType.BRIMO,
            symbol = "BRIMO",
            tokenNumeric = 4_600_000.0,
            walletCategory = "BANK",
            accountNumber = "0341-01-029384",
            allocationRole = "Operasional & Tagihan",
            isConnected = true
        ),
        CryptoAssetItem(
            id = "gopay",
            name = "GoPay",
            percentageAndValue = "4,6% • Transport & Makan Harian",
            tokenAmount = "Rp 1.350.000",
            totalUsdValue = "E-Wallet • 0812-3456-7890",
            iconType = AssetIconType.GOPAY,
            symbol = "GOPAY",
            tokenNumeric = 1_350_000.0,
            walletCategory = "E-WALLET",
            accountNumber = "0812-3456-7890",
            allocationRole = "Transport & Makan Harian",
            isConnected = true
        ),
        CryptoAssetItem(
            id = "dana",
            name = "DANA",
            percentageAndValue = "2,9% • Belanja & QRIS Harian",
            tokenAmount = "Rp 850.000",
            totalUsdValue = "E-Wallet • 0812-3456-7890",
            iconType = AssetIconType.DANA,
            symbol = "DANA",
            tokenNumeric = 850_000.0,
            walletCategory = "E-WALLET",
            accountNumber = "0812-3456-7890",
            allocationRole = "Belanja & QRIS Harian",
            isConnected = true
        ),
        CryptoAssetItem(
            id = "ovo",
            name = "OVO",
            percentageAndValue = "Plugin Tersedia • Hiburan & Grab",
            tokenAmount = "Rp 620.000",
            totalUsdValue = "E-Wallet • 0812-3456-7890",
            iconType = AssetIconType.OVO,
            symbol = "OVO",
            tokenNumeric = 620_000.0,
            walletCategory = "E-WALLET",
            accountNumber = "0812-3456-7890",
            allocationRole = "Hiburan & Langganan",
            isConnected = false
        ),
        CryptoAssetItem(
            id = "shopeepay",
            name = "ShopeePay",
            percentageAndValue = "Plugin Tersedia • Belanja Online",
            tokenAmount = "Rp 450.000",
            totalUsdValue = "E-Wallet • 0812-3456-7890",
            iconType = AssetIconType.SHOPEEPAY,
            symbol = "SPAY",
            tokenNumeric = 450_000.0,
            walletCategory = "E-WALLET",
            accountNumber = "0812-3456-7890",
            allocationRole = "Belanja E-Commerce",
            isConnected = false
        ),
        CryptoAssetItem(
            id = "seabank",
            name = "SeaBank",
            percentageAndValue = "Plugin Tersedia • Tabungan Cair",
            tokenAmount = "Rp 3.500.000",
            totalUsdValue = "Bank • 9012-8834-1920",
            iconType = AssetIconType.SEABANK,
            symbol = "SEABANK",
            tokenNumeric = 3_500_000.0,
            walletCategory = "BANK",
            accountNumber = "9012-8834-1920",
            allocationRole = "Tabungan Bunga Harian",
            isConnected = false
        )
    )
    return TuaiViewModel.recalculateAssetsBreakdown(rawList)
}

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
    val walletBalanceNumeric: Double = 29_550_000.0,
    val walletBalance: String = "Rp 29.550.000",
    val bankBalanceNumeric: Double = 27_350_000.0,
    val ewalletBalanceNumeric: Double = 2_200_000.0,
    val connectedPluginCount: Int = 5,
    val financialHealthStatus: String = "Sehat & Terkendali (92% Bank • 8% E-Wallet)",
    val financialConditionAdvice: String = "Saldo kas harian di GoPay & DANA (Rp 2,20 Jt) cukup untuk operasional, sementara Rp 27,35 Jt aman di BCA, Mandiri & BRImo.",
    val walletId: String = "2678900085",
    val walletAddressHex: String = "ID-TUAI-882019",
    val isBalanceHidden: Boolean = false,
    val biometricEnabled: Boolean = true,
    val notificationsEnabled: Boolean = true,
    val selectedAssetTab: Int = 0, // 0 = Dompet & Bank, 1 = Mutasi Gabungan
    val selectedWalletCategoryFilter: String = "ALL", // ALL, BANK, E-WALLET
    val selectedBottomNav: Int = 1, // 0 = Kondisi, 1 = Dompet, 2 = Analitik, 3 = Plugin
    val selectedTxFilter: String = "ALL", // ALL, IN, OUT, SWAP
    val selectedChartTimeframe: String = "1M", // 24H, 1W, 1M, 1Y
    val selectedChartAssetId: String = "all",
    val withdrawAmount: String = "150.000",
    val isWithdrawStarred: Boolean = false,
    val selectedWithdrawAssetId: String = "gopay",
    val withdrawDestination: String = "QRIS & Makan Harian",
    val activeModal: TuaiActiveModal = TuaiActiveModal.NONE,
    val selectedAssetForModal: CryptoAssetItem? = null,
    val selectedTxForModal: WalletTransactionItem? = null,
    val manualBalanceInput: String = "",
    val manualAccountInput: String = "",
    val customPluginNameInput: String = "",
    val customPluginCategoryInput: String = "E-WALLET",
    val customPluginAccountInput: String = "",
    val customPluginBalanceInput: String = "500000",
    val swapFromAssetId: String = "bca",
    val swapToAssetId: String = "gopay",
    val swapAmountInput: String = "500000",
    val topUpAmountInput: String = "500000",
    val topUpMethod: String = "Transfer dari BCA (myBCA)",
    val topUpTargetAssetId: String = "gopay",
    val recoveryInput: String = "",
    val isFirebaseSynced: Boolean = true,
    val lastSyncedLabel: String = "Baru saja • 5 Plugin Dompet & Bank Aktif",
    val assets: List<CryptoAssetItem> = defaultWalletPlugins(),
    val transactions: List<WalletTransactionItem> = listOf(
        WalletTransactionItem(
            id = "tx1",
            title = "Top Up GoPay dari BCA",
            date = "Hari ini, 09:41 WIB",
            amount = "+Rp 500.000",
            isPositive = true,
            iconType = AssetIconType.GOPAY,
            category = "SWAP",
            status = "Berhasil",
            referenceNote = "Alokasi Kas Harian: BCA → GoPay"
        ),
        WalletTransactionItem(
            id = "tx2",
            title = "Gaji & Pemasukan Mandiri",
            date = "Kemarin, 16:15 WIB",
            amount = "+Rp 8.250.000",
            isPositive = true,
            iconType = AssetIconType.MANDIRI,
            category = "RECEIVE",
            status = "Berhasil",
            referenceNote = "Rekening Livin' Mandiri • 137-00-982145"
        ),
        WalletTransactionItem(
            id = "tx3",
            title = "Bayar QRIS Makan & Kopi (DANA)",
            date = "Kemarin, 12:30 WIB",
            amount = "-Rp 85.000",
            isPositive = false,
            iconType = AssetIconType.DANA,
            category = "WITHDRAW",
            status = "Berhasil",
            referenceNote = "Pos Belanja & QRIS Harian via DANA"
        ),
        WalletTransactionItem(
            id = "tx4",
            title = "Tagihan Listrik & Internet (BRImo)",
            date = "28 Okt, 19:20 WIB",
            amount = "-Rp 420.000",
            isPositive = false,
            iconType = AssetIconType.BRIMO,
            category = "WITHDRAW",
            status = "Berhasil",
            referenceNote = "Pos Operasional & Tagihan via BRImo"
        )
    ),
    val notifications: List<TuaiNotificationItem> = listOf(
        TuaiNotificationItem(
            id = "notif1",
            title = "5 Plugin Dompet & Bank Terhubung",
            message = "BCA, Livin' Mandiri, BRImo, GoPay, dan DANA aktif disinkronkan secara real-time.",
            time = "Baru saja",
            isRead = false
        ),
        TuaiNotificationItem(
            id = "notif2",
            title = "Analisis Kondisi Keuanganmu",
            message = "Total uangmu saat ini Rp 29.550.000. Sebanyak 92,6% tersimpan aman di rekening Bank.",
            time = "10 menit lalu",
            isRead = false
        ),
        TuaiNotificationItem(
            id = "notif3",
            title = "Pengingat Alokasi Kas E-Wallet",
            message = "Saldo GoPay (Rp 1,35 Jt) & DANA (Rp 850 Rb) berada di batas aman untuk pengeluaran minggu ini.",
            time = "Kemarin",
            isRead = true
        )
    ),
    val savingsGoals: List<SavingsGoalItem> = listOf(
        SavingsGoalItem(
            id = "goal1",
            title = "Pos Dana Darurat & Tabungan Aman",
            currentUsd = 22_750_000.0,
            targetUsd = 30_000_000.0,
            deadline = "BCA & Livin' Mandiri",
            linkedWalletsLabel = "Tersimpan di BCA & Mandiri"
        ),
        SavingsGoalItem(
            id = "goal2",
            title = "Jatah Kas Harian & QRIS (E-Wallet)",
            currentUsd = 2_200_000.0,
            targetUsd = 4_000_000.0,
            deadline = "GoPay & DANA",
            linkedWalletsLabel = "Tersedia di GoPay, DANA & E-Wallet"
        ),
        SavingsGoalItem(
            id = "goal3",
            title = "Pos Tagihan Rutin & Operasional",
            currentUsd = 4_600_000.0,
            targetUsd = 5_000_000.0,
            deadline = "BRImo (BRI)",
            linkedWalletsLabel = "Siap pakai di BRImo & Bank"
        )
    ),
    val toastMessage: String? = null
) {
    val connectedAssets: List<CryptoAssetItem>
        get() = assets.filter { it.isConnected }
}

class TuaiViewModel(
    private val repository: TuaiFirebaseRepository = TuaiFirebaseRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(syncAggregatedBalances(TuaiUiState()))
    val uiState: StateFlow<TuaiUiState> = _uiState.asStateFlow()
    private var contextInitialized = false

    fun attachContext(context: Context) {
        if (!contextInitialized) {
            contextInitialized = true
            repository.attachContext(context)
            val saved = repository.loadSavedWalletState(_uiState.value.walletId)
            if (saved != null) {
                _uiState.update { current ->
                    saved.copy(currentScreen = current.currentScreen)
                }
            }
        }
    }

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
                        toastMessage = "Akun Tuai berhasil dibuat! Semua plugin dompet siap digunakan."
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
            if (state.emailOrPhone.isBlank() && state.password.isBlank()) {
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
                manualBalanceInput = asset.tokenNumeric.toLong().toString(),
                manualAccountInput = asset.accountNumber,
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

    // Plugin Hub & Wallet Connection Controls
    fun togglePluginConnection(assetId: String) {
        _uiState.update { state ->
            val target = state.assets.find { it.id == assetId } ?: return@update state
            val connectedCount = state.assets.count { it.isConnected }
            if (target.isConnected && connectedCount <= 1) {
                return@update state.copy(
                    toastMessage = "Minimal 1 dompet/bank harus tetap terhubung"
                )
            }
            val nowConnected = !target.isConnected
            val updatedAssets = state.assets.map { item ->
                if (item.id == assetId) item.copy(isConnected = nowConnected) else item
            }
            val refreshedTarget = updatedAssets.find { it.id == assetId }
            val next = syncAggregatedBalances(
                state.copy(
                    assets = updatedAssets,
                    selectedAssetForModal = if (state.selectedAssetForModal?.id == assetId) {
                        refreshedTarget
                    } else {
                        state.selectedAssetForModal
                    },
                    toastMessage = if (nowConnected) {
                        "Plugin ${target.name} berhasil dihubungkan (+${formatHeadlineBalance(target.tokenNumeric)})"
                    } else {
                        "Plugin ${target.name} diputuskan dari total saldo"
                    }
                )
            )
            repository.saveWalletState(next)
            next
        }
    }

    fun updateManualBalanceInput(value: String) {
        _uiState.update {
            it.copy(manualBalanceInput = value.filter { ch -> ch.isDigit() })
        }
    }

    fun updateManualAccountInput(value: String) {
        _uiState.update {
            it.copy(manualAccountInput = value)
        }
    }

    fun saveManualWalletBalance(assetId: String) {
        val state = _uiState.value
        val newBalance = state.manualBalanceInput.toDoubleOrNull()
        if (newBalance == null || newBalance < 0.0) {
            showMessage("Masukkan nominal saldo Rupiah yang valid")
            return
        }
        val target = state.assets.find { it.id == assetId } ?: return
        val newAcc = state.manualAccountInput.trim().ifBlank { target.accountNumber }
        val diff = newBalance - target.tokenNumeric

        val updatedAssets = state.assets.map { item ->
            if (item.id == assetId) {
                item.copy(
                    tokenNumeric = newBalance,
                    accountNumber = newAcc,
                    isConnected = true
                )
            } else {
                item
            }
        }

        val adjustmentTx = if (kotlin.math.abs(diff) >= 1.0) {
            WalletTransactionItem(
                id = "tx_${System.currentTimeMillis()}",
                title = "Penyesuaian Saldo ${target.name}",
                date = "Baru saja",
                amount = if (diff >= 0) "+${formatHeadlineBalance(diff)}" else "-${formatHeadlineBalance(-diff)}",
                isPositive = diff >= 0,
                iconType = target.iconType,
                category = if (diff >= 0) "RECEIVE" else "WITHDRAW",
                status = "Tersinkron",
                referenceNote = "Update Saldo Plugin ${target.name} menjadi ${formatHeadlineBalance(newBalance)}"
            )
        } else null

        _uiState.update { current ->
            val next = syncAggregatedBalances(
                current.copy(
                    assets = updatedAssets,
                    transactions = if (adjustmentTx != null) listOf(adjustmentTx) + current.transactions else current.transactions,
                    activeModal = TuaiActiveModal.NONE,
                    selectedAssetForModal = null,
                    toastMessage = "Saldo ${target.name} diperbarui menjadi ${formatHeadlineBalance(newBalance)}!"
                )
            )
            repository.saveWalletState(next)
            next
        }
    }

    fun cycleWalletAllocationRole(assetId: String) {
        val roles = listOf(
            "Tabungan & Pos Utama",
            "Dana Darurat & Investasi",
            "Operasional & Tagihan",
            "Transport & Makan Harian",
            "Belanja & QRIS Harian",
            "Hiburan & Gaya Hidup"
        )
        _uiState.update { state ->
            val target = state.assets.find { it.id == assetId } ?: return@update state
            val nextIdx = (roles.indexOf(target.allocationRole) + 1) % roles.size
            val newRole = roles[nextIdx]
            val updatedAssets = state.assets.map { item ->
                if (item.id == assetId) item.copy(allocationRole = newRole) else item
            }
            val next = syncAggregatedBalances(
                state.copy(
                    assets = updatedAssets,
                    selectedAssetForModal = updatedAssets.find { it.id == assetId },
                    toastMessage = "Pos ${target.name} diatur ke: $newRole"
                )
            )
            repository.saveWalletState(next)
            next
        }
    }

    fun updateCustomPluginName(value: String) {
        _uiState.update { it.copy(customPluginNameInput = value) }
    }

    fun updateCustomPluginCategory(category: String) {
        _uiState.update { it.copy(customPluginCategoryInput = category) }
    }

    fun updateCustomPluginAccount(value: String) {
        _uiState.update { it.copy(customPluginAccountInput = value) }
    }

    fun updateCustomPluginBalance(value: String) {
        _uiState.update { it.copy(customPluginBalanceInput = value.filter { ch -> ch.isDigit() }) }
    }

    fun addNewCustomPlugin() {
        val state = _uiState.value
        val cleanName = state.customPluginNameInput.trim()
        if (cleanName.isBlank()) {
            showMessage("Masukkan nama Bank atau E-Wallet (misal: BNI, BSI, LinkAja)")
            return
        }
        val balance = state.customPluginBalanceInput.toDoubleOrNull() ?: 0.0
        val account = state.customPluginAccountInput.trim().ifBlank { "0812-9988-7766" }
        val category = state.customPluginCategoryInput
        val iconType = when {
            cleanName.contains("bca", ignoreCase = true) -> AssetIconType.BCA
            cleanName.contains("mandiri", ignoreCase = true) || cleanName.contains("livin", ignoreCase = true) -> AssetIconType.MANDIRI
            cleanName.contains("bri", ignoreCase = true) -> AssetIconType.BRIMO
            cleanName.contains("gopay", ignoreCase = true) || cleanName.contains("gojek", ignoreCase = true) -> AssetIconType.GOPAY
            cleanName.contains("dana", ignoreCase = true) -> AssetIconType.DANA
            cleanName.contains("ovo", ignoreCase = true) -> AssetIconType.OVO
            cleanName.contains("shopee", ignoreCase = true) -> AssetIconType.SHOPEEPAY
            category == "BANK" -> AssetIconType.SEABANK
            else -> AssetIconType.GOPAY
        }
        val newId = "plugin_${System.currentTimeMillis()}"
        val newPlugin = CryptoAssetItem(
            id = newId,
            name = cleanName,
            percentageAndValue = "",
            tokenAmount = formatHeadlineBalance(balance),
            totalUsdValue = "$category • $account",
            iconType = iconType,
            symbol = cleanName.take(7).uppercase(),
            tokenNumeric = balance,
            walletCategory = category,
            accountNumber = account,
            allocationRole = if (category == "BANK") "Tabungan & Cadangan" else "Operasional & QRIS",
            isConnected = true
        )

        _uiState.update { current ->
            val next = syncAggregatedBalances(
                current.copy(
                    assets = current.assets + newPlugin,
                    customPluginNameInput = "",
                    customPluginAccountInput = "",
                    customPluginBalanceInput = "500000",
                    toastMessage = "Plugin $cleanName (${formatHeadlineBalance(balance)}) berhasil ditambahkan!"
                )
            )
            repository.saveWalletState(next)
            next
        }
    }

    fun selectWalletCategoryFilter(filter: String) {
        _uiState.update { it.copy(selectedWalletCategoryFilter = filter) }
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
                toastMessage = if (enabled) "Notifikasi mutasi lintas dompet aktif" else "Notifikasi transaksi dibisukan"
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
                    "Disimpan ke transaksi favorit"
                } else {
                    "Dihapus dari transaksi favorit"
                }
            )
            repository.saveWalletState(next)
            next
        }
    }

    fun selectWithdrawAsset(assetId: String, destination: String? = null) {
        _uiState.update { state ->
            val asset = state.assets.find { it.id == assetId }
            state.copy(
                selectedWithdrawAssetId = assetId,
                withdrawDestination = destination ?: asset?.allocationRole ?: state.withdrawDestination,
                activeModal = TuaiActiveModal.NONE
            )
        }
    }

    fun updateWithdrawDestination(destination: String) {
        _uiState.update { it.copy(withdrawDestination = destination) }
    }

    fun onKeypadDigit(digit: String) {
        _uiState.update { state ->
            val currentCleaned = state.withdrawAmount.filter { it.isDigit() }
            val nextRaw = if (currentCleaned == "0" || state.withdrawAmount == "150.000") {
                digit
            } else if (currentCleaned.length < 9) {
                currentCleaned + digit
            } else {
                currentCleaned
            }
            val numeric = nextRaw.toDoubleOrNull() ?: 0.0
            state.copy(withdrawAmount = formatCurrencyPlain(numeric))
        }
    }

    fun onKeypadReset() {
        _uiState.update { it.copy(withdrawAmount = "150.000") }
    }

    fun cycleQuickWithdrawAmount() {
        _uiState.update { state ->
            val presets = listOf("50.000", "150.000", "500.000", "1.000.000")
            val currentIdx = presets.indexOf(state.withdrawAmount)
            val nextAmount = presets[(currentIdx + 1) % presets.size]
            state.copy(
                withdrawAmount = nextAmount,
                toastMessage = "Nominal cepat: Rp $nextAmount"
            )
        }
    }

    fun onKeypadBackspace() {
        _uiState.update { state ->
            val currentCleaned = state.withdrawAmount.filter { it.isDigit() }
            val nextRaw = if (currentCleaned.length <= 1) {
                "0"
            } else {
                currentCleaned.dropLast(1)
            }
            val numeric = nextRaw.toDoubleOrNull() ?: 0.0
            state.copy(withdrawAmount = formatCurrencyPlain(numeric))
        }
    }

    fun completeWithdraw() {
        val state = _uiState.value
        val numericAmount = parseWithdrawAmount(state.withdrawAmount)
        if (numericAmount <= 0.0) {
            showMessage("Masukkan nominal lebih dari Rp 0")
            return
        }
        val targetAsset = state.connectedAssets.find { it.id == state.selectedWithdrawAssetId }
            ?: state.connectedAssets.firstOrNull()
            ?: state.assets.first()

        if (numericAmount > targetAsset.tokenNumeric) {
            showMessage("Saldo ${targetAsset.name} tidak mencukupi (Tersedia ${targetAsset.tokenAmount})")
            return
        }

        val updatedAssets = state.assets.map { item ->
            if (item.id == targetAsset.id) {
                item.copy(tokenNumeric = (item.tokenNumeric - numericAmount).coerceAtLeast(0.0))
            } else {
                item
            }
        }
        val formattedWithdraw = formatHeadlineBalance(numericAmount)

        val newTx = WalletTransactionItem(
            id = "tx_${System.currentTimeMillis()}",
            title = "Keluar dari ${targetAsset.name}",
            date = "Baru saja",
            amount = "-$formattedWithdraw",
            isPositive = false,
            iconType = targetAsset.iconType,
            category = "WITHDRAW",
            status = "Berhasil",
            referenceNote = "${state.withdrawDestination} • ${targetAsset.name}"
        )

        val newNotif = TuaiNotificationItem(
            id = "notif_${System.currentTimeMillis()}",
            title = "Mutasi Keluar ${targetAsset.name}",
            message = "Pengeluaran/Transfer $formattedWithdraw (${state.withdrawDestination}) dari ${targetAsset.name} berhasil dicatat.",
            time = "Baru saja",
            isRead = false
        )

        _uiState.update { current ->
            val next = syncAggregatedBalances(
                current.copy(
                    assets = updatedAssets,
                    transactions = listOf(newTx) + current.transactions,
                    notifications = listOf(newNotif) + current.notifications,
                    currentScreen = TuaiScreen.WALLET,
                    selectedBottomNav = 1,
                    withdrawAmount = "150.000",
                    toastMessage = "Transaksi $formattedWithdraw dari ${targetAsset.name} berhasil dicatat!"
                )
            )
            repository.saveWalletState(next)
            next
        }
    }

    fun updateTopUpAmount(value: String) {
        _uiState.update { it.copy(topUpAmountInput = value.filter { ch -> ch.isDigit() }) }
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
            showMessage("Masukkan nominal pemasukan / top up yang valid")
            return
        }
        val targetAsset = state.assets.find { it.id == state.topUpTargetAssetId }
            ?: state.connectedAssets.firstOrNull()
            ?: state.assets.first()

        val updatedAssets = state.assets.map { item ->
            if (item.id == targetAsset.id) {
                item.copy(
                    tokenNumeric = item.tokenNumeric + amount,
                    isConnected = true
                )
            } else {
                item
            }
        }
        val formattedAmount = formatHeadlineBalance(amount)

        val newTx = WalletTransactionItem(
            id = "tx_${System.currentTimeMillis()}",
            title = "Saldo Masuk ke ${targetAsset.name}",
            date = "Baru saja",
            amount = "+$formattedAmount",
            isPositive = true,
            iconType = targetAsset.iconType,
            category = "RECEIVE",
            status = "Berhasil",
            referenceNote = "Sumber: ${state.topUpMethod}"
        )

        val newNotif = TuaiNotificationItem(
            id = "notif_${System.currentTimeMillis()}",
            title = "Saldo ${targetAsset.name} Bertambah",
            message = "Dana sebesar +$formattedAmount via ${state.topUpMethod} masuk ke ${targetAsset.name}.",
            time = "Baru saja",
            isRead = false
        )

        _uiState.update { current ->
            val next = syncAggregatedBalances(
                current.copy(
                    assets = updatedAssets,
                    transactions = listOf(newTx) + current.transactions,
                    notifications = listOf(newNotif) + current.notifications,
                    activeModal = TuaiActiveModal.NONE,
                    toastMessage = "Saldo ${targetAsset.name} bertambah +$formattedAmount!"
                )
            )
            repository.saveWalletState(next)
            next
        }
    }

    fun simulateReceiveFunds(amountIdr: Double = 500_000.0) {
        val state = _uiState.value
        val targetAsset = state.connectedAssets.firstOrNull() ?: state.assets.first()
        val updatedAssets = state.assets.map { item ->
            if (item.id == targetAsset.id) {
                item.copy(tokenNumeric = item.tokenNumeric + amountIdr)
            } else {
                item
            }
        }
        val formattedAmount = formatHeadlineBalance(amountIdr)

        val newTx = WalletTransactionItem(
            id = "tx_${System.currentTimeMillis()}",
            title = "Terima Dana di ${targetAsset.name}",
            date = "Baru saja",
            amount = "+$formattedAmount",
            isPositive = true,
            iconType = targetAsset.iconType,
            category = "RECEIVE",
            status = "Berhasil",
            referenceNote = "QRIS / Transfer Masuk ke ${targetAsset.name}"
        )

        _uiState.update { current ->
            val next = syncAggregatedBalances(
                current.copy(
                    assets = updatedAssets,
                    transactions = listOf(newTx) + current.transactions,
                    activeModal = TuaiActiveModal.NONE,
                    toastMessage = "Dana +$formattedAmount diterima di ${targetAsset.name}!"
                )
            )
            repository.saveWalletState(next)
            next
        }
    }

    fun updateSwapAmount(value: String) {
        _uiState.update { it.copy(swapAmountInput = value.filter { ch -> ch.isDigit() }) }
    }

    fun flipSwapAssets() {
        _uiState.update {
            it.copy(
                swapFromAssetId = it.swapToAssetId,
                swapToAssetId = it.swapFromAssetId
            )
        }
    }

    fun prepareSwapFromWallet(fromAssetId: String) {
        _uiState.update { state ->
            val pool = state.connectedAssets.ifEmpty { state.assets }
            val nextToId = if (state.swapToAssetId == fromAssetId) {
                pool.firstOrNull { it.id != fromAssetId }?.id ?: "gopay"
            } else {
                state.swapToAssetId
            }
            state.copy(
                swapFromAssetId = fromAssetId,
                swapToAssetId = nextToId,
                activeModal = TuaiActiveModal.SWAP_ASSET
            )
        }
    }

    fun prepareTopUpForWallet(targetAssetId: String) {
        _uiState.update { state ->
            state.copy(
                topUpTargetAssetId = targetAssetId,
                activeModal = TuaiActiveModal.TOP_UP
            )
        }
    }

    fun cycleSwapFromAsset() {
        _uiState.update { state ->
            val pool = state.connectedAssets.ifEmpty { state.assets }
            val idx = pool.indexOfFirst { it.id == state.swapFromAssetId }
            val nextAsset = pool[(idx + 1) % pool.size]
            val nextToId = if (nextAsset.id == state.swapToAssetId) {
                pool.firstOrNull { it.id != nextAsset.id }?.id ?: state.swapToAssetId
            } else {
                state.swapToAssetId
            }
            state.copy(
                swapFromAssetId = nextAsset.id,
                swapToAssetId = nextToId
            )
        }
    }

    fun cycleSwapToAsset() {
        _uiState.update { state ->
            val pool = state.connectedAssets.ifEmpty { state.assets }
            val idx = pool.indexOfFirst { it.id == state.swapToAssetId }
            val nextAsset = pool[(idx + 1) % pool.size]
            val nextFromId = if (nextAsset.id == state.swapFromAssetId) {
                pool.firstOrNull { it.id != nextAsset.id }?.id ?: state.swapFromAssetId
            } else {
                state.swapFromAssetId
            }
            state.copy(
                swapToAssetId = nextAsset.id,
                swapFromAssetId = nextFromId
            )
        }
    }

    fun submitSwap() {
        val state = _uiState.value
        val amountIdr = state.swapAmountInput.toDoubleOrNull() ?: 0.0
        if (amountIdr <= 0.0) {
            showMessage("Masukkan nominal pindah dana yang valid")
            return
        }
        val fromAsset = state.assets.find { it.id == state.swapFromAssetId } ?: state.assets[0]
        val toAsset = state.assets.find { it.id == state.swapToAssetId } ?: state.assets[3]

        if (fromAsset.id == toAsset.id) {
            showMessage("Pilih dompet asal dan tujuan yang berbeda")
            return
        }
        if (amountIdr > fromAsset.tokenNumeric) {
            showMessage("Saldo ${fromAsset.name} tidak cukup (${fromAsset.tokenAmount})")
            return
        }

        val updatedAssets = state.assets.map { item ->
            when (item.id) {
                fromAsset.id -> item.copy(tokenNumeric = (item.tokenNumeric - amountIdr).coerceAtLeast(0.0))
                toAsset.id -> item.copy(tokenNumeric = item.tokenNumeric + amountIdr, isConnected = true)
                else -> item
            }
        }
        val formattedAmount = formatHeadlineBalance(amountIdr)

        val newTx = WalletTransactionItem(
            id = "tx_${System.currentTimeMillis()}",
            title = "Pindah Dana: ${fromAsset.symbol} → ${toAsset.symbol}",
            date = "Baru saja",
            amount = formattedAmount,
            isPositive = true,
            iconType = toAsset.iconType,
            category = "SWAP",
            status = "Berhasil",
            referenceNote = "Alokasi Keuangan dari ${fromAsset.name} ke ${toAsset.name}"
        )

        val newNotif = TuaiNotificationItem(
            id = "notif_${System.currentTimeMillis()}",
            title = "Alokasi Dana Antar Wallet Berhasil",
            message = "Dana $formattedAmount dipindahkan dari ${fromAsset.name} ke ${toAsset.name}.",
            time = "Baru saja",
            isRead = false
        )

        _uiState.update { current ->
            val next = syncAggregatedBalances(
                current.copy(
                    assets = updatedAssets,
                    transactions = listOf(newTx) + current.transactions,
                    notifications = listOf(newNotif) + current.notifications,
                    activeModal = TuaiActiveModal.NONE,
                    toastMessage = "Berhasil memindahkan $formattedAmount (${fromAsset.name} → ${toAsset.name})!"
                )
            )
            repository.saveWalletState(next)
            next
        }
    }

    fun contributeToSavingsGoal(goalId: String, amountIdr: Double = 500_000.0) {
        _uiState.update { state ->
            val updatedGoals = state.savingsGoals.map { goal ->
                if (goal.id == goalId) {
                    val nextCurrent = goal.currentUsd + amountIdr
                    val nextTarget = if (nextCurrent > goal.targetUsd) nextCurrent + 2_500_000.0 else goal.targetUsd
                    goal.copy(currentUsd = nextCurrent, targetUsd = nextTarget)
                } else {
                    goal
                }
            }
            val next = state.copy(
                savingsGoals = updatedGoals,
                toastMessage = "Alokasi pos keuangan bertambah +${formatHeadlineBalance(amountIdr)}!"
            )
            repository.saveWalletState(next)
            next
        }
    }

    fun syncFirebaseManual() {
        val current = _uiState.value
        val synced = syncAggregatedBalances(current)
        repository.saveWalletState(synced)
        _uiState.update {
            synced.copy(
                isFirebaseSynced = true,
                lastSyncedLabel = "Baru saja • ${synced.connectedPluginCount} Plugin Dompet & Bank Sinkron",
                toastMessage = "Seluruh saldo GoPay, DANA, Mandiri, BRImo & BCA berhasil disinkronkan!"
            )
        }
    }

    fun resetDemoData() {
        val fresh = syncAggregatedBalances(
            TuaiUiState(
                currentScreen = TuaiScreen.WALLET,
                selectedBottomNav = 1,
                toastMessage = "Data seluruh plugin dompet & bank dikembalikan ke posisi awal"
            )
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
        private val idLocale = Locale.forLanguageTag("id-ID")

        fun parseWithdrawAmount(raw: String): Double {
            if (raw == "150.000") return 150_000.0
            val cleaned = raw.filter { it.isDigit() }
            return cleaned.toDoubleOrNull() ?: 0.0
        }

        fun formatHeadlineBalance(amount: Double): String {
            val nf = NumberFormat.getNumberInstance(idLocale).apply {
                maximumFractionDigits = 0
                minimumFractionDigits = 0
            }
            return "Rp ${nf.format(amount.toLong())}"
        }

        fun formatCompactIdr(amount: Double): String {
            return when {
                amount >= 1_000_000_000.0 -> String.format(idLocale, "Rp %.2f M", amount / 1_000_000_000.0)
                amount >= 1_000_000.0 -> String.format(idLocale, "Rp %.2f Jt", amount / 1_000_000.0)
                amount >= 1_000.0 -> String.format(idLocale, "Rp %.0f Rb", amount / 1_000.0)
                else -> formatHeadlineBalance(amount)
            }
        }

        fun formatCurrencyWithCents(amount: Double): String {
            val nf = NumberFormat.getNumberInstance(idLocale).apply {
                minimumFractionDigits = 0
                maximumFractionDigits = 0
            }
            return nf.format(amount.toLong())
        }

        fun formatCurrencyPlain(amount: Double): String {
            val nf = NumberFormat.getNumberInstance(idLocale).apply {
                minimumFractionDigits = 0
                maximumFractionDigits = 0
            }
            return nf.format(amount.toLong())
        }

        fun recalculateAssetsBreakdown(assets: List<CryptoAssetItem>): List<CryptoAssetItem> {
            val totalConnected = assets.filter { it.isConnected }.sumOf { it.tokenNumeric }.coerceAtLeast(1.0)
            return assets.map { item ->
                val pct = if (item.isConnected) (item.tokenNumeric / totalConnected) * 100.0 else 0.0
                val pctStr = String.format(idLocale, "%.1f%%", pct)
                item.copy(
                    tokenAmount = formatHeadlineBalance(item.tokenNumeric),
                    percentageAndValue = if (item.isConnected) {
                        "$pctStr • ${item.allocationRole}"
                    } else {
                        "Plugin Nonaktif • Ketuk untuk Hubungkan"
                    },
                    totalUsdValue = "${if (item.walletCategory == "BANK") "Bank" else "E-Wallet"} • ${item.accountNumber}"
                )
            }
        }

        fun syncAggregatedBalances(state: TuaiUiState): TuaiUiState {
            val recalculatedAssets = recalculateAssetsBreakdown(state.assets)
            val connected = recalculatedAssets.filter { it.isConnected }
            val totalBalance = connected.sumOf { it.tokenNumeric }
            val bankBalance = connected.filter { it.walletCategory == "BANK" }.sumOf { it.tokenNumeric }
            val ewalletBalance = connected.filter { it.walletCategory == "E-WALLET" }.sumOf { it.tokenNumeric }

            val totalSafe = totalBalance.coerceAtLeast(1.0)
            val bankPct = ((bankBalance / totalSafe) * 100).toInt()
            val ewalletPct = 100 - bankPct

            val healthStatus = when {
                totalBalance <= 0.0 -> "Perlu Top Up • Saldo Kosong"
                ewalletBalance < 250_000.0 -> "Perhatian • Kas E-Wallet Menipis ($ewalletPct%)"
                ewalletPct > 35 -> "Saran • Pindahkan Kelebihan E-Wallet ke Bank"
                else -> "Sehat & Terkendali ($bankPct% Bank • $ewalletPct% E-Wallet)"
            }

            val advice = when {
                ewalletBalance < 250_000.0 ->
                    "Saldo E-Wallet (GoPay/DANA) tersisa ${formatHeadlineBalance(ewalletBalance)}. Disarankan pindah dana Rp 500rb dari BCA/Mandiri untuk kebutuhan harian."
                ewalletPct > 35 ->
                    "Sebanyak $ewalletPct% uangmu (${formatCompactIdr(ewalletBalance)}) ada di E-Wallet. Amankan sebagian ke rekening BCA atau Mandiri agar tidak boros."
                else ->
                    "Komposisi ideal! Kas harian di E-Wallet sebesar ${formatCompactIdr(ewalletBalance)} ($ewalletPct%) siap pakai, dan ${formatCompactIdr(bankBalance)} ($bankPct%) tersimpan aman di Bank."
            }

            // Keep Pos Kondisi Keuangan dynamically aligned with actual connected Bank & E-Wallet balances
            val brimoBalance = connected.filter { it.id == "brimo" }.sumOf { it.tokenNumeric }
            val mainSavingsBankBalance = (bankBalance - brimoBalance).coerceAtLeast(0.0)
            val syncedGoals = state.savingsGoals.map { goal ->
                when (goal.id) {
                    "goal1" -> goal.copy(
                        currentUsd = mainSavingsBankBalance,
                        targetUsd = goal.targetUsd.coerceAtLeast(mainSavingsBankBalance)
                    )
                    "goal2" -> goal.copy(
                        currentUsd = ewalletBalance,
                        targetUsd = goal.targetUsd.coerceAtLeast(ewalletBalance)
                    )
                    "goal3" -> goal.copy(
                        currentUsd = brimoBalance,
                        targetUsd = goal.targetUsd.coerceAtLeast(brimoBalance)
                    )
                    else -> goal
                }
            }

            return state.copy(
                assets = recalculatedAssets,
                walletBalanceNumeric = totalBalance,
                walletBalance = formatHeadlineBalance(totalBalance),
                bankBalanceNumeric = bankBalance,
                ewalletBalanceNumeric = ewalletBalance,
                connectedPluginCount = connected.size,
                financialHealthStatus = healthStatus,
                financialConditionAdvice = advice,
                savingsGoals = syncedGoals,
                lastSyncedLabel = "Aktif • ${connected.size} Plugin Terhubung (${formatCompactIdr(totalBalance)})"
            )
        }
    }
}
