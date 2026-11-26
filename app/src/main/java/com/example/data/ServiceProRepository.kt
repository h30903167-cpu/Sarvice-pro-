package com.example.data

import com.example.model.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

object ServiceProRepository {
    const val OWNER_PHONE = "+91 9205161464"
    const val HELPLINE_TOLLFREE = "1800-123-SERVICE"

    val services = listOf(
        ServiceItem("1", "Electrician", "इलेक्ट्रीशियन", 299, "Repairs", "Wiring, switchboard, fan, MCB & inverter repairs", "वायरिंग, स्विचबोर्ड, पंखा व इन्वर्टर रिपेयर", "Bolt", "FAST RESPONSE"),
        ServiceItem("2", "Plumber", "प्लंबर", 399, "Repairs", "Leaks, pipe fittings, taps, tanks & blockage clearing", "नल, पाइप फिटिंग, टंकी व लीकेज ठीक करवाएं", "Plumbing", "TOP RATED"),
        ServiceItem("3", "AC Mechanic", "एसी मैकेनिक", 399, "Appliance", "AC repair, gas refilling, jet cleaning & cooling check", "एसी रिपेयर, सर्विसिंग व गैस रीफिल", "AcUnit", "SUMMER SPECIAL"),
        ServiceItem("4", "Carpenter", "बढ़ई (Carpenter)", 299, "Furniture", "Door repair, locks, hinges, modular fittings & polish", "दरवाजे, अलमारी, लॉक रिपेयर व फर्नीचर कार्य", "Carpenter", "EXPERT"),
        ServiceItem("5", "Painter", "पेंटर", 299, "Home Decor", "Wall putty, emulsion, distemper, exterior water-proofing", "दीवार पुट्टी, डिस्टेंपर, वॉटरप्रूफिंग व पेंटिंग", "FormatPaint", "TRENDING"),
        ServiceItem("6", "Cleaning", "क्लीनिंग", 299, "Sanitation", "Deep home cleaning, kitchen & bathroom sanitation", "पूरे घर की डीप क्लीनिंग, किचन व बाथरूम सफाई", "CleaningServices", "DISCOUNT 20%"),
        ServiceItem("7", "Pest Control", "पेस्ट कंट्रोल", 399, "Sanitation", "Termite, cockroach, rodent & mosquito treatments", "दीमक, खटमल, कॉकरोच व कीड़े-मकोड़े से मुक्ति", "PestControl", "CERTIFIED"),
        ServiceItem("8", "Appliance", "होम अप्लायंस", 399, "Appliance", "Washing machine, refrigerator, microwave & geyser", "वॉशिंग मशीन, फ्रिज, गीजर व माइक्रोवेव रिपेयर", "Kitchen", "FAST RESPONSE"),
        ServiceItem("9", "RO Purifier", "आर.ओ वाटर प्यूरीफायर", 299, "Appliance", "Filter change, membrane replacement, leak fixing & TDS test", "फिल्टर, मेम्ब्रेन बदलाव, सर्विस व टीडीएस चेकिंग", "WaterDrop", "POPULAR"),
        ServiceItem("10", "CCTV", "सीसीटीवी कैमरा", 399, "Security", "HD camera installation, DVR setup & remote mobile view", "कैमरा फिटिंग, डीवीआर सेटअप व रिमोट व्यू", "Videocam", "TOP RATED"),
        ServiceItem("11", "Gardening", "माली / बागवानी", 299, "Outdoor", "Lawn mowing, flower pruning, fertilizers & potting", "पौधों की छंटाई, खाद व गमलों की देखभाल", "Yard", "GREEN"),
        ServiceItem("12", "Laundry", "लॉन्ड्री व ड्राई क्लीनिंग", 299, "Daily", "Wash & fold, steam press, shoe cleaning & dry-clean", "कपड़ों की धुलाई, स्टीम प्रेस व ड्राई क्लीन", "LocalLaundryService", "FAST 24H"),
        ServiceItem("13", "Beauty Spa", "ब्यूटी व स्पा", 399, "Personal", "Salon at home for men and women, facial, haircut & massage", "घर पर सैलून, फेशियल, हेयरकट व स्पा मसाज", "Spa", "PREMIUM"),
        ServiceItem("14", "Tutor", "होम ट्यूटर", 299, "Education", "Expert coaching for school classes, CBSE, BSEB & languages", "कक्षा 1 से 12 तक अनुभवी शिक्षक आपके घर पर", "School", "VERIFIED"),
        ServiceItem("15", "Driver", "ड्राइवर ऑन डिमांड", 399, "Travel", "Hourly & outstation personal car and commercial drivers", "प्रति घंटा या लंबी यात्रा के लिए कुशल ड्राइवर", "DirectionsCar", "24/7 AVAILABLE"),
        ServiceItem("16", "Security", "सुरक्षा गार्ड", 399, "Security", "Trained event, home & night duty security personnel", "अनुशासित सिक्योरिटी गार्ड व इवेंट गार्ड", "Security", "TRAINED"),
        ServiceItem("17", "Moving", "पैकर्स एंड मूवर्स", 399, "Travel", "Safe luggage packing, loading, unloading & transport", "सामान की सुरक्षित पैकिंग, लोडिंग व शिफ्टिंग", "LocalShipping", "ALL INDIA"),
        ServiceItem("18", "Welding", "वेल्डिंग व ग्रिल", 399, "Repairs", "Iron gate, window grill, rolling shutter & fabrication", "लोहे का गेट, खिड़की ग्रिल व शटर वेल्डिंग कार्य", "Handyman", "HEAVY DUTY"),
        ServiceItem("20", "Home Repairs", "जनरल रिपेयर", 299, "Repairs", "Masonry, tile fitting, masonry patches & odd handyman jobs", "टाइल फिटिंग, दीवार प्लास्टर व विविध मरम्मत", "Build", "INSTANT"),
        ServiceItem("21", "Kirana Store", "किराना स्टोर डिलीवरी", 199, "Daily", "Atta, dal, rice, oil, spices, snacks & daily household groceries delivered", "आटा, दाल, चावल, तेल, मसाले व घरेलू किराना सामान 30 मिनट में", "ShoppingCart", "FAST 30M"),
        ServiceItem("22", "Crane & Towing", "क्रेन व कार टोइंग सर्विस", 999, "Repairs", "Hydraulic crane, breakdown towing, car accident recovery & heavy machinery lifting in Marhaura & Saran", "हाइड्रोलिक क्रेन, कार लिफ्टिंग, ब्रेकडाउन टोइंग व एक्सीडेंट रिकवरी", "LocalShipping", "HEAVY DUTY")
    )

