package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.layout.ContentScale
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.components.ActionArrowDownLeftIcon
import com.example.ui.components.ActionArrowUpRightIcon
import com.example.ui.components.ActionPluginConnectorIcon
import com.example.ui.components.ActionSwapCircularIcon
import com.example.ui.components.CopyWalletIdIcon
import com.example.ui.components.NavHomeSmileIcon
import com.example.ui.components.NavPieChartIcon
import com.example.ui.components.NavSettingsCogIcon
import com.example.ui.components.NavWalletFilledIcon
import com.example.ui.components.NotificationBellWithDotIcon
import com.example.ui.components.TuaiChartTabContent
import com.example.ui.components.TuaiHomeTabContent
import com.example.ui.components.TuaiSettingsTabContent
import com.example.ui.components.WalletProviderBadge
import com.example.ui.theme.PlusJakartaSans
import com.example.ui.theme.TuaiBalanceCream
import com.example.ui.theme.TuaiBellCircleBg
import com.example.ui.theme.TuaiGoldAccent
import com.example.ui.theme.TuaiTerracotta
import com.example.ui.theme.TuaiTextDark
import com.example.ui.theme.TuaiTextMuted
import com.example.ui.theme.TuaiWalletIdText
import com.example.viewmodel.CryptoAssetItem
import com.example.viewmodel.TuaiActiveModal
import com.example.viewmodel.TuaiUiState
import com.example.viewmodel.TuaiViewModel
import com.example.viewmodel.WalletTransactionItem

