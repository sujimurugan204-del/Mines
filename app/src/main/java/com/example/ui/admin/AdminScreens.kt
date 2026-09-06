package com.example.ui.admin

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.DailyDataRecord
import com.example.data.model.WasteRequest
import com.example.data.repository.MineCycleRepository
import com.example.ui.components.*
import com.example.ui.theme.*

@Composable
fun AdminDashboardScreen(
    onOpenPassport: (String) -> Unit,
    onNavigateToAIClassification: () -> Unit
) {
    var activeSubTab by remember { mutableStateOf(AdminSubTab.OVERVIEW) }
    var selectedMineralStream by remember { mutableStateOf("All Streams") }
    var showExportNotice by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Surface)
    ) {
        // Horizontal Sub-Navigation Tab Bar
        ScrollableTabRow(
            selectedTabIndex = activeSubTab.ordinal,
            edgePadding = 8.dp,
            containerColor = SurfaceContainerLow,
            contentColor = Primary,
            modifier = Modifier.fillMaxWidth()
        ) {
            AdminSubTab.values().forEach { tab ->
                Tab(
                    selected = activeSubTab == tab,
                    onClick = { activeSubTab = tab },
                    text = {
                        Text(
                            text = tab.label,
                            fontSize = 11.sp,
                            fontWeight = if (activeSubTab == tab) FontWeight.Bold else FontWeight.Medium
                        )
                    }
                )
            }
        }

        if (showExportNotice) {
            Snackbar(
                action = { TextButton(onClick = { showExportNotice = false }) { Text("Dismiss", color = PrimaryFixed) } },
                containerColor = Primary,
                contentColor = OnPrimary,
                modifier = Modifier.padding(8.dp)
            ) {
                Text("Statutory Daily Tonnage Data Export (CSV) downloaded with NIC digital signature.", fontSize = 11.sp)
            }
        }

        when (activeSubTab) {
            AdminSubTab.OVERVIEW -> AdminOverviewContent(
                selectedMineralStream = selectedMineralStream,
                onSelectMineralStream = { selectedMineralStream = it },
                onStatutoryExport = { showExportNotice = true },
                onOpenPassport = onOpenPassport
            )
            AdminSubTab.DATA_SHEET -> AdminDailyDataSheetContent(onExport = { showExportNotice = true })
            AdminSubTab.COMPANIES -> AdminCompaniesContent()
            AdminSubTab.RECYCLERS -> AdminRecyclersContent()
            AdminSubTab.AI_DIAGNOSTIC -> AdminAiDiagnosticContent(onNavigate = onNavigateToAIClassification)
            AdminSubTab.ESCROW -> AdminEscrowContent()
            AdminSubTab.FRAUD_SENTINEL -> AdminFraudSentinelContent()
            AdminSubTab.ENVIRONMENTAL -> AdminEnvironmentalImpactContent()
            AdminSubTab.AUDIT_LOGS -> AdminAuditLogsContent()
        }
    }
}

enum class AdminSubTab(val label: String) {
    OVERVIEW("Overview & KPI"),
    DATA_SHEET("Daily Data Sheet"),
    COMPANIES("Companies"),
    RECYCLERS("Recycler Hubs"),
    AI_DIAGNOSTIC("AI Extraction"),
    ESCROW("DBT Escrow"),
    FRAUD_SENTINEL("Fraud Sentinel"),
    ENVIRONMENTAL("Eco Impact"),
    AUDIT_LOGS("Audit Logs")
}

