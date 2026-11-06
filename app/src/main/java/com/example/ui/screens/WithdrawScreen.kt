package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.components.ActionPlusIcon
import com.example.ui.components.KeypadBackspaceTagIcon
import com.example.ui.components.KeypadReloadIcon
import com.example.ui.theme.PlusJakartaSans
import com.example.ui.theme.TuaiExchangeCardBg
import com.example.ui.theme.TuaiExchangeText
import com.example.ui.theme.TuaiKeypadCircleBg
import com.example.ui.theme.TuaiPlusCircleBg
import com.example.ui.theme.TuaiTerracotta
import com.example.ui.theme.TuaiTextDark
import com.example.viewmodel.TuaiUiState
import kotlin.math.roundToInt

@Composable
fun WithdrawScreen(
    uiState: TuaiUiState,
    onBackToWallet: () -> Unit,
    onToggleStar: () -> Unit,
    onDigitClick: (String) -> Unit,
    onResetClick: () -> Unit,
    onBackspaceClick: () -> Unit,
    onWithdrawComplete: () -> Unit,
    modifier: Modifier = Modifier,
    onExchangeCardClick: () -> Unit = {},
    onPlusPresetClick: () -> Unit = onResetClick,
    onSelectWithdrawDestination: (String) -> Unit = {}
) {
    BackHandler(onBack = onBackToWallet)
    val activeAsset = uiState.connectedAssets.find { it.id == uiState.selectedWithdrawAssetId }
        ?: uiState.connectedAssets.firstOrNull()
        ?: uiState.assets.first()

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
            Spacer(modifier = Modifier.height(10.dp))

            // Top App Bar: Back (<) | "Catat Keluar / Transfer" | Star (☆)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .clickable(onClick = onBackToWallet)
                        .testTag("withdraw_back_button"),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                        contentDescription = "Back",
                        tint = Color.White,
                        modifier = Modifier.size(26.dp)
                    )
                }

                Text(
                    text = stringResource(R.string.withdraw_money_title),
                    fontFamily = PlusJakartaSans,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 18.sp,
                    color = Color.White
                )

                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .clickable(onClick = onToggleStar)
                        .testTag("withdraw_star_button"),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (uiState.isWithdrawStarred) {
                            Icons.Filled.Star
                        } else {
                            Icons.Outlined.StarBorder
                        },
                        contentDescription = "Favorite",
                        tint = Color.White,
                        modifier = Modifier.size(22.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Stacked Cards Area: "Sumber Dana" Peach Card + Main White Rounded Sheet
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                // Back "Sumber Dana" Card (Peach #EAC2B0) - Interactive Wallet/Bank Picker
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .height(84.dp)
                        .clip(RoundedCornerShape(topStart = 26.dp, topEnd = 26.dp))
                        .background(TuaiExchangeCardBg)
                        .clickable(onClick = onExchangeCardClick)
                        .padding(horizontal = 22.dp, vertical = 18.dp)
                        .testTag("withdraw_exchange_card"),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Text(
                        text = stringResource(R.string.exchange_label),
                        fontFamily = PlusJakartaSans,
                        fontWeight = FontWeight.Medium,
                        fontSize = 15.sp,
                        color = TuaiExchangeText
                    )
                    Text(
                        text = "${activeAsset.name} (${activeAsset.tokenAmount}) ▾",
                        fontFamily = PlusJakartaSans,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 12.5.sp,
                        color = TuaiExchangeText
                    )
                }

                // Foreground Main White Sheet overlapping the Sumber Dana card
                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 54.dp),
                    shape = RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp),
                    color = Color.White,
                    shadowElevation = 8.dp
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .windowInsetsPadding(WindowInsets.navigationBars)
                            .padding(horizontal = 24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Spacer(modifier = Modifier.height(18.dp))

                        // Current Wallet Balance Row + Soft Peach '+' Preset Button
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "${stringResource(R.string.current_wallet_balance)} (${activeAsset.name}: ${activeAsset.tokenAmount})",
                                    fontFamily = PlusJakartaSans,
                                    fontWeight = FontWeight.Normal,
                                    fontSize = 12.sp,
                                    color = Color(0xFF8E8680)
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = uiState.walletBalance,
                                    fontFamily = PlusJakartaSans,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 26.sp,
                                    color = TuaiTextDark
                                )
                            }

                            Box(
                                modifier = Modifier
                                    .size(44.dp)
                                    .clip(CircleShape)
                                    .background(TuaiPlusCircleBg)
                                    .clickable(onClick = onPlusPresetClick)
                                    .testTag("balance_plus_button"),
                                contentAlignment = Alignment.Center
                            ) {
                                ActionPlusIcon(
                                    tint = TuaiTerracotta,
                                    size = 16.dp
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        HorizontalDivider(
                            thickness = 1.dp,
                            color = Color(0xFFF4EFEA)
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        // "Nominal Pengeluaran / Transfer" & "Rp 150.000"
                        Text(
                            text = stringResource(R.string.withdraw_amount_label),
                            fontFamily = PlusJakartaSans,
                            fontWeight = FontWeight.Normal,
                            fontSize = 13.sp,
                            color = Color(0xFF7D746D)
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = "Rp ${uiState.withdrawAmount}",
                            fontFamily = PlusJakartaSans,
                            fontWeight = FontWeight.Bold,
                            fontSize = 34.sp,
                            color = TuaiTerracotta,
                            letterSpacing = (-0.5).sp,
                            modifier = Modifier.testTag("withdraw_amount_display")
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        // Quick Expense / Transfer Purpose Chips
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .horizontalScroll(rememberScrollState()),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            listOf(
                                "QRIS & Makan Harian",
                                "Transfer Antar Bank",
                                "Tagihan & Listrik",
                                "Belanja Kebutuhan"
                            ).forEach { purpose ->
                                val selected = uiState.withdrawDestination == purpose
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(50))
                                        .background(
                                            if (selected) TuaiTerracotta else Color(0xFFFAF3E8)
                                        )
                                        .clickable { onSelectWithdrawDestination(purpose) }
                                        .padding(horizontal = 12.dp, vertical = 5.dp)
                                ) {
                                    Text(
                                        text = purpose,
                                        fontFamily = PlusJakartaSans,
                                        fontWeight = FontWeight.SemiBold,
                                        fontSize = 11.sp,
                                        color = if (selected) Color.White else TuaiTextDark
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.weight(1f))

                        // 4x3 Circular Keypad (1 2 3 4 / 5 6 7 8 / ↻ 9 0 ⌫)
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            // Row 1: 1, 2, 3, 4
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                listOf("1", "2", "3", "4").forEach { digit ->
                                    KeypadDigitButton(
                                        digit = digit,
                                        onClick = { onDigitClick(digit) }
                                    )
                                }
                            }

                            // Row 2: 5, 6, 7, 8
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                listOf("5", "6", "7", "8").forEach { digit ->
                                    KeypadDigitButton(
                                        digit = digit,
                                        onClick = { onDigitClick(digit) }
                                    )
                                }
                            }

                            // Row 3: Reload (↻), 9, 0, Backspace (⌫)
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                KeypadIconButton(
                                    testTag = "keypad_reload",
                                    onClick = onResetClick
                                ) {
                                    KeypadReloadIcon(size = 20.dp)
                                }

                                KeypadDigitButton(
                                    digit = "9",
                                    onClick = { onDigitClick("9") }
                                )

                                KeypadDigitButton(
                                    digit = "0",
                                    onClick = { onDigitClick("0") }
                                )

                                KeypadIconButton(
                                    testTag = "keypad_backspace",
                                    onClick = onBackspaceClick
                                ) {
                                    KeypadBackspaceTagIcon(size = 22.dp)
                                }
                            }
                        }

                        Spacer(modifier = Modifier.weight(1f))
                        Spacer(modifier = Modifier.height(10.dp))

                        // Bottom "Geser untuk Konfirmasi" Slider Button
                        SwipeToWithdrawButton(
                            onSwipeCompleted = onWithdrawComplete,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 16.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun KeypadDigitButton(
    digit: String,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(64.dp)
            .clip(CircleShape)
            .background(TuaiKeypadCircleBg)
            .clickable(onClick = onClick)
            .testTag("keypad_digit_$digit"),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = digit,
            fontFamily = PlusJakartaSans,
            fontWeight = FontWeight.Medium,
            fontSize = 20.sp,
            color = TuaiTextDark
        )
    }
}

