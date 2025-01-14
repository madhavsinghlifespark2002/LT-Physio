package com.lifesparktech.lsphysio.android.Controller

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.lifesparktech.lsphysio.android.data.GASResult
import com.lifesparktech.lsphysio.android.data.MiniBestResult
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
fun updatePatient(patientId: String, updatedPatient: Patient) {
    val firestore = FirebaseFirestore.getInstance()

    firestore.collection("Patient")
        .document(patientId)
        .set(updatedPatient)
        .addOnSuccessListener {
            println("Patient updated successfully with ID: $patientId")
        }
        .addOnFailureListener { e ->
            e.printStackTrace()
            println("Failed to update patient: ${e.message}")
        }
}

suspend fun updatePatientId(patientId: String): Patient? {
    val firestore = FirebaseFirestore.getInstance()
    return try {
        val document = firestore.collection("Patient").document(patientId).get().await()
        document.toObject(Patient::class.java)?.copy(serialNo = document.id)
    } catch (e: Exception) {
        e.printStackTrace()
        null // Return null if an error occurs
    }
}
fun updatePatientWithTestResult(patient: Patient, timeTaken: Long?, testname: String) {
    val firestore = FirebaseFirestore.getInstance()
    if (timeTaken != null) {
        val seconds = (timeTaken / 1000)
        val milliseconds = (timeTaken % 1000)
        val timeString = "Timings: ${seconds}s ${milliseconds}ms"
        val timestamp = System.currentTimeMillis()
        val testResult = mapOf(
            "testName" to testname,
            "timeTaken" to timeString,
            "timestamp" to timestamp
        )
        val patientRef = firestore.collection("Patient").document(patient.serialNo)
        firestore.runTransaction { transaction ->
            val snapshot = transaction.get(patientRef)
            val currentResults = snapshot.get(testname) as? List<Map<String, Any>> ?: emptyList()
            val updatedResults = currentResults + testResult
            transaction.update(patientRef, testname, updatedResults)
        }.addOnSuccessListener {
            println("Patient test result updated successfully with timestamp.")
        }.addOnFailureListener { exception ->
            println("Error updating test result: ${exception.message}")
        }
    }
}
fun addedGasData(patient: Patient, result: GASResult) {
    val firestore = FirebaseFirestore.getInstance()
    val timestamp = System.currentTimeMillis()
    val testResult = mapOf(
        "testName" to "gasTest",
        "totalScore" to result.totalScore,
        "somaticScore" to result.somaticScore,
        "cognitiveScore" to result.cognitiveScore,
        "affectiveScore" to result.affectiveScore,
        "anxietyCategory" to result.anxietyCategory,
        "timestamp" to timestamp
    )
    val patientRef = firestore.collection("Patient").document(patient.serialNo)
    firestore.runTransaction { transaction ->
        val snapshot = transaction.get(patientRef)
        val currentResults = snapshot.get("gasTest") as? List<Map<String, Any>> ?: emptyList()
        val updatedResults = currentResults + testResult
        transaction.update(patientRef, "gasTest", updatedResults)
    }.addOnSuccessListener {
        println("Patient test result updated successfully.")
    }.addOnFailureListener { exception ->
        println("Error updating test result: ${exception.message}")
    }
}
fun addedMiniData(patient: Patient, result: MiniBestResult) {
    val firestore = FirebaseFirestore.getInstance()
    val timestamp = System.currentTimeMillis()
    val testResult = mapOf(
        "testName" to "minibestTest",
        "totalScore" to result.totalScore,
        "anticipatory" to result.anticipatory,
        "reactive_postural_control" to result.reactive_postural_control,
        "sensory_orientation" to result.sensory_orientation,
        "dynamic_gait" to result.dynamic_gait,
        "timestamp" to timestamp
    )
    val patientRef = firestore.collection("Patient").document(patient.serialNo)
    firestore.runTransaction { transaction ->
        val snapshot = transaction.get(patientRef)
        val currentResults = snapshot.get("minibestTest") as? List<Map<String, Any>> ?: emptyList()
        val updatedResults = currentResults + testResult
        transaction.update(patientRef, "minibestTest", updatedResults)
    }.addOnSuccessListener {
        println("Patient test result updated successfully.")
    }.addOnFailureListener { exception ->
        println("Error updating test result: ${exception.message}")
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

