package com.neighborly.app.data.repository

import com.neighborly.app.data.local.AppDatabase
import com.neighborly.app.data.local.entity.BookingEntity
import com.neighborly.app.data.local.entity.ProviderEntity
import com.neighborly.app.data.local.entity.ProviderSlotEntity
import com.neighborly.app.data.local.entity.ReviewEntity
import com.neighborly.app.data.local.entity.SubServiceEntity
import com.neighborly.app.data.local.entity.UserAccountEntity
import com.neighborly.app.model.BookingStatus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import kotlin.random.Random

class MarketplaceRepository(private val db: AppDatabase) {

    private val userAccountDao = db.userAccountDao()
    private val providerDao = db.providerDao()
    private val slotDao = db.slotDao()
    private val bookingDao = db.bookingDao()
    private val reviewDao = db.reviewDao()

    private val timeSlots = listOf(
        "09:00 AM - 10:00 AM",
        "10:00 AM - 11:00 AM",
        "11:00 AM - 12:00 PM",
        "02:00 PM - 03:00 PM",
        "03:00 PM - 04:00 PM",
        "04:00 PM - 05:00 PM",
        "05:00 PM - 06:00 PM",
        "06:00 PM - 07:00 PM"
    )

    private val avatarColors = listOf(
        "#5B5FEF", "#10B981", "#F59E0B", "#8B5CF6",
        "#EC4899", "#06B6D4", "#3B82F6", "#6366F1"
    )

    // Auth & Accounts
    suspend fun authenticateUser(identifier: String, password: String): Result<UserAccountEntity> = withContext(Dispatchers.IO) {
        val trimmed = identifier.trim()
        val account = userAccountDao.getUserByEmailOrPhone(trimmed)
            ?: return@withContext Result.failure(Exception("No account found with this email or phone."))

        if (account.password != password) {
            return@withContext Result.failure(Exception("Incorrect password. Please try again."))
        }

        Result.success(account)
    }

    suspend fun registerCustomer(
        name: String,
        phone: String,
        email: String,
        password: String,
        address: String
    ): Result<UserAccountEntity> = withContext(Dispatchers.IO) {
        try {
            val user = UserAccountEntity(
                name = name.trim(),
                phone = phone.trim(),
                email = email.trim().lowercase(),
                password = password,
                role = "USER",
                address = address.trim(),
                providerId = null
            )
            val id = userAccountDao.insertUser(user)
            Result.success(user.copy(id = id))
        } catch (e: Exception) {
            Result.failure(Exception("An account with this email or phone number already exists."))
        }
    }

    suspend fun registerProvider(
        name: String,
        phone: String,
        email: String,
        password: String,
        address: String,
        category: String,
        visitingFee: Int,
        experience: Int,
        bio: String
    ): Result<UserAccountEntity> = withContext(Dispatchers.IO) {
        try {
            val initials = name.split(" ")
                .filter { it.isNotBlank() }
                .take(2)
                .map { it.first().uppercase() }
                .joinToString("")
                .ifEmpty { "PR" }

            val randomColor = avatarColors[Random.nextInt(avatarColors.size)]

            // 1. Create ProviderEntity
            val provider = ProviderEntity(
                name = name.trim(),
                category = category,
                initials = initials,
                avatarColorHex = randomColor,
                rating = 5.0f,
                reviewCount = 1,
                distanceKm = 1.0f,
                responseTimeMins = 15,
                isVerified = true,
                isOnline = true,
                experienceYears = experience,
                phone = phone.trim(),
                area = address.trim().ifEmpty { "Udaipur, RJ" },
                bio = bio.ifBlank { "Professional $category in Udaipur offering quality home service and repairs." },
                visitingFeeInr = visitingFee
            )
            val providerId = providerDao.insertProvider(provider)

            // 2. Insert default 8 slots for new provider
            val slots = timeSlots.map { slotTime ->
                ProviderSlotEntity(
                    providerId = providerId,
                    slotDate = "Today",
                    slotTime = slotTime,
                    isBooked = false,
                    isBlockedByProvider = false
                )
            }
            slotDao.insertSlots(slots)

            // 3. Insert default sub-services
            val defaultServices = listOf(
                SubServiceEntity(providerId = providerId, serviceName = "Standard $category Diagnostic & Checkup", minPriceInr = visitingFee, maxPriceInr = visitingFee + 200),
                SubServiceEntity(providerId = providerId, serviceName = "Minor $category Repair Work", minPriceInr = 150, maxPriceInr = 450),
                SubServiceEntity(providerId = providerId, serviceName = "Major $category Installation & Fitting", minPriceInr = 400, maxPriceInr = 950)
            )
            providerDao.insertSubServices(defaultServices)

            // 4. Create UserAccountEntity linked to providerId
            val user = UserAccountEntity(
                name = name.trim(),
                phone = phone.trim(),
                email = email.trim().lowercase(),
                password = password,
                role = "PROVIDER",
                address = address.trim(),
                providerId = providerId
            )
            val userId = userAccountDao.insertUser(user)
            Result.success(user.copy(id = userId))
        } catch (e: Exception) {
            Result.failure(Exception("An account with this email or phone number already exists."))
        }
    }

