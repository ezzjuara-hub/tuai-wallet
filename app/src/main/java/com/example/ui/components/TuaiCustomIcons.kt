package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Exact 5-leaf wheat sprout emblem from the "Tuai" brand header:
 * - Top vertical leaf in warm gold gradient
 * - Middle pair of angled leaves in amber-gold gradient
 * - Lower pair of curved leaves in terracotta (#C6542B) with warm peach inner fill (#F9DEC1)
 *   and the signature downward pointed stem on the right leaf.
 */
@Composable
fun TuaiWheatEmblem(modifier: Modifier = Modifier.size(58.dp)) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height

        // 1. Top center vertical leaf (gold)
        val topLeaf = Path().apply {
            moveTo(w * 0.50f, h * 0.03f)
            cubicTo(w * 0.65f, h * 0.16f, w * 0.65f, h * 0.34f, w * 0.50f, h * 0.45f)
            cubicTo(w * 0.35f, h * 0.34f, w * 0.35f, h * 0.16f, w * 0.50f, h * 0.03f)
            close()
        }
        drawPath(
            path = topLeaf,
            brush = Brush.verticalGradient(
                colors = listOf(Color(0xFFEAB65D), Color(0xFFD48F38)),
                startY = 0f,
                endY = h * 0.45f
            )
        )

        // 2. Upper-left angled leaf (amber-gold)
        val midLeftLeaf = Path().apply {
            moveTo(w * 0.14f, h * 0.27f)
            cubicTo(w * 0.34f, h * 0.28f, w * 0.46f, h * 0.39f, w * 0.46f, h * 0.55f)
            cubicTo(w * 0.27f, h * 0.53f, w * 0.14f, h * 0.42f, w * 0.14f, h * 0.27f)
            close()
        }
        drawPath(
            path = midLeftLeaf,
            brush = Brush.linearGradient(
                colors = listOf(Color(0xFFE09C47), Color(0xFFCB792F)),
                start = Offset(w * 0.14f, h * 0.27f),
                end = Offset(w * 0.46f, h * 0.55f)
            )
        )

        // 3. Upper-right angled leaf (amber-gold)
        val midRightLeaf = Path().apply {
            moveTo(w * 0.86f, h * 0.27f)
            cubicTo(w * 0.66f, h * 0.28f, w * 0.54f, h * 0.39f, w * 0.54f, h * 0.55f)
            cubicTo(w * 0.73f, h * 0.53f, w * 0.86f, h * 0.42f, w * 0.86f, h * 0.27f)
            close()
        }
        drawPath(
            path = midRightLeaf,
            brush = Brush.linearGradient(
                colors = listOf(Color(0xFFE09C47), Color(0xFFCB792F)),
                start = Offset(w * 0.86f, h * 0.27f),
                end = Offset(w * 0.54f, h * 0.55f)
            )
        )

        // 4. Bottom-left outer terracotta petal
        val bottomLeftOuter = Path().apply {
            moveTo(w * 0.08f, h * 0.43f)
            cubicTo(w * 0.26f, h * 0.51f, w * 0.46f, h * 0.62f, w * 0.46f, h * 0.89f)
            cubicTo(w * 0.20f, h * 0.83f, w * 0.05f, h * 0.66f, w * 0.08f, h * 0.43f)
            close()
        }
        drawPath(path = bottomLeftOuter, color = Color(0xFFC8562C))

        // Bottom-left inner peach fill
        val bottomLeftInner = Path().apply {
            moveTo(w * 0.15f, h * 0.54f)
            cubicTo(w * 0.28f, h * 0.59f, w * 0.39f, h * 0.67f, w * 0.40f, h * 0.82f)
            cubicTo(w * 0.24f, h * 0.77f, w * 0.14f, h * 0.66f, w * 0.15f, h * 0.54f)
            close()
        }
        drawPath(path = bottomLeftInner, color = Color(0xFFF9DEC0))

        // 5. Bottom-right outer terracotta petal with downward stem point
        val bottomRightOuter = Path().apply {
            moveTo(w * 0.92f, h * 0.43f)
            cubicTo(w * 0.74f, h * 0.51f, w * 0.52f, h * 0.62f, w * 0.48f, h * 0.97f)
            lineTo(w * 0.56f, h * 0.89f)
            cubicTo(w * 0.80f, h * 0.83f, w * 0.95f, h * 0.66f, w * 0.92f, h * 0.43f)
            close()
        }
        drawPath(path = bottomRightOuter, color = Color(0xFFC8562C))

        // Also fill the smooth outer curve of the bottom-right petal
        val bottomRightCurve = Path().apply {
            moveTo(w * 0.92f, h * 0.43f)
            cubicTo(w * 0.74f, h * 0.51f, w * 0.50f, h * 0.63f, w * 0.48f, h * 0.97f)
            lineTo(w * 0.55f, h * 0.90f)
            cubicTo(w * 0.80f, h * 0.84f, w * 0.95f, h * 0.66f, w * 0.92f, h * 0.43f)
            close()
        }
        drawPath(path = bottomRightCurve, color = Color(0xFFC8562C))

        // Bottom-right inner peach fill
        val bottomRightInner = Path().apply {
            moveTo(w * 0.85f, h * 0.54f)
            cubicTo(w * 0.72f, h * 0.59f, w * 0.61f, h * 0.67f, w * 0.60f, h * 0.82f)
            cubicTo(w * 0.76f, h * 0.77f, w * 0.86f, h * 0.66f, w * 0.85f, h * 0.54f)
            close()
        }
        drawPath(path = bottomRightInner, color = Color(0xFFF9DEC0))
    }
}

