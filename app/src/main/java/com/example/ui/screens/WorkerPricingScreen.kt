package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.Localization
import com.example.data.ServiceProRepository
import com.example.model.Language
import com.example.ui.MainViewModel
import com.example.ui.components.TodayOfferBanner
import com.example.ui.components.WorkerGuaranteedGreenBox
import com.example.ui.theme.*

@Composable
fun WorkerPricingScreen(
    viewModel: MainViewModel,
    onNavigateToInvoice: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    var sliderValue by remember { mutableStateOf(1000f) }
    val currentCharge = sliderValue.toInt()
    val calc = ServiceProRepository.calculateWorkerRule(currentCharge)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundSlate)
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        // Front 3 Title & Header
        Text(
            text = "⚡ ServicePro Pricing & Payout Rule",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = ServiceProDarkBlue
        )
        Text(
            text = "Aapka Main Rule • 100% Pura Worker Charge + 10% Owner GST",
            fontSize = 12.sp,
            color = TextSecondary
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Same Offer Banner Red Border ke sath
        TodayOfferBanner()

        Spacer(modifier = Modifier.height(16.dp))

        // Front 3 Official Chart
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            border = BorderStroke(1.5.dp, CardBorder),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        imageVector = Icons.Default.TableChart,
                        contentDescription = null,
                        tint = ServiceProBlue,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Standard Pricing Chart",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = TextPrimary
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Chart Items with Tiered GST:
                // 1. Chhota Kaam (< 500) = 5% GST
                PricingChartRow(
                    jobLabel = "Chhota Kaam (< ₹500) e.g. ₹400",
                    workerGets = "₹400 pura (100%)",
                    gstAmount = "₹20 (5% GST)",
                    customerTotal = "₹420"
                )
                Divider(modifier = Modifier.padding(vertical = 8.dp), color = CardBorder)

                // 2. Standard Kaam (500) = 8% GST
                PricingChartRow(
                    jobLabel = "₹500 Kaam (Standard)",
                    workerGets = "₹500 pura (100%)",
                    gstAmount = "₹40 (8% GST)",
                    customerTotal = "₹540",
                    isHighlighted = true
                )
                Divider(modifier = Modifier.padding(vertical = 8.dp), color = CardBorder)

                // 3. Medium Kaam (1000) = 8% GST
                PricingChartRow(
                    jobLabel = "₹1000 Kaam",
                    workerGets = "₹1000 pura (100%)",
                    gstAmount = "₹80 (8% GST)",
                    customerTotal = "₹1080"
                )
                Divider(modifier = Modifier.padding(vertical = 8.dp), color = CardBorder)

                // 4. Bada Kaam (> 1000) = 10% GST
                PricingChartRow(
                    jobLabel = "Bada Kaam (> ₹1000) e.g. ₹1500",
                    workerGets = "₹1500 pura (100%)",
                    gstAmount = "₹150 (10% GST)",
                    customerTotal = "₹1650"
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // The Formula Explanation Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = ServiceProLightBlue),
            border = BorderStroke(1.dp, ServiceProBlue.copy(alpha = 0.4f))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "📐 The ServicePro Tiered Formula",
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 15.sp,
                    color = ServiceProDarkBlue
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "• Chhota Kaam (< ₹500): 5% GST\n• Standard Kaam (₹500 - ₹1000): 8% GST\n• Bada Kaam (> ₹1000): 10% GST\n• Pura GST Official Owner Account me transfer hoga\n• Worker ko NO DEDUCTION - Pura 100% Charge Milega\n• 50% Advance Booking Online • 50% Baad me Cash ya Online!",
                    fontSize = 13.sp,
                    color = TextPrimary,
                    lineHeight = 20.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Interactive Live Pricing Simulator
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            border = BorderStroke(1.dp, CardBorder)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Interactive Calculator",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )
                    Text(
                        text = "₹$currentCharge",
                        fontWeight = FontWeight.Black,
                        fontSize = 18.sp,
                        color = ServiceProBlue
                    )
                }

                Slider(
                    value = sliderValue,
                    onValueChange = { sliderValue = it },
                    valueRange = 200f..3000f,
                    steps = 27,
                    modifier = Modifier.fillMaxWidth().testTag("pricing_slider"),
                    colors = SliderDefaults.colors(
                        thumbColor = ServiceProBlue,
                        activeTrackColor = ServiceProBlue
                    )
                )

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    CalcStatBox(title = "Worker Payout", amount = "₹${calc.workerGetsPura}", subtitle = "100% No Cut", color = GuaranteeGreen)
                    CalcStatBox(title = "Owner GST (${calc.gstPercent}%)", amount = "₹${calc.ownerGstAccount}", subtitle = "Owner Account", color = OfferBorderRed)
                    CalcStatBox(title = "Customer Pay", amount = "₹${calc.customerTotal}", subtitle = "Total Bill", color = ServiceProBlue)
                }

                Spacer(modifier = Modifier.height(14.dp))

                // 50% Advance Online & 50% Final Cash/Online Box
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = Color(0xFFF0FDF4),
                    border = BorderStroke(1.dp, GuaranteeGreen),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Payment, contentDescription = null, tint = GuaranteeGreen, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "💳 50% Advance Online & 50% Cash/Online Rule",
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                color = DarkGreen
                            )
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "• 1st 50% Booking Advance: ₹${calc.advanceOnline50Percent} (Online Pay karna hoga)\n" +
                                   "  Iss advance se pura ${calc.gstPercent}% GST (₹${calc.gstDeductedFromAdvance}) Owner Account me deduct hoga.\n" +
                                   "• 2nd 50% Balance on Completion: ₹${calc.remaining50Percent}\n" +
                                   "  Customer CASH ya ONLINE dono me se kisi bhi mode se worker ko de sakta hai.\n" +
                                   "• Worker ko unka pura 100% (₹${calc.workerGetsPura}) bina kisi cut ke milega!",
                            fontSize = 11.sp,
                            lineHeight = 16.sp,
                            color = TextPrimary
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Fixed GST Rules for RoomKhoj & Shops
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            border = BorderStroke(1.dp, CardBorder)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "🏢 RoomKhoj & Commercial GST Rules",
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = TextPrimary
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    BadgeRuleBox(
                        title = "Shop / Mall GST",
                        amount = "₹80 Fix",
                        color = ServiceProBlue,
                        modifier = Modifier.weight(1f)
                    )
                    BadgeRuleBox(
                        title = "Room GST",
                        amount = "₹5 Fix",
                        color = OfferGold,
                        modifier = Modifier.weight(1f)
                    )
                    BadgeRuleBox(
                        title = "Student Special",
                        amount = "FREE (₹0)",
                        color = GuaranteeGreen,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Green Box as specified in rule 4
        WorkerGuaranteedGreenBox(amountText = "₹${calc.workerGetsPura}")

        Spacer(modifier = Modifier.height(14.dp))

        // Helpline Contact Row (Call + WhatsApp + Telegram)
        com.example.ui.components.HelplineContactRow()

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onNavigateToInvoice,
            modifier = Modifier.fillMaxWidth().height(48.dp).testTag("view_invoice_sample_btn"),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = ServiceProBlue)
        ) {
            Icon(Icons.Default.Receipt, contentDescription = null, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text("View Sample Customer Invoice / Bill")
        }
    }
}

