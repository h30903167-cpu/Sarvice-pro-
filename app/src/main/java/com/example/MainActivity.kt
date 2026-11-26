package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.model.UserRole
import com.example.ui.AppScreen
import com.example.ui.MainViewModel
import com.example.ui.components.LocationSearchDialog
import com.example.ui.components.ServiceProBottomBar
import com.example.ui.components.ServiceProHeader
import com.example.ui.screens.*
import com.example.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                val viewModel: MainViewModel = viewModel()
                ServiceProApp(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun ServiceProApp(viewModel: MainViewModel) {
    val uiState by viewModel.uiState.collectAsState()

    // Handle back button behavior for sub-screens
    if (uiState.currentScreen != AppScreen.HOME && uiState.currentScreen != AppScreen.LOGIN) {
        BackHandler {
            viewModel.navigateTo(AppScreen.HOME)
        }
    }

    val showTopBar = uiState.currentScreen in listOf(
        AppScreen.HOME,
        AppScreen.WORKER_DASHBOARD,
        AppScreen.CONTACT_SUPPORT,
        AppScreen.ROOM_KHOJ,
        AppScreen.PRICING_RULE,
        AppScreen.MY_BOOKINGS,
        AppScreen.PROFILE
    )

    val showBottomBar = uiState.currentScreen !in listOf(
        AppScreen.LOGIN,
        AppScreen.REGISTER,
        AppScreen.REGISTRATION_SUCCESS
    )

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        contentWindowInsets = WindowInsets.systemBars,
        topBar = {
            if (showTopBar) {
                ServiceProHeader(
                    language = uiState.language,
                    userRole = uiState.userRole,
                    onToggleLanguage = { viewModel.toggleLanguage() },
                    onRoleChange = { role -> viewModel.selectRole(role) },
                    onLocationClick = { viewModel.showLocationPicker(true) },
                    onLogout = { viewModel.logout() }
                )
            }
        },
        bottomBar = {
            if (showBottomBar) {
                ServiceProBottomBar(
                    currentScreen = uiState.currentScreen,
                    userRole = uiState.userRole,
                    onNavigate = { screen -> viewModel.navigateTo(screen) }
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (uiState.currentScreen) {
                AppScreen.LOGIN -> LoginScreen(
                    viewModel = viewModel,
                    onLoginSuccess = { viewModel.navigateTo(AppScreen.HOME) },
                    onNavigateToRegister = { viewModel.navigateTo(AppScreen.REGISTER) }
                )

                AppScreen.REGISTER -> RegisterScreen(
                    viewModel = viewModel,
                    onRegisterSuccess = { viewModel.navigateTo(AppScreen.REGISTRATION_SUCCESS) },
                    onNavigateToLogin = { viewModel.navigateTo(AppScreen.LOGIN) }
                )

                AppScreen.HOME -> HomeScreen(
                    viewModel = viewModel,
                    onNavigate = { screen -> viewModel.navigateTo(screen) }
                )

                AppScreen.ALL_SERVICES -> AllServicesGridScreen(
                    viewModel = viewModel,
                    onBack = { viewModel.navigateTo(AppScreen.HOME) },
                    onSelectService = { service ->
                        viewModel.selectService(service)
                        viewModel.navigateTo(AppScreen.SERVICE_DETAIL)
                    }
                )

                AppScreen.SERVICE_DETAIL -> ServiceDetailScreen(
                    viewModel = viewModel,
                    onBack = { viewModel.navigateTo(AppScreen.HOME) },
                    onBookNow = { viewModel.navigateTo(AppScreen.CHOOSE_WORKER) }
                )

                AppScreen.CHOOSE_WORKER -> ChooseWorkerScreen(
                    viewModel = viewModel,
                    onBack = { viewModel.navigateTo(AppScreen.SERVICE_DETAIL) },
                    onWorkerSelected = { worker ->
                        viewModel.navigateTo(AppScreen.BOOKING_CONFIRMED)
                    }
                )

                AppScreen.ADD_MEDIA -> AddMediaScreen(
                    viewModel = viewModel,
                    onBack = { viewModel.navigateTo(AppScreen.HOME) },
                    onContinue = { viewModel.navigateTo(AppScreen.CHOOSE_WORKER) }
                )

                AppScreen.BOOKING_CONFIRMED -> BookingConfirmedScreen(
                    viewModel = viewModel,
                    onViewDetails = { viewModel.navigateTo(AppScreen.LIVE_TRACKING) },
                    onCancel = {
                        viewModel.cancelActiveBooking()
                        viewModel.navigateTo(AppScreen.HOME)
                    }
                )

                AppScreen.LIVE_TRACKING -> LiveTrackingScreen(
                    viewModel = viewModel,
                    onBack = { viewModel.navigateTo(AppScreen.MY_BOOKINGS) },
                    onViewInvoice = { viewModel.navigateTo(AppScreen.INVOICE) }
                )

                AppScreen.INVOICE -> InvoiceScreen(
                    viewModel = viewModel,
                    onBack = { viewModel.navigateTo(AppScreen.HOME) }
                )

                AppScreen.PRICING_RULE -> WorkerPricingScreen(
                    viewModel = viewModel,
                    onNavigateToInvoice = { viewModel.navigateTo(AppScreen.INVOICE) }
                )

                AppScreen.WORKER_DASHBOARD -> WorkerDashboardScreen(
                    viewModel = viewModel,
                    onNavigateToPricingRule = { viewModel.navigateTo(AppScreen.PRICING_RULE) },
                    onNavigateToInvoice = { viewModel.navigateTo(AppScreen.INVOICE) }
                )

                AppScreen.ROOM_KHOJ -> RoomKhojScreen(
                    viewModel = viewModel
                )

                AppScreen.MY_BOOKINGS -> BookingsListScreen(
                    viewModel = viewModel,
                    onTrackBooking = { booking ->
                        viewModel.confirmBooking(
                            worker = com.example.data.ServiceProRepository.workers.first(),
                            service = com.example.data.ServiceProRepository.services.first()
                        )
                        viewModel.navigateTo(AppScreen.LIVE_TRACKING)
                    }
                )

                AppScreen.CONTACT_SUPPORT -> ContactSupportScreen(
                    viewModel = viewModel
                )

                AppScreen.REGISTRATION_SUCCESS -> RegistrationSuccessScreen(
                    onContinueToHome = { viewModel.navigateTo(AppScreen.HOME) }
                )

                AppScreen.PROFILE -> ProfileScreen(
                    viewModel = viewModel,
                    onNavigate = { screen -> viewModel.navigateTo(screen) },
                    onLogout = { viewModel.logout() }
                )
            }

            // Location Search & Selection Dialog (Shown after login or on location click)
            if (uiState.isLocationPickerVisible) {
                LocationSearchDialog(
                    currentArea = uiState.selectedArea,
                    onSelectArea = { area -> viewModel.selectArea(area) },
                    onDismiss = { viewModel.showLocationPicker(false) }
                )
            }
        }
    }
}
