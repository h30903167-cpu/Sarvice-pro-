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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.Localization
import com.example.data.ServiceProRepository
import com.example.model.Language
import com.example.model.UserAccount
import com.example.model.UserRole
import com.example.ui.AppScreen
import com.example.ui.MainViewModel
import com.example.ui.components.TodayOfferBanner
import com.example.ui.components.WorkerGuaranteedGreenBox
import com.example.ui.theme.*

@Composable
fun LoginScreen(
    viewModel: MainViewModel,
    onLoginSuccess: () -> Unit,
    onNavigateToRegister: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    var selectedTab by remember { mutableStateOf(if (uiState.userRole == UserRole.WORKER) 1 else 0) }
    var mobileNumber by remember { mutableStateOf("9205161464") }
    var password by remember { mutableStateOf("123456") }
    var otpCode by remember { mutableStateOf("4582") }
    var isOtpRequested by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundSlate)
            .statusBarsPadding()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Top bar: ServicePro Welcome & Language selector
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = ServiceProLightBlue,
                border = BorderStroke(1.dp, ServiceProBlue.copy(alpha = 0.3f))
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    Icon(Icons.Default.Shield, contentDescription = null, tint = ServiceProBlue, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("⚡ ServicePro Official Portal", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = ServiceProDarkBlue)
                }
            }

            OutlinedButton(
                onClick = { viewModel.toggleLanguage() },
                modifier = Modifier.height(34.dp).testTag("login_lang_toggle"),
                shape = RoundedCornerShape(17.dp),
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 0.dp),
                border = BorderStroke(1.dp, ServiceProBlue)
            ) {
                Text(
                    text = if (uiState.language == Language.EN) "हिंदी में बदलें" else "English",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = ServiceProBlue
                )
            }
        }

        Spacer(modifier = Modifier.height(28.dp))

        // Shield Logo ⚡ ServicePro Marhaura
        Box(
            modifier = Modifier
                .size(76.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(ServiceProBlue)
                .shadow(6.dp, RoundedCornerShape(20.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Shield,
                contentDescription = "ServicePro Shield",
                tint = Color.White,
                modifier = Modifier.size(46.dp)
            )
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = null,
                tint = ServiceProBlue,
                modifier = Modifier.size(24.dp)
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        Text(
            text = "ServicePro",
            fontSize = 28.sp,
            fontWeight = FontWeight.Black,
            color = Color(0xFF0C1E3C),
            letterSpacing = (-0.5).sp
        )
        Text(
            text = "Marhaura • Home Services & Partner Network",
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium,
            color = TextSecondary
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Tab Selector: Customer vs Worker
        TabRow(
            selectedTabIndex = selectedTab,
            containerColor = Color.White,
            modifier = Modifier
                .clip(RoundedCornerShape(14.dp))
                .border(1.dp, CardBorder, RoundedCornerShape(14.dp))
        ) {
            Tab(
                selected = selectedTab == 0,
                onClick = {
                    selectedTab = 0
                    viewModel.selectRole(UserRole.CUSTOMER)
                },
                text = { Text("Customer Login", fontWeight = FontWeight.Bold, fontSize = 13.sp) }
            )
            Tab(
                selected = selectedTab == 1,
                onClick = {
                    selectedTab = 1
                    viewModel.selectRole(UserRole.WORKER)
                },
                text = { Text("Worker / Partner", fontWeight = FontWeight.Bold, fontSize = 13.sp) }
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Login Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            border = BorderStroke(1.dp, CardBorder),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                if (selectedTab == 0) {
                    // CUSTOMER LOGIN (No password required! Google + Phone OTP per user's instruction)
                    Text(
                        text = "Sign in to ServicePro",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF0C1E3C)
                    )
                    Text(
                        text = "Instant login with Google or Phone Number. No password needed!",
                        fontSize = 12.sp,
                        color = TextSecondary
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // 1-Click Google Sign In Button
                    OutlinedButton(
                        onClick = {
                            Toast.makeText(context, "Logged in via Google Account", Toast.LENGTH_SHORT).show()
                            onLoginSuccess()
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .testTag("google_login_btn"),
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(1.2.dp, CardBorder)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Surface(
                                shape = CircleShape,
                                color = Color(0xFFEA4335),
                                modifier = Modifier.size(24.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Text("G", color = Color.White, fontWeight = FontWeight.Black, fontSize = 14.sp)
                                }
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = "Continue with Google",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF1E293B)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Divider(modifier = Modifier.weight(1f), color = CardBorder)
                        Text(
                            text = "  OR USE MOBILE NUMBER  ",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextMuted
                        )
                        Divider(modifier = Modifier.weight(1f), color = CardBorder)
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    var usePasswordForCustomer by remember { mutableStateOf(false) }

                    OutlinedTextField(
                        value = mobileNumber,
                        onValueChange = { mobileNumber = it },
                        label = { Text("📱 +91 Mobile Number or Grahak ID") },
                        placeholder = { Text("e.g. 9205161464 ya SP-PRIYANKA-9205") },
                        modifier = Modifier.fillMaxWidth().testTag("mobile_input"),
                        singleLine = true,
                        leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null, tint = ServiceProBlue) },
                        shape = RoundedCornerShape(12.dp)
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (usePasswordForCustomer) "🔒 Password Login Active" else "⚡ 1-Click OTP Login",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = ServiceProBlue
                        )
                        TextButton(
                            onClick = { usePasswordForCustomer = !usePasswordForCustomer },
                            contentPadding = PaddingValues(0.dp)
                        ) {
                            Text(
                                text = if (usePasswordForCustomer) "Switch to OTP" else "Password se Login Karein",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = OfferBorderRed
                            )
                        }
                    }

                    if (usePasswordForCustomer) {
                        OutlinedTextField(
                            value = password,
                            onValueChange = { password = it },
                            label = { Text("🔒 Enter Password / Security PIN") },
                            modifier = Modifier.fillMaxWidth().testTag("cust_password_input"),
                            singleLine = true,
                            visualTransformation = PasswordVisualTransformation(),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                            leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null, tint = ServiceProBlue) },
                            shape = RoundedCornerShape(12.dp)
                        )
                        Spacer(modifier = Modifier.height(14.dp))
                    } else if (isOtpRequested) {
                        OutlinedTextField(
                            value = otpCode,
                            onValueChange = { otpCode = it },
                            label = { Text("Enter OTP (Default: 4582)") },
                            modifier = Modifier.fillMaxWidth().testTag("otp_input"),
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null, tint = GuaranteeGreen) },
                            shape = RoundedCornerShape(12.dp)
                        )
                        Spacer(modifier = Modifier.height(14.dp))
                    }

                    Button(
                        onClick = {
                            if (usePasswordForCustomer) {
                                val name = if (mobileNumber.contains("PRIYANKA", ignoreCase = true)) "Priyanka" else "Customer"
                                Toast.makeText(context, "Welcome $name!", Toast.LENGTH_SHORT).show()
                                viewModel.login(UserRole.CUSTOMER, name, mobileNumber, if (mobileNumber.contains("PRIYANKA", ignoreCase = true)) "SP-PRIYANKA-9205" else "SP-CUST-8492")
                                onLoginSuccess()
                            } else if (!isOtpRequested) {
                                isOtpRequested = true
                                Toast.makeText(context, "OTP 4582 sent to $mobileNumber", Toast.LENGTH_SHORT).show()
                            } else {
                                val name = if (mobileNumber.contains("PRIYANKA", ignoreCase = true)) "Priyanka" else "Customer"
                                Toast.makeText(context, "OTP Verified! Welcome $name.", Toast.LENGTH_SHORT).show()
                                viewModel.login(UserRole.CUSTOMER, name, mobileNumber, if (mobileNumber.contains("PRIYANKA", ignoreCase = true)) "SP-PRIYANKA-9205" else "SP-CUST-8492")
                                onLoginSuccess()
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .testTag("login_submit_btn"),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = ServiceProBlue)
                    ) {
                        Text(
                            text = if (usePasswordForCustomer) "Login with Password →" else if (!isOtpRequested) "Send OTP & Continue" else "Verify & Explore Area Services →",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                } else {
                    // WORKER LOGIN
                    Text(
                        text = "Worker / Partner Login",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF0C1E3C)
                    )
                    Text(
                        text = "Access your jobs, live earnings (100% full payout) & client leads",
                        fontSize = 12.sp,
                        color = TextSecondary
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = mobileNumber,
                        onValueChange = { mobileNumber = it },
                        label = { Text("📱 Registered Mobile or Worker ID") },
                        placeholder = { Text("e.g. 9205161464 ya SP-WRK-920516") },
                        modifier = Modifier.fillMaxWidth().testTag("mobile_input"),
                        singleLine = true,
                        leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null, tint = ServiceProBlue) },
                        shape = RoundedCornerShape(12.dp)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = { Text("🔒 Partner Password / Security PIN") },
                        modifier = Modifier.fillMaxWidth().testTag("password_input"),
                        singleLine = true,
                        visualTransformation = PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null, tint = ServiceProBlue) },
                        shape = RoundedCornerShape(12.dp)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Button(
                        onClick = {
                            Toast.makeText(context, "Welcome Partner! Opening Dashboard.", Toast.LENGTH_SHORT).show()
                            viewModel.login(UserRole.WORKER, "Rahul Sharma", mobileNumber, "SP-WRK-920516")
                            onLoginSuccess()
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .testTag("worker_login_submit_btn"),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = ServiceProBlue)
                    ) {
                        Text(
                            text = "Login to Partner Dashboard →",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Today Offer Banner
        TodayOfferBanner(onClick = onNavigateToRegister)

        Spacer(modifier = Modifier.height(16.dp))

        // New? Register links for Customer & Worker
        Row(
            modifier = Modifier.clickable { onNavigateToRegister() },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "New to ServicePro? ",
                fontSize = 14.sp,
                color = TextSecondary
            )
            Text(
                text = "Register as Grahak / Worker →",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = ServiceProBlue
            )
        }
    }
}

