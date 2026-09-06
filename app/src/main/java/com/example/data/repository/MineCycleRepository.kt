package com.example.data.repository

import com.example.data.model.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.text.SimpleDateFormat
import java.util.*

object MineCycleRepository {

    private val _currentUser = MutableStateFlow<UserProfile>(
        UserProfile(
            fullName = "Dr. S. K. Verma",
            username = "skverma.mines",
            phoneNumber = "+91 98110 44291",
            email = "admin.mines@gov.in",
            userId = "GOV-MOM-2024-001",
            accountNumber = "994018274019",
            companyName = "Ministry of Mines, Govt of India",
            location = "Shastri Bhawan, New Delhi",
            address = "Room 304, Ministry of Mines, New Delhi 110001",
            userType = UserRole.ADMIN,
            gstNumber = "07GOVM00001A1Z0",
            licenseCode = "STQC-SOVEREIGN-LVL3",
            isVerified = true
        )
    )
    val currentUser: StateFlow<UserProfile> = _currentUser.asStateFlow()

    private val _wasteRequests = MutableStateFlow<List<WasteRequest>>(
        listOf(
            WasteRequest(
                id = "WST-2025-9482",
                providerName = "Hindustan Copper Ltd",
                leaseCode = "ML-MP-0091",
                wasteType = "Copper Slag",
                approximateQuantityTons = 420.0,
                location = "Pit 4, Malanjkhand Complex, MP",
                collectionDate = "2025-05-18",
                collectionTime = "09:30 IST",
                description = "High density copper beneficiation tailings from primary flotation circuit with 1.2% residual copper.",
                status = WasteLifecycleStatus.COLLECTED,
                allocatedRecycler = "Hindalco Beneficiation Hub #3",
                aiConfidence = 99.4,
                predictedComposition = "Cu 1.28%, Fe 38.2%, SiO2 33.9%, Co 0.05%",
                truckRfid = "DL-01-AX-9921",
                driverName = "Mohan Singh",
                weighbridgeGross = 62.40,
                weighbridgeTare = 20.40,
                escrowAmount = 2450000.0,
                escrowStatus = EscrowStatus.SETTLED
            ),
            WasteRequest(
                id = "WST-2025-9478",
                providerName = "NMDC Kirandul",
                leaseCode = "ML-CG-0881",
                wasteType = "Iron Ore Slimes",
                approximateQuantityTons = 120.0,
                location = "Tailing Dam #2, Kirandul, CG",
                collectionDate = "2025-05-17",
                collectionTime = "14:00 IST",
                description = "Fine fraction hematite tailings slurry dewatered cake. Flagged by toll gate weighbridge sensor.",
                status = WasteLifecycleStatus.REQUESTED,
                allocatedRecycler = "Jindal Pellet Matrix 2",
                aiConfidence = 64.2,
                predictedComposition = "Fe 54.1%, SiO2 8.4%, Al2O3 5.2%",
                truckRfid = "CG-04-E-1200",
                driverName = "R. K. Rathore",
                weighbridgeGross = 1250.0,
                weighbridgeTare = 22.0,
                escrowAmount = 4200000.0,
                escrowStatus = EscrowStatus.FROZEN
            ),
            WasteRequest(
                id = "WST-2025-9475",
                providerName = "HZL Rampura Agucha",
                leaseCode = "ML-RJ-0319",
                wasteType = "Zinc Residue",
                approximateQuantityTons = 680.0,
                location = "Dump Site 1B, Bhilwara, RJ",
                collectionDate = "2025-05-19",
                collectionTime = "11:00 IST",
                description = "Neutralized jarosite precipitation cake containing zinc, lead and minor silver values.",
                status = WasteLifecycleStatus.ACCEPTED,
                allocatedRecycler = "Eco-Cementation Unit 1",
                aiConfidence = 99.1,
                predictedComposition = "Zn 4.8%, Pb 1.2%, Ag 45g/t, Fe 29%",
                truckRfid = "RJ-06-GB-8841",
                driverName = "Suresh Yadav",
                weighbridgeGross = 48.0,
                weighbridgeTare = 18.0,
                escrowAmount = 1850000.0,
                escrowStatus = EscrowStatus.SETTLED
            ),
            WasteRequest(
                id = "WST-2025-9471",
                providerName = "National Aluminium Co (NALCO)",
                leaseCode = "ML-OD-0412",
                wasteType = "Bauxite Residue (Red Mud)",
                approximateQuantityTons = 950.0,
                location = "Damanjodi Alumina Refinery, Odisha",
                collectionDate = "2025-05-20",
                collectionTime = "16:00 IST",
                description = "Alkaline red mud filtered cake for rare earth element (Scandium, Titanium) extraction and geopolymer brick manufacturing.",
                status = WasteLifecycleStatus.RECYCLED,
                allocatedRecycler = "Vedanta Circular Beneficiation",
                aiConfidence = 98.7,
                predictedComposition = "Fe2O3 48.2%, Al2O3 18.5%, TiO2 6.1%, Sc 95ppm",
                truckRfid = "OD-10-Q-4190",
                driverName = "Bikash Jena",
                weighbridgeGross = 55.0,
                weighbridgeTare = 20.0,
                escrowAmount = 3100000.0,
                escrowStatus = EscrowStatus.SETTLED
            )
        )
    )
    val wasteRequests: StateFlow<List<WasteRequest>> = _wasteRequests.asStateFlow()

