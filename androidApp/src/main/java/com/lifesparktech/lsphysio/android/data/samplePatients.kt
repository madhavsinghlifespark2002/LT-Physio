package com.lifesparktech.lsphysio.android.data

data class Patient(
    val serialNo: Int,
    val name: String,
    val age: Int,
    val dateOfBirth: String,
    val status: String,
    val email: String,
    val phone: String
)
val samplePatients = listOf(
    Patient(1, "John Doe", 30, "1994-12-10", "Active", "john@example.com", "123-456-7890"),
    Patient(2, "Jane Smith", 25, "1999-07-15", "Inactive", "jane@example.com", "123-456-7891"),
    Patient(3, "Michael Brown", 35, "1989-05-22", "Active", "michael@example.com", "123-456-7892"),
    Patient(4, "Emily Davis", 28, "1996-03-14", "Active", "emily@example.com", "123-456-7893"),
    Patient(5, "William Wilson", 40, "1984-11-30", "Inactive", "william@example.com", "123-456-7894"),
    Patient(6, "Sophia Johnson", 32, "1992-08-19", "Active", "sophia@example.com", "123-456-7895"),
    Patient(7, "James Garcia", 29, "1995-10-25", "Active", "james@example.com", "123-456-7896"),
    Patient(8, "Ava Martinez", 31, "1993-04-05", "Inactive", "ava@example.com", "123-456-7897"),
    Patient(9, "Isabella Hernandez", 27, "1997-02-12", "Active", "isabella@example.com", "123-456-7898"),
    Patient(10, "Benjamin Clark", 34, "1990-06-21", "Inactive", "benjamin@example.com", "123-456-7899"),
    Patient(11, "Mia Lewis", 26, "1998-01-07", "Active", "mia@example.com", "123-456-7800"),
    Patient(12, "Lucas Walker", 33, "1991-09-18", "Inactive", "lucas@example.com", "123-456-7801"),
    Patient(13, "Charlotte Hall", 30, "1994-12-12", "Active", "charlotte@example.com", "123-456-7802"),
    Patient(14, "Elijah Allen", 36, "1988-07-29", "Active", "elijah@example.com", "123-456-7803"),
    Patient(15, "Amelia Young", 37, "1987-03-03", "Inactive", "amelia@example.com", "123-456-7804"),
    Patient(16, "Olivia Scott", 24, "2000-08-15", "Active", "olivia@example.com", "123-456-7805"),
    Patient(17, "Ethan Harris", 38, "1986-11-02", "Inactive", "ethan@example.com", "123-456-7806"),
    Patient(18, "Grace Lee", 29, "1995-05-20", "Active", "grace@example.com", "123-456-7807"),
    Patient(19, "Noah Wright", 34, "1990-03-11", "Inactive", "noah@example.com", "123-456-7808"),
    Patient(20, "Lily Thompson", 27, "1997-12-09", "Active", "lily@example.com", "123-456-7809"),
//    Patient(21, "Jack Martinez", 40, "1984-09-21", "Inactive", "jack@example.com", "123-456-7810"),
//    Patient(22, "Chloe Walker", 25, "1999-06-30", "Active", "chloe@example.com", "123-456-7811"),
//    Patient(23, "Henry Baker", 31, "1993-02-18", "Active", "henry@example.com", "123-456-7812"),
//    Patient(24, "Sophie Green", 28, "1996-01-03", "Inactive", "sophie@example.com", "123-456-7813"),
//    Patient(25, "Daniel Evans", 39, "1985-04-17", "Active", "daniel@example.com", "123-456-7814")
)