// -------------------------------------------------------------
// REGISTRATION SCREEN (Matches Screenshots 1, 3 & 4: Customer & Worker Tabs)
// -------------------------------------------------------------
@Composable
fun RegisterScreen(
    viewModel: MainViewModel,
    onRegisterSuccess: () -> Unit,
    onNavigateToLogin: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    var regTab by remember { mutableStateOf(0) } // 0: Grahak (Customer - Screenshot 3), 1: Worker (Karigar - Screenshots 1 & 4)

    // Customer Fields (Screenshot 3)
    var custName by remember { mutableStateOf("Priyanka") }
    var custPhone by remember { mutableStateOf("9205161464") }
    var custEmail by remember { mutableStateOf("priyanka@gmail.com") }
    var custPassword by remember { mutableStateOf("123456") }
    var custConfirmPassword by remember { mutableStateOf("123456") }
    var custReferralCode by remember { mutableStateOf("SP-MARHAURA-50") }
    var custAgreedToTerms by remember { mutableStateOf(true) }
    var showCustPassword by remember { mutableStateOf(false) }
    var showCustConfirmPassword by remember { mutableStateOf(false) }

    // Worker Fields (Screenshots 1 & 4)
    var workerName by remember { mutableStateOf("Vikram Yadav") }
    var workerMobile by remember { mutableStateOf("9205161464") }
    var workerOtp by remember { mutableStateOf("123456") }
    var isWorkerOtpVerified by remember { mutableStateOf(true) }
    var workerExpYears by remember { mutableStateOf("5") }
    var workerArea by remember { mutableStateOf("Marhaura") }
    var workerPassword by remember { mutableStateOf("123456") }
    var workerConfirmPassword by remember { mutableStateOf("123456") }
    var showWorkerPassword by remember { mutableStateOf(false) }
    var showWorkerConfirmPassword by remember { mutableStateOf(false) }
    var isPhotoUploaded by remember { mutableStateOf(true) }
    var isAadhaarUploaded by remember { mutableStateOf(true) }
    var isCraneLicenseUploaded by remember { mutableStateOf(true) }
    var workerAgreedToTerms by remember { mutableStateOf(true) }

    // Multi-select work categories (Screenshots 1 & 4)
    val availableServices = listOf(
        "Electrician / इलेक्ट्रिशियन",
        "Plumber / प्लंबर",
        "Crane Service / क्रेन सर्विस (Towing)",
        "AC Repair / एसी रिपेयर",
        "Carpenter / बढ़ई",
        "Painter / पेंटर",
        "RO Repairing / आरओ रिपेयरिंग",
        "Kirana & Home Delivery / किराना स्टोर"
    )
    val selectedServices = remember {
        mutableStateListOf(
            "Crane Service / क्रेन सर्विस (Towing)",
            "Electrician / इलेक्ट्रिशियन"
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundSlate)
            .statusBarsPadding()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Top Navigation Bar
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.clickable { onNavigateToLogin() }
            ) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back to Login", tint = ServiceProBlue)
                Spacer(modifier = Modifier.width(6.dp))
                Text("Login / लॉगिन", color = ServiceProBlue, fontWeight = FontWeight.Bold, fontSize = 14.sp)
            }

            // Language Switcher
            OutlinedButton(
                onClick = { viewModel.toggleLanguage() },
                modifier = Modifier.height(34.dp).testTag("reg_lang_toggle"),
                shape = RoundedCornerShape(17.dp),
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 0.dp),
                border = BorderStroke(1.dp, ServiceProBlue)
            ) {
                Text(
                    text = if (uiState.language == Language.EN) "EN | हिंदी" else "हिंदी | EN",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = ServiceProBlue
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Brand Banner
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(ServiceProBlue),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Shield,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(26.dp)
                )
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = null,
                    tint = ServiceProBlue,
                    modifier = Modifier.size(14.dp)
                )
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                Text(
                    text = "ServicePro",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Black,
                    color = Color(0xFF0C1E3C),
                    letterSpacing = (-0.5).sp
                )
                Text(
                    text = "Marhaura • Bihar",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = TextSecondary
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Green LIMITED OFFER Banner
        TodayOfferBanner(onClick = {})

        Spacer(modifier = Modifier.height(16.dp))

        // Tab Selector: Grahak / Customer (Screenshot 3) vs Worker / Karigar (Screenshots 1 & 4)
        TabRow(
            selectedTabIndex = regTab,
            containerColor = Color.White,
            modifier = Modifier
                .clip(RoundedCornerShape(14.dp))
                .border(1.dp, CardBorder, RoundedCornerShape(14.dp))
        ) {
            Tab(
                selected = regTab == 0,
                onClick = { regTab = 0 },
                text = { Text("👤 Grahak / Use (Customer)", fontWeight = FontWeight.Bold, fontSize = 12.sp) }
            )
            Tab(
                selected = regTab == 1,
                onClick = { regTab = 1 },
                text = { Text("🛠 Karigar / Worker (श्रमिक)", fontWeight = FontWeight.Bold, fontSize = 12.sp) }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (regTab == 0) {
            // =========================================================================
            // TAB 0: CREATE ACCOUNT / GRAHAK REGISTRATION (Matches Screenshot 3 Exactly)
            // =========================================================================
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(1.dp, CardBorder),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier.padding(22.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Create Account",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Black,
                        color = Color(0xFF0C1E3C),
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Join ServicePro to book trusted pros in minutes",
                        fontSize = 13.sp,
                        color = TextSecondary,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    // Full Name / पूरा नाम
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Text("Full Name\nपूरा नाम", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                        Spacer(modifier = Modifier.height(4.dp))
                        OutlinedTextField(
                            value = custName,
                            onValueChange = { custName = it },
                            placeholder = { Text("Enter your full name") },
                            modifier = Modifier.fillMaxWidth().testTag("reg_cust_name"),
                            singleLine = true,
                            leadingIcon = {
                                Icon(Icons.Default.Person, contentDescription = null, tint = Color(0xFF64748B))
                            },
                            shape = RoundedCornerShape(12.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Phone Number / मोबाइल नंबर
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Text("Phone Number\nमोबाइल नंबर", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                        Spacer(modifier = Modifier.height(4.dp))
                        OutlinedTextField(
                            value = custPhone,
                            onValueChange = { custPhone = it },
                            placeholder = { Text("98765 43210") },
                            prefix = { Text("+91 | ", fontWeight = FontWeight.Bold, color = Color(0xFF1E293B)) },
                            modifier = Modifier.fillMaxWidth().testTag("reg_cust_phone"),
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                            leadingIcon = {
                                Icon(Icons.Default.Phone, contentDescription = null, tint = Color(0xFF64748B))
                            },
                            shape = RoundedCornerShape(12.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Email / ईमेल पता
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Text("Email\nईमेल पता", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                        Spacer(modifier = Modifier.height(4.dp))
                        OutlinedTextField(
                            value = custEmail,
                            onValueChange = { custEmail = it },
                            placeholder = { Text("Enter your email address") },
                            modifier = Modifier.fillMaxWidth().testTag("reg_cust_email"),
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                            leadingIcon = {
                                Icon(Icons.Default.Email, contentDescription = null, tint = Color(0xFF64748B))
                            },
                            shape = RoundedCornerShape(12.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Password / पासवर्ड
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Text("Password\nपासवर्ड", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                        Spacer(modifier = Modifier.height(4.dp))
                        OutlinedTextField(
                            value = custPassword,
                            onValueChange = { custPassword = it },
                            placeholder = { Text("Create a password") },
                            modifier = Modifier.fillMaxWidth().testTag("reg_cust_password"),
                            singleLine = true,
                            visualTransformation = if (showCustPassword) VisualTransformation.None else PasswordVisualTransformation(),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                            leadingIcon = {
                                Icon(Icons.Default.Lock, contentDescription = null, tint = Color(0xFF64748B))
                            },
                            trailingIcon = {
                                IconButton(onClick = { showCustPassword = !showCustPassword }) {
                                    Icon(
                                        imageVector = if (showCustPassword) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                        contentDescription = "Toggle password",
                                        tint = Color(0xFF64748B)
                                    )
                                }
                            },
                            shape = RoundedCornerShape(12.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Confirm Password / पासवर्ड की पुष्टि करें
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Text("Confirm Password\nपासवर्ड की पुष्टि करें", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                        Spacer(modifier = Modifier.height(4.dp))
                        OutlinedTextField(
                            value = custConfirmPassword,
                            onValueChange = { custConfirmPassword = it },
                            placeholder = { Text("Re-enter your password") },
                            modifier = Modifier.fillMaxWidth().testTag("reg_cust_confirm_password"),
                            singleLine = true,
                            visualTransformation = if (showCustConfirmPassword) VisualTransformation.None else PasswordVisualTransformation(),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                            leadingIcon = {
                                Icon(Icons.Default.Lock, contentDescription = null, tint = Color(0xFF64748B))
                            },
                            trailingIcon = {
                                IconButton(onClick = { showCustConfirmPassword = !showCustConfirmPassword }) {
                                    Icon(
                                        imageVector = if (showCustConfirmPassword) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                        contentDescription = "Toggle password",
                                        tint = Color(0xFF64748B)
                                    )
                                }
                            },
                            shape = RoundedCornerShape(12.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Referral Code (Optional) / रेफरल कोड (वैकल्पिक)
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Text("Referral Code (Optional)\nरेफरल कोड (वैकल्पिक)", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                        Spacer(modifier = Modifier.height(4.dp))
                        OutlinedTextField(
                            value = custReferralCode,
                            onValueChange = { custReferralCode = it },
                            placeholder = { Text("Enter referral code if you have one") },
                            modifier = Modifier.fillMaxWidth().testTag("reg_cust_referral"),
                            singleLine = true,
                            leadingIcon = {
                                Icon(Icons.Default.CardGiftcard, contentDescription = null, tint = Color(0xFF64748B))
                            },
                            shape = RoundedCornerShape(12.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Terms & Conditions Checkbox
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { custAgreedToTerms = !custAgreedToTerms },
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = custAgreedToTerms,
                            onCheckedChange = { custAgreedToTerms = it },
                            colors = CheckboxDefaults.colors(checkedColor = ServiceProBlue)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Column {
                            Text("I agree to the", fontSize = 12.sp, color = TextPrimary)
                            Text(
                                "Terms & Conditions & Privacy Policy",
                                fontSize = 12.sp,
                                color = ServiceProBlue,
                                textDecoration = TextDecoration.Underline,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // Blue Button: Register / रजिस्टर
                    Button(
                        onClick = {
                            if (!custAgreedToTerms) {
                                Toast.makeText(context, "Please agree to the Terms & Conditions", Toast.LENGTH_SHORT).show()
                                return@Button
                            }
                            val newId = "SP-${custName.uppercase().replace(" ", "").take(8)}-9205"
                            val account = UserAccount(
                                userId = newId,
                                fullName = custName,
                                phone = custPhone,
                                email = custEmail,
                                role = UserRole.CUSTOMER,
                                documentType = "Aadhaar Card",
                                documentNumber = "4829-1920-5161",
                                referralCode = custReferralCode,
                                isAadhaarVerified = true,
                                isPhotoVerified = true,
                                locationArea = "Marhaura"
                            )
                            viewModel.registerUser(account)
                            Toast.makeText(context, "Account Created Successfully! ID: $newId", Toast.LENGTH_SHORT).show()
                            onRegisterSuccess()
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp)
                            .testTag("reg_cust_submit_btn"),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = ServiceProBlue)
                    ) {
                        Text(
                            text = "Register / रजिस्टर",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Already have account? Login / लॉगिन
                    Row(
                        modifier = Modifier.clickable { onNavigateToLogin() },
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Already have account? ", fontSize = 13.sp, color = TextSecondary)
                        Text(
                            "Login / लॉगिन",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = ServiceProBlue,
                            textDecoration = TextDecoration.Underline
                        )
                    }
                }
            }
        } else {
            // =========================================================================
            // TAB 1: WORKER REGISTRATION / कर्मचारी पंजीकरण (Matches Screenshots 1 & 4)
            // =========================================================================
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(1.dp, CardBorder),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = "Worker Registration\n/ कर्मचारी पंजीकरण",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Black,
                        color = Color(0xFF0C1E3C)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Register yourself as a verified ServicePro worker in Marhaura, Bihar. Fill the details below to get started.",
                        fontSize = 12.sp,
                        color = TextSecondary,
                        lineHeight = 16.sp
                    )

                    Spacer(modifier = Modifier.height(18.dp))

                    // 1. Select Your Work / अपना काम चुनें (Multi-select Checkboxes)
                    Text(
                        text = "Select Your Work / अपना काम चुनें",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF0C1E3C)
                    )
                    Text(
                        text = "Select one or more services you provide",
                        fontSize = 11.sp,
                        color = TextSecondary
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(0xFFF8FAFC), RoundedCornerShape(12.dp))
                            .border(1.dp, CardBorder, RoundedCornerShape(12.dp))
                            .padding(8.dp)
                    ) {
                        availableServices.forEach { serviceName ->
                            val isSelected = selectedServices.contains(serviceName)
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        if (isSelected) selectedServices.remove(serviceName)
                                        else selectedServices.add(serviceName)
                                    }
                                    .padding(vertical = 4.dp, horizontal = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Checkbox(
                                    checked = isSelected,
                                    onCheckedChange = { checked ->
                                        if (checked) selectedServices.add(serviceName)
                                        else selectedServices.remove(serviceName)
                                    },
                                    colors = CheckboxDefaults.colors(checkedColor = ServiceProBlue)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = serviceName,
                                    fontSize = 13.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    color = if (isSelected) ServiceProBlue else TextPrimary
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // 2. Full Name / पूरा नाम
                    Text("Full Name / पूरा नाम", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                    Spacer(modifier = Modifier.height(4.dp))
                    OutlinedTextField(
                        value = workerName,
                        onValueChange = { workerName = it },
                        placeholder = { Text("Enter your full name") },
                        modifier = Modifier.fillMaxWidth().testTag("reg_worker_name"),
                        singleLine = true,
                        leadingIcon = { Icon(Icons.Default.Person, contentDescription = null, tint = Color(0xFF64748B)) },
                        shape = RoundedCornerShape(12.dp)
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // 3. Mobile Number +91 with Send OTP / Verified Button
                    Text("Mobile Number / मोबाइल नंबर +91", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedTextField(
                            value = workerMobile,
                            onValueChange = { workerMobile = it },
                            placeholder = { Text("98765 43210") },
                            prefix = { Text("+91 ", fontWeight = FontWeight.Bold) },
                            modifier = Modifier.weight(1f).testTag("reg_worker_mobile"),
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                            leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null, tint = Color(0xFF64748B)) },
                            shape = RoundedCornerShape(12.dp)
                        )

                        Button(
                            onClick = {
                                isWorkerOtpVerified = true
                                Toast.makeText(context, "OTP Verified ✓", Toast.LENGTH_SHORT).show()
                            },
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.height(52.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (isWorkerOtpVerified) GuaranteeGreen else ServiceProBlue
                            )
                        ) {
                            Text(
                                text = if (isWorkerOtpVerified) "✓ Verified" else "Send OTP",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // 4. Enter OTP / OTP दर्ज करें
                    Text("Enter OTP / OTP दर्ज करें", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                    Spacer(modifier = Modifier.height(4.dp))
                    OutlinedTextField(
                        value = workerOtp,
                        onValueChange = { workerOtp = it },
                        placeholder = { Text("123456") },
                        modifier = Modifier.fillMaxWidth().testTag("reg_worker_otp"),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        shape = RoundedCornerShape(12.dp)
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // 5. Experience Years / अनुभव (वर्षों में)
                    Text("Experience Years / अनुभव (वर्षों में)", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                    Spacer(modifier = Modifier.height(4.dp))
                    OutlinedTextField(
                        value = workerExpYears,
                        onValueChange = { workerExpYears = it },
                        placeholder = { Text("e.g. 3") },
                        trailingIcon = { Text("Years ", fontWeight = FontWeight.Bold, color = TextSecondary, fontSize = 12.sp) },
                        modifier = Modifier.fillMaxWidth().testTag("reg_worker_exp"),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        shape = RoundedCornerShape(12.dp)
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // 6. Area / क्षेत्र
                    Text("Area / क्षेत्र Marhaura", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                    Spacer(modifier = Modifier.height(4.dp))
                    OutlinedTextField(
                        value = workerArea,
                        onValueChange = { workerArea = it },
                        placeholder = { Text("Marhaura Station Road / पूरा क्षेत्र") },
                        modifier = Modifier.fillMaxWidth().testTag("reg_worker_area"),
                        singleLine = true,
                        leadingIcon = { Icon(Icons.Default.LocationOn, contentDescription = null, tint = ServiceProBlue) },
                        shape = RoundedCornerShape(12.dp)
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // 7. Create Password / पासवर्ड बनाएं
                    Text("Create Password / पासवर्ड बनाएं", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                    Spacer(modifier = Modifier.height(4.dp))
                    OutlinedTextField(
                        value = workerPassword,
                        onValueChange = { workerPassword = it },
                        placeholder = { Text("Create Password for worker only") },
                        modifier = Modifier.fillMaxWidth().testTag("reg_worker_password_input"),
                        singleLine = true,
                        visualTransformation = if (showWorkerPassword) VisualTransformation.None else PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null, tint = Color(0xFF64748B)) },
                        trailingIcon = {
                            IconButton(onClick = { showWorkerPassword = !showWorkerPassword }) {
                                Icon(
                                    imageVector = if (showWorkerPassword) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                    contentDescription = null,
                                    tint = Color(0xFF64748B)
                                )
                            }
                        },
                        shape = RoundedCornerShape(12.dp)
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // 8. Confirm Password / पासवर्ड की पुष्टि करें with match indicator
                    Text("Confirm Password / पासवर्ड की पुष्टि करें", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                    Spacer(modifier = Modifier.height(4.dp))
                    OutlinedTextField(
                        value = workerConfirmPassword,
                        onValueChange = { workerConfirmPassword = it },
                        placeholder = { Text("Confirm password") },
                        modifier = Modifier.fillMaxWidth().testTag("reg_worker_confirm_password"),
                        singleLine = true,
                        visualTransformation = if (showWorkerConfirmPassword) VisualTransformation.None else PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null, tint = Color(0xFF64748B)) },
                        trailingIcon = {
                            if (workerPassword == workerConfirmPassword && workerPassword.isNotEmpty()) {
                                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(end = 8.dp)) {
                                    Icon(Icons.Default.CheckCircle, contentDescription = null, tint = GuaranteeGreen, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("Passwords match", fontSize = 11.sp, color = GuaranteeGreen, fontWeight = FontWeight.Bold)
                                }
                            }
                        },
                        shape = RoundedCornerShape(12.dp)
                    )

                    Spacer(modifier = Modifier.height(18.dp))

                    // 9. Upload Documents / दस्तावेज़ अपलोड करें (Matching Screenshots 1 & 4)
                    Text("Upload Documents / दस्तावेज़ अपलोड करें", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0C1E3C))
                    Spacer(modifier = Modifier.height(10.dp))

                    // Upload Photo Box
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                isPhotoUploaded = true
                                Toast.makeText(context, "Photo Uploaded Successfully ✓", Toast.LENGTH_SHORT).show()
                            },
                        shape = RoundedCornerShape(12.dp),
                        color = Color(0xFFF8FAFC),
                        border = BorderStroke(1.2.dp, if (isPhotoUploaded) GuaranteeGreen else Color(0xFF94A3B8))
                    ) {
                        Row(
                            modifier = Modifier.padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(46.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(if (isPhotoUploaded) GuaranteeLightGreen else Color(0xFFE2E8F0)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = if (isPhotoUploaded) Icons.Default.CheckCircle else Icons.Default.CameraAlt,
                                    contentDescription = null,
                                    tint = if (isPhotoUploaded) DarkGreen else Color(0xFF64748B),
                                    modifier = Modifier.size(26.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text("Upload Photo / फोटो अपलोड करें", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                Text("Click to upload or drag & drop • PNG/JPG, Max 2MB", fontSize = 11.sp, color = TextSecondary)
                                if (isPhotoUploaded) {
                                    Text("✓ Photo Uploaded (Selfie ID Attached)", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = DarkGreen)
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Upload Aadhaar Box
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                isAadhaarUploaded = true
                                Toast.makeText(context, "Aadhaar Front & Back Uploaded ✓", Toast.LENGTH_SHORT).show()
                            },
                        shape = RoundedCornerShape(12.dp),
                        color = Color(0xFFF8FAFC),
                        border = BorderStroke(1.2.dp, if (isAadhaarUploaded) GuaranteeGreen else Color(0xFF94A3B8))
                    ) {
                        Row(
                            modifier = Modifier.padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(46.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(if (isAadhaarUploaded) GuaranteeLightGreen else Color(0xFFE2E8F0)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = if (isAadhaarUploaded) Icons.Default.VerifiedUser else Icons.Default.Badge,
                                    contentDescription = null,
                                    tint = if (isAadhaarUploaded) DarkGreen else Color(0xFF64748B),
                                    modifier = Modifier.size(26.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text("Upload ID Aadhaar / आधार कार्ड अपलोड करें", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                Text("Upload Aadhaar front & back • PDF/JPG/PNG - clear & legible", fontSize = 11.sp, color = TextSecondary)
                                if (isAadhaarUploaded) {
                                    Text("✓ Aadhaar Card Verified (4829-1920-5161)", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = DarkGreen)
                                }
                            }
                        }
                    }

                    // Crane Service License Card (if Crane is selected or general)
                    if (selectedServices.any { it.contains("Crane", ignoreCase = true) }) {
                        Spacer(modifier = Modifier.height(10.dp))
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    isCraneLicenseUploaded = true
                                    Toast.makeText(context, "Crane Driving License Verified ✓", Toast.LENGTH_SHORT).show()
                                },
                            shape = RoundedCornerShape(12.dp),
                            color = GuaranteeLightGreen,
                            border = BorderStroke(1.2.dp, GuaranteeGreen)
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(Icons.Default.DirectionsCar, contentDescription = null, tint = DarkGreen, modifier = Modifier.size(24.dp))
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text("🚜 Crane Operator & Heavy Vehicle License", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = DarkGreen)
                                    Text("BR 04 CR 1464 • Valid Crane & Towing Permit Verified ✓", fontSize = 11.sp, color = DarkGreen.copy(alpha = 0.8f))
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Encryption security notice (Screenshot 1 & 4)
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp),
                        color = Color(0xFFEFF6FF),
                        border = BorderStroke(1.dp, Color(0xFFBFDBFE))
                    ) {
                        Row(
                            modifier = Modifier.padding(10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.Info, contentDescription = null, tint = ServiceProBlue, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Your data is secure and encrypted. We will verify your Aadhaar as per Govt. guidelines.",
                                fontSize = 11.sp,
                                color = ServiceProDarkBlue,
                                lineHeight = 14.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Worker Agreement Checkbox
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { workerAgreedToTerms = !workerAgreedToTerms },
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = workerAgreedToTerms,
                            onCheckedChange = { workerAgreedToTerms = it },
                            colors = CheckboxDefaults.colors(checkedColor = ServiceProBlue)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            "I confirm that all worker documents & Aadhaar are genuine.",
                            fontSize = 11.sp,
                            color = TextPrimary
                        )
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    // Blue Button: Register as Worker / कार्यकर्ता के रूप में पंजीकृत करें >
                    Button(
                        onClick = {
                            val newWorkerId = "SP-WRK-${workerMobile.takeLast(6)}"
                            val primeService = selectedServices.firstOrNull() ?: "Crane Service / क्रेन सर्विस"
                            val account = UserAccount(
                                userId = newWorkerId,
                                fullName = workerName,
                                phone = workerMobile,
                                email = "worker.${workerMobile.takeLast(4)}@servicepro.in",
                                role = UserRole.WORKER,
                                documentType = "Aadhaar Card",
                                documentNumber = "4829-1920-5161",
                                aadhaarNumber = "4829-1920-5161",
                                experienceYears = workerExpYears.toIntOrNull() ?: 5,
                                locationArea = workerArea,
                                isAadhaarVerified = true,
                                isPhotoVerified = true
                            )
                            viewModel.registerUser(account)
                            Toast.makeText(context, "Worker Account Created! ID: $newWorkerId", Toast.LENGTH_LONG).show()
                            onRegisterSuccess()
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp)
                            .testTag("reg_worker_submit_btn"),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = ServiceProBlue)
                    ) {
                        Text(
                            text = "Register as Worker / कार्यकर्ता के रूप में पंजीकृत करें >",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Footer disclaimers
                    Text(
                        text = "By registering, you agree to ServicePro's Terms of Service & Privacy Policy | पंजीकरण करके आप सेवा की सेवा शर्तों और गोपनीयता नीति से सहमत हैं",
                        fontSize = 10.sp,
                        color = TextSecondary,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "© 2024 ServicePro Marhaura, Bihar • Support: Helpline • help@servicepro.in",
                        fontSize = 10.sp,
                        color = TextSecondary,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}