@Composable
private fun AdminOverviewContent(
    selectedMineralStream: String,
    onSelectMineralStream: (String) -> Unit,
    onStatutoryExport: () -> Unit,
    onOpenPassport: (String) -> Unit
) {
    val wasteRequests by MineCycleRepository.wasteRequests.collectAsState()
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Command Strip Header
        Card(
            shape = RoundedCornerShape(6.dp),
            colors = CardDefaults.cardColors(containerColor = SurfaceContainerLowest),
            border = androidx.compose.foundation.BorderStroke(1.dp, OutlineVariant),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            Text("National Mineral Waste Command", fontSize = 15.sp, fontWeight = FontWeight.Bold)
                            Surface(
                                color = PrimaryFixed,
                                shape = RoundedCornerShape(4.dp)
                            ) {
                                Text(
                                    text = "36 STATES ONLINE",
                                    fontSize = 8.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = OnPrimaryFixed,
                                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                                )
                            }
                        }
                        Text("Statutory real-time supervision under MMDR Act 1957 Section 23C", fontSize = 10.sp, color = OnSurfaceVariant)
                    }
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    listOf("All Streams", "Copper Tailings", "Bauxite Red Mud", "Iron Ore Slimes").forEach { stream ->
                        FilterChip(
                            selected = selectedMineralStream == stream,
                            onClick = { onSelectMineralStream(stream) },
                            label = { Text(stream, fontSize = 10.sp) }
                        )
                    }
                    Button(
                        onClick = onStatutoryExport,
                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                        shape = RoundedCornerShape(4.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Secondary),
                        modifier = Modifier.height(32.dp)
                    ) {
                        Icon(Icons.Default.Download, contentDescription = null, modifier = Modifier.size(14.dp))
                        Spacer(Modifier.width(4.dp))
                        Text("Statutory Export", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // 8 KPI Grid
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            KpiCard(
                title = "Enterprises",
                value = "12,480",
                subStatus = "98 Pending",
                subValue = "+14.2% YoY",
                icon = Icons.Default.Business,
                modifier = Modifier.weight(1f)
            )
            KpiCard(
                title = "Recycler Hubs",
                value = "412",
                subStatus = "STQC Certified",
                subValue = "92.4% Load",
                icon = Icons.Default.Recycling,
                iconTint = Secondary,
                modifier = Modifier.weight(1f)
            )
        }

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            KpiCard(
                title = "Critical Yield",
                value = "142.6K MT",
                subStatus = "Cu: 64kt • Li: 18kt",
                subValue = "REE: 4.8kt",
                icon = Icons.Default.Diamond,
                modifier = Modifier.weight(1f)
            )
            KpiCard(
                title = "DBT Disbursed",
                value = "₹1,842.5 Cr",
                subStatus = "NPCI Direct",
                subValue = "₹4.2Cr Held",
                icon = Icons.Default.Payments,
                iconTint = Tertiary,
                modifier = Modifier.weight(1f)
            )
        }

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            KpiCard(
                title = "Carbon Avoided",
                value = "840,000 t",
                subStatus = "BEE Verified",
                subValue = "Reg #2024-91",
                icon = Icons.Default.Eco,
                modifier = Modifier.weight(1f)
            )
            KpiCard(
                title = "e-RUPI Vouchers",
                value = "₹48.6 Cr",
                subStatus = "32,100 Kabadiwalas",
                subValue = "Instant DBT",
                icon = Icons.Default.CardGiftcard,
                iconTint = Tertiary,
                modifier = Modifier.weight(1f)
            )
        }

        // SpectroNet AI Anomaly Alert Box (From Screenshot 2)
        Card(
            shape = RoundedCornerShape(6.dp),
            colors = CardDefaults.cardColors(containerColor = SurfaceContainerLowest),
            border = androidx.compose.foundation.BorderStroke(1.5.dp, ErrorColor.copy(alpha = 0.5f)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        Icon(Icons.Default.Warning, contentDescription = null, tint = ErrorColor, modifier = Modifier.size(18.dp))
                        Text("SpectroNet AI Anomaly Alert", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = ErrorColor)
                    }
                    Surface(
                        color = ErrorColor,
                        shape = RoundedCornerShape(4.dp)
                    ) {
                        Text(
                            text = "ESCROW HELD",
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            color = OnError,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }

                Surface(
                    color = ErrorContainer.copy(alpha = 0.4f),
                    shape = RoundedCornerShape(4.dp)
                ) {
                    Column(modifier = Modifier.padding(8.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(
                            text = "Consignment #WST-8819 registered weight spiked from 120 MT to 1,250 MT at NH Toll Gate 4. Auto-Frozen under MMDR Sec 23C.",
                            fontSize = 11.sp,
                            color = OnSurface
                        )
                        HorizontalDivider(color = ErrorColor.copy(alpha = 0.2f))
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Pred Cu: 42.4%", fontSize = 10.sp, color = OnSurfaceVariant)
                            Text("Discrepancy: +941%", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = ErrorColor)
                        }
                    }
                }

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Button(
                        onClick = { MineCycleRepository.resolveFraudAnomaly("WST-2025-9478", "dispatch") },
                        shape = RoundedCornerShape(4.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Primary),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Dispatch IBM Auditor", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                    OutlinedButton(
                        onClick = { MineCycleRepository.resolveFraudAnomaly("WST-2025-9478", "override") },
                        shape = RoundedCornerShape(4.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Manual Override", fontSize = 11.sp)
                    }
                }
            }
        }

        // State Machine Live Telemetry
        Card(
            shape = RoundedCornerShape(6.dp),
            colors = CardDefaults.cardColors(containerColor = SurfaceContainerLowest),
            border = androidx.compose.foundation.BorderStroke(1.dp, OutlineVariant),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("Waste Lifecycle State Machine", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                        Text("Real-time 7-stage state telemetry of active industrial manifests", fontSize = 10.sp, color = OnSurfaceVariant)
                    }
                    Text("LIVE SYNC", fontSize = 9.sp, fontFamily = FontFamily.Monospace, color = Primary, fontWeight = FontWeight.Bold)
                }

                // 7 Segment Boxes
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    StateStepBox("Pending", "142")
                    StateStepBox("Assigned", "89", color = Secondary)
                    StateStepBox("Accepted", "76", color = Secondary)
                    StateStepBox("Collected", "210", color = Tertiary)
                    StateStepBox("Processing", "340", color = Primary)
                    StateStepBox("Recycled", "520", color = Primary)
                    StateStepBox("Settled", "1,450", isHighlight = true)
                }

                // Recent Manifests Table Mobile Cards
                Text("Active High-Priority Consignments", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = OnSurfaceVariant)
                wasteRequests.take(3).forEach { req ->
                    ManifestRowCard(req = req, onOpenPassport = { onOpenPassport(req.id) })
                }

                // Recoverability Index Bar
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Critical Mineral Recoverability Index", fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                        Text("Overall Yield: 68.4%", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Primary, fontFamily = FontFamily.Monospace)
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(10.dp)
                            .clip(RoundedCornerShape(4.dp))
                    ) {
                        Box(modifier = Modifier.weight(0.44f).fillMaxHeight().background(Primary))
                        Box(modifier = Modifier.weight(0.22f).fillMaxHeight().background(Secondary))
                        Box(modifier = Modifier.weight(0.18f).fillMaxHeight().background(TertiaryContainer))
                        Box(modifier = Modifier.weight(0.16f).fillMaxHeight().background(SurfaceContainerHigh))
                    }
                }
            }
        }

        // Dynamic Recycler Proximity Matrix
        Card(
            shape = RoundedCornerShape(6.dp),
            colors = CardDefaults.cardColors(containerColor = SurfaceContainerLowest),
            border = androidx.compose.foundation.BorderStroke(1.dp, OutlineVariant),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        Icon(Icons.Default.ShareLocation, contentDescription = null, tint = Secondary, modifier = Modifier.size(18.dp))
                        Text("Dynamic Recycler Proximity Matrix", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    }
                    Surface(color = SecondaryContainer, shape = RoundedCornerShape(4.dp)) {
                        Text("GPS-MATRIX", fontSize = 8.sp, fontWeight = FontWeight.Bold, color = OnSecondaryFixed, modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp))
                    }
                }

                // Recycler 1: Normal load
                Surface(
                    color = SurfaceContainerLow,
                    shape = RoundedCornerShape(4.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, OutlineVariant)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("Hindalco Beneficiation Hub #3", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            Text("34 km away • Accepts Cu Slag & Tailings", fontSize = 10.sp, color = OnSurfaceVariant)
                            Spacer(Modifier.height(2.dp))
                            Text("78% Load (Healthy)", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = Primary)
                        }
                        StatusBadge("ASSIGNED")
                    }
                }

                // Recycler 2: Overload ceiling exceeded
                Surface(
                    color = ErrorContainer.copy(alpha = 0.3f),
                    shape = RoundedCornerShape(4.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, ErrorColor.copy(alpha = 0.4f))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("Tata Refractory & Extraction", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = ErrorColor)
                            Text("41 km • Capacity Overload Trigger", fontSize = 10.sp, color = OnSurfaceVariant)
                            Spacer(Modifier.height(2.dp))
                            Text("94% Load (>85% Ceiling Exceeded)", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = ErrorColor)
                        }
                        StatusBadge("BYPASSED")
                    }
                }
            }
        }
    }
}