    private val _workOrders = MutableStateFlow<List<WorkOrder>>(
        listOf(
            WorkOrder(
                id = "WO-2025-081",
                wasteId = "WST-2025-9482",
                recyclerCompany = "Hindalco Beneficiation Hub #3",
                estimatedWeight = 420.0,
                actualWeight = 418.5,
                materialType = "Copper Slag",
                quality = "Grade A (1.28% Cu)",
                status = "In Beneficiation",
                createdAt = "2025-05-18 10:15"
            ),
            WorkOrder(
                id = "WO-2025-079",
                wasteId = "WST-2025-9475",
                recyclerCompany = "Eco-Cementation Unit 1",
                estimatedWeight = 680.0,
                actualWeight = 676.0,
                materialType = "Zinc Jarosite Residue",
                quality = "Neutralized Industrial Grade",
                status = "Completed",
                createdAt = "2025-05-16 08:30"
            )
        )
    )
    val workOrders: StateFlow<List<WorkOrder>> = _workOrders.asStateFlow()

    private val _recycledProducts = MutableStateFlow<List<RecycledProduct>>(
        listOf(
            RecycledProduct(
                id = "PRD-CU-99",
                productName = "Recycled Copper Cathode Grade A",
                sourceWaste = "Copper Slag (Malanjkhand)",
                recyclerCompany = "Hindalco Beneficiation Hub #3",
                quantity = 140.0,
                qualityGrade = "99.95% Purity LME Registered",
                price = 720.0,
                unit = "kg",
                availableStock = 140.0,
                soldQuantity = 380.0,
                location = "Dahej Cluster, Gujarat",
                rating = 4.9
            ),
            RecycledProduct(
                id = "PRD-FE-64",
                productName = "Iron Micro-Pellets (64% Fe)",
                sourceWaste = "Iron Ore Slimes (Bailadila)",
                recyclerCompany = "Jindal Pellet Matrix",
                quantity = 1200.0,
                qualityGrade = "Direct Reduction Grade",
                price = 9200.0,
                unit = "MT",
                availableStock = 1200.0,
                soldQuantity = 4500.0,
                location = "Raigarh Industrial Estate, CG",
                rating = 4.8
            ),
            RecycledProduct(
                id = "PRD-AL-GEO",
                productName = "Precipitated Alumina Sand",
                sourceWaste = "Bauxite Residue (Red Mud)",
                recyclerCompany = "Vedanta Circular Beneficiation",
                quantity = 820.0,
                qualityGrade = "Refractory Grade 72%",
                price = 18500.0,
                unit = "MT",
                availableStock = 820.0,
                soldQuantity = 1900.0,
                location = "Koraput Plant, Odisha",
                rating = 4.7
            ),
            RecycledProduct(
                id = "PRD-BRK-GEO",
                productName = "Engineered Geo-Polymer Bricks",
                sourceWaste = "Fly Ash & Blast Furnace Slag",
                recyclerCompany = "Eco-Cementation Unit 1",
                quantity = 450000.0,
                qualityGrade = "IS 1077 Heavy Duty Class 25",
                price = 8.50,
                unit = "Pcs",
                availableStock = 450000.0,
                soldQuantity = 1200000.0,
                location = "Dahej, Gujarat",
                rating = 4.9
            ),
            RecycledProduct(
                id = "PRD-AGG-SLAG",
                productName = "Recycled High-Density Slag Aggregate",
                sourceWaste = "Steel & Copper Slag",
                recyclerCompany = "Tata Refractory & Extraction",
                quantity = 2500.0,
                qualityGrade = "Road Base & Bituminous Grade",
                price = 1400.0,
                unit = "MT",
                availableStock = 2500.0,
                soldQuantity = 8000.0,
                location = "Jamshedpur Hub, Jharkhand",
                rating = 4.6
            )
        )
    )
    val recycledProducts: StateFlow<List<RecycledProduct>> = _recycledProducts.asStateFlow()

