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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
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
import com.neighborly.app.data.local.entity.ProviderSlotEntity
import com.neighborly.app.data.local.entity.SubServiceEntity
import com.neighborly.app.ui.theme.CardBorderStroke
import com.neighborly.app.ui.theme.DeepNavy
import com.neighborly.app.ui.theme.PrimaryIndigo
import com.neighborly.app.ui.theme.SoftLavender
import com.neighborly.app.ui.theme.TextPrimary
import com.neighborly.app.ui.theme.TextSecondary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SlotBookingScreen(
    providerId: Long,
    viewModel: CustomerViewModel,
    onBackClick: () -> Unit,
    onBookingSuccess: () -> Unit
) {
    val providers by viewModel.filteredProviders.collectAsState()
    val provider = providers.firstOrNull { it.id == providerId }

    val subServices by viewModel.getSubServices(providerId).collectAsState(initial = emptyList())
    val availableSlots by viewModel.getAvailableSlots(providerId).collectAsState(initial = emptyList())

    var selectedSubService by remember { mutableStateOf<SubServiceEntity?>(null) }
    var selectedSlot by remember { mutableStateOf<ProviderSlotEntity?>(null) }
    var issueNote by remember { mutableStateOf("") }

    // Pre-select first sub-service and slot when loaded
    if (selectedSubService == null && subServices.isNotEmpty()) {
        selectedSubService = subServices.first()
    }
    if (selectedSlot == null && availableSlots.isNotEmpty()) {
        selectedSlot = availableSlots.first()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Book Appointment", fontWeight = FontWeight.Bold, color = DeepNavy) },
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
        },
        bottomBar = {
            Surface(
                color = Color.White,
                shadowElevation = 8.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    val visitingFee = provider?.visitingFeeInr ?: 0
                    val minPrice = (selectedSubService?.minPriceInr ?: 0) + visitingFee
                    val maxPrice = (selectedSubService?.maxPriceInr ?: 0) + visitingFee

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Estimated: ₹$minPrice - ₹$maxPrice",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = DeepNavy
                            )
                            Text(
                                text = "Visiting fee + service estimate",
                                fontSize = 11.sp,
                                color = TextSecondary
                            )
                        }

                        Button(
                            onClick = {
                                val currentService = selectedSubService
                                val currentSlot = selectedSlot
                                if (currentService != null && currentSlot != null) {
                                    viewModel.bookSlot(
                                        providerId = providerId,
                                        slotId = currentSlot.id,
                                        slotTime = currentSlot.slotTime,
                                        subServiceName = currentService.serviceName,
                                        initialQuoteMin = currentService.minPriceInr + visitingFee,
                                        initialQuoteMax = currentService.maxPriceInr + visitingFee,
                                        issueNote = issueNote.ifBlank { "Standard repair & diagnosis request." },
                                        onSuccess = { onBookingSuccess() }
                                    )
                                }
                            },
                            enabled = selectedSubService != null && selectedSlot != null,
                            colors = ButtonDefaults.buttonColors(containerColor = PrimaryIndigo),
                            shape = RoundedCornerShape(14.dp),
                            modifier = Modifier.height(48.dp)
                        ) {
                            Text(
                                text = "Confirm Booking",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }
                }
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF9F9FE))
                .padding(padding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(4.dp))
                // Provider banner
                if (provider != null) {
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = SoftLavender),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "Booking with ${provider.name}",
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = DeepNavy
                                )
                                Text(
                                    text = "${provider.category} • Base Visit Fee: ₹${provider.visitingFeeInr}",
                                    fontSize = 12.sp,
                                    color = TextSecondary
                                )
                            }
                        }
                    }
                }
            }

            // Step 1: Select Sub-Service
            item {
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, CardBorderStroke, RoundedCornerShape(20.dp))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "1. Select Sub-Service",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = DeepNavy
                        )
                        Spacer(modifier = Modifier.height(10.dp))

                        subServices.forEach { service ->
                            val isSelected = selectedSubService?.id == service.id
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(if (isSelected) SoftLavender else Color.Transparent)
                                    .clickable { selectedSubService = service }
                                    .padding(vertical = 8.dp, horizontal = 10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                RadioButton(
                                    selected = isSelected,
                                    onClick = { selectedSubService = service },
                                    colors = RadioButtonDefaults.colors(selectedColor = PrimaryIndigo)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = service.serviceName,
                                        fontSize = 13.sp,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                        color = DeepNavy
                                    )
                                    Text(
                                        text = "Est. ₹${service.minPriceInr} - ₹${service.maxPriceInr}",
                                        fontSize = 11.sp,
                                        color = PrimaryIndigo
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Step 2: Select 1-Hour Time Slot
            item {
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, CardBorderStroke, RoundedCornerShape(20.dp))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "2. Select 1-Hour Time Slot (Today)",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = DeepNavy
                        )
                        Spacer(modifier = Modifier.height(10.dp))

                        if (availableSlots.isEmpty()) {
                            Text(
                                text = "No open slots available today for this professional.",
                                fontSize = 12.sp,
                                color = TextSecondary,
                                modifier = Modifier.padding(vertical = 8.dp)
                            )
                        } else {
                            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                availableSlots.forEach { slot ->
                                    val isSelected = selectedSlot?.id == slot.id
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clip(RoundedCornerShape(12.dp))
                                            .background(if (isSelected) PrimaryIndigo else SoftLavender)
                                            .border(
                                                1.dp,
                                                if (isSelected) PrimaryIndigo else CardBorderStroke,
                                                RoundedCornerShape(12.dp)
                                            )
                                            .clickable { selectedSlot = slot }
                                            .padding(horizontal = 14.dp, vertical = 10.dp)
                                    ) {
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text(
                                                text = slot.slotTime,
                                                fontSize = 13.sp,
                                                fontWeight = FontWeight.SemiBold,
                                                color = if (isSelected) Color.White else DeepNavy
                                            )
                                            if (isSelected) {
                                                Icon(
                                                    imageVector = Icons.Default.Check,
                                                    contentDescription = "Selected",
                                                    tint = Color.White,
                                                    modifier = Modifier.size(16.dp)
                                                )
                                            } else {
                                                Text(
                                                    text = "Open",
                                                    fontSize = 11.sp,
                                                    color = PrimaryIndigo,
                                                    fontWeight = FontWeight.Bold
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

            // Step 3: Issue Description / Notes
            item {
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, CardBorderStroke, RoundedCornerShape(20.dp))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "3. Issue Note / Address Description",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = DeepNavy
                        )
                        Spacer(modifier = Modifier.height(10.dp))

                        OutlinedTextField(
                            value = issueNote,
                            onValueChange = { issueNote = it },
                            placeholder = {
                                Text(
                                    "Describe the issue or landmark (e.g. Near Saheliyon Ki Bari, 2nd floor, switchboard making buzzing noise)...",
                                    fontSize = 12.sp
                                )
                            },
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(90.dp)
                        )
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }
}

