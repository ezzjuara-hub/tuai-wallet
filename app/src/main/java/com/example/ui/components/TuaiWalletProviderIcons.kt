package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.PlusJakartaSans
import com.example.ui.theme.TuaiBcaBlue
import com.example.ui.theme.TuaiBnbYellow
import com.example.ui.theme.TuaiBrimoBlue
import com.example.ui.theme.TuaiDanaBlue
import com.example.ui.theme.TuaiEthOrange
import com.example.ui.theme.TuaiGopayCyan
import com.example.ui.theme.TuaiMandiriNavy
import com.example.ui.theme.TuaiOvoPurple
import com.example.ui.theme.TuaiSeabankOrange
import com.example.ui.theme.TuaiShopeeOrange
import com.example.ui.theme.TuaiUsdtGreen
import com.example.viewmodel.AssetIconType

fun providerColorFor(iconType: AssetIconType): Color {
    return when (iconType) {
        AssetIconType.BCA -> TuaiBcaBlue
        AssetIconType.MANDIRI -> TuaiMandiriNavy
        AssetIconType.BRIMO -> TuaiBrimoBlue
        AssetIconType.GOPAY -> TuaiGopayCyan
        AssetIconType.DANA -> TuaiDanaBlue
        AssetIconType.OVO -> TuaiOvoPurple
        AssetIconType.SHOPEEPAY -> TuaiShopeeOrange
        AssetIconType.SEABANK -> TuaiSeabankOrange
        AssetIconType.ETHEREUM -> TuaiEthOrange
        AssetIconType.BINANCE -> TuaiBnbYellow
        AssetIconType.TETHER -> TuaiUsdtGreen
    }
}

@Composable
fun WalletProviderBadge(
    iconType: AssetIconType,
    size: Dp = 46.dp,
    modifier: Modifier = Modifier
) {
    val bg = providerColorFor(iconType)
    Box(
        modifier = modifier
            .size(size)
            .clip(CircleShape)
            .background(bg),
        contentAlignment = Alignment.Center
    ) {
        when (iconType) {
            AssetIconType.BCA -> BcaEmblemIcon(size = size * 0.56f)
            AssetIconType.MANDIRI -> MandiriEmblemIcon(size = size * 0.58f)
            AssetIconType.BRIMO -> BrimoEmblemIcon(size = size * 0.56f)
            AssetIconType.GOPAY -> GopayEmblemIcon(size = size * 0.56f)
            AssetIconType.DANA -> DanaEmblemIcon(size = size * 0.56f)
            AssetIconType.OVO -> OvoEmblemIcon(size = size * 0.54f)
            AssetIconType.SHOPEEPAY -> ShopeePayEmblemIcon(size = size * 0.56f)
            AssetIconType.SEABANK -> SeaBankEmblemIcon(size = size * 0.56f)
            AssetIconType.ETHEREUM -> EthereumCrystalIcon(size = size * 0.52f)
            AssetIconType.BINANCE -> BinanceDiamondIcon(size = size * 0.52f)
            AssetIconType.TETHER -> TetherSymbolIcon(size = size * 0.52f)
        }
    }
}

@Composable
fun BcaEmblemIcon(size: Dp = 24.dp) {
    Box(contentAlignment = Alignment.Center, modifier = Modifier.size(size)) {
        Canvas(modifier = Modifier.size(size)) {
            val w = this.size.width
            val h = this.size.height
            // Outer stylized lotus/shield frame
            val shield = Path().apply {
                moveTo(w * 0.50f, h * 0.06f)
                cubicTo(w * 0.86f, h * 0.14f, w * 0.94f, h * 0.56f, w * 0.50f, h * 0.94f)
                cubicTo(w * 0.06f, h * 0.56f, w * 0.14f, h * 0.14f, w * 0.50f, h * 0.06f)
                close()
            }
            drawPath(
                path = shield,
                color = Color.White.copy(alpha = 0.22f)
            )
            drawPath(
                path = shield,
                color = Color.White,
                style = Stroke(width = w * 0.08f)
            )
        }
        Text(
            text = "BCA",
            fontFamily = PlusJakartaSans,
            fontWeight = FontWeight.ExtraBold,
            fontSize = (size.value * 0.36f).sp,
            color = Color.White
        )
    }
}