    private val _manufacturerOrders = MutableStateFlow<List<ManufacturerOrder>>(
        listOf(
            ManufacturerOrder(
                id = "PO-MFR-2025-019",
                productId = "PRD-CU-99",
                productName = "Recycled Copper Cathode Grade A",
                manufacturerName = "Bharat Heavy Electricals Ltd (BHEL)",
                recyclerName = "Hindalco Beneficiation Hub #3",
                quantity = 20.0,
                quality = "99.95% Purity",
                deliveryDate = "2025-05-22",
                deliveryLocation = "BHEL Haridwar Works, Uttarakhand",
                requirements = "Mill test certificate and STQC mineral provenance QR required",
                totalAmount = 14400000.0,
                status = OrderLifecycleStatus.IN_TRANSIT,
                vehicleId = "GJ-06-TT-4412",
                eta = "2.4 hours",
                orderDate = "2025-05-17"
            ),
            ManufacturerOrder(
                id = "PO-MFR-2025-018",
                productId = "PRD-BRK-GEO",
                productName = "Engineered Geo-Polymer Bricks",
                manufacturerName = "Larsen & Toubro Green Infra",
                recyclerName = "Eco-Cementation Unit 1",
                quantity = 50000.0,
                quality = "IS 1077 Class 25",
                deliveryDate = "2025-05-25",
                deliveryLocation = "Bullet Train Project Pier Site 14, Surat",
                requirements = "Green rating certificate for IGBC Platinum compliance",
                totalAmount = 425000.0,
                status = OrderLifecycleStatus.ACCEPTED,
                vehicleId = "GJ-05-XY-8812",
                eta = "Scheduled 24 May",
                orderDate = "2025-05-18"
            )
        )
    )
    val manufacturerOrders: StateFlow<List<ManufacturerOrder>> = _manufacturerOrders.asStateFlow()

    private val _aiClassifications = MutableStateFlow<List<AIClassificationResult>>(
        listOf(
            AIClassificationResult(
                id = "AIC-2025-001",
                wasteType = "Copper Slag & Tailings",
                material = "Ferrous Silicate Matrix with Chalcopyrite and Bornite inclusions",
                confidenceScore = 99.4,
                recommendedMethod = "Froth Flotation followed by Hydrometallurgical Atmospheric Leaching",
                recoverableMaterials = listOf(
                    RecoverableMaterial("Refined Copper", 1.28, 5.37),
                    RecoverableMaterial("Iron Concentrate", 38.2, 160.4),
                    RecoverableMaterial("Cobalt / Nickel traces", 0.05, 0.21),
                    RecoverableMaterial("Silica Geopolymer Aggregate", 54.0, 226.8)
                ),
                verifiedBy = "Dr. S. K. Verma (MOM/IBM)",
                isVerified = true,
                status = "Verified by Admin",
                timestamp = "2025-05-18 10:30"
            ),
            AIClassificationResult(
                id = "AIC-2025-002",
                wasteType = "Bauxite Residue (Red Mud)",
                material = "Sodalite, Hematite, Anatase, Goethite & Boehmite",
                confidenceScore = 98.7,
                recommendedMethod = "Magnetic Separation followed by Oxalic Acid Rare Earth Leaching",
                recoverableMaterials = listOf(
                    RecoverableMaterial("Hematite Fe2O3", 48.2, 457.9),
                    RecoverableMaterial("Alumina Al2O3", 18.5, 175.7),
                    RecoverableMaterial("Titanium Dioxide TiO2", 6.1, 57.9),
                    RecoverableMaterial("Scandium REE", 0.0095, 0.09)
                ),
                verifiedBy = "Chief Metallurgist, Dahej Hub",
                isVerified = true,
                status = "Verified by Recycler",
                timestamp = "2025-05-17 14:20"
            )
        )
    )
    val aiClassifications: StateFlow<List<AIClassificationResult>> = _aiClassifications.asStateFlow()

