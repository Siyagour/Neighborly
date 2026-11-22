package com.neighborly.app.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.neighborly.app.ui.theme.AmberStar

@Composable
fun StarRatingBar(
    rating: Float,
    modifier: Modifier = Modifier,
    maxStars: Int = 5,
    starSize: Dp = 20.dp,
    isInteractive: Boolean = false,
    onRatingChanged: ((Float) -> Unit)? = null
) {
    Row(modifier = modifier) {
        for (i in 1..maxStars) {
            val isFilled = i <= rating
            Icon(
                imageVector = if (isFilled) Icons.Default.Star else Icons.Default.StarBorder,
                contentDescription = "Star $i",
                tint = if (isFilled) AmberStar else Color(0xFFD1D5DB),
                modifier = Modifier
                    .size(starSize)
                    .clickable(enabled = isInteractive) {
                        onRatingChanged?.invoke(i.toFloat())
                    }
            )
        }
    }
}