@Composable
fun MailOutlineIcon(
    tint: Color = Color(0xFF6B5E54),
    size: Dp = 20.dp
) {
    Canvas(modifier = Modifier.size(size)) {
        val w = this.size.width
        val h = this.size.height
        val stroke = Stroke(width = w * 0.08f, cap = StrokeCap.Round, join = StrokeJoin.Round)
        drawRoundRect(
            color = tint,
            topLeft = Offset(w * 0.1f, h * 0.2f),
            size = Size(w * 0.8f, h * 0.6f),
            cornerRadius = CornerRadius(w * 0.09f, w * 0.09f),
            style = stroke
        )
        val flap = Path().apply {
            moveTo(w * 0.14f, h * 0.24f)
            lineTo(w * 0.5f, h * 0.53f)
            lineTo(w * 0.86f, h * 0.24f)
        }
        drawPath(path = flap, color = tint, style = stroke)
    }
}

@Composable
fun PadlockOutlineIcon(
    tint: Color = Color(0xFF6B5E54),
    size: Dp = 20.dp
) {
    Canvas(modifier = Modifier.size(size)) {
        val w = this.size.width
        val h = this.size.height
        val stroke = Stroke(width = w * 0.08f, cap = StrokeCap.Round, join = StrokeJoin.Round)
        // Lock body
        drawRoundRect(
            color = tint,
            topLeft = Offset(w * 0.18f, h * 0.44f),
            size = Size(w * 0.64f, h * 0.44f),
            cornerRadius = CornerRadius(w * 0.1f, w * 0.1f),
            style = stroke
        )
        // Shackle arch
        val shackle = Path().apply {
            moveTo(w * 0.31f, h * 0.44f)
            lineTo(w * 0.31f, h * 0.30f)
            arcTo(
                rect = Rect(w * 0.31f, h * 0.12f, w * 0.69f, h * 0.46f),
                startAngleDegrees = 180f,
                sweepAngleDegrees = 180f,
                forceMoveTo = false
            )
            lineTo(w * 0.69f, h * 0.44f)
        }
        drawPath(path = shackle, color = tint, style = stroke)
    }
}

@Composable
fun SlashedEyeIcon(
    isVisible: Boolean,
    tint: Color = Color(0xFF6B5E54),
    size: Dp = 22.dp
) {
    Canvas(modifier = Modifier.size(size)) {
        val w = this.size.width
        val h = this.size.height
        val stroke = Stroke(width = w * 0.075f, cap = StrokeCap.Round, join = StrokeJoin.Round)

        val eyePath = Path().apply {
            moveTo(w * 0.10f, h * 0.52f)
            cubicTo(w * 0.28f, h * 0.24f, w * 0.72f, h * 0.24f, w * 0.90f, h * 0.52f)
            cubicTo(w * 0.72f, h * 0.80f, w * 0.28f, h * 0.80f, w * 0.10f, h * 0.52f)
            close()
        }
        drawPath(path = eyePath, color = tint, style = stroke)

        drawCircle(
            color = tint,
            radius = w * 0.13f,
            center = Offset(w * 0.50f, h * 0.52f),
            style = stroke
        )

        if (!isVisible) {
            // Diagonal slash + small upper eyelashes like in the mockup
            drawLine(
                color = tint,
                start = Offset(w * 0.18f, h * 0.20f),
                end = Offset(w * 0.82f, h * 0.84f),
                strokeWidth = w * 0.075f,
                cap = StrokeCap.Round
            )
            // Small eyelash ticks along top right
            drawLine(
                color = tint,
                start = Offset(w * 0.62f, h * 0.28f),
                end = Offset(w * 0.67f, h * 0.19f),
                strokeWidth = w * 0.065f,
                cap = StrokeCap.Round
            )
            drawLine(
                color = tint,
                start = Offset(w * 0.77f, h * 0.35f),
                end = Offset(w * 0.84f, h * 0.27f),
                strokeWidth = w * 0.065f,
                cap = StrokeCap.Round
            )
        }
    }
}