    private val _recyclingProcesses = MutableStateFlow<List<RecyclingProcessRecord>>(
        listOf(
            RecyclingProcessRecord(
                runId = "RUN-2025-CU-82",
                inputWeight = 420.0,
                recoveredMaterial = "Electrolytic Copper Sponge (99.4% Cu)",
                recoveredQuantity = 5.2,
                wasteLoss = 8.4,
                recyclingRate = 98.0,
                outputProduct = "Recycled Copper Cathode Grade A",
                processingStatus = "Quality Check Passed",
                stage = "Ready for Sale"
            ),
            RecyclingProcessRecord(
                runId = "RUN-2025-FE-91",
                inputWeight = 680.0,
                recoveredMaterial = "Magnetite Pellet Concentrate (65% Fe)",
                recoveredQuantity = 410.0,
                wasteLoss = 42.0,
                recyclingRate = 93.8,
                outputProduct = "Iron Micro-Pellets",
                processingStatus = "Material Recovery in Progress",
                stage = "Material Recovery"
            )
        )
    )
    val recyclingProcesses: StateFlow<List<RecyclingProcessRecord>> = _recyclingProcesses.asStateFlow()

    private val _payments = MutableStateFlow<List<PaymentRecord>>(
        listOf(
            PaymentRecord(
                transactionId = "TXN-NPCI-9921",
                fromParty = "National Escrow Facility",
                toBeneficiary = "Hindalco Beneficiation Hub #3",
                amount = 2450000.0,
                date = "2025-05-18",
                status = PaymentStatus.COMPLETED,
                blockchainHash = "0x4a8f9b...129c",
                paymentType = "DBT Escrow Disbursal"
            ),
            PaymentRecord(
                transactionId = "TXN-NPCI-9918",
                fromParty = "National Escrow Facility",
                toBeneficiary = "Jindal Pellet Matrix 2",
                amount = 4200000.0,
                date = "2025-05-17",
                status = PaymentStatus.FAILED,
                blockchainHash = "0xee41a0...77fa",
                paymentType = "Frozen (Weighbridge Discrepancy)"
            ),
            PaymentRecord(
                transactionId = "TXN-ERUPI-3310",
                fromParty = "Hindalco Dahej CSR Escrow",
                toBeneficiary = "Mohd. Rafiq (Kabadiwala Cluster #12)",
                amount = 38400.0,
                date = "2025-05-18",
                status = PaymentStatus.CREDITED,
                blockchainHash = "0x992fa1...881b",
                paymentType = "e-RUPI Green Incentive Voucher"
            ),
            PaymentRecord(
                transactionId = "TXN-ERUPI-3308",
                fromParty = "Hindalco Dahej CSR Escrow",
                toBeneficiary = "Sunita Devi (Kabadiwala Cluster #04)",
                amount = 21600.0,
                date = "2025-05-17",
                status = PaymentStatus.CREDITED,
                blockchainHash = "0x12bb90...ff42",
                paymentType = "e-RUPI Green Incentive Voucher"
            ),
            PaymentRecord(
                transactionId = "TXN-MFR-7712",
                fromParty = "BHEL Haridwar Works",
                toBeneficiary = "National Escrow Facility",
                amount = 14400000.0,
                date = "2025-05-17",
                status = PaymentStatus.PROCESSING,
                blockchainHash = "0xbb4412...3319",
                paymentType = "Manufacturer PO Escrow Hold"
            )
        )
    )
    val payments: StateFlow<List<PaymentRecord>> = _payments.asStateFlow()

