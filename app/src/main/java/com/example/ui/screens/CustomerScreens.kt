package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.res.painterResource
import com.example.R
import com.example.data.Localization
import com.example.data.ServiceProRepository
import com.example.model.*
import com.example.ui.AppScreen
import com.example.ui.MainViewModel
import com.example.ui.components.*
import com.example.ui.theme.*

@Composable
fun HomeScreen(
    viewModel: MainViewModel,
    onNavigate: (AppScreen) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundSlate)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(6.dp))
            // Helpline Bar
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { makePhoneCall(context, ServiceProRepository.OWNER_PHONE) },
                shape = RoundedCornerShape(10.dp),
                colors = CardDefaults.cardColors(containerColor = ServiceProLightBlue),
                border = BorderStroke(1.dp, ServiceProBlue.copy(alpha = 0.3f))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.SupportAgent, contentDescription = null, tint = ServiceProBlue, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "24x7 Customer Helpline • Direct Support Desk",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = ServiceProDarkBlue
                        )
                    }
                    Text(
                        text = "Call Helpline 📞",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = GuaranteeGreen
                    )
                }
            }
        }

        // Active Area Indicator Card
        item {
            Surface(
                shape = RoundedCornerShape(10.dp),
                color = ServiceProLightBlue,
                border = BorderStroke(1.dp, ServiceProBlue.copy(alpha = 0.35f)),
                modifier = Modifier.fillMaxWidth().clickable { viewModel.showLocationPicker(true) }.testTag("switch_area_btn")
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.LocationOn, contentDescription = null, tint = ServiceProBlue, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Active Area: ${uiState.selectedArea}",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = ServiceProDarkBlue
                        )
                    }
                    Text(
                        text = "Switch Area 📍",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = ServiceProBlue
                    )
                }
            }
        }

        // Today Offer Banner
        item {
            TodayOfferBanner(onClick = { onNavigate(AppScreen.PRICING_RULE) })
        }

        // "What do you need fixed?" Header matching Screenshot 2
        item {
            Column {
                Text(
                    text = Localization.get("need_fixed_title", uiState.language),
                    fontSize = 32.sp,
                    lineHeight = 36.sp,
                    fontWeight = FontWeight.Black,
                    color = Color(0xFF0C1E3C),
                    letterSpacing = (-0.5).sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = Localization.get("need_fixed_sub", uiState.language),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Normal,
                    color = Color(0xFF64748B)
                )
            }
        }

        // Featured Service Cards Carousel (Matches Screenshot 2)
        item {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(vertical = 4.dp)
            ) {
                items(ServiceProRepository.services.take(5)) { service ->
                    FeaturedServiceCard(
                        service = service,
                        language = uiState.language,
                        onClick = {
                            viewModel.selectService(service)
                            onNavigate(AppScreen.SERVICE_DETAIL)
                        }
                    )
                }
            }
        }

        // Problem Photo / Video Card (Matches Screenshot 2)
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onNavigate(AppScreen.ADD_MEDIA) }
                    .testTag("upload_problem_card"),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Outlined.PhotoCamera,
                            contentDescription = null,
                            tint = ServiceProBlue,
                            modifier = Modifier.size(22.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = Localization.get("problem_photo_video", uiState.language),
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            color = Color(0xFF0C1E3C)
                        )
                    }
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = Localization.get("photo_sub", uiState.language),
                        fontSize = 11.sp,
                        color = Color(0xFF64748B)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            OutlinedButton(
                                onClick = { onNavigate(AppScreen.ADD_MEDIA) },
                                shape = RoundedCornerShape(8.dp),
                                border = BorderStroke(1.dp, ServiceProBlue),
                                colors = ButtonDefaults.outlinedButtonColors(contentColor = ServiceProBlue),
                                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 0.dp),
                                modifier = Modifier.height(34.dp)
                            ) {
                                Icon(Icons.Default.CameraAlt, contentDescription = null, modifier = Modifier.size(14.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Add Photo", fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                            }

                            OutlinedButton(
                                onClick = { onNavigate(AppScreen.ADD_MEDIA) },
                                shape = RoundedCornerShape(8.dp),
                                border = BorderStroke(1.dp, ServiceProBlue),
                                colors = ButtonDefaults.outlinedButtonColors(contentColor = ServiceProBlue),
                                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 0.dp),
                                modifier = Modifier.height(34.dp)
                            ) {
                                Icon(Icons.Default.Videocam, contentDescription = null, modifier = Modifier.size(14.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Add Video", fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                            }
                        }

                        Column(horizontalAlignment = Alignment.End) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Outlined.Description, contentDescription = null, tint = Color(0xFF0C1E3C), modifier = Modifier.size(13.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Price estimate", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0C1E3C))
                            }
                            Text("Get instant estimate •", fontSize = 9.sp, color = Color(0xFF64748B))
                            Text("Usually within minutes", fontSize = 9.sp, color = Color(0xFF64748B))
                            Text("Estimate: ₹400–₹1200 • After review", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = ServiceProBlue)
                        }
                    }
                }
            }
        }

        // Search Capsule Bar (Matches Screenshot 2)
        item {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(46.dp)
                    .clickable { onNavigate(AppScreen.ALL_SERVICES) }
                    .testTag("search_services_capsule"),
                shape = CircleShape,
                color = Color(0xFFF1F5F9),
                border = BorderStroke(1.dp, Color(0xFFE2E8F0))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        tint = Color(0xFF64748B),
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "Search for another service...",
                        fontSize = 13.sp,
                        color = Color(0xFF64748B)
                    )
                }
            }
        }

        // Quick Actions Row (Matches Screenshot 2)
        item {
            Column {
                Text(
                    text = "Quick Actions",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0C1E3C)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    QuickActionChip(
                        title = "Schedule Later",
                        icon = Icons.Default.CalendarMonth,
                        modifier = Modifier.weight(1f),
                        onClick = { onNavigate(AppScreen.MY_BOOKINGS) }
                    )
                    QuickActionChip(
                        title = "Safety Checked Pros",
                        icon = Icons.Default.VerifiedUser,
                        modifier = Modifier.weight(1.2f),
                        onClick = { onNavigate(AppScreen.CHOOSE_WORKER) }
                    )
                    QuickActionChip(
                        title = "Offers 20% OFF",
                        icon = Icons.Default.LocalOffer,
                        modifier = Modifier.weight(1f),
                        onClick = { onNavigate(AppScreen.PRICING_RULE) }
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // User's specialized portals: RoomKhoj & Worker Pricing Rule
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    QuickActionChip(
                        title = "🏢 RoomKhoj (Hostel/Godown)",
                        icon = Icons.Default.Apartment,
                        modifier = Modifier.weight(1f),
                        onClick = { onNavigate(AppScreen.ROOM_KHOJ) }
                    )
                    QuickActionChip(
                        title = "📊 Worker Pricing Rule",
                        icon = Icons.Default.Calculate,
                        modifier = Modifier.weight(1f),
                        onClick = { onNavigate(AppScreen.PRICING_RULE) }
                    )
                }
            }
        }

        // Section: "Explore All 20 Services" Header + Button (Matches Screenshot 3)
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = Localization.get("explore_20", uiState.language),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF0C1E3C)
                    )
                    Text("Prices start from ₹299 • 20 services", fontSize = 11.sp, color = Color(0xFF64748B))
                }
                TextButton(
                    onClick = { onNavigate(AppScreen.ALL_SERVICES) },
                    modifier = Modifier.testTag("view_all_services_btn")
                ) {
                    Text("View All →", fontWeight = FontWeight.Bold, color = ServiceProBlue)
                }
            }
        }

        // 4-Column Compact Grid Preview of Top 8 Services on Home screen (Matching Screenshot 3)
        items(ServiceProRepository.services.take(8).chunked(4)) { rowServices ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                rowServices.forEach { service ->
                    Box(modifier = Modifier.weight(1f)) {
                        CompactServiceGridCard(
                            service = service,
                            language = uiState.language,
                            onClick = {
                                viewModel.selectService(service)
                                onNavigate(AppScreen.SERVICE_DETAIL)
                            }
                        )
                    }
                }
                // Fill remaining space if less than 4
                repeat(4 - rowServices.size) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

fun getServicePhotoRes(serviceId: String): Int {
    return when (serviceId) {
        "1" -> R.drawable.photo_electrician
        "2" -> R.drawable.photo_plumber
        "3" -> R.drawable.photo_ac
        "4" -> R.drawable.photo_carpenter
        "5" -> R.drawable.photo_painter
        "6" -> R.drawable.photo_cleaning
        "7" -> R.drawable.photo_pest
        "8" -> R.drawable.photo_appliance
        "9" -> R.drawable.photo_ro
        "10" -> R.drawable.photo_cctv
        "11" -> R.drawable.photo_gardening
        "12" -> R.drawable.photo_laundry
        "13" -> R.drawable.photo_beauty
        "14" -> R.drawable.photo_tutor
        "15" -> R.drawable.photo_driver
        "16" -> R.drawable.photo_security
        "17" -> R.drawable.photo_moving
        "18" -> R.drawable.photo_welding
        "19" -> R.drawable.photo_computer
        "20" -> R.drawable.photo_homerepair
        "21" -> R.drawable.photo_moving
        "22" -> R.drawable.photo_crane
        else -> R.drawable.photo_electrician
    }
}

// -------------------------------------------------------------
// FEATURED SERVICE CARD (Matches Screenshot 2)
// -------------------------------------------------------------
@Composable
private fun FeaturedServiceCard(
    service: ServiceItem,
    language: Language,
    onClick: () -> Unit
) {
    val photoRes = getServicePhotoRes(service.id)

    val (badgeText, badgeColor, subDetail) = when (service.id) {
        "1" -> Triple(
            "FAST RESPONSE",
            Color(0xFF00A3C4),
            "Wiring • Repairs • Installation\n4.9★ • 24/7"
        )
        "2" -> Triple(
            "TOP RATED",
            Color(0xFF059669),
            "Leaks • Drains • Pipes\n• 4.8★ • 30min"
        )
        "3" -> Triple(
            "SUMMER SPECIAL",
            Color(0xFF0284C7),
            "AC Repair • Service\nCooling • 4.9★ • Today"
        )
        "4" -> Triple(
            "EXPERT",
            Color(0xFFEA580C),
            "Door repair • Locks • Hinges\n4.6★ • Today"
        )
        else -> Triple(
            service.tag,
            ServiceProBlue,
            "Certified Pros • Verified\nFast Response"
        )
    }

    Card(
        modifier = Modifier
            .width(160.dp)
            .height(225.dp)
            .clickable(onClick = onClick)
            .testTag("featured_service_${service.id}"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            // Real photo background cropped from user's demo screens
            Image(
                painter = painterResource(id = photoRes),
                contentDescription = service.nameEn,
                contentScale = androidx.compose.ui.layout.ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            // Top-left Pill Badge
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = badgeColor,
                modifier = Modifier
                    .padding(top = 12.dp, start = 12.dp)
            ) {
                Text(
                    text = badgeText,
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                )
            }

            // Bottom Dark Gradient Overlay with Title, details & circular arrow button
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .background(
                        Brush.verticalGradient(
                            listOf(Color.Transparent, Color(0x99000000), Color(0xFA050B14))
                        )
                    )
                    .padding(horizontal = 12.dp, vertical = 10.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Bottom
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = if (language == Language.HI) service.nameHi else service.nameEn,
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = subDetail,
                            fontSize = 10.sp,
                            lineHeight = 13.sp,
                            color = Color.White.copy(alpha = 0.9f),
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                    }

                    // Circular White Button with arrow matching Screenshot 2
                    Box(
                        modifier = Modifier
                            .size(34.dp)
                            .shadow(3.dp, CircleShape)
                            .clip(CircleShape)
                            .background(Color.White)
                            .clickable(onClick = onClick),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowForward,
                            contentDescription = "Go to service",
                            tint = Color(0xFF0C1E3C),
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }
        }
    }
}

// -------------------------------------------------------------
// COMPACT SERVICE GRID CARD (Matches Screenshot 3 - 4 Columns)
// -------------------------------------------------------------
@Composable
fun CompactServiceGridCard(
    service: ServiceItem,
    language: Language,
    onClick: () -> Unit
) {
    val photoRes = getServicePhotoRes(service.id)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .testTag("compact_service_${service.id}"),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Thumbnail matching Screenshot 3 with real photo
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(68.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(Color(0xFFF1F5F9)),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = photoRes),
                contentDescription = service.nameEn,
                contentScale = androidx.compose.ui.layout.ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }

        Spacer(modifier = Modifier.height(5.dp))

        Text(
            text = if (language == Language.HI) service.nameHi else service.nameEn,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF0C1E3C),
            textAlign = TextAlign.Center,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        Text(
            text = "Starting ₹${service.basePrice}",
            fontSize = 9.sp,
            fontWeight = FontWeight.SemiBold,
            color = ServiceProBlue,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun ServiceGridCard(
    service: ServiceItem,
    language: Language,
    onClick: () -> Unit
) {
    CompactServiceGridCard(
        service = service,
        language = language,
        onClick = onClick
    )
}

@Composable
private fun QuickActionChip(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Surface(
        modifier = modifier
            .height(38.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(8.dp),
        color = Color.White,
        border = BorderStroke(1.dp, CardBorder)
    ) {
        Row(
            modifier = Modifier.fillMaxSize().padding(horizontal = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(icon, contentDescription = null, modifier = Modifier.size(15.dp), tint = ServiceProBlue)
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                title,
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextPrimary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

// -------------------------------------------------------------
// ALL 20 SERVICES GRID SCREEN (Matches Screenshot 3 - 4 Columns)
// -------------------------------------------------------------
@Composable
fun AllServicesGridScreen(
    viewModel: MainViewModel,
    onBack: () -> Unit,
    onSelectService: (ServiceItem) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundSlate)
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onBack) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color(0xFF0C1E3C))
                }
                Text(
                    text = "Explore All 20 Services",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0C1E3C)
                )
            }
            Text(
                text = "Prices start from ₹299 • 20 services",
                fontSize = 11.sp,
                color = Color(0xFF64748B)
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // 4-Column Grid exactly matching Screenshot 3
        LazyVerticalGrid(
            columns = GridCells.Fixed(4),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(ServiceProRepository.services) { service ->
                CompactServiceGridCard(
                    service = service,
                    language = uiState.language,
                    onClick = { onSelectService(service) }
                )
            }
        }
    }
}

// -------------------------------------------------------------
// SERVICE DETAIL SCREEN (Matches Screenshot 12)
// -------------------------------------------------------------
@Composable
fun ServiceDetailScreen(
    viewModel: MainViewModel,
    onBack: () -> Unit,
    onBookNow: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val service = uiState.selectedService ?: ServiceProRepository.services.first()
    val language = uiState.language

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundSlate)
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        // Top Nav
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back")
            }
            Text("Service Detail", fontWeight = FontWeight.Bold, fontSize = 18.sp)
            IconButton(onClick = { /* Share */ }) {
                Icon(Icons.Default.Share, contentDescription = "Share")
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Hero Service Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            border = BorderStroke(1.dp, CardBorder)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(140.dp)
                        .clip(RoundedCornerShape(12.dp))
                ) {
                    Image(
                        painter = painterResource(id = getServicePhotoRes(service.id)),
                        contentDescription = service.nameEn,
                        contentScale = androidx.compose.ui.layout.ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = ServiceProLightBlue
                ) {
                    Text(
                        text = "${service.nameEn.uppercase()} REPAIR SERVICE",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = ServiceProDarkBlue,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = if (language == Language.HI) service.nameHi else service.nameEn,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Black,
                    color = TextPrimary
                )

                Text(
                    text = if (language == Language.HI) service.descriptionHi else service.descriptionEn,
                    fontSize = 12.sp,
                    color = TextSecondary
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("★★★★★ 4.8 (1.2k reviews)", fontSize = 11.sp, color = RatingAmber, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.width(8.dp))
                    Surface(shape = RoundedCornerShape(4.dp), color = GuaranteeLightGreen) {
                        Text("✓ Verified Service", fontSize = 10.sp, color = DarkGreen, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // "Worker can set own price" Pricing Box (Screenshot 12)
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            border = BorderStroke(1.dp, CardBorder)
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("Price starting", fontSize = 11.sp, color = TextSecondary)
                        Text("₹${service.basePrice}", fontSize = 22.sp, fontWeight = FontWeight.Black, color = ServiceProBlue)
                    }
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = ServiceProLightBlue
                    ) {
                        Text(
                            text = "Worker can set own price",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = ServiceProDarkBlue,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Card(
                    shape = RoundedCornerShape(8.dp),
                    colors = CardDefaults.cardColors(containerColor = BackgroundSlate)
                ) {
                    Column(modifier = Modifier.padding(10.dp)) {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Technician Service Fee", fontSize = 12.sp)
                            Text("₹500", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        }
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("GST (10%) Owner Account", fontSize = 12.sp)
                            Text("₹50", fontWeight = FontWeight.SemiBold, fontSize = 12.sp, color = OfferBorderRed)
                        }
                        Divider(modifier = Modifier.padding(vertical = 4.dp))
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Total", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                            Text("₹550", fontWeight = FontWeight.Black, fontSize = 14.sp, color = ServiceProBlue)
                        }
                    }
                }

                Text(
                    text = "* You can negotiate price with technician. Final price may vary based on issue.",
                    fontSize = 10.sp,
                    color = TextSecondary,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Services Included (Screenshot 12)
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            border = BorderStroke(1.dp, CardBorder)
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Text("Services Included", fontSize = 15.sp, fontWeight = FontWeight.Bold)
                Text("शामिल सेवाएं / What we do", fontSize = 11.sp, color = TextSecondary)
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    ServiceIncludedChip(icon = Icons.Default.Build, title = "Inspection", subtitle = "निरीक्षण")
                    ServiceIncludedChip(icon = Icons.Default.SwapHoriz, title = "Parts Fitting", subtitle = "पुर्जे बदलाव")
                    ServiceIncludedChip(icon = Icons.Default.Settings, title = "Tuning", subtitle = "सर्विस")
                    ServiceIncludedChip(icon = Icons.Default.Verified, title = "Warranty", subtitle = "वारंटी")
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Technician card preview (Screenshot 12)
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            border = BorderStroke(1.dp, CardBorder)
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Text("Technician / On-ground expert", fontSize = 12.sp, color = TextSecondary)
                Spacer(modifier = Modifier.height(6.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(46.dp)
                            .clip(CircleShape)
                            .background(ServiceProBlue),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("RK", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("Rajesh Kumar", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                            Spacer(modifier = Modifier.width(6.dp))
                            Icon(Icons.Default.Verified, contentDescription = null, tint = GuaranteeGreen, modifier = Modifier.size(16.dp))
                        }
                        Text("5+ Years Experience • Marhaura Pro Expert", fontSize = 11.sp, color = TextSecondary)
                        Text("🧰 Tools: Complete toolkit, testing meter & safety gear", fontSize = 10.sp, color = TextSecondary)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        Button(
            onClick = onBookNow,
            modifier = Modifier.fillMaxWidth().height(50.dp).testTag("book_service_now_btn"),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = ServiceProBlue)
        ) {
            Text("Book Service Now → Choose Worker", fontSize = 15.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun ServiceIncludedChip(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    subtitle: String
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .size(38.dp)
                .clip(CircleShape)
                .background(ServiceProLightBlue),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, contentDescription = null, tint = ServiceProBlue, modifier = Modifier.size(20.dp))
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(title, fontSize = 11.sp, fontWeight = FontWeight.Bold)
        Text(subtitle, fontSize = 9.sp, color = TextSecondary)
    }
}

// -------------------------------------------------------------
// CHOOSE WORKER SCREEN (Matches Screenshot 1)
// -------------------------------------------------------------
@Composable
fun ChooseWorkerScreen(
    viewModel: MainViewModel,
    onBack: () -> Unit,
    onWorkerSelected: (Worker) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val service = uiState.selectedService ?: ServiceProRepository.services.first()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundSlate)
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        // Top Header: Back Arrow + Centered ServicePro Shield & Marhaura
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            IconButton(
                onClick = onBack,
                modifier = Modifier.align(Alignment.CenterStart)
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBackIosNew,
                    contentDescription = "Back",
                    tint = Color(0xFF0C1E3C),
                    modifier = Modifier.size(18.dp)
                )
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .clip(RoundedCornerShape(6.dp))
                            .background(ServiceProBlue),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Shield,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = null,
                            tint = ServiceProBlue,
                            modifier = Modifier.size(9.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "ServicePro",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF0C1E3C)
                    )
                }
                Text(
                    text = "Marhaura",
                    fontSize = 11.sp,
                    color = Color(0xFF64748B)
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Center Title & Subtitle matching Screenshot 1
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Choose Worker",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF0C1E3C)
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = "Available workers in Marhaura • Today • 3 workers available",
                fontSize = 12.sp,
                color = Color(0xFF64748B),
                textAlign = TextAlign.Center
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.weight(1f)
        ) {
            items(ServiceProRepository.workers) { worker ->
                WorkerItemCard(
                    worker = worker,
                    onBook = {
                        viewModel.confirmBooking(worker, service)
                        onWorkerSelected(worker)
                    }
                )
            }

            item {
                Spacer(modifier = Modifier.height(10.dp))
                // Footer matching Screenshot 1
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "ⓘ All prices are in Indian Rupees (₹) • Secure payments via ServicePro",
                        fontSize = 11.sp,
                        color = Color(0xFF64748B),
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    // Carousel dots indicator
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .width(14.dp)
                                .height(5.dp)
                                .clip(RoundedCornerShape(3.dp))
                                .background(ServiceProBlue)
                        )
                        Box(
                            modifier = Modifier
                                .size(5.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFCBD5E1))
                        )
                        Box(
                            modifier = Modifier
                                .size(5.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFCBD5E1))
                        )
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
private fun WorkerItemCard(
    worker: Worker,
    onBook: () -> Unit
) {
    val (badgeBg, badgeTextColor, avatarTheme) = when (worker.id) {
        "w1" -> Triple(Color(0xFFE0F2FE), Color(0xFF0284C7), 1) // Alex R (cyan/blue)
        "w2" -> Triple(Color(0xFFF3E8FF), Color(0xFF7E22CE), 2) // Mia K (purple)
        "w3" -> Triple(Color(0xFFFFEDD5), Color(0xFFC2410C), 3) // James T (orange)
        "w4" -> Triple(Color(0xFFE0F2FE), Color(0xFF0284C7), 1) // Ravi
        "w5" -> Triple(Color(0xFFF3E8FF), Color(0xFF7E22CE), 2) // Priya
        else -> Triple(Color(0xFFDCFCE7), Color(0xFF15803D), 3) // Rajesh
    }

    val avatarRes = when (worker.id) {
        "w1" -> R.drawable.avatar_alex
        "w2" -> R.drawable.avatar_mia
        "w3" -> R.drawable.avatar_james
        "w4" -> R.drawable.avatar_alex
        "w5" -> R.drawable.avatar_mia
        "w8_crane" -> R.drawable.photo_crane
        else -> R.drawable.avatar_rahul
    }
    val pricing = ServiceProRepository.calculateWorkerRule(worker.hourlyRate)

    var showReviews by remember { mutableStateOf(false) }
    var userRating by remember { mutableStateOf(5) }
    var userFeedback by remember { mutableStateOf("") }
    var hasSubmittedReview by remember { mutableStateOf(false) }
    val context = LocalContext.current

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("worker_card_${worker.id}"),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Left: Portrait Photo Avatar + Info
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Image(
                        painter = painterResource(id = avatarRes),
                        contentDescription = worker.name,
                        contentScale = androidx.compose.ui.layout.ContentScale.Crop,
                        modifier = Modifier
                            .size(54.dp)
                            .clip(CircleShape)
                            .border(1.5.dp, Color(0xFFCBD5E1), CircleShape)
                    )

                    Spacer(modifier = Modifier.width(12.dp))

                    Column {
                        Text(
                            text = worker.name,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = Color(0xFF0C1E3C)
                        )

                        Spacer(modifier = Modifier.height(2.dp))

                        // Stars rating & jobs matching Screenshot 1
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "★★★★★",
                                fontSize = 11.sp,
                                color = RatingAmber,
                                letterSpacing = 1.sp
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "${worker.rating} • ${worker.jobsCompleted} jobs",
                                fontSize = 11.sp,
                                color = Color(0xFF64748B)
                            )
                        }

                        Spacer(modifier = Modifier.height(4.dp))

                        // Specialization Tag Capsule matching Screenshot 1
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = badgeBg
                        ) {
                            Text(
                                text = "${worker.serviceCategory} • ${worker.experienceYears}+ years",
                                fontSize = 10.sp,
                                color = badgeTextColor,
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                            )
                        }
                    }
                }

                // Right: Price per hour + [Book] button matching Screenshot 1
                Column(
                    horizontalAlignment = Alignment.End,
                    modifier = Modifier.padding(start = 8.dp)
                ) {
                    Text(
                        text = "₹${worker.hourlyRate}",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Black,
                        color = Color(0xFF0C1E3C)
                    )
                    Text(
                        text = "per hour",
                        fontSize = 11.sp,
                        color = Color(0xFF64748B)
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Button(
                        onClick = onBook,
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp),
                        modifier = Modifier
                            .height(34.dp)
                            .testTag("book_worker_${worker.id}"),
                        colors = ButtonDefaults.buttonColors(containerColor = ServiceProBlue)
                    ) {
                        Text(
                            text = "Book",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }

            // Specialization Trust Badges
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Surface(shape = RoundedCornerShape(4.dp), color = GuaranteeLightGreen) {
                    Text("✓ Verified Pro", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = DarkGreen, modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp))
                }
                Surface(shape = RoundedCornerShape(4.dp), color = ServiceProLightBlue) {
                    Text("⚡ Fast Response", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = ServiceProDarkBlue, modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp))
                }
                Surface(shape = RoundedCornerShape(4.dp), color = Color(0xFFF1F5F9)) {
                    Text("🛡️ Safety Checked", fontSize = 9.sp, fontWeight = FontWeight.Medium, color = TextSecondary, modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp))
                }
            }

            // 50% Online Advance & 50% Cash/Online on completion rule notice
            Spacer(modifier = Modifier.height(8.dp))
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = Color(0xFFF8FAFC),
                border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp, vertical = 6.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("💳 50% Advance Online: ₹${pricing.advanceOnline50Percent}", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = ServiceProBlue)
                        Text("GST ₹${pricing.gstDeductedFromAdvance} (10%) included to Owner", fontSize = 9.sp, color = Color(0xFF64748B))
                    }
                    Column(horizontalAlignment = Alignment.End) {
                        Text("50% Balance: ₹${pricing.remaining50Percent}", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = GuaranteeGreen)
                        Text("Cash ya Online (Worker ko)", fontSize = 9.sp, color = Color(0xFF64748B))
                    }
                }
            }

            // Verified Reviews & Ratings Component Toggle
            Spacer(modifier = Modifier.height(6.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(6.dp))
                    .clickable { showReviews = !showReviews }
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Star, contentDescription = null, tint = RatingAmber, modifier = Modifier.size(15.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Verified Provider Feedback (${worker.reviews.size} reviews) • Trust Score 98%",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = ServiceProBlue
                    )
                }
                Icon(
                    imageVector = if (showReviews) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = null,
                    tint = TextSecondary,
                    modifier = Modifier.size(16.dp)
                )
            }

            // Expandable Reviews List
            if (showReviews) {
                Spacer(modifier = Modifier.height(6.dp))
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFFF8FAFC), RoundedCornerShape(8.dp))
                        .padding(10.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    worker.reviews.forEach { review ->
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color.White, RoundedCornerShape(6.dp))
                                .border(1.dp, CardBorder, RoundedCornerShape(6.dp))
                                .padding(8.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(review.authorName, fontWeight = FontWeight.Bold, fontSize = 11.sp, color = TextPrimary)
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("✓ Verified", fontSize = 9.sp, color = DarkGreen, fontWeight = FontWeight.Bold)
                                }
                                Text("★".repeat(review.rating), fontSize = 10.sp, color = RatingAmber)
                            }
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(review.comment, fontSize = 11.sp, color = TextSecondary)
                            Text(review.dateStr, fontSize = 9.sp, color = TextMuted)
                        }
                    }

                    // Interactive Rating & Feedback Input
                    Divider(color = CardBorder, modifier = Modifier.padding(vertical = 4.dp))
                    if (!hasSubmittedReview) {
                        Column {
                            Text("Rate ${worker.name}:", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                (1..5).forEach { star ->
                                    Icon(
                                        imageVector = if (star <= userRating) Icons.Default.Star else Icons.Default.StarBorder,
                                        contentDescription = null,
                                        tint = RatingAmber,
                                        modifier = Modifier
                                            .size(20.dp)
                                            .clickable { userRating = star }
                                    )
                                }
                                Spacer(modifier = Modifier.weight(1f))
                                Button(
                                    onClick = {
                                        hasSubmittedReview = true
                                        Toast.makeText(context, "Review submitted! Thank you for rating ${worker.name}", Toast.LENGTH_SHORT).show()
                                    },
                                    shape = RoundedCornerShape(6.dp),
                                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 2.dp),
                                    modifier = Modifier.height(28.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = ServiceProBlue)
                                ) {
                                    Text("Submit", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    } else {
                        Text("✓ Your 5-star rating has been recorded!", fontSize = 10.sp, color = DarkGreen, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

// -------------------------------------------------------------
// ADD MEDIA SCREEN (Matches Screenshots 9 & 11)
// -------------------------------------------------------------
@Composable
fun AddMediaScreen(
    viewModel: MainViewModel,
    onBack: () -> Unit,
    onContinue: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundSlate)
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back")
            }
            Text("Add photos & video", fontWeight = FontWeight.Bold, fontSize = 20.sp)
        }

        Text(
            text = "Show us the issue — up to 3 photos and 1 video (15s max)",
            fontSize = 12.sp,
            color = TextSecondary,
            modifier = Modifier.padding(start = 12.dp)
        )

        Spacer(modifier = Modifier.height(14.dp))

        // Viewfinder Mockup (Screenshot 9)
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp)
                .clickable {
                    viewModel.addPhotoSample()
                    Toast.makeText(context, "Photo captured & analyzed!", Toast.LENGTH_SHORT).show()
                },
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B))
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                Column(
                    modifier = Modifier.align(Alignment.Center),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(60.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.CameraAlt, contentDescription = null, tint = Color.White, modifier = Modifier.size(32.dp))
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("TAKING PHOTO / FOCUSING", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    Text("Tap to simulate shutter click", color = Color.White.copy(alpha = 0.7f), fontSize = 10.sp)
                }

                // Shutter button simulation
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 12.dp)
                        .size(44.dp)
                        .clip(CircleShape)
                        .border(3.dp, Color.White, CircleShape)
                        .background(Color.White.copy(alpha = 0.3f))
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text("UPLOADED (${uiState.uploadedPhotoCount} PHOTOS • 1 VIDEO)", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = TextSecondary)
        Spacer(modifier = Modifier.height(8.dp))

        // Uploaded items row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            (1..3).forEach { index ->
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .height(90.dp),
                    shape = RoundedCornerShape(10.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = BorderStroke(1.dp, if (index <= uiState.uploadedPhotoCount) ServiceProBlue else CardBorder)
                ) {
                    Box(modifier = Modifier.fillMaxSize()) {
                        Column(
                            modifier = Modifier.align(Alignment.Center),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(Icons.Default.Image, contentDescription = null, tint = ServiceProBlue, modifier = Modifier.size(24.dp))
                            Text("$index/3", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        }
                        if (index <= uiState.uploadedPhotoCount) {
                            Surface(
                                shape = CircleShape,
                                color = ServiceProBlue,
                                modifier = Modifier.align(Alignment.TopEnd).padding(4.dp).size(16.dp)
                            ) {
                                Icon(Icons.Default.Check, contentDescription = null, tint = Color.White, modifier = Modifier.size(10.dp))
                            }
                        }
                    }
                }
            }

            // Video item
            Card(
                modifier = Modifier
                    .weight(1f)
                    .height(90.dp),
                shape = RoundedCornerShape(10.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(1.dp, GuaranteeGreen)
            ) {
                Box(modifier = Modifier.fillMaxSize()) {
                    Column(
                        modifier = Modifier.align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(Icons.Default.PlayCircle, contentDescription = null, tint = GuaranteeGreen, modifier = Modifier.size(24.dp))
                        Text("VIDEO 15s", fontSize = 9.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedButton(
            onClick = {
                viewModel.addPhotoSample()
                Toast.makeText(context, "Added sample photo", Toast.LENGTH_SHORT).show()
            },
            modifier = Modifier.fillMaxWidth().height(44.dp),
            shape = RoundedCornerShape(10.dp)
        ) {
            Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text("Add More Photos / Retake")
        }

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = onContinue,
            modifier = Modifier.fillMaxWidth().height(50.dp).testTag("media_continue_btn"),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = ServiceProBlue)
        ) {
            Text("Continue → View Instant Estimate", fontSize = 15.sp, fontWeight = FontWeight.Bold)
        }
    }
}

// -------------------------------------------------------------
// BOOKING CONFIRMED SCREEN (Matches Screenshot 2)
// -------------------------------------------------------------
@Composable
fun BookingConfirmedScreen(
    viewModel: MainViewModel,
    onViewDetails: () -> Unit,
    onCancel: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val booking = uiState.activeBooking ?: ServiceProRepository.defaultBookings.first()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundSlate)
            .statusBarsPadding()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(20.dp))

        // Big Green Checkmark
        Box(
            modifier = Modifier
                .size(80.dp)
                .clip(CircleShape)
                .background(GuaranteeGreen),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = "Confirmed",
                tint = Color.White,
                modifier = Modifier.size(48.dp)
            )
        }

        Spacer(modifier = Modifier.height(18.dp))

        Text(
            text = "Booking Confirmed!",
            fontSize = 24.sp,
            fontWeight = FontWeight.Black,
            color = TextPrimary
        )
        Text(
            text = "Your service is booked in Marhaura",
            fontSize = 13.sp,
            color = TextSecondary
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Confirmation Card (Matches Screenshot 2)
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            border = BorderStroke(1.dp, CardBorder),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                // Date & Time
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(GuaranteeLightGreen),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.CalendarToday, contentDescription = null, tint = GuaranteeGreen, modifier = Modifier.size(20.dp))
                    }
                    Spacer(modifier = Modifier.width(14.dp))
                    Column {
                        Text(booking.dateTimeStr, fontWeight = FontWeight.Bold, fontSize = 15.sp, color = TextPrimary)
                        Text("Date & Time", fontSize = 11.sp, color = TextSecondary)
                    }
                }

                Divider(modifier = Modifier.padding(vertical = 12.dp), color = CardBorder)

                // Service Address
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(ServiceProLightBlue),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.LocationOn, contentDescription = null, tint = ServiceProBlue, modifier = Modifier.size(20.dp))
                    }
                    Spacer(modifier = Modifier.width(14.dp))
                    Column {
                        Text(booking.address, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = TextPrimary)
                        Text("Service Address (Marhaura, Saran)", fontSize = 11.sp, color = TextSecondary)
                    }
                }

                Divider(modifier = Modifier.padding(vertical = 12.dp), color = CardBorder)

                // Assigned Worker with Photo
                val avatarRes = when {
                    booking.workerName.contains("Alex", ignoreCase = true) -> R.drawable.avatar_alex
                    booking.workerName.contains("Mia", ignoreCase = true) -> R.drawable.avatar_mia
                    booking.workerName.contains("James", ignoreCase = true) -> R.drawable.avatar_james
                    else -> R.drawable.avatar_rahul
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        painter = painterResource(id = avatarRes),
                        contentDescription = booking.workerName,
                        contentScale = androidx.compose.ui.layout.ContentScale.Crop,
                        modifier = Modifier
                            .size(46.dp)
                            .clip(CircleShape)
                            .border(1.5.dp, Color(0xFFCBD5E1), CircleShape)
                    )
                    Spacer(modifier = Modifier.width(14.dp))
                    Column {
                        Text(booking.workerName, fontWeight = FontWeight.Bold, fontSize = 15.sp, color = TextPrimary)
                        Text("Assigned Pro • ${booking.serviceName}", fontSize = 11.sp, color = TextSecondary)
                    }
                }

                Divider(modifier = Modifier.padding(vertical = 12.dp), color = CardBorder)

                // 50% Advance Online & 50% Remaining (Cash or Online) breakdown
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFFF8FAFC), RoundedCornerShape(10.dp))
                        .padding(10.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Total Service Amount", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                        Text("₹${booking.totalAmount}", fontSize = 14.sp, fontWeight = FontWeight.Black, color = ServiceProBlue)
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text("1. 50% Advance Paid Online", fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = GuaranteeGreen)
                            Text("GST (10%) ₹${booking.gstDeductedFromAdvance} deducted to Owner", fontSize = 9.sp, color = OfferBorderRed)
                        }
                        Text("₹${booking.advancePaidOnline}", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = GuaranteeGreen)
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text("2. Remaining 50% on Completion", fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = TextPrimary)
                            Text("Pay via CASH ya ONLINE directly to worker", fontSize = 9.sp, color = Color(0xFF64748B))
                        }
                        Text("₹${booking.remainingBalance}", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Helpline Contact Row (Call + WhatsApp + Telegram)
        HelplineContactRow()

        Spacer(modifier = Modifier.height(16.dp))

        // View Details / Track button
        Button(
            onClick = onViewDetails,
            modifier = Modifier.fillMaxWidth().height(50.dp).testTag("view_details_track_btn"),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = ServiceProBlue)
        ) {
            Text("View Details & Track Live", fontSize = 15.sp, fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(10.dp))

        TextButton(
            onClick = onCancel,
            modifier = Modifier.testTag("manage_cancel_booking_btn")
        ) {
            Text("Manage booking • Cancel", color = TextSecondary, fontSize = 13.sp)
        }
    }
}