@Composable
private fun PricingChartRow(
    jobLabel: String,
    workerGets: String,
    gstAmount: String,
    customerTotal: String,
    isHighlighted: Boolean = false
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(if (isHighlighted) Color(0xFFEFF6FF) else Color.Transparent)
            .padding(horizontal = 8.dp, vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1.2f)) {
            Text(jobLabel, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = TextPrimary)
            Text("Worker: $workerGets", fontSize = 11.sp, color = GuaranteeGreen, fontWeight = FontWeight.SemiBold)
        }
        Column(modifier = Modifier.weight(1f), horizontalAlignment = Alignment.CenterHorizontally) {
            Text("GST 10%", fontSize = 10.sp, color = TextSecondary)
            Text(gstAmount, fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = OfferBorderRed)
        }
        Column(modifier = Modifier.weight(1f), horizontalAlignment = Alignment.End) {
            Text("Customer", fontSize = 10.sp, color = TextSecondary)
            Text(customerTotal, fontSize = 14.sp, fontWeight = FontWeight.Black, color = ServiceProBlue)
        }
    }
}

@Composable
private fun CalcStatBox(
    title: String,
    amount: String,
    subtitle: String,
    color: Color
) {
    Card(
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = color.copy(alpha = 0.08f)),
        modifier = Modifier.width(100.dp)
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(title, fontSize = 10.sp, color = TextSecondary, textAlign = TextAlign.Center)
            Spacer(modifier = Modifier.height(2.dp))
            Text(amount, fontSize = 15.sp, fontWeight = FontWeight.Black, color = color)
            Text(subtitle, fontSize = 9.sp, color = TextSecondary)
        }
    }
}

@Composable
private fun BadgeRuleBox(
    title: String,
    amount: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = color.copy(alpha = 0.08f)),
        border = BorderStroke(1.dp, color.copy(alpha = 0.3f)),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(title, fontSize = 10.sp, fontWeight = FontWeight.Medium, color = TextSecondary, textAlign = TextAlign.Center)
            Spacer(modifier = Modifier.height(2.dp))
            Text(amount, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = color)
        }
    }
}
