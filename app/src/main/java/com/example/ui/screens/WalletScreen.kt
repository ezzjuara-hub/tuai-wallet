package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.components.ActionArrowDownLeftIcon
import com.example.ui.components.ActionArrowUpRightIcon
import com.example.ui.components.ActionPlusIcon
import com.example.ui.components.ActionSwapCircularIcon
import com.example.ui.components.BinanceDiamondIcon
import com.example.ui.components.CopyWalletIdIcon
import com.example.ui.components.EthereumCrystalIcon
import com.example.ui.components.NavHomeSmileIcon
import com.example.ui.components.NavPieChartIcon
import com.example.ui.components.NavSettingsCogIcon
import com.example.ui.components.NavWalletFilledIcon
import com.example.ui.components.NotificationBellWithDotIcon
import com.example.ui.components.TetherSymbolIcon
import com.example.ui.theme.PlusJakartaSans
import com.example.ui.theme.TuaiBalanceCream
import com.example.ui.theme.TuaiBellCircleBg
import com.example.ui.theme.TuaiBnbYellow
import com.example.ui.theme.TuaiEthOrange
import com.example.ui.theme.TuaiGoldAccent
import com.example.ui.theme.TuaiTerracotta
import com.example.ui.theme.TuaiTextDark
import com.example.ui.theme.TuaiTextMuted
import com.example.ui.theme.TuaiUsdtGreen
import com.example.ui.theme.TuaiWalletIdText
import com.example.viewmodel.AssetIconType
import com.example.viewmodel.CryptoAssetItem
import com.example.viewmodel.TuaiUiState
import com.example.viewmodel.WalletTransactionItem

