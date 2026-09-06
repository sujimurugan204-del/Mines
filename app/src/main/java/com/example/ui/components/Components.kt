package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.model.UserRole
import com.example.data.repository.MineCycleRepository
import com.example.ui.theme.*
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun SovereignApexBanner() {
    var currentTime by remember { mutableStateOf("11:59:41 IST") }

    LaunchedEffect(Unit) {
        while (true) {
            val df = SimpleDateFormat("HH:mm:ss 'IST'", Locale.getDefault())
            currentTime = df.format(Date())
            kotlinx.coroutines.delay(1000)
        }
    }

    Surface(
        color = Primary,
        contentColor = OnPrimary,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                Box(
                    modifier = Modifier
                        .size(6.dp)
                        .background(PrimaryFixed, CircleShape)
                )
                Text(
                    text = "भारत सरकार | Government of India",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = PrimaryFixed
                )
                Text("•", fontSize = 10.sp, color = OutlineVariant)
                Text(
                    text = "Ministry of Mines",
                    fontSize = 10.sp,
                    color = SurfaceContainerLowest
                )
            }
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                Text(
                    text = "MMDR Sec 14A",
                    fontSize = 10.sp,
                    color = PrimaryFixed
                )
                Text("•", fontSize = 10.sp, color = OutlineVariant)
                Text(
                    text = currentTime,
                    fontSize = 10.sp,
                    fontFamily = FontFamily.Monospace,
                    color = SurfaceContainerLowest
                )
            }
        }
    }
}

@Composable
fun ActiveRoleToolbar(
    activeRole: UserRole,
    onRoleSelect: (UserRole) -> Unit,
    onOpenPassport: () -> Unit,
    onOpenScanner: () -> Unit
) {
    Surface(
        color = SurfaceContainer,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = 8.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(
                text = "ROLE PORTAL:",
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = Primary
            )

            RoleButton(
                title = "Admin Command",
                isSelected = activeRole == UserRole.ADMIN,
                onClick = { onRoleSelect(UserRole.ADMIN) }
            )
            RoleButton(
                title = "Waste Provider",
                isSelected = activeRole == UserRole.WASTE_PROVIDER,
                onClick = { onRoleSelect(UserRole.WASTE_PROVIDER) }
            )
            RoleButton(
                title = "Recycler Hub",
                isSelected = activeRole == UserRole.RECYCLER,
                onClick = { onRoleSelect(UserRole.RECYCLER) }
            )
            RoleButton(
                title = "Manufacturer",
                isSelected = activeRole == UserRole.MANUFACTURER,
                onClick = { onRoleSelect(UserRole.MANUFACTURER) }
            )

            // QR Actions
            Button(
                onClick = onOpenPassport,
                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                shape = RoundedCornerShape(4.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = SecondaryContainer,
                    contentColor = OnSecondaryFixed
                ),
                modifier = Modifier.height(28.dp)
            ) {
                Icon(Icons.Default.QrCode2, contentDescription = null, modifier = Modifier.size(14.dp))
                Spacer(Modifier.width(4.dp))
                Text("QR Passport", fontSize = 11.sp, fontWeight = FontWeight.Bold)
            }

            IconButton(
                onClick = onOpenScanner,
                modifier = Modifier.size(28.dp)
            ) {
                Icon(Icons.Default.QrCodeScanner, contentDescription = "Scan QR", tint = Primary, modifier = Modifier.size(18.dp))
            }
        }
    }
}

@Composable
private fun RoleButton(title: String, isSelected: Boolean, onClick: () -> Unit) {
    Surface(
        color = if (isSelected) Primary else SurfaceContainerLowest,
        contentColor = if (isSelected) OnPrimary else OnSurfaceVariant,
        shape = RoundedCornerShape(4.dp),
        border = if (isSelected) null else androidx.compose.foundation.BorderStroke(1.dp, OutlineVariant),
        modifier = Modifier
            .height(28.dp)
            .clickable(onClick = onClick)
    ) {
        Box(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = title,
                fontSize = 11.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
            )
        }
    }
}

