package com.lifesparktech.lsphysio.android.Controller

import com.google.firebase.firestore.FirebaseFirestore
import com.lifesparktech.lsphysio.android.data.Patient
import com.lifesparktech.lsphysio.android.pages.generateRandomStringId
import kotlinx.coroutines.tasks.await

suspend fun fetchPatients(): List<Patient> {
    val firestore = FirebaseFirestore.getInstance()
    return try {
        val snapshot = firestore.collection("Patient").get().await()
        println("this is snapshot: $snapshot")
        //emptyList()
        snapshot.documents.mapNotNull { doc ->
            doc.toObject(Patient::class.java)?.copy(serialNo = doc.id)
        }
    } catch (e: Exception) {
        e.printStackTrace()
        emptyList()
    }
}
fun addPatient(patient: Patient) {
    val firestore = FirebaseFirestore.getInstance()
    val patientId = generateRandomStringId()
//        val clinicId = currentUser?.uid ?: throw IllegalStateException("User not logged in")
    firestore.collection("Patient")
        .document(patientId)
        .set(patient.copy(serialNo = patientId))
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