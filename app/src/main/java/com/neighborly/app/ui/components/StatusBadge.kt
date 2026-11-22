package com.neighborly.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.neighborly.app.model.BookingStatus
import com.neighborly.app.ui.theme.AmberLight
import com.neighborly.app.ui.theme.AmberStar
import com.neighborly.app.ui.theme.CrimsonLight
import com.neighborly.app.ui.theme.CrimsonRed
import com.neighborly.app.ui.theme.EmeraldGreen
import com.neighborly.app.ui.theme.EmeraldLight
import com.neighborly.app.ui.theme.PrimaryIndigo
import com.neighborly.app.ui.theme.SkyBlue
import com.neighborly.app.ui.theme.SkyLight
import com.neighborly.app.ui.theme.SoftLavender

@Composable
fun StatusBadge(
    status: String,
    modifier: Modifier = Modifier
) {
    val (bgColor, textColor) = when (status) {
        BookingStatus.REQUESTED.label -> Pair(SoftLavender, PrimaryIndigo)
        BookingStatus.ACCEPTED.label -> Pair(SkyLight, SkyBlue)
        BookingStatus.IN_PROGRESS.label -> Pair(AmberLight, AmberStar)
        BookingStatus.COMPLETED.label -> Pair(EmeraldLight, EmeraldGreen)
        BookingStatus.DECLINED.label -> Pair(CrimsonLight, CrimsonRed)
        BookingStatus.CANCELLED.label -> Pair(Color(0xFFF3F4F6), Color(0xFF6B7280))
        else -> Pair(SoftLavender, PrimaryIndigo)
    }

    Text(
        text = status,
        color = textColor,
        fontWeight = FontWeight.Bold,
        fontSize = 12.sp,
        modifier = modifier
            .background(bgColor, RoundedCornerShape(12.dp))
            .padding(horizontal = 10.dp, vertical = 4.dp)
    )
}