@Composable
fun SharedTopHeader(
    activeRole: UserRole,
    unreadNotificationCount: Int,
    onNotificationsClick: () -> Unit,
    onProfileClick: () -> Unit,
    onEmergencyLock: () -> Unit
) {
    Surface(
        color = SurfaceContainerLowest,
        shadowElevation = 2.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .background(Primary, RoundedCornerShape(6.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Default.Layers,
                        contentDescription = "Logo",
                        tint = OnPrimary,
                        modifier = Modifier.size(22.dp)
                    )
                }
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text(
                            text = "MINECYCLE AI",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Primary
                        )
                        Box(
                            modifier = Modifier
                                .background(SecondaryContainer, RoundedCornerShape(4.dp))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = activeRole.badgeText,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                color = OnSecondaryFixed
                            )
                        }
                    }
                    Text(
                        text = "National Mineral Tailings & Critical Raw Materials Registry",
                        fontSize = 10.sp,
                        color = OnSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                IconButton(
                    onClick = onNotificationsClick,
                    modifier = Modifier.size(32.dp)
                ) {
                    BadgedBox(
                        badge = {
                            if (unreadNotificationCount > 0) {
                                Badge(
                                    containerColor = ErrorColor,
                                    contentColor = OnError
                                ) {
                                    Text(unreadNotificationCount.toString(), fontSize = 9.sp)
                                }
                            }
                        }
                    ) {
                        Icon(Icons.Default.Notifications, contentDescription = "Alerts", tint = OnSurfaceVariant, modifier = Modifier.size(20.dp))
                    }
                }

                IconButton(
                    onClick = onEmergencyLock,
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(Icons.Default.LockClock, contentDescription = "Circuit Breaker", tint = ErrorColor, modifier = Modifier.size(20.dp))
                }

                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .background(PrimaryContainer, RoundedCornerShape(6.dp))
                        .clickable(onClick = onProfileClick),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = when (activeRole) {
                            UserRole.ADMIN -> "SKV"
                            UserRole.WASTE_PROVIDER -> "HCL"
                            UserRole.RECYCLER -> "HIN"
                            UserRole.MANUFACTURER -> "BHL"
                        },
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = OnPrimaryContainer
                    )
                }
            }
        }
    }
}

@Composable
fun StatusBadge(status: String) {
    val (bgColor, textColor) = when (status.uppercase(Locale.getDefault())) {
        "COMPLETED", "SETTLED", "RECYCLED", "VERIFIED", "PASS", "AUDITED" -> MintSuccessBg to MintSuccess
        "IN TRANSIT", "PROCESSING", "ACCEPTED", "IN BENEFICIATION" -> SecondaryContainer to OnSecondaryFixed
        "REQUESTED", "REVIEWING", "PACKED", "PENDING" -> AmberAlert to AmberAlertText
        "ESCROW HELD", "FROZEN", "FLAGGED", "FAILED", "BYPASSED", "OVERLOAD" -> ErrorContainer to ErrorColor
        else -> SurfaceContainerHigh to OnSurfaceVariant
    }

    Surface(
        color = bgColor,
        shape = RoundedCornerShape(4.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, textColor.copy(alpha = 0.3f))
    ) {
        Text(
            text = status,
            color = textColor,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
        )
    }
}

