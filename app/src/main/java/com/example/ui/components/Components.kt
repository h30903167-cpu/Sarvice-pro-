package com.example.ui.components

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.Localization
import com.example.data.ServiceProRepository
import com.example.model.Language
import com.example.model.UserRole
import com.example.ui.AppScreen
import com.example.ui.theme.*

fun makePhoneCall(context: Context, phoneNumber: String = ServiceProRepository.OWNER_PHONE) {
    try {
        val cleanNumber = phoneNumber.replace(" ", "")
        val intent = Intent(Intent.ACTION_DIAL).apply {
            data = Uri.parse("tel:$cleanNumber")
        }
        context.startActivity(intent)
    } catch (e: Exception) {
        Toast.makeText(context, "Dialing $phoneNumber", Toast.LENGTH_SHORT).show()
    }
}

fun openWhatsApp(context: Context, phoneNumber: String = "919205161464", message: String = "Namaste ServicePro Marhaura! I need assistance with booking and services.") {
    try {
        val cleanNumber = phoneNumber.replace("+", "").replace(" ", "")
        val encoded = Uri.encode(message)
        val url = "https://api.whatsapp.com/send?phone=$cleanNumber&text=$encoded"
        val intent = Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse(url)
        }
        context.startActivity(intent)
    } catch (e: Exception) {
        Toast.makeText(context, "Opening WhatsApp for $phoneNumber", Toast.LENGTH_SHORT).show()
    }
}

fun openTelegram(context: Context, telegramTarget: String = "ServiceProMarhaura") {
    try {
        val cleanTarget = telegramTarget.replace("+", "").replace(" ", "")
        val url = "https://t.me/$cleanTarget"
        val intent = Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse(url)
        }
        context.startActivity(intent)
    } catch (e: Exception) {
        Toast.makeText(context, "Opening Telegram: $telegramTarget", Toast.LENGTH_SHORT).show()
    }
}