@Composable
private fun StateStepBox(title: String, count: String, color: Color = OnSurface, isHighlight: Boolean = false) {
    Surface(
        color = if (isHighlight) PrimaryContainer else SurfaceContainerLow,
        shape = RoundedCornerShape(4.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, if (isHighlight) Primary else OutlineVariant),
        modifier = Modifier.width(62.dp)
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 4.dp, vertical = 6.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(title, fontSize = 9.sp, color = if (isHighlight) PrimaryFixed else OnSurfaceVariant)
            Text(count, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = if (isHighlight) OnPrimary else color)
        }
    }
}

@Composable
private fun ManifestRowCard(req: WasteRequest, onOpenPassport: () -> Unit) {
    Surface(
        color = if (req.escrowStatus == com.example.data.model.EscrowStatus.FROZEN) ErrorContainer.copy(alpha = 0.2f) else SurfaceContainerLow,
        shape = RoundedCornerShape(4.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, OutlineVariant),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(req.id, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Primary, fontFamily = FontFamily.Monospace)
                    StatusBadge(req.status.label)
                }
                Text("${req.providerName} • ${req.wasteType}", fontSize = 11.sp, fontWeight = FontWeight.Medium)
                Text("Tonnage: ${req.approximateQuantityTons} MT | AI Conf: ${req.aiConfidence}%", fontSize = 10.sp, color = OnSurfaceVariant)
            }
            IconButton(onClick = onOpenPassport, modifier = Modifier.size(32.dp)) {
                Icon(Icons.Default.QrCode2, contentDescription = "Passport", tint = Primary, modifier = Modifier.size(20.dp))
            }
        }
    }
}