@Composable
fun KpiCard(
    title: String,
    value: String,
    subValue: String? = null,
    subStatus: String? = null,
    icon: ImageVector,
    iconTint: Color = Primary,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(6.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceContainerLowest),
        border = androidx.compose.foundation.BorderStroke(1.dp, OutlineVariant)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title.uppercase(Locale.getDefault()),
                    fontSize = 10.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = OnSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Icon(icon, contentDescription = null, tint = iconTint, modifier = Modifier.size(18.dp))
            }
            Spacer(Modifier.height(4.dp))
            Text(
                text = value,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = OnSurface
            )
            if (subValue != null || subStatus != null) {
                Spacer(Modifier.height(6.dp))
                HorizontalDivider(color = SurfaceContainerHigh, thickness = 1.dp)
                Spacer(Modifier.height(6.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (subStatus != null) {
                        Text(
                            text = subStatus,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Medium,
                            color = Primary
                        )
                    }
                    if (subValue != null) {
                        Text(
                            text = subValue,
                            fontSize = 10.sp,
                            color = OnSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun VisualTimeline(
    currentStepIndex: Int,
    steps: List<String>,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            steps.forEachIndexed { index, label ->
                val stepNum = index + 1
                val isDone = stepNum < currentStepIndex
                val isCurrent = stepNum == currentStepIndex

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.width(80.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(26.dp)
                                .background(
                                    color = when {
                                        isDone -> Primary
                                        isCurrent -> Secondary
                                        else -> SurfaceContainerHigh
                                    },
                                    shape = CircleShape
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            if (isDone) {
                                Icon(Icons.Default.Check, contentDescription = null, tint = OnPrimary, modifier = Modifier.size(14.dp))
                            } else {
                                Text(
                                    text = stepNum.toString(),
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isCurrent) OnSecondary else OnSurfaceVariant
                                )
                            }
                        }
                        Spacer(Modifier.height(4.dp))
                        Text(
                            text = label,
                            fontSize = 9.sp,
                            fontWeight = if (isCurrent) FontWeight.Bold else FontWeight.Normal,
                            color = if (isCurrent) Primary else OnSurfaceVariant,
                            textAlign = TextAlign.Center,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                    }

                    if (index < steps.size - 1) {
                        Box(
                            modifier = Modifier
                                .width(24.dp)
                                .height(2.dp)
                                .background(if (isDone) Primary else SurfaceContainerHigh)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun DigitalPassportDialog(
    passportId: String = "DPP-IND-2025-CU-091",
    consignmentId: String = "WST-2025-9482",
    productName: String = "Secondary Copper Sponge (Cu 99.4%)",
    originMine: String = "Hindustan Copper (Malanjkhand Pit 4)",
    tonnage: String = "420.00 MT",
    emissionsSaved: String = "-1.82 tCO2e / tCu",
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(8.dp),
            colors = CardDefaults.cardColors(containerColor = SurfaceContainerLowest),
            border = androidx.compose.foundation.BorderStroke(1.dp, OutlineVariant),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Icon(Icons.Default.QrCode2, contentDescription = null, tint = Primary, modifier = Modifier.size(24.dp))
                        Column {
                            Text("Digital Mineral Passport (DPP)", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                            Text("Sovereign Registry • MMDR Sec 14A", fontSize = 10.sp, color = OnSurfaceVariant)
                        }
                    }
                    IconButton(onClick = onDismiss, modifier = Modifier.size(24.dp)) {
                        Icon(Icons.Default.Close, contentDescription = "Close", modifier = Modifier.size(18.dp))
                    }
                }

                HorizontalDivider(color = SurfaceContainerHigh)

                // Visual QR Simulator Card
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(SurfaceContainerLow, RoundedCornerShape(6.dp))
                        .padding(10.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // QR block
                    Box(
                        modifier = Modifier
                            .size(90.dp)
                            .background(SurfaceContainerLowest, RoundedCornerShape(4.dp))
                            .border(1.dp, OutlineVariant, RoundedCornerShape(4.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(Icons.Default.QrCode2, contentDescription = null, tint = Primary, modifier = Modifier.size(60.dp))
                            Text(consignmentId, fontSize = 8.sp, fontFamily = FontFamily.Monospace, color = OnSurfaceVariant)
                        }
                    }

                    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                        Text("Passport ID: $passportId", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Primary, fontFamily = FontFamily.Monospace)
                        Text("Origin: $originMine", fontSize = 11.sp, color = OnSurface)
                        Text("Metal: $productName", fontSize = 11.sp, color = OnSurface)
                        Text("Net Mass: $tonnage", fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                        Text("Avoided: $emissionsSaved", fontSize = 11.sp, color = Secondary, fontWeight = FontWeight.SemiBold)
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            StatusBadge("STQC GRADE A+")
                        }
                    }
                }

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(SurfaceContainerHigh, RoundedCornerShape(4.dp))
                        .padding(8.dp),
                    verticalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    Text("Crypto Hash: 0x82f918ba0391c4...441e", fontSize = 9.sp, fontFamily = FontFamily.Monospace, color = OnSurfaceVariant)
                    Text("Audit Officer DSC: DR. S. K. VERMA (VALIDATED)", fontSize = 9.sp, fontFamily = FontFamily.Monospace, color = Primary)
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("Close", fontSize = 12.sp)
                    }
                    Spacer(Modifier.width(8.dp))
                    Button(
                        onClick = onDismiss,
                        shape = RoundedCornerShape(4.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Primary)
                    ) {
                        Icon(Icons.Default.Download, contentDescription = null, modifier = Modifier.size(14.dp))
                        Spacer(Modifier.width(4.dp))
                        Text("Download PDF", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
fun QrScannerDialog(
    onDismiss: () -> Unit,
    onResultFound: (String) -> Unit
) {
    var searchInput by remember { mutableStateOf("") }
    var quickSelection by remember { mutableStateOf("") }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(8.dp),
            colors = CardDefaults.cardColors(containerColor = SurfaceContainerLowest),
            border = androidx.compose.foundation.BorderStroke(1.dp, OutlineVariant),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Icon(Icons.Default.QrCodeScanner, contentDescription = null, tint = Primary)
                        Text("Digital QR Scanner & Audit", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                    }
                    IconButton(onClick = onDismiss, modifier = Modifier.size(24.dp)) {
                        Icon(Icons.Default.Close, contentDescription = null, modifier = Modifier.size(18.dp))
                    }
                }

                // Simulated Scanner Viewfinder
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(130.dp)
                        .background(Color(0xFF0F172A), RoundedCornerShape(6.dp))
                        .border(2.dp, Primary, RoundedCornerShape(6.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(Icons.Default.FilterCenterFocus, contentDescription = null, tint = PrimaryFixed, modifier = Modifier.size(48.dp))
                        Spacer(Modifier.height(4.dp))
                        Text("Align Consignment / Product QR in Viewfinder", fontSize = 10.sp, color = Color.LightGray)
                    }
                }

                Text("Or Enter ID Directly:", fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = OnSurfaceVariant)

                OutlinedTextField(
                    value = searchInput,
                    onValueChange = { searchInput = it },
                    placeholder = { Text("e.g. WST-2025-9482 or PRD-CU-99", fontSize = 12.sp) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    textStyle = androidx.compose.ui.text.TextStyle(fontSize = 12.sp, fontFamily = FontFamily.Monospace)
                )

                Text("Quick Demo Targets:", fontSize = 10.sp, color = OnSurfaceVariant)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    AssistChip(
                        onClick = { searchInput = "WST-2025-9482" },
                        label = { Text("WST-9482", fontSize = 10.sp) }
                    )
                    AssistChip(
                        onClick = { searchInput = "PRD-CU-99" },
                        label = { Text("PRD-CU-99", fontSize = 10.sp) }
                    )
                    AssistChip(
                        onClick = { searchInput = "PO-MFR-2025-019" },
                        label = { Text("PO-019", fontSize = 10.sp) }
                    )
                }

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                    TextButton(onClick = onDismiss) { Text("Cancel", fontSize = 12.sp) }
                    Spacer(Modifier.width(8.dp))
                    Button(
                        onClick = {
                            if (searchInput.isNotBlank()) {
                                onResultFound(searchInput.trim())
                            }
                        },
                        shape = RoundedCornerShape(4.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Primary)
                    ) {
                        Text("Verify & Open", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
