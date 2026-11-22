package com.neighborly.app.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.neighborly.app.data.local.entity.UserAccountEntity
import com.neighborly.app.data.repository.MarketplaceRepository
import com.neighborly.app.ui.auth.AuthScreen
import com.neighborly.app.ui.auth.AuthViewModel
import com.neighborly.app.ui.components.TopBarWithRoleSwitch
import com.neighborly.app.ui.customer.CustomerDiscoveryScreen
import com.neighborly.app.ui.customer.CustomerViewModel
import com.neighborly.app.ui.customer.MyBookingsScreen
import com.neighborly.app.ui.customer.ProviderDetailScreen
import com.neighborly.app.ui.customer.SlotBookingScreen
import com.neighborly.app.ui.profile.ProfileScreen
import com.neighborly.app.ui.profile.ProfileViewModel
import com.neighborly.app.ui.provider.IncomingRequestsScreen
import com.neighborly.app.ui.provider.ProviderDashboardScreen
import com.neighborly.app.ui.provider.ProviderViewModel
import com.neighborly.app.ui.provider.SlotManagerScreen

@Composable
fun AppNavigation(repository: MarketplaceRepository) {
    val navController = rememberNavController()

    var currentUser by remember { mutableStateOf<UserAccountEntity?>(null) }

    val authViewModel = remember { AuthViewModel(repository) }
    val customerViewModel = remember { CustomerViewModel(repository) }
    val providerViewModel = remember { ProviderViewModel(repository) }

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    // Hide top bar on Auth screen and Profile screen
    val showTopBar = currentUser != null && currentRoute != Screen.Auth.route && currentRoute != Screen.Profile.route

    Scaffold(
        topBar = {
            if (showTopBar) {
                TopBarWithRoleSwitch(
                    currentUser = currentUser,
                    onProfileClick = {
                        navController.navigate(Screen.Profile.route)
                    }
                )
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            NavHost(
                navController = navController,
                startDestination = Screen.Auth.route
            ) {
                // Auth Screen
                composable(Screen.Auth.route) {
                    AuthScreen(
                        viewModel = authViewModel,
                        onLoginSuccess = { user ->
                            currentUser = user
                            if (user.role == "PROVIDER") {
                                providerViewModel.setActiveProviderId(user.providerId ?: 1L)
                                navController.navigate(Screen.ProviderDashboard.route) {
                                    popUpTo(Screen.Auth.route) { inclusive = true }
                                }
                            } else {
                                customerViewModel.setCurrentCustomer(user)
                                navController.navigate(Screen.CustomerDiscovery.route) {
                                    popUpTo(Screen.Auth.route) { inclusive = true }
                                }
                            }
                        }
                    )
                }

                // Customer Routes
                composable(Screen.CustomerDiscovery.route) {
                    CustomerDiscoveryScreen(
                        viewModel = customerViewModel,
                        onProviderClick = { providerId ->
                            navController.navigate(Screen.ProviderDetail.createRoute(providerId))
                        },
                        onBookClick = { providerId ->
                            navController.navigate(Screen.SlotBooking.createRoute(providerId))
                        },
                        onNavigateToMyBookings = {
                            navController.navigate(Screen.MyBookings.route)
                        }
                    )
                }

                composable(
                    route = Screen.ProviderDetail.route,
                    arguments = listOf(navArgument("providerId") { type = NavType.LongType })
                ) { backStackEntry ->
                    val providerId = backStackEntry.arguments?.getLong("providerId") ?: 1L
                    ProviderDetailScreen(
                        providerId = providerId,
                        viewModel = customerViewModel,
                        onBackClick = { navController.popBackStack() },
                        onBookClick = { id ->
                            navController.navigate(Screen.SlotBooking.createRoute(id))
                        }
                    )
                }

                composable(
                    route = Screen.SlotBooking.route,
                    arguments = listOf(navArgument("providerId") { type = NavType.LongType })
                ) { backStackEntry ->
                    val providerId = backStackEntry.arguments?.getLong("providerId") ?: 1L
                    SlotBookingScreen(
                        providerId = providerId,
                        viewModel = customerViewModel,
                        onBackClick = { navController.popBackStack() },
                        onBookingSuccess = {
                            navController.navigate(Screen.MyBookings.route) {
                                popUpTo(Screen.CustomerDiscovery.route)
                            }
                        }
                    )
                }

                composable(Screen.MyBookings.route) {
                    MyBookingsScreen(
                        viewModel = customerViewModel,
                        onBackClick = { navController.popBackStack() }
                    )
                }

                // Provider Routes
                composable(Screen.ProviderDashboard.route) {
                    ProviderDashboardScreen(
                        viewModel = providerViewModel,
                        onNavigateToSlotManager = {
                            navController.navigate(Screen.SlotManager.route)
                        },
                        onNavigateToIncomingRequests = {
                            navController.navigate(Screen.IncomingRequests.route)
                        }
                    )
                }

                composable(Screen.SlotManager.route) {
                    SlotManagerScreen(
                        viewModel = providerViewModel,
                        onBackClick = { navController.popBackStack() }
                    )
                }

                composable(Screen.IncomingRequests.route) {
                    IncomingRequestsScreen(
                        viewModel = providerViewModel,
                        onBackClick = { navController.popBackStack() }
                    )
                }

                // Profile Route (Shared by both Customer and Provider)
                composable(Screen.Profile.route) {
                    val user = currentUser
                    if (user != null) {
                        val profileViewModel = remember(user.id) {
                            ProfileViewModel(repository, user)
                        }
                        ProfileScreen(
                            viewModel = profileViewModel,
                            onBackClick = { navController.popBackStack() },
                            onLogoutClick = {
                                currentUser = null
                                navController.navigate(Screen.Auth.route) {
                                    popUpTo(0) { inclusive = true }
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}