@Composable
private fun KeypadIconButton(
    testTag: String,
    onClick: () -> Unit,
    content: @Composable () -> Unit
) {
    Box(
        modifier = Modifier
            .size(64.dp)
            .clip(CircleShape)
            .background(TuaiKeypadCircleBg)
            .clickable(onClick = onClick)
            .testTag(testTag),
        contentAlignment = Alignment.Center
    ) {
        content()
    }
}

@Composable
private fun SwipeToWithdrawButton(
    onSwipeCompleted: () -> Unit,
    modifier: Modifier = Modifier
) {
    val density = LocalDensity.current
    var dragOffsetPx by remember { mutableFloatStateOf(0f) }

    BoxWithConstraints(
        modifier = modifier
            .height(62.dp)
            .clip(RoundedCornerShape(50))
            .background(TuaiTerracotta)
            .clickable { onSwipeCompleted() }
            .padding(6.dp)
            .testTag("swipe_to_withdraw_button"),
        contentAlignment = Alignment.CenterStart
    ) {
        val thumbSizeDp = 50.dp
        val maxDragPx = with(density) { (maxWidth - thumbSizeDp).toPx().coerceAtLeast(1f) }
        val animatedOffset by animateFloatAsState(
            targetValue = dragOffsetPx,
            animationSpec = tween(durationMillis = 150),
            label = "swipe_offset"
        )

        // Centered Label: "Geser untuk Konfirmasi"
        Text(
            text = stringResource(R.string.swipe_to_withdraw),
            fontFamily = PlusJakartaSans,
            fontWeight = FontWeight.Medium,
            fontSize = 15.sp,
            color = Color.White,
            modifier = Modifier.align(Alignment.Center)
        )

        // Draggable White Circle Thumb with Right Arrow
        Box(
            modifier = Modifier
                .offset { IntOffset(animatedOffset.roundToInt(), 0) }
                .size(thumbSizeDp)
                .clip(CircleShape)
                .background(Color.White)
                .draggable(
                    orientation = Orientation.Horizontal,
                    state = rememberDraggableState { delta ->
                        dragOffsetPx = (dragOffsetPx + delta).coerceIn(0f, maxDragPx)
                    },
                    onDragStopped = {
                        if (dragOffsetPx > maxDragPx * 0.6f) {
                            onSwipeCompleted()
                        }
                        dragOffsetPx = 0f
                    }
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = stringResource(R.string.swipe_to_withdraw),
                tint = TuaiTerracotta,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}
