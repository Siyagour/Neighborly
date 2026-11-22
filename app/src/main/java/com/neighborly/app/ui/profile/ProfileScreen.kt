package com.neighborly.app.ui.profile

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.neighborly.app.model.ServiceCategory
import com.neighborly.app.ui.components.InitialsAvatar
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
fun ProfileScreen(
    viewModel: ProfileViewModel,
    onBackClick: () -> Unit,
    onLogoutClick: () -> Unit
) {
    val user by viewModel.currentUser.collectAsState()
    val provider by viewModel.providerProfile.collectAsState()

    var showEditDialog by remember { mutableStateOf(false) }

    val initials = user.name.split(" ")
        .filter { it.isNotBlank() }
        .take(2)
        .map { it.first().uppercase() }
        .joinToString("")
        .ifEmpty { "ME" }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Profile & Account", fontWeight = FontWeight.Bold, color = DeepNavy) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = DeepNavy)
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
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Profile Card
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, CardBorderStroke, RoundedCornerShape(20.dp))
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    InitialsAvatar(
                        initials = initials,
                        colorHex = provider?.avatarColorHex ?: "#5B5FEF",
                        size = 72.dp,
                        fontSize = 24
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = user.name,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = DeepNavy
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    // Role Badge
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(if (user.role == "USER") SoftLavender else EmeraldLight)
                            .padding(horizontal = 12.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = if (user.role == "USER") "👤 Customer Account" else "🛠️ Service Provider Account",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (user.role == "USER") PrimaryIndigo else EmeraldGreen
                        )
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    // Account Details Rows
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(14.dp))
                            .background(SoftLavender)
                            .padding(14.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Email, contentDescription = null, tint = PrimaryIndigo, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(text = user.email, fontSize = 13.sp, color = TextPrimary)
                        }
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Phone, contentDescription = null, tint = PrimaryIndigo, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(text = user.phone, fontSize = 13.sp, color = TextPrimary)
                        }
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Home, contentDescription = null, tint = PrimaryIndigo, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(text = user.address, fontSize = 13.sp, color = TextPrimary)
                        }
                    }
                }
            }

            // If Service Provider: Provider Details Card
            if (user.role == "PROVIDER" && provider != null) {
                val p = provider!!
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, CardBorderStroke, RoundedCornerShape(20.dp))
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Text(
                            text = "🛠️ Provider Marketplace Listing",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = DeepNavy
                        )
                        Spacer(modifier = Modifier.height(10.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text("Trade Category", fontSize = 11.sp, color = TextSecondary)
                                Text(p.category, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = DeepNavy)
                            }
                            Column {
                                Text("Visiting Fee", fontSize = 11.sp, color = TextSecondary)
                                Text("₹${p.visitingFeeInr}", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = PrimaryIndigo)
                            }
                            Column {
                                Text("Experience", fontSize = 11.sp, color = TextSecondary)
                                Text("${p.experienceYears} yrs", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = DeepNavy)
                            }
                        }

                        if (p.bio.isNotBlank()) {
                            Spacer(modifier = Modifier.height(12.dp))
                            Text("Bio / Description", fontSize = 11.sp, color = TextSecondary)
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(p.bio, fontSize = 12.sp, color = TextPrimary)
                        }
                    }
                }
            }

            // Edit Profile Button
            Button(
                onClick = { showEditDialog = true },
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryIndigo),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
            ) {
                Icon(Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("Edit Profile Information", fontWeight = FontWeight.Bold, fontSize = 14.sp)
            }

            // Log Out Button
            OutlinedButton(
                onClick = onLogoutClick,
                colors = ButtonDefaults.outlinedButtonColors(contentColor = CrimsonRed),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .border(1.dp, CrimsonLight, RoundedCornerShape(14.dp))
            ) {
                Icon(Icons.Default.ExitToApp, contentDescription = null, tint = CrimsonRed, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("Log Out", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = CrimsonRed)
            }
        }
    }

    // Edit Profile Modal Dialog
    if (showEditDialog) {
        var editName by remember { mutableStateOf(user.name) }
        var editPhone by remember { mutableStateOf(user.phone) }
        var editAddress by remember { mutableStateOf(user.address) }

        var editCategory by remember { mutableStateOf(provider?.category ?: ServiceCategory.ELECTRICIAN.displayName) }
        var editFee by remember { mutableStateOf(provider?.visitingFeeInr?.toString() ?: "199") }
        var editExp by remember { mutableStateOf(provider?.experienceYears?.toString() ?: "5") }
        var editBio by remember { mutableStateOf(provider?.bio ?: "") }
        var categoryDropdownOpen by remember { mutableStateOf(false) }

        val categories = ServiceCategory.entries.filter { it != ServiceCategory.ALL }

        Dialog(onDismissRequest = { showEditDialog = false }) {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        text = "Edit Profile",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = DeepNavy
                    )

                    OutlinedTextField(
                        value = editName,
                        onValueChange = { editName = it },
                        label = { Text("Name") },
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = editPhone,
                        onValueChange = { editPhone = it },
                        label = { Text("Phone") },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = editAddress,
                        onValueChange = { editAddress = it },
                        label = { Text("Address") },
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    )

                    if (user.role == "PROVIDER") {
                        ExposedDropdownMenuBox(
                            expanded = categoryDropdownOpen,
                            onExpandedChange = { categoryDropdownOpen = it }
                        ) {
                            OutlinedTextField(
                                value = editCategory,
                                onValueChange = {},
                                readOnly = true,
                                label = { Text("Trade Category") },
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = categoryDropdownOpen) },
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .menuAnchor()
                            )
                            ExposedDropdownMenu(
                                expanded = categoryDropdownOpen,
                                onDismissRequest = { categoryDropdownOpen = false }
                            ) {
                                categories.forEach { cat ->
                                    DropdownMenuItem(
                                        text = { Text("${cat.icon} ${cat.displayName}") },
                                        onClick = {
                                            editCategory = cat.displayName
                                            categoryDropdownOpen = false
                                        }
                                    )
                                }
                            }
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            OutlinedTextField(
                                value = editFee,
                                onValueChange = { editFee = it },
                                label = { Text("Visiting Fee (₹)") },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.weight(1f)
                            )
                            OutlinedTextField(
                                value = editExp,
                                onValueChange = { editExp = it },
                                label = { Text("Exp (Yrs)") },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.weight(1f)
                            )
                        }

                        OutlinedTextField(
                            value = editBio,
                            onValueChange = { editBio = it },
                            label = { Text("Bio / Specialization") },
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        OutlinedButton(
                            onClick = { showEditDialog = false },
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text("Cancel", color = TextSecondary)
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Button(
                            onClick = {
                                viewModel.updateProfile(
                                    name = editName,
                                    phone = editPhone,
                                    address = editAddress,
                                    bio = if (user.role == "PROVIDER") editBio else null,
                                    visitingFee = if (user.role == "PROVIDER") editFee.toIntOrNull() else null,
                                    experience = if (user.role == "PROVIDER") editExp.toIntOrNull() else null,
                                    category = if (user.role == "PROVIDER") editCategory else null,
                                    onDone = { showEditDialog = false }
                                )
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = PrimaryIndigo),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text("Save Changes", color = Color.White, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}

