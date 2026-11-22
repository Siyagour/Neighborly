package com.neighborly.app.ui.navigation

sealed class Screen(val route: String) {
    object Auth : Screen("auth")
    object CustomerDiscovery : Screen("customer_discovery")
    object ProviderDetail : Screen("provider_detail/{providerId}") {
        fun createRoute(providerId: Long) = "provider_detail/$providerId"
    }
    object SlotBooking : Screen("slot_booking/{providerId}") {
        fun createRoute(providerId: Long) = "slot_booking/$providerId"
    }
    object MyBookings : Screen("my_bookings")

    object ProviderDashboard : Screen("provider_dashboard")
    object SlotManager : Screen("slot_manager")
    object IncomingRequests : Screen("incoming_requests")

    object Profile : Screen("profile")
}