@Composable
fun GoogleMultiColorIcon(size: Dp = 24.dp) {
    Canvas(modifier = Modifier.size(size)) {
        val w = this.size.width
        val h = this.size.height
        val strokeWidth = w * 0.19f
        val arcRect = Rect(
            left = strokeWidth * 0.65f,
            top = strokeWidth * 0.65f,
            right = w - strokeWidth * 0.65f,
            bottom = h - strokeWidth * 0.65f
        )
        val stroke = Stroke(width = strokeWidth, cap = StrokeCap.Butt)

        // Red top-left arc
        drawArc(
            color = Color(0xFFEA4335),
            startAngle = 215f,
            sweepAngle = 100f,
            useCenter = false,
            topLeft = arcRect.topLeft,
            size = arcRect.size,
            style = stroke
        )
        // Yellow left arc
        drawArc(
            color = Color(0xFFFBBC05),
            startAngle = 145f,
            sweepAngle = 70f,
            useCenter = false,
            topLeft = arcRect.topLeft,
            size = arcRect.size,
            style = stroke
        )
        // Green bottom arc
        drawArc(
            color = Color(0xFF34A853),
            startAngle = 45f,
            sweepAngle = 100f,
            useCenter = false,
            topLeft = arcRect.topLeft,
            size = arcRect.size,
            style = stroke
        )
        // Blue bottom-right arc
        drawArc(
            color = Color(0xFF4285F4),
            startAngle = -12f,
            sweepAngle = 60f,
            useCenter = false,
            topLeft = arcRect.topLeft,
            size = arcRect.size,
            style = stroke
        )
        // Horizontal blue bar of 'G'
        drawRect(
            color = Color(0xFF4285F4),
            topLeft = Offset(w * 0.50f, h * 0.42f),
            size = Size(w * 0.42f, strokeWidth * 0.92f)
        )
    }
}

@Composable
fun AppleBlackIcon(size: Dp = 24.dp) {
    Canvas(modifier = Modifier.size(size)) {
        val w = this.size.width
        val h = this.size.height

        // Apple body silhouette
        val body = Path().apply {
            moveTo(w * 0.50f, h * 0.30f)
            cubicTo(w * 0.42f, h * 0.30f, w * 0.34f, h * 0.25f, w * 0.26f, h * 0.28f)
            cubicTo(w * 0.13f, h * 0.32f, w * 0.08f, h * 0.50f, w * 0.16f, h * 0.69f)
            cubicTo(w * 0.21f, h * 0.81f, w * 0.29f, h * 0.92f, w * 0.37f, h * 0.92f)
            cubicTo(w * 0.43f, h * 0.92f, w * 0.46f, h * 0.88f, w * 0.51f, h * 0.88f)
            cubicTo(w * 0.56f, h * 0.88f, w * 0.59f, h * 0.92f, w * 0.65f, h * 0.92f)
            cubicTo(w * 0.73f, h * 0.92f, w * 0.80f, h * 0.82f, w * 0.85f, h * 0.71f)
            cubicTo(w * 0.73f, h * 0.65f, w * 0.72f, h * 0.46f, w * 0.84f, h * 0.38f)
            cubicTo(w * 0.78f, h * 0.29f, w * 0.69f, h * 0.26f, w * 0.61f, h * 0.27f)
            cubicTo(w * 0.55f, h * 0.28f, w * 0.53f, h * 0.30f, w * 0.50f, h * 0.30f)
            close()
        }
        drawPath(path = body, color = Color(0xFF191615))

        // Top leaf
        val leaf = Path().apply {
            moveTo(w * 0.50f, h * 0.25f)
            cubicTo(w * 0.50f, h * 0.15f, w * 0.58f, h * 0.07f, w * 0.67f, h * 0.06f)
            cubicTo(w * 0.68f, h * 0.16f, w * 0.60f, h * 0.24f, w * 0.50f, h * 0.25f)
            close()
        }
        drawPath(path = leaf, color = Color(0xFF191615))
    }
}