    private val defaultWorkers = listOf(
        Worker(
            id = "w1",
            name = "Alex R",
            serviceCategory = "Electrician",
            rating = 4.9,
            jobsCompleted = 128,
            hourlyRate = 500,
            experienceYears = 5,
            isOnline = true,
            status = WorkerAvailability.ONLINE,
            tags = listOf("Fast Response", "Electrician", "5+ years", "Verified"),
            distanceKm = 1.2
        ),
        Worker(
            id = "w2",
            name = "Mia K",
            serviceCategory = "Plumber",
            rating = 4.8,
            jobsCompleted = 96,
            hourlyRate = 550,
            experienceYears = 4,
            isOnline = true,
            status = WorkerAvailability.ONLINE,
            tags = listOf("Fast Response", "Plumber", "4+ years", "Top Rated"),
            distanceKm = 1.8
        ),
        Worker(
            id = "w3",
            name = "James T",
            serviceCategory = "Carpenter",
            rating = 4.6,
            jobsCompleted = 84,
            hourlyRate = 480,
            experienceYears = 3,
            isOnline = true,
            status = WorkerAvailability.ONLINE,
            tags = listOf("Carpenter", "3+ years", "Safety Checked"),
            distanceKm = 2.4
        ),
        Worker(
            id = "w4",
            name = "Ravi Sharma",
            serviceCategory = "Electrician",
            rating = 4.9,
            jobsCompleted = 142,
            hourlyRate = 500,
            experienceYears = 6,
            isOnline = true,
            status = WorkerAvailability.WORKING,
            tags = listOf("Fast Response", "Wiring", "Repairs", "On-site today"),
            distanceKm = 0.9
        ),
        Worker(
            id = "w5",
            name = "Priya Mehta",
            serviceCategory = "Plumber",
            rating = 4.8,
            jobsCompleted = 99,
            hourlyRate = 550,
            experienceYears = 5,
            isOnline = true,
            status = WorkerAvailability.ONLINE,
            tags = listOf("Fast Response", "Leaks", "Drains", "Pipes"),
            distanceKm = 1.5
        ),
        Worker(
            id = "w6",
            name = "Rajesh Kumar",
            serviceCategory = "RO Purifier",
            rating = 4.9,
            jobsCompleted = 114,
            hourlyRate = 500,
            experienceYears = 5,
            isOnline = true,
            status = WorkerAvailability.ONLINE,
            tags = listOf("RO & Purifier Specialist", "TDS Meter Expert", "Tools Included"),
            distanceKm = 1.1
        ),
        Worker(
            id = "w7",
            name = "Ramesh Kirana Store Pro",
            serviceCategory = "Kirana Store",
            rating = 4.9,
            jobsCompleted = 160,
            hourlyRate = 200,
            experienceYears = 5,
            isOnline = true,
            status = WorkerAvailability.ONLINE,
            tags = listOf("Fast 30 Min Delivery", "Kirana Store", "Verified Pro", "Marhaura Bazaar"),
            vehicleNumber = "BR 04 K 9205",
            distanceKm = 0.6
        ),
        Worker(
            id = "w8_crane",
            name = "Vikram Yadav (Crane Operator)",
            serviceCategory = "Crane & Towing",
            rating = 4.9,
            jobsCompleted = 112,
            hourlyRate = 1200,
            experienceYears = 7,
            isOnline = true,
            status = WorkerAvailability.ONLINE,
            phone = "+91 9205161464",
            tags = listOf("Hydraulic Crane", "Car Recovery", "24x7 Road Assist", "Safety Certified"),
            vehicleNumber = "BR 04 CR 1464",
            distanceKm = 1.8,
            photoPath = "public/assets/aistudio/Screenshot_20261105_145617_366240ad.jpg",
            licenseNumber = "BR-04-CRANE-HEAVY-2018",
            reviews = listOf(
                WorkerReview("cr1", "Sanjay Kumar", 5, "Yesterday", "Car breakdown ho gayi thi Saran road par, 25 minute me Crane lekar aaye aur safely tow kiya!", true),
                WorkerReview("cr2", "Mukesh Singh", 5, "4 days ago", "Very experienced crane driver. Heavy machine safely lift kiya.", true)
            )
        )
    )
    private val _workers = MutableStateFlow(defaultWorkers)
    val workers: List<Worker> get() = _workers.value
    val workersFlow = _workers.asStateFlow()

