package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.components.AppleBlackIcon
import com.example.ui.components.FingerprintTerracottaIcon
import com.example.ui.components.GoogleMultiColorIcon
import com.example.ui.components.MailOutlineIcon
import com.example.ui.components.PadlockOutlineIcon
import com.example.ui.components.SlashedEyeIcon
import com.example.ui.components.TuaiWheatEmblem
import com.example.ui.theme.PlusJakartaSans
import com.example.ui.theme.TuaiCreamBg
import com.example.ui.theme.TuaiDividerLine
import com.example.ui.theme.TuaiInputBg
import com.example.ui.theme.TuaiInputHint
import com.example.ui.theme.TuaiInputIcon
import com.example.ui.theme.TuaiSocialBtnBg
import com.example.ui.theme.TuaiSocialBtnBorder
import com.example.ui.theme.TuaiTerracotta
import com.example.ui.theme.TuaiTerracottaDark
import com.example.ui.theme.TuaiTextDark
import com.example.viewmodel.TuaiUiState

@Composable
fun LoginScreen(
    uiState: TuaiUiState,
    onEmailChanged: (String) -> Unit,
    onPasswordChanged: (String) -> Unit,
    onTogglePasswordVisibility: () -> Unit,
    onLoginSuccess: () -> Unit,
    onForgotPasswordClick: () -> Unit,
    onRegisterClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(TuaiCreamBg)
            .windowInsetsPadding(WindowInsets.statusBars)
            .windowInsetsPadding(WindowInsets.navigationBars),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .widthIn(max = 460.dp)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 28.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Top & Middle Content
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(28.dp))

                // Brand Logo Lockup: Wheat Emblem + "Tuai"
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    TuaiWheatEmblem(modifier = Modifier.size(56.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = stringResource(R.string.app_name),
                        fontFamily = PlusJakartaSans,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 42.sp,
                        color = TuaiTerracottaDark,
                        letterSpacing = (-0.8).sp
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = stringResource(R.string.brand_tagline),
                    fontFamily = PlusJakartaSans,
                    fontWeight = FontWeight.Normal,
                    fontSize = 13.5.sp,
                    color = Color(0xFF726960)
                )

                Spacer(modifier = Modifier.height(28.dp))

                // Heading: "Masuk ke Akun"
                Text(
                    text = stringResource(R.string.login_title),
                    fontFamily = PlusJakartaSans,
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp,
                    color = TuaiTextDark
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = stringResource(R.string.login_subtitle),
                    fontFamily = PlusJakartaSans,
                    fontWeight = FontWeight.Normal,
                    fontSize = 13.5.sp,
                    color = Color(0xFF595048)
                )

                Spacer(modifier = Modifier.height(22.dp))

                // Input 1: Email atau Nomor HP
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp)
                        .clip(RoundedCornerShape(50))
                        .background(TuaiInputBg)
                        .padding(horizontal = 22.dp)
                        .testTag("email_or_phone_input"),
                    contentAlignment = Alignment.CenterStart
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        MailOutlineIcon(tint = TuaiInputIcon, size = 20.dp)
                        Spacer(modifier = Modifier.width(14.dp))
                        Box(modifier = Modifier.weight(1f)) {
                            if (uiState.emailOrPhone.isEmpty()) {
                                Text(
                                    text = stringResource(R.string.email_or_phone_hint),
                                    fontFamily = PlusJakartaSans,
                                    fontWeight = FontWeight.Normal,
                                    fontSize = 14.5.sp,
                                    color = TuaiInputHint
                                )
                            }
                            BasicTextField(
                                value = uiState.emailOrPhone,
                                onValueChange = onEmailChanged,
                                singleLine = true,
                                textStyle = TextStyle(
                                    fontFamily = PlusJakartaSans,
                                    fontWeight = FontWeight.Medium,
                                    fontSize = 14.5.sp,
                                    color = TuaiTextDark
                                ),
                                cursorBrush = SolidColor(TuaiTerracotta),
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Email,
                                    imeAction = ImeAction.Next
                                ),
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Input 2: Kata Sandi
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp)
                        .clip(RoundedCornerShape(50))
                        .background(TuaiInputBg)
                        .padding(horizontal = 22.dp)
                        .testTag("password_input"),
                    contentAlignment = Alignment.CenterStart
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        PadlockOutlineIcon(tint = TuaiInputIcon, size = 20.dp)
                        Spacer(modifier = Modifier.width(14.dp))
                        Box(modifier = Modifier.weight(1f)) {
                            if (uiState.password.isEmpty()) {
                                Text(
                                    text = stringResource(R.string.password_hint),
                                    fontFamily = PlusJakartaSans,
                                    fontWeight = FontWeight.Normal,
                                    fontSize = 14.5.sp,
                                    color = TuaiInputHint
                                )
                            }
                            BasicTextField(
                                value = uiState.password,
                                onValueChange = onPasswordChanged,
                                singleLine = true,
                                visualTransformation = if (uiState.isPasswordVisible) {
                                    VisualTransformation.None
                                } else {
                                    PasswordVisualTransformation()
                                },
                                textStyle = TextStyle(
                                    fontFamily = PlusJakartaSans,
                                    fontWeight = FontWeight.Medium,
                                    fontSize = 14.5.sp,
                                    color = TuaiTextDark
                                ),
                                cursorBrush = SolidColor(TuaiTerracotta),
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Password,
                                    imeAction = ImeAction.Done
                                ),
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .clickable(onClick = onTogglePasswordVisibility)
                                .testTag("toggle_password_visibility"),
                            contentAlignment = Alignment.Center
                        ) {
                            SlashedEyeIcon(
                                isVisible = uiState.isPasswordVisible,
                                tint = TuaiInputIcon,
                                size = 22.dp
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Lupa Kata Sandi?
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.CenterEnd
                ) {
                    Text(
                        text = stringResource(R.string.forgot_password),
                        fontFamily = PlusJakartaSans,
                        fontWeight = FontWeight.Medium,
                        fontSize = 13.sp,
                        color = TuaiTerracotta,
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .clickable(onClick = onForgotPasswordClick)
                            .padding(vertical = 4.dp, horizontal = 4.dp)
                            .testTag("forgot_password_button")
                    )
                }

                Spacer(modifier = Modifier.height(18.dp))

                // Primary "Masuk" Pill Button with Right Arrow
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp)
                        .shadow(
                            elevation = 8.dp,
                            shape = RoundedCornerShape(50),
                            ambientColor = TuaiTerracotta.copy(alpha = 0.35f),
                            spotColor = TuaiTerracotta.copy(alpha = 0.35f)
                        )
                        .clip(RoundedCornerShape(50))
                        .background(
                            brush = Brush.horizontalGradient(
                                colors = listOf(Color(0xFFD46237), Color(0xFFC8542B))
                            )
                        )
                        .clickable(onClick = onLoginSuccess)
                        .padding(horizontal = 24.dp)
                        .testTag("login_submit_button"),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(R.string.login_button),
                        fontFamily = PlusJakartaSans,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 16.sp,
                        color = Color.White
                    )
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = stringResource(R.string.login_button),
                        tint = Color.White,
                        modifier = Modifier
                            .align(Alignment.CenterEnd)
                            .size(20.dp)
                    )
                }

                Spacer(modifier = Modifier.height(22.dp))

                // Divider Row: "Atau masuk dengan"
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    HorizontalDivider(
                        modifier = Modifier.weight(1f),
                        thickness = 1.dp,
                        color = TuaiDividerLine
                    )
                    Text(
                        text = stringResource(R.string.or_login_with),
                        fontFamily = PlusJakartaSans,
                        fontWeight = FontWeight.Normal,
                        fontSize = 13.sp,
                        color = Color(0xFF5A5047),
                        modifier = Modifier.padding(horizontal = 14.dp)
                    )
                    HorizontalDivider(
                        modifier = Modifier.weight(1f),
                        thickness = 1.dp,
                        color = TuaiDividerLine
                    )
                }

                Spacer(modifier = Modifier.height(18.dp))

                // Social & Biometric Login Row (Google, Apple, Fingerprint)
                Row(
                    horizontalArrangement = Arrangement.spacedBy(20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    SocialCircleButton(
                        testTag = "google_login_button",
                        onClick = onLoginSuccess
                    ) {
                        GoogleMultiColorIcon(size = 24.dp)
                    }

                    SocialCircleButton(
                        testTag = "apple_login_button",
                        onClick = onLoginSuccess
                    ) {
                        AppleBlackIcon(size = 24.dp)
                    }

                    SocialCircleButton(
                        testTag = "biometric_login_button",
                        onClick = onLoginSuccess
                    ) {
                        FingerprintTerracottaIcon(tint = TuaiTerracotta, size = 25.dp)
                    }
                }
            }

            // Footer: "Belum punya akun? Daftar"
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(top = 18.dp, bottom = 16.dp)
            ) {
                Text(
                    text = buildAnnotatedString {
                        withStyle(
                            style = SpanStyle(
                                fontFamily = PlusJakartaSans,
                                fontWeight = FontWeight.Normal,
                                fontSize = 13.5.sp,
                                color = Color(0xFF3B322C)
                            )
                        ) {
                            append(stringResource(R.string.no_account_prefix))
                        }
                        withStyle(
                            style = SpanStyle(
                                fontFamily = PlusJakartaSans,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 13.5.sp,
                                color = TuaiTerracotta
                            )
                        ) {
                            append(stringResource(R.string.register_action))
                        }
                    },
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .clickable(onClick = onRegisterClick)
                        .padding(horizontal = 8.dp, vertical = 6.dp)
                        .testTag("register_link_button")
                )
            }
        }
    }
}

@Composable
private fun SocialCircleButton(
    testTag: String,
    onClick: () -> Unit,
    content: @Composable () -> Unit
) {
    Surface(
        modifier = Modifier
            .size(56.dp)
            .clip(CircleShape)
            .border(1.dp, TuaiSocialBtnBorder, CircleShape)
            .clickable(onClick = onClick)
            .testTag(testTag),
        shape = CircleShape,
        color = TuaiSocialBtnBg
    ) {
        Box(contentAlignment = Alignment.Center) {
            content()
        }
    }
}
