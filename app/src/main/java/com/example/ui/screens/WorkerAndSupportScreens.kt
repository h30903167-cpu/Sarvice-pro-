package com.example.ui.screens

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.ServiceProRepository
import com.example.model.Booking
import com.example.model.BookingStatus
import com.example.model.WorkerAvailability
import com.example.ui.AppScreen
import com.example.ui.MainViewModel
import com.example.ui.components.HelplineContactRow
import com.example.ui.components.TodayOfferBanner
import com.example.ui.components.WorkerGuaranteedGreenBox
import com.example.ui.components.makePhoneCall
import com.example.ui.components.openTelegram
import com.example.ui.components.openWhatsApp
import com.example.ui.theme.*

// -------------------------------------------------------------
// WORKER DASHBOARD SCREEN (Matches Screenshots 3 & 5)
// -------------------------------------------------------------
@Composable
fun WorkerDashboardScreen(
    viewModel: MainViewModel,
    onNavigateToPricingRule: () -> Unit,
    onNavigateToInvoice: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundSlate)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Front 3 Offer Banner teaser
        item {
            TodayOfferBanner(onClick = onNavigateToPricingRule)
        }

        // Header (Matches Screenshot 3 & 5: Provider Dashboard - Hello Rahul)
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = ServiceProLightBlue),
                border = BorderStroke(1.dp, ServiceProBlue.copy(alpha = 0.3f))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.avatar_rahul),
                        contentDescription = "Rahul Sharma",
                        contentScale = androidx.compose.ui.layout.ContentScale.Crop,
                        modifier = Modifier
                            .size(60.dp)
                            .clip(CircleShape)
                            .border(2.dp, ServiceProBlue, CircleShape)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Rahul Sharma",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Black,
                            color = ServiceProDarkBlue
                        )
                        Spacer(modifier = Modifier.height(3.dp))
                        Surface(shape = RoundedCornerShape(10.dp), color = ServiceProBlue) {
                            Text(
                                text = "Electrician • AC Repair • Plumbing ✓",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(3.dp))
                        Text(
                            text = "★ 4.8 (128 reviews • 24 jobs completed)",
                            fontSize = 11.sp,
                            color = Color(0xFF64748B)
                        )
                    }
                }
            }
        }

        // Top 2 KPI Cards: Earnings Today & Total Jobs (Matches Screenshot 3)
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Earnings Today Card
                Card(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = BorderStroke(1.dp, CardBorder)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .clip(CircleShape)
                                    .background(ServiceProLightBlue),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("₹", color = ServiceProBlue, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            }
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Earnings Today", fontSize = 11.sp, color = TextSecondary)
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "₹${uiState.workerDailyEarnings}",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Black,
                            color = TextPrimary
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "↑ +12% from yesterday",
                            fontSize = 11.sp,
                            color = GuaranteeGreen,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }

                // Total Jobs Card
                Card(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = BorderStroke(1.dp, CardBorder)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .clip(CircleShape)
                                    .background(ServiceProLightBlue),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.WorkOutline, contentDescription = null, tint = ServiceProBlue, modifier = Modifier.size(16.dp))
                            }
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Total Jobs", fontSize = 11.sp, color = TextSecondary)
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "${uiState.workerJobsToday}",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Black,
                            color = TextPrimary
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "Completed today",
                            fontSize = 11.sp,
                            color = TextSecondary
                        )
                    }
                }
            }
        }

        // Availability Status Toggle (Matches Screenshot 3)
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(1.dp, CardBorder)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Availability Status",
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            color = TextPrimary
                        )
                        Text(
                            text = if (uiState.isWorkerOnline) "You are online and will receive new job requests in Marhaura" else "You are offline. Turn on to receive jobs.",
                            fontSize = 11.sp,
                            color = TextSecondary
                        )
                    }
                    Switch(
                        checked = uiState.isWorkerOnline,
                        onCheckedChange = { viewModel.toggleWorkerOnline(it) },
                        modifier = Modifier.testTag("worker_online_switch"),
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.White,
                            checkedTrackColor = ServiceProBlue
                        )
                    )
                }
            }
        }

        // Public Profile & 3 Statuses (Matches Screenshot 5)
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(1.dp, CardBorder)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text("Worker Status Mode", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        StatusSelectChip(
                            label = "Online",
                            isSelected = uiState.workerStatus == WorkerAvailability.ONLINE,
                            color = GuaranteeGreen,
                            onClick = { viewModel.setWorkerStatus(WorkerAvailability.ONLINE) },
                            modifier = Modifier.weight(1f)
                        )
                        StatusSelectChip(
                            label = "Offline",
                            isSelected = uiState.workerStatus == WorkerAvailability.OFFLINE,
                            color = TextSecondary,
                            onClick = { viewModel.setWorkerStatus(WorkerAvailability.OFFLINE) },
                            modifier = Modifier.weight(1f)
                        )
                        StatusSelectChip(
                            label = "Working",
                            isSelected = uiState.workerStatus == WorkerAvailability.WORKING,
                            color = ServiceProBlue,
                            onClick = { viewModel.setWorkerStatus(WorkerAvailability.WORKING) },
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Public profile visible to customers", fontSize = 12.sp, color = TextPrimary)
                        Switch(
                            checked = uiState.isPublicProfileVisible,
                            onCheckedChange = { viewModel.togglePublicProfile(it) },
                            colors = SwitchDefaults.colors(checkedTrackColor = GuaranteeGreen)
                        )
                    }
                }
            }
        }

        // Worker Summary Dashboard Row (Jobs Completed 24, Hours Worked 6h 15m, This Week Rs 3,200)
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(1.dp, CardBorder)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text("Overall Partner Stats", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        PartnerStatItem(title = "Jobs Completed", value = "24", sub = "92% success")
                        PartnerStatItem(title = "Hours Worked", value = "6h 15m", sub = "Today active")
                        PartnerStatItem(title = "This Week", value = "Rs 3,200", sub = "incl. GST 10%")
                    }
                }
            }
        }

        // Worker Guarantee Reminder
        item {
            WorkerGuaranteedGreenBox(amountText = "100% Direct Payout")
        }

        // Today's Activity (Matches Screenshot 3)
        item {
            Text(
                text = "Today's Activity",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
        }

        items(ServiceProRepository.providerActivity) { act ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(1.dp, CardBorder)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(34.dp)
                                .clip(CircleShape)
                                .background(GuaranteeLightGreen),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Check, contentDescription = null, tint = GuaranteeGreen, modifier = Modifier.size(18.dp))
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(act.title, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = TextPrimary)
                            Text("Completed • ${act.time}", fontSize = 11.sp, color = TextSecondary)
                        }
                    }

                    Column(horizontalAlignment = Alignment.End) {
                        Text("+₹${act.workerEarning}", fontSize = 14.sp, fontWeight = FontWeight.Black, color = GuaranteeGreen)
                        Text("GST ₹${act.gstOwner}", fontSize = 10.sp, color = OfferBorderRed)
                    }
                }
            }
        }

        item {
            Button(
                onClick = onNavigateToInvoice,
                modifier = Modifier.fillMaxWidth().height(48.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = ServiceProBlue)
            ) {
                Text("View Sample Worker Payout Slip & GST Bill")
            }
        }
    }
}

