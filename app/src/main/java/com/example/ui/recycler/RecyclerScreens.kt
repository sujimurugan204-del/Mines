package com.example.ui.recycler

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
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.ai.AIClassificationService
import com.example.data.model.AIClassificationResult
import com.example.data.model.RecycledProduct
import com.example.data.model.WasteLifecycleStatus
import com.example.data.model.WasteRequest
import com.example.data.repository.MineCycleRepository
import com.example.ui.components.*
import com.example.ui.theme.*
import kotlinx.coroutines.launch

@Composable
fun RecyclerHubScreen(
    onOpenPassport: (String) -> Unit
) {
    var activeTab by remember { mutableStateOf(RecyclerTab.DASHBOARD) }

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
            RecyclerTab.values().forEach { tab ->
                Tab(
                    selected = activeTab == tab,
                    onClick = { activeTab = tab },
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
            RecyclerTab.DASHBOARD -> RecyclerDashboardContent(
                onNavigateToRequests = { activeTab = RecyclerTab.ASSIGNED_REQUESTS },
                onNavigateToAI = { activeTab = RecyclerTab.AI_CLASSIFICATION },
                onOpenPassport = onOpenPassport
            )
            RecyclerTab.ASSIGNED_REQUESTS -> RecyclerAssignedRequestsContent(onOpenPassport = onOpenPassport)
            RecyclerTab.WORK_ORDERS -> RecyclerWorkOrdersContent()
            RecyclerTab.WEIGHBRIDGE -> RecyclerWeighbridgeContent()
            RecyclerTab.AI_CLASSIFICATION -> RecyclerAiClassificationContent()
            RecyclerTab.RECYCLING_PROCESS -> RecyclerRecyclingProcessContent()
            RecyclerTab.INVENTORY -> RecyclerInventoryContent()
            RecyclerTab.ERUPI_PAYMENTS -> RecyclerERupiContent()
        }
    }
}

enum class RecyclerTab(val label: String) {
    DASHBOARD("Hub Overview"),
    ASSIGNED_REQUESTS("Assigned Manifests"),
    WORK_ORDERS("Work Orders"),
    WEIGHBRIDGE("IoT Weighbridge"),
    AI_CLASSIFICATION("AI Classification"),
    RECYCLING_PROCESS("Recycling Stages"),
    INVENTORY("Product Stock"),
    ERUPI_PAYMENTS("e-RUPI & Informal")
}

