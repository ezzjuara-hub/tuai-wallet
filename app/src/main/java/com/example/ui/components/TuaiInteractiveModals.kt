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
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.PlusJakartaSans
import com.example.ui.theme.TuaiBnbYellow
import com.example.ui.theme.TuaiCreamBg
import com.example.ui.theme.TuaiEthOrange
import com.example.ui.theme.TuaiGoldAccent
import com.example.ui.theme.TuaiInputBg
import com.example.ui.theme.TuaiInputHint
import com.example.ui.theme.TuaiTerracotta
import com.example.ui.theme.TuaiTerracottaDark
import com.example.ui.theme.TuaiTextDark
import com.example.ui.theme.TuaiTextMuted
import com.example.ui.theme.TuaiUsdtGreen
import com.example.viewmodel.AssetIconType
import com.example.viewmodel.TuaiActiveModal
import com.example.viewmodel.TuaiUiState
import com.example.viewmodel.TuaiViewModel

@Composable
fun TuaiModalOverlayHost(
    uiState: TuaiUiState,
    viewModel: TuaiViewModel,
    onCopyText: (String, String) -> Unit
) {
    if (uiState.activeModal == TuaiActiveModal.NONE) return

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
                .clickable(enabled = false) {},
            shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
            color = TuaiCreamBg,
            shadowElevation = 16.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
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
                Spacer(modifier = Modifier.height(18.dp))

                when (uiState.activeModal) {
                    TuaiActiveModal.RECEIVE_QR -> {
                        Text(
                            text = "Terima Aset & QR Dompet",
                            fontFamily = PlusJakartaSans,
                            fontWeight = FontWeight.Bold,
                            fontSize = 19.sp,
                            color = TuaiTextDark
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Pindai kode QR atau bagikan ID Dompet Tuai Anda",
                            fontFamily = PlusJakartaSans,
                            fontSize = 13.sp,
                            color = TuaiTextMuted
                        )
                        Spacer(modifier = Modifier.height(18.dp))

                        // Simulated QR Code Box
                        Box(
                            modifier = Modifier
                                .size(164.dp)
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

                        Spacer(modifier = Modifier.height(14.dp))
                        Text(
                            text = "Wallet ID: ${uiState.walletId} • ${uiState.walletAddressHex}",
                            fontFamily = PlusJakartaSans,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 13.5.sp,
                            color = TuaiTextDark
                        )
                        Spacer(modifier = Modifier.height(16.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(48.dp)
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
                                    fontSize = 14.sp,
                                    color = TuaiTextDark
                                )
                            }
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(48.dp)
                                    .clip(RoundedCornerShape(50))
                                    .background(TuaiTerracotta)
                                    .clickable { viewModel.simulateReceiveFunds(500.0) },
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "Terima +$500",
                                    fontFamily = PlusJakartaSans,
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 14.sp,
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
                            text = "Swap & Konversi Aset Instan",
                            fontFamily = PlusJakartaSans,
                            fontWeight = FontWeight.Bold,
                            fontSize = 19.sp,
                            color = TuaiTextDark
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Tukar aset kripto secara real-time tanpa biaya tersembunyi",
                            fontFamily = PlusJakartaSans,
                            fontSize = 13.sp,
                            color = TuaiTextMuted
                        )
                        Spacer(modifier = Modifier.height(16.dp))

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(18.dp))
                                .background(Color.White)
                                .padding(14.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "Dari Aset",
                                    fontFamily = PlusJakartaSans,
                                    fontSize = 12.sp,
                                    color = TuaiTextMuted
                                )
                                Text(
                                    text = "${fromAsset.name} (${fromAsset.symbol})",
                                    fontFamily = PlusJakartaSans,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp,
                                    color = TuaiTextDark
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
                            Column(horizontalAlignment = Alignment.End) {
                                Text(
                                    text = "Ke Aset",
                                    fontFamily = PlusJakartaSans,
                                    fontSize = 12.sp,
                                    color = TuaiTextMuted
                                )
                                Text(
                                    text = "${toAsset.name} (${toAsset.symbol})",
                                    fontFamily = PlusJakartaSans,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp,
                                    color = TuaiTerracotta
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Nominal Input
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(52.dp)
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
                            listOf("100", "500", "1000", "2500").forEach { preset ->
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
                                        text = "$$preset",
                                        fontFamily = PlusJakartaSans,
                                        fontWeight = FontWeight.SemiBold,
                                        fontSize = 12.5.sp,
                                        color = TuaiTerracotta
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                                .clip(RoundedCornerShape(50))
                                .background(TuaiTerracotta)
                                .clickable { viewModel.submitSwap() },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Konfirmasi Swap Sekarang",
                                fontFamily = PlusJakartaSans,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 15.sp,
                                color = Color.White
                            )
                        }
                    }

                    TuaiActiveModal.TOP_UP -> {
                        Text(
                            text = "Top Up & Tambah Aset",
                            fontFamily = PlusJakartaSans,
                            fontWeight = FontWeight.Bold,
                            fontSize = 19.sp,
                            color = TuaiTextDark
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Pilih nominal dan metode pembayaran instan",
                            fontFamily = PlusJakartaSans,
                            fontSize = 13.sp,
                            color = TuaiTextMuted
                        )
                        Spacer(modifier = Modifier.height(14.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            listOf("100", "500", "1000", "5000").forEach { preset ->
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
                                        text = "+$$preset",
                                        fontFamily = PlusJakartaSans,
                                        fontWeight = FontWeight.SemiBold,
                                        fontSize = 13.sp,
                                        color = if (selected) Color.White else TuaiTextDark
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        listOf(
                            "BCA Virtual Account",
                            "Mandiri Virtual Account",
                            "QRIS Tuai Instan"
                        ).forEach { method ->
                            val isSelected = uiState.topUpMethod == method
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                                    .clip(RoundedCornerShape(14.dp))
                                    .background(if (isSelected) Color(0xFFF7E4D4) else Color.White)
                                    .clickable { viewModel.updateTopUpMethod(method) }
                                    .padding(horizontal = 16.dp, vertical = 12.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = method,
                                    fontFamily = PlusJakartaSans,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                    fontSize = 13.5.sp,
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

                        Spacer(modifier = Modifier.height(16.dp))

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                                .clip(RoundedCornerShape(50))
                                .background(TuaiTerracotta)
                                .clickable { viewModel.submitTopUp() },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Top Up +$${uiState.topUpAmountInput} Sekarang",
                                fontFamily = PlusJakartaSans,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 15.sp,
                                color = Color.White
                            )
                        }
                    }

                    TuaiActiveModal.NOTIFICATIONS -> {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Pusat Notifikasi",
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
                                    fontSize = 12.5.sp,
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
                            text = "Sentuh sensor sidik jari di bawah untuk masuk secara instan",
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

                    TuaiActiveModal.ASSET_DETAIL -> {
                        val asset = uiState.selectedAssetForModal ?: uiState.assets.first()
                        Text(
                            text = "${asset.name} (${asset.symbol})",
                            fontFamily = PlusJakartaSans,
                            fontWeight = FontWeight.Bold,
                            fontSize = 19.sp,
                            color = TuaiTextDark
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "${asset.tokenAmount} • Nilai Total ${asset.totalUsdValue}",
                            fontFamily = PlusJakartaSans,
                            fontWeight = FontWeight.Medium,
                            fontSize = 13.5.sp,
                            color = TuaiTerracotta
                        )
                        Spacer(modifier = Modifier.height(16.dp))
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
                                        viewModel.openModal(TuaiActiveModal.SWAP_ASSET)
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "Swap ${asset.symbol}",
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
                                    .clickable {
                                        viewModel.selectWithdrawAsset(asset.id)
                                        viewModel.navigateTo(com.example.viewmodel.TuaiScreen.WITHDRAW)
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "Withdraw ${asset.symbol}",
                                    fontFamily = PlusJakartaSans,
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 13.5.sp,
                                    color = Color.White
                                )
                            }
                        }
                    }

                    TuaiActiveModal.TRANSACTION_DETAIL -> {
                        val tx = uiState.selectedTxForModal ?: uiState.transactions.first()
                        Text(
                            text = "Bukti Transaksi Resmi",
                            fontFamily = PlusJakartaSans,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            color = TuaiTextDark
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = tx.amount,
                            fontFamily = PlusJakartaSans,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 28.sp,
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
                            text = "Pilih Aset & Tujuan Penarikan",
                            fontFamily = PlusJakartaSans,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            color = TuaiTextDark
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        uiState.assets.forEach { asset ->
                            val isSelected = uiState.selectedWithdrawAssetId == asset.id
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                                    .clip(RoundedCornerShape(14.dp))
                                    .background(if (isSelected) Color(0xFFF7E4D4) else Color.White)
                                    .clickable { viewModel.selectWithdrawAsset(asset.id) }
                                    .padding(14.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "${asset.name} (${asset.tokenAmount})",
                                    fontFamily = PlusJakartaSans,
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 14.sp,
                                    color = TuaiTextDark
                                )
                                Text(
                                    text = if (isSelected) "Aktif ✓" else "Pilih",
                                    fontFamily = PlusJakartaSans,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.5.sp,
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
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Status Database: ${uiState.lastSyncedLabel}",
                            fontFamily = PlusJakartaSans,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 12.sp,
                            color = Color(0xFF4C8C4A)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
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
                                        viewModel.selectBottomNav(3)
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "Pengaturan",
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

@Composable
fun TuaiHomeTabContent(
    uiState: TuaiUiState,
    onNavigateToWithdraw: () -> Unit,
    onOpenModal: (TuaiActiveModal) -> Unit,
    onContributeGoal: (String) -> Unit,
    onSwitchToWalletTab: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp, vertical = 12.dp)
    ) {
        Text(
            text = "Halo, ${uiState.userName}",
            fontFamily = PlusJakartaSans,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            color = TuaiTextDark
        )
        Text(
            text = "Pantau & panen pertumbuhan aset finansialmu hari ini",
            fontFamily = PlusJakartaSans,
            fontSize = 13.sp,
            color = TuaiTextMuted
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Target Tuai / Financial Harvest Goals
        Text(
            text = "Target Panen Finansial (Tuai Goals)",
            fontFamily = PlusJakartaSans,
            fontWeight = FontWeight.SemiBold,
            fontSize = 15.sp,
            color = TuaiTextDark
        )
        Spacer(modifier = Modifier.height(10.dp))

        uiState.savingsGoals.forEach { goal ->
            val progress = (goal.currentUsd / goal.targetUsd).toFloat().coerceIn(0f, 1f)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp)
                    .clip(RoundedCornerShape(18.dp))
                    .background(Color(0xFFFAF3E8))
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = goal.title,
                        fontFamily = PlusJakartaSans,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.5.sp,
                        color = TuaiTextDark,
                        modifier = Modifier.weight(1f)
                    )
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(50))
                            .background(TuaiTerracotta)
                            .clickable { onContributeGoal(goal.id) }
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "+ Panen $250",
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
                        text = "$${goal.currentUsd.toInt()} terkumpul",
                        fontFamily = PlusJakartaSans,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 12.sp,
                        color = TuaiTerracottaDark
                    )
                    Text(
                        text = "Target $${goal.targetUsd.toInt()} (${goal.deadline})",
                        fontFamily = PlusJakartaSans,
                        fontSize = 12.sp,
                        color = TuaiTextMuted
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Aktivitas Terkini",
                fontFamily = PlusJakartaSans,
                fontWeight = FontWeight.SemiBold,
                fontSize = 15.sp,
                color = TuaiTextDark
            )
            Text(
                text = "Lihat Dompet →",
                fontFamily = PlusJakartaSans,
                fontWeight = FontWeight.SemiBold,
                fontSize = 12.5.sp,
                color = TuaiTerracotta,
                modifier = Modifier.clickable(onClick = onSwitchToWalletTab)
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        uiState.transactions.take(2).forEach { tx ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = tx.title,
                        fontFamily = PlusJakartaSans,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp,
                        color = TuaiTextDark
                    )
                    Text(
                        text = tx.date,
                        fontFamily = PlusJakartaSans,
                        fontSize = 12.sp,
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
            .padding(horizontal = 24.dp, vertical = 12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Analitik Portofolio",
                    fontFamily = PlusJakartaSans,
                    fontWeight = FontWeight.Bold,
                    fontSize = 19.sp,
                    color = TuaiTextDark
                )
                Text(
                    text = "Pertumbuhan +18.64% (${uiState.selectedChartTimeframe})",
                    fontFamily = PlusJakartaSans,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 13.sp,
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

        Spacer(modifier = Modifier.height(14.dp))

        // Smooth Area Line Chart Canvas
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(148.dp)
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

        Spacer(modifier = Modifier.height(14.dp))

        Text(
            text = "Alokasi Aset Kripto",
            fontFamily = PlusJakartaSans,
            fontWeight = FontWeight.SemiBold,
            fontSize = 14.5.sp,
            color = TuaiTextDark
        )
        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(10.dp)
                .clip(RoundedCornerShape(50))
        ) {
            Box(modifier = Modifier.weight(0.45f).fillMaxSize().background(TuaiEthOrange))
            Box(modifier = Modifier.weight(0.25f).fillMaxSize().background(TuaiBnbYellow))
            Box(modifier = Modifier.weight(0.30f).fillMaxSize().background(TuaiUsdtGreen))
        }

        Spacer(modifier = Modifier.height(14.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(44.dp)
                    .clip(RoundedCornerShape(50))
                    .background(TuaiGoldAccent)
                    .clickable(onClick = onOpenSwap),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Swap Aset",
                    fontFamily = PlusJakartaSans,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 13.5.sp,
                    color = TuaiTextDark
                )
            }
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(44.dp)
                    .clip(RoundedCornerShape(50))
                    .background(TuaiTerracotta)
                    .clickable(onClick = onNavigateToWithdraw),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Withdraw",
                    fontFamily = PlusJakartaSans,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 13.5.sp,
                    color = Color.White
                )
            }
        }
    }
}

@Composable
fun TuaiSettingsTabContent(
    uiState: TuaiUiState,
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
        // Firebase Cloud Sync Banner
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
                        text = "Firebase Cloud & Local Database",
                        fontFamily = PlusJakartaSans,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = TuaiTextDark
                    )
                    Text(
                        text = uiState.lastSyncedLabel,
                        fontFamily = PlusJakartaSans,
                        fontSize = 12.sp,
                        color = Color(0xFF4C8C4A)
                    )
                }
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(50))
                        .background(TuaiTerracotta)
                        .clickable(onClick = onSyncFirebase)
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = "Sync Cloud",
                        fontFamily = PlusJakartaSans,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 11.5.sp,
                        color = Color.White
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Settings Switches
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
            title = "Notifikasi Transaksi Real-Time",
            checked = uiState.notificationsEnabled,
            onCheckedChange = { onToggleNotifications() }
        )

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
                    .background(TuaiInputBg)
                    .clickable(onClick = onResetData),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Reset Demo",
                    fontFamily = PlusJakartaSans,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 13.sp,
                    color = TuaiTextDark
                )
            }
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(42.dp)
                    .clip(RoundedCornerShape(50))
                    .background(TuaiTerracotta)
                    .clickable(onClick = onLogout),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Keluar Akun",
                    fontFamily = PlusJakartaSans,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 13.sp,
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
            fontSize = 13.5.sp,
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