@Composable
private fun StatusSelectChip(
    label: String,
    isSelected: Boolean,
    color: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .height(36.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(18.dp),
        color = if (isSelected) color.copy(alpha = 0.15f) else ChipBackground,
        border = BorderStroke(1.dp, if (isSelected) color else CardBorder)
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .clip(CircleShape)
                    .background(if (isSelected) color else TextMuted)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(label, fontSize = 11.sp, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium, color = if (isSelected) color else TextSecondary)
        }
    }
}

@Composable
private fun PartnerStatItem(
    title: String,
    value: String,
    sub: String
) {
    Column {
        Text(title, fontSize = 10.sp, color = TextSecondary)
        Text(value, fontSize = 16.sp, fontWeight = FontWeight.Black, color = TextPrimary)
        Text(sub, fontSize = 9.sp, color = GuaranteeGreen)
    }
}

// -------------------------------------------------------------
// BOOKINGS LIST SCREEN (Matches Screenshot 5)
// -------------------------------------------------------------
@Composable
fun BookingsListScreen(
    viewModel: MainViewModel,
    onTrackBooking: (Booking) -> Unit
) {
    val bookings by viewModel.bookings.collectAsState()
    var selectedTab by remember { mutableStateOf(0) } // 0: Upcoming, 1: Completed
    val context = LocalContext.current

    val upcoming = bookings.filter { it.status == BookingStatus.ON_THE_WAY || it.status == BookingStatus.ACCEPTED }
    val completed = bookings.filter { it.status == BookingStatus.COMPLETED || it.status == BookingStatus.CANCELLED }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundSlate)
            .padding(16.dp)
    ) {
        Text("My Bookings", fontSize = 22.sp, fontWeight = FontWeight.Black, color = TextPrimary)
        Text("All on-demand services in Marhaura", fontSize = 12.sp, color = TextSecondary)

        Spacer(modifier = Modifier.height(14.dp))

        // Tabs: Upcoming vs Completed (Screenshot 5)
        TabRow(
            selectedTabIndex = selectedTab,
            containerColor = Color.White,
            modifier = Modifier.clip(RoundedCornerShape(12.dp)).border(1.dp, CardBorder, RoundedCornerShape(12.dp))
        ) {
            Tab(
                selected = selectedTab == 0,
                onClick = { selectedTab = 0 },
                text = { Text("Upcoming (${upcoming.size})", fontWeight = FontWeight.Bold) }
            )
            Tab(
                selected = selectedTab == 1,
                onClick = { selectedTab = 1 },
                text = { Text("Completed (${completed.size})", fontWeight = FontWeight.Bold) }
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        val currentList = if (selectedTab == 0) upcoming else completed

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(currentList) { booking ->
                BookingCardItem(
                    booking = booking,
                    onTrack = { onTrackBooking(booking) },
                    onCancel = {
                        viewModel.cancelActiveBooking()
                        Toast.makeText(context, "Booking cancelled", Toast.LENGTH_SHORT).show()
                    },
                    onRebook = {
                        Toast.makeText(context, "Rebooking ${booking.serviceName}...", Toast.LENGTH_SHORT).show()
                    }
                )
            }
        }
    }
}

