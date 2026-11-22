package com.neighborly.app.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.neighborly.app.data.local.entity.ProviderEntity
import com.neighborly.app.data.local.entity.UserAccountEntity
import com.neighborly.app.data.repository.MarketplaceRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val repository: MarketplaceRepository,
    initialUser: UserAccountEntity
) : ViewModel() {

    private val _currentUser = MutableStateFlow(initialUser)
    val currentUser: StateFlow<UserAccountEntity> = _currentUser.asStateFlow()

    val providerProfile: StateFlow<ProviderEntity?> = _currentUser.flatMapLatest { user ->
        if (user.role == "PROVIDER" && user.providerId != null) {
            repository.getProvider(user.providerId)
        } else {
            flowOf(null)
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    fun setUser(user: UserAccountEntity) {
        _currentUser.value = user
    }

    fun updateProfile(
        name: String,
        phone: String,
        address: String,
        bio: String? = null,
        visitingFee: Int? = null,
        experience: Int? = null,
        category: String? = null,
        onDone: () -> Unit
    ) {
        viewModelScope.launch {
            val updatedUser = _currentUser.value.copy(
                name = name.trim(),
                phone = phone.trim(),
                address = address.trim()
            )
            repository.updateUserProfile(
                user = updatedUser,
                providerBio = bio,
                providerFee = visitingFee,
                providerExp = experience,
                providerCategory = category
            )
            _currentUser.value = updatedUser
            onDone()
        }
    }
}