    private val _dailyDataSheet = MutableStateFlow<List<DailyDataRecord>>(
        listOf(
            DailyDataRecord("2025-05-18", "ML-MP-0091", "Hindustan Copper Ltd", "Copper Slag", 1420.0, 1420.0, 980.0, "Refined Copper Sponge", 2450000.0, 128.4, "AUDITED"),
            DailyDataRecord("2025-05-18", "ML-OD-0412", "NALCO Damanjodi", "Red Mud", 3200.0, 3200.0, 2150.0, "Alumina Sand & Bricks", 3800000.0, 340.0, "AUDITED"),
            DailyDataRecord("2025-05-18", "ML-RJ-0319", "HZL Rampura Agucha", "Zinc Jarosite", 680.0, 680.0, 640.0, "Zinc Concentrates", 1850000.0, 92.5, "AUDITED"),
            DailyDataRecord("2025-05-17", "ML-CG-0881", "NMDC Kirandul", "Iron Slimes", 1250.0, 0.0, 0.0, "None", 0.0, 0.0, "FLAGGED"),
            DailyDataRecord("2025-05-17", "ML-JH-1102", "Singhbhum Consortium", "Iron Slag", 890.0, 890.0, 810.0, "Micro-Pellets", 1450000.0, 110.2, "AUDITED"),
            DailyDataRecord("2025-05-16", "ML-TN-0012", "NLC India Neyveli", "Thermal Fly Ash", 5400.0, 5400.0, 5200.0, "Geo-Polymer Bricks", 2800000.0, 610.8, "AUDITED")
        )
    )
    val dailyDataSheet: StateFlow<List<DailyDataRecord>> = _dailyDataSheet.asStateFlow()

    private val _notifications = MutableStateFlow<List<NotificationItem>>(
        listOf(
            NotificationItem("N1", UserRole.ADMIN, "SpectroNet Toll Anomaly Alert", "Consignment #WST-8819 weight spiked from 120 MT to 1,250 MT at NH Toll Gate 4. Auto-frozen.", "5m ago", false, "ALERT"),
            NotificationItem("N2", UserRole.ADMIN, "New Enterprise Registration", "Singhbhum Tailings Consortium submitted Form 4 KYC for STQC verification.", "22m ago", false, "NORMAL"),
            NotificationItem("N3", UserRole.WASTE_PROVIDER, "Recycler Assigned to #WST-9482", "Hindalco Beneficiation Hub #3 assigned. Truck RFID DL-01-AX-9921 en route.", "1h ago", false, "NORMAL"),
            NotificationItem("N4", UserRole.WASTE_PROVIDER, "DBT Escrow Released", "₹24,50,000 credited to Bank of Baroda A/c ending 4019.", "3h ago", true, "SUCCESS"),
            NotificationItem("N5", UserRole.RECYCLER, "New Consignment Allocation", "Consignment #WST-9482 (Copper Slag 420 MT) allocated. Please accept or reject.", "2h ago", false, "NORMAL"),
            NotificationItem("N6", UserRole.RECYCLER, "New Purchase Order Received", "BHEL Haridwar sent PO #PO-MFR-2025-019 for 20 MT Recycled Copper Cathode.", "4h ago", false, "NORMAL"),
            NotificationItem("N7", UserRole.MANUFACTURER, "Order Dispatched", "Consignment for PO #PO-MFR-2025-019 dispatched on truck GJ-06-TT-4412. ETA 2.4 hrs.", "30m ago", false, "NORMAL")
        )
    )
    val notifications: StateFlow<List<NotificationItem>> = _notifications.asStateFlow()

    private val _environmentalMetrics = MutableStateFlow(
        EnvironmentalMetrics(
            totalWasteRecycledMT = 4820000.0,
            materialRecoveredMT = 142600.0,
            co2AvoidedTons = 840000.0,
            energySavedMWh = 1250000.0,
            rawMaterialConservedMT = 3800000.0
        )
    )
    val environmentalMetrics: StateFlow<EnvironmentalMetrics> = _environmentalMetrics.asStateFlow()

    // --- Actions ---

