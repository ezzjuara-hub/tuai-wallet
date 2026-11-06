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
    fun `verify all three Tuai screens and multi-wallet plugins render and navigate accurately`() {
        composeTestRule.setContent {
            MyApplicationTheme {
                TuaiApp()
            }
        }

        // 1. Verify Screen 1: Login ("Masuk ke Akun")
        composeTestRule.onNodeWithText("Tuai").assertIsDisplayed()
        composeTestRule.onNodeWithText("Konektor Dompet, Bank & Kendali Keuanganmu").assertIsDisplayed()
        composeTestRule.onNodeWithText("Masuk ke Akun").assertIsDisplayed()
        composeTestRule.onNodeWithText("Email atau Nomor HP").assertIsDisplayed()
        composeTestRule.onNodeWithText("Kata Sandi").assertIsDisplayed()

        // Navigate to Screen 2: Connected Wallets Dashboard
        composeTestRule.onNodeWithTag("login_submit_button").performClick()
        composeTestRule.waitForIdle()

        // 2. Verify Screen 2: Aggregated Wallets & Banks (BCA, Mandiri, BRImo, GoPay, DANA)
        composeTestRule.onNodeWithTag("wallet_balance_text").assertIsDisplayed()
        composeTestRule.onNodeWithText("Rp 29.550.000").assertIsDisplayed()
        composeTestRule.onNodeWithText("Dompet & Bank").assertIsDisplayed()
        composeTestRule.onNodeWithText("Mutasi Gabungan").assertIsDisplayed()
        composeTestRule.onNodeWithText("BCA (myBCA)").assertIsDisplayed()
        composeTestRule.onNodeWithText("Livin' by Mandiri").assertIsDisplayed()
        composeTestRule.onNodeWithText("BRImo (Bank BRI)").assertIsDisplayed()
        composeTestRule.onNodeWithText("GoPay").assertIsDisplayed()
        composeTestRule.onNodeWithText("DANA").assertIsDisplayed()

        // Navigate to Screen 3: Catat Keluar / Transfer
        composeTestRule.onNodeWithTag("action_withdraw_button").performClick()
        composeTestRule.waitForIdle()

        // 3. Verify Screen 3: Catat Keluar / Transfer
        composeTestRule.onNodeWithText("Catat Keluar / Transfer").assertIsDisplayed()
        composeTestRule.onNodeWithText("Sumber Dana").assertIsDisplayed()
        composeTestRule.onNodeWithText("Nominal Pengeluaran / Transfer").assertIsDisplayed()
        composeTestRule.onNodeWithText("Rp 150.000").assertIsDisplayed()
        composeTestRule.onNodeWithText("Geser untuk Konfirmasi").assertIsDisplayed()
    }
}
