package com.example.ui.manufacturer

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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ManufacturerOrder
import com.example.data.model.OrderLifecycleStatus
import com.example.data.model.RecycledProduct
import com.example.data.repository.MineCycleRepository
import com.example.ui.components.*
import com.example.ui.theme.*

@Composable
fun ManufacturerScreen(
    onOpenPassport: (String) -> Unit
) {
    var activeTab by remember { mutableStateOf(ManufacturerTab.DASHBOARD) }
    var selectedProductForOrder by remember { mutableStateOf<RecycledProduct?>(null) }
    var selectedOrderForTracking by remember { mutableStateOf<ManufacturerOrder?>(null) }

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
            ManufacturerTab.values().forEach { tab ->
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
            ManufacturerTab.DASHBOARD -> ManufacturerDashboardContent(
                onBrowseProducts = { activeTab = ManufacturerTab.FIND_PRODUCTS },
                onViewOrder = { order ->
                    selectedOrderForTracking = order
                    activeTab = ManufacturerTab.ORDER_STATUS
                },
                onOpenPassport = onOpenPassport
            )
            ManufacturerTab.FIND_PRODUCTS -> ManufacturerFindProductsContent(
                onPlaceOrder = { prd ->
                    selectedProductForOrder = prd
                    activeTab = ManufacturerTab.ORDER_REQUEST
                }
            )
            ManufacturerTab.NEARBY_RECYCLERS -> ManufacturerNearbyRecyclersContent(
                onSelectRecyclerProduct = { prd ->
                    selectedProductForOrder = prd
                    activeTab = ManufacturerTab.ORDER_REQUEST
                }
            )
            ManufacturerTab.ORDER_REQUEST -> ManufacturerOrderRequestContent(
                product = selectedProductForOrder,
                onOrderCreated = { order ->
                    selectedOrderForTracking = order
                    activeTab = ManufacturerTab.ORDER_STATUS
                }
            )
            ManufacturerTab.ORDER_STATUS -> ManufacturerOrderStatusContent(
                order = selectedOrderForTracking,
                onOpenPassport = onOpenPassport
            )
            ManufacturerTab.PURCHASE_HISTORY -> ManufacturerPurchaseHistoryContent()
        }
    }
}

enum class ManufacturerTab(val label: String) {
    DASHBOARD("Marketplace"),
    FIND_PRODUCTS("Products Catalog"),
    NEARBY_RECYCLERS("Nearby Recyclers"),
    ORDER_REQUEST("Create Purchase Order"),
    ORDER_STATUS("Delivery & ETA"),
    PURCHASE_HISTORY("Purchase History")
}

