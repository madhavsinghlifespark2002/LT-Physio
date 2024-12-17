package com.lifesparktech.lsphysio.android.data
data class Patient(
    val serialNo: String,
    val clinicId: String = "",
    val name: String,
    val age: Int,
    val gender: String,
//    val status: String = "pending", // default value
//    val email: String,
//    val phone: String,
//    val address: String,
//    val height: Int, // Added height
//    val weight: Int, // Added weight
//    val diagnostics: List<String> = emptyList(), // Added diagnostics
//    val extraDetails: List<String> = emptyList() // Added extraDetails
)
//val samplePatients = listOf(
//    Patient(
//        serialNo = "1",
//        name = "John Doe",
//        clinicId = "C101",
//        age = 30,
//        gender = "Male",
//        status = "Active",
//        email = "john@example.com",
//        phone = "123-456-7890",
//        address = "123 Main St, City A",
//        height = 175,
//        weight = 70,
//        diagnostics = listOf("Hypertension", "Diabetes"),
//        extraDetails = listOf("Blood Group: O+", "Allergy: None")
//    ),
//    Patient(
//        serialNo = "2",
//        name = "Jane Smith",
//        clinicId = "C102",
//        age = 25,
//        gender = "Female",
//        status = "Inactive",
//        email = "jane@example.com",
//        phone = "123-456-7891",
//        address = "456 Elm St, City B",
//        height = 160,
//        weight = 55,
//        diagnostics = listOf("Anemia"),
//        extraDetails = listOf("Blood Group: A+", "Allergy: Penicillin")
//    ),
//    Patient(
//        serialNo = "3",
//        name = "Michael Brown",
//        clinicId = "C103",
//        age = 35,
//        gender = "Male",
//        status = "Active",
//        email = "michael@example.com",
//        phone = "123-456-7892",
//        address = "789 Oak St, City C",
//        height = 180,
//        weight = 85,
//        diagnostics = listOf("Asthma"),
//        extraDetails = listOf("Blood Group: B-", "Allergy: Dust")
//    ),
//    Patient(
//        serialNo = "4",
//        name = "Emily Davis",
//        clinicId = "C104",
//        age = 28,
//        gender = "Female",
//        status = "Active",
//        email = "emily@example.com",
//        phone = "123-456-7893",
//        address = "101 Pine St, City D",
//        height = 165,
//        weight = 60,
//        diagnostics = listOf("Thyroid Disorder"),
//        extraDetails = listOf("Blood Group: AB+", "Allergy: Shellfish")
//    ),
//    Patient(
//        serialNo = "5",
//        name = "William Wilson",
//        clinicId = "C105",
//        age = 40,
//        gender = "Male",
//        status = "Inactive",
//        email = "william@example.com",
//        phone = "123-456-7894",
//        address = "202 Cedar St, City E",
//        height = 178,
//        weight = 82,
//        diagnostics = listOf("Arthritis"),
//        extraDetails = listOf("Blood Group: O-", "Allergy: None")
//    ),
//    Patient(
//        serialNo = "6",
//        name = "Sophia Johnson",
//        clinicId = "C106",
//        age = 32,
//        gender = "Female",
//        status = "Active",
//        email = "sophia@example.com",
//        phone = "123-456-7895",
//        address = "303 Birch St, City F",
//        height = 162,
//        weight = 58,
//        diagnostics = listOf("Migraines"),
//        extraDetails = listOf("Blood Group: A-", "Allergy: Pollen")
//    ),
//    Patient(
//        serialNo = "7",
//        name = "James Garcia",
//        clinicId = "C107",
//        age = 29,
//        gender = "Male",
//        status = "Active",
//        email = "james@example.com",
//        phone = "123-456-7896",
//        address = "404 Walnut St, City G",
//        height = 177,
//        weight = 73,
//        diagnostics = listOf("Sports Injury"),
//        extraDetails = listOf("Blood Group: B+", "Allergy: None")
//    ),
//    Patient(
//        serialNo = "8",
//        name = "Ava Martinez",
//        clinicId = "C108",
//        age = 31,
//        gender = "Female",
//        status = "Inactive",
//        email = "ava@example.com",
//        phone = "123-456-7897",
//        address = "505 Maple St, City H",
//        height = 159,
//        weight = 54,
//        diagnostics = listOf("Low Blood Pressure"),
//        extraDetails = listOf("Blood Group: O+", "Allergy: Nuts")
//    ),
//    Patient(
//        serialNo = "9",
//        name = "Isabella Hernandez",
//        clinicId = "C109",
//        age = 27,
//        gender = "Female",
//        status = "Active",
//        email = "isabella@example.com",
//        phone = "123-456-7898",
//        address = "606 Cherry St, City I",
//        height = 166,
//        weight = 59,
//        diagnostics = listOf("Stress Disorder"),
//        extraDetails = listOf("Blood Group: AB-", "Allergy: None")
//    ),
//    Patient(
//        serialNo = "10",
//        name = "Benjamin Clark",
//        clinicId = "C110",
//        age = 34,
//        gender = "Male",
//        status = "Inactive",
//        email = "benjamin@example.com",
//        phone = "123-456-7899",
//        address = "707 Ash St, City J",
//        height = 182,
//        weight = 87,
//        diagnostics = listOf("Obesity", "Cholesterol"),
//        extraDetails = listOf("Blood Group: B+", "Allergy: Dairy")
//    ),
//    Patient(
//        serialNo = "11",
//        name = "Mia Lewis",
//        clinicId = "C111",
//        age = 26,
//        gender = "Female",
//        status = "Active",
//        email = "mia@example.com",
//        phone = "123-456-7800",
//        address = "808 Spruce St, City K",
//        height = 164,
//        weight = 57,
//        diagnostics = listOf("Anxiety"),
//        extraDetails = listOf("Blood Group: A+", "Allergy: None")
//    ),
//    Patient(
//        serialNo = "12",
//        name = "Lucas Walker",
//        clinicId = "C112",
//        age = 33,
//        gender = "Male",
//        status = "Inactive",
//        email = "lucas@example.com",
//        phone = "123-456-7801",
//        address = "909 Willow St, City L",
//        height = 179,
//        weight = 80,
//        diagnostics = listOf("Back Pain"),
//        extraDetails = listOf("Blood Group: O-", "Allergy: None")
//    ),
//    Patient(
//        serialNo = "13",
//        name = "Charlotte Hall",
//        clinicId = "C113",
//        age = 30,
//        gender = "Female",
//        status = "Active",
//        email = "charlotte@example.com",
//        phone = "123-456-7802",
//        address = "1010 Magnolia St, City M",
//        height = 168,
//        weight = 60,
//        diagnostics = listOf("Hypertension"),
//        extraDetails = listOf("Blood Group: B-", "Allergy: None")
//    ),
//    Patient(
//        serialNo = "14",
//        name = "Elijah Allen",
//        clinicId = "C114",
//        age = 36,
//        gender = "Male",
//        status = "Active",
//        email = "elijah@example.com",
//        phone = "123-456-7803",
//        address = "1111 Palm St, City N",
//        height = 183,
//        weight = 85,
//        diagnostics = listOf("Diabetes"),
//        extraDetails = listOf("Blood Group: AB+", "Allergy: None")
//    ),
//    Patient(
//        serialNo = "15",
//        name = "Amelia Young",
//        clinicId = "C115",
//        age = 37,
//        gender = "Female",
//        status = "Inactive",
//        email = "amelia@example.com",
//        phone = "123-456-7804",
//        address = "1212 Cypress St, City O",
//        height = 162,
//        weight = 65,
//        diagnostics = listOf("Heart Disease"),
//        extraDetails = listOf("Blood Group: O+", "Allergy: Pollen")
//    )
//)
