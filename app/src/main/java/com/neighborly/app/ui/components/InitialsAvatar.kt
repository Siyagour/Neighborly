package com.neighborly.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.neighborly.app.ui.theme.PrimaryIndigo

@Composable
fun InitialsAvatar(
    initials: String,
    colorHex: String,
    modifier: Modifier = Modifier,
    size: Dp = 48.dp,
    fontSize: Int = 16
) {
    val bgColor = try {
        Color(android.graphics.Color.parseColor(colorHex))
    } catch (_: Exception) {
        PrimaryIndigo
    }

    Box(
        modifier = modifier
            .size(size)
            .clip(CircleShape)
            .background(bgColor),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = initials.uppercase(),
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = fontSize.sp
        )
    }
}

