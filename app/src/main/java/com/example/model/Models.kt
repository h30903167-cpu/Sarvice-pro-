package com.example.model

enum class Language(val code: String, val displayName: String) {
    EN("en", "English"),
    HI("hi", "हिंदी")
}

enum class UserRole {
    CUSTOMER,
    WORKER
}

enum class WorkerAvailability {
    ONLINE,
    OFFLINE,
    WORKING
}

data class ServiceItem(
    val id: String,
    val nameEn: String,
    val nameHi: String,
    val basePrice: Int,
    val category: String,
    val descriptionEn: String,
    val descriptionHi: String,
    val iconName: String,
    val tag: String = "POPULAR"
)

data class WorkerReview(
    val id: String,
    val authorName: String,
    val rating: Int,
    val dateStr: String,
    val comment: String,
    val verifiedBooking: Boolean = true
)

data class Worker(
    val id: String,
    val name: String,
    val serviceCategory: String,
    val rating: Double,
    val jobsCompleted: Int,
    val hourlyRate: Int,
    val experienceYears: Int,
    val isOnline: Boolean = true,
    val status: WorkerAvailability = WorkerAvailability.ONLINE,
    val phone: String = "+91 9205161464",
    val tags: List<String> = listOf("Fast Response", "Verified Pro", "On-site today"),
    val vehicleNumber: String = "BR 04 M 1464",
    val distanceKm: Double = 1.2,
    val photoPath: String = "",
    val licenseNumber: String = "BR-04-CR-2024-9205",
    val reviews: List<WorkerReview> = listOf(
        WorkerReview("r1", "Priyanka S.", 5, "Yesterday", "Bahut badhiya kaam kiya! Station road Marhaura par 20 min me pahuch gaye.", true),
        WorkerReview("r2", "Amit Kumar", 5, "3 days ago", "Proper bill mila, worker ko 100% full wage diya aur GST owner account me gaya.", true),
        WorkerReview("r3", "Rajeev Verma", 4, "1 week ago", "Professional and clean work. Fully satisfied with 50% split payment.", true)
    )
)

enum class PaymentStage {
    UPFRONT_50_PAID,
    FULLY_PAID_ONLINE,
    FULLY_PAID_CASH
}

data class UserAccount(
    val userId: String,
    val fullName: String,
    val phone: String,
    val email: String = "",
    val role: UserRole = UserRole.CUSTOMER,
    val documentType: String = "Aadhaar Card",
    val documentNumber: String = "",
    val aadhaarNumber: String = "4829-1920-5161",
    val experienceYears: Int = 3,
    val locationArea: String = "Marhaura Station Road",
    val referralCode: String = "SP-MARHAURA-50",
    val photoUrl: String = "",
    val isAadhaarVerified: Boolean = true,
    val isPhotoVerified: Boolean = true,
    val walletBalance: Int = 150
)

data class Booking(
    val id: String,
    val serviceName: String,
    val workerName: String,
    val workerPhone: String = "+91 9205161464",
    val dateTimeStr: String,
    val address: String,
    val workerCharge: Int,
    val gstAmount: Int,
    val totalAmount: Int,
    val advancePaidOnline: Int = totalAmount / 2, // 50% paid online at booking
    val remainingBalance: Int = totalAmount - (totalAmount / 2), // 50% remaining
    val remainingPaymentMethod: String = "Cash / Online on Completion", // Customer can pay cash or online
    val gstDeductedFromAdvance: Int = gstAmount, // Full GST deducted from the 50% advance
    val workerAdvanceShare: Int = (totalAmount / 2) - gstAmount, // Advance transferred to worker
    val status: BookingStatus,
    val otp: String = "4582",
    val etaMinutes: Int = 8,
    val userRole: UserRole = UserRole.CUSTOMER,
    val paymentStage: PaymentStage = PaymentStage.UPFRONT_50_PAID
)

enum class BookingStatus {
    ACCEPTED,
    ON_THE_WAY,
    ARRIVED,
    COMPLETED,
    CANCELLED
}

enum class PropertyType {
    ROOM,
    HOME,
    GODOWN_WAREHOUSE,
    SHOP_MALL
}

data class RoomKhojListing(
    val id: String,
    val title: String,
    val titleHi: String,
    val propertyType: PropertyType,
    val location: String,
    val monthlyRent: Int,
    val gstFix: Int, // ₹5 for Room, ₹80 for Mall, 0 for student
    val isStudentFreeGst: Boolean = true,
    val malikName: String = "Malik Ji (Owner)",
    val malikPhone: String = "+91 9205161464",
    val areaSqFt: Int,
    val features: List<String>,
    val verified: Boolean = true
)

data class TodayJobActivity(
    val id: String,
    val title: String,
    val time: String,
    val workerEarning: Int,
    val gstOwner: Int,
    val totalPaid: Int,
    val isCompleted: Boolean = true
)

data class EmergencyService(
    val id: String,
    val titleEn: String,
    val titleHi: String,
    val etaMinutes: Int,
    val baseRate: Int,
    val descEn: String,
    val descHi: String,
    val iconType: String = "ALERT"
)

data class ServiceNotification(
    val id: String,
    val title: String,
    val message: String,
    val time: String,
    val isRead: Boolean = false,
    val type: String = "PROMO"
)

data class ChatMessage(
    val id: String,
    val senderName: String,
    val message: String,
    val timestamp: String,
    val isFromUser: Boolean
)
