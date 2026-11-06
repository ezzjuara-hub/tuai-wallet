package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.PlusJakartaSans
import com.example.ui.theme.TuaiCreamBg
import com.example.ui.theme.TuaiGoldAccent
import com.example.ui.theme.TuaiInputBg
import com.example.ui.theme.TuaiInputHint
import com.example.ui.theme.TuaiTerracotta
import com.example.ui.theme.TuaiTextDark
import com.example.ui.theme.TuaiTextMuted
import com.example.viewmodel.TuaiActiveModal
import com.example.viewmodel.TuaiScreen
import com.example.viewmodel.TuaiUiState
import com.example.viewmodel.TuaiViewModel

@Composable
fun TuaiModalOverlayHost(
    uiState: TuaiUiState,
    viewModel: TuaiViewModel,
    onCopyText: (String, String) -> Unit
) {
    if (uiState.activeModal == TuaiActiveModal.NONE) return
    val context = LocalContext.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.48f))
            .clickable { viewModel.closeModal() },
        contentAlignment = Alignment.BottomCenter
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .widthIn(max = 500.dp)
                .heightIn(max = 680.dp)
                .clickable(enabled = false) {},
            shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
            color = TuaiCreamBg,
            shadowElevation = 16.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 24.dp, vertical = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Drag Handle
                Box(
                    modifier = Modifier
                        .width(44.dp)
                        .height(4.dp)
                        .clip(RoundedCornerShape(50))
                        .background(Color(0xFFD7C8B6))
                )
                Spacer(modifier = Modifier.height(16.dp))

                when (uiState.activeModal) {
                    TuaiActiveModal.PLUGIN_MANAGER -> {
                        Text(
                            text = "Plugin Konektor Dompet & Bank",
                            fontFamily = PlusJakartaSans,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            color = TuaiTextDark
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Hubungkan GoPay, DANA, Mandiri, BRImo, BCA, OVO, ShopeePay & lainnya agar seluruh isi uangmu tampil menyatu.",
                            fontFamily = PlusJakartaSans,
                            fontSize = 12.sp,
                            color = TuaiTextMuted
                        )
                        Spacer(modifier = Modifier.height(10.dp))

                        // Live Aggregated Total Banner inside Plugin Hub
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(14.dp))
                                .background(Color(0xFFF7E4D4))
                                .padding(horizontal = 14.dp, vertical = 10.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "Total Uang Gabungan (${uiState.connectedPluginCount} Plugin)",
                                    fontFamily = PlusJakartaSans,
                                    fontSize = 11.sp,
                                    color = TuaiTextMuted
                                )
                                Text(
                                    text = uiState.walletBalance,
                                    fontFamily = PlusJakartaSans,
                                    fontWeight = FontWeight.ExtraBold,
                                    fontSize = 16.sp,
                                    color = TuaiTerracotta
                                )
                            }
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(50))
                                    .background(TuaiTerracotta)
                                    .clickable { viewModel.syncFirebaseManual() }
                                    .padding(horizontal = 12.dp, vertical = 6.dp)
                            ) {
                                Text(
                                    text = "Sinkron Semua",
                                    fontFamily = PlusJakartaSans,
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 11.sp,
                                    color = Color.White
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        uiState.assets.forEach { plugin ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                                    .clip(RoundedCornerShape(14.dp))
                                    .background(Color.White)
                                    .clickable { viewModel.openAssetDetail(plugin) }
                                    .padding(horizontal = 12.dp, vertical = 10.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.weight(1f)
                                ) {
                                    WalletProviderBadge(iconType = plugin.iconType, size = 38.dp)
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Column {
                                        Text(
                                            text = plugin.name,
                                            fontFamily = PlusJakartaSans,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 13.5.sp,
                                            color = TuaiTextDark
                                        )
                                        Text(
                                            text = "${plugin.tokenAmount} • ${plugin.allocationRole}",
                                            fontFamily = PlusJakartaSans,
                                            fontSize = 11.5.sp,
                                            color = TuaiTextMuted
                                        )
                                    }
                                }

                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(50))
                                        .background(
                                            if (plugin.isConnected) Color(0xFF4C8C4A) else TuaiTerracotta
                                        )
                                        .clickable { viewModel.togglePluginConnection(plugin.id) }
                                        .padding(horizontal = 12.dp, vertical = 6.dp)
                                ) {
                                    Text(
                                        text = if (plugin.isConnected) "Terhubung ✓" else "+ Hubungkan",
                                        fontFamily = PlusJakartaSans,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 11.sp,
                                        color = Color.White
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))
                        HorizontalDivider(color = Color(0xFFE3D8C6))
                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = "Tambah Plugin Dompet / Bank Lainnya",
                            fontFamily = PlusJakartaSans,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = TuaiTextDark
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        // Category Selector: E-WALLET vs BANK
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            listOf("E-WALLET" to "Dompet Digital (E-Wallet)", "BANK" to "Rekening Bank").forEach { (cat, label) ->
                                val selected = uiState.customPluginCategoryInput == cat
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(RoundedCornerShape(50))
                                        .background(if (selected) TuaiTerracotta else Color.White)
                                        .clickable { viewModel.updateCustomPluginCategory(cat) }
                                        .padding(vertical = 8.dp),
                                    contentAlignment = Alignment.Center
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

                        Spacer(modifier = Modifier.height(8.dp))

                        // Custom Plugin Name & Initial Balance
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .weight(1.2f)
                                    .height(44.dp)
                                    .clip(RoundedCornerShape(14.dp))
                                    .background(TuaiInputBg)
                                    .padding(horizontal = 12.dp),
                                contentAlignment = Alignment.CenterStart
                            ) {
                                if (uiState.customPluginNameInput.isEmpty()) {
                                    Text(
                                        text = "Nama (BNI, BSI, LinkAja)",
                                        fontFamily = PlusJakartaSans,
                                        fontSize = 12.sp,
                                        color = TuaiInputHint
                                    )
                                }
                                BasicTextField(
                                    value = uiState.customPluginNameInput,
                                    onValueChange = viewModel::updateCustomPluginName,
                                    singleLine = true,
                                    textStyle = TextStyle(
                                        fontFamily = PlusJakartaSans,
                                        fontWeight = FontWeight.SemiBold,
                                        fontSize = 13.sp,
                                        color = TuaiTextDark
                                    ),
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }

                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(44.dp)
                                    .clip(RoundedCornerShape(14.dp))
                                    .background(TuaiInputBg)
                                    .padding(horizontal = 12.dp),
                                contentAlignment = Alignment.CenterStart
                            ) {
                                BasicTextField(
                                    value = uiState.customPluginBalanceInput,
                                    onValueChange = viewModel::updateCustomPluginBalance,
                                    singleLine = true,
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                    textStyle = TextStyle(
                                        fontFamily = PlusJakartaSans,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.sp,
                                        color = TuaiTextDark
                                    ),
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(46.dp)
                                .clip(RoundedCornerShape(50))
                                .background(TuaiTerracotta)
                                .clickable { viewModel.addNewCustomPlugin() },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "+ Hubungkan Plugin Baru Sekarang",
                                fontFamily = PlusJakartaSans,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 13.5.sp,
                                color = Color.White
                            )
                        }
                    }

                    TuaiActiveModal.ASSET_DETAIL -> {
                        val asset = uiState.assets.find { it.id == uiState.selectedAssetForModal?.id }
                            ?: uiState.selectedAssetForModal
                            ?: uiState.assets.first()

                        WalletProviderBadge(iconType = asset.iconType, size = 54.dp)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = asset.name,
                            fontFamily = PlusJakartaSans,
                            fontWeight = FontWeight.Bold,
                            fontSize = 19.sp,
                            color = TuaiTextDark
                        )
                        Text(
                            text = "${asset.tokenAmount} • ${asset.totalUsdValue}",
                            fontFamily = PlusJakartaSans,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 13.5.sp,
                            color = TuaiTerracotta
                        )
                        Spacer(modifier = Modifier.height(6.dp))

                        // Pos Peruntukan Dompet (tap to cycle role)
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(50))
                                .background(Color.White)
                                .border(1.dp, TuaiTerracotta.copy(alpha = 0.4f), RoundedCornerShape(50))
                                .clickable { viewModel.cycleWalletAllocationRole(asset.id) }
                                .padding(horizontal = 14.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = "Pos Keuangan: ${asset.allocationRole} (Ketuk Ubah ↻)",
                                fontFamily = PlusJakartaSans,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 11.5.sp,
                                color = TuaiTextDark
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Update Account / Phone Number + Real Balance Section
                        Text(
                            text = "Atur No. Rek/HP & Saldo Asli di ${asset.name}",
                            fontFamily = PlusJakartaSans,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 12.sp,
                            color = TuaiTextMuted
                        )
                        Spacer(modifier = Modifier.height(6.dp))

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(42.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color.White)
                                .padding(horizontal = 14.dp),
                            contentAlignment = Alignment.CenterStart
                        ) {
                            BasicTextField(
                                value = uiState.manualAccountInput,
                                onValueChange = viewModel::updateManualAccountInput,
                                singleLine = true,
                                textStyle = TextStyle(
                                    fontFamily = PlusJakartaSans,
                                    fontWeight = FontWeight.Medium,
                                    fontSize = 13.sp,
                                    color = TuaiTextDark
                                ),
                                cursorBrush = SolidColor(TuaiTerracotta),
                                modifier = Modifier.fillMaxWidth()
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(48.dp)
                                    .clip(RoundedCornerShape(14.dp))
                                    .background(TuaiInputBg)
                                    .padding(horizontal = 14.dp),
                                contentAlignment = Alignment.CenterStart
                            ) {
                                BasicTextField(
                                    value = uiState.manualBalanceInput,
                                    onValueChange = viewModel::updateManualBalanceInput,
                                    singleLine = true,
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                    textStyle = TextStyle(
                                        fontFamily = PlusJakartaSans,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 15.sp,
                                        color = TuaiTextDark
                                    ),
                                    cursorBrush = SolidColor(TuaiTerracotta),
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }

                            Box(
                                modifier = Modifier
                                    .height(48.dp)
                                    .clip(RoundedCornerShape(14.dp))
                                    .background(Color(0xFF4C8C4A))
                                    .clickable { viewModel.saveManualWalletBalance(asset.id) }
                                    .padding(horizontal = 16.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "Simpan Saldo",
                                    fontFamily = PlusJakartaSans,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                    color = Color.White
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        // Quick Balance Presets
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            listOf(
                                "500000" to "500 Rb",
                                "1500000" to "1,5 Jt",
                                "5000000" to "5 Jt",
                                "15000000" to "15 Jt"
                            ).forEach { (raw, label) ->
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(RoundedCornerShape(50))
                                        .background(Color.White)
                                        .clickable { viewModel.updateManualBalanceInput(raw) }
                                        .padding(vertical = 6.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = label,
                                        fontFamily = PlusJakartaSans,
                                        fontWeight = FontWeight.SemiBold,
                                        fontSize = 11.5.sp,
                                        color = TuaiTerracotta
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(42.dp)
                                    .clip(RoundedCornerShape(50))
                                    .background(TuaiInputBg)
                                    .clickable {
                                        val launchIntent = if (asset.packageName.isNotBlank()) {
                                            runCatching {
                                                context.packageManager.getLaunchIntentForPackage(asset.packageName)
                                            }.getOrNull()
                                        } else null
                                        if (launchIntent != null) {
                                            runCatching { context.startActivity(launchIntent) }
                                        } else {
                                            viewModel.prepareTopUpForWallet(asset.id)
                                        }
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "+ Isi Saldo",
                                    fontFamily = PlusJakartaSans,
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 12.sp,
                                    color = TuaiTextDark
                                )
                            }
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(42.dp)
                                    .clip(RoundedCornerShape(50))
                                    .background(TuaiGoldAccent)
                                    .clickable {
                                        viewModel.prepareSwapFromWallet(asset.id)
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "Pindah Dana",
                                    fontFamily = PlusJakartaSans,
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 12.sp,
                                    color = TuaiTextDark
                                )
                            }
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(42.dp)
                                    .clip(RoundedCornerShape(50))
                                    .background(TuaiTerracotta)
                                    .clickable {
                                        viewModel.selectWithdrawAsset(asset.id)
                                        viewModel.navigateTo(TuaiScreen.WITHDRAW)
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "Catat Keluar",
                                    fontFamily = PlusJakartaSans,
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 12.sp,
                                    color = Color.White
                                )
                            }
                        }
                    }

                    TuaiActiveModal.SWAP_ASSET -> {
                        val fromAsset = uiState.assets.find { it.id == uiState.swapFromAssetId }
                            ?: uiState.assets.first()
                        val toAsset = uiState.assets.find { it.id == uiState.swapToAssetId }
                            ?: uiState.assets.last()

                        Text(
                            text = "Pindah Dana & Alokasi Antar Dompet",
                            fontFamily = PlusJakartaSans,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            color = TuaiTextDark
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Kondisikan uangmu: pindahkan saldo antar Bank (BCA, Mandiri, BRImo) & E-Wallet (GoPay, DANA)",
                            fontFamily = PlusJakartaSans,
                            fontSize = 12.sp,
                            color = TuaiTextMuted
                        )
                        Spacer(modifier = Modifier.height(14.dp))

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(18.dp))
                                .background(Color.White)
                                .padding(14.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable { viewModel.cycleSwapFromAsset() }
                            ) {
                                Text(
                                    text = "Dari Dompet (Ketuk Ubah ▾)",
                                    fontFamily = PlusJakartaSans,
                                    fontSize = 11.sp,
                                    color = TuaiTextMuted
                                )
                                Text(
                                    text = fromAsset.name,
                                    fontFamily = PlusJakartaSans,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    color = TuaiTextDark
                                )
                                Text(
                                    text = fromAsset.tokenAmount,
                                    fontFamily = PlusJakartaSans,
                                    fontSize = 11.5.sp,
                                    color = TuaiTerracotta
                                )
                            }

                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(CircleShape)
                                    .background(TuaiGoldAccent)
                                    .clickable { viewModel.flipSwapAssets() },
                                contentAlignment = Alignment.Center
                            ) {
                                ActionSwapCircularIcon(size = 20.dp)
                            }

                            Column(
                                horizontalAlignment = Alignment.End,
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable { viewModel.cycleSwapToAsset() }
                            ) {
                                Text(
                                    text = "Ke Dompet (Ketuk Ubah ▾)",
                                    fontFamily = PlusJakartaSans,
                                    fontSize = 11.sp,
                                    color = TuaiTextMuted
                                )
                                Text(
                                    text = toAsset.name,
                                    fontFamily = PlusJakartaSans,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    color = TuaiTerracotta
                                )
                                Text(
                                    text = toAsset.tokenAmount,
                                    fontFamily = PlusJakartaSans,
                                    fontSize = 11.5.sp,
                                    color = Color(0xFF4C8C4A)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .background(TuaiInputBg)
                                .padding(horizontal = 18.dp),
                            contentAlignment = Alignment.CenterStart
                        ) {
                            BasicTextField(
                                value = uiState.swapAmountInput,
                                onValueChange = viewModel::updateSwapAmount,
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                textStyle = TextStyle(
                                    fontFamily = PlusJakartaSans,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp,
                                    color = TuaiTextDark
                                ),
                                cursorBrush = SolidColor(TuaiTerracotta),
                                modifier = Modifier.fillMaxWidth()
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            listOf(
                                "100000" to "Rp 100rb",
                                "250000" to "Rp 250rb",
                                "500000" to "Rp 500rb",
                                "1000000" to "Rp 1 Jt"
                            ).forEach { (preset, label) ->
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(RoundedCornerShape(50))
                                        .background(Color.White)
                                        .clickable { viewModel.updateSwapAmount(preset) }
                                        .padding(vertical = 8.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = label,
                                        fontFamily = PlusJakartaSans,
                                        fontWeight = FontWeight.SemiBold,
                                        fontSize = 11.5.sp,
                                        color = TuaiTerracotta
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp)
                                .clip(RoundedCornerShape(50))
                                .background(TuaiTerracotta)
                                .clickable { viewModel.submitSwap() },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Pindahkan Dana Sekarang",
                                fontFamily = PlusJakartaSans,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 14.5.sp,
                                color = Color.White
                            )
                        }
                    }

                    TuaiActiveModal.TOP_UP -> {
                        Text(
                            text = "Isi Saldo / Catat Pemasukan",
                            fontFamily = PlusJakartaSans,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            color = TuaiTextDark
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Pilih dompet/rekening tujuan dan nominal uang masuk",
                            fontFamily = PlusJakartaSans,
                            fontSize = 12.5.sp,
                            color = TuaiTextMuted
                        )
                        Spacer(modifier = Modifier.height(12.dp))

                        // Target Wallet Horizontal Picker
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .horizontalScroll(rememberScrollState()),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            uiState.assets.forEach { wallet ->
                                val isTarget = uiState.topUpTargetAssetId == wallet.id
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(50))
                                        .background(if (isTarget) TuaiTerracotta else Color.White)
                                        .clickable { viewModel.updateTopUpTargetAsset(wallet.id) }
                                        .padding(horizontal = 14.dp, vertical = 8.dp)
                                ) {
                                    Text(
                                        text = wallet.name,
                                        fontFamily = PlusJakartaSans,
                                        fontWeight = FontWeight.SemiBold,
                                        fontSize = 12.sp,
                                        color = if (isTarget) Color.White else TuaiTextDark
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            listOf(
                                "100000" to "+100rb",
                                "500000" to "+500rb",
                                "1000000" to "+1 Jt",
                                "5000000" to "+5 Jt"
                            ).forEach { (preset, label) ->
                                val selected = uiState.topUpAmountInput == preset
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(RoundedCornerShape(14.dp))
                                        .background(if (selected) TuaiTerracotta else Color.White)
                                        .clickable { viewModel.updateTopUpAmount(preset) }
                                        .padding(vertical = 10.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = label,
                                        fontFamily = PlusJakartaSans,
                                        fontWeight = FontWeight.SemiBold,
                                        fontSize = 12.sp,
                                        color = if (selected) Color.White else TuaiTextDark
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        listOf(
                            "Transfer dari BCA (myBCA)",
                            "Gaji / Penghasilan Bulanan",
                            "Transfer Masuk / QRIS"
                        ).forEach { method ->
                            val isSelected = uiState.topUpMethod == method
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 3.dp)
                                    .clip(RoundedCornerShape(14.dp))
                                    .background(if (isSelected) Color(0xFFF7E4D4) else Color.White)
                                    .clickable { viewModel.updateTopUpMethod(method) }
                                    .padding(horizontal = 14.dp, vertical = 10.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = method,
                                    fontFamily = PlusJakartaSans,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                    fontSize = 13.sp,
                                    color = TuaiTextDark
                                )
                                Text(
                                    text = if (isSelected) "Dipilih ✓" else "Pilih",
                                    fontFamily = PlusJakartaSans,
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 12.sp,
                                    color = TuaiTerracotta
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        val numericTopUp = uiState.topUpAmountInput.toDoubleOrNull() ?: 0.0
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp)
                                .clip(RoundedCornerShape(50))
                                .background(TuaiTerracotta)
                                .clickable { viewModel.submitTopUp() },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Tambah +${TuaiViewModel.formatHeadlineBalance(numericTopUp)} Sekarang",
                                fontFamily = PlusJakartaSans,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 14.5.sp,
                                color = Color.White
                            )
                        }
                    }

                    TuaiActiveModal.RECEIVE_QR -> {
                        Text(
                            text = "Terima Dana & QRIS Tuai",
                            fontFamily = PlusJakartaSans,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            color = TuaiTextDark
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Pindai kode QR atau bagikan ID Dompet Tuai Anda",
                            fontFamily = PlusJakartaSans,
                            fontSize = 12.5.sp,
                            color = TuaiTextMuted
                        )
                        Spacer(modifier = Modifier.height(16.dp))

                        Box(
                            modifier = Modifier
                                .size(154.dp)
                                .clip(RoundedCornerShape(20.dp))
                                .background(Color.White)
                                .border(2.dp, TuaiGoldAccent, RoundedCornerShape(20.dp))
                                .padding(16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Canvas(modifier = Modifier.fillMaxSize()) {
                                val cell = size.width / 7f
                                val pattern = listOf(
                                    listOf(1, 1, 1, 0, 1, 1, 1),
                                    listOf(1, 0, 1, 1, 1, 0, 1),
                                    listOf(1, 1, 1, 0, 1, 1, 1),
                                    listOf(0, 1, 0, 1, 0, 1, 0),
                                    listOf(1, 1, 1, 1, 1, 0, 1),
                                    listOf(1, 0, 1, 0, 0, 1, 0),
                                    listOf(1, 1, 1, 0, 1, 1, 1)
                                )
                                for (r in 0..6) {
                                    for (c in 0..6) {
                                        if (pattern[r][c] == 1) {
                                            drawRect(
                                                color = Color(0xFF231A16),
                                                topLeft = Offset(c * cell, r * cell),
                                                size = Size(cell * 0.86f, cell * 0.86f)
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "Wallet ID: ${uiState.walletId} • ${uiState.walletAddressHex}",
                            fontFamily = PlusJakartaSans,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 13.sp,
                            color = TuaiTextDark
                        )
                        Spacer(modifier = Modifier.height(14.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(46.dp)
                                    .clip(RoundedCornerShape(50))
                                    .background(TuaiInputBg)
                                    .clickable {
                                        onCopyText(
                                            uiState.walletId,
                                            "Wallet ID ${uiState.walletId} disalin!"
                                        )
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "Salin ID",
                                    fontFamily = PlusJakartaSans,
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 13.5.sp,
                                    color = TuaiTextDark
                                )
                            }
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(46.dp)
                                    .clip(RoundedCornerShape(50))
                                    .background(TuaiTerracotta)
                                    .clickable { viewModel.simulateReceiveFunds(500_000.0) },
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "Terima +Rp 500rb",
                                    fontFamily = PlusJakartaSans,
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 13.5.sp,
                                    color = Color.White
                                )
                            }
                        }
                    }

                    TuaiActiveModal.NOTIFICATIONS -> {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Pusat Notifikasi & Kondisi",
                                fontFamily = PlusJakartaSans,
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp,
                                color = TuaiTextDark
                            )
                            Text(
                                text = "Tandai Dibaca",
                                fontFamily = PlusJakartaSans,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 12.5.sp,
                                color = TuaiTerracotta,
                                modifier = Modifier.clickable { viewModel.markAllNotificationsRead() }
                            )
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                        uiState.notifications.take(4).forEach { notif ->
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 5.dp)
                                    .clip(RoundedCornerShape(14.dp))
                                    .background(Color.White)
                                    .padding(14.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = notif.title,
                                        fontFamily = PlusJakartaSans,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.5.sp,
                                        color = TuaiTextDark
                                    )
                                    Text(
                                        text = notif.time,
                                        fontFamily = PlusJakartaSans,
                                        fontSize = 11.5.sp,
                                        color = TuaiTextMuted
                                    )
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = notif.message,
                                    fontFamily = PlusJakartaSans,
                                    fontSize = 12.sp,
                                    color = TuaiTextMuted
                                )
                            }
                        }
                    }

                    TuaiActiveModal.FORGOT_PASSWORD -> {
                        Text(
                            text = "Pemulihan Kata Sandi",
                            fontFamily = PlusJakartaSans,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            color = TuaiTextDark
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Masukkan email atau nomor HP terdaftar untuk menerima tautan reset Firebase Auth.",
                            fontFamily = PlusJakartaSans,
                            fontSize = 13.sp,
                            color = TuaiTextMuted
                        )
                        Spacer(modifier = Modifier.height(14.dp))
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                                .clip(RoundedCornerShape(50))
                                .background(TuaiInputBg)
                                .padding(horizontal = 20.dp),
                            contentAlignment = Alignment.CenterStart
                        ) {
                            if (uiState.recoveryInput.isEmpty()) {
                                Text(
                                    text = uiState.emailOrPhone.ifBlank { "nama@email.com" },
                                    fontFamily = PlusJakartaSans,
                                    fontSize = 14.sp,
                                    color = TuaiInputHint
                                )
                            }
                            BasicTextField(
                                value = uiState.recoveryInput,
                                onValueChange = viewModel::updateRecoveryInput,
                                singleLine = true,
                                textStyle = TextStyle(
                                    fontFamily = PlusJakartaSans,
                                    fontSize = 14.sp,
                                    color = TuaiTextDark
                                ),
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp)
                                .clip(RoundedCornerShape(50))
                                .background(TuaiTerracotta)
                                .clickable { viewModel.submitForgotPassword() },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Kirim Tautan Pemulihan",
                                fontFamily = PlusJakartaSans,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 14.5.sp,
                                color = Color.White
                            )
                        }
                    }

                    TuaiActiveModal.BIOMETRIC_PROMPT -> {
                        Text(
                            text = "Verifikasi Biometrik Tuai",
                            fontFamily = PlusJakartaSans,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            color = TuaiTextDark
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Sentuh sensor sidik jari di bawah untuk mengakses seluruh dompet & bank terhubung",
                            fontFamily = PlusJakartaSans,
                            fontSize = 13.sp,
                            color = TuaiTextMuted
                        )
                        Spacer(modifier = Modifier.height(20.dp))
                        Box(
                            modifier = Modifier
                                .size(84.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFF7E4D4))
                                .border(2.dp, TuaiTerracotta, CircleShape)
                                .clickable { viewModel.loginWithSocial("Sidik Jari Biometrik") },
                            contentAlignment = Alignment.Center
                        ) {
                            FingerprintTerracottaIcon(tint = TuaiTerracotta, size = 44.dp)
                        }
                        Spacer(modifier = Modifier.height(14.dp))
                        Text(
                            text = "Ketuk ikon sidik jari untuk memindai",
                            fontFamily = PlusJakartaSans,
                            fontWeight = FontWeight.Medium,
                            fontSize = 12.5.sp,
                            color = TuaiTerracotta
                        )
                    }

                    TuaiActiveModal.TRANSACTION_DETAIL -> {
                        val tx = uiState.selectedTxForModal ?: uiState.transactions.first()
                        WalletProviderBadge(iconType = tx.iconType, size = 48.dp)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Rincian Mutasi Dompet",
                            fontFamily = PlusJakartaSans,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            color = TuaiTextDark
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = tx.amount,
                            fontFamily = PlusJakartaSans,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 26.sp,
                            color = if (tx.isPositive) Color(0xFF4C8C4A) else TuaiTerracotta
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "${tx.title} • ${tx.status}",
                            fontFamily = PlusJakartaSans,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 14.sp,
                            color = TuaiTextDark
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "${tx.date} | ${tx.referenceNote}",
                            fontFamily = PlusJakartaSans,
                            fontSize = 12.5.sp,
                            color = TuaiTextMuted
                        )
                    }

                    TuaiActiveModal.WITHDRAW_EXCHANGE_PICKER -> {
                        Text(
                            text = "Pilih Dompet / Rekening Sumber Dana",
                            fontFamily = PlusJakartaSans,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            color = TuaiTextDark
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        uiState.connectedAssets.forEach { asset ->
                            val isSelected = uiState.selectedWithdrawAssetId == asset.id
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                                    .clip(RoundedCornerShape(14.dp))
                                    .background(if (isSelected) Color(0xFFF7E4D4) else Color.White)
                                    .clickable { viewModel.selectWithdrawAsset(asset.id) }
                                    .padding(12.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    WalletProviderBadge(iconType = asset.iconType, size = 34.dp)
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Column {
                                        Text(
                                            text = asset.name,
                                            fontFamily = PlusJakartaSans,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 13.5.sp,
                                            color = TuaiTextDark
                                        )
                                        Text(
                                            text = "${asset.tokenAmount} • ${asset.allocationRole}",
                                            fontFamily = PlusJakartaSans,
                                            fontSize = 11.5.sp,
                                            color = TuaiTextMuted
                                        )
                                    }
                                }
                                Text(
                                    text = if (isSelected) "Aktif ✓" else "Pilih",
                                    fontFamily = PlusJakartaSans,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp,
                                    color = TuaiTerracotta
                                )
                            }
                        }
                    }

                    TuaiActiveModal.PROFILE_ACCOUNT -> {
                        Text(
                            text = uiState.userName,
                            fontFamily = PlusJakartaSans,
                            fontWeight = FontWeight.Bold,
                            fontSize = 19.sp,
                            color = TuaiTextDark
                        )
                        Text(
                            text = "${uiState.userEmail} • ID #${uiState.walletId}",
                            fontFamily = PlusJakartaSans,
                            fontSize = 13.sp,
                            color = TuaiTextMuted
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = uiState.financialHealthStatus,
                            fontFamily = PlusJakartaSans,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 12.sp,
                            color = Color(0xFF4C8C4A)
                        )
                        Spacer(modifier = Modifier.height(14.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(46.dp)
                                    .clip(RoundedCornerShape(50))
                                    .background(TuaiInputBg)
                                    .clickable {
                                        viewModel.openModal(TuaiActiveModal.PLUGIN_MANAGER)
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "Kelola Plugin",
                                    fontFamily = PlusJakartaSans,
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 13.5.sp,
                                    color = TuaiTextDark
                                )
                            }
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(46.dp)
                                    .clip(RoundedCornerShape(50))
                                    .background(TuaiTerracotta)
                                    .clickable { viewModel.logout() },
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "Keluar Akun",
                                    fontFamily = PlusJakartaSans,
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 13.5.sp,
                                    color = Color.White
                                )
                            }
                        }
                    }

                    TuaiActiveModal.NONE -> {}
                }

                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}
