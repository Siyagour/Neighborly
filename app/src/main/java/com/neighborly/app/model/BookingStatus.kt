package com.neighborly.app.model

enum class BookingStatus(val label: String) {
    REQUESTED("Requested"),
    ACCEPTED("Accepted"),
    IN_PROGRESS("In Progress"),
    COMPLETED("Completed"),
    DECLINED("Declined"),
    CANCELLED("Cancelled");

    val isTerminal: Boolean
        get() = this == COMPLETED || this == DECLINED || this == CANCELLED
}