@Composable
private fun ManufacturerDashboardContent(
    onBrowseProducts: () -> Unit,
    onViewOrder: (ManufacturerOrder) -> Unit,
    onOpenPassport: (String) -> Unit
) {
    val orders by MineCycleRepository.manufacturerOrders.collectAsState()
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
                        Text("Bharat Heavy Electricals Ltd (BHEL)", fontSize = 15.sp, fontWeight = FontWeight.Bold)
                        Text("Haridwar Works • GSTIN: 05AAACB1102A1Z3", fontSize = 11.sp, color = OnSurfaceVariant)
                    }
                    StatusBadge("OFF-TAKER VERIFIED")
                }
                HorizontalDivider(color = SurfaceContainerHigh)
                Button(
                    onClick = onBrowseProducts,
                    shape = RoundedCornerShape(4.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Primary),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(Icons.Default.Storefront, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(Modifier.width(6.dp))
                    Text("Browse Certified Secondary Minerals Marketplace", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
        }

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            KpiCard(
                title = "Total Procurement",
                value = "₹1.48 Cr",
                subStatus = "Escrow Secured",
                subValue = "2 Active POs",
                icon = Icons.Default.ShoppingCart,
                modifier = Modifier.weight(1f)
            )
            KpiCard(
                title = "Carbon Offset",
                value = "36.4 tCO2e",
                subStatus = "Scope 3 Credit",
                subValue = "IGBC Platinum",
                icon = Icons.Default.Eco,
                iconTint = MintSuccess,
                modifier = Modifier.weight(1f)
            )
        }

        Text("Active Purchase Orders", fontSize = 13.sp, fontWeight = FontWeight.Bold)

        orders.forEach { order ->
            Card(
                shape = RoundedCornerShape(6.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceContainerLowest),
                border = androidx.compose.foundation.BorderStroke(1.dp, OutlineVariant),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onViewOrder(order) }
            ) {
                Column(modifier = Modifier.padding(10.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text(order.id, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Primary, fontFamily = FontFamily.Monospace)
                        StatusBadge(order.status.label)
                    }
                    Text(order.productName, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    Text("Supplier: ${order.recyclerName} • Qty: ${order.quantity} MT", fontSize = 11.sp, color = OnSurfaceVariant)
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("ETA: ${order.eta ?: "2 days"}", fontSize = 10.sp, color = Secondary, fontWeight = FontWeight.Bold)
                        TextButton(onClick = { onViewOrder(order) }) {
                            Text("Track GPS →", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ManufacturerFindProductsContent(
    onPlaceOrder: (RecycledProduct) -> Unit
) {
    val products by MineCycleRepository.recycledProducts.collectAsState()
    var searchKeyword by remember { mutableStateOf("") }
    var selectedFilter by remember { mutableStateOf("All") }

    val filteredProducts = products.filter {
        (selectedFilter == "All" || it.productName.contains(selectedFilter, true) || it.sourceWaste.contains(selectedFilter, true)) &&
        (it.productName.contains(searchKeyword, true) || it.recyclerCompany.contains(searchKeyword, true))
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        OutlinedTextField(
            value = searchKeyword,
            onValueChange = { searchKeyword = it },
            placeholder = { Text("Search copper, iron pellets, alumina, bricks...", fontSize = 11.sp) },
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
            listOf("All", "Copper", "Iron", "Alumina", "Bricks", "Slag").forEach { f ->
                FilterChip(
                    selected = selectedFilter == f,
                    onClick = { selectedFilter = f },
                    label = { Text(f, fontSize = 10.sp) }
                )
            }
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(filteredProducts) { prd ->
                Card(
                    shape = RoundedCornerShape(6.dp),
                    colors = CardDefaults.cardColors(containerColor = SurfaceContainerLowest),
                    border = androidx.compose.foundation.BorderStroke(1.dp, OutlineVariant),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(10.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text(prd.productName, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                            StatusBadge("STQC VERIFIED")
                        }
                        Text("Quality: ${prd.qualityGrade} • Source: ${prd.sourceWaste}", fontSize = 10.sp, color = OnSurfaceVariant)
                        Text("Recycler: ${prd.recyclerCompany} (${prd.location})", fontSize = 10.sp)

                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                            Column {
                                Text("Price: ₹${prd.price} / ${prd.unit}", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Primary)
                                Text("Available: ${prd.availableStock.toInt()} ${prd.unit}", fontSize = 10.sp, color = OnSurfaceVariant)
                            }
                            Button(
                                onClick = { onPlaceOrder(prd) },
                                shape = RoundedCornerShape(4.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Primary),
                                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                                modifier = Modifier.height(32.dp)
                            ) {
                                Text("Raise PO", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ManufacturerNearbyRecyclersContent(
    onSelectRecyclerProduct: (RecycledProduct) -> Unit
) {
    val products by MineCycleRepository.recycledProducts.collectAsState()

    val recyclers = listOf(
        Triple("Hindalco Beneficiation Hub #3", "34 km away • Dahej, Gujarat", products.firstOrNull { it.id == "PRD-CU-99" }),
        Triple("Eco-Cementation Unit 1", "48 km away • Dahej Cluster", products.firstOrNull { it.id == "PRD-BRK-GEO" }),
        Triple("Jindal Pellet Matrix", "120 km away • Raigarh, CG", products.firstOrNull { it.id == "PRD-FE-64" }),
        Triple("Vedanta Circular Beneficiation", "180 km away • Koraput, OD", products.firstOrNull { it.id == "PRD-AL-GEO" })
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            Text("Certified Recyclers Proximity Directory", fontSize = 14.sp, fontWeight = FontWeight.Bold)
            Text("Ranked by GPS distance and available dispatch inventory", fontSize = 11.sp, color = OnSurfaceVariant)
            Spacer(Modifier.height(4.dp))
        }
        items(recyclers) { (name, loc, prd) ->
            Card(
                shape = RoundedCornerShape(6.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceContainerLowest),
                border = androidx.compose.foundation.BorderStroke(1.dp, OutlineVariant),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(10.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text(name, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        StatusBadge("RATING 4.9 ★")
                    }
                    Text(loc, fontSize = 10.sp, color = OnSurfaceVariant)
                    if (prd != null) {
                        Text("Flagship Product: ${prd.productName} (₹${prd.price}/${prd.unit})", fontSize = 11.sp, color = Primary, fontWeight = FontWeight.SemiBold)
                        Button(
                            onClick = { onSelectRecyclerProduct(prd) },
                            shape = RoundedCornerShape(4.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Secondary),
                            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                            modifier = Modifier.height(28.dp)
                        ) {
                            Text("Request Consignment", fontSize = 10.sp)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ManufacturerOrderRequestContent(
    product: RecycledProduct?,
    onOrderCreated: (ManufacturerOrder) -> Unit
) {
    val products by MineCycleRepository.recycledProducts.collectAsState()
    val activePrd = product ?: products.first()

    var quantityText by remember { mutableStateOf("20") }
    var deliveryDate by remember { mutableStateOf("2025-05-28") }
    var destination by remember { mutableStateOf("BHEL Haridwar Works, Ranipur, Haridwar, Uttarakhand") }
    var requirements by remember { mutableStateOf("STQC mineral provenance passport QR and mill test certificate required.") }
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
                Text("Generate Statutory Purchase Order (PO)", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                Text("Automated Escrow hold under National Mineral Registry guidelines", fontSize = 10.sp, color = OnSurfaceVariant)

                HorizontalDivider(color = SurfaceContainerHigh)

                Surface(
                    color = SurfaceContainerLow,
                    shape = RoundedCornerShape(4.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(8.dp), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                        Text("Product: ${activePrd.productName}", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        Text("Supplier: ${activePrd.recyclerCompany}", fontSize = 11.sp)
                        Text("Rate: ₹${activePrd.price} / ${activePrd.unit}", fontSize = 11.sp, color = Primary, fontWeight = FontWeight.Bold)
                    }
                }

                OutlinedTextField(
                    value = quantityText,
                    onValueChange = { quantityText = it },
                    label = { Text("Quantity in ${activePrd.unit}", fontSize = 11.sp) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = deliveryDate,
                    onValueChange = { deliveryDate = it },
                    label = { Text("Required Delivery Date", fontSize = 11.sp) },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = destination,
                    onValueChange = { destination = it },
                    label = { Text("Factory Delivery Location / Pier Site", fontSize = 11.sp) },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = requirements,
                    onValueChange = { requirements = it },
                    label = { Text("Special Technical / Quality Requirements", fontSize = 11.sp) },
                    maxLines = 2,
                    modifier = Modifier.fillMaxWidth()
                )

                val qty = quantityText.toDoubleOrNull() ?: 0.0
                val total = qty * activePrd.price

                Surface(
                    color = PrimaryContainer,
                    shape = RoundedCornerShape(4.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("TOTAL ESCROW COMMITMENT", fontSize = 10.sp, color = PrimaryFixed, fontWeight = FontWeight.Bold)
                        Text("₹${total.toInt()}", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = OnPrimary)
                    }
                }

                Button(
                    onClick = {
                        val order = MineCycleRepository.placeManufacturerOrder(
                            product = activePrd,
                            quantity = qty,
                            deliveryDate = deliveryDate,
                            destination = destination,
                            requirements = requirements
                        )
                        onOrderCreated(order)
                    },
                    shape = RoundedCornerShape(4.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Primary),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(Icons.Default.Lock, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(Modifier.width(6.dp))
                    Text("Place PO & Secure Escrow Hold", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
private fun ManufacturerOrderStatusContent(
    order: ManufacturerOrder?,
    onOpenPassport: (String) -> Unit
) {
    val orders by MineCycleRepository.manufacturerOrders.collectAsState()
    val activeOrder = order ?: orders.firstOrNull()

    if (activeOrder == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("No active order selected.", color = OnSurfaceVariant)
        }
        return
    }

    val steps = listOf(
        "Requested",
        "Reviewing",
        "Accepted",
        "Payment Pending",
        "Payment Done",
        "Processing",
        "Packed",
        "Dispatched",
        "In Transit",
        "Delivered",
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
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(activeOrder.id, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Primary, fontFamily = FontFamily.Monospace)
                    StatusBadge(activeOrder.status.label)
                }
                Text(activeOrder.productName, fontSize = 13.sp, fontWeight = FontWeight.Bold)

                HorizontalDivider(color = SurfaceContainerHigh)

                Text("11-Stage Delivery Lifecycle Tracking", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Primary)

                VisualTimeline(
                    currentStepIndex = activeOrder.status.stepIndex,
                    steps = steps,
                    modifier = Modifier.fillMaxWidth()
                )

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Text("Simulate GPS Milestone:", fontSize = 11.sp, color = OnSurfaceVariant)
                    Button(
                        onClick = { MineCycleRepository.advanceOrderLifecycle(activeOrder.id) },
                        shape = RoundedCornerShape(4.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Primary),
                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                        modifier = Modifier.height(30.dp)
                    ) {
                        Text("Advance Delivery →", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // Fleet & GPS Telemetry Card
        Card(
            shape = RoundedCornerShape(6.dp),
            colors = CardDefaults.cardColors(containerColor = SurfaceContainerLowest),
            border = androidx.compose.foundation.BorderStroke(1.dp, OutlineVariant),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    Icon(Icons.Default.GpsFixed, contentDescription = null, tint = Secondary)
                    Text("Fleet Telemetry & Vehicle Tracking", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
                Text("Assigned Vehicle: ${activeOrder.vehicleId ?: "GJ-06-TT-4412"}", fontSize = 11.sp, fontFamily = FontFamily.Monospace)
                Text("Live ETA: ${activeOrder.eta ?: "2.4 hours remaining"}", fontSize = 11.sp, color = Secondary, fontWeight = FontWeight.Bold)
                Text("Destination: ${activeOrder.deliveryLocation}", fontSize = 11.sp, color = OnSurfaceVariant)

                Button(
                    onClick = { onOpenPassport(activeOrder.productId) },
                    shape = RoundedCornerShape(4.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Secondary),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(Icons.Default.QrCode2, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(Modifier.width(6.dp))
                    Text("View Digital Product Passport (DPP)", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
private fun ManufacturerPurchaseHistoryContent() {
    val orders by MineCycleRepository.manufacturerOrders.collectAsState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            Text("Procurement & Purchase History", fontSize = 14.sp, fontWeight = FontWeight.Bold)
            Text("Settled contracts and mill test certificate archives", fontSize = 11.sp, color = OnSurfaceVariant)
            Spacer(Modifier.height(4.dp))
        }
        items(orders) { order ->
            Card(
                shape = RoundedCornerShape(6.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceContainerLowest),
                border = androidx.compose.foundation.BorderStroke(1.dp, OutlineVariant),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(10.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text(order.id, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Primary, fontFamily = FontFamily.Monospace)
                        StatusBadge(order.status.label)
                    }
                    Text(order.productName, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    Text("Amount: ₹${order.totalAmount.toInt()} • Supplier: ${order.recyclerName}", fontSize = 11.sp, color = OnSurfaceVariant)
                }
            }
        }
    }
}