@Composable
fun HelplineContactRow(
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = ServiceProLightBlue),
        border = BorderStroke(1.dp, ServiceProBlue.copy(alpha = 0.35f))
    ) {
        Column(modifier = Modifier.padding(10.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.SupportAgent, contentDescription = null, tint = ServiceProBlue, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Column {
                        Text(
                            text = "24x7 Customer Helpline Desk",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = ServiceProDarkBlue
                        )
                        Text(
                            text = "Official Toll-Free Support • WhatsApp & Telegram Desk",
                            fontSize = 10.sp,
                            color = TextSecondary
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                // Call Button
                Button(
                    onClick = { makePhoneCall(context, ServiceProRepository.OWNER_PHONE) },
                    modifier = Modifier.weight(1.1f).height(32.dp),
                    shape = RoundedCornerShape(6.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = ServiceProBlue),
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Icon(Icons.Default.Call, contentDescription = null, tint = Color.White, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Call Helpline", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                }

                // WhatsApp Button
                Button(
                    onClick = { openWhatsApp(context, ServiceProRepository.OWNER_PHONE) },
                    modifier = Modifier.weight(1.2f).height(32.dp),
                    shape = RoundedCornerShape(6.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = GuaranteeGreen),
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Icon(Icons.Default.Chat, contentDescription = null, tint = Color.White, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("WhatsApp", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                }

                // Telegram Button
                Button(
                    onClick = { openTelegram(context, "ServiceProMarhaura") },
                    modifier = Modifier.weight(1.2f).height(32.dp),
                    shape = RoundedCornerShape(6.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0088CC)),
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Icon(Icons.Default.Send, contentDescription = null, tint = Color.White, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Telegram", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun ServiceProHeader(
    language: Language,
    userRole: UserRole,
    onToggleLanguage: () -> Unit,
    onRoleChange: (UserRole) -> Unit,
    onOpenSupport: () -> Unit = {},
    onLocationClick: () -> Unit = {},
    onLogout: () -> Unit = {}
) {
    val context = LocalContext.current
    Surface(
        color = Color.White,
        shadowElevation = 1.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Brand logo & title: Shield with checkmark + ServicePro (Exact match to screenshot 1 & 6)
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable { /* Home click */ }
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(ServiceProBlue),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Shield,
                            contentDescription = "ServicePro Shield Logo",
                            tint = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = null,
                            tint = ServiceProBlue,
                            modifier = Modifier.size(13.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "ServicePro",
                        fontSize = 21.sp,
                        fontWeight = FontWeight.Black,
                        color = Color(0xFF0C1E3C),
                        letterSpacing = (-0.5).sp
                    )
                }

                // Right controls: Location pill + Language + Role + Logout button
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    // Location Pill matching screenshot: 📍 Marhaura ∨
                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = Color(0xFFF1F5F9),
                        modifier = Modifier
                            .height(32.dp)
                            .clickable {
                                Toast.makeText(context, "Serving Marhaura, Saran (Bihar)", Toast.LENGTH_SHORT).show()
                                onLocationClick()
                            }
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 10.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.LocationOn,
                                contentDescription = null,
                                tint = ServiceProBlue,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Marhaura",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color(0xFF0C1E3C)
                            )
                            Spacer(modifier = Modifier.width(2.dp))
                            Icon(
                                imageVector = Icons.Default.KeyboardArrowDown,
                                contentDescription = null,
                                tint = TextSecondary,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }

                    // Language toggle pill
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = ServiceProLightBlue,
                        modifier = Modifier
                            .height(32.dp)
                            .clickable(onClick = onToggleLanguage)
                            .testTag("lang_toggle_btn")
                    ) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier.padding(horizontal = 8.dp)
                        ) {
                            Text(
                                text = if (language == Language.EN) "हिंदी" else "EN",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = ServiceProBlue
                            )
                        }
                    }

                    // Role switch chip: Customer vs Worker/Partner
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = if (userRole == UserRole.WORKER) GuaranteeLightGreen else Color(0xFFF1F5F9),
                        border = BorderStroke(1.dp, if (userRole == UserRole.WORKER) GuaranteeGreen else CardBorder),
                        modifier = Modifier
                            .height(32.dp)
                            .clickable {
                                val next = if (userRole == UserRole.CUSTOMER) UserRole.WORKER else UserRole.CUSTOMER
                                onRoleChange(next)
                            }
                            .testTag("role_switch_chip")
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 8.dp)
                        ) {
                            Text(
                                text = if (userRole == UserRole.WORKER) "🛠 Partner" else "👤 User",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (userRole == UserRole.WORKER) DarkGreen else TextPrimary
                            )
                        }
                    }

                    // Logout Icon Button
                    Surface(
                        shape = CircleShape,
                        color = Color(0xFFFEE2E2),
                        border = BorderStroke(1.dp, Color(0xFFEF4444).copy(alpha = 0.5f)),
                        modifier = Modifier
                            .size(32.dp)
                            .clickable {
                                Toast.makeText(context, "Logged out of ServicePro", Toast.LENGTH_SHORT).show()
                                onLogout()
                            }
                            .testTag("header_logout_btn")
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.Logout,
                                contentDescription = "Logout",
                                tint = Color(0xFFDC2626),
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TodayOfferBanner(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .border(2.dp, Color(0xFF16A34A), RoundedCornerShape(14.dp))
            .shadow(2.dp, RoundedCornerShape(14.dp))
            .testTag("today_offer_banner"),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E9))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFC8E6C9)),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "⚡", fontSize = 20.sp)
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "LIMITED OFFER: Aaj Registration Karo Apna Dam Khud Set Karo",
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 12.sp,
                    color = Color(0xFF14532D)
                )
                Text(
                    text = "Worker ko pura 100% milega • No Cut • GST 5%, 8% ya 10% (Chhota/Bada Kaam)",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF166534)
                )
            }
            Icon(
                imageVector = Icons.Default.ArrowForwardIos,
                contentDescription = null,
                tint = Color(0xFF16A34A),
                modifier = Modifier.size(13.dp)
            )
        }
    }
}