@Composable
fun FingerprintTerracottaIcon(
    tint: Color = Color(0xFFC85A32),
    size: Dp = 25.dp
) {
    Canvas(modifier = Modifier.size(size)) {
        val w = this.size.width
        val h = this.size.height
        val stroke = Stroke(width = w * 0.07f, cap = StrokeCap.Round)

        // Outer arch
        drawArc(
            color = tint,
            startAngle = 195f,
            sweepAngle = 150f,
            useCenter = false,
            topLeft = Offset(w * 0.10f, h * 0.08f),
            size = Size(w * 0.80f, h * 0.80f),
            style = stroke
        )
        // Second ridge
        drawArc(
            color = tint,
            startAngle = 180f,
            sweepAngle = 180f,
            useCenter = false,
            topLeft = Offset(w * 0.20f, h * 0.20f),
            size = Size(w * 0.60f, h * 0.65f),
            style = stroke
        )
        // Third ridge
        drawArc(
            color = tint,
            startAngle = 175f,
            sweepAngle = 190f,
            useCenter = false,
            topLeft = Offset(w * 0.31f, h * 0.32f),
            size = Size(w * 0.38f, h * 0.50f),
            style = stroke
        )
        // Inner core loop
        val core = Path().apply {
            moveTo(w * 0.43f, h * 0.86f)
            cubicTo(w * 0.42f, h * 0.68f, w * 0.43f, h * 0.46f, w * 0.50f, h * 0.46f)
            cubicTo(w * 0.57f, h * 0.46f, w * 0.58f, h * 0.68f, w * 0.55f, h * 0.88f)
        }
        drawPath(path = core, color = tint, style = stroke)

        // Lower side curves
        drawLine(
            color = tint,
            start = Offset(w * 0.20f, h * 0.52f),
            end = Offset(w * 0.24f, h * 0.78f),
            strokeWidth = w * 0.07f,
            cap = StrokeCap.Round
        )
        drawLine(
            color = tint,
            start = Offset(w * 0.80f, h * 0.52f),
            end = Offset(w * 0.76f, h * 0.80f),
            strokeWidth = w * 0.07f,
            cap = StrokeCap.Round
        )
        drawLine(
            color = tint,
            start = Offset(w * 0.31f, h * 0.58f),
            end = Offset(w * 0.34f, h * 0.85f),
            strokeWidth = w * 0.07f,
            cap = StrokeCap.Round
        )
        drawLine(
            color = tint,
            start = Offset(w * 0.69f, h * 0.58f),
            end = Offset(w * 0.66f, h * 0.85f),
            strokeWidth = w * 0.07f,
            cap = StrokeCap.Round
        )
    }
}

@Composable
fun NotificationBellWithDotIcon(
    tint: Color = Color(0xFF2B211B),
    size: Dp = 20.dp
) {
    Canvas(modifier = Modifier.size(size)) {
        val w = this.size.width
        val h = this.size.height
        val stroke = Stroke(width = w * 0.085f, cap = StrokeCap.Round, join = StrokeJoin.Round)

        val bellPath = Path().apply {
            moveTo(w * 0.22f, h * 0.72f)
            lineTo(w * 0.78f, h * 0.72f)
            cubicTo(w * 0.72f, h * 0.62f, w * 0.70f, h * 0.48f, w * 0.70f, h * 0.40f)
            cubicTo(w * 0.70f, h * 0.25f, w * 0.61f, h * 0.16f, w * 0.50f, h * 0.16f)
            cubicTo(w * 0.39f, h * 0.16f, w * 0.30f, h * 0.25f, w * 0.30f, h * 0.40f)
            cubicTo(w * 0.30f, h * 0.48f, w * 0.28f, h * 0.62f, w * 0.22f, h * 0.72f)
            close()
        }
        drawPath(path = bellPath, color = tint, style = stroke)

        // Clapper arc at bottom
        drawArc(
            color = tint,
            startAngle = 20f,
            sweepAngle = 140f,
            useCenter = false,
            topLeft = Offset(w * 0.40f, h * 0.69f),
            size = Size(w * 0.20f, h * 0.18f),
            style = stroke
        )

        // Notification dot on upper right of bell
        drawCircle(
            color = Color(0xFFF7E8D4),
            radius = w * 0.15f,
            center = Offset(w * 0.71f, h * 0.24f)
        )
        drawCircle(
            color = tint,
            radius = w * 0.095f,
            center = Offset(w * 0.71f, h * 0.24f)
        )
    }
}