@Composable
fun MandiriEmblemIcon(size: Dp = 24.dp) {
    Box(contentAlignment = Alignment.Center, modifier = Modifier.size(size)) {
        Canvas(modifier = Modifier.size(size)) {
            val w = this.size.width
            val h = this.size.height
            // Signature gold wave ribbon at top
            val wave = Path().apply {
                moveTo(w * 0.12f, h * 0.34f)
                cubicTo(w * 0.36f, h * 0.10f, w * 0.64f, h * 0.44f, w * 0.88f, h * 0.20f)
                lineTo(w * 0.88f, h * 0.34f)
                cubicTo(w * 0.64f, h * 0.56f, w * 0.36f, h * 0.24f, w * 0.12f, h * 0.46f)
                close()
            }
            drawPath(path = wave, color = Color(0xFFFFB700))
        }
        Text(
            text = "LIVIN",
            fontFamily = PlusJakartaSans,
            fontWeight = FontWeight.ExtraBold,
            fontSize = (size.value * 0.29f).sp,
            color = Color.White,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}

@Composable
fun BrimoEmblemIcon(size: Dp = 24.dp) {
    Box(contentAlignment = Alignment.Center, modifier = Modifier.size(size)) {
        Canvas(modifier = Modifier.size(size)) {
            val w = this.size.width
            val h = this.size.height
            drawRoundRect(
                color = Color.White.copy(alpha = 0.20f),
                topLeft = Offset(w * 0.08f, h * 0.16f),
                size = Size(w * 0.84f, h * 0.68f),
                cornerRadius = CornerRadius(w * 0.18f, w * 0.18f)
            )
            // Small orange BRImo accent dot
            drawCircle(
                color = Color(0xFFF37021),
                radius = w * 0.11f,
                center = Offset(w * 0.80f, h * 0.24f)
            )
        }
        Text(
            text = "BRI",
            fontFamily = PlusJakartaSans,
            fontWeight = FontWeight.ExtraBold,
            fontSize = (size.value * 0.38f).sp,
            color = Color.White
        )
    }
}

@Composable
fun GopayEmblemIcon(size: Dp = 24.dp) {
    Canvas(modifier = Modifier.size(size)) {
        val w = this.size.width
        val h = this.size.height
        val stroke = Stroke(width = w * 0.10f, cap = StrokeCap.Round, join = StrokeJoin.Round)
        // Rounded wallet pouch
        drawRoundRect(
            color = Color.White,
            topLeft = Offset(w * 0.14f, h * 0.22f),
            size = Size(w * 0.72f, h * 0.56f),
            cornerRadius = CornerRadius(w * 0.18f, w * 0.18f),
            style = stroke
        )
        // Inner GoPay coin dot
        drawCircle(
            color = Color.White,
            radius = w * 0.13f,
            center = Offset(w * 0.56f, h * 0.50f)
        )
    }
}

@Composable
fun DanaEmblemIcon(size: Dp = 24.dp) {
    Box(contentAlignment = Alignment.Center, modifier = Modifier.size(size)) {
        Canvas(modifier = Modifier.size(size)) {
            val w = this.size.width
            val h = this.size.height
            // Stylized fluid wave bar
            val wave = Path().apply {
                moveTo(w * 0.12f, h * 0.72f)
                cubicTo(w * 0.35f, h * 0.60f, w * 0.65f, h * 0.84f, w * 0.88f, h * 0.70f)
            }
            drawPath(
                path = wave,
                color = Color.White,
                style = Stroke(width = w * 0.09f, cap = StrokeCap.Round)
            )
        }
        Text(
            text = "DANA",
            fontFamily = PlusJakartaSans,
            fontWeight = FontWeight.ExtraBold,
            fontSize = (size.value * 0.28f).sp,
            color = Color.White
        )
    }
}

@Composable
fun OvoEmblemIcon(size: Dp = 24.dp) {
    Box(contentAlignment = Alignment.Center, modifier = Modifier.size(size)) {
        Text(
            text = "OVO",
            fontFamily = PlusJakartaSans,
            fontWeight = FontWeight.ExtraBold,
            fontSize = (size.value * 0.35f).sp,
            color = Color.White
        )
    }
}

@Composable
fun ShopeePayEmblemIcon(size: Dp = 24.dp) {
    Box(contentAlignment = Alignment.Center, modifier = Modifier.size(size)) {
        Canvas(modifier = Modifier.size(size)) {
            val w = this.size.width
            val h = this.size.height
            // Shopping bag handle arc
            drawArc(
                color = Color.White,
                startAngle = 180f,
                sweepAngle = 180f,
                useCenter = false,
                topLeft = Offset(w * 0.32f, h * 0.08f),
                size = Size(w * 0.36f, h * 0.28f),
                style = Stroke(width = w * 0.08f, cap = StrokeCap.Round)
            )
            drawRoundRect(
                color = Color.White.copy(alpha = 0.22f),
                topLeft = Offset(w * 0.18f, h * 0.24f),
                size = Size(w * 0.64f, h * 0.64f),
                cornerRadius = CornerRadius(w * 0.14f, w * 0.14f)
            )
        }
        Text(
            text = "S",
            fontFamily = PlusJakartaSans,
            fontWeight = FontWeight.ExtraBold,
            fontSize = (size.value * 0.44f).sp,
            color = Color.White
        )
    }
}

@Composable
fun SeaBankEmblemIcon(size: Dp = 24.dp) {
    Box(contentAlignment = Alignment.Center, modifier = Modifier.size(size)) {
        Text(
            text = "SEA",
            fontFamily = PlusJakartaSans,
            fontWeight = FontWeight.ExtraBold,
            fontSize = (size.value * 0.34f).sp,
            color = Color.White
        )
    }
}

@Composable
fun ActionPluginConnectorIcon(
    tint: Color = Color(0xFF281F1A),
    size: Dp = 20.dp
) {
    Canvas(modifier = Modifier.size(size)) {
        val w = this.size.width
        val h = this.size.height
        val stroke = Stroke(width = w * 0.085f, cap = StrokeCap.Round, join = StrokeJoin.Round)
        // Plug body
        drawRoundRect(
            color = tint,
            topLeft = Offset(w * 0.24f, h * 0.36f),
            size = Size(w * 0.52f, h * 0.34f),
            cornerRadius = CornerRadius(w * 0.12f, w * 0.12f),
            style = stroke
        )
        // Left & right prongs
        drawLine(
            color = tint,
            start = Offset(w * 0.38f, h * 0.16f),
            end = Offset(w * 0.38f, h * 0.36f),
            strokeWidth = w * 0.085f,
            cap = StrokeCap.Round
        )
        drawLine(
            color = tint,
            start = Offset(w * 0.62f, h * 0.16f),
            end = Offset(w * 0.62f, h * 0.36f),
            strokeWidth = w * 0.085f,
            cap = StrokeCap.Round
        )
        // Bottom cord stem
        drawLine(
            color = tint,
            start = Offset(w * 0.50f, h * 0.70f),
            end = Offset(w * 0.50f, h * 0.88f),
            strokeWidth = w * 0.085f,
            cap = StrokeCap.Round
        )
    }
}