@Composable
fun WalletScreen(
    uiState: TuaiUiState,
    onBackToLogin: () -> Unit,
    onNavigateToWithdraw: () -> Unit,
    onSelectAssetTab: (Int) -> Unit,
    onSelectBottomNav: (Int) -> Unit,
    onShowMessage: (String) -> Unit,
    modifier: Modifier = Modifier,
    onOpenModal: (TuaiActiveModal) -> Unit = {},
    onAssetClick: (CryptoAssetItem) -> Unit = { onNavigateToWithdraw() },
    onTransactionClick: (WalletTransactionItem) -> Unit = { onNavigateToWithdraw() },
    onSelectTxFilter: (String) -> Unit = {},
    onSelectWalletCategoryFilter: (String) -> Unit = {},
    onSelectChartTimeframe: (String) -> Unit = {},
    onContributeGoal: (String) -> Unit = {},
    onTogglePlugin: (String) -> Unit = {},
    onTogglePrivacy: () -> Unit = {},
    onToggleBiometric: () -> Unit = {},
    onToggleNotifications: () -> Unit = {},
    onSyncFirebase: () -> Unit = {},
    onResetData: () -> Unit = {}
) {
    BackHandler(onBack = onBackToLogin)
    val context = LocalContext.current

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(TuaiTerracotta),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .widthIn(max = 500.dp)
                .windowInsetsPadding(WindowInsets.statusBars)
        ) {
            // Top Header Section on Terracotta Background
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(12.dp))

                // Top Bar: "Dompet Terhubung" + Notification Bell + Profile Avatar
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = stringResource(R.string.wallet_title),
                            fontFamily = PlusJakartaSans,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 19.sp,
                            color = Color.White
                        )
                        Text(
                            text = "Total Isi Uang dari ${uiState.connectedPluginCount} Dompet & Bank",
                            fontFamily = PlusJakartaSans,
                            fontWeight = FontWeight.Normal,
                            fontSize = 11.5.sp,
                            color = TuaiWalletIdText
                        )
                    }

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(42.dp)
                                .clip(CircleShape)
                                .background(TuaiBellCircleBg)
                                .clickable {
                                    onOpenModal(TuaiActiveModal.NOTIFICATIONS)
                                }
                                .testTag("wallet_notification_button"),
                            contentAlignment = Alignment.Center
                        ) {
                            NotificationBellWithDotIcon(
                                tint = Color(0xFF2B211B),
                                size = 20.dp
                            )
                        }

                        Box(
                            modifier = Modifier
                                .size(42.dp)
                                .clip(CircleShape)
                                .background(TuaiBellCircleBg)
                                .clickable {
                                    onOpenModal(TuaiActiveModal.PROFILE_ACCOUNT)
                                }
                                .testTag("wallet_profile_avatar"),
                            contentAlignment = Alignment.Center
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.avatar_profile),
                                contentDescription = "Profile Avatar",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .scale(1.16f)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                // Large Total Aggregated Balance Text (in Rupiah)
                Text(
                    text = if (uiState.isBalanceHidden) "Rp ••••••••" else uiState.walletBalance,
                    fontFamily = PlusJakartaSans,
                    fontWeight = FontWeight.Bold,
                    fontSize = 36.sp,
                    color = TuaiBalanceCream,
                    letterSpacing = (-0.8).sp,
                    modifier = Modifier
                        .clickable { onTogglePrivacy() }
                        .testTag("wallet_balance_text")
                )

                Spacer(modifier = Modifier.height(6.dp))

                // Bank vs E-Wallet Breakdown Pill
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(50))
                        .background(Color(0xFFB94A22))
                        .clickable { onOpenModal(TuaiActiveModal.PLUGIN_MANAGER) }
                        .padding(horizontal = 12.dp, vertical = 4.dp)
                        .testTag("wallet_breakdown_pill")
                ) {
                    Text(
                        text = if (uiState.isBalanceHidden) {
                            "${uiState.connectedPluginCount} Plugin Aktif • Ketuk untuk kelola"
                        } else {
                            "Bank: ${TuaiViewModel.formatCompactIdr(uiState.bankBalanceNumeric)} • E-Wallet: ${TuaiViewModel.formatCompactIdr(uiState.ewalletBalanceNumeric)}"
                        },
                        fontFamily = PlusJakartaSans,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 11.5.sp,
                        color = TuaiBalanceCream
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                // Wallet ID + Copy Icon Row
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .clickable {
                            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as? ClipboardManager
                            clipboard?.setPrimaryClip(ClipData.newPlainText("Tuai Wallet ID", uiState.walletId))
                            onShowMessage("ID Konektor Tuai ${uiState.walletId} disalin")
                        }
                        .padding(horizontal = 8.dp, vertical = 2.dp)
                        .testTag("copy_wallet_id_button")
                ) {
                    Text(
                        text = "Wallet id: ${uiState.walletId}",
                        fontFamily = PlusJakartaSans,
                        fontWeight = FontWeight.Normal,
                        fontSize = 12.sp,
                        color = TuaiWalletIdText
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    CopyWalletIdIcon(
                        tint = TuaiWalletIdText,
                        size = 13.dp
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // 4 Circular Golden Action Buttons with clear labels:
                // 1. Terima / Top Up, 2. Catat Keluar, 3. Pindah Dana, 4. Plugin Hub
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    WalletActionCircleButton(
                        label = "Isi Saldo",
                        testTag = "action_receive_button",
                        onClick = { onOpenModal(TuaiActiveModal.TOP_UP) }
                    ) {
                        ActionArrowDownLeftIcon(size = 20.dp)
                    }

                    WalletActionCircleButton(
                        label = "Catat Keluar",
                        testTag = "action_withdraw_button",
                        onClick = onNavigateToWithdraw
                    ) {
                        ActionArrowUpRightIcon(size = 20.dp)
                    }

                    WalletActionCircleButton(
                        label = "Pindah Dana",
                        testTag = "action_swap_button",
                        onClick = { onOpenModal(TuaiActiveModal.SWAP_ASSET) }
                    ) {
                        ActionSwapCircularIcon(size = 22.dp)
                    }

                    WalletActionCircleButton(
                        label = "Plugin (${uiState.connectedPluginCount})",
                        testTag = "action_add_button",
                        onClick = { onOpenModal(TuaiActiveModal.PLUGIN_MANAGER) }
                    ) {
                        ActionPluginConnectorIcon(size = 20.dp)
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))
            }

            // Custom Asymmetric Folder-Tab Sheet + White Content Area
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val w = size.width
                    val h = size.height
                    val tabHeight = 50.dp.toPx()
                    val topCorner = 26.dp.toPx()

                    drawRoundRect(
                        color = Color(0xFFC2512A),
                        topLeft = Offset(w * 0.36f, 4.dp.toPx()),
                        size = Size(w * 0.64f, tabHeight + 24.dp.toPx()),
                        cornerRadius = CornerRadius(topCorner, topCorner)
                    )

                    val sheetPath = Path().apply {
                        moveTo(0f, h)
                        lineTo(0f, topCorner)
                        arcTo(
                            rect = Rect(0f, 0f, topCorner * 2f, topCorner * 2f),
                            startAngleDegrees = 180f,
                            sweepAngleDegrees = 90f,
                            forceMoveTo = false
                        )
                        lineTo(w * 0.36f, 0f)
                        cubicTo(
                            w * 0.44f, 0f,
                            w * 0.44f, tabHeight,
                            w * 0.53f, tabHeight
                        )
                        lineTo(w - topCorner, tabHeight)
                        arcTo(
                            rect = Rect(
                                w - topCorner * 2f,
                                tabHeight,
                                w,
                                tabHeight + topCorner * 2f
                            ),
                            startAngleDegrees = 270f,
                            sweepAngleDegrees = 90f,
                            forceMoveTo = false
                        )
                        lineTo(w, h)
                        close()
                    }
                    drawPath(path = sheetPath, color = Color.White)
                }

                Column(modifier = Modifier.fillMaxSize()) {
                    // Tab Labels Row ("Dompet & Bank" | "Mutasi Gabungan")
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .weight(0.46f)
                                .height(50.dp)
                                .clickable(
                                    interactionSource = remember { MutableInteractionSource() },
                                    indication = null,
                                    onClick = {
                                        onSelectBottomNav(1)
                                        onSelectAssetTab(0)
                                    }
                                )
                                .padding(start = 24.dp, top = 4.dp)
                                .testTag("tab_my_assets"),
                            contentAlignment = Alignment.CenterStart
                        ) {
                            Text(
                                text = stringResource(R.string.my_assets_tab),
                                fontFamily = PlusJakartaSans,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 13.5.sp,
                                color = TuaiTextDark
                            )
                        }

                        Box(
                            modifier = Modifier
                                .weight(0.54f)
                                .height(50.dp)
                                .clickable(
                                    interactionSource = remember { MutableInteractionSource() },
                                    indication = null,
                                    onClick = {
                                        onSelectBottomNav(1)
                                        onSelectAssetTab(1)
                                    }
                                )
                                .padding(end = 16.dp)
                                .testTag("tab_my_transaction"),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = stringResource(R.string.my_transaction_tab),
                                fontFamily = PlusJakartaSans,
                                fontWeight = if (uiState.selectedAssetTab == 1) {
                                    FontWeight.SemiBold
                                } else {
                                    FontWeight.Normal
                                },
                                fontSize = 13.5.sp,
                                color = if (uiState.selectedAssetTab == 1) {
                                    Color.White
                                } else {
                                    Color(0xFFF3B89E)
                                }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    // Content switches based on selectedBottomNav (0=Kondisi, 1=Dompet, 2=Analitik, 3=Plugin)
                    when (uiState.selectedBottomNav) {
                        0 -> {
                            TuaiHomeTabContent(
                                uiState = uiState,
                                onNavigateToWithdraw = onNavigateToWithdraw,
                                onOpenModal = onOpenModal,
                                onContributeGoal = onContributeGoal,
                                onSwitchToWalletTab = { onSelectBottomNav(1) },
                                modifier = Modifier.weight(1f)
                            )
                        }

                        2 -> {
                            TuaiChartTabContent(
                                uiState = uiState,
                                onSelectTimeframe = onSelectChartTimeframe,
                                onNavigateToWithdraw = onNavigateToWithdraw,
                                onOpenSwap = { onOpenModal(TuaiActiveModal.SWAP_ASSET) },
                                modifier = Modifier.weight(1f)
                            )
                        }

                        3 -> {
                            TuaiSettingsTabContent(
                                uiState = uiState,
                                onTogglePlugin = onTogglePlugin,
                                onOpenPluginModal = { onOpenModal(TuaiActiveModal.PLUGIN_MANAGER) },
                                onEditWalletAsset = onAssetClick,
                                onTogglePrivacy = onTogglePrivacy,
                                onToggleBiometric = onToggleBiometric,
                                onToggleNotifications = onToggleNotifications,
                                onSyncFirebase = onSyncFirebase,
                                onResetData = onResetData,
                                onLogout = onBackToLogin,
                                modifier = Modifier.weight(1f)
                            )
                        }

                        else -> {
                            // Default Wallet View (Dompet & Bank / Mutasi Gabungan)
                            if (uiState.selectedAssetTab == 0) {
                                val displayedWallets = remember(
                                    uiState.assets,
                                    uiState.selectedWalletCategoryFilter
                                ) {
                                    val connected = uiState.assets.filter { it.isConnected }
                                    when (uiState.selectedWalletCategoryFilter) {
                                        "BANK" -> connected.filter { it.walletCategory == "BANK" }
                                        "E-WALLET" -> connected.filter { it.walletCategory == "E-WALLET" }
                                        else -> connected
                                    }
                                }

                                // Filter Chips: Semua | Bank (BCA/Mandiri/BRImo) | E-Wallet (GoPay/DANA) | + Plugin
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 24.dp, vertical = 4.dp),
                                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    listOf(
                                        "ALL" to "Semua (${uiState.connectedAssets.size})",
                                        "BANK" to "Bank (${uiState.connectedAssets.count { it.walletCategory == "BANK" }})",
                                        "E-WALLET" to "E-Wallet (${uiState.connectedAssets.count { it.walletCategory == "E-WALLET" }})"
                                    ).forEach { (key, label) ->
                                        val selected = uiState.selectedWalletCategoryFilter == key
                                        Box(
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(50))
                                                .background(
                                                    if (selected) TuaiTerracotta else Color(0xFFFAF3E8)
                                                )
                                                .clickable { onSelectWalletCategoryFilter(key) }
                                                .padding(horizontal = 10.dp, vertical = 5.dp)
                                        ) {
                                            Text(
                                                text = label,
                                                fontFamily = PlusJakartaSans,
                                                fontWeight = FontWeight.SemiBold,
                                                fontSize = 11.sp,
                                                color = if (selected) Color.White else TuaiTextDark
                                            )
                                        }
                                    }

                                    Spacer(modifier = Modifier.weight(1f))

                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(50))
                                            .background(TuaiGoldAccent.copy(alpha = 0.35f))
                                            .border(1.dp, TuaiTerracotta.copy(alpha = 0.4f), RoundedCornerShape(50))
                                            .clickable { onOpenModal(TuaiActiveModal.PLUGIN_MANAGER) }
                                            .padding(horizontal = 10.dp, vertical = 5.dp)
                                            .testTag("open_plugin_hub_chip")
                                    ) {
                                        Text(
                                            text = "+ Plugin",
                                            fontFamily = PlusJakartaSans,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 11.sp,
                                            color = TuaiTerracotta
                                        )
                                    }
                                }

                                LazyColumn(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .weight(1f)
                                        .padding(horizontal = 24.dp)
                                ) {
                                    itemsIndexed(
                                        items = displayedWallets,
                                        key = { _, item -> item.id }
                                    ) { index, asset ->
                                        AssetListRow(
                                            asset = asset,
                                            isBalanceHidden = uiState.isBalanceHidden,
                                            onClick = { onAssetClick(asset) }
                                        )
                                        if (index < displayedWallets.lastIndex) {
                                            HorizontalDivider(
                                                thickness = 1.dp,
                                                color = Color(0xFFF5F1EC)
                                            )
                                        }
                                    }

                                    // Footer CTA to connect more wallets (OVO, ShopeePay, SeaBank, etc.)
                                    item {
                                        val disconnectedCount = uiState.assets.count { !it.isConnected }
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(vertical = 12.dp)
                                                .clip(RoundedCornerShape(16.dp))
                                                .background(Color(0xFFFAF3E8))
                                                .clickable { onOpenModal(TuaiActiveModal.PLUGIN_MANAGER) }
                                                .padding(horizontal = 16.dp, vertical = 12.dp),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Column(modifier = Modifier.weight(1f)) {
                                                Text(
                                                    text = "Hubungkan Dompet & Bank Lainnya",
                                                    fontFamily = PlusJakartaSans,
                                                    fontWeight = FontWeight.Bold,
                                                    fontSize = 13.sp,
                                                    color = TuaiTextDark
                                                )
                                                Text(
                                                    text = if (disconnectedCount > 0) {
                                                        "$disconnectedCount plugin belum aktif (OVO, ShopeePay, SeaBank, dll)"
                                                    } else {
                                                        "Tambah plugin kustom (BNI, BSI, Jago, LinkAja)"
                                                    },
                                                    fontFamily = PlusJakartaSans,
                                                    fontSize = 11.5.sp,
                                                    color = TuaiTextMuted
                                                )
                                            }
                                            Text(
                                                text = "Kelola →",
                                                fontFamily = PlusJakartaSans,
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 12.5.sp,
                                                color = TuaiTerracotta
                                            )
                                        }
                                    }
                                }
                            } else {
                                val filteredTransactions = remember(
                                    uiState.transactions,
                                    uiState.selectedTxFilter
                                ) {
                                    when (uiState.selectedTxFilter) {
                                        "IN" -> uiState.transactions.filter { it.isPositive }
                                        "OUT" -> uiState.transactions.filter { !it.isPositive }
                                        "SWAP" -> uiState.transactions.filter { it.category == "SWAP" }
                                        else -> uiState.transactions
                                    }
                                }

                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 24.dp, vertical = 4.dp),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    listOf(
                                        "ALL" to "Semua",
                                        "IN" to "Masuk",
                                        "OUT" to "Keluar",
                                        "SWAP" to "Alokasi/Pindah"
                                    ).forEach { (key, label) ->
                                        val selected = uiState.selectedTxFilter == key
                                        Box(
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(50))
                                                .background(
                                                    if (selected) TuaiTerracotta else Color(0xFFFAF3E8)
                                                )
                                                .clickable { onSelectTxFilter(key) }
                                                .padding(horizontal = 12.dp, vertical = 5.dp)
                                        ) {
                                            Text(
                                                text = label,
                                                fontFamily = PlusJakartaSans,
                                                fontWeight = FontWeight.SemiBold,
                                                fontSize = 11.5.sp,
                                                color = if (selected) Color.White else TuaiTextDark
                                            )
                                        }
                                    }
                                }

                                LazyColumn(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .weight(1f)
                                        .padding(horizontal = 24.dp)
                                ) {
                                    itemsIndexed(
                                        items = filteredTransactions,
                                        key = { _, tx -> tx.id }
                                    ) { index, tx ->
                                        TransactionListRow(
                                            tx = tx,
                                            onClick = { onTransactionClick(tx) }
                                        )
                                        if (index < filteredTransactions.lastIndex) {
                                            HorizontalDivider(
                                                thickness = 1.dp,
                                                color = Color(0xFFF5F1EC)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Bottom Navigation Bar (Kondisi, Dompet, Analitik, Plugin)
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = Color.White,
                shadowElevation = 6.dp
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .windowInsetsPadding(WindowInsets.navigationBars)
                ) {
                    HorizontalDivider(thickness = 1.dp, color = Color(0xFFF3EFEA))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(68.dp)
                            .padding(horizontal = 12.dp),
                        horizontalArrangement = Arrangement.SpaceAround,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        BottomNavItem(
                            label = stringResource(R.string.nav_home),
                            textColor = if (uiState.selectedBottomNav == 0) {
                                TuaiTerracotta
                            } else {
                                Color(0xFFB86B43)
                            },
                            fontWeight = if (uiState.selectedBottomNav == 0) {
                                FontWeight.SemiBold
                            } else {
                                FontWeight.Normal
                            },
                            testTag = "nav_home_tab",
                            onClick = { onSelectBottomNav(0) }
                        ) {
                            NavHomeSmileIcon(
                                tint = if (uiState.selectedBottomNav == 0) {
                                    TuaiTerracotta
                                } else {
                                    Color(0xFFB86B43)
                                },
                                size = 22.dp
                            )
                        }

                        BottomNavItem(
                            label = stringResource(R.string.nav_wallet),
                            textColor = TuaiTerracotta,
                            fontWeight = if (uiState.selectedBottomNav == 1) {
                                FontWeight.SemiBold
                            } else {
                                FontWeight.Medium
                            },
                            testTag = "nav_wallet_tab",
                            onClick = { onSelectBottomNav(1) }
                        ) {
                            NavWalletFilledIcon(tint = TuaiTerracotta, size = 22.dp)
                        }

                        BottomNavItem(
                            label = stringResource(R.string.nav_chart),
                            textColor = if (uiState.selectedBottomNav == 2) {
                                TuaiTerracotta
                            } else {
                                Color(0xFF2B2522)
                            },
                            fontWeight = if (uiState.selectedBottomNav == 2) {
                                FontWeight.SemiBold
                            } else {
                                FontWeight.Normal
                            },
                            testTag = "nav_chart_tab",
                            onClick = { onSelectBottomNav(2) }
                        ) {
                            NavPieChartIcon(
                                tint = if (uiState.selectedBottomNav == 2) {
                                    TuaiTerracotta
                                } else {
                                    Color(0xFF2B2522)
                                },
                                size = 22.dp
                            )
                        }

                        BottomNavItem(
                            label = stringResource(R.string.nav_settings),
                            textColor = if (uiState.selectedBottomNav == 3) {
                                TuaiTerracotta
                            } else {
                                Color(0xFF2B2522)
                            },
                            fontWeight = if (uiState.selectedBottomNav == 3) {
                                FontWeight.SemiBold
                            } else {
                                FontWeight.Normal
                            },
                            testTag = "nav_settings_tab",
                            onClick = { onSelectBottomNav(3) }
                        ) {
                            NavSettingsCogIcon(
                                tint = if (uiState.selectedBottomNav == 3) {
                                    TuaiTerracotta
                                } else {
                                    Color(0xFF2B2522)
                                },
                                size = 22.dp
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun WalletActionCircleButton(
    label: String,
    testTag: String,
    onClick: () -> Unit,
    content: @Composable () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(56.dp)
                .clip(CircleShape)
                .background(TuaiGoldAccent)
                .clickable(onClick = onClick)
                .testTag(testTag),
            contentAlignment = Alignment.Center
        ) {
            content()
        }
        Spacer(modifier = Modifier.height(5.dp))
        Text(
            text = label,
            fontFamily = PlusJakartaSans,
            fontWeight = FontWeight.Medium,
            fontSize = 11.sp,
            color = TuaiBalanceCream
        )
    }
}

@Composable
private fun AssetListRow(
    asset: CryptoAssetItem,
    isBalanceHidden: Boolean = false,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 13.dp)
            .testTag("asset_row_${asset.id}"),
        verticalAlignment = Alignment.CenterVertically
    ) {
        WalletProviderBadge(
            iconType = asset.iconType,
            size = 46.dp
        )

        Spacer(modifier = Modifier.width(14.dp))

        // Provider Name & Percentage + Financial Allocation Role
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = asset.name,
                fontFamily = PlusJakartaSans,
                fontWeight = FontWeight.SemiBold,
                fontSize = 15.sp,
                color = TuaiTextDark
            )
            Spacer(modifier = Modifier.height(3.dp))
            Text(
                text = asset.percentageAndValue,
                fontFamily = PlusJakartaSans,
                fontWeight = FontWeight.Normal,
                fontSize = 12.sp,
                color = TuaiTextMuted
            )
        }

        // Balance in Rupiah & Account Info
        Column(horizontalAlignment = Alignment.End) {
            Text(
                text = if (isBalanceHidden) "Rp •••••••" else asset.tokenAmount,
                fontFamily = PlusJakartaSans,
                fontWeight = FontWeight.Bold,
                fontSize = 14.5.sp,
                color = TuaiTextDark
            )
            Spacer(modifier = Modifier.height(3.dp))
            Text(
                text = asset.totalUsdValue,
                fontFamily = PlusJakartaSans,
                fontWeight = FontWeight.Normal,
                fontSize = 11.5.sp,
                color = TuaiTextMuted
            )
        }
    }
}

