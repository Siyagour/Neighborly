package com.neighborly.app.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

private val LightColorScheme = lightColorScheme(
    primary = PrimaryIndigo,
    onPrimary = Color.White,
    primaryContainer = SoftLavender,
    onPrimaryContainer = DeepNavy,
    secondary = DeepNavy,
    onSecondary = Color.White,
    secondaryContainer = SoftLavenderLight,
    onSecondaryContainer = DeepNavy,
    background = Color(0xFFF9F9FE),
    onBackground = DeepNavy,
    surface = Color.White,
    onSurface = DeepNavy,
    surfaceVariant = SoftLavender,
    onSurfaceVariant = TextSecondary,
    outline = CardBorderStroke
)

val NeighborlyShapes = Shapes(
    extraSmall = RoundedCornerShape(8.dp),
    small = RoundedCornerShape(12.dp),
    medium = RoundedCornerShape(16.dp),
    large = RoundedCornerShape(20.dp),
    extraLarge = RoundedCornerShape(24.dp)
)

@Composable
fun NeighborlyTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = Typography,
        shapes = NeighborlyShapes,
        content = content
    )
}

