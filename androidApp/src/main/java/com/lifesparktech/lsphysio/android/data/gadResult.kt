package com.lifesparktech.lsphysio.android.data
import java.io.Serializable

data class GASResult(
    val totalScore: Int,
    val somaticScore: Int,
    val cognitiveScore: Int,
    val affectiveScore: Int,
    val anxietyCategory: String
) : Serializable
