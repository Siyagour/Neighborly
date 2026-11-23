package com.neighborly.app.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.MenuBook
import androidx.compose.material.icons.rounded.AcUnit
import androidx.compose.material.icons.rounded.Carpenter
import androidx.compose.material.icons.rounded.Category
import androidx.compose.material.icons.rounded.CleaningServices
import androidx.compose.material.icons.rounded.ElectricalServices
import androidx.compose.material.icons.rounded.FormatPaint
import androidx.compose.material.icons.rounded.Handyman
import androidx.compose.material.icons.rounded.Plumbing
import androidx.compose.ui.graphics.vector.ImageVector

enum class ServiceCategory(val displayName: String) {
    ALL("All"),
    ELECTRICIAN("Electrician"),
    PLUMBER("Plumber"),
    TUTOR("Tutor"),
    CARPENTER("Carpenter"),
    AC_APPLIANCE("AC & Appliance"),
    PAINTER("Painter"),
    CLEANING("Home Cleaning"),
    HANDYMAN("Handyman");

    val icon: ImageVector
        get() = when (this) {
            ALL -> Icons.Rounded.Category
            ELECTRICIAN -> Icons.Rounded.ElectricalServices
            PLUMBER -> Icons.Rounded.Plumbing
            TUTOR -> Icons.AutoMirrored.Rounded.MenuBook
            CARPENTER -> Icons.Rounded.Carpenter
            AC_APPLIANCE -> Icons.Rounded.AcUnit
            PAINTER -> Icons.Rounded.FormatPaint
            CLEANING -> Icons.Rounded.CleaningServices
            HANDYMAN -> Icons.Rounded.Handyman
        }

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
