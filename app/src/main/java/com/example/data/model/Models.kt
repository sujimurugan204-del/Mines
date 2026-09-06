package com.example.data.model

enum class UserRole(val displayName: String, val badgeText: String) {
    ADMIN("Super Admin (Ministry of Mines)", "SOVEREIGN ADMIN"),
    WASTE_PROVIDER("Mining Enterprise / Waste Provider", "WASTE PROVIDER"),
    RECYCLER("Certified Recycler / Kabadiwala", "RECYCLER HUB"),
    MANUFACTURER("Industrial Manufacturer", "MANUFACTURER")
}

data class UserProfile(
    val fullName: String,
    val username: String,
    val phoneNumber: String,
    val email: String,
    val userId: String,
    val accountNumber: String,
    val companyName: String,
    val location: String,
    val address: String,
    val userType: UserRole,
    val gstNumber: String = "07AAACM9182C1Z4",
    val licenseCode: String = "STQC-LVL3-2024-91",
    val isVerified: Boolean = true
)

enum class WasteLifecycleStatus(val stepIndex: Int, val label: String) {
    REQUESTED(1, "Requested"),
    VERIFIED(2, "Verified"),
    RECYCLER_ASSIGNED(3, "Recycler Assigned"),
    ACCEPTED(4, "Accepted"),
    COLLECTED(5, "Collected"),
    PROCESSING(6, "Processing"),
    RECYCLED(7, "Recycled"),
    PRODUCT_CREATED(8, "Product Created"),
    SOLD(9, "Sold"),
    COMPLETED(10, "Completed")
}

enum class EscrowStatus(val label: String) {
    PENDING("Pending"),
    ESCROW_HELD("Escrow Held"),
    SETTLED("Settled"),
    FROZEN("Frozen")
}

data class WasteRequest(
    val id: String,
    val providerName: String,
    val leaseCode: String,
    val wasteType: String,
    val approximateQuantityTons: Double,
    val location: String,
    val collectionDate: String,
    val collectionTime: String,
    val description: String,
    val imageUrl: String? = null,
    val status: WasteLifecycleStatus,
    val allocatedRecycler: String? = null,
    val aiConfidence: Double = 99.4,
    val predictedComposition: String = "Cu 1.2%, Fe 38.4%, SiO2 34.1%",
    val truckRfid: String? = null,
    val driverName: String? = null,
    val weighbridgeGross: Double? = null,
    val weighbridgeTare: Double? = null,
    val escrowAmount: Double = 0.0,
    val escrowStatus: EscrowStatus = EscrowStatus.PENDING,
    val createdAt: String = "Today, 10:40 IST"
)

data class WorkOrder(
    val id: String,
    val wasteId: String,
    val recyclerCompany: String,
    val estimatedWeight: Double,
    var actualWeight: Double,
    val materialType: String,
    var quality: String,
    val status: String,
    val createdAt: String,
    val photoNote: String = "Calibrated digital weigh scale verified"
)

data class RecoverableMaterial(
    val name: String,
    val percentage: Double,
    val estimatedTons: Double
)

data class AIClassificationResult(
    val id: String,
    val wasteType: String,
    val material: String,
    val confidenceScore: Double,
    val recommendedMethod: String,
    val recoverableMaterials: List<RecoverableMaterial>,
    val verifiedBy: String? = null,
    val isVerified: Boolean = false,
    val status: String = "AI Predicted",
    val timestamp: String,
    val spectralSignature: String = "XRD-2024-IND-BAND4"
)

data class RecyclingProcessRecord(
    val runId: String,
    val inputWeight: Double,
    val recoveredMaterial: String,
    val recoveredQuantity: Double,
    val wasteLoss: Double,
    val recyclingRate: Double,
    val outputProduct: String,
    val processingStatus: String,
    val stage: String
)

data class RecycledProduct(
    val id: String,
    val productName: String,
    val sourceWaste: String,
    val recyclerCompany: String,
    val quantity: Double,
    val qualityGrade: String,
    val price: Double,
    val unit: String,
    val availableStock: Double,
    val soldQuantity: Double,
    val location: String = "Dahej Recycler Complex, Gujarat",
    val rating: Double = 4.8,
    val stqcVerified: Boolean = true
)

enum class OrderLifecycleStatus(val stepIndex: Int, val label: String) {
    REQUESTED(1, "Requested"),
    REVIEWING(2, "Reviewing"),
    ACCEPTED(3, "Accepted"),
    PAYMENT_PENDING(4, "Payment Pending"),
    PAYMENT_COMPLETED(5, "Payment Completed"),
    PROCESSING(6, "Processing"),
    PACKED(7, "Packed"),
    DISPATCHED(8, "Dispatched"),
    IN_TRANSIT(9, "In Transit"),
    DELIVERED(10, "Delivered"),
    COMPLETED(11, "Completed")
}

data class ManufacturerOrder(
    val id: String,
    val productId: String,
    val productName: String,
    val manufacturerName: String,
    val recyclerName: String,
    val quantity: Double,
    val quality: String,
    val deliveryDate: String,
    val deliveryLocation: String,
    val requirements: String,
    val totalAmount: Double,
    val status: OrderLifecycleStatus,
    val vehicleId: String? = null,
    val eta: String? = null,
    val orderDate: String = "Today"
)

enum class PaymentStatus {
    PENDING,
    PROCESSING,
    CREDITED,
    FAILED,
    COMPLETED
}

data class PaymentRecord(
    val transactionId: String,
    val fromParty: String,
    val toBeneficiary: String,
    val amount: Double,
    val date: String,
    val status: PaymentStatus,
    val blockchainHash: String,
    val paymentType: String
)

data class NotificationItem(
    val id: String,
    val targetRole: UserRole,
    val title: String,
    val message: String,
    val timestamp: String,
    val isRead: Boolean = false,
    val severity: String = "NORMAL"
)

data class DailyDataRecord(
    val date: String,
    val leaseId: String,
    val company: String,
    val wasteType: String,
    val wasteQuantity: Double,
    val collected: Double,
    val recycled: Double,
    val product: String,
    val revenue: Double,
    val carbonSaved: Double,
    val status: String
)

data class EnvironmentalMetrics(
    val totalWasteRecycledMT: Double,
    val materialRecoveredMT: Double,
    val co2AvoidedTons: Double,
    val energySavedMWh: Double,
    val rawMaterialConservedMT: Double
)
