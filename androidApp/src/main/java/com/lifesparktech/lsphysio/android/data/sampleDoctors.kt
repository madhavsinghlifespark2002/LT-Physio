package com.lifesparktech.lsphysio.android.data

data class Doctors(
    val serialNo: Int,
    val name: String,
    val email: String,
    val phone: String,
    val workingDays: List<String>,
    val department: String
)

val sampleDoctors = listOf(
    Doctors(1, "John Doe", "john@example.com", "123-456-7890", listOf("Monday", "Wednesday", "Friday"), "Cardiology"),
    Doctors(2, "Jane Smith", "jane@example.com", "123-456-7891", listOf("Tuesday", "Thursday"), "Neurology"),
    Doctors(3, "Michael Brown", "michael@example.com", "123-456-7892", listOf("Monday", "Tuesday", "Friday"), "Orthopedics"),
    Doctors(4, "Emily Davis", "emily@example.com", "123-456-7893", listOf("Wednesday", "Friday"), "Pediatrics"),
    Doctors(5, "William Wilson", "william@example.com", "123-456-7894", listOf("Monday", "Thursday"), "General Surgery"),
    Doctors(6, "Sophia Johnson", "sophia@example.com", "123-456-7895", listOf("Tuesday", "Thursday", "Saturday"), "Gynecology"),
    Doctors(7, "James Garcia", "james@example.com", "123-456-7896", listOf("Monday", "Friday"), "Dermatology"),
    Doctors(8, "Ava Martinez", "ava@example.com", "123-456-7897", listOf("Tuesday", "Thursday"), "Radiology"),
    Doctors(9, "Isabella Hernandez", "isabella@example.com", "123-456-7898", listOf("Monday", "Wednesday", "Saturday"), "Ophthalmology"),
    Doctors(10, "Benjamin Clark", "benjamin@example.com", "123-456-7899", listOf("Thursday", "Friday"), "Pathology"),
    Doctors(11, "Mia Lewis", "mia@example.com", "123-456-7800", listOf("Monday", "Friday"), "Anesthesiology"),
    Doctors(12, "Lucas Walker", "lucas@example.com", "123-456-7801", listOf("Wednesday", "Saturday"), "Psychiatry"),
    Doctors(13, "Charlotte Hall", "charlotte@example.com", "123-456-7802", listOf("Tuesday", "Thursday"), "Urology"),
    Doctors(14, "Elijah Allen", "elijah@example.com", "123-456-7803", listOf("Monday", "Wednesday", "Friday"), "ENT"),
    Doctors(15, "Amelia Young", "amelia@example.com", "123-456-7804", listOf("Tuesday", "Thursday"), "Oncology"),
    Doctors(16, "Olivia Scott", "olivia@example.com", "123-456-7805", listOf("Monday", "Saturday"), "Rheumatology"),
    Doctors(17, "Ethan Harris", "ethan@example.com", "123-456-7806", listOf("Thursday", "Friday"), "Gastroenterology"),
    Doctors(18, "Grace Lee", "grace@example.com", "123-456-7807", listOf("Monday", "Wednesday", "Friday"), "Endocrinology"),
    Doctors(19, "Noah Wright", "noah@example.com", "123-456-7808", listOf("Tuesday", "Thursday"), "Nephrology"),
    Doctors(20, "Lily Thompson", "lily@example.com", "123-456-7809", listOf("Monday", "Friday"), "Pulmonology")
)
