package com.example.data

import com.example.model.Language

object Localization {
    fun get(key: String, language: Language): String {
        val entry = translations[key] ?: return key
        return if (language == Language.HI) entry.hi else entry.en
    }

    private data class TransEntry(val en: String, val hi: String)

    private val translations = mapOf(
        "app_title" to TransEntry("ServicePro Marhaura", "सर्विसप्रो मढ़ौरा"),
        "city_marhaura" to TransEntry("Marhaura, Saran (Bihar)", "मढ़ौरा, सारण (बिहार)"),
        "need_fixed_title" to TransEntry("What do you need fixed?", "आपको क्या ठीक करवाना है?"),
        "need_fixed_sub" to TransEntry("Book trusted pros in minutes • Available now", "भरोसेमंद मिस्त्री मिनटों में बुक करें • अभी उपलब्ध"),
        "explore_20" to TransEntry("Explore All 20 Services", "सभी 20 सेवाएं देखें"),
        "price_start_from" to TransEntry("Prices start from ₹299", "शुरुआती कीमत ₹299 से"),
        "choose_worker" to TransEntry("Choose Worker", "कारीगर (Worker) चुनें"),
        "workers_available" to TransEntry("Available workers in Marhaura • Today", "मढ़ौरा में उपलब्ध मिस्त्री • आज"),
        "book_btn" to TransEntry("Book", "बुक करें"),
        "booking_confirmed" to TransEntry("Booking Confirmed!", "बुकिंग कन्फर्म हो गई!"),
        "service_booked_sub" to TransEntry("Your service is booked", "आपकी सेवा सफलतापूर्वक बुक हो चुकी है"),
        "view_details" to TransEntry("View Details & Track", "विवरण और ट्रैकिंग देखें"),
        "cancel_booking" to TransEntry("Manage booking • Cancel", "बुकिंग रद्द / बदलें"),
        "live_tracking" to TransEntry("Live Tracking", "लाइव ट्रैकिंग"),
        "on_the_way" to TransEntry("On the way", "रास्ते में हैं"),
        "arrived" to TransEntry("Arrived", "पहुंच गए"),
        "contact_worker" to TransEntry("Contact Worker", "मिस्त्री से संपर्क करें"),
        "call" to TransEntry("Call", "कॉल करें"),
        "whatsapp" to TransEntry("WhatsApp", "व्हाट्सएप"),
        "otp_for_service" to TransEntry("OTP for service", "सेवा शुरू करने का OTP"),
        "problem_photo_video" to TransEntry("Problem Photo / Video", "समस्या की फोटो / वीडियो"),
        "photo_sub" to TransEntry("Upload photos or video to get instant price estimate", "तुरंत अनुमानित दाम जानने के लिए फोटो या वीडियो जोड़ें"),
        "price_estimate" to TransEntry("Price estimate: ₹400 - ₹1200", "अनुमानित खर्च: ₹400 - ₹1200"),
        "quick_actions" to TransEntry("Quick Actions", "त्वरित सुविधाएं"),
        "worker_dashboard" to TransEntry("Provider Dashboard", "कारीगर डैशबोर्ड"),
        "earnings_today" to TransEntry("Earnings Today", "आज की कमाई"),
        "total_jobs" to TransEntry("Total Jobs", "कुल काम"),
        "availability_status" to TransEntry("Availability Status", "उपलब्धता स्थिति"),
        "online" to TransEntry("Online", "ऑनलाइन"),
        "offline" to TransEntry("Offline", "ऑफलाइन"),
        "working" to TransEntry("Working", "काम पर"),
        "todays_activity" to TransEntry("Today's Activity", "आज की गतिविधियां"),
        "pricing_rule_title" to TransEntry("Worker Pricing Rule", "कारीगर मूल्य नियम"),
        "pricing_banner" to TransEntry("🔥 TODAY OFFER: Aaj Registration Karo Apna Dam Khud Set Karo 🔥", "🔥 आज का ऑफर: आज रजिस्ट्रेशन करो अपना दाम खुद तय करो 🔥"),
        "worker_full_payment" to TransEntry("Worker gets 100% full payment! No cut!", "कारीगर को मिलेगा 100% पूरा पैसा! कोई कटौती नहीं!"),
        "gst_owner_share" to TransEntry("10% GST goes to Owner Account", "10% जीएसटी मालिक के खाते में जाएगा"),
        "customer_total" to TransEntry("Customer Total", "ग्राहक का कुल भुगतान"),
        "working_charge" to TransEntry("Worker Charge", "कारीगर का चार्ज"),
        "invoice_title" to TransEntry("Invoice / Bill", "बिल / रसीद"),
        "room_khoj" to TransEntry("RoomKhoj", "रूमखोज"),
        "room_khoj_sub" to TransEntry("All India Home + Godown Warehouse Search", "पूरे भारत में रूम, घर व गोदाम वेयरहाउस खोजें"),
        "roommate_app" to TransEntry("RoomMate App Integration", "रूममेट ऐप इंटीग्रेशन"),
        "call_malik" to TransEntry("Call Malik", "मालिक को कॉल करें"),
        "shop_mall_gst" to TransEntry("Shop Mall GST: ₹80 Fix", "दुकान/मॉल जीएसटी: ₹80 फिक्स"),
        "room_gst" to TransEntry("Room GST: ₹5 Fix", "कमरा जीएसटी: ₹5 फिक्स"),
        "student_free" to TransEntry("Student ke liye FREE", "विद्यार्थियों के लिए एकदम फ्री"),
        "helpline_text" to TransEntry("Call / WhatsApp: 1800-123-SERVICE | +91 9205161464", "हेल्पलाइन: 1800-123-SERVICE | +91 9205161464"),
        "my_jobs" to TransEntry("My Jobs", "मेरे काम"),
        "earnings" to TransEntry("Earnings", "कमाई"),
        "home" to TransEntry("Home", "होम"),
        "bookings" to TransEntry("Bookings", "बुकिंग"),
        "support" to TransEntry("Support", "सपोर्ट"),
        "profile" to TransEntry("Profile", "प्रोफाइल")
    )
}
