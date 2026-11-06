package com.example

import android.content.Context
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.core.app.ApplicationProvider
import com.example.ui.theme.MyApplicationTheme
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [35], qualifiers = "w411dp-h891dp-xhdpi")
class ExampleRobolectricTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun `read string from context`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val appName = context.getString(R.string.app_name)
        assertEquals("Tuai", appName)
    }

    @Test
    fun `verify all three Tuai screens render and navigate accurately`() {
        composeTestRule.setContent {
            MyApplicationTheme {
                TuaiApp()
            }
        }

        // 1. Verify Screen 1: Login ("Masuk ke Akun")
        composeTestRule.onNodeWithText("Tuai").assertIsDisplayed()
        composeTestRule.onNodeWithText("Kelola & Tuai Hasil Finansialmu").assertIsDisplayed()
        composeTestRule.onNodeWithText("Masuk ke Akun").assertIsDisplayed()
        composeTestRule.onNodeWithText("Silakan masuk untuk melanjutkan").assertIsDisplayed()
        composeTestRule.onNodeWithText("Email atau Nomor HP").assertIsDisplayed()
        composeTestRule.onNodeWithText("Kata Sandi").assertIsDisplayed()
        composeTestRule.onNodeWithText("Lupa Kata Sandi?").assertIsDisplayed()
        composeTestRule.onNodeWithText("Atau masuk dengan").assertIsDisplayed()

        // Navigate to Screen 2: Wallet
        composeTestRule.onNodeWithTag("login_submit_button").performClick()
        composeTestRule.waitForIdle()

        // 2. Verify Screen 2: Wallet
        composeTestRule.onNodeWithTag("wallet_balance_text").assertIsDisplayed()
        composeTestRule.onNodeWithText("Wallet id: 2678900085").assertIsDisplayed()
        composeTestRule.onNodeWithText("My Assets").assertIsDisplayed()
        composeTestRule.onNodeWithText("My Transaction").assertIsDisplayed()
        composeTestRule.onNodeWithText("Ethereum").assertIsDisplayed()
        composeTestRule.onNodeWithText("28,74% ($28,015)").assertIsDisplayed()
        composeTestRule.onNodeWithText("Binance").assertIsDisplayed()
        composeTestRule.onNodeWithText("107.70 BNB").assertIsDisplayed()
        composeTestRule.onNodeWithText("Tether usd").assertIsDisplayed()
        composeTestRule.onNodeWithText("29.74% ($26,015)").assertIsDisplayed()

        // Navigate to Screen 3: Withdraw Money
        composeTestRule.onNodeWithTag("action_withdraw_button").performClick()
        composeTestRule.waitForIdle()

        // 3. Verify Screen 3: Withdraw Money
        composeTestRule.onNodeWithText("Withdraw Money").assertIsDisplayed()
        composeTestRule.onNodeWithText("Exchange").assertIsDisplayed()
        composeTestRule.onNodeWithText("Current Wallet Balance").assertIsDisplayed()
        composeTestRule.onNodeWithText("Withdraw Amount").assertIsDisplayed()
        composeTestRule.onNodeWithText("$19,29.00").assertIsDisplayed()
        composeTestRule.onNodeWithText("Swipe to Withdraw").assertIsDisplayed()
    }
}