    fun switchRole(role: UserRole) {
        val user = when (role) {
            UserRole.ADMIN -> UserProfile(
                fullName = "Dr. S. K. Verma",
                username = "skverma.mines",
                phoneNumber = "+91 98110 44291",
                email = "admin.mines@gov.in",
                userId = "GOV-MOM-2024-001",
                accountNumber = "994018274019",
                companyName = "Ministry of Mines, Govt of India",
                location = "Shastri Bhawan, New Delhi",
                address = "Room 304, Ministry of Mines, New Delhi 110001",
                userType = UserRole.ADMIN,
                gstNumber = "07GOVM00001A1Z0",
                licenseCode = "STQC-SOVEREIGN-LVL3",
                isVerified = true
            )
            UserRole.WASTE_PROVIDER -> UserProfile(
                fullName = "Rajeev Singhania",
                username = "rajeev.hcl",
                phoneNumber = "+91 94250 88201",
                email = "mine.manager@hindustancopper.com",
                userId = "MINE-HCL-0091",
                accountNumber = "441098271109",
                companyName = "Hindustan Copper Limited",
                location = "Malanjkhand Copper Project, MP",
                address = "MCP Administrative Building, Malanjkhand, Balaghat MP 481116",
                userType = UserRole.WASTE_PROVIDER,
                gstNumber = "23AAACH0091M1ZF",
                licenseCode = "ML-MP-0091",
                isVerified = true
            )
            UserRole.RECYCLER -> UserProfile(
                fullName = "Ananya Chatterjee",
                username = "ananya.hindalco",
                phoneNumber = "+91 98201 55902",
                email = "beneficiation@hindalco.adityabirla.com",
                userId = "REC-STQC-4410",
                accountNumber = "772091823341",
                companyName = "Hindalco Beneficiation Hub #3",
                location = "Dahej Industrial Estate, Gujarat",
                address = "Plot 14-B, GIDC Dahej, Bharuch, Gujarat 392130",
                userType = UserRole.RECYCLER,
                gstNumber = "24AAACH7712M1Z8",
                licenseCode = "REC-STQC-4410",
                isVerified = true
            )
            UserRole.MANUFACTURER -> UserProfile(
                fullName = "Vikramaditya Rao",
                username = "vrao.bhel",
                phoneNumber = "+91 98102 77194",
                email = "procurement@bhel.in",
                userId = "MFR-BHEL-019",
                accountNumber = "551029384756",
                companyName = "Bharat Heavy Electricals Ltd (BHEL)",
                location = "Heavy Electrical Equipment Plant, Haridwar",
                address = "Ranipur, Haridwar, Uttarakhand 249403",
                userType = UserRole.MANUFACTURER,
                gstNumber = "05AAACB1102A1Z3",
                licenseCode = "MFR-PSU-0012",
                isVerified = true
            )
        }
        _currentUser.value = user
    }

    fun login(role: UserRole, email: String) {
        switchRole(role)
    }

    fun signUp(
        fullName: String,
        username: String,
        phone: String,
        email: String,
        userId: String,
        accountNumber: String,
        companyName: String,
        location: String,
        address: String,
        role: UserRole
    ) {
        _currentUser.value = UserProfile(
            fullName = fullName,
            username = username,
            phoneNumber = phone,
            email = email,
            userId = userId,
            accountNumber = accountNumber,
            companyName = companyName,
            location = location,
            address = address,
            userType = role,
            isVerified = true
        )
    }

    fun createWasteRequest(
        wasteType: String,
        quantityTons: Double,
        location: String,
        collectionDate: String,
        collectionTime: String,
        description: String,
        imageUrl: String? = null
    ): WasteRequest {
        val randomNum = (1000..9999).random()
        val newId = "WST-2025-$randomNum"
        val user = _currentUser.value

        val newRequest = WasteRequest(
            id = newId,
            providerName = user.companyName.ifEmpty { "Registered Mining Unit" },
            leaseCode = user.licenseCode.ifEmpty { "ML-2025-010" },
            wasteType = wasteType,
            approximateQuantityTons = quantityTons,
            location = location,
            collectionDate = collectionDate,
            collectionTime = collectionTime,
            description = description,
            imageUrl = imageUrl,
            status = WasteLifecycleStatus.REQUESTED,
            allocatedRecycler = "Hindalco Beneficiation Hub #3",
            aiConfidence = 99.2,
            predictedComposition = when {
                wasteType.contains("Copper", true) -> "Cu 1.25%, Fe 38%, SiO2 34%"
                wasteType.contains("Bauxite", true) || wasteType.contains("Red Mud", true) -> "Fe2O3 48%, Al2O3 18%, TiO2 6%"
                wasteType.contains("Iron", true) -> "Fe 56%, SiO2 9%, Al2O3 4%"
                else -> "Mineral Recoverable Grade: 68.4%"
            },
            escrowAmount = quantityTons * 5800.0,
            escrowStatus = EscrowStatus.PENDING
        )

        _wasteRequests.value = listOf(newRequest) + _wasteRequests.value

        // Notify Admin & Recycler
        val adminNotif = NotificationItem(
            id = UUID.randomUUID().toString(),
            targetRole = UserRole.ADMIN,
            title = "New Waste Request $newId",
            message = "$quantityTons MT of $wasteType registered by ${newRequest.providerName}.",
            timestamp = "Just now",
            isRead = false,
            severity = "NORMAL"
        )
        _notifications.value = listOf(adminNotif) + _notifications.value

        return newRequest
    }