@Composable
fun WorkerGuaranteedGreenBox(
    modifier: Modifier = Modifier,
    amountText: String = "₹500 / 100%"
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = GuaranteeLightGreen),
        border = BorderStroke(1.5.dp, GuaranteeGreen)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .clip(CircleShape)
                    .background(GuaranteeGreen),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Guaranteed",
                    tint = Color.White,
                    modifier = Modifier.size(22.dp)
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text = "Worker ko pura paisa milega!",
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = DarkGreen
                )
                Text(
                    text = "No Platform Cut from Worker ($amountText) • Pura GST Official Owner Account me jayega",
                    fontSize = 11.sp,
                    color = DarkGreen.copy(alpha = 0.9f),
                    lineHeight = 15.sp
                )
            }
        }
    }
}

@Composable
fun ServiceProBottomBar(
    currentScreen: AppScreen,
    userRole: UserRole,
    onNavigate: (AppScreen) -> Unit
) {
    // Exact 4 navigation items matching screenshots: Home, Bookings, Support, Profile
    NavigationBar(
        containerColor = Color.White,
        tonalElevation = 8.dp,
        modifier = Modifier.height(68.dp)
    ) {
        NavigationBarItem(
            selected = (currentScreen == AppScreen.HOME || currentScreen == AppScreen.ALL_SERVICES || currentScreen == AppScreen.SERVICE_DETAIL || currentScreen == AppScreen.CHOOSE_WORKER),
            onClick = { onNavigate(AppScreen.HOME) },
            icon = {
                Icon(
                    if (currentScreen == AppScreen.HOME) Icons.Filled.Home else Icons.Outlined.Home,
                    contentDescription = "Home",
                    modifier = Modifier.size(24.dp)
                )
            },
            label = { Text("Home", fontSize = 11.sp, fontWeight = if (currentScreen == AppScreen.HOME) FontWeight.Bold else FontWeight.Normal) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = ServiceProBlue,
                selectedTextColor = ServiceProBlue,
                unselectedIconColor = TextMuted,
                unselectedTextColor = TextMuted,
                indicatorColor = Color.Transparent
            )
        )

        NavigationBarItem(
            selected = (currentScreen == AppScreen.MY_BOOKINGS || currentScreen == AppScreen.LIVE_TRACKING || currentScreen == AppScreen.BOOKING_CONFIRMED),
            onClick = { onNavigate(AppScreen.MY_BOOKINGS) },
            icon = {
                Icon(
                    if (currentScreen == AppScreen.MY_BOOKINGS) Icons.Filled.BookOnline else Icons.Outlined.BookOnline,
                    contentDescription = "Bookings",
                    modifier = Modifier.size(24.dp)
                )
            },
            label = { Text("Bookings", fontSize = 11.sp, fontWeight = if (currentScreen == AppScreen.MY_BOOKINGS) FontWeight.Bold else FontWeight.Normal) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = ServiceProBlue,
                selectedTextColor = ServiceProBlue,
                unselectedIconColor = TextMuted,
                unselectedTextColor = TextMuted,
                indicatorColor = Color.Transparent
            )
        )

        NavigationBarItem(
            selected = (currentScreen == AppScreen.CONTACT_SUPPORT || currentScreen == AppScreen.ROOM_KHOJ),
            onClick = { onNavigate(AppScreen.CONTACT_SUPPORT) },
            icon = {
                Icon(
                    if (currentScreen == AppScreen.CONTACT_SUPPORT) Icons.Filled.HeadsetMic else Icons.Outlined.HeadsetMic,
                    contentDescription = "Support",
                    modifier = Modifier.size(24.dp)
                )
            },
            label = { Text("Support", fontSize = 11.sp, fontWeight = if (currentScreen == AppScreen.CONTACT_SUPPORT) FontWeight.Bold else FontWeight.Normal) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = ServiceProBlue,
                selectedTextColor = ServiceProBlue,
                unselectedIconColor = TextMuted,
                unselectedTextColor = TextMuted,
                indicatorColor = Color.Transparent
            )
        )

        NavigationBarItem(
            selected = (currentScreen == AppScreen.PROFILE || currentScreen == AppScreen.WORKER_DASHBOARD),
            onClick = { onNavigate(AppScreen.PROFILE) },
            icon = {
                Icon(
                    if (currentScreen == AppScreen.PROFILE || currentScreen == AppScreen.WORKER_DASHBOARD) Icons.Filled.Person else Icons.Outlined.Person,
                    contentDescription = "Profile",
                    modifier = Modifier.size(24.dp)
                )
            },
            label = { Text("Profile", fontSize = 11.sp, fontWeight = if (currentScreen == AppScreen.PROFILE) FontWeight.Bold else FontWeight.Normal) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = ServiceProBlue,
                selectedTextColor = ServiceProBlue,
                unselectedIconColor = TextMuted,
                unselectedTextColor = TextMuted,
                indicatorColor = Color.Transparent
            )
        )
    }
}