@Composable
fun CopyWalletIdIcon(
    tint: Color = Color(0xFFF2C2A9),
    size: Dp = 14.dp
) {
    Canvas(modifier = Modifier.size(size)) {
        val w = this.size.width
        val h = this.size.height
        val stroke = Stroke(width = w * 0.09f, cap = StrokeCap.Round, join = StrokeJoin.Round)
        // Back rectangle
        drawRoundRect(
            color = tint,
            topLeft = Offset(w * 0.14f, h * 0.12f),
            size = Size(w * 0.54f, h * 0.58f),
            cornerRadius = CornerRadius(w * 0.1f, w * 0.1f),
            style = stroke
        )
        // Front rectangle
        drawRoundRect(
            color = Color(0xFFCE5A31),
            topLeft = Offset(w * 0.32f, h * 0.30f),
            size = Size(w * 0.54f, h * 0.58f),
            cornerRadius = CornerRadius(w * 0.1f, w * 0.1f)
        )
        drawRoundRect(
            color = tint,
            topLeft = Offset(w * 0.32f, h * 0.30f),
            size = Size(w * 0.54f, h * 0.58f),
            cornerRadius = CornerRadius(w * 0.1f, w * 0.1f),
            style = stroke
        )
    }
}

@Composable
fun ActionArrowDownLeftIcon(
    tint: Color = Color(0xFF281F1A),
    size: Dp = 20.dp
) {
    Canvas(modifier = Modifier.size(size)) {
        val w = this.size.width
        val h = this.size.height
        val stroke = Stroke(width = w * 0.085f, cap = StrokeCap.Round, join = StrokeJoin.Round)
        drawLine(
            color = tint,
            start = Offset(w * 0.72f, h * 0.28f),
            end = Offset(w * 0.28f, h * 0.72f),
            strokeWidth = w * 0.085f,
            cap = StrokeCap.Round
        )
        val head = Path().apply {
            moveTo(w * 0.28f, h * 0.40f)
            lineTo(w * 0.28f, h * 0.72f)
            lineTo(w * 0.60f, h * 0.72f)
        }
        drawPath(path = head, color = tint, style = stroke)
    }
}

@Composable
fun ActionArrowUpRightIcon(
    tint: Color = Color(0xFF281F1A),
    size: Dp = 20.dp
) {
    Canvas(modifier = Modifier.size(size)) {
        val w = this.size.width
        val h = this.size.height
        val stroke = Stroke(width = w * 0.085f, cap = StrokeCap.Round, join = StrokeJoin.Round)
        drawLine(
            color = tint,
            start = Offset(w * 0.28f, h * 0.72f),
            end = Offset(w * 0.72f, h * 0.28f),
            strokeWidth = w * 0.085f,
            cap = StrokeCap.Round
        )
        val head = Path().apply {
            moveTo(w * 0.40f, h * 0.28f)
            lineTo(w * 0.72f, h * 0.28f)
            lineTo(w * 0.72f, h * 0.60f)
        }
        drawPath(path = head, color = tint, style = stroke)
    }
}

@Composable
fun ActionSwapCircularIcon(
    tint: Color = Color(0xFF281F1A),
    size: Dp = 22.dp
) {
    Canvas(modifier = Modifier.size(size)) {
        val w = this.size.width
        val h = this.size.height
        val stroke = Stroke(width = w * 0.075f, cap = StrokeCap.Round, join = StrokeJoin.Round)

        // Upper arc curving right
        drawArc(
            color = tint,
            startAngle = 195f,
            sweepAngle = 145f,
            useCenter = false,
            topLeft = Offset(w * 0.14f, h * 0.18f),
            size = Size(w * 0.72f, h * 0.64f),
            style = stroke
        )
        // Right arrowhead
        val rightHead = Path().apply {
            moveTo(w * 0.70f, h * 0.22f)
            lineTo(w * 0.85f, h * 0.36f)
            lineTo(w * 0.68f, h * 0.43f)
        }
        drawPath(path = rightHead, color = tint, style = stroke)

        // Lower arc curving left
        drawArc(
            color = tint,
            startAngle = 15f,
            sweepAngle = 145f,
            useCenter = false,
            topLeft = Offset(w * 0.14f, h * 0.18f),
            size = Size(w * 0.72f, h * 0.64f),
            style = stroke
        )
        // Left arrowhead
        val leftHead = Path().apply {
            moveTo(w * 0.30f, h * 0.78f)
            lineTo(w * 0.15f, h * 0.64f)
            lineTo(w * 0.32f, h * 0.57f)
        }
        drawPath(path = leftHead, color = tint, style = stroke)
    }
}

