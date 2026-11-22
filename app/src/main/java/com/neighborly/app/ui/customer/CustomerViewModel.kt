package com.neighborly.app.ui.customer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.neighborly.app.data.local.entity.BookingEntity
import com.neighborly.app.data.local.entity.ProviderEntity
import com.neighborly.app.data.local.entity.ProviderSlotEntity
import com.neighborly.app.data.local.entity.ReviewEntity
import com.neighborly.app.data.local.entity.SubServiceEntity
import com.neighborly.app.data.local.entity.UserAccountEntity
import com.neighborly.app.data.repository.MarketplaceRepository
import com.neighborly.app.model.ServiceCategory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class CustomerUiFilter(
    val category: ServiceCategory = ServiceCategory.ALL,
    val searchQuery: String = "",
    val maxDistanceKm: Float? = null,
    val minRating: Float? = null
)

class CustomerViewModel(
    private val repository: MarketplaceRepository,
    var defaultPhone: String = "9988776655",
    var defaultName: String = "Vikas Jain"
) : ViewModel() {

    private val _currentCustomer = MutableStateFlow<UserAccountEntity?>(null)
    val currentCustomer: StateFlow<UserAccountEntity?> = _currentCustomer.asStateFlow()

    fun setCurrentCustomer(user: UserAccountEntity) {
        _currentCustomer.value = user
    }

    private val _filter = MutableStateFlow(CustomerUiFilter())
    val filter: StateFlow<CustomerUiFilter> = _filter.asStateFlow()

    // Filtered providers
    val filteredProviders: StateFlow<List<ProviderEntity>> = combine(
        repository.allProviders,
        _filter
    ) { providers, filter ->
        providers.filter { provider ->
            val matchesCategory = filter.category == ServiceCategory.ALL ||
                    provider.category.equals(filter.category.displayName, ignoreCase = true) ||
                    provider.category.equals(filter.category.name, ignoreCase = true)
            val matchesSearch = filter.searchQuery.isBlank() ||
                    provider.name.contains(filter.searchQuery, ignoreCase = true) ||
                    provider.category.contains(filter.searchQuery, ignoreCase = true) ||
                    provider.area.contains(filter.searchQuery, ignoreCase = true)
            val matchesDistance = filter.maxDistanceKm == null || provider.distanceKm <= filter.maxDistanceKm
            val matchesRating = filter.minRating == null || provider.rating >= filter.minRating

            matchesCategory && matchesSearch && matchesDistance && matchesRating
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Customer Bookings
    val customerBookings: StateFlow<List<BookingEntity>> = _currentCustomer.flatMapLatest { user ->
        val phone = user?.phone ?: defaultPhone
        repository.getCustomerBookings(phone)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Provider Details state
    private val _selectedProviderId = MutableStateFlow<Long?>(null)
    val selectedProviderId: StateFlow<Long?> = _selectedProviderId.asStateFlow()

    fun selectProvider(id: Long) {
        _selectedProviderId.value = id
    }

    fun setCategory(category: ServiceCategory) {
        _filter.update { it.copy(category = category) }
    }

    fun setSearchQuery(query: String) {
        _filter.update { it.copy(searchQuery = query) }
    }

    fun toggleDistanceFilter(distance: Float) {
        _filter.update {
            it.copy(maxDistanceKm = if (it.maxDistanceKm == distance) null else distance)
        }
    }

    fun toggleRatingFilter(minRating: Float) {
        _filter.update {
            it.copy(minRating = if (it.minRating == minRating) null else minRating)
        }
    }

    fun getSubServices(providerId: Long) = repository.getSubServices(providerId)

    fun getAvailableSlots(providerId: Long) = repository.getAvailableSlots(providerId)

    fun getReviews(providerId: Long) = repository.getReviews(providerId)

    fun bookSlot(
        providerId: Long,
        slotId: Long,
        slotTime: String,
        subServiceName: String,
        initialQuoteMin: Int,
        initialQuoteMax: Int,
        issueNote: String,
        onSuccess: (Long) -> Unit
    ) {
        val custName = _currentCustomer.value?.name ?: defaultName
        val custPhone = _currentCustomer.value?.phone ?: defaultPhone
        viewModelScope.launch {
            val bookingId = repository.createBooking(
                providerId = providerId,
                slotId = slotId,
                slotTime = slotTime,
                customerName = custName,
                customerPhone = custPhone,
                subServiceName = subServiceName,
                initialQuoteMin = initialQuoteMin,
                initialQuoteMax = initialQuoteMax,
                issueNote = issueNote
            )
            onSuccess(bookingId)
        }
    }

    fun cancelBooking(bookingId: Long) {
        viewModelScope.launch {
            repository.cancelBooking(bookingId)
        }
    }

    fun approveRevisedQuote(bookingId: Long) {
        viewModelScope.launch {
            repository.approveRevisedQuote(bookingId)
        }
    }

    fun submitRating(
        bookingId: Long,
        providerId: Long,
        rating: Float,
        comment: String
    ) {
        val custName = _currentCustomer.value?.name ?: defaultName
        viewModelScope.launch {
            repository.submitRating(
                bookingId = bookingId,
                providerId = providerId,
                rating = rating,
                reviewComment = comment,
                customerName = custName
            )
        }
    }
}