@Composable
fun WalletScreen(
    uiState: TuaiUiState,
    onBackToLogin: () -> Unit,
    onNavigateToWithdraw: () -> Unit,
    onSelectAssetTab: (Int) -> Unit,
    onSelectBottomNav: (Int) -> Unit,
    onShowMessage: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler(onBack = onBackToLogin)
    val clipboardManager = LocalClipboardManager.current

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
                Spacer(modifier = Modifier.height(14.dp))

                // Top Bar: "Wallet" + Notification Bell + Profile Avatar
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(R.string.wallet_title),
                        fontFamily = PlusJakartaSans,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 20.sp,
                        color = Color.White
                    )

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Cream Notification Bell Button
                        Box(
                            modifier = Modifier
                                .size(42.dp)
                                .clip(CircleShape)
                                .background(TuaiBellCircleBg)
                                .clickable { onShowMessage("Tidak ada notifikasi baru") }
                                .testTag("wallet_notification_button"),
                            contentAlignment = Alignment.Center
                        ) {
                            NotificationBellWithDotIcon(
                                tint = Color(0xFF2B211B),
                                size = 20.dp
                            )
                        }

                        // Circular User Profile Photo (extracted from reference image)
                        Box(
                            modifier = Modifier
                                .size(42.dp)
                                .clip(CircleShape)
                                .background(TuaiBellCircleBg)
                                .clickable(onClick = onBackToLogin)
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

                Spacer(modifier = Modifier.height(32.dp))

                // Large Balance Text: "$23,867"
                Text(
                    text = uiState.walletBalance,
                    fontFamily = PlusJakartaSans,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 50.sp,
                    color = TuaiBalanceCream,
                    letterSpacing = (-1).sp,
                    modifier = Modifier.testTag("wallet_balance_text")
                )

                Spacer(modifier = Modifier.height(6.dp))

                // Wallet ID + Copy Icon Row
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .clickable {
                            clipboardManager.setText(AnnotatedString(uiState.walletId))
                            onShowMessage("Wallet ID ${uiState.walletId} disalin")
                        }
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                        .testTag("copy_wallet_id_button")
                ) {
                    Text(
                        text = "Wallet id: ${uiState.walletId}",
                        fontFamily = PlusJakartaSans,
                        fontWeight = FontWeight.Normal,
                        fontSize = 12.5.sp,
                        color = TuaiWalletIdText
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    CopyWalletIdIcon(
                        tint = TuaiWalletIdText,
                        size = 14.dp
                    )
                }

                Spacer(modifier = Modifier.height(26.dp))

                // 4 Circular Golden Action Buttons: Receive (↙), Withdraw (↗), Swap (⇄), Add (+)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    WalletActionCircleButton(
                        testTag = "action_receive_button",
                        onClick = { onShowMessage("Wallet ID siap menerima aset") }
                    ) {
                        ActionArrowDownLeftIcon(size = 20.dp)
                    }

                    WalletActionCircleButton(
                        testTag = "action_withdraw_button",
                        onClick = onNavigateToWithdraw
                    ) {
                        ActionArrowUpRightIcon(size = 20.dp)
                    }

                    WalletActionCircleButton(
                        testTag = "action_swap_button",
                        onClick = onNavigateToWithdraw
                    ) {
                        ActionSwapCircularIcon(size = 22.dp)
                    }

                    WalletActionCircleButton(
                        testTag = "action_add_button",
                        onClick = onNavigateToWithdraw
                    ) {
                        ActionPlusIcon(size = 20.dp)
                    }
                }

                Spacer(modifier = Modifier.height(26.dp))
            }

            // Custom Asymmetric Folder-Tab Sheet + White Content Area
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                // Custom Canvas drawing the exact S-curved folder tab & white card body
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val w = size.width
                    val h = size.height
                    val tabHeight = 50.dp.toPx()
                    val topCorner = 26.dp.toPx()

                    // Darker recessed background tab on the right ("My Transaction" background)
                    drawRoundRect(
                        color = Color(0xFFC2512A),
                        topLeft = Offset(w * 0.36f, 4.dp.toPx()),
                        size = Size(w * 0.64f, tabHeight + 24.dp.toPx()),
                        cornerRadius = CornerRadius(topCorner, topCorner)
                    )

                    // White Folder-Tab Sheet Path matching the middle phone in reference image
                    val sheetPath = Path().apply {
                        // Start at bottom-left
                        moveTo(0f, h)
                        // Up to top-left of "My Assets" tab
                        lineTo(0f, topCorner)
                        arcTo(
                            rect = Rect(0f, 0f, topCorner * 2f, topCorner * 2f),
                            startAngleDegrees = 180f,
                            sweepAngleDegrees = 90f,
                            forceMoveTo = false
                        )
                        // Top horizontal edge of "My Assets" tab
                        lineTo(w * 0.36f, 0f)
                        // Smooth S-curve sloping down to the right tab baseline
                        cubicTo(
                            w * 0.44f, 0f,
                            w * 0.44f, tabHeight,
                            w * 0.53f, tabHeight
                        )
                        // Horizontal baseline across "My Transaction" to right edge corner
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
                        // Down to bottom-right
                        lineTo(w, h)
                        close()
                    }
                    drawPath(path = sheetPath, color = Color.White)
                }

                Column(modifier = Modifier.fillMaxSize()) {
                    // Tab Labels Row ("My Assets" | "My Transaction")
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
                                    onClick = { onSelectAssetTab(0) }
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
                                    onClick = { onSelectAssetTab(1) }
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

                    Spacer(modifier = Modifier.height(8.dp))

                    // List Content inside the White Surface
                    if (uiState.selectedAssetTab == 0) {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f)
                                .padding(horizontal = 24.dp)
                        ) {
                            itemsIndexed(
                                items = uiState.assets,
                                key = { _, item -> item.id }
                            ) { index, asset ->
                                AssetListRow(
                                    asset = asset,
                                    onClick = onNavigateToWithdraw
                                )
                                if (index < uiState.assets.lastIndex) {
                                    HorizontalDivider(
                                        thickness = 1.dp,
                                        color = Color(0xFFF5F1EC)
                                    )
                                }
                            }
                        }
                    } else {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f)
                                .padding(horizontal = 24.dp)
                        ) {
                            itemsIndexed(
                                items = uiState.transactions,
                                key = { _, tx -> tx.id }
                            ) { index, tx ->
                                TransactionListRow(
                                    tx = tx,
                                    onClick = onNavigateToWithdraw
                                )
                                if (index < uiState.transactions.lastIndex) {
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

            // Bottom Navigation Bar (Home, Wallet, Chart, Settings)
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
                            textColor = Color(0xFFB86B43),
                            fontWeight = FontWeight.Normal,
                            testTag = "nav_home_tab",
                            onClick = { onSelectBottomNav(0) }
                        ) {
                            NavHomeSmileIcon(tint = Color(0xFFB86B43), size = 22.dp)
                        }

                        BottomNavItem(
                            label = stringResource(R.string.nav_wallet),
                            textColor = TuaiTerracotta,
                            fontWeight = FontWeight.Medium,
                            testTag = "nav_wallet_tab",
                            onClick = { onSelectBottomNav(1) }
                        ) {
                            NavWalletFilledIcon(tint = TuaiTerracotta, size = 22.dp)
                        }

                        BottomNavItem(
                            label = stringResource(R.string.nav_chart),
                            textColor = Color(0xFF2B2522),
                            fontWeight = FontWeight.Normal,
                            testTag = "nav_chart_tab",
                            onClick = {
                                onSelectBottomNav(2)
                                onNavigateToWithdraw()
                            }
                        ) {
                            NavPieChartIcon(tint = Color(0xFF2B2522), size = 22.dp)
                        }

                        BottomNavItem(
                            label = stringResource(R.string.nav_settings),
                            textColor = Color(0xFF2B2522),
                            fontWeight = FontWeight.Normal,
                            testTag = "nav_settings_tab",
                            onClick = {
                                onSelectBottomNav(3)
                                onBackToLogin()
                            }
                        ) {
                            NavSettingsCogIcon(tint = Color(0xFF2B2522), size = 22.dp)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun WalletActionCircleButton(
    testTag: String,
    onClick: () -> Unit,
    content: @Composable () -> Unit
) {
    Box(
        modifier = Modifier
            .size(60.dp)
            .clip(CircleShape)
            .background(TuaiGoldAccent)
            .clickable(onClick = onClick)
            .testTag(testTag),
        contentAlignment = Alignment.Center
    ) {
        content()
    }
}

@Composable
private fun AssetListRow(
    asset: CryptoAssetItem,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 15.dp)
            .testTag("asset_row_${asset.id}"),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Colored Circle + Crypto Vector Emblem
        val circleBg = when (asset.iconType) {
            AssetIconType.ETHEREUM -> TuaiEthOrange
            AssetIconType.BINANCE -> TuaiBnbYellow
            AssetIconType.TETHER -> TuaiUsdtGreen
        }
        Box(
            modifier = Modifier
                .size(46.dp)
                .clip(CircleShape)
                .background(circleBg),
            contentAlignment = Alignment.Center
        ) {
            when (asset.iconType) {
                AssetIconType.ETHEREUM -> EthereumCrystalIcon(size = 24.dp)
                AssetIconType.BINANCE -> BinanceDiamondIcon(size = 24.dp)
                AssetIconType.TETHER -> TetherSymbolIcon(size = 24.dp)
            }
        }

        Spacer(modifier = Modifier.width(14.dp))

        // Asset Name & Percentage
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
                fontSize = 12.5.sp,
                color = TuaiTextMuted
            )
        }

        // Token Amount & USD Value
        Column(horizontalAlignment = Alignment.End) {
            Text(
                text = asset.tokenAmount,
                fontFamily = PlusJakartaSans,
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.5.sp,
                color = TuaiTextDark
            )
            Spacer(modifier = Modifier.height(3.dp))
            Text(
                text = asset.totalUsdValue,
                fontFamily = PlusJakartaSans,
                fontWeight = FontWeight.Normal,
                fontSize = 12.5.sp,
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
            .padding(vertical = 15.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        val circleBg = when (tx.iconType) {
            AssetIconType.ETHEREUM -> TuaiEthOrange
            AssetIconType.BINANCE -> TuaiBnbYellow
            AssetIconType.TETHER -> TuaiUsdtGreen
        }
        Box(
            modifier = Modifier
                .size(46.dp)
                .clip(CircleShape)
                .background(circleBg),
            contentAlignment = Alignment.Center
        ) {
            when (tx.iconType) {
                AssetIconType.ETHEREUM -> EthereumCrystalIcon(size = 24.dp)
                AssetIconType.BINANCE -> BinanceDiamondIcon(size = 24.dp)
                AssetIconType.TETHER -> TetherSymbolIcon(size = 24.dp)
            }
        }

        Spacer(modifier = Modifier.width(14.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = tx.title,
                fontFamily = PlusJakartaSans,
                fontWeight = FontWeight.SemiBold,
                fontSize = 15.sp,
                color = TuaiTextDark
            )
            Spacer(modifier = Modifier.height(3.dp))
            Text(
                text = tx.date,
                fontFamily = PlusJakartaSans,
                fontWeight = FontWeight.Normal,
                fontSize = 12.5.sp,
                color = TuaiTextMuted
            )
        }

        Text(
            text = tx.amount,
            fontFamily = PlusJakartaSans,
            fontWeight = FontWeight.SemiBold,
            fontSize = 14.5.sp,
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