    private val defaultRoomKhojListings = listOf(
        RoomKhojListing(
            id = "rk1",
            title = "1 BHK Furnished Student Room near Marhaura Station",
            titleHi = "1 बीएचके फर्निश्ड स्टूडेंट कमरा - मढ़ौरा स्टेशन के पास",
            propertyType = PropertyType.ROOM,
            location = "Station Road, Marhaura, Saran (Bihar)",
            monthlyRent = 2500,
            gstFix = 5,
            isStudentFreeGst = true,
            malikName = "Awadhesh Singh",
            malikPhone = "+91 9205161464",
            areaSqFt = 220,
            features = listOf("24h Water", "Electricity Meter", "Wi-Fi Ready", "Study Table", "Safe for Students")
        ),
        RoomKhojListing(
            id = "rk2",
            title = "Large RCC Godown & Warehouse for Grain / Goods Storage",
            titleHi = "विशाल आरसीसी गोदाम व वेयरहाउस - अनाज/सामान भंडारण हेतु",
            propertyType = PropertyType.GODOWN_WAREHOUSE,
            location = "Marhaura Industrial Corridor, Saran (Bihar)",
            monthlyRent = 22000,
            gstFix = 80,
            isStudentFreeGst = false,
            malikName = "Chaudhary Warehouse Owner",
            malikPhone = "+91 9205161464",
            areaSqFt = 3200,
            features = listOf("Truck Loading Bay", "18 Ft Ceiling", "High Power Connection", "CCTV Protected")
        ),
        RoomKhojListing(
            id = "rk3",
            title = "Main Road Commercial Shop / Mall Showroom Front",
            titleHi = "मेन रोड कमर्शियल दुकान / मॉल शोरूम स्पेस",
            propertyType = PropertyType.SHOP_MALL,
            location = "Marhaura Chowk, Near SBI Bank, Saran",
            monthlyRent = 12500,
            gstFix = 80,
            isStudentFreeGst = false,
            malikName = "Rakesh Gupta (Malik)",
            malikPhone = "+91 9205161464",
            areaSqFt = 450,
            features = listOf("Glass Front", "Rolling Shutter", "High Footfall Market", "Backup Generator")
        ),
        RoomKhojListing(
            id = "rk4",
            title = "2 BHK Complete Independent Family Home",
            titleHi = "2 बीएचके स्वतंत्र पारिवारिक घर - शांत वातावरण",
            propertyType = PropertyType.HOME,
            location = "Civil Lines Colony, Marhaura, Saran",
            monthlyRent = 6000,
            gstFix = 5,
            isStudentFreeGst = true,
            malikName = "Suresh Pandey",
            malikPhone = "+91 9205161464",
            areaSqFt = 850,
            features = listOf("Car Parking", "Modular Kitchen", "Balcony", "Garden Area")
        ),
        RoomKhojListing(
            id = "rk5",
            title = "Warehouse & Logistics Depot (All India Hub)",
            titleHi = "वेयरहाउस व लॉजिस्टिक्स डिपो (ऑल इंडिया हब)",
            propertyType = PropertyType.GODOWN_WAREHOUSE,
            location = "Patna - Chhapra Highway, Saran Border",
            monthlyRent = 35000,
            gstFix = 80,
            isStudentFreeGst = false,
            malikName = "Logistics Hub Owner",
            malikPhone = "+91 9205161464",
            areaSqFt = 5500,
            features = listOf("Multi-Axle Access", "24/7 Security", "Fire Extinguishers", "Weighbridge Nearby")
        )
    )
    private val _roomKhojListings = MutableStateFlow(defaultRoomKhojListings)
    val roomKhojListings: List<RoomKhojListing> get() = _roomKhojListings.value
    val roomKhojListingsFlow = _roomKhojListings.asStateFlow()

