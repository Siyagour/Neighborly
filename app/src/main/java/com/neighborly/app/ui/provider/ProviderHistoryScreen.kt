package com.neighborly.app.ui.provider

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.neighborly.app.data.local.entity.BookingEntity
import com.neighborly.app.model.BookingStatus
import com.neighborly.app.ui.components.StarRatingBar
import com.neighborly.app.ui.components.StatusBadge
import com.neighborly.app.ui.theme.CardBorderStroke
import com.neighborly.app.ui.theme.CrimsonLight
import com.neighborly.app.ui.theme.CrimsonRed
import com.neighborly.app.ui.theme.DeepNavy
import com.neighborly.app.ui.theme.EmeraldGreen
import com.neighborly.app.ui.theme.EmeraldLight
import com.neighborly.app.ui.theme.PrimaryIndigo
import com.neighborly.app.ui.theme.SoftLavender
import com.neighborly.app.ui.theme.TextPrimary
import com.neighborly.app.ui.theme.TextSecondary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProviderHistoryScreen(
    viewModel: ProviderViewModel,
    onBackClick: () -> Unit
) {
    val historyBookings by viewModel.historyBookings.collectAsState()
    var selectedFilterIndex by remember { mutableIntStateOf(0) } // 0: All, 1: Completed, 2: Cancelled/Declined

    val completedList = historyBookings.filter { it.status == BookingStatus.COMPLETED.label }
    val cancelledDeclinedList = historyBookings.filter {
        it.status == BookingStatus.CANCELLED.label || it.status == BookingStatus.DECLINED.label
    }

    val totalEarnings = completedList.sumOf {
        it.finalPaidAmountInr ?: (it.revisedQuoteInr ?: it.initialQuoteMinInr)
    }

    val displayedList = when (selectedFilterIndex) {
        1 -> completedList
        2 -> cancelledDeclinedList
        else -> historyBookings
    }.sortedByDescending { it.createdAt }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Work & Earnings History", fontWeight = FontWeight.Bold, color = DeepNavy) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = DeepNavy)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White),
                modifier = Modifier.statusBarsPadding()
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF9F9FE))
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // 1. Top Summary Card
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, CardBorderStroke, RoundedCornerShape(20.dp))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Total Earnings
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "₹$totalEarnings",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = EmeraldGreen
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(text = "Total Earned", fontSize = 11.sp, color = TextSecondary)
                    }

                    Box(
                        modifier = Modifier
                            .width(1.dp)
                            .height(36.dp)
                            .background(CardBorderStroke)
                    )

                    // Completed Jobs
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "${completedList.size}",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = DeepNavy
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(text = "Completed", fontSize = 11.sp, color = TextSecondary)
                    }

                    Box(
                        modifier = Modifier
                            .width(1.dp)
                            .height(36.dp)
                            .background(CardBorderStroke)
                    )

                    // Cancelled / Declined
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "${cancelledDeclinedList.size}",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = CrimsonRed
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(text = "Cancelled / Off", fontSize = 11.sp, color = TextSecondary)
                    }
                }
            }

            // 2. Filter Chips: All | Completed | Cancelled / Declined
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                val filters = listOf(
                    "All (${historyBookings.size})",
                    "Completed (${completedList.size})",
                    "Cancelled / Declined (${cancelledDeclinedList.size})"
                )

                filters.forEachIndexed { index, title ->
                    val isSelected = selectedFilterIndex == index
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(if (isSelected) PrimaryIndigo else SoftLavender)
                            .clickable { selectedFilterIndex = index }
                            .padding(horizontal = 12.dp, vertical = 7.dp)
                    ) {
                        Text(
                            text = title,
                            fontSize = 11.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            color = if (isSelected) Color.White else DeepNavy
                        )
                    }
                }
            }

            // 3. History Cards List
            if (displayedList.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No history records for this filter.",
                        fontSize = 13.sp,
                        color = TextSecondary
                    )
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(displayedList, key = { it.id }) { booking ->
                        val isCompleted = booking.status == BookingStatus.COMPLETED.label
                        val isCancelled = booking.status == BookingStatus.CANCELLED.label
                        val isDeclined = booking.status == BookingStatus.DECLINED.label

                        val paidAmount = booking.finalPaidAmountInr
                            ?: (booking.revisedQuoteInr ?: booking.initialQuoteMinInr)

                        Card(
                            shape = RoundedCornerShape(18.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(1.dp, CardBorderStroke, RoundedCornerShape(18.dp))
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                // Header: Service Name + Status Badge
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = booking.subServiceName,
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = DeepNavy
                                        )
                                        Text(
                                            text = "Customer: ${booking.customerName} • ${booking.slotTime}",
                                            fontSize = 12.sp,
                                            color = TextSecondary
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(8.dp))
                                    StatusBadge(status = booking.status)
                                }

                                if (booking.issueNote.isNotBlank()) {
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = "Note: \"${booking.issueNote}\"",
                                        fontSize = 11.sp,
                                        color = TextPrimary,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(SoftLavender)
                                            .padding(8.dp)
                                    )
                                }

                                Spacer(modifier = Modifier.height(10.dp))

                                // Earnings Breakdown / Settlement
                                if (isCompleted) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clip(RoundedCornerShape(10.dp))
                                            .background(EmeraldLight)
                                            .padding(horizontal = 12.dp, vertical = 8.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Column {
                                            Text(
                                                text = if (booking.revisedQuoteInr != null)
                                                    "Visiting Fee + Revised Parts/Labor"
                                                else
                                                    "Visiting Fee + Standard Service",
                                                fontSize = 11.sp,
                                                color = Color(0xFF065F46)
                                            )
                                            if (booking.revisedQuoteReason != null) {
                                                Text(
                                                    text = "Item: ${booking.revisedQuoteReason}",
                                                    fontSize = 10.sp,
                                                    color = Color(0xFF047857)
                                                )
                                            }
                                        }

                                        Text(
                                            text = "+ ₹$paidAmount",
                                            fontSize = 15.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = EmeraldGreen
                                        )
                                    }

                                    // Customer Rating if reviewed
                                    if (booking.isReviewed && booking.rating != null) {
                                        Spacer(modifier = Modifier.height(8.dp))
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            modifier = Modifier.fillMaxWidth()
                                        ) {
                                            StarRatingBar(rating = booking.rating, starSize = 13.dp)
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Text(
                                                text = booking.reviewText ?: "Customer rated 5.0",
                                                fontSize = 11.sp,
                                                color = TextSecondary,
                                                maxLines = 1
                                            )
                                        }
                                    }
                                } else {
                                    // Cancelled or Declined row
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clip(RoundedCornerShape(10.dp))
                                            .background(Color(0xFFF3F4F6))
                                            .padding(horizontal = 12.dp, vertical = 8.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = if (isCancelled) "Customer Cancelled Request" else "Declined by Professional",
                                            fontSize = 11.sp,
                                            color = TextSecondary,
                                            fontWeight = FontWeight.Medium
                                        )
                                        Text(
                                            text = "₹0",
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = TextSecondary
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

