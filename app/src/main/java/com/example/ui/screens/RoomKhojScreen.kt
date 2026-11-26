package com.example.ui.screens

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.ServiceProRepository
import com.example.model.Language
import com.example.model.PropertyType
import com.example.model.RoomKhojListing
import com.example.ui.MainViewModel
import com.example.ui.components.HelplineContactRow
import com.example.ui.components.makePhoneCall
import com.example.ui.components.openWhatsApp
import com.example.ui.theme.*

@Composable
fun RoomKhojScreen(
    viewModel: MainViewModel
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    var selectedTab by remember { mutableStateOf<PropertyType?>(null) }
    var searchQuery by remember { mutableStateOf("") }
    var isStudentFilterActive by remember { mutableStateOf(false) }

    val allListings = ServiceProRepository.roomKhojListings
    val filteredListings = remember(selectedTab, searchQuery, isStudentFilterActive) {
        allListings.filter { listing ->
            val matchType = selectedTab == null || listing.propertyType == selectedTab
            val matchSearch = searchQuery.isBlank() ||
                    listing.title.contains(searchQuery, ignoreCase = true) ||
                    listing.location.contains(searchQuery, ignoreCase = true)
            val matchStudent = !isStudentFilterActive || listing.isStudentFreeGst
            matchType && matchSearch && matchStudent
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundSlate)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            // Front 5 Header & RoomMate App Direct Download & Search Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = ServiceProDarkBlue)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(42.dp)
                                    .clip(CircleShape)
                                    .background(Color.White),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Apartment,
                                    contentDescription = "RoomMate DP",
                                    tint = ServiceProBlue,
                                    modifier = Modifier.size(26.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = "RoomKhoj",
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Black,
                                    color = Color.White
                                )
                                Text(
                                    text = "All India Home + Godown Warehouse Search",
                                    fontSize = 11.sp,
                                    color = Color.White.copy(alpha = 0.85f)
                                )
                            }
                        }

                        // RoomMate App Badge
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = GuaranteeGreen
                        ) {
                            Text(
                                text = "RoomMate App",
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "RoomMate App link se download karke pure India ke rooms, flats, student hostels aur warehouses direct search karein:",
                        fontSize = 11.sp,
                        color = Color.White.copy(alpha = 0.9f),
                        lineHeight = 15.sp
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // Direct Download & Search Action Buttons
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = {
                                try {
                                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.roommate.app"))
                                    context.startActivity(intent)
                                } catch (e: Exception) {
                                    Toast.makeText(context, "Opening RoomMate App Download Link...", Toast.LENGTH_SHORT).show()
                                }
                            },
                            modifier = Modifier.weight(1f).height(38.dp),
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = GuaranteeGreen)
                        ) {
                            Icon(Icons.Default.Download, contentDescription = null, tint = Color.White, modifier = Modifier.size(15.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Download App", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }

                        OutlinedButton(
                            onClick = {
                                Toast.makeText(context, "Searching All India on RoomMate Portal...", Toast.LENGTH_SHORT).show()
                            },
                            modifier = Modifier.weight(1f).height(38.dp),
                            shape = RoundedCornerShape(8.dp),
                            border = BorderStroke(1.dp, Color.White),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White)
                        ) {
                            Icon(Icons.Default.TravelExplore, contentDescription = null, tint = Color.White, modifier = Modifier.size(15.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Search on App", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Fixed GST Rules:
                    // Shop Mall GST: ₹80 Fix
                    // Room GST: ₹5 Fix
                    // Student ke liye FREE
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        GstPillBadge(title = "Room GST", value = "₹5 Fix", color = OfferGold, modifier = Modifier.weight(1f))
                        GstPillBadge(title = "Mall/Shop GST", value = "₹80 Fix", color = Color(0xFF60A5FA), modifier = Modifier.weight(1f))
                        GstPillBadge(title = "Students", value = "FREE (₹0)", color = GuaranteeGreen, modifier = Modifier.weight(1f))
                    }
                }
            }
        }

        // Helpline support quick bar
        item {
            HelplineContactRow()
        }

        // Search Input
        item {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier.fillMaxWidth().testTag("roomkhoj_search_input"),
                placeholder = { Text("Search Marhaura, Bihar, Delhi, Godown, Room...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = ServiceProBlue) },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { searchQuery = "" }) {
                            Icon(Icons.Default.Clear, contentDescription = "Clear")
                        }
                    }
                },
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White
                )
            )
        }

        // Category Filter Chips
        item {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                item {
                    FilterChip(
                        selected = (selectedTab == null),
                        onClick = { selectedTab = null },
                        label = { Text("All Listings") }
                    )
                }
                item {
                    FilterChip(
                        selected = (selectedTab == PropertyType.ROOM),
                        onClick = { selectedTab = PropertyType.ROOM },
                        label = { Text("Rooms (₹5 GST)") }
                    )
                }
                item {
                    FilterChip(
                        selected = (selectedTab == PropertyType.GODOWN_WAREHOUSE),
                        onClick = { selectedTab = PropertyType.GODOWN_WAREHOUSE },
                        label = { Text("Godown / Warehouse") }
                    )
                }
                item {
                    FilterChip(
                        selected = (selectedTab == PropertyType.SHOP_MALL),
                        onClick = { selectedTab = PropertyType.SHOP_MALL },
                        label = { Text("Shop / Mall (₹80 GST)") }
                    )
                }
                item {
                    FilterChip(
                        selected = isStudentFilterActive,
                        onClick = { isStudentFilterActive = !isStudentFilterActive },
                        label = { Text("🎓 Student FREE") },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = GuaranteeLightGreen,
                            selectedLabelColor = DarkGreen
                        )
                    )
                }
            }
        }

        // Listing Cards
        items(filteredListings) { listing ->
            RoomKhojCard(
                listing = listing,
                onCallMalik = {
                    makePhoneCall(context, listing.malikPhone)
                },
                onWhatsAppMalik = {
                    openWhatsApp(context, listing.malikPhone)
                }
            )
        }
    }
}

