package com.neighborly.app.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.neighborly.app.data.local.entity.UserAccountEntity
import com.neighborly.app.data.repository.MarketplaceRepository
import com.neighborly.app.model.ServiceCategory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

enum class AuthMode {
    SIGN_IN,
    CREATE_ACCOUNT
}

data class AuthUiState(
    val selectedRole: String = "USER", // "USER" or "PROVIDER"
    val authMode: AuthMode = AuthMode.SIGN_IN,

    // Sign In fields
    val loginIdentifier: String = "vikas@neighborly.in",
    val loginPassword: String = "password123",

    // Create Account fields
    val name: String = "",
    val phone: String = "",
    val email: String = "",
    val registerPassword: String = "",
    val address: String = "Udaipur, RJ",

    // Provider specific fields
    val category: String = ServiceCategory.ELECTRICIAN.displayName,
    val visitingFeeText: String = "199",
    val experienceYearsText: String = "5",
    val bio: String = "",

    val errorMessage: String? = null,
    val isLoading: Boolean = false
)

class AuthViewModel(private val repository: MarketplaceRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    fun setRole(role: String) {
        _uiState.update {
            val defaultId = if (role == "USER") "vikas@neighborly.in" else "rajesh@neighborly.in"
            it.copy(
                selectedRole = role,
                loginIdentifier = defaultId,
                errorMessage = null
            )
        }
    }

    fun setAuthMode(mode: AuthMode) {
        _uiState.update { it.copy(authMode = mode, errorMessage = null) }
    }

    fun onLoginIdentifierChange(v: String) = _uiState.update { it.copy(loginIdentifier = v, errorMessage = null) }
    fun onLoginPasswordChange(v: String) = _uiState.update { it.copy(loginPassword = v, errorMessage = null) }

    fun onNameChange(v: String) = _uiState.update { it.copy(name = v, errorMessage = null) }
    fun onPhoneChange(v: String) = _uiState.update { it.copy(phone = v, errorMessage = null) }
    fun onEmailChange(v: String) = _uiState.update { it.copy(email = v, errorMessage = null) }
    fun onRegisterPasswordChange(v: String) = _uiState.update { it.copy(registerPassword = v, errorMessage = null) }
    fun onAddressChange(v: String) = _uiState.update { it.copy(address = v, errorMessage = null) }

    fun onCategoryChange(v: String) = _uiState.update { it.copy(category = v) }
    fun onVisitingFeeChange(v: String) = _uiState.update { it.copy(visitingFeeText = v) }
    fun onExperienceChange(v: String) = _uiState.update { it.copy(experienceYearsText = v) }
    fun onBioChange(v: String) = _uiState.update { it.copy(bio = v) }

    fun fillDemoCredentials(role: String) {
        if (role == "USER") {
            _uiState.update {
                it.copy(
                    selectedRole = "USER",
                    authMode = AuthMode.SIGN_IN,
                    loginIdentifier = "vikas@neighborly.in",
                    loginPassword = "password123",
                    errorMessage = null
                )
            }
        } else {
            _uiState.update {
                it.copy(
                    selectedRole = "PROVIDER",
                    authMode = AuthMode.SIGN_IN,
                    loginIdentifier = "rajesh@neighborly.in",
                    loginPassword = "password123",
                    errorMessage = null
                )
            }
        }
    }

    fun signIn(onSuccess: (UserAccountEntity) -> Unit) {
        val s = _uiState.value
        if (s.loginIdentifier.isBlank() || s.loginPassword.isBlank()) {
            _uiState.update { it.copy(errorMessage = "Please enter your email/phone and password.") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            val res = repository.authenticateUser(s.loginIdentifier, s.loginPassword)
            _uiState.update { it.copy(isLoading = false) }

            res.fold(
                onSuccess = { user ->
                    if (user.role != s.selectedRole) {
                        _uiState.update {
                            it.copy(errorMessage = "This account is registered as ${if (user.role == "USER") "User" else "Service Provider"}. Please toggle role above.")
                        }
                    } else {
                        onSuccess(user)
                    }
                },
                onFailure = { err ->
                    _uiState.update { it.copy(errorMessage = err.message ?: "Authentication failed.") }
                }
            )
        }
    }

    fun createAccount(onSuccess: (UserAccountEntity) -> Unit) {
        val s = _uiState.value
        if (s.name.isBlank() || s.phone.isBlank() || s.email.isBlank() || s.registerPassword.isBlank()) {
            _uiState.update { it.copy(errorMessage = "Please fill in all required fields.") }
            return
        }
        if (s.registerPassword.length < 6) {
            _uiState.update { it.copy(errorMessage = "Password must be at least 6 characters.") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            val res = if (s.selectedRole == "USER") {
                repository.registerCustomer(
                    name = s.name,
                    phone = s.phone,
                    email = s.email,
                    password = s.registerPassword,
                    address = s.address
                )
            } else {
                val fee = s.visitingFeeText.toIntOrNull() ?: 199
                val exp = s.experienceYearsText.toIntOrNull() ?: 3
                repository.registerProvider(
                    name = s.name,
                    phone = s.phone,
                    email = s.email,
                    password = s.registerPassword,
                    address = s.address,
                    category = s.category,
                    visitingFee = fee,
                    experience = exp,
                    bio = s.bio
                )
            }

            _uiState.update { it.copy(isLoading = false) }

            res.fold(
                onSuccess = { user -> onSuccess(user) },
                onFailure = { err ->
                    _uiState.update { it.copy(errorMessage = err.message ?: "Account registration failed.") }
                }
            )
        }
    }
}
