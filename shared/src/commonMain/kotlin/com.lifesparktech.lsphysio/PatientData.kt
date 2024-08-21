package com.lifesparktech.lsphysio

data class PatientData(
    var name: String = "",
    var age: String = "",
    var dateOfAssessment: String = "",
    var doctorId: String = "",
    var gender: String = "",
    var hnyScore: String = "",
    var primaryDiagnosis: String = "",
    var summary: String = "",
    var timeOfAssessment: String = "",
    var freezingWith: String = "",
    var freezingWithout: String = "",
    var totalWith: Int = 0,
    var totalWithout: Int = 0
//    var withScore
) {
    companion object CurrentPatient {
        var patientData  = PatientData()
            set(value) = run {
                field = value
            }
    }
    fun toMap(): Map<String, Any> {
        return mapOf(
            "name" to name,
            "age" to age,
            "dateOfAssessment" to dateOfAssessment,
            "doctorId" to doctorId,
            "gender" to gender,
            "hnyScore" to hnyScore,
            "primaryDiagnosis" to primaryDiagnosis,
            "summary" to summary,
            "timeOfAssessment" to timeOfAssessment,
            "freezingWith" to freezingWith,
            "freezingWithout" to freezingWithout,
            "totalWith" to totalWith,
            "totalWithout" to totalWithout
        )
    }
}
