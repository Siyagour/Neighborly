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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import androidx.compose.ui.window.Dialog
import com.neighborly.app.data.local.entity.BookingEntity
import com.neighborly.app.model.BookingStatus
import com.neighborly.app.ui.components.StatusBadge
import com.neighborly.app.ui.theme.AmberLight
import com.neighborly.app.ui.theme.AmberStar
import com.neighborly.app.ui.theme.CardBorderStroke
import com.neighborly.app.ui.theme.CrimsonRed
import com.neighborly.app.ui.theme.DeepNavy
import com.neighborly.app.ui.theme.EmeraldGreen
import com.neighborly.app.ui.theme.EmeraldLight
import com.neighborly.app.ui.theme.PrimaryIndigo
import com.neighborly.app.ui.theme.SkyLight
import com.neighborly.app.ui.theme.SoftLavender
import com.neighborly.app.ui.theme.TextPrimary
import com.neighborly.app.ui.theme.TextSecondary

@Composable
fun ProviderDashboardScreen(
    viewModel: ProviderViewModel,
    onNavigateToSlotManager: () -> Unit,
    onNavigateToIncomingRequests: () -> Unit
) {
    val provider by viewModel.activeProvider.collectAsState()
    val metrics by viewModel.metrics.collectAsState()
    val activeJobs by viewModel.activeJobs.collectAsState()
    val incomingRequests by viewModel.incomingRequests.collectAsState()

    var otpDialogBooking by remember { mutableStateOf<BookingEntity?>(null) }
    var enteredOtp by remember { mutableStateOf("") }
    var otpError by remember { mutableStateOf<String?>(null) }

    var reviseQuoteBooking by remember { mutableStateOf<BookingEntity?>(null) }
    var revisedAmountText by remember { mutableStateOf("") }
    var revisedReasonText by remember { mutableStateOf("") }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF9F9FE))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 1. Availability Switch Card
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, CardBorderStroke, RoundedCornerShape(20.dp))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = if (provider?.isOnline == true) "🟢 Online & Accepting Jobs" else "⚪ Offline",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (provider?.isOnline == true) EmeraldGreen else TextSecondary
                        )
                        Text(
                            text = if (provider?.isOnline == true) "Visible to nearby Udaipur customers" else "You are not receiving new requests",
                            fontSize = 12.sp,
                            color = TextSecondary
                        )
                    }

                    Switch(
                        checked = provider?.isOnline ?: true,
                        onCheckedChange = { viewModel.toggleOnlineStatus(it) },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.White,
                            checkedTrackColor = EmeraldGreen
                        )
                    )
                }
            }
        }

        // 2. Metrics Summary 2x2 Grid
        item {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Total Earnings
                    Card(
                        shape = RoundedCornerShape(18.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        modifier = Modifier
                            .weight(1f)
                            .border(1.dp, CardBorderStroke, RoundedCornerShape(18.dp))
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(text = "Total Earnings", fontSize = 11.sp, color = TextSecondary)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "₹${metrics.totalEarningsInr}",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = DeepNavy
                            )
                        }
                    }

                    // Completed Jobs
                    Card(
                        shape = RoundedCornerShape(18.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        modifier = Modifier
                            .weight(1f)
                            .border(1.dp, CardBorderStroke, RoundedCornerShape(18.dp))
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(text = "Completed Jobs", fontSize = 11.sp, color = TextSecondary)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "${metrics.completedJobsCount}",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = DeepNavy
                            )
                        }
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Active Jobs
                    Card(
                        shape = RoundedCornerShape(18.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        modifier = Modifier
                            .weight(1f)
                            .border(1.dp, CardBorderStroke, RoundedCornerShape(18.dp))
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(text = "Active Requests", fontSize = 11.sp, color = TextSecondary)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "${metrics.activeJobsCount}",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = PrimaryIndigo
                            )
                        }
                    }

                    // Average Rating
                    Card(
                        shape = RoundedCornerShape(18.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        modifier = Modifier
                            .weight(1f)
                            .border(1.dp, CardBorderStroke, RoundedCornerShape(18.dp))
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(text = "Average Rating", fontSize = 11.sp, color = TextSecondary)
                            Spacer(modifier = Modifier.height(4.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Star,
                                    contentDescription = null,
                                    tint = AmberStar,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "${metrics.averageRating}",
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = DeepNavy
                                )
                            }
                        }
                    }
                }
            }
        }

        // 3. Action Navigation Buttons
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Incoming Requests button with badge
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = SoftLavender),
                    modifier = Modifier
                        .weight(1f)
                        .clickable { onNavigateToIncomingRequests() }
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Notifications,
                                contentDescription = "Requests",
                                tint = PrimaryIndigo,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Requests",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = DeepNavy
                            )
                        }
                        if (incomingRequests.isNotEmpty()) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(PrimaryIndigo)
                                    .padding(horizontal = 8.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = incomingRequests.size.toString(),
                                    color = Color.White,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }

                // Slot Manager button
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = SoftLavender),
                    modifier = Modifier
                        .weight(1f)
                        .clickable { onNavigateToSlotManager() }
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.CalendarMonth,
                            contentDescription = "Slots",
                            tint = DeepNavy,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Slot Manager",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = DeepNavy
                        )
                    }
                }
            }
        }

        // 4. Active Job Execution Section
        item {
            Text(
                text = "Active Jobs (${activeJobs.size})",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = DeepNavy
            )
        }

        if (activeJobs.isEmpty()) {
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Box(modifier = Modifier.padding(24.dp), contentAlignment = Alignment.Center) {
                        Text(
                            text = "No jobs currently in progress or waiting for OTP.",
                            fontSize = 13.sp,
                            color = TextSecondary
                        )
                    }
                }
            }
        } else {
            items(activeJobs, key = { it.id }) { job ->
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, CardBorderStroke, RoundedCornerShape(20.dp))
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = job.subServiceName,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = DeepNavy
                                )
                                Text(
                                    text = "${job.customerName} • ${job.slotTime}",
                                    fontSize = 12.sp,
                                    color = TextSecondary
                                )
                            }
                            StatusBadge(status = job.status)
                        }

                        if (job.issueNote.isNotBlank()) {
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                text = "Issue: \"${job.issueNote}\"",
                                fontSize = 12.sp,
                                color = TextPrimary,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(SoftLavender)
                                    .padding(10.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Step 1: Accepted -> Enter Start OTP
                        if (job.status == BookingStatus.ACCEPTED.label) {
                            Button(
                                onClick = {
                                    otpDialogBooking = job
                                    enteredOtp = ""
                                    otpError = null
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = PrimaryIndigo),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = "Enter Customer 4-Digit OTP to Start Job",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }
                        }

                        // Step 2 & 3: In Progress -> Revise Quote / Complete
                        if (job.status == BookingStatus.IN_PROGRESS.label) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                OutlinedButton(
                                    onClick = {
                                        reviseQuoteBooking = job
                                        revisedAmountText = (job.initialQuoteMaxInr + 200).toString()
                                        revisedReasonText = ""
                                    },
                                    shape = RoundedCornerShape(12.dp),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text("Revise Quote", fontSize = 12.sp, color = DeepNavy)
                                }

                                Button(
                                    onClick = {
                                        val finalAmount = job.revisedQuoteInr ?: job.initialQuoteMinInr
                                        viewModel.completeBooking(job.id, finalAmount)
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = EmeraldGreen),
                                    shape = RoundedCornerShape(12.dp),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text("Mark Paid & Complete", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // OTP Verification Dialog
    if (otpDialogBooking != null) {
        val job = otpDialogBooking!!
        Dialog(onDismissRequest = { otpDialogBooking = null }) {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Enter Start OTP",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = DeepNavy
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Ask customer ${job.customerName} for their 4-digit code to begin the job.",
                        fontSize = 12.sp,
                        color = TextSecondary
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = enteredOtp,
                        onValueChange = {
                            if (it.length <= 4) enteredOtp = it
                            otpError = null
                        },
                        label = { Text("4-Digit Code (Demo: ${job.startOtp})") },
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    )

                    if (otpError != null) {
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(text = otpError ?: "", color = CrimsonRed, fontSize = 12.sp)
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        OutlinedButton(
                            onClick = { otpDialogBooking = null },
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text("Cancel", color = TextSecondary)
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Button(
                            onClick = {
                                viewModel.verifyStartOtp(job.id, enteredOtp) { success ->
                                    if (success) {
                                        otpDialogBooking = null
                                    } else {
                                        otpError = "Incorrect OTP. Please check with customer."
                                    }
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = PrimaryIndigo),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text("Verify & Start", color = Color.White, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }

    // Revise Quote Dialog
    if (reviseQuoteBooking != null) {
        val job = reviseQuoteBooking!!
        Dialog(onDismissRequest = { reviseQuoteBooking = null }) {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(24.dp)) {
                    Text(
                        text = "Revise Job Quote",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = DeepNavy
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Add extra cost for spare parts or additional labor.",
                        fontSize = 12.sp,
                        color = TextSecondary
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = revisedAmountText,
                        onValueChange = { revisedAmountText = it },
                        label = { Text("New Total Amount (₹)") },
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = revisedReasonText,
                        onValueChange = { revisedReasonText = it },
                        label = { Text("Reason (e.g. Capacitor + Extra copper wiring)") },
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        OutlinedButton(
                            onClick = { reviseQuoteBooking = null },
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text("Cancel", color = TextSecondary)
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Button(
                            onClick = {
                                val amount = revisedAmountText.toIntOrNull() ?: 500
                                viewModel.reviseQuote(
                                    bookingId = job.id,
                                    amount = amount,
                                    reason = revisedReasonText.ifBlank { "Additional repair work and replacement parts." }
                                )
                                reviseQuoteBooking = null
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = DeepNavy),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text("Send to Customer", color = Color.White, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}