@Composable
private fun RecyclerDashboardContent(
    onNavigateToRequests: () -> Unit,
    onNavigateToAI: () -> Unit,
    onOpenPassport: (String) -> Unit
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Card(
            shape = RoundedCornerShape(6.dp),
            colors = CardDefaults.cardColors(containerColor = SurfaceContainerLowest),
            border = androidx.compose.foundation.BorderStroke(1.dp, OutlineVariant),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Column {
                        Text("Hindalco Beneficiation Hub #3", fontSize = 15.sp, fontWeight = FontWeight.Bold)
                        Text("Dahej Industrial Estate • STQC License: REC-STQC-4410", fontSize = 11.sp, color = OnSurfaceVariant)
                    }
                    StatusBadge("OPERATIONAL")
                }
                HorizontalDivider(color = SurfaceContainerHigh)
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Plant Processing Load: 78%", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Primary)
                    Text("Ceiling: 85% (Safe)", fontSize = 11.sp, color = OnSurfaceVariant)
                }
                LinearProgressIndicator(
                    progress = { 0.78f },
                    color = Primary,
                    trackColor = SurfaceContainerHigh,
                    modifier = Modifier.fillMaxWidth().height(6.dp)
                )
            }
        }

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            KpiCard(
                title = "Assigned Batches",
                value = "14 Lots",
                subStatus = "420 MT Cu Slag",
                subValue = "In Transit",
                icon = Icons.Default.LocalShipping,
                modifier = Modifier.weight(1f)
            )
            KpiCard(
                title = "Finished Goods",
                value = "₹84.2 Lakh",
                subStatus = "Cathode & Pellets",
                subValue = "Ready for Sale",
                icon = Icons.Default.Inventory,
                iconTint = MintSuccess,
                modifier = Modifier.weight(1f)
            )
        }

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(
                onClick = onNavigateToRequests,
                shape = RoundedCornerShape(4.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Primary),
                modifier = Modifier.weight(1f)
            ) {
                Text("Inspect Allocations", fontSize = 11.sp, fontWeight = FontWeight.Bold)
            }
            Button(
                onClick = onNavigateToAI,
                shape = RoundedCornerShape(4.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Secondary),
                modifier = Modifier.weight(1f)
            ) {
                Text("AI Mineral Assay", fontSize = 11.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
private fun RecyclerAssignedRequestsContent(onOpenPassport: (String) -> Unit) {
    val requests by MineCycleRepository.wasteRequests.collectAsState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            Text("Allocated Waste Consignments", fontSize = 14.sp, fontWeight = FontWeight.Bold)
            Text("Accept incoming consignments or record weighbridge entry", fontSize = 11.sp, color = OnSurfaceVariant)
            Spacer(Modifier.height(4.dp))
        }
        items(requests) { req ->
            Card(
                shape = RoundedCornerShape(6.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceContainerLowest),
                border = androidx.compose.foundation.BorderStroke(1.dp, OutlineVariant),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(10.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text(req.id, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Primary, fontFamily = FontFamily.Monospace)
                        StatusBadge(req.status.label)
                    }
                    Text("${req.wasteType} (${req.approximateQuantityTons} MT)", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    Text("Provider: ${req.providerName} • ${req.location}", fontSize = 11.sp, color = OnSurfaceVariant)
                    Text("Pickup: ${req.collectionDate} at ${req.collectionTime} • Distance: 34 km", fontSize = 10.sp)

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        if (req.status == WasteLifecycleStatus.REQUESTED || req.status == WasteLifecycleStatus.RECYCLER_ASSIGNED) {
                            Button(
                                onClick = { MineCycleRepository.acceptWasteRequest(req.id, "Hindalco Beneficiation Hub #3") },
                                shape = RoundedCornerShape(4.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Primary),
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("Accept & Create WO", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                            }
                        } else {
                            Button(
                                onClick = { MineCycleRepository.advanceWasteLifecycle(req.id) },
                                shape = RoundedCornerShape(4.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Secondary),
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("Start Processing Stage", fontSize = 10.sp)
                            }
                        }

                        IconButton(onClick = { onOpenPassport(req.id) }, modifier = Modifier.size(36.dp)) {
                            Icon(Icons.Default.QrCode2, contentDescription = "Passport", tint = Primary)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun RecyclerWorkOrdersContent() {
    val workOrders by MineCycleRepository.workOrders.collectAsState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            Text("Active Beneficiation Work Orders", fontSize = 14.sp, fontWeight = FontWeight.Bold)
            Text("Calibrated weighing and quality certification tracking", fontSize = 11.sp, color = OnSurfaceVariant)
            Spacer(Modifier.height(4.dp))
        }
        items(workOrders) { wo ->
            Card(
                shape = RoundedCornerShape(6.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceContainerLowest),
                border = androidx.compose.foundation.BorderStroke(1.dp, OutlineVariant),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(10.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text(wo.id, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Primary, fontFamily = FontFamily.Monospace)
                        StatusBadge(wo.status)
                    }
                    Text("Waste Manifest: ${wo.wasteId} • Material: ${wo.materialType}", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                    Text("Est Weight: ${wo.estimatedWeight} MT | Actual Verified: ${wo.actualWeight} MT", fontSize = 11.sp)
                    Text("Quality: ${wo.quality}", fontSize = 10.sp, color = OnSurfaceVariant)
                }
            }
        }
    }
}

@Composable
private fun RecyclerWeighbridgeContent() {
    var grossInput by remember { mutableStateOf("62.40") }
    var tareInput by remember { mutableStateOf("20.40") }
    var netResult by remember { mutableStateOf(42.00) }
    var sensorSynced by remember { mutableStateOf(true) }

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
            Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("IoT Load-Cell Weighbridge Calculator", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                        Text("Sensor Node: DAHEJ-WB-04 • Calibrated ±0.05 MT", fontSize = 10.sp, color = OnSurfaceVariant)
                    }
                    StatusBadge("SENSOR LIVE")
                }

                HorizontalDivider(color = SurfaceContainerHigh)

                OutlinedTextField(
                    value = grossInput,
                    onValueChange = {
                        grossInput = it
                        val g = it.toDoubleOrNull() ?: 0.0
                        val t = tareInput.toDoubleOrNull() ?: 0.0
                        netResult = (g - t).coerceAtLeast(0.0)
                    },
                    label = { Text("Gross Weight (MT)", fontSize = 11.sp) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = tareInput,
                    onValueChange = {
                        tareInput = it
                        val g = grossInput.toDoubleOrNull() ?: 0.0
                        val t = it.toDoubleOrNull() ?: 0.0
                        netResult = (g - t).coerceAtLeast(0.0)
                    },
                    label = { Text("Tare Weight of Vehicle (MT)", fontSize = 11.sp) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )

                Surface(
                    color = PrimaryContainer,
                    shape = RoundedCornerShape(4.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text("NET MATERIAL PAYLOAD", fontSize = 10.sp, color = PrimaryFixed, fontWeight = FontWeight.Bold)
                        Text(
                            text = String.format("%.2f MT", netResult),
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = OnPrimary
                        )
                    }
                }

                Button(
                    onClick = { sensorSynced = true },
                    shape = RoundedCornerShape(4.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Primary),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(Icons.Default.CheckCircle, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(Modifier.width(6.dp))
                    Text("Validate & Broadcast to Gov Escrow", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
private fun RecyclerAiClassificationContent() {
    val scope = rememberCoroutineScope()
    val classifications by MineCycleRepository.aiClassifications.collectAsState()

    var sampleCategory by remember { mutableStateOf("Copper Slag & Flotation Tailings") }
    var sampleNotes by remember { mutableStateOf("Malanjkhand Concentrator tailings slurry, 1.25% Cu assay, grain size 75 microns") }
    var isAnalyzing by remember { mutableStateOf(false) }
    var lastResult by remember { mutableStateOf<AIClassificationResult?>(null) }

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
                    Icon(Icons.Default.Psychology, contentDescription = null, tint = Primary)
                    Column {
                        Text("AI Waste Classification & Spectral Assay", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                        Text("Powered by SpectroNet Gemini / CSIR-NML Mineral Library", fontSize = 10.sp, color = OnSurfaceVariant)
                    }
                }

                HorizontalDivider(color = SurfaceContainerHigh)

                Text("Sample Type / Stream", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    listOf("Copper Slag", "Bauxite Red Mud", "Iron Slimes", "Zinc Jarosite", "Fly Ash").forEach { type ->
                        FilterChip(
                            selected = sampleCategory.contains(type),
                            onClick = { sampleCategory = type },
                            label = { Text(type, fontSize = 10.sp) }
                        )
                    }
                }

                OutlinedTextField(
                    value = sampleNotes,
                    onValueChange = { sampleNotes = it },
                    label = { Text("Sample Observations / XRD Band Info", fontSize = 11.sp) },
                    maxLines = 2,
                    modifier = Modifier.fillMaxWidth(),
                    textStyle = androidx.compose.ui.text.TextStyle(fontSize = 12.sp)
                )

                Button(
                    onClick = {
                        isAnalyzing = true
                        scope.launch {
                            val res = AIClassificationService.classifyWasteSample(sampleNotes, sampleCategory)
                            MineCycleRepository.addAIClassification(res)
                            lastResult = res
                            isAnalyzing = false
                        }
                    },
                    shape = RoundedCornerShape(4.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Primary),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    if (isAnalyzing) {
                        CircularProgressIndicator(color = OnPrimary, modifier = Modifier.size(16.dp), strokeWidth = 2.dp)
                        Spacer(Modifier.width(6.dp))
                        Text("Computing Spectral Assay...", fontSize = 11.sp)
                    } else {
                        Icon(Icons.Default.Biotech, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(Modifier.width(6.dp))
                        Text("Run AI Spectral Assay", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // Active/Latest Result Card
        val activeRes = lastResult ?: classifications.firstOrNull()
        if (activeRes != null) {
            Card(
                shape = RoundedCornerShape(6.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceContainerLowest),
                border = androidx.compose.foundation.BorderStroke(1.dp, Primary),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Assay Prediction: ${activeRes.id}", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Primary, fontFamily = FontFamily.Monospace)
                        StatusBadge("${activeRes.confidenceScore}% CONFIDENCE")
                    }
                    Text("Identified Material:", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    Text(activeRes.material, fontSize = 11.sp, color = OnSurfaceVariant)

                    Text("Recommended Recovery Method:", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    Text(activeRes.recommendedMethod, fontSize = 11.sp, color = Secondary)

                    Text("Recoverable Mineral Fractions (per 100 MT):", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    activeRes.recoverableMaterials.forEach { mat ->
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("• ${mat.name}", fontSize = 10.sp)
                            Text("${mat.percentage}% (${mat.estimatedTons} MT)", fontSize = 10.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
                        }
                    }

                    if (!activeRes.isVerified) {
                        Button(
                            onClick = { MineCycleRepository.verifyAIClassification(activeRes.id, "Dr. S. K. Verma (MOM)") },
                            shape = RoundedCornerShape(4.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Primary),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Verify Assay & Sign with DSC", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    } else {
                        Surface(color = MintSuccessBg, shape = RoundedCornerShape(4.dp), modifier = Modifier.fillMaxWidth()) {
                            Text(
                                text = "Verified by: ${activeRes.verifiedBy}",
                                color = MintSuccess,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(6.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun RecyclerRecyclingProcessContent() {
    val processes by MineCycleRepository.recyclingProcesses.collectAsState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            Text("Industrial Recovery Runs (7 Stages)", fontSize = 14.sp, fontWeight = FontWeight.Bold)
            Text("Sorting → Separation → Processing → Material Recovery → Manufacturing → QC → Ready", fontSize = 10.sp, color = OnSurfaceVariant)
            Spacer(Modifier.height(4.dp))
        }
        items(processes) { p ->
            Card(
                shape = RoundedCornerShape(6.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceContainerLowest),
                border = androidx.compose.foundation.BorderStroke(1.dp, OutlineVariant),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(10.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text(p.runId, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Primary, fontFamily = FontFamily.Monospace)
                        StatusBadge(p.stage)
                    }
                    Text("Input: ${p.inputWeight} MT | Recovered: ${p.recoveredQuantity} MT (${p.recoveredMaterial})", fontSize = 11.sp, fontWeight = FontWeight.Medium)
                    Text("Waste Slag Loss: ${p.wasteLoss} MT | Circularity Yield: ${p.recyclingRate}%", fontSize = 10.sp, color = OnSurfaceVariant)
                    Text("Output Commercial Product: ${p.outputProduct}", fontSize = 10.sp, color = Secondary, fontWeight = FontWeight.SemiBold)
                }
            }
        }
    }
}

@Composable
private fun RecyclerInventoryContent() {
    val products by MineCycleRepository.recycledProducts.collectAsState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            Text("Finished Recycled Products Inventory", fontSize = 14.sp, fontWeight = FontWeight.Bold)
            Text("Ready for procurement by industrial off-takers and manufacturers", fontSize = 11.sp, color = OnSurfaceVariant)
            Spacer(Modifier.height(4.dp))
        }
        items(products) { prd ->
            Card(
                shape = RoundedCornerShape(6.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceContainerLowest),
                border = androidx.compose.foundation.BorderStroke(1.dp, OutlineVariant),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(10.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text(prd.productName, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        StatusBadge("STQC GRADE A")
                    }
                    Text("Source: ${prd.sourceWaste} • Grade: ${prd.qualityGrade}", fontSize = 10.sp, color = OnSurfaceVariant)
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Available: ${prd.availableStock.toInt()} ${prd.unit}", fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                        Text("₹${prd.price} / ${prd.unit}", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Primary)
                    }
                }
            }
        }
    }
}

@Composable
private fun RecyclerERupiContent() {
    var beneficiaryName by remember { mutableStateOf("Mohd. Rafiq (Cluster #12)") }
    var voucherAmount by remember { mutableStateOf("38400") }
    var successMsg by remember { mutableStateOf<String?>(null) }

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
            Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Icon(Icons.Default.CardGiftcard, contentDescription = null, tint = Tertiary)
                    Column {
                        Text("e-RUPI Green Incentive Disbursal", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                        Text("Instant DBT vouchers for informal kabadiwalas & micro-collectors", fontSize = 10.sp, color = OnSurfaceVariant)
                    }
                }

                HorizontalDivider(color = SurfaceContainerHigh)

                OutlinedTextField(
                    value = beneficiaryName,
                    onValueChange = { beneficiaryName = it },
                    label = { Text("Kabadiwala / Cluster Name", fontSize = 11.sp) },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = voucherAmount,
                    onValueChange = { voucherAmount = it },
                    label = { Text("Voucher Amount in INR (₹)", fontSize = 11.sp) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )

                if (successMsg != null) {
                    Surface(color = MintSuccessBg, shape = RoundedCornerShape(4.dp), modifier = Modifier.fillMaxWidth()) {
                        Text(successMsg!!, color = MintSuccess, fontSize = 11.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(8.dp))
                    }
                }

                Button(
                    onClick = {
                        val amt = voucherAmount.toDoubleOrNull() ?: 0.0
                        if (amt > 0) {
                            MineCycleRepository.disburseERupiVoucher(beneficiaryName, amt)
                            successMsg = "e-RUPI SMS token of ₹$amt dispatched to $beneficiaryName. NPCI Ref #TXN-ERUPI-${(1000..9999).random()}."
                        }
                    },
                    shape = RoundedCornerShape(4.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Primary),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(Icons.AutoMirrored.Filled.Send, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(Modifier.width(6.dp))
                    Text("Issue Instant e-RUPI Voucher", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
