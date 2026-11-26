package com.example.ui.screens

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.ServiceProRepository
import com.example.ui.MainViewModel
import com.example.ui.components.WorkerGuaranteedGreenBox
import com.example.ui.theme.*

@Composable
fun InvoiceScreen(
    viewModel: MainViewModel,
    onBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    val booking = uiState.activeBooking ?: ServiceProRepository.defaultBookings.first()

    val workerCharge = booking.workerCharge
    val gstAmount = booking.gstAmount
    val totalAmount = booking.totalAmount

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundSlate)
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        // Top back bar
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack, modifier = Modifier.testTag("invoice_back_btn")) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = TextPrimary)
            }
            Text(
                text = "Tax Invoice & Bill Summary",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = TextPrimary
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Official Bill Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            border = BorderStroke(1.dp, CardBorder),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                // Header of the invoice
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Bolt, contentDescription = null, tint = ServiceProBlue, modifier = Modifier.size(20.dp))
                            Text("ServicePro", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = ServiceProBlue)
                        }
                        Text("Marhaura, Saran (Bihar)", fontSize = 11.sp, color = TextSecondary)
                        Text("GSTIN / Reg: 10AABCS1464M1Z2", fontSize = 10.sp, color = TextMuted)
                    }
                    Column(horizontalAlignment = Alignment.End) {
                        when (booking.paymentStage) {
                            com.example.model.PaymentStage.UPFRONT_50_PAID -> {
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = Color(0xFFFEF3C7),
                                    border = BorderStroke(1.dp, Color(0xFFF59E0B))
                                ) {
                                    Text("50% PAID (ADVANCE)", modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp), fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color(0xFFB45309))
                                }
                            }
                            com.example.model.PaymentStage.FULLY_PAID_ONLINE -> {
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = GuaranteeLightGreen,
                                    border = BorderStroke(1.dp, GuaranteeGreen)
                                ) {
                                    Text("FULLY PAID (100% ONLINE)", modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp), fontSize = 10.sp, fontWeight = FontWeight.Bold, color = DarkGreen)
                                }
                            }
                            com.example.model.PaymentStage.FULLY_PAID_CASH -> {
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = Color(0xFFE0F2FE),
                                    border = BorderStroke(1.dp, ServiceProBlue)
                                ) {
                                    Text("FULLY PAID (50% CASH)", modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp), fontSize = 10.sp, fontWeight = FontWeight.Bold, color = ServiceProBlue)
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("Inv #${booking.id.takeLast(6).uppercase()}", fontSize = 11.sp, fontWeight = FontWeight.Medium)
                        Text(booking.dateTimeStr.replace("Tomorrow • ", ""), fontSize = 10.sp, color = TextSecondary)
                    }
                }

                Divider(modifier = Modifier.padding(vertical = 14.dp), color = CardBorder)

                // Service Details
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Column {
                        Text("Service Name", fontSize = 11.sp, color = TextSecondary)
                        Text(booking.serviceName, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                    }
                    Column(horizontalAlignment = Alignment.End) {
                        Text("Assigned Pro", fontSize = 11.sp, color = TextSecondary)
                        Text(booking.workerName, fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = ServiceProBlue)
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Text("Service Location", fontSize = 11.sp, color = TextSecondary)
                Text(booking.address, fontSize = 12.sp, color = TextPrimary)

                Divider(modifier = Modifier.padding(vertical = 14.dp), color = CardBorder)

                // Front 4 Specified Line Items:
                // My Working Charge: Jo Worker ko milega
                // GST 10%: Owner Account me
                // Customer Total
                Text("Itemized Charges Breakdown", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("My Working Charge", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = TextPrimary)
                        Text("(Jo Worker ko milega • 100% Pura)", fontSize = 10.sp, color = GuaranteeGreen, fontWeight = FontWeight.Medium)
                    }
                    Text("₹$workerCharge", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("GST (काम अनुसार 5%, 8% या 10%)", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = TextPrimary)
                        Text("(Official Owner Account me)", fontSize = 10.sp, color = OfferBorderRed, fontWeight = FontWeight.Medium)
                    }
                    Text("₹$gstAmount", fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = OfferBorderRed)
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Platform Convenience Fee", fontSize = 12.sp, color = TextSecondary)
                    Text("₹0 (FREE)", fontSize = 12.sp, color = GuaranteeGreen, fontWeight = FontWeight.SemiBold)
                }

                Divider(modifier = Modifier.padding(vertical = 12.dp), color = CardBorder)

                // Customer Total
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Customer Total Payable", fontSize = 15.sp, fontWeight = FontWeight.ExtraBold, color = TextPrimary)
                    Text("₹$totalAmount", fontSize = 20.sp, fontWeight = FontWeight.Black, color = ServiceProBlue)
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Payment Split Summary Box with Visual Badges
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = Color(0xFFF8FAFC),
                    border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                            Text("1. Advance Paid Upfront (50%)", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = GuaranteeGreen)
                            Surface(shape = RoundedCornerShape(4.dp), color = GuaranteeLightGreen) {
                                Text("✓ PAID ₹${booking.advancePaidOnline}", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = DarkGreen, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                            }
                        }
                        Text("  • Full GST (₹${booking.gstDeductedFromAdvance}) deducted to Official Owner Account", fontSize = 9.sp, color = OfferBorderRed)

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                            Text("2. Remaining Balance (50%)", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                            if (booking.remainingBalance > 0) {
                                Surface(shape = RoundedCornerShape(4.dp), color = Color(0xFFFEF3C7)) {
                                    Text("DUE ₹${booking.remainingBalance}", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color(0xFFB45309), modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                                }
                            } else {
                                Surface(shape = RoundedCornerShape(4.dp), color = GuaranteeLightGreen) {
                                    Text("✓ SETTLED", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = DarkGreen, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                                }
                            }
                        }
                        Text("  • Payment Mode: ${booking.remainingPaymentMethod}", fontSize = 9.sp, color = TextSecondary)
                    }
                }

                // Interactive Split Payment Action If Remaining Balance Due
                if (booking.remainingBalance > 0) {
                    Spacer(modifier = Modifier.height(14.dp))
                    Card(
                        shape = RoundedCornerShape(10.dp),
                        colors = CardDefaults.cardColors(containerColor = ServiceProLightBlue),
                        border = BorderStroke(1.dp, ServiceProBlue.copy(alpha = 0.4f)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(
                                text = "💳 Pay Remaining 50% Balance (₹${booking.remainingBalance}):",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = ServiceProDarkBlue
                            )
                            Text(
                                text = "Kaam pura hone par worker ko online ya cash dono me se kisi bhi tarike se pay kar sakte hain:",
                                fontSize = 10.sp,
                                color = TextSecondary
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Button(
                                    onClick = {
                                        viewModel.completeRemainingPayment(booking.id, "ONLINE")
                                        Toast.makeText(context, "Remaining ₹${booking.remainingBalance} Paid Online via UPI!", Toast.LENGTH_LONG).show()
                                    },
                                    modifier = Modifier.weight(1f).height(38.dp),
                                    shape = RoundedCornerShape(8.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = ServiceProBlue),
                                    contentPadding = PaddingValues(horizontal = 4.dp, vertical = 0.dp)
                                ) {
                                    Icon(Icons.Default.CreditCard, contentDescription = null, modifier = Modifier.size(14.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("Pay Online (UPI)", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                }

                                Button(
                                    onClick = {
                                        viewModel.completeRemainingPayment(booking.id, "CASH")
                                        Toast.makeText(context, "Marked: Remaining ₹${booking.remainingBalance} Paid in Cash to Worker!", Toast.LENGTH_LONG).show()
                                    },
                                    modifier = Modifier.weight(1f).height(38.dp),
                                    shape = RoundedCornerShape(8.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = GuaranteeGreen),
                                    contentPadding = PaddingValues(horizontal = 4.dp, vertical = 0.dp)
                                ) {
                                    Icon(Icons.Default.Payments, contentDescription = null, modifier = Modifier.size(14.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("Pay Cash", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Front 4 requirement:
        // Green Box: Worker ko pura paisa milega
        WorkerGuaranteedGreenBox(amountText = "₹$workerCharge")

        Spacer(modifier = Modifier.height(14.dp))

        // Helpline Contact Row (Call + WhatsApp + Telegram)
        com.example.ui.components.HelplineContactRow()

        Spacer(modifier = Modifier.height(16.dp))

        // Actions: Share / Download Invoice
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            OutlinedButton(
                onClick = {
                    Toast.makeText(context, "Invoice receipt shared to WhatsApp (+91 9205161464)", Toast.LENGTH_SHORT).show()
                },
                modifier = Modifier.weight(1f).height(48.dp).testTag("share_invoice_btn"),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(Icons.Default.Share, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("Share Bill")
            }

            Button(
                onClick = {
                    Toast.makeText(context, "Official PDF Invoice saved to device storage", Toast.LENGTH_SHORT).show()
                },
                modifier = Modifier.weight(1f).height(48.dp).testTag("download_invoice_btn"),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = ServiceProBlue)
            ) {
                Icon(Icons.Default.Download, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("Download PDF")
            }
        }
    }
}