    fun getUserAccount(id: Long): Flow<UserAccountEntity?> = userAccountDao.getUserById(id)

    suspend fun getUserAccountDirect(id: Long): UserAccountEntity? = userAccountDao.getUserByIdDirect(id)

    suspend fun updateUserProfile(
        user: UserAccountEntity,
        providerBio: String? = null,
        providerFee: Int? = null,
        providerExp: Int? = null,
        providerCategory: String? = null
    ) = withContext(Dispatchers.IO) {
        userAccountDao.updateUser(user)
        if (user.role == "PROVIDER" && user.providerId != null) {
            val pId = user.providerId
            val current = providerDao.getProviderByIdDirect(pId)
            if (current != null) {
                providerDao.updateProviderProfile(
                    id = pId,
                    name = user.name,
                    phone = user.phone,
                    bio = providerBio ?: current.bio,
                    visitingFee = providerFee ?: current.visitingFeeInr,
                    experience = providerExp ?: current.experienceYears,
                    category = providerCategory ?: current.category
                )
            }
        }
    }

    // Providers
    val allProviders: Flow<List<ProviderEntity>> = providerDao.getAllProviders()

    fun getProvider(id: Long): Flow<ProviderEntity?> = providerDao.getProviderById(id)

    suspend fun getProviderDirect(id: Long): ProviderEntity? = providerDao.getProviderByIdDirect(id)

    fun getSubServices(providerId: Long): Flow<List<SubServiceEntity>> = providerDao.getSubServices(providerId)

    fun getSlots(providerId: Long): Flow<List<ProviderSlotEntity>> = slotDao.getSlotsForProvider(providerId)

    fun getAvailableSlots(providerId: Long): Flow<List<ProviderSlotEntity>> = slotDao.getAvailableSlotsForProvider(providerId)

    fun getReviews(providerId: Long): Flow<List<ReviewEntity>> = reviewDao.getReviewsForProvider(providerId)

    suspend fun setProviderOnline(providerId: Long, isOnline: Boolean) = withContext(Dispatchers.IO) {
        providerDao.updateOnlineStatus(providerId, isOnline)
    }

    suspend fun toggleSlotBlocked(slotId: Long, currentBlocked: Boolean) = withContext(Dispatchers.IO) {
        slotDao.updateBlockedStatus(slotId, !currentBlocked)
    }

    // Bookings
    fun getCustomerBookings(phone: String): Flow<List<BookingEntity>> = bookingDao.getBookingsForCustomer(phone)

    fun getProviderBookings(providerId: Long): Flow<List<BookingEntity>> = bookingDao.getBookingsForProvider(providerId)