// -------------------------------------------------------------
// LOCATION SEARCH & SELECTOR DIALOG (Post-Login & Location Searcher)
// -------------------------------------------------------------
@Composable
fun LocationSearchDialog(
    currentArea: String,
    onSelectArea: (String) -> Unit,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    var searchQuery by remember { mutableStateOf("") }

    val allMarhauraAreas = listOf(
        "Marhaura Station Road" to "मढ़ौरा स्टेशन रोड • मुख्य बाज़ार व स्टेशन संपर्क",
        "Main Bazaar Marhaura" to "मेन बाज़ार मढ़ौरा • व्यावसायिक केंद्र",
        "Silhauri Road" to "सिल्हौरी रोड • आवासीय व कृषि क्षेत्र",
        "Garakha Road" to "गड़खा रोड • स्टेट हाईवे संपर्क",
        "Mirzapur" to "मिर्ज़ापुर • आवासीय कॉलोनी व बाज़ार",
        "Awatar Nagar" to "अवतार नगर • रेल व मुख्य सड़क संपर्क",
        "Rail Wheel Factory Corridor" to "रेल पहिया कारखाना क्षेत्र • इंडस्ट्रियल हब",
        "Hospital Chowk" to "अस्पताल चौक • मढ़ौरा रेफरल अस्पताल क्षेत्र",
        "Chhapra Highway (NH-19)" to "छपरा हाईवे • नेशनल हाईवे बाईपास",
        "Masrakh Road" to "मसरख रोड • उत्तरी सारण संपर्क",
        "Industrial Area Marhaura" to "औद्योगिक क्षेत्र • गोदाम व फैक्ट्री परिसर",
        "All Marhaura & Saran Area" to "पूरा मढ़ौरा व सारण क्षेत्र • संपूर्ण सेवा कवरेज"
    )

    val filteredAreas = remember(searchQuery) {
        if (searchQuery.isBlank()) {
            allMarhauraAreas
        } else {
            val q = searchQuery.trim().lowercase()
            allMarhauraAreas.filter { (nameEn, nameHi) ->
                nameEn.lowercase().contains(q) || nameHi.lowercase().contains(q)
            }
        }
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.94f)
                .fillMaxHeight(0.85f)
                .shadow(16.dp, RoundedCornerShape(24.dp)),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp)
            ) {
                // Header with icon and close button
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            shape = CircleShape,
                            color = ServiceProLightBlue,
                            modifier = Modifier.size(40.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.LocationOn,
                                    contentDescription = null,
                                    tint = ServiceProBlue,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "Select Location",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Black,
                                color = Color(0xFF0C1E3C)
                            )
                            Text(
                                text = "अपना सेवा क्षेत्र चुनें (Searcher / खोजें)",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = TextSecondary
                            )
                        }
                    }

                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = TextSecondary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Location Searcher (Search Bar) - Matches "location me sucher ka bhi option do"
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text("Search area, mohalla, road (खोजें)...", fontSize = 13.sp) },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search",
                            tint = ServiceProBlue
                        )
                    },
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(onClick = { searchQuery = "" }) {
                                Icon(
                                    imageVector = Icons.Default.Clear,
                                    contentDescription = "Clear",
                                    tint = TextSecondary,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("location_search_input"),
                    shape = RoundedCornerShape(14.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = ServiceProBlue,
                        unfocusedBorderColor = CardBorder,
                        focusedContainerColor = Color(0xFFF8FAFC),
                        unfocusedContainerColor = Color(0xFFF8FAFC)
                    ),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(12.dp))

                // GPS Current Location Button
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            val detected = "Marhaura Station Chowk (GPS Detected)"
                            Toast.makeText(context, "Location set: $detected", Toast.LENGTH_SHORT).show()
                            onSelectArea(detected)
                        },
                    shape = RoundedCornerShape(12.dp),
                    color = ServiceProLightBlue,
                    border = BorderStroke(1.dp, ServiceProBlue.copy(alpha = 0.4f))
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.MyLocation,
                            contentDescription = "GPS",
                            tint = ServiceProBlue,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "Use Current Location (GPS) 📍",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = ServiceProDarkBlue
                            )
                            Text(
                                text = "वर्तमान जीपीएस स्थान से तुरंत सेवा पाएं",
                                fontSize = 11.sp,
                                color = TextSecondary
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Custom Location Option if user types a new area
                if (searchQuery.isNotBlank() && filteredAreas.none { it.first.equals(searchQuery.trim(), ignoreCase = true) }) {
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                val customLoc = searchQuery.trim()
                                Toast.makeText(context, "Custom Location set: $customLoc", Toast.LENGTH_SHORT).show()
                                onSelectArea(customLoc)
                            },
                        shape = RoundedCornerShape(12.dp),
                        color = GuaranteeLightGreen,
                        border = BorderStroke(1.2.dp, GuaranteeGreen)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.AddLocationAlt, contentDescription = null, tint = DarkGreen, modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = "+ Set \"${searchQuery.trim()}\" as Location",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = DarkGreen
                                )
                                Text(
                                    text = "Tap to deliver / book services at this custom area",
                                    fontSize = 11.sp,
                                    color = DarkGreen.copy(alpha = 0.8f)
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                }

                // Section Title: Available Areas
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (searchQuery.isBlank()) "Popular Areas in Marhaura & Saran" else "Search Results (${filteredAreas.size})",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextSecondary
                    )
                    Text(
                        text = "Current: $currentArea",
                        fontSize = 10.sp,
                        color = ServiceProBlue,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Scrollable List of Areas
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    if (filteredAreas.isEmpty()) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 24.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(Icons.Default.LocationOff, contentDescription = null, tint = TextMuted, modifier = Modifier.size(36.dp))
                                Spacer(modifier = Modifier.height(6.dp))
                                Text("No matching locality found", color = TextSecondary, fontSize = 13.sp)
                                Text("Use the button above to add custom location", color = TextMuted, fontSize = 11.sp)
                            }
                        }
                    } else {
                        filteredAreas.forEach { (nameEn, desc) ->
                            val isSelected = currentArea.contains(nameEn, ignoreCase = true) || nameEn.contains(currentArea, ignoreCase = true)
                            Surface(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        Toast.makeText(context, "Location set to $nameEn", Toast.LENGTH_SHORT).show()
                                        onSelectArea(nameEn)
                                    },
                                shape = RoundedCornerShape(12.dp),
                                color = if (isSelected) Color(0xFFEFF6FF) else Color(0xFFF8FAFC),
                                border = BorderStroke(1.dp, if (isSelected) ServiceProBlue else CardBorder)
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = if (isSelected) Icons.Default.CheckCircle else Icons.Default.Place,
                                        contentDescription = null,
                                        tint = if (isSelected) ServiceProBlue else Color(0xFF64748B),
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = nameEn,
                                            fontSize = 13.sp,
                                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.SemiBold,
                                            color = if (isSelected) ServiceProDarkBlue else TextPrimary
                                        )
                                        Text(
                                            text = desc,
                                            fontSize = 10.sp,
                                            color = TextSecondary
                                        )
                                    }
                                    if (isSelected) {
                                        Surface(
                                            shape = RoundedCornerShape(6.dp),
                                            color = ServiceProBlue,
                                            modifier = Modifier.padding(start = 4.dp)
                                        ) {
                                            Text(
                                                text = "Active",
                                                color = Color.White,
                                                fontSize = 9.sp,
                                                fontWeight = FontWeight.Bold,
                                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

