package com.lifesparktech.lsphysio.android.data

data class PaymentReceipt(
    val receiptId: String,
    val patientId: String,
    val patientName: String,
    val paymentAmount: Double,
    val paymentDate: String,
    val paymentMethod: String,
    val doctorName: String,
    val serviceRendered: String,
    val clinicName: String,
    val receiptStatus: String
)
val paymentReceipts = listOf(
    PaymentReceipt("RCP001", "P001", "John Doe", 150.0, "2024-12-01", "Credit Card", "Dr. Smith", "Consultation", "Health Clinic", "Paid"),
    PaymentReceipt("RCP002", "P002", "Jane Smith", 250.0, "2024-12-02", "Cash", "Dr. Lee", "X-Ray", "Wellness Clinic", "Paid"),
    PaymentReceipt("RCP003", "P003", "Alice Johnson", 100.0, "2024-12-03", "Insurance", "Dr. Clark", "Routine Checkup", "City Health Center", "Pending"),
    PaymentReceipt("RCP004", "P004", "Bob Brown", 500.0, "2024-12-04", "Credit Card", "Dr. Green", "Surgery", "Main Hospital", "Paid"),
    PaymentReceipt("RCP005", "P005", "Charlie Davis", 75.0, "2024-12-05", "Cash", "Dr. Taylor", "Consultation", "Health Clinic", "Paid"),
    PaymentReceipt("RCP006", "P006", "David Wilson", 200.0, "2024-12-06", "Insurance", "Dr. Wilson", "Blood Test", "Wellness Clinic", "Paid"),
    PaymentReceipt("RCP007", "P007", "Emily Moore", 300.0, "2024-12-07", "Credit Card", "Dr. Miller", "MRI Scan", "City Health Center", "Cancelled"),
    PaymentReceipt("RCP008", "P008", "Frank Harris", 120.0, "2024-12-08", "Cash", "Dr. Anderson", "Dental Checkup", "Dental Clinic", "Paid"),
    PaymentReceipt("RCP009", "P009", "Grace Lee", 180.0, "2024-12-09", "Insurance", "Dr. White", "Eye Exam", "Vision Clinic", "Paid"),
    PaymentReceipt("RCP010", "P010", "Henry Clark", 220.0, "2024-12-10", "Credit Card", "Dr. Harris", "Physiotherapy", "Main Hospital", "Paid"),
    PaymentReceipt("RCP011", "P011", "Isabel Scott", 90.0, "2024-12-11", "Cash", "Dr. Young", "Consultation", "Health Clinic", "Paid"),
    PaymentReceipt("RCP012", "P012", "Jack Adams", 160.0, "2024-12-12", "Insurance", "Dr. Taylor", "Surgery", "Wellness Clinic", "Pending"),
    PaymentReceipt("RCP013", "P013", "Kathy Turner", 50.0, "2024-12-13", "Cash", "Dr. White", "Dental Checkup", "Dental Clinic", "Paid"),
    PaymentReceipt("RCP014", "P014", "Leo Martin", 210.0, "2024-12-14", "Credit Card", "Dr. Green", "Consultation", "Main Hospital", "Paid"),
    PaymentReceipt("RCP015", "P015", "Mona King", 350.0, "2024-12-15", "Insurance", "Dr. Lee", "X-Ray", "City Health Center", "Paid"),
    PaymentReceipt("RCP016", "P016", "Nathan Harris", 80.0, "2024-12-16", "Cash", "Dr. Clark", "Routine Checkup", "Health Clinic", "Paid"),
    PaymentReceipt("RCP017", "P017", "Olivia Baker", 400.0, "2024-12-17", "Credit Card", "Dr. Taylor", "Surgery", "Wellness Clinic", "Paid"),
    PaymentReceipt("RCP018", "P018", "Paul Green", 180.0, "2024-12-18", "Insurance", "Dr. Miller", "Blood Test", "City Health Center", "Cancelled"),
    PaymentReceipt("RCP019", "P019", "Quincy Wright", 150.0, "2024-12-19", "Cash", "Dr. Wilson", "Consultation", "Health Clinic", "Paid"),
    PaymentReceipt("RCP020", "P020", "Rachel Roberts", 260.0, "2024-12-20", "Credit Card", "Dr. White", "Eye Exam", "Vision Clinic", "Paid")
)
