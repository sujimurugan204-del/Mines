package com.example.ui.provider

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.WasteLifecycleStatus
import com.example.data.model.WasteRequest
import com.example.data.repository.MineCycleRepository
import com.example.ui.components.*
import com.example.ui.theme.*

@Composable
fun WasteProviderScreen(
    onOpenPassport: (String) -> Unit
) {
    var activeTab by remember { mutableStateOf(ProviderTab.OVERVIEW) }
    var selectedRequestForDetails by remember { mutableStateOf<WasteRequest?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Surface)
    ) {
        ScrollableTabRow(
            selectedTabIndex = activeTab.ordinal,
            edgePadding = 8.dp,
            containerColor = SurfaceContainerLow,
            contentColor = Primary,
            modifier = Modifier.fillMaxWidth()
        ) {
            ProviderTab.values().forEach { tab ->
                Tab(
                    selected = activeTab == tab,
                    onClick = {
                        activeTab = tab
                        if (tab != ProviderTab.REQUEST_STATUS) selectedRequestForDetails = null
                    },
                    text = {
                        Text(
                            text = tab.label,
                            fontSize = 11.sp,
                            fontWeight = if (activeTab == tab) FontWeight.Bold else FontWeight.Medium
                        )
                    }
                )
            }
        }

        when (activeTab) {
            ProviderTab.OVERVIEW -> ProviderOverviewContent(
                onCreateClick = { activeTab = ProviderTab.CREATE_REQUEST },
                onViewRequest = { req ->
                    selectedRequestForDetails = req
                    activeTab = ProviderTab.REQUEST_STATUS
                },
                onOpenPassport = onOpenPassport
            )
            ProviderTab.CREATE_REQUEST -> ProviderCreateRequestContent(
                onRequestCreated = { req ->
                    selectedRequestForDetails = req
                    activeTab = ProviderTab.REQUEST_STATUS
                }
            )
            ProviderTab.MY_REQUESTS -> ProviderMyRequestsContent(
                onSelectRequest = { req ->
                    selectedRequestForDetails = req
                    activeTab = ProviderTab.REQUEST_STATUS
                },
                onOpenPassport = onOpenPassport
            )
            ProviderTab.REQUEST_STATUS -> ProviderRequestDetailsContent(
                request = selectedRequestForDetails,
                onOpenPassport = onOpenPassport
            )
            ProviderTab.COLLECTION_STATUS -> ProviderCollectionStatusContent()
            ProviderTab.RECYCLING_STATUS -> ProviderRecyclingStatusContent()
            ProviderTab.AMOUNT_STATUS -> ProviderAmountStatusContent()
            ProviderTab.COMPLIANCE -> ProviderComplianceContent()
        }
    }
}

enum class ProviderTab(val label: String) {
    OVERVIEW("Overview"),
    CREATE_REQUEST("Create Request"),
    MY_REQUESTS("My Manifests"),
    REQUEST_STATUS("10-Stage Status"),
    COLLECTION_STATUS("Collection & Weighbridge"),
    RECYCLING_STATUS("Recovery Yield"),
    AMOUNT_STATUS("DBT Escrow"),
    COMPLIANCE("MMDR Compliance")
}