// -------------------------------------------------------------
// LIVE TRACKING SCREEN (Matches Screenshots 6 & 7)
// -------------------------------------------------------------
@Composable
fun LiveTrackingScreen(
    viewModel: MainViewModel,
    onBack: () -> Unit,
    onViewInvoice: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    val booking = uiState.activeBooking ?: ServiceProRepository.defaultBookings.first()

    // Animation for vehicle beacon along path
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val beaconPulse by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulseAnim"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundSlate)
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        // Top Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back")
            }
            Text("Live Tracking", fontWeight = FontWeight.Black, fontSize = 22.sp, color = TextPrimary)
            Spacer(modifier = Modifier.weight(1f))
            Surface(shape = RoundedCornerShape(12.dp), color = GuaranteeLightGreen) {
                Row(
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(modifier = Modifier.size(6.dp).clip(CircleShape).background(GuaranteeGreen))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Live", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = DarkGreen)
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // 3-Step Progress Bar: Accepted -> On the way -> Arrived
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            TrackingStepItem(label = "Accepted", sub = "Just now", isDone = true)
            Divider(modifier = Modifier.weight(1f).height(2.dp), color = ServiceProBlue)
            TrackingStepItem(label = "On the way", sub = "Now", isDone = true, isActive = true)
            Divider(modifier = Modifier.weight(1f).height(2.dp), color = CardBorder)
            TrackingStepItem(label = "Arrived", sub = "Est. soon", isDone = false)
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Stylized Interactive Map Canvas
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(210.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFF1F5F9)),
            border = BorderStroke(1.dp, CardBorder)
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val w = size.width
                    val h = size.height

                    // Background grid roads
                    drawLine(Color(0xFFE2E8F0), Offset(0f, h * 0.3f), Offset(w, h * 0.35f), strokeWidth = 8f)
                    drawLine(Color(0xFFE2E8F0), Offset(0f, h * 0.7f), Offset(w, h * 0.65f), strokeWidth = 10f)
                    drawLine(Color(0xFFE2E8F0), Offset(w * 0.3f, 0f), Offset(w * 0.25f, h), strokeWidth = 8f)
                    drawLine(Color(0xFFE2E8F0), Offset(w * 0.7f, 0f), Offset(w * 0.75f, h), strokeWidth = 8f)

                    // Route path in blue
                    val routePath = Path().apply {
                        moveTo(w * 0.2f, h * 0.25f)
                        cubicTo(w * 0.4f, h * 0.35f, w * 0.5f, h * 0.6f, w * 0.75f, h * 0.7f)
                    }
                    drawPath(routePath, color = Color(0xFF0052CC), style = Stroke(width = 8f, cap = StrokeCap.Round))

                    // Origin Pin (Worker start)
                    drawCircle(Color(0xFF0052CC), radius = 12f, center = Offset(w * 0.2f, h * 0.25f))
                    drawCircle(Color.White, radius = 5f, center = Offset(w * 0.2f, h * 0.25f))

                    // Destination Pin (Your Location)
                    drawCircle(Color(0xFF00875A), radius = 14f, center = Offset(w * 0.75f, h * 0.7f))
                    drawCircle(Color.White, radius = 6f, center = Offset(w * 0.75f, h * 0.7f))

                    // Vehicle Animated Pulse Beacon
                    val vehicleX = w * 0.48f
                    val vehicleY = h * 0.48f
                    drawCircle(Color(0x330052CC), radius = 24f * beaconPulse, center = Offset(vehicleX, vehicleY))
                    drawCircle(Color(0xFF0052CC), radius = 12f, center = Offset(vehicleX, vehicleY))
                    drawCircle(Color.White, radius = 5f, center = Offset(vehicleX, vehicleY))
                }

                // Map badge: 1.2 km away • ETA 8 min
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = ServiceProBlue,
                    modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 12.dp)
                ) {
                    Text(
                        text = "1.2 km away • ETA 8 min to Marhaura Bazar",
                        color = Color.White,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Worker on the way card (Screenshot 6 & 7)
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            border = BorderStroke(1.dp, CardBorder),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(54.dp)
                            .clip(CircleShape)
                            .background(ServiceProBlue),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(booking.workerName.take(2).uppercase(), color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "${booking.workerName} is on the way",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("★ 4.9", fontSize = 11.sp, color = RatingAmber, fontWeight = FontWeight.Bold)
                            Text(" • ${booking.serviceName} • En route", fontSize = 11.sp, color = TextSecondary)
                        }
                        Text("Vehicle: BR 04 M 1464", fontSize = 10.sp, color = TextSecondary)
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // OTP Box (Screenshot 7: OTP for service 4582)
                Card(
                    shape = RoundedCornerShape(10.dp),
                    colors = CardDefaults.cardColors(containerColor = ChipBackground),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 14.dp, vertical = 10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Lock, contentDescription = null, tint = ServiceProBlue, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("OTP for service:", fontSize = 12.sp, color = TextSecondary)
                        }

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = booking.otp,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Black,
                                color = TextPrimary
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            IconButton(
                                onClick = {
                                    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                    clipboard.setPrimaryClip(ClipData.newPlainText("OTP", booking.otp))
                                    Toast.makeText(context, "OTP ${booking.otp} copied!", Toast.LENGTH_SHORT).show()
                                },
                                modifier = Modifier.size(28.dp).testTag("copy_otp_btn")
                            ) {
                                Icon(Icons.Default.ContentCopy, contentDescription = "Copy OTP", modifier = Modifier.size(16.dp))
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Call & Message buttons (dials +91 9205161464)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Button(
                        onClick = { makePhoneCall(context, booking.workerPhone) },
                        modifier = Modifier.weight(1f).height(46.dp).testTag("call_worker_btn"),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = ServiceProBlue)
                    ) {
                        Icon(Icons.Default.Phone, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Call ${booking.workerName.takeWhile { it != ' ' }}")
                    }

                    OutlinedButton(
                        onClick = { openWhatsApp(context, booking.workerPhone) },
                        modifier = Modifier.weight(1f).height(46.dp).testTag("message_worker_btn"),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Icon(Icons.Default.Chat, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Message")
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // View Bill / Invoice action button
        OutlinedButton(
            onClick = onViewInvoice,
            modifier = Modifier.fillMaxWidth().height(48.dp).testTag("view_invoice_from_track_btn"),
            shape = RoundedCornerShape(10.dp)
        ) {
            Icon(Icons.Default.ReceiptLong, contentDescription = null, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text("View Invoice & Pricing Formula (₹${booking.totalAmount})")
        }
    }
}

@Composable
private fun TrackingStepItem(
    label: String,
    sub: String,
    isDone: Boolean,
    isActive: Boolean = false
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
                .background(if (isDone) ServiceProBlue else Color(0xFFE2E8F0)),
            contentAlignment = Alignment.Center
        ) {
            if (isActive) {
                Icon(Icons.Default.DirectionsCar, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
            } else if (isDone) {
                Icon(Icons.Default.Check, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
            } else {
                Box(modifier = Modifier.size(10.dp).clip(CircleShape).background(Color.White))
            }
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(label, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = if (isDone) TextPrimary else TextMuted)
        Text(sub, fontSize = 9.sp, color = TextSecondary)
    }
}