@Composable
fun ActionPlusIcon(
    tint: Color = Color(0xFF281F1A),
    size: Dp = 20.dp
) {
    Canvas(modifier = Modifier.size(size)) {
        val w = this.size.width
        val h = this.size.height
        val sw = w * 0.085f
        drawLine(
            color = tint,
            start = Offset(w * 0.50f, h * 0.22f),
            end = Offset(w * 0.50f, h * 0.78f),
            strokeWidth = sw,
            cap = StrokeCap.Round
        )
        drawLine(
            color = tint,
            start = Offset(w * 0.22f, h * 0.50f),
            end = Offset(w * 0.78f, h * 0.50f),
            strokeWidth = sw,
            cap = StrokeCap.Round
        )
    }
}

@Composable
fun EthereumCrystalIcon(size: Dp = 24.dp) {
    Canvas(modifier = Modifier.size(size)) {
        val w = this.size.width
        val h = this.size.height

        // Top pyramid
        val topPyramid = Path().apply {
            moveTo(w * 0.50f, h * 0.10f)
            lineTo(w * 0.78f, h * 0.51f)
            lineTo(w * 0.50f, h * 0.63f)
            lineTo(w * 0.22f, h * 0.51f)
            close()
        }
        drawPath(path = topPyramid, color = Color.White)

        // Bottom pyramid
        val bottomPyramid = Path().apply {
            moveTo(w * 0.50f, h * 0.70f)
            lineTo(w * 0.78f, h * 0.57f)
            lineTo(w * 0.50f, h * 0.90f)
            lineTo(w * 0.22f, h * 0.57f)
            close()
        }
        drawPath(path = bottomPyramid, color = Color.White.copy(alpha = 0.92f))
    }
}

@Composable
fun BinanceDiamondIcon(size: Dp = 24.dp) {
    Canvas(modifier = Modifier.size(size)) {
        val w = this.size.width
        val h = this.size.height

        // Top chevron
        val topChevron = Path().apply {
            moveTo(w * 0.50f, h * 0.12f)
            lineTo(w * 0.73f, h * 0.35f)
            lineTo(w * 0.63f, h * 0.45f)
            lineTo(w * 0.50f, h * 0.32f)
            lineTo(w * 0.37f, h * 0.45f)
            lineTo(w * 0.27f, h * 0.35f)
            close()
        }
        drawPath(path = topChevron, color = Color.White)

        // Bottom chevron
        val bottomChevron = Path().apply {
            moveTo(w * 0.50f, h * 0.88f)
            lineTo(w * 0.73f, h * 0.65f)
            lineTo(w * 0.63f, h * 0.55f)
            lineTo(w * 0.50f, h * 0.68f)
            lineTo(w * 0.37f, h * 0.55f)
            lineTo(w * 0.27f, h * 0.65f)
            close()
        }
        drawPath(path = bottomChevron, color = Color.White)

        // Center small diamond
        val centerDiamond = Path().apply {
            moveTo(w * 0.50f, h * 0.39f)
            lineTo(w * 0.61f, h * 0.50f)
            lineTo(w * 0.50f, h * 0.61f)
            lineTo(w * 0.39f, h * 0.50f)
            close()
        }
        drawPath(path = centerDiamond, color = Color.White)

        // Left small diamond
        val leftDiamond = Path().apply {
            moveTo(w * 0.20f, h * 0.41f)
            lineTo(w * 0.29f, h * 0.50f)
            lineTo(w * 0.20f, h * 0.59f)
            lineTo(w * 0.11f, h * 0.50f)
            close()
        }
        drawPath(path = leftDiamond, color = Color.White)

        // Right small diamond
        val rightDiamond = Path().apply {
            moveTo(w * 0.80f, h * 0.41f)
            lineTo(w * 0.89f, h * 0.50f)
            lineTo(w * 0.80f, h * 0.59f)
            lineTo(w * 0.71f, h * 0.50f)
            close()
        }
        drawPath(path = rightDiamond, color = Color.White)
    }
}