@Composable
private fun ProviderOverviewContent(
    onCreateClick: () -> Unit,
    onViewRequest: (WasteRequest) -> Unit,
    onOpenPassport: (String) -> Unit
) {
    val requests by MineCycleRepository.wasteRequests.collectAsState()
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Hero Card
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
                        Text("Mining Concession Waste Registry", fontSize = 15.sp, fontWeight = FontWeight.Bold)
                        Text("Hindustan Copper Limited • Lease: ML-MP-0091", fontSize = 11.sp, color = OnSurfaceVariant)
                    }
                    StatusBadge("STQC ACTIVE")
                }

                HorizontalDivider(color = SurfaceContainerHigh)

                Button(
                    onClick = onCreateClick,
                    shape = RoundedCornerShape(4.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Primary),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(Icons.Default.AddCircle, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(Modifier.width(6.dp))
                    Text("Register New Waste Consignment / Manifest", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
        }

        // Tonnage Metrics
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            KpiCard(
                title = "Tailings Registered",
                value = "2,170 MT",
                subStatus = "3 Ponds Active",
                subValue = "Malanjkhand Pit 4",
                icon = Icons.Default.Terrain,
                modifier = Modifier.weight(1f)
            )
            KpiCard(
                title = "Escrow Disbursed",
                value = "₹43.0 Lakh",
                subStatus = "Bank of Baroda",
                subValue = "NPCI Verified",
                icon = Icons.Default.AccountBalance,
                iconTint = MintSuccess,
                modifier = Modifier.weight(1f)
            )
        }

        Text("Active Tailings Manifests", fontSize = 13.sp, fontWeight = FontWeight.Bold)

        requests.forEach { req ->
            Card(
                shape = RoundedCornerShape(6.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceContainerLowest),
                border = androidx.compose.foundation.BorderStroke(1.dp, OutlineVariant),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onViewRequest(req) }
            ) {
                Column(modifier = Modifier.padding(10.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text(req.id, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Primary, fontFamily = FontFamily.Monospace)
                        StatusBadge(req.status.label)
                    }
                    Text(req.wasteType, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                    Text("Tonnage: ${req.approximateQuantityTons} MT • ${req.location}", fontSize = 11.sp, color = OnSurfaceVariant)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("AI Confidence: ${req.aiConfidence}%", fontSize = 10.sp, color = Primary, fontWeight = FontWeight.Medium)
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            TextButton(onClick = { onOpenPassport(req.id) }) {
                                Icon(Icons.Default.QrCode2, contentDescription = null, modifier = Modifier.size(14.dp))
                                Spacer(Modifier.width(2.dp))
                                Text("QR", fontSize = 10.sp)
                            }
                            TextButton(onClick = { onViewRequest(req) }) {
                                Text("Track 10-Stage →", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ProviderCreateRequestContent(
    onRequestCreated: (WasteRequest) -> Unit
) {
    val wasteTypes = listOf(
        "Copper Slag & Flotation Tailings",
        "Bauxite Residue (Alkaline Red Mud)",
        "Iron Ore Beneficiation Slimes",
        "Neutralized Zinc Jarosite Residue",
        "Thermal Power Fly Ash & Bottom Slag",
        "Lithic Pegmatite & Mica Tailings"
    )

    var selectedWasteType by remember { mutableStateOf(wasteTypes[0]) }
    var quantityText by remember { mutableStateOf("420") }
    var location by remember { mutableStateOf("Pit 4, Malanjkhand Complex, Balaghat, MP") }
    var collectionDate by remember { mutableStateOf("2025-05-24") }
    var collectionTime by remember { mutableStateOf("10:00 IST") }
    var description by remember { mutableStateOf("High-density dewatered copper tailings filter cake with 1.25% recoverable copper fraction.") }
    var wasteImageAttached by remember { mutableStateOf(true) }
    var errorMsg by remember { mutableStateOf<String?>(null) }
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Card(
            shape = RoundedCornerShape(6.dp),
            colors = CardDefaults.cardColors(containerColor = SurfaceContainerLowest),
            border = androidx.compose.foundation.BorderStroke(1.dp, OutlineVariant),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Icon(Icons.Default.PostAdd, contentDescription = null, tint = Primary)
                    Column {
                        Text("Create Waste Manifest Consignment", fontSize = 15.sp, fontWeight = FontWeight.Bold)
                        Text("Generates unique tracking ID & SpectroNet XRD Assay", fontSize = 11.sp, color = OnSurfaceVariant)
                    }
                }

                HorizontalDivider(color = SurfaceContainerHigh)

                Text("Waste Stream / Category", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Primary)

                var dropdownExpanded by remember { mutableStateOf(false) }
                ExposedDropdownMenuBox(
                    expanded = dropdownExpanded,
                    onExpandedChange = { dropdownExpanded = it }
                ) {
                    OutlinedTextField(
                        value = selectedWasteType,
                        onValueChange = {},
                        readOnly = true,
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = dropdownExpanded) },
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth(),
                        textStyle = androidx.compose.ui.text.TextStyle(fontSize = 12.sp)
                    )
                    ExposedDropdownMenu(
                        expanded = dropdownExpanded,
                        onDismissRequest = { dropdownExpanded = false }
                    ) {
                        wasteTypes.forEach { type ->
                            DropdownMenuItem(
                                text = { Text(type, fontSize = 12.sp) },
                                onClick = {
                                    selectedWasteType = type
                                    dropdownExpanded = false
                                }
                            )
                        }
                    }
                }

                OutlinedTextField(
                    value = quantityText,
                    onValueChange = { quantityText = it },
                    label = { Text("Approximate Quantity in Metric Tonnes (MT)", fontSize = 11.sp) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    textStyle = androidx.compose.ui.text.TextStyle(fontSize = 12.sp)
                )

                OutlinedTextField(
                    value = location,
                    onValueChange = { location = it },
                    label = { Text("Pit-Head / Tailing Dam / Dispatch Location", fontSize = 11.sp) },
                    modifier = Modifier.fillMaxWidth(),
                    textStyle = androidx.compose.ui.text.TextStyle(fontSize = 12.sp)
                )

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = collectionDate,
                        onValueChange = { collectionDate = it },
                        label = { Text("Collection Date", fontSize = 11.sp) },
                        modifier = Modifier.weight(1f),
                        singleLine = true,
                        textStyle = androidx.compose.ui.text.TextStyle(fontSize = 12.sp)
                    )
                    OutlinedTextField(
                        value = collectionTime,
                        onValueChange = { collectionTime = it },
                        label = { Text("Collection Time", fontSize = 11.sp) },
                        modifier = Modifier.weight(1f),
                        singleLine = true,
                        textStyle = androidx.compose.ui.text.TextStyle(fontSize = 12.sp)
                    )
                }

                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Material Assay Notes / Moisture / Particle Size", fontSize = 11.sp) },
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 3,
                    textStyle = androidx.compose.ui.text.TextStyle(fontSize = 12.sp)
                )

                // Waste Image / Assay Upload
                Surface(
                    color = SurfaceContainerLow,
                    shape = RoundedCornerShape(4.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, OutlineVariant),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Icon(Icons.Default.CameraAlt, contentDescription = null, tint = Primary)
                            Column {
                                Text("SpectroNet Assay Image / Photo", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                Text(if (wasteImageAttached) "sample_copper_slag_assay.jpg (Attached)" else "No image attached", fontSize = 10.sp, color = OnSurfaceVariant)
                            }
                        }
                        Switch(
                            checked = wasteImageAttached,
                            onCheckedChange = { wasteImageAttached = it }
                        )
                    }
                }

                if (errorMsg != null) {
                    Text(errorMsg!!, color = ErrorColor, fontSize = 11.sp)
                }

                Button(
                    onClick = {
                        val qty = quantityText.toDoubleOrNull()
                        if (qty == null || qty <= 0) {
                            errorMsg = "Please enter a valid tonnage."
                        } else {
                            val req = MineCycleRepository.createWasteRequest(
                                wasteType = selectedWasteType,
                                quantityTons = qty,
                                location = location,
                                collectionDate = collectionDate,
                                collectionTime = collectionTime,
                                description = description,
                                imageUrl = if (wasteImageAttached) "sample_copper_slag_assay.jpg" else null
                            )
                            onRequestCreated(req)
                        }
                    },
                    shape = RoundedCornerShape(4.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Primary),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(Icons.Default.Verified, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(Modifier.width(6.dp))
                    Text("Register Consignment & Generate Waste ID", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
private fun ProviderMyRequestsContent(
    onSelectRequest: (WasteRequest) -> Unit,
    onOpenPassport: (String) -> Unit
) {
    val requests by MineCycleRepository.wasteRequests.collectAsState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            Text("Registered Waste Consignments (${requests.size})", fontSize = 14.sp, fontWeight = FontWeight.Bold)
            Text("Click on any consignment to view the full 10-stage lifecycle progress", fontSize = 11.sp, color = OnSurfaceVariant)
            Spacer(Modifier.height(4.dp))
        }
        items(requests) { req ->
            Card(
                shape = RoundedCornerShape(6.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceContainerLowest),
                border = androidx.compose.foundation.BorderStroke(1.dp, OutlineVariant),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onSelectRequest(req) }
            ) {
                Column(modifier = Modifier.padding(10.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text(req.id, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Primary, fontFamily = FontFamily.Monospace)
                        StatusBadge(req.status.label)
                    }
                    Text("${req.wasteType} • ${req.approximateQuantityTons} MT", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                    Text("Recycler: ${req.allocatedRecycler ?: "Pending Allocation"}", fontSize = 11.sp, color = OnSurfaceVariant)
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Escrow: ₹${req.escrowAmount.toInt()}", fontSize = 10.sp, color = Primary, fontWeight = FontWeight.Bold)
                        TextButton(onClick = { onOpenPassport(req.id) }) {
                            Icon(Icons.Default.QrCode2, contentDescription = null, modifier = Modifier.size(14.dp))
                            Spacer(Modifier.width(2.dp))
                            Text("Passport", fontSize = 10.sp)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ProviderRequestDetailsContent(
    request: WasteRequest?,
    onOpenPassport: (String) -> Unit
) {
    val allRequests by MineCycleRepository.wasteRequests.collectAsState()
    val currentRequest = request ?: allRequests.firstOrNull()

    if (currentRequest == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("No active request selected.", color = OnSurfaceVariant)
        }
        return
    }

    val steps = listOf(
        "Requested",
        "Verified",
        "Recycler Assigned",
        "Accepted",
        "Collected",
        "Processing",
        "Recycled",
        "Product Created",
        "Sold",
        "Completed"
    )

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
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
                        Text(currentRequest.id, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Primary, fontFamily = FontFamily.Monospace)
                        Text(currentRequest.wasteType, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                    }
                    StatusBadge(currentRequest.status.label)
                }

                HorizontalDivider(color = SurfaceContainerHigh)

                Text("10-Stage Statutory Lifecycle Timeline", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Primary)

                VisualTimeline(
                    currentStepIndex = currentRequest.status.stepIndex,
                    steps = steps,
                    modifier = Modifier.fillMaxWidth()
                )

                // Advance Simulation Action
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Advance Lifecycle (Demo Mode):", fontSize = 11.sp, color = OnSurfaceVariant)
                    Button(
                        onClick = { MineCycleRepository.advanceWasteLifecycle(currentRequest.id) },
                        shape = RoundedCornerShape(4.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Primary),
                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                        modifier = Modifier.height(30.dp)
                    ) {
                        Text("Next Stage →", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // Details breakdown
        Card(
            shape = RoundedCornerShape(6.dp),
            colors = CardDefaults.cardColors(containerColor = SurfaceContainerLowest),
            border = androidx.compose.foundation.BorderStroke(1.dp, OutlineVariant),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text("Manifest Specifications", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                DetailRow("Origin Concession", currentRequest.providerName)
                DetailRow("Mining Lease Code", currentRequest.leaseCode)
                DetailRow("Tonnage Registered", "${currentRequest.approximateQuantityTons} MT")
                DetailRow("Assay Prediction", currentRequest.predictedComposition)
                DetailRow("AI Confidence", "${currentRequest.aiConfidence}%")
                DetailRow("Allocated Recycler", currentRequest.allocatedRecycler ?: "Awaiting Allocation")
                DetailRow("Assigned Truck RFID", currentRequest.truckRfid ?: "DL-01-AX-9921")
                DetailRow("Driver In Charge", currentRequest.driverName ?: "Mohan Singh")
                DetailRow("Escrow Amount", "₹${currentRequest.escrowAmount.toInt()}")
                DetailRow("Escrow Status", currentRequest.escrowStatus.label)

                Button(
                    onClick = { onOpenPassport(currentRequest.id) },
                    shape = RoundedCornerShape(4.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Secondary),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(Icons.Default.QrCode2, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(Modifier.width(6.dp))
                    Text("View Digital Mineral Passport QR", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
private fun DetailRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, fontSize = 11.sp, color = OnSurfaceVariant)
        Text(value, fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = OnSurface)
    }
}

@Composable
private fun ProviderCollectionStatusContent() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Card(
            shape = RoundedCornerShape(6.dp),
            colors = CardDefaults.cardColors(containerColor = SurfaceContainerLowest),
            border = androidx.compose.foundation.BorderStroke(1.dp, OutlineVariant),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Weighbridge Sensor & Fleet Telemetry", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                Text("Synced via IoT Load-Cell Weighbridge (STQC Validated)", fontSize = 10.sp, color = OnSurfaceVariant)

                Surface(
                    color = SurfaceContainerLow,
                    shape = RoundedCornerShape(4.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, OutlineVariant)
                ) {
                    Column(modifier = Modifier.padding(10.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Truck RFID: DL-01-AX-9921", fontSize = 11.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
                            StatusBadge("WEIGHBRIDGE SYNCED")
                        }
                        Text("Gross Weight: 62.40 MT", fontSize = 11.sp)
                        Text("Tare Weight: 20.40 MT", fontSize = 11.sp)
                        Text("Net Pay Tonnage: 42.00 MT", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Primary)
                    }
                }
            }
        }
    }
}

@Composable
private fun ProviderRecyclingStatusContent() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Card(
            shape = RoundedCornerShape(6.dp),
            colors = CardDefaults.cardColors(containerColor = SurfaceContainerLowest),
            border = androidx.compose.foundation.BorderStroke(1.dp, OutlineVariant),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Beneficiation & Extraction Yield", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                DetailRow("Input Tailings Mass", "420.0 MT")
                DetailRow("Recovered Copper Sponge", "5.37 MT (99.5% Cu)")
                DetailRow("Recovered Iron Pellets", "160.4 MT (64% Fe)")
                DetailRow("Geopolymer Aggregate", "226.8 MT")
                DetailRow("Tailings Slag Loss", "27.43 MT (6.5%)")
                DetailRow("Overall Circularity Yield", "93.5%")
                StatusBadge("QUALITY ASSAY VERIFIED")
            }
        }
    }
}

@Composable
private fun ProviderAmountStatusContent() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Card(
            shape = RoundedCornerShape(6.dp),
            colors = CardDefaults.cardColors(containerColor = SurfaceContainerLowest),
            border = androidx.compose.foundation.BorderStroke(1.dp, OutlineVariant),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Direct Benefit Transfer (DBT) Escrow Account", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                DetailRow("Beneficiary Account", "Bank of Baroda •••• 4019")
                DetailRow("Total Settled", "₹24,50,000")
                DetailRow("Pending Escrow Hold", "₹18,50,000")
                DetailRow("Transaction Reference", "TXN-NPCI-9921")
                StatusBadge("SETTLED")
            }
        }
    }
}

@Composable
private fun ProviderComplianceContent() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Card(
            shape = RoundedCornerShape(6.dp),
            colors = CardDefaults.cardColors(containerColor = SurfaceContainerLowest),
            border = androidx.compose.foundation.BorderStroke(1.dp, OutlineVariant),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Statutory Compliance & Legal Guidelines", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                Text("Under Mines and Minerals (Development and Regulation) Act 1957:", fontSize = 11.sp, color = OnSurfaceVariant)
                HorizontalDivider(color = SurfaceContainerHigh)
                Text("• Section 14A: Mandatory reporting of critical raw materials contained in mine dumps, tailings, and metallurgical slag.", fontSize = 11.sp)
                Text("• Section 23C: Powers of State Governments to prevent illegal mining, transportation, and storage through digital e-transit passes.", fontSize = 11.sp)
                Text("• Rule 37: Prohibition on unauthorized dilution or disposal of mineral tailings without certified STQC beneficiation audit.", fontSize = 11.sp)
                Surface(
                    color = PrimaryContainer,
                    shape = RoundedCornerShape(4.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Concession Status: COMPLIANT • STQC License Valid till Dec 2026",
                        color = OnPrimaryContainer,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(8.dp)
                    )
                }
            }
        }
    }
}
