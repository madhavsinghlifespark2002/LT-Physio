package com.lifesparktech.lsphysio.android.data

data class Department(
    val serialNo: Int,
    val name: String,
    val numberOfDoctors: Int,
    val numberOfPatients: Int,
    val headOfDepartment: String,
    val contactInfo: String
)

val sampleDepartments = listOf(
    Department(
        serialNo = 1,
        name = "Physiotherapy",
        numberOfDoctors = 5,
        numberOfPatients = 50,
        headOfDepartment = "Dr. John Smith",
        contactInfo = "physiotherapy@example.com,\n +123-456-7890"
    ),
    Department(
        serialNo = 2,
        name = "Cardiology",
        numberOfDoctors = 8,
        numberOfPatients = 120,
        headOfDepartment = "Dr. Jane Doe",
        contactInfo = "cardiology@example.com,\n +123-456-7891"
    ),
    Department(
        serialNo = 3,
        name = "Neurology",
        numberOfDoctors = 6,
        numberOfPatients = 70,
        headOfDepartment = "Dr. Michael Brown",
        contactInfo = "neurology@example.com,\n +123-456-7892"
    ),
    Department(
        serialNo = 4,
        name = "Pediatrics",
        numberOfDoctors = 10,
        numberOfPatients = 200,
        headOfDepartment = "Dr. Emily Davis",
        contactInfo = "pediatrics@example.com,\n +123-456-7893"
    ),
    Department(
        serialNo = 5,
        name = "Orthopedics",
        numberOfDoctors = 7,
        numberOfPatients = 90,
        headOfDepartment = "Dr. William Wilson",
        contactInfo = "orthopedics@example.com,\n +123-456-7894"
    ),
    Department(
        serialNo = 6,
        name = "Dermatology",
        numberOfDoctors = 4,
        numberOfPatients = 60,
        headOfDepartment = "Dr. Sophia Johnson",
        contactInfo = "dermatology@example.com,\n +123-456-7895"
    ),
    Department(
        serialNo = 7,
        name = "Oncology",
        numberOfDoctors = 9,
        numberOfPatients = 150,
        headOfDepartment = "Dr. James Garcia",
        contactInfo = "oncology@example.com,\n +123-456-7896"
    ),
    Department(
        serialNo = 8,
        name = "Radiology",
        numberOfDoctors = 6,
        numberOfPatients = 80,
        headOfDepartment = "Dr. Ava Martinez",
        contactInfo = "radiology@example.com,\n +123-456-7897"
    ),
    Department(
        serialNo = 9,
        name = "General Medicine",
        numberOfDoctors = 12,
        numberOfPatients = 300,
        headOfDepartment = "Dr. Isabella Hernandez",
        contactInfo = "generalmedicine@example.com, +123-456-7898"
    ),
    Department(
        serialNo = 10,
        name = "Gynecology",
        numberOfDoctors = 8,
        numberOfPatients = 100,
        headOfDepartment = "Dr. Benjamin Clark",
        contactInfo = "gynecology@example.com, +123-456-7899"
    )
)