@Composable
private fun BookingCardItem(
    booking: Booking,
    onTrack: () -> Unit,
    onCancel: () -> Unit,
    onRebook: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, CardBorder),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(ServiceProLightBlue),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Bolt, contentDescription = null, tint = ServiceProBlue, modifier = Modifier.size(22.dp))
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(booking.serviceName, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                        Text(booking.dateTimeStr, fontSize = 11.sp, color = TextSecondary)
                    }
                }

                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = when (booking.status) {
                        BookingStatus.ON_THE_WAY -> Color(0xFFEFF6FF)
                        BookingStatus.COMPLETED -> GuaranteeLightGreen
                        BookingStatus.CANCELLED -> Color(0xFFFEE2E2)
                        else -> ChipBackground
                    }
                ) {
                    Text(
                        text = when (booking.status) {
                            BookingStatus.ON_THE_WAY -> "ON THE WAY"
                            BookingStatus.ACCEPTED -> "UPCOMING"
                            BookingStatus.COMPLETED -> "COMPLETED"
                            BookingStatus.CANCELLED -> "CANCELLED"
                            else -> "ACCEPTED"
                        },
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = when (booking.status) {
                            BookingStatus.ON_THE_WAY -> ServiceProBlue
                            BookingStatus.COMPLETED -> DarkGreen
                            BookingStatus.CANCELLED -> OfferBorderRed
                            else -> TextSecondary
                        },
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Person, contentDescription = null, tint = TextSecondary, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Pro: ${booking.workerName}", fontSize = 12.sp, color = TextPrimary)
                }
                Text("₹${booking.totalAmount} (incl. 10% GST)", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = ServiceProBlue)
            }

            Divider(modifier = Modifier.padding(vertical = 10.dp), color = CardBorder)

            if (booking.status == BookingStatus.ON_THE_WAY || booking.status == BookingStatus.ACCEPTED) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = onTrack,
                        modifier = Modifier.weight(1f).height(38.dp),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = ServiceProBlue)
                    ) {
                        Icon(Icons.Default.LocationOn, contentDescription = null, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Track Booking", fontSize = 12.sp)
                    }

                    OutlinedButton(
                        onClick = onCancel,
                        modifier = Modifier.weight(1f).height(38.dp),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Cancel", fontSize = 12.sp, color = OfferBorderRed)
                    }
                }
            } else {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("★★★★★ Rated 4.8", fontSize = 11.sp, color = RatingAmber, fontWeight = FontWeight.Bold)
                    Button(
                        onClick = onRebook,
                        modifier = Modifier.height(34.dp),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = ServiceProBlue)
                    ) {
                        Text("Rebook →", fontSize = 11.sp)
                    }
                }
            }
        }
    }
}