@Composable
private fun AdminDailyDataSheetContent(onExport: () -> Unit) {
    val records by MineCycleRepository.dailyDataSheet.collectAsState()
    var searchQuery by remember { mutableStateOf("") }
    var selectedMineral by remember { mutableStateOf("All") }

    val filteredRecords = records.filter {
        (selectedMineral == "All" || it.wasteType.contains(selectedMineral, true)) &&
        (it.company.contains(searchQuery, true) || it.leaseId.contains(searchQuery, true) || it.wasteType.contains(searchQuery, true))
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text("Daily Statutory Tonnage Sheet", fontSize = 15.sp, fontWeight = FontWeight.Bold)
                Text("Statutory filings across all certified concessions", fontSize = 10.sp, color = OnSurfaceVariant)
            }
            Button(
                onClick = onExport,
                shape = RoundedCornerShape(4.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Primary),
                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                modifier = Modifier.height(30.dp)
            ) {
                Icon(Icons.Default.Download, contentDescription = null, modifier = Modifier.size(14.dp))
                Spacer(Modifier.width(4.dp))
                Text("CSV Export", fontSize = 10.sp, fontWeight = FontWeight.Bold)
            }
        }

        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = { Text("Search by Company, Lease, Waste Type...", fontSize = 11.sp) },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, modifier = Modifier.size(16.dp)) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            textStyle = androidx.compose.ui.text.TextStyle(fontSize = 12.sp)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            listOf("All", "Copper", "Red Mud", "Iron", "Zinc", "Fly Ash").forEach { category ->
                FilterChip(
                    selected = selectedMineral == category,
                    onClick = { selectedMineral = category },
                    label = { Text(category, fontSize = 10.sp) }
                )
            }
        }

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(filteredRecords) { rec ->
                Card(
                    shape = RoundedCornerShape(6.dp),
                    colors = CardDefaults.cardColors(containerColor = SurfaceContainerLowest),
                    border = androidx.compose.foundation.BorderStroke(1.dp, OutlineVariant),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(10.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(rec.leaseId, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Primary, fontFamily = FontFamily.Monospace)
                            StatusBadge(rec.status)
                        }
                        Text(rec.company, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Waste: ${rec.wasteType} (${rec.wasteQuantity} MT)", fontSize = 11.sp, color = OnSurfaceVariant)
                            Text("Recycled: ${rec.recycled} MT", fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = Primary)
                        }
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Product: ${rec.product}", fontSize = 11.sp, color = OnSurface)
                            Text("CO2 Saved: ${rec.carbonSaved} t", fontSize = 11.sp, color = Secondary, fontWeight = FontWeight.SemiBold)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun AdminCompaniesContent() {
    val companies = listOf(
        Triple("Hindustan Copper Ltd", "ML-MP-0091", "Copper Tailings • Madhya Pradesh"),
        Triple("National Aluminium Co (NALCO)", "ML-OD-0412", "Bauxite (Red Mud) • Odisha"),
        Triple("NMDC Kirandul", "ML-CG-0881", "Iron Ore Slimes • Chhattisgarh"),
        Triple("HZL Rampura Agucha", "ML-RJ-0319", "Zinc Jarosite • Rajasthan"),
        Triple("Singhbhum Tailings Consortium", "ML-JH-1102", "Iron Ore Slag • Jharkhand")
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            Text("Registered Mining Concessions (12,480 Units)", fontSize = 14.sp, fontWeight = FontWeight.Bold)
            Text("Licensed operations under MMDR Act 1957 Section 4A", fontSize = 11.sp, color = OnSurfaceVariant)
            Spacer(Modifier.height(4.dp))
        }
        items(companies) { (name, code, desc) ->
            Card(
                shape = RoundedCornerShape(6.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceContainerLowest),
                border = androidx.compose.foundation.BorderStroke(1.dp, OutlineVariant),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(name, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        Text(code, fontSize = 11.sp, fontFamily = FontFamily.Monospace, color = Primary)
                        Text(desc, fontSize = 10.sp, color = OnSurfaceVariant)
                    }
                    StatusBadge("VERIFIED")
                }
            }
        }
    }
}

