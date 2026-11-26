package com.example.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.ServiceProRepository
import com.example.model.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

enum class AppScreen {
    LOGIN,
    REGISTER,
    REGISTRATION_SUCCESS,
    HOME,
    ALL_SERVICES,
    SERVICE_DETAIL,
    CHOOSE_WORKER,
    ADD_MEDIA,
    BOOKING_CONFIRMED,
    LIVE_TRACKING,
    INVOICE,
    PRICING_RULE,
    WORKER_DASHBOARD,
    ROOM_KHOJ,
    MY_BOOKINGS,
    CONTACT_SUPPORT,
    PROFILE
}

data class UiState(
    val currentScreen: AppScreen = AppScreen.LOGIN,
    val previousScreen: AppScreen = AppScreen.LOGIN,
    val language: Language = Language.EN,
    val userRole: UserRole = UserRole.CUSTOMER,
    val isLoggedIn: Boolean = false,
    val userName: String = "Priyanka",
    val userPhone: String = "+91 9205161464",
    val selectedArea: String = "Marhaura Station Road",
    val userAccount: UserAccount = UserAccount(
        userId = "SP-PRIYANKA-9205",
        fullName = "Priyanka",
        phone = "+91 9205161464",
        email = "priyanka@gmail.com",
        role = UserRole.CUSTOMER,
        aadhaarNumber = "4829-1920-5161",
        experienceYears = 3,
        locationArea = "Marhaura Station Road"
    ),
    // Worker / Provider states
    val isWorkerOnline: Boolean = true,
    val isPublicProfileVisible: Boolean = true,
    val workerStatus: WorkerAvailability = WorkerAvailability.ONLINE,
    val workerDailyEarnings: Int = 2450,
    val workerJobsToday: Int = 5,
    // Active selections
    val selectedService: ServiceItem? = ServiceProRepository.services.first(),
    val selectedWorker: Worker? = ServiceProRepository.workers.first(),
    val activeBooking: Booking? = ServiceProRepository.defaultBookings.first(),
    val selectedListing: RoomKhojListing? = ServiceProRepository.roomKhojListings.first(),
    // Pricing calculator custom input
    val customPricingInput: Int = 1000,
    // Add Media states
    val uploadedPhotoCount: Int = 3,
    val hasUploadedVideo: Boolean = true,
    // Filter for RoomKhoj
    val roomKhojFilter: PropertyType? = null,
    val isStudentSelected: Boolean = false,
    val searchQuery: String = "",
    val isLocationPickerVisible: Boolean = false
)

class MainViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    val bookings = ServiceProRepository.bookings

    fun toggleLanguage() {
        val next = if (_uiState.value.language == Language.EN) Language.HI else Language.EN
        _uiState.value = _uiState.value.copy(language = next)
    }

    fun setLanguage(language: Language) {
        _uiState.value = _uiState.value.copy(language = language)
    }

    fun navigateTo(screen: AppScreen) {
        _uiState.value = _uiState.value.copy(
            previousScreen = _uiState.value.currentScreen,
            currentScreen = screen
        )
    }

    fun navigateBack() {
        _uiState.value = _uiState.value.copy(
            currentScreen = _uiState.value.previousScreen
        )
    }

    fun selectRole(role: UserRole) {
        _uiState.value = _uiState.value.copy(
            userRole = role,
            currentScreen = if (role == UserRole.WORKER) AppScreen.WORKER_DASHBOARD else AppScreen.HOME
        )
    }

    fun selectService(service: ServiceItem) {
        _uiState.value = _uiState.value.copy(
            selectedService = service,
            currentScreen = AppScreen.SERVICE_DETAIL
        )
    }

    fun selectWorker(worker: Worker) {
        _uiState.value = _uiState.value.copy(
            selectedWorker = worker
        )
    }

    fun setCustomPricing(amount: Int) {
        _uiState.value = _uiState.value.copy(customPricingInput = amount)
    }

    fun toggleWorkerOnline(isOnline: Boolean) {
        _uiState.value = _uiState.value.copy(
            isWorkerOnline = isOnline,
            workerStatus = if (isOnline) WorkerAvailability.ONLINE else WorkerAvailability.OFFLINE
        )
    }

    fun togglePublicProfile(isVisible: Boolean) {
        _uiState.value = _uiState.value.copy(isPublicProfileVisible = isVisible)
    }

    fun setWorkerStatus(status: WorkerAvailability) {
        _uiState.value = _uiState.value.copy(
            workerStatus = status,
            isWorkerOnline = (status != WorkerAvailability.OFFLINE)
        )
    }

    fun confirmBooking(worker: Worker, service: ServiceItem, address: String = "123 Oak St, Marhaura Bazar") {
        val pricing = ServiceProRepository.calculateWorkerRule(worker.hourlyRate)
        val newBooking = Booking(
            id = "b_${System.currentTimeMillis()}",
            serviceName = service.nameEn,
            workerName = worker.name,
            workerPhone = worker.phone,
            dateTimeStr = "Tomorrow • 10:30 AM",
            address = address,
            workerCharge = pricing.workerGetsPura,
            gstAmount = pricing.gstAmount,
            totalAmount = pricing.customerTotal,
            status = BookingStatus.ON_THE_WAY,
            otp = "4582",
            etaMinutes = 8
        )
        ServiceProRepository.addBooking(newBooking)
        _uiState.value = _uiState.value.copy(
            activeBooking = newBooking,
            selectedWorker = worker,
            currentScreen = AppScreen.BOOKING_CONFIRMED
        )
    }

    fun cancelActiveBooking() {
        _uiState.value.activeBooking?.let {
            ServiceProRepository.cancelBooking(it.id)
            _uiState.value = _uiState.value.copy(
                activeBooking = it.copy(status = BookingStatus.CANCELLED)
            )
        }
    }

    fun setRoomKhojFilter(type: PropertyType?) {
        _uiState.value = _uiState.value.copy(roomKhojFilter = type)
    }

    fun toggleStudentDiscount(isStudent: Boolean) {
        _uiState.value = _uiState.value.copy(isStudentSelected = isStudent)
    }

    fun setSearchQuery(query: String) {
        _uiState.value = _uiState.value.copy(searchQuery = query)
    }

    fun logout() {
        _uiState.value = _uiState.value.copy(
            isLoggedIn = false,
            currentScreen = AppScreen.LOGIN
        )
    }

    fun showLocationPicker(show: Boolean) {
        _uiState.value = _uiState.value.copy(isLocationPickerVisible = show)
    }

    fun selectArea(area: String) {
        val updatedAcct = _uiState.value.userAccount.copy(locationArea = area)
        _uiState.value = _uiState.value.copy(
            selectedArea = area,
            userAccount = updatedAcct,
            isLocationPickerVisible = false
        )
    }

    fun login(role: UserRole, name: String, phone: String, userId: String = "SP-PRIYANKA-9205", area: String = "Marhaura Station Road") {
        val currentAcct = _uiState.value.userAccount.copy(
            userId = userId,
            fullName = name,
            phone = phone,
            role = role,
            locationArea = area
        )
        _uiState.value = _uiState.value.copy(
            isLoggedIn = true,
            userRole = role,
            userName = name,
            userPhone = phone,
            selectedArea = area,
            userAccount = currentAcct,
            isLocationPickerVisible = true, // Prompt location selection right after login
            currentScreen = if (role == UserRole.WORKER) AppScreen.WORKER_DASHBOARD else AppScreen.HOME
        )
    }

    fun registerUser(account: UserAccount) {
        _uiState.value = _uiState.value.copy(
            isLoggedIn = true,
            userRole = account.role,
            userName = account.fullName,
            userPhone = account.phone,
            selectedArea = account.locationArea,
            userAccount = account,
            currentScreen = AppScreen.REGISTRATION_SUCCESS
        )
    }

    fun updateUserAccount(account: UserAccount) {
        _uiState.value = _uiState.value.copy(
            userAccount = account,
            userName = account.fullName,
            userPhone = account.phone,
            selectedArea = account.locationArea
        )
    }

    fun completeRemainingPayment(bookingId: String, method: String) {
        ServiceProRepository.updateBookingPayment(bookingId, method)
        val stage = if (method.uppercase() == "ONLINE") PaymentStage.FULLY_PAID_ONLINE else PaymentStage.FULLY_PAID_CASH
        _uiState.value.activeBooking?.let { active ->
            if (active.id == bookingId) {
                _uiState.value = _uiState.value.copy(
                    activeBooking = active.copy(
                        paymentStage = stage,
                        remainingBalance = 0,
                        remainingPaymentMethod = if (method.uppercase() == "ONLINE") "Online Paid (UPI/Card)" else "Cash Handed to Worker",
                        status = BookingStatus.COMPLETED
                    )
                )
            }
        }
    }

    fun addPhotoSample() {
        val current = _uiState.value.uploadedPhotoCount
        val next = if (current >= 3) 1 else current + 1
        _uiState.value = _uiState.value.copy(uploadedPhotoCount = next)
    }
}