@Composable
private fun TransactionListRow(
    tx: WalletTransactionItem,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        WalletProviderBadge(
            iconType = tx.iconType,
            size = 44.dp
        )

        Spacer(modifier = Modifier.width(14.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = tx.title,
                fontFamily = PlusJakartaSans,
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.5.sp,
                color = TuaiTextDark
            )
            Spacer(modifier = Modifier.height(3.dp))
            Text(
                text = "${tx.date} • ${tx.referenceNote}",
                fontFamily = PlusJakartaSans,
                fontWeight = FontWeight.Normal,
                fontSize = 11.5.sp,
                color = TuaiTextMuted
            )
        }

        Text(
            text = tx.amount,
            fontFamily = PlusJakartaSans,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
            color = if (tx.isPositive) Color(0xFF4C8C4A) else TuaiTerracotta
        )
    }
}

@Composable
private fun BottomNavItem(
    label: String,
    textColor: Color,
    fontWeight: FontWeight,
    testTag: String,
    onClick: () -> Unit,
    icon: @Composable () -> Unit
) {
    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 14.dp, vertical = 6.dp)
            .testTag(testTag),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        icon()
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = label,
            fontFamily = PlusJakartaSans,
            fontWeight = fontWeight,
            fontSize = 11.5.sp,
            color = textColor
        )
    }
}