@Composable
fun TetherSymbolIcon(size: Dp = 24.dp) {
    Canvas(modifier = Modifier.size(size)) {
        val w = this.size.width
        val h = this.size.height

        // Top horizontal bar of T
        drawRoundRect(
            color = Color.White,
            topLeft = Offset(w * 0.20f, h * 0.20f),
            size = Size(w * 0.60f, h * 0.13f),
            cornerRadius = CornerRadius(w * 0.03f, w * 0.03f)
        )
        // Vertical stem of T
        drawRoundRect(
            color = Color.White,
            topLeft = Offset(w * 0.43f, h * 0.22f),
            size = Size(w * 0.14f, h * 0.60f),
            cornerRadius = CornerRadius(w * 0.03f, w * 0.03f)
        )
        // Horizontal elliptical ring across the middle
        drawOval(
            color = Color.White,
            topLeft = Offset(w * 0.15f, h * 0.40f),
            size = Size(w * 0.70f, h * 0.18f),
            style = Stroke(width = w * 0.075f)
        )
    }
}

@Composable
fun NavHomeSmileIcon(
    tint: Color = Color(0xFFB86B43),
    size: Dp = 22.dp
) {
    Canvas(modifier = Modifier.size(size)) {
        val w = this.size.width
        val h = this.size.height
        val stroke = Stroke(width = w * 0.075f, cap = StrokeCap.Round, join = StrokeJoin.Round)

        val house = Path().apply {
            moveTo(w * 0.50f, h * 0.14f)
            lineTo(w * 0.84f, h * 0.42f)
            lineTo(w * 0.84f, h * 0.80f)
            quadraticTo(w * 0.84f, h * 0.86f, w * 0.76f, h * 0.86f)
            lineTo(w * 0.24f, h * 0.86f)
            quadraticTo(w * 0.16f, h * 0.86f, w * 0.16f, h * 0.80f)
            lineTo(w * 0.16f, h * 0.42f)
            close()
        }
        drawPath(path = house, color = tint, style = stroke)

        // Smile arc inside house
        drawArc(
            color = tint,
            startAngle = 20f,
            sweepAngle = 140f,
            useCenter = false,
            topLeft = Offset(w * 0.36f, h * 0.48f),
            size = Size(w * 0.28f, h * 0.22f),
            style = stroke
        )
    }
}

@Composable
fun NavWalletFilledIcon(
    tint: Color = Color(0xFFCE5A31),
    size: Dp = 22.dp
) {
    Canvas(modifier = Modifier.size(size)) {
        val w = this.size.width
        val h = this.size.height

        // Top folded flap
        val topFlap = Path().apply {
            moveTo(w * 0.18f, h * 0.30f)
            lineTo(w * 0.72f, h * 0.16f)
            quadraticTo(w * 0.80f, h * 0.14f, w * 0.80f, h * 0.24f)
            lineTo(w * 0.80f, h * 0.32f)
            lineTo(w * 0.18f, h * 0.32f)
            close()
        }
        drawPath(path = topFlap, color = tint.copy(alpha = 0.75f))

        // Main wallet body
        drawRoundRect(
            color = tint,
            topLeft = Offset(w * 0.12f, h * 0.28f),
            size = Size(w * 0.76f, h * 0.56f),
            cornerRadius = CornerRadius(w * 0.12f, w * 0.12f)
        )

        // Right clasp tab cutout
        drawRoundRect(
            color = Color.White,
            topLeft = Offset(w * 0.62f, h * 0.46f),
            size = Size(w * 0.28f, h * 0.20f),
            cornerRadius = CornerRadius(w * 0.08f, w * 0.08f)
        )
        drawCircle(
            color = tint,
            radius = w * 0.045f,
            center = Offset(w * 0.71f, h * 0.56f)
        )
    }
}

