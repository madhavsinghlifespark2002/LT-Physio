package com.lifesparktech.lsphysio.android.Controller

import com.google.firebase.firestore.FirebaseFirestore
import com.lifesparktech.lsphysio.android.data.Patient
import com.lifesparktech.lsphysio.android.pages.generateRandomStringId
import kotlinx.coroutines.tasks.await
suspend fun fetchPatients(): List<Patient> {
    val firestore = FirebaseFirestore.getInstance()
    return try {
        val patientSnapshots = firestore.collection("Patient").get().await()
        patientSnapshots.documents.flatMap { patientDoc ->
            val patientId = patientDoc.id
            val basicInfoSnapshots = firestore.collection("Patient")
                .document(patientId)
                .collection("Basic information")
                .get()
                .await()
            basicInfoSnapshots.documents.mapNotNull { basicInfoDoc ->
                try {
                    basicInfoDoc.toObject(Patient::class.java)?.copy(serialNo = patientId)
                } catch (e: Exception) {
                    e.printStackTrace()
                    null // Skip documents with errors
                }
            }
        }
    } catch (e: Exception) {
        e.printStackTrace()
        emptyList()
    }
}

//fun addPatient(patient: Patient) {
//    val firestore = FirebaseFirestore.getInstance()
//    val patientId = generateRandomStringId()
//    val infoId = generateRandomStringId()
//    firestore.collection("Patient")
//        .document(patientId)
//        .collection("Basic information")
//        .document(infoId)
//        .set(patient.copy(serialNo = patientId))
//        .addOnSuccessListener {
//            println("Patient added successfully with ID: $patientId")
//        }
//        .addOnFailureListener { e ->
//            e.printStackTrace()
//            println("Failed to add patient: ${e.message}")
//        }
//}
suspend fun addPatient(patient: Patient){
    val firestore = FirebaseFirestore.getInstance()
    return try {
        val patientRef = firestore.collection("Patient").document(patient.serialNo)
        val basicInfoRef = patientRef.collection("Basic information").document()
        firestore.runBatch { batch ->
            batch.set(basicInfoRef, patient)
        }.await()
        println("Patient with Serial No [${patient.serialNo}] successfully added.")

    } catch (e: Exception) {
        e.printStackTrace()

    }
}

suspend fun fetchPatientById(patientId: String): Patient? {
    val firestore = FirebaseFirestore.getInstance()
    return try {
        val document = firestore.collection("Patient").document(patientId).get().await()
        document.toObject(Patient::class.java)?.copy(serialNo = document.id)
    } catch (e: Exception) {
        e.printStackTrace()
        null // Return null if an error occurs
    }
}