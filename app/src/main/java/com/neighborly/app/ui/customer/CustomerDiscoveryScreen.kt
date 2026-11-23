package com.neighborly.app.ui.customer

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.rounded.NearMe
import androidx.compose.material.icons.rounded.SearchOff
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
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
import com.neighborly.app.model.ServiceCategory
import com.neighborly.app.ui.components.ProviderCard
import com.neighborly.app.ui.theme.AmberStar
import com.neighborly.app.ui.theme.CardBorderStroke
import com.neighborly.app.ui.theme.DeepNavy
import com.neighborly.app.ui.theme.PrimaryIndigo
import com.neighborly.app.ui.theme.SoftLavender
import com.neighborly.app.ui.theme.TextSecondary

@Composable
fun CustomerDiscoveryScreen(
    viewModel: CustomerViewModel,
    onProviderClick: (Long) -> Unit,
    onBookClick: (Long) -> Unit,
    onNavigateToMyBookings: () -> Unit
) {
    val providers by viewModel.filteredProviders.collectAsState()
    val filter by viewModel.filter.collectAsState()
    val bookings by viewModel.customerBookings.collectAsState()
    val activeBookingsCount = bookings.count { !it.status.equals("Completed", true) && !it.status.equals("Cancelled", true) && !it.status.equals("Declined", true) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF9F9FE))
    ) {
        // Search bar & My Bookings Shortcut
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = filter.searchQuery,
                onValueChange = { viewModel.setSearchQuery(it) },
                placeholder = { Text("Search electrician, plumber, tutor...", fontSize = 13.sp) },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        tint = PrimaryIndigo,
                        modifier = Modifier.size(20.dp)
                    )
                },
                trailingIcon = {
                    if (filter.searchQuery.isNotEmpty()) {
                        IconButton(onClick = { viewModel.setSearchQuery("") }) {
                            Icon(imageVector = Icons.Default.Close, contentDescription = "Clear", modifier = Modifier.size(18.dp))
                        }
                    }
                },
                singleLine = true,
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.weight(1f)
            )

            Spacer(modifier = Modifier.width(10.dp))

            // My Bookings Icon with badge
            BadgedBox(
                badge = {
                    if (activeBookingsCount > 0) {
                        Badge(containerColor = PrimaryIndigo) {
                            Text(text = activeBookingsCount.toString(), color = Color.White)
                        }
                    }
                },
                modifier = Modifier
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.White)
                    .border(1.dp, CardBorderStroke, RoundedCornerShape(16.dp))
                    .clickable { onNavigateToMyBookings() }
                    .padding(12.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.BookmarkBorder,
                    contentDescription = "My Bookings",
                    tint = DeepNavy,
                    modifier = Modifier.size(22.dp)
                )
            }
        }

        // Category Filter Chips
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(ServiceCategory.entries.toTypedArray()) { category ->
                val isSelected = filter.category == category
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(14.dp))
                        .background(if (isSelected) PrimaryIndigo else Color.White)
                        .border(
                            1.dp,
                            if (isSelected) PrimaryIndigo else CardBorderStroke,
                            RoundedCornerShape(14.dp)
                        )
                        .clickable { viewModel.setCategory(category) }
                        .padding(horizontal = 14.dp, vertical = 8.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            imageVector = category.icon,
                            contentDescription = category.displayName,
                            tint = if (isSelected) Color.White else PrimaryIndigo,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = category.displayName,
                            fontSize = 13.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            color = if (isSelected) Color.White else DeepNavy
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Distance & Rating Filter Pills
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Distance < 2 km
            val is2km = filter.maxDistanceKm == 2.0f
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(if (is2km) DeepNavy else SoftLavender)
                    .clickable { viewModel.toggleDistanceFilter(2.0f) }
                    .padding(horizontal = 10.dp, vertical = 6.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Rounded.NearMe,
                        contentDescription = null,
                        tint = if (is2km) Color.White else PrimaryIndigo,
                        modifier = Modifier.size(12.dp)
                    )
                    Text(
                        text = "< 2 km",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = if (is2km) Color.White else DeepNavy
                    )
                }
            }

            // Distance < 5 km
            val is5km = filter.maxDistanceKm == 5.0f
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(if (is5km) DeepNavy else SoftLavender)
                    .clickable { viewModel.toggleDistanceFilter(5.0f) }
                    .padding(horizontal = 10.dp, vertical = 6.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Rounded.NearMe,
                        contentDescription = null,
                        tint = if (is5km) Color.White else PrimaryIndigo,
                        modifier = Modifier.size(12.dp)
                    )
                    Text(
                        text = "< 5 km",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = if (is5km) Color.White else DeepNavy
                    )
                }
            }

            // Rating 4.8+
            val isTopRated = filter.minRating == 4.8f
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(if (isTopRated) AmberStar else SoftLavender)
                    .clickable { viewModel.toggleRatingFilter(4.8f) }
                    .padding(horizontal = 10.dp, vertical = 6.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Star,
                        contentDescription = null,
                        tint = if (isTopRated) Color.White else AmberStar,
                        modifier = Modifier.size(13.dp)
                    )
                    Text(
                        text = "4.8+ Top Rated",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = if (isTopRated) Color.White else DeepNavy
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Providers List
        if (providers.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Rounded.SearchOff,
                        contentDescription = null,
                        tint = TextSecondary,
                        modifier = Modifier.size(44.dp)
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "No professionals found",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = DeepNavy
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Try clearing filters or searching for another skill.",
                        fontSize = 13.sp,
                        color = TextSecondary
                    )
                }
            }
        } else {
            LazyColumn(
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(providers, key = { it.id }) { provider ->
                    ProviderCard(
                        provider = provider,
                        onClick = { onProviderClick(provider.id) },
                        onBookClick = { onBookClick(provider.id) }
                    )
                }
            }
        }
    }
}

