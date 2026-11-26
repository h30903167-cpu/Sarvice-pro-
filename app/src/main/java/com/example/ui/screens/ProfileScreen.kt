package com.example.ui.screens

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.ServiceProRepository
import com.example.model.UserAccount
import com.example.model.UserRole
import com.example.ui.AppScreen
import com.example.ui.MainViewModel
import com.example.ui.components.HelplineContactRow
import com.example.ui.theme.*

@Composable
fun ProfileScreen(
    viewModel: MainViewModel,
    onNavigate: (AppScreen) -> Unit,
    onLogout: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    val account = uiState.userAccount
    val isWorker = (uiState.userRole == UserRole.WORKER)

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
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = if (isWorker) "🛠️ Partner Profile / श्रमिक प्रोफ़ाइल" else "👤 User Profile / ग्राहक प्रोफ़ाइल",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = ServiceProDarkBlue
            )
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = if (isWorker) GuaranteeLightGreen else ServiceProLightBlue,
                border = BorderStroke(1.dp, if (isWorker) GuaranteeGreen else ServiceProBlue)
            ) {
                Text(
                    text = if (isWorker) "✓ VERIFIED WORKER" else "✓ VERIFIED USER",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isWorker) DarkGreen else ServiceProBlue,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Main Profile Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            border = BorderStroke(1.dp, CardBorder)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Profile Avatar
                    Box(
                        modifier = Modifier
                            .size(68.dp)
                            .clip(CircleShape)
                            .background(if (isWorker) ServiceProBlue else Color(0xFFE0E7FF)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = if (isWorker) Icons.Default.Engineering else Icons.Default.Person,
                            contentDescription = "Avatar",
                            tint = if (isWorker) Color.White else ServiceProBlue,
                            modifier = Modifier.size(38.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(14.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = account.fullName.ifEmpty { if (isWorker) "Rahul Sharma" else "Priyanka" },
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF0C1E3C)
                        )
                        Text(
                            text = "ID: ${account.userId.ifEmpty { if (isWorker) "SP-WRK-920516" else "SP-PRIYANKA-9205" }}",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = ServiceProBlue
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Phone, contentDescription = null, tint = TextSecondary, modifier = Modifier.size(12.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(text = account.phone, fontSize = 11.sp, color = TextSecondary)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))
                Divider(color = CardBorder)
                Spacer(modifier = Modifier.height(10.dp))

                // Area / Location
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.LocationOn, contentDescription = null, tint = ServiceProBlue, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = "Area: ${uiState.selectedArea}", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                    }
                    Text(
                        text = "Change / बदलें 📍",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = ServiceProBlue,
                        modifier = Modifier.clickable { viewModel.showLocationPicker(true) }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Document Verification Section
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            border = BorderStroke(1.dp, CardBorder)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "📄 Document & Verification Status (दस्तावेज़ स्थिति)",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = ServiceProDarkBlue
                )
                Spacer(modifier = Modifier.height(10.dp))

                DocumentStatusRow(
                    title = "Aadhaar Card (आधार कार्ड)",
                    number = "4829-XXXX-5161",
                    isVerified = true,
                    subtitle = "Govt. Guidelines Compliant • Front & Back Verified ✓"
                )

                Divider(modifier = Modifier.padding(vertical = 8.dp), color = CardBorder)

                DocumentStatusRow(
                    title = "Photo ID & Selfie (फोटो सत्यापन)",
                    number = "Profile Photo ID",
                    isVerified = true,
                    subtitle = "Face Match 100% Approved ✓"
                )

                if (isWorker) {
                    Divider(modifier = Modifier.padding(vertical = 8.dp), color = CardBorder)

                    DocumentStatusRow(
                        title = "Crane & Heavy Vehicle License",
                        number = "BR-04-CRANE-HEAVY-2018",
                        isVerified = true,
                        subtitle = "Commercial Towing & Crane Operator Approved ✓"
                    )

                    Divider(modifier = Modifier.padding(vertical = 8.dp), color = CardBorder)

                    DocumentStatusRow(
                        title = "Bank Account / UPI Payout",
                        number = "Direct 100% Pura Settlement",
                        isVerified = true,
                        subtitle = "Zero Commission Platform Payout Active ✓"
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // If Worker: Show Quick Dashboard & Working Services
        if (isWorker) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = ServiceProLightBlue),
                border = BorderStroke(1.dp, ServiceProBlue.copy(alpha = 0.4f))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "🏗️ Registered Services / कार्य सेवाएँ:",
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        color = ServiceProDarkBlue
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        listOf("🏗️ Crane & Towing", "⚡ Electrician", "🔧 Plumber", "🛒 Kirana Store").forEach { s ->
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = Color.White,
                                border = BorderStroke(1.dp, ServiceProBlue.copy(alpha = 0.3f))
                            ) {
                                Text(
                                    text = s,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = ServiceProBlue,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Button(
                        onClick = { onNavigate(AppScreen.WORKER_DASHBOARD) },
                        modifier = Modifier.fillMaxWidth().height(44.dp),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = ServiceProBlue)
                    ) {
                        Icon(Icons.Default.Dashboard, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Open Full Partner Dashboard →", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        } else {
            // Customer Referral & Quick Actions
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF0FDF4)),
                border = BorderStroke(1.dp, GuaranteeGreen)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.CardGiftcard, contentDescription = null, tint = GuaranteeGreen, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "🎁 Referral Code / रेफरल कोड",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = DarkGreen
                        )
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Share with family & friends in Marhaura to get 50% discount on first service booking!",
                        fontSize = 11.sp,
                        color = TextSecondary
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = Color.White,
                        border = BorderStroke(1.dp, GuaranteeGreen)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = "SP-MARHAURA-50", fontWeight = FontWeight.ExtraBold, fontSize = 14.sp, color = DarkGreen)
                            Text(
                                text = "Copy Code",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = ServiceProBlue,
                                modifier = Modifier.clickable {
                                    Toast.makeText(context, "Referral Code Copied: SP-MARHAURA-50", Toast.LENGTH_SHORT).show()
                                }
                            )
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        // 24x7 Helpline Desk
        HelplineContactRow()

        Spacer(modifier = Modifier.height(16.dp))

        // Role Switch Card
        OutlinedButton(
            onClick = {
                val nextRole = if (isWorker) UserRole.CUSTOMER else UserRole.WORKER
                viewModel.selectRole(nextRole)
                Toast.makeText(context, "Switched to ${if (nextRole == UserRole.WORKER) "Worker Partner" else "Customer User"}", Toast.LENGTH_SHORT).show()
            },
            modifier = Modifier.fillMaxWidth().height(48.dp),
            shape = RoundedCornerShape(12.dp),
            border = BorderStroke(1.5.dp, ServiceProBlue)
        ) {
            Icon(Icons.Default.Sync, contentDescription = null, tint = ServiceProBlue)
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = if (isWorker) "Switch to Customer Mode (ग्राहक मोड)" else "Switch to Worker Mode (कारीगर मोड)",
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp,
                color = ServiceProBlue
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Logout Button
        Button(
            onClick = onLogout,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .testTag("profile_logout_btn"),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFDC2626))
        ) {
            Icon(Icons.Default.Logout, contentDescription = null, tint = Color.White)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Logout of ServicePro (लॉगआउट)", fontWeight = FontWeight.Bold, fontSize = 13.sp)
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
fun DocumentStatusRow(
    title: String,
    number: String,
    isVerified: Boolean,
    subtitle: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(text = title, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
            Text(text = number, fontSize = 11.sp, color = ServiceProBlue, fontWeight = FontWeight.SemiBold)
            Text(text = subtitle, fontSize = 10.sp, color = DarkGreen)
        }
        if (isVerified) {
            Surface(
                shape = CircleShape,
                color = GuaranteeLightGreen,
                modifier = Modifier.size(24.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(Icons.Default.Check, contentDescription = "Verified", tint = DarkGreen, modifier = Modifier.size(16.dp))
                }
            }
        }
    }
}
