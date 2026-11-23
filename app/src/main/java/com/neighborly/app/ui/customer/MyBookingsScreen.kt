package com.neighborly.app.ui.customer

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.Engineering
import androidx.compose.material.icons.rounded.HourglassEmpty
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
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
import com.neighborly.app.ui.components.RatingDialog
import com.neighborly.app.ui.components.StarRatingBar
import com.neighborly.app.ui.components.StatusBadge
import com.neighborly.app.ui.theme.AmberLight
import com.neighborly.app.ui.theme.AmberStar
import com.neighborly.app.ui.theme.CardBorderStroke
import com.neighborly.app.ui.theme.CrimsonLight
import com.neighborly.app.ui.theme.CrimsonRed
import com.neighborly.app.ui.theme.DeepNavy
import com.neighborly.app.ui.theme.EmeraldGreen
import com.neighborly.app.ui.theme.PrimaryIndigo
import com.neighborly.app.ui.theme.SkyLight
import com.neighborly.app.ui.theme.SoftLavender
import com.neighborly.app.ui.theme.TextPrimary
import com.neighborly.app.ui.theme.TextSecondary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyBookingsScreen(
    viewModel: CustomerViewModel,
    onBackClick: () -> Unit
) {
    val bookings by viewModel.customerBookings.collectAsState()
    val allProviders by viewModel.filteredProviders.collectAsState()

    var selectedTabIndex by remember { mutableIntStateOf(0) }
    var ratingBooking by remember { mutableStateOf<BookingEntity?>(null) }

    val activeBookings = bookings.filter {
        it.status != BookingStatus.COMPLETED.label &&
                it.status != BookingStatus.DECLINED.label &&
                it.status != BookingStatus.CANCELLED.label
    }
    val pastBookings = bookings.filter {
        it.status == BookingStatus.COMPLETED.label ||
                it.status == BookingStatus.DECLINED.label ||
                it.status == BookingStatus.CANCELLED.label
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("My Bookings & Tracker", fontWeight = FontWeight.Bold, color = DeepNavy) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = DeepNavy
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF9F9FE))
                .padding(padding)
        ) {
            TabRow(
                selectedTabIndex = selectedTabIndex,
                containerColor = Color.White,
                contentColor = PrimaryIndigo
            ) {
                Tab(
                    selected = selectedTabIndex == 0,
                    onClick = { selectedTabIndex = 0 },
                    text = {
                        Text(
                            text = "Active (${activeBookings.size})",
                            fontWeight = FontWeight.Bold,
                            color = if (selectedTabIndex == 0) PrimaryIndigo else TextSecondary
                        )
                    }
                )
                Tab(
                    selected = selectedTabIndex == 1,
                    onClick = { selectedTabIndex = 1 },
                    text = {
                        Text(
                            text = "Completed & Past (${pastBookings.size})",
                            fontWeight = FontWeight.Bold,
                            color = if (selectedTabIndex == 1) PrimaryIndigo else TextSecondary
                        )
                    }
                )
            }

            val currentList = if (selectedTabIndex == 0) activeBookings else pastBookings

            if (currentList.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (selectedTabIndex == 0) "No active bookings right now." else "No past bookings yet.",
                        color = TextSecondary,
                        fontSize = 14.sp
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(currentList, key = { it.id }) { booking ->
                        val provider = allProviders.firstOrNull { it.id == booking.providerId }
                        val providerName = provider?.name ?: "Technician"

                        Card(
                            shape = RoundedCornerShape(20.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(1.dp, CardBorderStroke, RoundedCornerShape(20.dp))
                        ) {
                            Column(modifier = Modifier.padding(18.dp)) {
                                // Header: Subservice, Status, Time
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = booking.subServiceName,
                                            fontSize = 15.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = DeepNavy
                                        )
                                        Text(
                                            text = "With $providerName • ${booking.slotTime}",
                                            fontSize = 12.sp,
                                            color = TextSecondary
                                        )
                                    }
                                    StatusBadge(status = booking.status)
                                }

                                Spacer(modifier = Modifier.height(12.dp))

                                // Issue Note
                                if (booking.issueNote.isNotBlank()) {
                                    Text(
                                        text = "Note: \"${booking.issueNote}\"",
                                        fontSize = 12.sp,
                                        color = TextPrimary,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clip(RoundedCornerShape(10.dp))
                                            .background(SoftLavender)
                                            .padding(10.dp)
                                    )
                                    Spacer(modifier = Modifier.height(12.dp))
                                }

                                // State 1: Requested
                                if (booking.status == BookingStatus.REQUESTED.label) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                                        ) {
                                            Icon(
                                                imageVector = Icons.Rounded.HourglassEmpty,
                                                contentDescription = null,
                                                tint = TextSecondary,
                                                modifier = Modifier.size(14.dp)
                                            )
                                            Text(
                                                text = "Waiting for professional to accept...",
                                                fontSize = 12.sp,
                                                color = TextSecondary
                                            )
                                        }
                                        OutlinedButton(
                                            onClick = { viewModel.cancelBooking(booking.id) },
                                            shape = RoundedCornerShape(10.dp)
                                        ) {
                                            Text("Cancel", color = CrimsonRed, fontSize = 11.sp)
                                        }
                                    }
                                }

                                // State 2: Accepted -> Prominent Start OTP
                                if (booking.status == BookingStatus.ACCEPTED.label) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clip(RoundedCornerShape(14.dp))
                                            .background(SkyLight)
                                            .border(1.dp, Color(0xFFBAE6FD), RoundedCornerShape(14.dp))
                                            .padding(14.dp)
                                    ) {
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Column {
                                                Row(verticalAlignment = Alignment.CenterVertically) {
                                                    Icon(
                                                        imageVector = Icons.Default.Lock,
                                                        contentDescription = "OTP",
                                                        tint = PrimaryIndigo,
                                                        modifier = Modifier.size(16.dp)
                                                    )
                                                    Spacer(modifier = Modifier.width(4.dp))
                                                    Text(
                                                        text = "START JOB OTP",
                                                        fontSize = 11.sp,
                                                        fontWeight = FontWeight.Bold,
                                                        color = PrimaryIndigo
                                                    )
                                                }
                                                Spacer(modifier = Modifier.height(2.dp))
                                                Text(
                                                    text = "Share with technician upon arrival",
                                                    fontSize = 11.sp,
                                                    color = TextSecondary
                                                )
                                            }

                                            Text(
                                                text = booking.startOtp,
                                                fontSize = 24.sp,
                                                fontWeight = FontWeight.ExtraBold,
                                                letterSpacing = 4.sp,
                                                color = DeepNavy
                                            )
                                        }
                                    }
                                }

                                // State 3: In Progress
                                if (booking.status == BookingStatus.IN_PROGRESS.label) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clip(RoundedCornerShape(12.dp))
                                            .background(AmberLight)
                                            .padding(10.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Icon(
                                                imageVector = Icons.Rounded.Engineering,
                                                contentDescription = null,
                                                tint = Color(0xFF92400E),
                                                modifier = Modifier.size(16.dp)
                                            )
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Text(
                                                text = "Job currently in progress by $providerName",
                                                fontSize = 12.sp,
                                                fontWeight = FontWeight.SemiBold,
                                                color = Color(0xFF92400E)
                                            )
                                        }
                                    }
                                }

                                // Revised Quote Banner (if provider proposed extra parts/labor)
                                if (booking.revisedQuoteInr != null) {
                                    Spacer(modifier = Modifier.height(10.dp))
                                    Card(
                                        shape = RoundedCornerShape(14.dp),
                                        colors = CardDefaults.cardColors(
                                            containerColor = if (booking.isRevisedQuoteApproved) SoftLavender else AmberLight
                                        ),
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Column(modifier = Modifier.padding(12.dp)) {
                                            Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                horizontalArrangement = Arrangement.SpaceBetween,
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Text(
                                                    text = "Revised Quote: ₹${booking.revisedQuoteInr}",
                                                    fontSize = 14.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = DeepNavy
                                                )
                                                if (booking.isRevisedQuoteApproved) {
                                                    Row(
                                                        verticalAlignment = Alignment.CenterVertically,
                                                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                                                    ) {
                                                        Icon(
                                                            imageVector = Icons.Rounded.CheckCircle,
                                                            contentDescription = null,
                                                            tint = EmeraldGreen,
                                                            modifier = Modifier.size(14.dp)
                                                        )
                                                        Text(
                                                            text = "Approved by You",
                                                            fontSize = 11.sp,
                                                            fontWeight = FontWeight.Bold,
                                                            color = EmeraldGreen
                                                        )
                                                    }
                                                }
                                            }

                                            if (!booking.revisedQuoteReason.isNullOrBlank()) {
                                                Spacer(modifier = Modifier.height(2.dp))
                                                Text(
                                                    text = "Reason: ${booking.revisedQuoteReason}",
                                                    fontSize = 12.sp,
                                                    color = TextPrimary
                                                )
                                            }

                                            if (!booking.isRevisedQuoteApproved) {
                                                Spacer(modifier = Modifier.height(10.dp))
                                                Button(
                                                    onClick = { viewModel.approveRevisedQuote(booking.id) },
                                                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryIndigo),
                                                    shape = RoundedCornerShape(10.dp),
                                                    modifier = Modifier.fillMaxWidth()
                                                ) {
                                                    Text(
                                                        text = "Approve Revised Quote (₹${booking.revisedQuoteInr})",
                                                        fontSize = 12.sp,
                                                        fontWeight = FontWeight.Bold,
                                                        color = Color.White
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }

                                // State 4: Completed -> Final Bill & Rating Dialog trigger
                                if (booking.status == BookingStatus.COMPLETED.label) {
                                    Spacer(modifier = Modifier.height(10.dp))
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Column {
                                            Text(
                                                text = "Paid ₹${booking.finalPaidAmountInr ?: (booking.revisedQuoteInr ?: booking.initialQuoteMinInr)}",
                                                fontSize = 14.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = EmeraldGreen
                                            )
                                            Text(text = "Completed & Paid", fontSize = 11.sp, color = TextSecondary)
                                        }

                                        if (!booking.isReviewed) {
                                            Button(
                                                onClick = { ratingBooking = booking },
                                                colors = ButtonDefaults.buttonColors(containerColor = DeepNavy),
                                                shape = RoundedCornerShape(12.dp)
                                            ) {
                                                Text("Rate & Review", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                            }
                                        } else {
                                            Column(horizontalAlignment = Alignment.End) {
                                                StarRatingBar(rating = booking.rating ?: 5f, starSize = 14.dp)
                                                Spacer(modifier = Modifier.height(2.dp))
                                                Row(
                                                    verticalAlignment = Alignment.CenterVertically,
                                                    horizontalArrangement = Arrangement.spacedBy(2.dp)
                                                ) {
                                                    Icon(
                                                        imageVector = Icons.Rounded.Check,
                                                        contentDescription = null,
                                                        tint = EmeraldGreen,
                                                        modifier = Modifier.size(12.dp)
                                                    )
                                                    Text(text = "Reviewed", fontSize = 11.sp, color = EmeraldGreen, fontWeight = FontWeight.Bold)
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
        }

        // Rating Dialog
        if (ratingBooking != null) {
            val bookingToRate = ratingBooking!!
            val provider = allProviders.firstOrNull { it.id == bookingToRate.providerId }
            RatingDialog(
                providerName = provider?.name ?: "Technician",
                onDismiss = { ratingBooking = null },
                onSubmit = { rating, comment ->
                    viewModel.submitRating(
                        bookingId = bookingToRate.id,
                        providerId = bookingToRate.providerId,
                        rating = rating,
                        comment = comment
                    )
                    ratingBooking = null
                }
            )
        }
    }
}