// -------------------------------------------------------------
// CONTACT / SUPPORT SCREEN (Front 6)
// -------------------------------------------------------------
@Composable
fun ContactSupportScreen(
    viewModel: MainViewModel
) {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundSlate)
            .padding(16.dp)
    ) {
        Text("⚡ ServicePro Support", fontSize = 22.sp, fontWeight = FontWeight.Black, color = ServiceProDarkBlue)
        Text("Marhaura, Saran (Bihar) 24x7 Helpline", fontSize = 12.sp, color = TextSecondary)

        Spacer(modifier = Modifier.height(16.dp))

        // Front 6 Box:
        // Call / WhatsApp: 1800-123-SERVICE | +91 9205161464
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            border = BorderStroke(1.5.dp, ServiceProBlue)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Emergency & Customer Helpline",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = TextPrimary
                )
                Text(
                    text = "Direct call with ServicePro Owner & Support Center",
                    fontSize = 11.sp,
                    color = TextSecondary
                )

                Spacer(modifier = Modifier.height(14.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = { makePhoneCall(context, ServiceProRepository.OWNER_PHONE) },
                        modifier = Modifier.weight(1f).height(46.dp).testTag("helpline_call_btn"),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = ServiceProBlue),
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Icon(Icons.Default.Phone, contentDescription = null, modifier = Modifier.size(15.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Call Helpline", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }

                    Button(
                        onClick = { openWhatsApp(context, "919205161464") },
                        modifier = Modifier.weight(1.1f).height(46.dp).testTag("helpline_whatsapp_btn"),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = GuaranteeGreen),
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Icon(Icons.Default.Chat, contentDescription = null, modifier = Modifier.size(15.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("WhatsApp", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }

                    Button(
                        onClick = { openTelegram(context, "ServiceProMarhaura") },
                        modifier = Modifier.weight(1.1f).height(46.dp).testTag("helpline_telegram_btn"),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0088CC)),
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Icon(Icons.Default.Send, contentDescription = null, modifier = Modifier.size(15.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Telegram", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedButton(
                    onClick = { makePhoneCall(context, "18001237378") },
                    modifier = Modifier.fillMaxWidth().height(42.dp),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text("📞 Connect to Official Toll-Free Helpline Desk")
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Quick Direct Support Bar
        HelplineContactRow()

        Spacer(modifier = Modifier.height(16.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            border = BorderStroke(1.dp, CardBorder)
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Text("Service Coverage Area", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                Spacer(modifier = Modifier.height(6.dp))
                Text("• Marhaura Main Bazar, Railway Station Rd, Saran\n• All 20 Wards & Industrial Corridor\n• RoomKhoj: All India Network (Bihar, UP, Delhi, Mumbai)", fontSize = 12.sp, color = TextSecondary, lineHeight = 20.sp)
            }
        }
    }
}
