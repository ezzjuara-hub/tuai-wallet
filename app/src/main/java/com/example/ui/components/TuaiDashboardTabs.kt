package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.PlusJakartaSans
import com.example.ui.theme.TuaiBcaBlue
import com.example.ui.theme.TuaiGoldAccent
import com.example.ui.theme.TuaiGopayCyan
import com.example.ui.theme.TuaiInputBg
import com.example.ui.theme.TuaiTerracotta
import com.example.ui.theme.TuaiTerracottaDark
import com.example.ui.theme.TuaiTextDark
import com.example.ui.theme.TuaiTextMuted
import com.example.viewmodel.CryptoAssetItem
import com.example.viewmodel.TuaiActiveModal
import com.example.viewmodel.TuaiUiState
import com.example.viewmodel.TuaiViewModel

@Composable
fun TuaiHomeTabContent(
    uiState: TuaiUiState,
    onNavigateToWithdraw: () -> Unit,
    onOpenModal: (TuaiActiveModal) -> Unit,
    onContributeGoal: (String) -> Unit,
    onSwitchToWalletTab: () -> Unit,
    modifier: Modifier = Modifier
) {
    val totalSafe = uiState.walletBalanceNumeric.coerceAtLeast(1.0)
    val bankRatio = (uiState.bankBalanceNumeric / totalSafe).toFloat().coerceIn(0.05f, 0.95f)
    val ewalletRatio = (1f - bankRatio).coerceAtLeast(0.05f)

    Column(
        modifier = modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp, vertical = 10.dp)
    ) {
        Text(
            text = "Kondisi Keuanganmu, ${uiState.userName}",
            fontFamily = PlusJakartaSans,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            color = TuaiTextDark
        )
        Text(
            text = uiState.financialHealthStatus,
            fontFamily = PlusJakartaSans,
            fontWeight = FontWeight.SemiBold,
            fontSize = 12.5.sp,
            color = Color(0xFF4C8C4A)
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Kartu Sebaran Uang (Rekening Bank vs E-Wallet) + Rekomendasi Pintar
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(18.dp))
                .background(Color(0xFFFAF3E8))
                .border(1.dp, Color(0xFFE7DAC7), RoundedCornerShape(18.dp))
                .padding(16.dp)
        ) {
            Text(
                text = "Peta Sebaran Uangku Saat Ini",
                fontFamily = PlusJakartaSans,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = TuaiTextDark
            )
            Spacer(modifier = Modifier.height(8.dp))

            // Dual Bar Bank vs E-Wallet
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(10.dp)
                    .clip(RoundedCornerShape(50))
            ) {
                Box(
                    modifier = Modifier
                        .weight(bankRatio)
                        .fillMaxSize()
                        .background(TuaiBcaBlue)
                )
                Box(
                    modifier = Modifier
                        .weight(ewalletRatio)
                        .fillMaxSize()
                        .background(TuaiGopayCyan)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "Rekening Bank (BCA, Mandiri, BRImo)",
                        fontFamily = PlusJakartaSans,
                        fontSize = 11.sp,
                        color = TuaiTextMuted
                    )
                    Text(
                        text = TuaiViewModel.formatHeadlineBalance(uiState.bankBalanceNumeric),
                        fontFamily = PlusJakartaSans,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.5.sp,
                        color = TuaiBcaBlue
                    )
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "E-Wallet (GoPay, DANA, dll)",
                        fontFamily = PlusJakartaSans,
                        fontSize = 11.sp,
                        color = TuaiTextMuted
                    )
                    Text(
                        text = TuaiViewModel.formatHeadlineBalance(uiState.ewalletBalanceNumeric),
                        fontFamily = PlusJakartaSans,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.5.sp,
                        color = Color(0xFF0088A9)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = uiState.financialConditionAdvice,
                fontFamily = PlusJakartaSans,
                fontSize = 11.5.sp,
                color = TuaiTextDark
            )

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(38.dp)
                        .clip(RoundedCornerShape(50))
                        .background(TuaiTerracotta)
                        .clickable { onOpenModal(TuaiActiveModal.SWAP_ASSET) },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Atur / Pindah Dana",
                        fontFamily = PlusJakartaSans,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 12.sp,
                        color = Color.White
                    )
                }
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(38.dp)
                        .clip(RoundedCornerShape(50))
                        .background(Color.White)
                        .border(1.dp, TuaiTerracotta, RoundedCornerShape(50))
                        .clickable { onOpenModal(TuaiActiveModal.PLUGIN_MANAGER) },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Plugin (${uiState.connectedPluginCount} Aktif)",
                        fontFamily = PlusJakartaSans,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 12.sp,
                        color = TuaiTerracotta
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Pos Kondisi Keuangan (Budget & Financial Buckets)
        Text(
            text = "Pos Alokasi & Kondisi Keuangan",
            fontFamily = PlusJakartaSans,
            fontWeight = FontWeight.SemiBold,
            fontSize = 14.5.sp,
            color = TuaiTextDark
        )
        Spacer(modifier = Modifier.height(8.dp))

        uiState.savingsGoals.forEach { goal ->
            val progress = (goal.currentUsd / goal.targetUsd).toFloat().coerceIn(0f, 1f)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 10.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFFFAF3E8))
                    .padding(14.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = goal.title,
                            fontFamily = PlusJakartaSans,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = TuaiTextDark
                        )
                        Text(
                            text = goal.linkedWalletsLabel,
                            fontFamily = PlusJakartaSans,
                            fontSize = 11.sp,
                            color = TuaiTextMuted
                        )
                    }
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(50))
                            .background(TuaiTerracotta)
                            .clickable { onContributeGoal(goal.id) }
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "+ Alokasi 500rb",
                            fontFamily = PlusJakartaSans,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 11.sp,
                            color = Color.White
                        )
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .clip(RoundedCornerShape(50))
                        .background(Color(0xFFE5D8C5))
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(progress)
                            .height(8.dp)
                            .clip(RoundedCornerShape(50))
                            .background(TuaiTerracotta)
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "${TuaiViewModel.formatCompactIdr(goal.currentUsd)} tersedia",
                        fontFamily = PlusJakartaSans,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 11.5.sp,
                        color = TuaiTerracottaDark
                    )
                    Text(
                        text = "Target Pos: ${TuaiViewModel.formatCompactIdr(goal.targetUsd)}",
                        fontFamily = PlusJakartaSans,
                        fontSize = 11.5.sp,
                        color = TuaiTextMuted
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Mutasi Lintas Dompet Terkini",
                fontFamily = PlusJakartaSans,
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.5.sp,
                color = TuaiTextDark
            )
            Text(
                text = "Lihat Dompet →",
                fontFamily = PlusJakartaSans,
                fontWeight = FontWeight.SemiBold,
                fontSize = 12.sp,
                color = TuaiTerracotta,
                modifier = Modifier.clickable(onClick = onSwitchToWalletTab)
            )
        }
        Spacer(modifier = Modifier.height(6.dp))
        uiState.transactions.take(3).forEach { tx ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 5.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = tx.title,
                        fontFamily = PlusJakartaSans,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 13.5.sp,
                        color = TuaiTextDark
                    )
                    Text(
                        text = tx.date,
                        fontFamily = PlusJakartaSans,
                        fontSize = 11.5.sp,
                        color = TuaiTextMuted
                    )
                }
                Text(
                    text = tx.amount,
                    fontFamily = PlusJakartaSans,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.5.sp,
                    color = if (tx.isPositive) Color(0xFF4C8C4A) else TuaiTerracotta
                )
            }
        }
    }
}