@Composable
private fun AdminRecyclersContent() {
    val hubs = listOf(
        Triple("Hindalco Beneficiation Hub #3", "REC-STQC-4410", "Cap: 250,000 MT/yr • Cu Extraction • 78% Load (Normal)"),
        Triple("Tata Refractory & Extraction", "REC-STQC-4402", "Cap: 180,000 MT/yr • Bauxite/Alumina • 94% Load (OVERLOAD)"),
        Triple("Vedanta Circular Recovery", "REC-STQC-4422", "Cap: 300,000 MT/yr • Zinc Jarosite • 52% Load (Prime)"),
        Triple("Eco-Cementation Unit 1", "REC-STQC-4390", "Cap: 500,000 MT/yr • Fly Ash Bricks • 64% Load (Normal)")
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            Text("Certified Beneficiation Recycler Hubs (412)", fontSize = 14.sp, fontWeight = FontWeight.Bold)
            Text("STQC certified secondary metal and mineral beneficiation plants", fontSize = 11.sp, color = OnSurfaceVariant)
            Spacer(Modifier.height(4.dp))
        }
        items(hubs) { (name, code, status) ->
            Card(
                shape = RoundedCornerShape(6.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceContainerLowest),
                border = androidx.compose.foundation.BorderStroke(1.dp, OutlineVariant),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(10.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text(name, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        StatusBadge(if (status.contains("OVERLOAD")) "OVERLOAD" else "PASS")
                    }
                    Text(code, fontSize = 10.sp, fontFamily = FontFamily.Monospace, color = Primary)
                    Text(status, fontSize = 10.sp, color = OnSurfaceVariant)
                }
            }
        }
    }
}