@Composable
private fun RoomKhojCard(
    listing: RoomKhojListing,
    onCallMalik: () -> Unit,
    onWhatsAppMalik: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth().testTag("listing_card_${listing.id}"),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, CardBorder),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = listing.title,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = TextPrimary
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.LocationOn, contentDescription = null, tint = TextSecondary, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(3.dp))
                        Text(listing.location, fontSize = 11.sp, color = TextSecondary)
                    }
                }

                // Rent pill
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = ServiceProLightBlue
                ) {
                    Column(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        horizontalAlignment = Alignment.End
                    ) {
                        Text(
                            text = "₹${listing.monthlyRent}/mo",
                            fontWeight = FontWeight.Black,
                            fontSize = 14.sp,
                            color = ServiceProDarkBlue
                        )
                        Text(
                            text = "${listing.areaSqFt} sq.ft",
                            fontSize = 9.sp,
                            color = TextSecondary
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Front 5 GST & Student Badges
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                // Fixed GST Badge
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = if (listing.propertyType == PropertyType.SHOP_MALL) Color(0xFFEFF6FF) else Color(0xFFFEF3C7)
                ) {
                    Text(
                        text = if (listing.propertyType == PropertyType.SHOP_MALL) "Shop Mall GST: ₹80 Fix" else "Room GST: ₹${listing.gstFix} Fix",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (listing.propertyType == PropertyType.SHOP_MALL) ServiceProBlue else Color(0xFF92400E),
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }

                if (listing.isStudentFreeGst) {
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = GuaranteeLightGreen
                    ) {
                        Text(
                            text = "Student ke liye FREE",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = DarkGreen,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Amenities / Features tags
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                listing.features.take(3).forEach { feature ->
                    Surface(
                        shape = RoundedCornerShape(4.dp),
                        color = ChipBackground
                    ) {
                        Text(
                            text = "✓ $feature",
                            fontSize = 10.sp,
                            color = TextSecondary,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }
            }

            Divider(modifier = Modifier.padding(vertical = 10.dp), color = CardBorder)

            // Malik Info & "Call Malik Button pe 9205161464" (Front 5 Requirement!)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = listing.malikName,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 12.sp,
                        color = TextPrimary
                    )
                    Text(
                        text = "Verified Property Owner • Direct Contact",
                        fontSize = 11.sp,
                        color = GuaranteeGreen,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    OutlinedButton(
                        onClick = onWhatsAppMalik,
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                        modifier = Modifier.height(36.dp).testTag("whatsapp_malik_${listing.id}")
                    ) {
                        Icon(Icons.Default.Chat, contentDescription = null, modifier = Modifier.size(14.dp), tint = GuaranteeGreen)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Chat", fontSize = 11.sp, color = GuaranteeGreen, fontWeight = FontWeight.Bold)
                    }

                    Button(
                        onClick = onCallMalik,
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = GuaranteeGreen),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                        modifier = Modifier.height(36.dp).testTag("call_malik_${listing.id}")
                    ) {
                        Icon(Icons.Default.Phone, contentDescription = null, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Call Malik", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
private fun GstPillBadge(
    title: String,
    value: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(8.dp),
        color = Color.White.copy(alpha = 0.15f),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(vertical = 6.dp, horizontal = 4.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(title, fontSize = 9.sp, color = Color.White.copy(alpha = 0.8f))
            Text(value, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = color)
        }
    }
}
