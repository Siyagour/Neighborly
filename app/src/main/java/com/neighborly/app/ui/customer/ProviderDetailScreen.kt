package com.neighborly.app.ui.customer

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
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
import com.neighborly.app.data.local.entity.ProviderEntity
import com.neighborly.app.ui.components.InitialsAvatar
import com.neighborly.app.ui.components.StarRatingBar
import com.neighborly.app.ui.theme.AmberStar
import com.neighborly.app.ui.theme.CardBorderStroke
import com.neighborly.app.ui.theme.DeepNavy
import com.neighborly.app.ui.theme.EmeraldGreen
import com.neighborly.app.ui.theme.PrimaryIndigo
import com.neighborly.app.ui.theme.SoftLavender
import com.neighborly.app.ui.theme.TextPrimary
import com.neighborly.app.ui.theme.TextSecondary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProviderDetailScreen(
    providerId: Long,
    viewModel: CustomerViewModel,
    onBackClick: () -> Unit,
    onBookClick: (Long) -> Unit
) {
    val providers by viewModel.filteredProviders.collectAsState()
    val provider = providers.firstOrNull { it.id == providerId }

    val subServices by viewModel.getSubServices(providerId).collectAsState(initial = emptyList())
    val reviews by viewModel.getReviews(providerId).collectAsState(initial = emptyList())

    if (provider == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Loading professional details...", color = TextSecondary)
        }
        return
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(provider.name, fontWeight = FontWeight.Bold, color = DeepNavy) },
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
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "₹${provider.visitingFeeInr}",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = DeepNavy
                        )
                        Text(
                            text = "Base Visiting Fee",
                            fontSize = 11.sp,
                            color = TextSecondary
                        )
                    }

                    Button(
                        onClick = { onBookClick(provider.id) },
                        colors = ButtonDefaults.buttonColors(containerColor = PrimaryIndigo),
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier.height(48.dp)
                    ) {
                        Text(
                            text = "Book a Slot",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
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
                // Hero Card
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, CardBorderStroke, RoundedCornerShape(20.dp))
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            InitialsAvatar(
                                initials = provider.initials,
                                colorHex = provider.avatarColorHex,
                                size = 64.dp,
                                fontSize = 22
                            )

                            Spacer(modifier = Modifier.width(16.dp))

                            Column {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = provider.name,
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = DeepNavy
                                    )
                                    if (provider.isVerified) {
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Icon(
                                            imageVector = Icons.Default.CheckCircle,
                                            contentDescription = "Verified",
                                            tint = EmeraldGreen,
                                            modifier = Modifier.size(18.dp)
                                        )
                                    }
                                }

                                Text(
                                    text = "${provider.category} • ${provider.experienceYears} yrs exp",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = PrimaryIndigo
                                )
                                Text(
                                    text = "📍 ${provider.area} (${provider.distanceKm} km away)",
                                    fontSize = 12.sp,
                                    color = TextSecondary
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // 3-Column Metric Row
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(14.dp))
                                .background(SoftLavender)
                                .padding(vertical = 12.dp, horizontal = 14.dp),
                            horizontalArrangement = Arrangement.SpaceAround,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = "⏱ ${provider.responseTimeMins} mins",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = DeepNavy
                                )
                                Text("Response Time", fontSize = 11.sp, color = TextSecondary)
                            }

                            Box(modifier = Modifier.width(1.dp).height(28.dp).background(CardBorderStroke))

                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(imageVector = Icons.Default.Star, contentDescription = null, tint = AmberStar, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(2.dp))
                                    Text("${provider.rating}", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = DeepNavy)
                                }
                                Text("${provider.reviewCount} Reviews", fontSize = 11.sp, color = TextSecondary)
                            }

                            Box(modifier = Modifier.width(1.dp).height(28.dp).background(CardBorderStroke))

                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("₹${provider.visitingFeeInr}", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = DeepNavy)
                                Text("Visiting Fee", fontSize = 11.sp, color = TextSecondary)
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Bio
                        Text(
                            text = provider.bio,
                            fontSize = 13.sp,
                            lineHeight = 19.sp,
                            color = TextPrimary
                        )
                    }
                }
            }

            // Transparent Rate Card
            item {
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, CardBorderStroke, RoundedCornerShape(20.dp))
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Text(
                            text = "📋 Transparent Rate Card",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = DeepNavy
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Visiting fee includes 30 mins diagnostics. Extra labor/parts charged fairly as per estimates below.",
                            fontSize = 12.sp,
                            color = TextSecondary
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        // Base visiting fee row
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(SoftLavender)
                                .padding(horizontal = 12.dp, vertical = 10.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Base Visiting & Diagnostic Fee",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = DeepNavy
                            )
                            Text(
                                text = "₹${provider.visitingFeeInr}",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = PrimaryIndigo
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Sub services list
                        subServices.forEach { service ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp, horizontal = 4.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = service.serviceName,
                                    fontSize = 13.sp,
                                    color = TextPrimary,
                                    modifier = Modifier.weight(1f)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "₹${service.minPriceInr} - ₹${service.maxPriceInr}",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = DeepNavy
                                )
                            }
                            HorizontalDivider(color = CardBorderStroke.copy(alpha = 0.5f))
                        }
                    }
                }
            }

            // Reviews Section
            item {
                Text(
                    text = "Customer Reviews (${reviews.size})",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = DeepNavy
                )
            }

            items(reviews) { review ->
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, CardBorderStroke, RoundedCornerShape(16.dp))
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = review.customerName,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = DeepNavy
                            )
                            Text(
                                text = review.dateStr,
                                fontSize = 11.sp,
                                color = TextSecondary
                            )
                        }

                        Spacer(modifier = Modifier.height(4.dp))

                        StarRatingBar(rating = review.rating, starSize = 14.dp)

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = review.comment,
                            fontSize = 12.sp,
                            lineHeight = 17.sp,
                            color = TextPrimary
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

