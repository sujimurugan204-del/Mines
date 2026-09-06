package com.example.ui.navigation

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.UserRole
import com.example.data.repository.MineCycleRepository
import com.example.ui.admin.AdminDashboardScreen
import com.example.ui.auth.*
import com.example.ui.components.*
import com.example.ui.manufacturer.ManufacturerScreen
import com.example.ui.provider.WasteProviderScreen
import com.example.ui.recycler.RecyclerHubScreen
import com.example.ui.theme.Surface

enum class AppDestination {
    SPLASH,
    LOGIN,
    SIGN_UP,
    FORGOT_PASSWORD,
    MAIN_APP,
    PROFILE
}

@Composable
fun MineCycleApp() {
    var currentDestination by remember { mutableStateOf(AppDestination.SPLASH) }
    val currentUser by MineCycleRepository.currentUser.collectAsState()
    var activeRole by remember { mutableStateOf(currentUser.userType) }

    // Dialog States
    var showPassportDialog by remember { mutableStateOf(false) }
    var selectedPassportId by remember { mutableStateOf("WST-2025-9482") }
    var showScannerDialog by remember { mutableStateOf(false) }
    var showNotificationsDialog by remember { mutableStateOf(false) }
    var showCircuitBreakerDialog by remember { mutableStateOf(false) }

    // Sync active role when user profile changes
    LaunchedEffect(currentUser.userType) {
        activeRole = currentUser.userType
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Surface
    ) {
        Crossfade(targetState = currentDestination, label = "AppTransition") { dest ->
            when (dest) {
                AppDestination.SPLASH -> {
                    SplashScreen(
                        onTimeout = { currentDestination = AppDestination.LOGIN }
                    )
                }

                AppDestination.LOGIN -> {
                    Column(modifier = Modifier.fillMaxSize()) {
                        SovereignApexBanner()
                        Box(modifier = Modifier.weight(1f)) {
                            LoginScreen(
                                onLoginSuccess = { role ->
                                    activeRole = role
                                    currentDestination = AppDestination.MAIN_APP
                                },
                                onNavigateToSignUp = { currentDestination = AppDestination.SIGN_UP },
                                onNavigateToForgotPassword = { currentDestination = AppDestination.FORGOT_PASSWORD }
                            )
                        }
                    }
                }

                AppDestination.SIGN_UP -> {
                    Column(modifier = Modifier.fillMaxSize()) {
                        SovereignApexBanner()
                        Box(modifier = Modifier.weight(1f)) {
                            SignUpScreen(
                                onSignUpSuccess = { role ->
                                    activeRole = role
                                    currentDestination = AppDestination.MAIN_APP
                                },
                                onNavigateToLogin = { currentDestination = AppDestination.LOGIN }
                            )
                        }
                    }
                }

                AppDestination.FORGOT_PASSWORD -> {
                    Column(modifier = Modifier.fillMaxSize()) {
                        SovereignApexBanner()
                        Box(modifier = Modifier.weight(1f)) {
                            ForgotPasswordScreen(
                                onNavigateBack = { currentDestination = AppDestination.LOGIN }
                            )
                        }
                    }
                }

                AppDestination.PROFILE -> {
                    Column(modifier = Modifier.fillMaxSize()) {
                        SovereignApexBanner()
                        SharedTopHeader(
                            activeRole = activeRole,
                            unreadNotificationCount = 0,
                            onNotificationsClick = { showNotificationsDialog = true },
                            onProfileClick = { currentDestination = AppDestination.MAIN_APP },
                            onEmergencyLock = { showCircuitBreakerDialog = true }
                        )
                        Box(modifier = Modifier.weight(1f)) {
                            ProfileScreen(
                                onSignOut = { currentDestination = AppDestination.LOGIN }
                            )
                        }
                    }
                }

                AppDestination.MAIN_APP -> {
                    Scaffold(
                        topBar = {
                            Column {
                                SovereignApexBanner()
                                SharedTopHeader(
                                    activeRole = activeRole,
                                    unreadNotificationCount = 2,
                                    onNotificationsClick = { showNotificationsDialog = true },
                                    onProfileClick = { currentDestination = AppDestination.PROFILE },
                                    onEmergencyLock = { showCircuitBreakerDialog = true }
                                )
                                ActiveRoleToolbar(
                                    activeRole = activeRole,
                                    onRoleSelect = { newRole ->
                                        activeRole = newRole
                                        MineCycleRepository.switchRole(newRole)
                                    },
                                    onOpenPassport = {
                                        selectedPassportId = "WST-2025-9482"
                                        showPassportDialog = true
                                    },
                                    onOpenScanner = {
                                        showScannerDialog = true
                                    }
                                )
                            }
                        }
                    ) { innerPadding ->
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(innerPadding)
                        ) {
                            when (activeRole) {
                                UserRole.ADMIN -> {
                                    AdminDashboardScreen(
                                        onOpenPassport = { id ->
                                            selectedPassportId = id
                                            showPassportDialog = true
                                        },
                                        onNavigateToAIClassification = {
                                            activeRole = UserRole.RECYCLER
                                            MineCycleRepository.switchRole(UserRole.RECYCLER)
                                        }
                                    )
                                }
                                UserRole.WASTE_PROVIDER -> {
                                    WasteProviderScreen(
                                        onOpenPassport = { id ->
                                            selectedPassportId = id
                                            showPassportDialog = true
                                        }
                                    )
                                }
                                UserRole.RECYCLER -> {
                                    RecyclerHubScreen(
                                        onOpenPassport = { id ->
                                            selectedPassportId = id
                                            showPassportDialog = true
                                        }
                                    )
                                }
                                UserRole.MANUFACTURER -> {
                                    ManufacturerScreen(
                                        onOpenPassport = { id ->
                                            selectedPassportId = id
                                            showPassportDialog = true
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // Global Modals
        if (showPassportDialog) {
            DigitalPassportDialog(
                consignmentId = selectedPassportId,
                onDismiss = { showPassportDialog = false }
            )
        }

        if (showScannerDialog) {
            QrScannerDialog(
                onDismiss = { showScannerDialog = false },
                onResultFound = { queryId ->
                    showScannerDialog = false
                    selectedPassportId = queryId
                    showPassportDialog = true
                }
            )
        }

        if (showNotificationsDialog) {
            NotificationsDialog(
                activeRole = activeRole,
                onDismiss = { showNotificationsDialog = false }
            )
        }

        if (showCircuitBreakerDialog) {
            AlertDialog(
                onDismissRequest = { showCircuitBreakerDialog = false },
                title = { Text("Emergency Statutory Circuit Breaker", fontSize = 14.sp) },
                text = {
                    Text(
                        "Under MMDR Section 23C, triggering the circuit breaker instantly suspends automated DBT escrow clearances across high-risk weighbridge nodes pending physical inspector re-verification.",
                        fontSize = 12.sp
                    )
                },
                confirmButton = {
                    Button(
                        onClick = {
                            MineCycleRepository.resolveFraudAnomaly("WST-2025-9478", "dispatch")
                            showCircuitBreakerDialog = false
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = com.example.ui.theme.ErrorColor)
                    ) {
                        Text("Enforce Escrow Freeze")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showCircuitBreakerDialog = false }) {
                        Text("Cancel")
                    }
                }
            )
        }
    }
}
