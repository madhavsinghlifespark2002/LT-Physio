package com.lifesparktech.lsphysio.android.Controller

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.lifesparktech.lsphysio.android.data.Patient

import kotlinx.coroutines.tasks.await
import com.lifesparktech.lsphysio.android.pages.generateRandomStringId
suspend fun fetchPatients(): List<Patient> {
    val firestore = FirebaseFirestore.getInstance()
    return try {
        val patientSnapshots = firestore.collection("Patient").get().await()
        patientSnapshots.documents.mapNotNull { patientDoc ->
            try {
                patientDoc.toObject(Patient::class.java)?.copy(serialNo = patientDoc.id)
            } catch (e: Exception) {
                e.printStackTrace()
                null // Skip documents with errors
            }
        }
    } catch (e: Exception) {
        e.printStackTrace()
        emptyList() // Return an empty list if there is an error
    }
}

fun addPatient(patient: Patient) {
    val firestore = FirebaseFirestore.getInstance()
    val patientId = generateRandomStringId()
    firestore.collection("Patient")
        .document(patientId)
        .set(patient.copy(serialNo = patientId))
        .addOnSuccessListener {
            println("Patient added successfully with ID: $patientId")
        }
        .addOnFailureListener { e ->
            e.printStackTrace()
            println("Failed to add patient: ${e.message}")
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