@Composable
private fun AdminAiDiagnosticContent(onNavigate: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Card(
            shape = RoundedCornerShape(6.dp),
            colors = CardDefaults.cardColors(containerColor = SurfaceContainerLowest),
            border = androidx.compose.foundation.BorderStroke(1.dp, OutlineVariant),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .background(PrimaryContainer, RoundedCornerShape(6.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Biotech, contentDescription = null, tint = PrimaryFixed, modifier = Modifier.size(28.dp))
                    }
                    Column {
                        Text("SpectroNet AI Extraction Diagnostic", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                        Text("CSIR-NML Benchmarked Deep Learning Model v4.2", fontSize = 11.sp, color = OnSurfaceVariant)
                    }
                }

                HorizontalDivider(color = SurfaceContainerHigh)

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Assay Prediction Accuracy", fontSize = 11.sp, color = OnSurfaceVariant)
                    Text("99.4% (Strict)", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Primary)
                }
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Inference Latency", fontSize = 11.sp, color = OnSurfaceVariant)
                    Text("140ms per scan", fontSize = 11.sp, fontFamily = FontFamily.Monospace)
                }
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Daily Classified Tonnes", fontSize = 11.sp, color = OnSurfaceVariant)
                    Text("32,450 MT", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }

                Button(
                    onClick = onNavigate,
                    shape = RoundedCornerShape(4.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Primary),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(Icons.Default.Psychology, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(Modifier.width(6.dp))
                    Text("Launch AI Classifier & Assay Lab", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
private fun AdminEscrowContent() {
    val payments by MineCycleRepository.payments.collectAsState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            Text("DBT Escrow Settlements & Disbursals", fontSize = 14.sp, fontWeight = FontWeight.Bold)
            Text("Real-time NPCI e-RUPI settlement ledger under RBI/Gov guidelines", fontSize = 11.sp, color = OnSurfaceVariant)
            Spacer(Modifier.height(4.dp))
        }
        items(payments) { p ->
            Card(
                shape = RoundedCornerShape(6.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceContainerLowest),
                border = androidx.compose.foundation.BorderStroke(1.dp, OutlineVariant),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(10.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text(p.transactionId, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Primary, fontFamily = FontFamily.Monospace)
                        StatusBadge(p.status.name)
                    }
                    Text("To: ${p.toBeneficiary}", fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                    Text("Amount: ₹${p.amount.toInt()} • ${p.paymentType}", fontSize = 11.sp, color = OnSurfaceVariant)
                    Text("Hash: ${p.blockchainHash}", fontSize = 9.sp, fontFamily = FontFamily.Monospace, color = Primary)
                }
            }
        }
    }
}

@Composable
private fun AdminFraudSentinelContent() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Card(
            shape = RoundedCornerShape(6.dp),
            colors = CardDefaults.cardColors(containerColor = SurfaceContainerLowest),
            border = androidx.compose.foundation.BorderStroke(1.dp, ErrorColor),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    Icon(Icons.Default.Security, contentDescription = null, tint = ErrorColor)
                    Text("Active Fraud Investigation Sentinel", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = ErrorColor)
                }
                Text("Automatic circuit breakers triggered by weighbridge load-cell discrepancies or mineral assay spoofing.", fontSize = 11.sp, color = OnSurfaceVariant)

                Surface(
                    color = ErrorContainer.copy(alpha = 0.3f),
                    shape = RoundedCornerShape(4.dp)
                ) {
                    Column(modifier = Modifier.padding(10.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text("Case #ANOM-2025-0042 • Kirandul Pit 2", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = ErrorColor)
                        Text("Gross tonnage spiked by 941% at NHAI toll sensor. Escrow hold of ₹4.20 Cr enforced.", fontSize = 11.sp)
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Button(
                                onClick = { MineCycleRepository.resolveFraudAnomaly("WST-2025-9478", "dispatch") },
                                colors = ButtonDefaults.buttonColors(containerColor = ErrorColor),
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("Issue Seizure Order", fontSize = 11.sp)
                            }
                            OutlinedButton(
                                onClick = { MineCycleRepository.resolveFraudAnomaly("WST-2025-9478", "override") },
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("Clear Discrepancy", fontSize = 11.sp)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun AdminEnvironmentalImpactContent() {
    val metrics by MineCycleRepository.environmentalMetrics.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text("National Environmental Impact Dashboard", fontSize = 14.sp, fontWeight = FontWeight.Bold)
        Text("Verified circularity contributions registered with Bureau of Energy Efficiency (BEE)", fontSize = 11.sp, color = OnSurfaceVariant)

        Card(
            shape = RoundedCornerShape(6.dp),
            colors = CardDefaults.cardColors(containerColor = SurfaceContainerLowest),
            border = androidx.compose.foundation.BorderStroke(1.dp, OutlineVariant),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                ImpactMetricRow("Total Mining Waste Recycled", "${metrics.totalWasteRecycledMT / 1000000} Million MT", Icons.Default.Recycling, Primary)
                ImpactMetricRow("Critical Materials Recovered", "${metrics.materialRecoveredMT / 1000}K Tonnes", Icons.Default.Diamond, Secondary)
                ImpactMetricRow("Net CO2 Emissions Avoided", "${metrics.co2AvoidedTons / 1000}K tCO2e", Icons.Default.Eco, Primary)
                ImpactMetricRow("Industrial Energy Saved", "${metrics.energySavedMWh / 1000000} Million MWh", Icons.Default.Bolt, Tertiary)
                ImpactMetricRow("Virgin Raw Material Conserved", "${metrics.rawMaterialConservedMT / 1000000} Million MT", Icons.Default.Terrain, Secondary)
            }
        }
    }
}

@Composable
private fun ImpactMetricRow(label: String, value: String, icon: androidx.compose.ui.graphics.vector.ImageVector, tint: Color) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Icon(icon, contentDescription = null, tint = tint, modifier = Modifier.size(20.dp))
            Text(label, fontSize = 12.sp, color = OnSurface)
        }
        Text(value, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Primary, fontFamily = FontFamily.Monospace)
    }
}

