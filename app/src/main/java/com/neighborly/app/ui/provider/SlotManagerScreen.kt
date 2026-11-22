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
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.neighborly.app.ui.theme.CardBorderStroke
import com.neighborly.app.ui.theme.DeepNavy
import com.neighborly.app.ui.theme.EmeraldGreen
import com.neighborly.app.ui.theme.EmeraldLight
import com.neighborly.app.ui.theme.PrimaryIndigo
import com.neighborly.app.ui.theme.SoftLavender
import com.neighborly.app.ui.theme.TextSecondary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SlotManagerScreen(
    viewModel: ProviderViewModel,
    onBackClick: () -> Unit
) {
    val slots by viewModel.providerSlots.collectAsState()
    val provider by viewModel.activeProvider.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Daily Slot Manager", fontWeight = FontWeight.Bold, color = DeepNavy) },
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
                .padding(16.dp)
        ) {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = SoftLavender),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        text = "Schedule for Today: ${provider?.name ?: "Provider"}",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = DeepNavy
                    )
                    Text(
                        text = "Toggle slots ON to make them open for booking, or OFF to block for personal time.",
                        fontSize = 12.sp,
                        color = TextSecondary
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(slots, key = { it.id }) { slot ->
                    val isAvailable = !slot.isBooked && !slot.isBlockedByProvider

                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = when {
                                slot.isBooked -> Color(0xFFF3F4F6)
                                slot.isBlockedByProvider -> Color(0xFFFAFAFA)
                                else -> Color.White
                            }
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, CardBorderStroke, RoundedCornerShape(16.dp))
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 14.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = slot.slotTime,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (slot.isBooked) TextSecondary else DeepNavy
                                )
                                Text(
                                    text = when {
                                        slot.isBooked -> "🔒 Booked by Customer"
                                        slot.isBlockedByProvider -> "⛔ Blocked by You"
                                        else -> "🟢 Open for Booking"
                                    },
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = when {
                                        slot.isBooked -> PrimaryIndigo
                                        slot.isBlockedByProvider -> TextSecondary
                                        else -> EmeraldGreen
                                    }
                                )
                            }

                            if (!slot.isBooked) {
                                Switch(
                                    checked = !slot.isBlockedByProvider,
                                    onCheckedChange = { isAvailableNew ->
                                        viewModel.toggleSlotBlocked(slot.id, !isAvailableNew)
                                    },
                                    colors = SwitchDefaults.colors(
                                        checkedThumbColor = Color.White,
                                        checkedTrackColor = EmeraldGreen
                                    )
                                )
                            } else {
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(SoftLavender)
                                        .padding(horizontal = 8.dp, vertical = 4.dp)
                                ) {
                                    Text(
                                        text = "Active Job",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = PrimaryIndigo
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