    fun advanceWasteLifecycle(requestId: String) {
        val currentList = _wasteRequests.value.toMutableList()
        val index = currentList.indexOfFirst { it.id == requestId }
        if (index != -1) {
            val req = currentList[index]
            val nextStatus = when (req.status) {
                WasteLifecycleStatus.REQUESTED -> WasteLifecycleStatus.VERIFIED
                WasteLifecycleStatus.VERIFIED -> WasteLifecycleStatus.RECYCLER_ASSIGNED
                WasteLifecycleStatus.RECYCLER_ASSIGNED -> WasteLifecycleStatus.ACCEPTED
                WasteLifecycleStatus.ACCEPTED -> WasteLifecycleStatus.COLLECTED
                WasteLifecycleStatus.COLLECTED -> WasteLifecycleStatus.PROCESSING
                WasteLifecycleStatus.PROCESSING -> WasteLifecycleStatus.RECYCLED
                WasteLifecycleStatus.RECYCLED -> WasteLifecycleStatus.PRODUCT_CREATED
                WasteLifecycleStatus.PRODUCT_CREATED -> WasteLifecycleStatus.SOLD
                WasteLifecycleStatus.SOLD -> WasteLifecycleStatus.COMPLETED
                WasteLifecycleStatus.COMPLETED -> WasteLifecycleStatus.COMPLETED
            }
            currentList[index] = req.copy(status = nextStatus)
            _wasteRequests.value = currentList
        }
    }

    fun acceptWasteRequest(requestId: String, recyclerName: String) {
        val currentList = _wasteRequests.value.toMutableList()
        val index = currentList.indexOfFirst { it.id == requestId }
        if (index != -1) {
            val req = currentList[index]
            currentList[index] = req.copy(
                status = WasteLifecycleStatus.ACCEPTED,
                allocatedRecycler = recyclerName
            )
            _wasteRequests.value = currentList

            // Also create Work Order
            val wo = WorkOrder(
                id = "WO-2025-${(100..999).random()}",
                wasteId = req.id,
                recyclerCompany = recyclerName,
                estimatedWeight = req.approximateQuantityTons,
                actualWeight = req.approximateQuantityTons,
                materialType = req.wasteType,
                quality = "Inspection Grade A",
                status = "Collection Started",
                createdAt = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(Date())
            )
            _workOrders.value = listOf(wo) + _workOrders.value
        }
    }

    fun updateWorkOrder(workOrderId: String, actualWeight: Double, quality: String) {
        val currentList = _workOrders.value.toMutableList()
        val index = currentList.indexOfFirst { it.id == workOrderId }
        if (index != -1) {
            val wo = currentList[index]
            currentList[index] = wo.copy(
                actualWeight = actualWeight,
                quality = quality,
                status = "Weighbridge Verified"
            )
            _workOrders.value = currentList
        }
    }

    fun addAIClassification(result: AIClassificationResult) {
        _aiClassifications.value = listOf(result) + _aiClassifications.value
    }

    fun verifyAIClassification(id: String, verifiedBy: String) {
        val currentList = _aiClassifications.value.toMutableList()
        val index = currentList.indexOfFirst { it.id == id }
        if (index != -1) {
            val item = currentList[index]
            currentList[index] = item.copy(
                isVerified = true,
                verifiedBy = verifiedBy,
                status = "STQC Verified & DSC Signed"
            )
            _aiClassifications.value = currentList
        }
    }