    val defaultBookings = listOf(
        Booking(
            id = "b1",
            serviceName = "Electrician Service",
            workerName = "Alex R",
            workerPhone = "+91 9205161464",
            dateTimeStr = "Tomorrow • 10:30 AM",
            address = "123 Oak St, Marhaura Bazar, Saran",
            workerCharge = 500,
            gstAmount = 50,
            totalAmount = 550,
            status = BookingStatus.ON_THE_WAY,
            otp = "4582",
            etaMinutes = 8
        ),
        Booking(
            id = "b2",
            serviceName = "AC Repair",
            workerName = "Rohan Sharma",
            workerPhone = "+91 9205161464",
            dateTimeStr = "Scheduled • Sat, Oct 12 • 2:00 PM",
            address = "Station Road, Marhaura",
            workerCharge = 1000,
            gstAmount = 100,
            totalAmount = 1100,
            status = BookingStatus.ACCEPTED,
            otp = "7193",
            etaMinutes = 45
        ),
        Booking(
            id = "b3",
            serviceName = "Plumber Service",
            workerName = "Vikram Singh",
            workerPhone = "+91 9205161464",
            dateTimeStr = "Completed • Oct 6 • 4:15 PM",
            address = "Hospital Road, Marhaura",
            workerCharge = 700,
            gstAmount = 70,
            totalAmount = 770,
            status = BookingStatus.COMPLETED,
            otp = "3321",
            etaMinutes = 0
        ),
        Booking(
            id = "b4",
            serviceName = "Home Cleaning",
            workerName = "Priya Kumar",
            workerPhone = "+91 9205161464",
            dateTimeStr = "Completed • Oct 3 • 11:00 AM",
            address = "Gandhi Chowk, Marhaura",
            workerCharge = 500,
            gstAmount = 50,
            totalAmount = 550,
            status = BookingStatus.COMPLETED,
            otp = "9820",
            etaMinutes = 0
        )
    )