@Composable
fun NavPieChartIcon(
    tint: Color = Color(0xFF2B2522),
    size: Dp = 22.dp
) {
    Canvas(modifier = Modifier.size(size)) {
        val w = this.size.width
        val h = this.size.height
        val stroke = Stroke(width = w * 0.075f, cap = StrokeCap.Round, join = StrokeJoin.Round)

        // Main 270-degree circle
        val mainSlice = Path().apply {
            moveTo(w * 0.48f, h * 0.52f)
            lineTo(w * 0.48f, h * 0.16f)
            arcTo(
                rect = Rect(w * 0.14f, h * 0.18f, w * 0.82f, h * 0.86f),
                startAngleDegrees = 270f,
                sweepAngleDegrees = -270f,
                forceMoveTo = false
            )
            close()
        }
        drawPath(path = mainSlice, color = tint, style = stroke)

        // Separated top-right quadrant slice
        val topSlice = Path().apply {
            moveTo(w * 0.57f, h * 0.43f)
            lineTo(w * 0.57f, h * 0.12f)
            arcTo(
                rect = Rect(w * 0.26f, h * 0.12f, w * 0.88f, h * 0.74f),
                startAngleDegrees = 270f,
                sweepAngleDegrees = 90f,
                forceMoveTo = false
            )
            close()
        }
        drawPath(path = topSlice, color = tint, style = stroke)
    }
}

@Composable
fun NavSettingsCogIcon(
    tint: Color = Color(0xFF2B2522),
    size: Dp = 22.dp
) {
    Canvas(modifier = Modifier.size(size)) {
        val w = this.size.width
        val h = this.size.height
        val stroke = Stroke(width = w * 0.075f, cap = StrokeCap.Round, join = StrokeJoin.Round)

        // Inner circle
        drawCircle(
            color = tint,
            radius = w * 0.14f,
            center = Offset(w * 0.50f, h * 0.50f),
            style = stroke
        )
        // Outer gear circle with 8 teeth ticks
        drawCircle(
            color = tint,
            radius = w * 0.29f,
            center = Offset(w * 0.50f, h * 0.50f),
            style = stroke
        )
        val angles = listOf(0f, 45f, 90f, 135f, 180f, 225f, 270f, 315f)
        for (deg in angles) {
            val rad = Math.toRadians(deg.toDouble())
            val cos = kotlin.math.cos(rad).toFloat()
            val sin = kotlin.math.sin(rad).toFloat()
            drawLine(
                color = tint,
                start = Offset(w * 0.50f + cos * w * 0.29f, h * 0.50f + sin * h * 0.29f),
                end = Offset(w * 0.50f + cos * w * 0.39f, h * 0.50f + sin * h * 0.39f),
                strokeWidth = w * 0.085f,
                cap = StrokeCap.Round
            )
        }
    }
}

@Composable
fun KeypadReloadIcon(
    tint: Color = Color(0xFF2B231E),
    size: Dp = 20.dp
) {
    Canvas(modifier = Modifier.size(size)) {
        val w = this.size.width
        val h = this.size.height
        val stroke = Stroke(width = w * 0.085f, cap = StrokeCap.Round, join = StrokeJoin.Round)

        drawArc(
            color = tint,
            startAngle = 35f,
            sweepAngle = 295f,
            useCenter = false,
            topLeft = Offset(w * 0.18f, h * 0.18f),
            size = Size(w * 0.64f, h * 0.64f),
            style = stroke
        )
        val head = Path().apply {
            moveTo(w * 0.64f, h * 0.16f)
            lineTo(w * 0.82f, h * 0.34f)
            lineTo(w * 0.62f, h * 0.38f)
        }
        drawPath(path = head, color = tint, style = stroke)
    }
}

@Composable
fun KeypadBackspaceTagIcon(
    tint: Color = Color(0xFF2B231E),
    size: Dp = 22.dp
) {
    Canvas(modifier = Modifier.size(size)) {
        val w = this.size.width
        val h = this.size.height
        val stroke = Stroke(width = w * 0.075f, cap = StrokeCap.Round, join = StrokeJoin.Round)

        val tag = Path().apply {
            moveTo(w * 0.14f, h * 0.50f)
            lineTo(w * 0.34f, h * 0.26f)
            lineTo(w * 0.84f, h * 0.26f)
            lineTo(w * 0.84f, h * 0.74f)
            lineTo(w * 0.34f, h * 0.74f)
            close()
        }
        drawPath(path = tag, color = tint, style = stroke)

        // Small 'x' inside tag
        drawLine(
            color = tint,
            start = Offset(w * 0.47f, h * 0.40f),
            end = Offset(w * 0.67f, h * 0.60f),
            strokeWidth = w * 0.075f,
            cap = StrokeCap.Round
        )
        drawLine(
            color = tint,
            start = Offset(w * 0.67f, h * 0.40f),
            end = Offset(w * 0.47f, h * 0.60f),
            strokeWidth = w * 0.075f,
            cap = StrokeCap.Round
        )
    }
}