    fun placeManufacturerOrder(
        product: RecycledProduct,
        quantity: Double,
        deliveryDate: String,
        destination: String,
        requirements: String
    ): ManufacturerOrder {
        val user = _currentUser.value
        val newOrderId = "PO-MFR-2025-${(100..999).random()}"
        val totalAmount = product.price * quantity

        val newOrder = ManufacturerOrder(
            id = newOrderId,
            productId = product.id,
            productName = product.productName,
            manufacturerName = user.companyName.ifEmpty { "Industrial Off-taker" },
            recyclerName = product.recyclerCompany,
            quantity = quantity,
            quality = product.qualityGrade,
            deliveryDate = deliveryDate,
            deliveryLocation = destination,
            requirements = requirements,
            totalAmount = totalAmount,
            status = OrderLifecycleStatus.REQUESTED,
            vehicleId = "DL-01-MFR-${(1000..9999).random()}",
            eta = "2 business days",
            orderDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        )

        _manufacturerOrders.value = listOf(newOrder) + _manufacturerOrders.value

        // Escrow payment hold record
        val payment = PaymentRecord(
            transactionId = "TXN-MFR-${(1000..9999).random()}",
            fromParty = newOrder.manufacturerName,
            toBeneficiary = "National Escrow Facility",
            amount = totalAmount,
            date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date()),
            status = PaymentStatus.PROCESSING,
            blockchainHash = "0x" + UUID.randomUUID().toString().replace("-", "").take(16),
            paymentType = "Manufacturer PO Escrow Hold"
        )
        _payments.value = listOf(payment) + _payments.value

        return newOrder
    }

    fun advanceOrderLifecycle(orderId: String) {
        val currentList = _manufacturerOrders.value.toMutableList()
        val index = currentList.indexOfFirst { it.id == orderId }
        if (index != -1) {
            val order = currentList[index]
            val nextStatus = when (order.status) {
                OrderLifecycleStatus.REQUESTED -> OrderLifecycleStatus.REVIEWING
                OrderLifecycleStatus.REVIEWING -> OrderLifecycleStatus.ACCEPTED
                OrderLifecycleStatus.ACCEPTED -> OrderLifecycleStatus.PAYMENT_PENDING
                OrderLifecycleStatus.PAYMENT_PENDING -> OrderLifecycleStatus.PAYMENT_COMPLETED
                OrderLifecycleStatus.PAYMENT_COMPLETED -> OrderLifecycleStatus.PROCESSING
                OrderLifecycleStatus.PROCESSING -> OrderLifecycleStatus.PACKED
                OrderLifecycleStatus.PACKED -> OrderLifecycleStatus.DISPATCHED
                OrderLifecycleStatus.DISPATCHED -> OrderLifecycleStatus.IN_TRANSIT
                OrderLifecycleStatus.IN_TRANSIT -> OrderLifecycleStatus.DELIVERED
                OrderLifecycleStatus.DELIVERED -> OrderLifecycleStatus.COMPLETED
                OrderLifecycleStatus.COMPLETED -> OrderLifecycleStatus.COMPLETED
            }
            currentList[index] = order.copy(status = nextStatus)
            _manufacturerOrders.value = currentList
        }
    }

    fun disburseERupiVoucher(beneficiary: String, amount: Double) {
        val record = PaymentRecord(
            transactionId = "TXN-ERUPI-${(1000..9999).random()}",
            fromParty = "Hindalco Beneficiation Hub #3 Escrow",
            toBeneficiary = beneficiary,
            amount = amount,
            date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date()),
            status = PaymentStatus.CREDITED,
            blockchainHash = "0x" + UUID.randomUUID().toString().replace("-", "").take(16),
            paymentType = "e-RUPI Green Incentive Voucher"
        )
        _payments.value = listOf(record) + _payments.value
    }

    fun resolveFraudAnomaly(manifestId: String, action: String) {
        val currentList = _wasteRequests.value.toMutableList()
        val index = currentList.indexOfFirst { it.id == manifestId }
        if (index != -1) {
            val req = currentList[index]
            currentList[index] = req.copy(
                escrowStatus = if (action == "override") EscrowStatus.SETTLED else EscrowStatus.FROZEN
            )
            _wasteRequests.value = currentList
        }
    }

    fun findPassportRecord(query: String): Any? {
        val trimmed = query.trim()
        val waste = _wasteRequests.value.firstOrNull { it.id.equals(trimmed, true) }
        if (waste != null) return waste
        val product = _recycledProducts.value.firstOrNull { it.id.equals(trimmed, true) }
        if (product != null) return product
        val order = _manufacturerOrders.value.firstOrNull { it.id.equals(trimmed, true) }
        if (order != null) return order
        val workOrder = _workOrders.value.firstOrNull { it.id.equals(trimmed, true) }
        if (workOrder != null) return workOrder
        return null
    }
}
