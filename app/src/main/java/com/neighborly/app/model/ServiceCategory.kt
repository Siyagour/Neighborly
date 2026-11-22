package com.neighborly.app.model

enum class ServiceCategory(val displayName: String, val icon: String) {
    ALL("All", "⭐"),
    ELECTRICIAN("Electrician", "⚡"),
    PLUMBER("Plumber", "🔧"),
    TUTOR("Tutor", "📚"),
    CARPENTER("Carpenter", "🪚"),
    AC_APPLIANCE("AC & Appliance", "❄️"),
    PAINTER("Painter", "🎨"),
    CLEANING("Home Cleaning", "🧹"),
    HANDYMAN("Handyman", "🛠️");

    companion object {
        fun fromString(value: String): ServiceCategory {
            return entries.firstOrNull {
                it.name.equals(value, ignoreCase = true) ||
                        it.displayName.equals(value, ignoreCase = true) ||
                        value.contains(it.displayName, ignoreCase = true)
            } ?: ALL
        }
    }
}