    val providerActivity = listOf(
        TodayJobActivity("j1", "Job Completed: Plumbing repair", "10:32 AM", 800, 80, 880, true),
        TodayJobActivity("j2", "Job Completed: Electrical wiring", "09:15 AM", 1100, 110, 1210, true),
        TodayJobActivity("j3", "Job Completed: AC servicing", "08:20 AM", 550, 55, 605, true)
    )

    data class PricingRuleCalculation(
        val baseCharge: Int,
        val gstPercent: Int = 10,
        val gstAmount: Int,
        val customerTotal: Int,
        val workerGetsPura: Int,
        val ownerGstAccount: Int,
        val advanceOnline50Percent: Int = (customerTotal / 2),
        val remaining50Percent: Int = customerTotal - (customerTotal / 2),
        val gstDeductedFromAdvance: Int = gstAmount,
        val workerAdvancePart: Int = (customerTotal / 2) - gstAmount,
        val workerFinalPart: Int = customerTotal - (customerTotal / 2)
    )

    fun calculateWorkerRule(charge: Int): PricingRuleCalculation {
        val safeCharge = if (charge < 0) 0 else charge
        // GST Tier: Chhota kaam (< ₹500) = 5% GST, Medium (₹500-₹1000) = 8% GST, Bada (> ₹1000) = 10% GST
        val gstPercent = when {
            safeCharge < 500 -> 5
            safeCharge <= 1000 -> 8
            else -> 10
        }
        val gst = ((safeCharge * gstPercent) / 100.0).toInt()
        val total = safeCharge + gst
        val advance = total / 2
        val remaining = total - advance
        return PricingRuleCalculation(
            baseCharge = safeCharge,
            gstPercent = gstPercent,
            gstAmount = gst,
            customerTotal = total,
            workerGetsPura = safeCharge, // Worker ko pura 100% paisa milega!
            ownerGstAccount = gst, // GST pura owner account me
            advanceOnline50Percent = advance, // 50% advance online at booking
            remaining50Percent = remaining, // 50% cash or online on completion
            gstDeductedFromAdvance = gst, // Total GST deducted from 50% advance
            workerAdvancePart = advance - gst, // Worker's advance share
            workerFinalPart = remaining // Worker's final share on completion
        )
    }

    // State flows for real-time reactivity
    private val _bookings = MutableStateFlow(defaultBookings)
    val bookings = _bookings.asStateFlow()

    fun addBooking(booking: Booking) {
        val updated = listOf(booking) + _bookings.value
        _bookings.value = updated
    }

    fun cancelBooking(bookingId: String) {
        _bookings.value = _bookings.value.map {
            if (it.id == bookingId) it.copy(status = BookingStatus.CANCELLED) else it
        }
    }

    fun updateBookingPayment(bookingId: String, method: String) {
        val stage = if (method.uppercase() == "ONLINE") PaymentStage.FULLY_PAID_ONLINE else PaymentStage.FULLY_PAID_CASH
        _bookings.value = _bookings.value.map {
            if (it.id == bookingId) it.copy(
                paymentStage = stage,
                remainingBalance = 0,
                remainingPaymentMethod = if (method.uppercase() == "ONLINE") "Online Paid (UPI/Card)" else "Cash Handed to Worker",
                status = BookingStatus.COMPLETED
            ) else it
        }
    }
}