@Composable
fun TuaiChartTabContent(
    uiState: TuaiUiState,
    onSelectTimeframe: (String) -> Unit,
    onNavigateToWithdraw: () -> Unit,
    onOpenSwap: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp, vertical = 10.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Analitik & Sebaran Uang",
                    fontFamily = PlusJakartaSans,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = TuaiTextDark
                )
                Text(
                    text = "Total ${uiState.walletBalance} di ${uiState.connectedPluginCount} Dompet",
                    fontFamily = PlusJakartaSans,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 12.5.sp,
                    color = Color(0xFF4C8C4A)
                )
            }
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                listOf("24H", "1W", "1M", "1Y").forEach { tf ->
                    val active = uiState.selectedChartTimeframe == tf
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(50))
                            .background(if (active) TuaiTerracotta else Color(0xFFFAF3E8))
                            .clickable { onSelectTimeframe(tf) }
                            .padding(horizontal = 10.dp, vertical = 5.dp)
                    ) {
                        Text(
                            text = tf,
                            fontFamily = PlusJakartaSans,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 11.5.sp,
                            color = if (active) Color.White else TuaiTextDark
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Smooth Area Line Chart Canvas
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(132.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(Color(0xFFFAF3E8))
                .padding(16.dp)
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val points = when (uiState.selectedChartTimeframe) {
                    "24H" -> listOf(0.55f, 0.48f, 0.52f, 0.38f, 0.42f, 0.28f, 0.22f)
                    "1W" -> listOf(0.70f, 0.60f, 0.50f, 0.54f, 0.35f, 0.30f, 0.18f)
                    "1Y" -> listOf(0.85f, 0.72f, 0.64f, 0.48f, 0.40f, 0.25f, 0.12f)
                    else -> listOf(0.75f, 0.62f, 0.68f, 0.45f, 0.38f, 0.24f, 0.15f)
                }
                val stepX = size.width / (points.size - 1).coerceAtLeast(1)
                val linePath = Path()
                val fillPath = Path()

                points.forEachIndexed { i, yRatio ->
                    val x = i * stepX
                    val y = yRatio * size.height
                    if (i == 0) {
                        linePath.moveTo(x, y)
                        fillPath.moveTo(x, size.height)
                        fillPath.lineTo(x, y)
                    } else {
                        linePath.lineTo(x, y)
                        fillPath.lineTo(x, y)
                    }
                }
                fillPath.lineTo(size.width, size.height)
                fillPath.close()

                drawPath(
                    path = fillPath,
                    brush = Brush.verticalGradient(
                        colors = listOf(TuaiTerracotta.copy(alpha = 0.28f), Color.Transparent)
                    )
                )
                drawPath(
                    path = linePath,
                    color = TuaiTerracotta,
                    style = Stroke(width = 3.dp.toPx(), cap = StrokeCap.Round)
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Komposisi Isi Dompet & Bank Terhubung",
            fontFamily = PlusJakartaSans,
            fontWeight = FontWeight.SemiBold,
            fontSize = 14.sp,
            color = TuaiTextDark
        )
        Spacer(modifier = Modifier.height(8.dp))

        val connected = uiState.connectedAssets
        val totalSafe = uiState.walletBalanceNumeric.coerceAtLeast(1.0)

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(12.dp)
                .clip(RoundedCornerShape(50))
        ) {
            connected.forEach { asset ->
                val weightVal = (asset.tokenNumeric / totalSafe).toFloat().coerceAtLeast(0.04f)
                Box(
                    modifier = Modifier
                        .weight(weightVal)
                        .fillMaxSize()
                        .background(providerColorFor(asset.iconType))
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        connected.forEach { asset ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .clip(CircleShape)
                            .background(providerColorFor(asset.iconType))
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "${asset.name} (${asset.allocationRole})",
                        fontFamily = PlusJakartaSans,
                        fontWeight = FontWeight.Medium,
                        fontSize = 12.sp,
                        color = TuaiTextDark
                    )
                }
                Text(
                    text = asset.tokenAmount,
                    fontFamily = PlusJakartaSans,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    color = TuaiTextDark
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(42.dp)
                    .clip(RoundedCornerShape(50))
                    .background(TuaiGoldAccent)
                    .clickable(onClick = onOpenSwap),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Pindah Dana Antar Wallet",
                    fontFamily = PlusJakartaSans,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 12.5.sp,
                    color = TuaiTextDark
                )
            }
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(42.dp)
                    .clip(RoundedCornerShape(50))
                    .background(TuaiTerracotta)
                    .clickable(onClick = onNavigateToWithdraw),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Catat Pengeluaran",
                    fontFamily = PlusJakartaSans,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 12.5.sp,
                    color = Color.White
                )
            }
        }
    }
}