@Composable
private fun AdminAuditLogsContent() {
    val logs = listOf(
        Triple("10:48:12 IST", "MANIFEST_SIGNED #WST-9482 by DR_SK_VERMA", "0x4a8f9b...129c"),
        Triple("10:45:01 IST", "WEIGHBRIDGE_TARE_CAPTURED #WB-TOLL-04", "0x88fc12...99ee"),
        Triple("10:42:30 IST", "ESCROW_FROZEN #TXN-NPCI-9918 (ANOMALY_TRIGGER)", "0xee41a0...77fa"),
        Triple("10:30:14 IST", "AI_SPECTRAL_ASSAY_SUBMITTED #AIC-2025-001", "0x33b190...88ab"),
        Triple("10:15:00 IST", "ERUPI_VOUCHERS_DISBURSED ₹38,400 to Cluster 12", "0x992fa1...881b")
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            Text("Immutable Sovereign Audit Trail", fontSize = 14.sp, fontWeight = FontWeight.Bold)
            Text("Hyperledger fabric cryptographic event sequence", fontSize = 11.sp, color = OnSurfaceVariant)
            Spacer(Modifier.height(4.dp))
        }
        items(logs) { (time, event, hash) ->
            Surface(
                color = if (event.contains("FROZEN")) ErrorContainer.copy(alpha = 0.25f) else SurfaceContainerLow,
                shape = RoundedCornerShape(4.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, OutlineVariant),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(8.dp), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text(time, fontSize = 9.sp, color = OnSurfaceVariant, fontFamily = FontFamily.Monospace)
                        Text(hash, fontSize = 9.sp, color = Primary, fontFamily = FontFamily.Monospace)
                    }
                    Text(event, fontSize = 11.sp, fontWeight = FontWeight.Medium, fontFamily = FontFamily.Monospace)
                }
            }
        }
    }
}