    fun getBookingById(id: Long): Flow<BookingEntity?> = bookingDao.getBookingById(id)

    suspend fun createBooking(
        providerId: Long,
        slotId: Long,
        slotTime: String,
        customerName: String,
        customerPhone: String,
        subServiceName: String,
        initialQuoteMin: Int,
        initialQuoteMax: Int,
        issueNote: String
    ): Long = withContext(Dispatchers.IO) {
        val startOtp = String.format("%04d", Random.nextInt(1000, 9999))
        val booking = BookingEntity(
            providerId = providerId,
            slotId = slotId,
            customerName = customerName,
            customerPhone = customerPhone,
            subServiceName = subServiceName,
            slotTime = slotTime,
            status = BookingStatus.REQUESTED.label,
            issueNote = issueNote,
            startOtp = startOtp,
            initialQuoteMinInr = initialQuoteMin,
            initialQuoteMaxInr = initialQuoteMax
        )
        val bookingId = bookingDao.insertBooking(booking)
        slotDao.updateBookedStatus(slotId, true)
        bookingId
    }

    suspend fun acceptBooking(bookingId: Long) = withContext(Dispatchers.IO) {
        bookingDao.updateStatus(bookingId, BookingStatus.ACCEPTED.label)
    }

    suspend fun declineBooking(bookingId: Long) = withContext(Dispatchers.IO) {
        val booking = bookingDao.getBookingByIdDirect(bookingId) ?: return@withContext
        bookingDao.updateStatus(bookingId, BookingStatus.DECLINED.label)
        slotDao.updateBookedStatus(booking.slotId, false)
    }

    suspend fun cancelBooking(bookingId: Long) = withContext(Dispatchers.IO) {
        val booking = bookingDao.getBookingByIdDirect(bookingId) ?: return@withContext
        bookingDao.updateStatus(bookingId, BookingStatus.CANCELLED.label)
        slotDao.updateBookedStatus(booking.slotId, false)
    }

    suspend fun verifyStartOtp(bookingId: Long, enteredOtp: String): Boolean = withContext(Dispatchers.IO) {
        val booking = bookingDao.getBookingByIdDirect(bookingId) ?: return@withContext false
        if (booking.startOtp.trim() == enteredOtp.trim()) {
            bookingDao.updateStatus(bookingId, BookingStatus.IN_PROGRESS.label)
            true
        } else {
            false
        }
    }

    suspend fun reviseQuote(bookingId: Long, revisedAmount: Int, reason: String) = withContext(Dispatchers.IO) {
        bookingDao.proposeRevisedQuote(bookingId, revisedAmount, reason)
    }

    suspend fun approveRevisedQuote(bookingId: Long) = withContext(Dispatchers.IO) {
        bookingDao.approveRevisedQuote(bookingId)
    }

    suspend fun completeBooking(bookingId: Long, finalAmount: Int) = withContext(Dispatchers.IO) {
        bookingDao.completeBooking(bookingId, finalAmount)
    }

    suspend fun submitRating(
        bookingId: Long,
        providerId: Long,
        rating: Float,
        reviewComment: String,
        customerName: String
    ) = withContext(Dispatchers.IO) {
        bookingDao.submitRating(bookingId, rating, reviewComment)

        reviewDao.insertReview(
            ReviewEntity(
                providerId = providerId,
                customerName = customerName,
                rating = rating,
                comment = reviewComment,
                dateStr = "Just now"
            )
        )

        val allReviews = reviewDao.getReviewsForProviderDirect(providerId)
        val newReviewCount = allReviews.size
        val avgRating = if (newReviewCount > 0) {
            allReviews.map { it.rating }.average().toFloat()
        } else {
            rating
        }
        val roundedRating = Math.round(avgRating * 10f) / 10f
        providerDao.updateRating(providerId, roundedRating, newReviewCount)
    }
}
