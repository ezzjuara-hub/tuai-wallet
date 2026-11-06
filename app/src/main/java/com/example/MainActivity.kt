package com.example

import android.content.ClipData
import android.content.ClipboardManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.components.TuaiModalOverlayHost
import com.example.ui.screens.LoginScreen
import com.example.ui.screens.WalletScreen
import com.example.ui.screens.WithdrawScreen
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.PlusJakartaSans
import com.example.ui.theme.TuaiTextDark
import com.example.viewmodel.TuaiActiveModal
import com.example.viewmodel.TuaiScreen
import com.example.viewmodel.TuaiUiState
import com.example.viewmodel.TuaiViewModel
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.auto(
                android.graphics.Color.TRANSPARENT,
                android.graphics.Color.TRANSPARENT
            ),
            navigationBarStyle = SystemBarStyle.light(
                Color.White.toArgb(),
                Color.Black.toArgb()
            )
        )
        setContent {
            MyApplicationTheme {
                TuaiApp()
            }
        }
    }
}

@Composable
fun TuaiApp(viewModel: TuaiViewModel = viewModel()) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.attachContext(context)
    }

    LaunchedEffect(uiState.toastMessage) {
        if (uiState.toastMessage != null) {
            delay(2400)
            viewModel.clearMessage()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Crossfade(
            targetState = uiState.currentScreen,
            animationSpec = tween(durationMillis = 220),
            label = "screen_transition"
        ) { screen ->
            when (screen) {
                TuaiScreen.LOGIN -> {
                    LoginScreen(
                        uiState = uiState,
                        onEmailChanged = viewModel::updateEmailOrPhone,
                        onPasswordChanged = viewModel::updatePassword,
                        onTogglePasswordVisibility = viewModel::togglePasswordVisibility,
                        onLoginSuccess = viewModel::submitAuth,
                        onForgotPasswordClick = {
                            viewModel.openModal(TuaiActiveModal.FORGOT_PASSWORD)
                        },
                        onRegisterClick = viewModel::toggleRegisterMode,
                        onNameChanged = viewModel::updateRegisterName,
                        onConfirmPasswordChanged = viewModel::updateConfirmPassword,
                        onGoogleLoginClick = { viewModel.loginWithSocial("Google") },
                        onAppleLoginClick = { viewModel.loginWithSocial("Apple ID") },
                        onBiometricLoginClick = {
                            viewModel.openModal(TuaiActiveModal.BIOMETRIC_PROMPT)
                        }
                    )
                }

                TuaiScreen.WALLET -> {
                    WalletScreen(
                        uiState = uiState,
                        onBackToLogin = viewModel::logout,
                        onNavigateToWithdraw = { viewModel.navigateTo(TuaiScreen.WITHDRAW) },
                        onSelectAssetTab = viewModel::selectAssetTab,
                        onSelectBottomNav = viewModel::selectBottomNav,
                        onShowMessage = viewModel::showMessage,
                        onOpenModal = viewModel::openModal,
                        onAssetClick = viewModel::openAssetDetail,
                        onTransactionClick = viewModel::openTransactionDetail,
                        onSelectTxFilter = viewModel::selectTxFilter,
                        onSelectWalletCategoryFilter = viewModel::selectWalletCategoryFilter,
                        onSelectChartTimeframe = viewModel::selectChartTimeframe,
                        onContributeGoal = viewModel::contributeToSavingsGoal,
                        onTogglePlugin = viewModel::togglePluginConnection,
                        onTogglePrivacy = viewModel::toggleBalancePrivacy,
                        onToggleBiometric = viewModel::toggleBiometricSetting,
                        onToggleNotifications = viewModel::toggleNotificationsSetting,
                        onSyncFirebase = viewModel::syncFirebaseManual,
                        onResetData = viewModel::resetDemoData
                    )
                }

                TuaiScreen.WITHDRAW -> {
                    WithdrawScreen(
                        uiState = uiState,
                        onBackToWallet = { viewModel.navigateTo(TuaiScreen.WALLET) },
                        onToggleStar = viewModel::toggleWithdrawStar,
                        onDigitClick = viewModel::onKeypadDigit,
                        onResetClick = viewModel::onKeypadReset,
                        onBackspaceClick = viewModel::onKeypadBackspace,
                        onWithdrawComplete = viewModel::completeWithdraw,
                        onExchangeCardClick = {
                            viewModel.openModal(TuaiActiveModal.WITHDRAW_EXCHANGE_PICKER)
                        },
                        onPlusPresetClick = viewModel::cycleQuickWithdrawAmount,
                        onSelectWithdrawDestination = viewModel::updateWithdrawDestination
                    )
                }
            }
        }

        // Interactive Modal Bottom Sheet Overlay
        TuaiModalOverlayHost(
            uiState = uiState,
            viewModel = viewModel,
            onCopyText = { text, message ->
                runCatching {
                    val cm = context.getSystemService(ClipboardManager::class.java)
                    cm?.setPrimaryClip(ClipData.newPlainText("Tuai Wallet ID", text))
                }
                viewModel.showMessage(message)
            }
        )

        // Subtle Floating Feedback Banner when an action is triggered
        uiState.toastMessage?.let { message ->
            Surface(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .windowInsetsPadding(WindowInsets.statusBars)
                    .padding(top = 12.dp, start = 24.dp, end = 24.dp),
                shape = RoundedCornerShape(50),
                color = TuaiTextDark.copy(alpha = 0.94f),
                shadowElevation = 6.dp
            ) {
                Text(
                    text = message,
                    fontFamily = PlusJakartaSans,
                    fontWeight = FontWeight.Medium,
                    fontSize = 13.sp,
                    color = Color.White,
                    modifier = Modifier.padding(horizontal = 18.dp, vertical = 10.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 390, heightDp = 844)
@Composable
fun LoginScreenPreview() {
    MyApplicationTheme {
        LoginScreen(
            uiState = TuaiUiState(currentScreen = TuaiScreen.LOGIN),
            onEmailChanged = {},
            onPasswordChanged = {},
            onTogglePasswordVisibility = {},
            onLoginSuccess = {},
            onForgotPasswordClick = {},
            onRegisterClick = {}
        )
    }
}

@Preview(showBackground = true, widthDp = 390, heightDp = 844)
@Composable
fun WalletScreenPreview() {
    MyApplicationTheme {
        WalletScreen(
            uiState = TuaiUiState(currentScreen = TuaiScreen.WALLET),
            onBackToLogin = {},
            onNavigateToWithdraw = {},
            onSelectAssetTab = {},
            onSelectBottomNav = {},
            onShowMessage = {}
        )
    }
}

@Preview(showBackground = true, widthDp = 390, heightDp = 844)
@Composable
fun WithdrawScreenPreview() {
    MyApplicationTheme {
        WithdrawScreen(
            uiState = TuaiUiState(currentScreen = TuaiScreen.WITHDRAW),
            onBackToWallet = {},
            onToggleStar = {},
            onDigitClick = {},
            onResetClick = {},
            onBackspaceClick = {},
            onWithdrawComplete = {}
        )
    }
}