@Composable
fun TuaiSettingsTabContent(
    uiState: TuaiUiState,
    onTogglePlugin: (String) -> Unit = {},
    onOpenPluginModal: () -> Unit = {},
    onEditWalletAsset: (CryptoAssetItem) -> Unit = {},
    onTogglePrivacy: () -> Unit,
    onToggleBiometric: () -> Unit,
    onToggleNotifications: () -> Unit,
    onSyncFirebase: () -> Unit,
    onResetData: () -> Unit,
    onLogout: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp, vertical = 10.dp)
    ) {
        // Plugin Connector Header Card
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(18.dp))
                .background(Color(0xFFFAF3E8))
                .padding(14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Plugin Konektor Dompet & Bank",
                        fontFamily = PlusJakartaSans,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = TuaiTextDark
                    )
                    Text(
                        text = uiState.lastSyncedLabel,
                        fontFamily = PlusJakartaSans,
                        fontSize = 11.5.sp,
                        color = Color(0xFF4C8C4A)
                    )
                }
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(50))
                        .background(TuaiTerracotta)
                        .clickable(onClick = onOpenPluginModal)
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = "+ Tambah Plugin",
                        fontFamily = PlusJakartaSans,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 11.5.sp,
                        color = Color.White
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = "Daftar Plugin (GoPay, DANA, Mandiri, BRImo, BCA, dll)",
            fontFamily = PlusJakartaSans,
            fontWeight = FontWeight.Bold,
            fontSize = 13.sp,
            color = TuaiTextDark
        )
        Spacer(modifier = Modifier.height(6.dp))

        uiState.assets.forEach { plugin ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 5.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(Color(0xFFFAF3E8).copy(alpha = 0.7f))
                    .clickable { onEditWalletAsset(plugin) }
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    WalletProviderBadge(iconType = plugin.iconType, size = 36.dp)
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = plugin.name,
                            fontFamily = PlusJakartaSans,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = TuaiTextDark
                        )
                        Text(
                            text = "${plugin.tokenAmount} • Ketuk untuk ubah saldo",
                            fontFamily = PlusJakartaSans,
                            fontSize = 11.sp,
                            color = TuaiTextMuted
                        )
                    }
                }
                Switch(
                    checked = plugin.isConnected,
                    onCheckedChange = { onTogglePlugin(plugin.id) },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color.White,
                        checkedTrackColor = TuaiTerracotta
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        SettingsToggleRow(
            title = "Sembunyikan Saldo (Privacy Mode)",
            checked = uiState.isBalanceHidden,
            onCheckedChange = { onTogglePrivacy() }
        )
        HorizontalDivider(color = Color(0xFFF3EFEA))
        SettingsToggleRow(
            title = "Login Sidik Jari Biometrik",
            checked = uiState.biometricEnabled,
            onCheckedChange = { onToggleBiometric() }
        )
        HorizontalDivider(color = Color(0xFFF3EFEA))
        SettingsToggleRow(
            title = "Notifikasi Mutasi Real-Time",
            checked = uiState.notificationsEnabled,
            onCheckedChange = { onToggleNotifications() }
        )

        Spacer(modifier = Modifier.height(10.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(40.dp)
                    .clip(RoundedCornerShape(50))
                    .background(TuaiInputBg)
                    .clickable(onClick = onSyncFirebase),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Sync Semua",
                    fontFamily = PlusJakartaSans,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 12.sp,
                    color = TuaiTextDark
                )
            }
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(40.dp)
                    .clip(RoundedCornerShape(50))
                    .background(TuaiInputBg)
                    .clickable(onClick = onResetData),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Reset Awal",
                    fontFamily = PlusJakartaSans,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 12.sp,
                    color = TuaiTextDark
                )
            }
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(40.dp)
                    .clip(RoundedCornerShape(50))
                    .background(TuaiTerracotta)
                    .clickable(onClick = onLogout),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Keluar",
                    fontFamily = PlusJakartaSans,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 12.sp,
                    color = Color.White
                )
            }
        }
    }
}

@Composable
private fun SettingsToggleRow(
    title: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            fontFamily = PlusJakartaSans,
            fontWeight = FontWeight.Medium,
            fontSize = 13.sp,
            color = TuaiTextDark
        )
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = TuaiTerracotta
            )
        )
    }
}
