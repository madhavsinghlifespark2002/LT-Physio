package com.lifesparktech.lsphysio.android.data
import androidx.annotation.Keep
@Keep
data class Patient(
    val serialNo: String ="",
    val clinicId: String = "",
    val name: String = "", // Done
    val age: Int =0, // done
    val gender: String ="", // done
    val status: String = "pending", // default value // not done
    val email: String ="", // done
    val phone: String ="", // done
    val address: String= "", // done
    val height: Int = 0, // Added height // done
    val weight: Int= 0, // Added weight // done
    val diagnostics: List<String> = emptyList(), // Added diagnostics
    val stsTest: List<Map<String, Any>> = emptyList(),
    val tugTest: List<Map<String, Any>> = emptyList(),
    val extraDetails: List<String> = emptyList(),
    val bloodGroup: String="", // Add this field
    val allergies: String="",  // Add this field
    val bodymassindex: Double=0.0, // Add this field
    val bloodPressure: String="",
    val chronicdisease: String="",
    val diabetesemergencies: String="",
    val surgery: String="",
    val relatedcomplication: String="",
    val  familydisease: String="",
    val hepaticIssues: String="",
    val hemoglobin: String="",
    val bloodsugar: String="",
    val heartrate: String=""
)