package com.lifesparktech.lsphysio.android.data
import java.io.Serializable

data class GASResult(
    val totalScore: Int,
    val somaticScore: Int,
    val cognitiveScore: Int,
    val affectiveScore: Int,
    val anxietyCategory: String
) : Serializable

data class MiniBestResult(
    val totalScore: Int,
    val anticipatory: Int,
    val reactive_postural_control: Int,
    val sensory_orientation: Int,
    val dynamic_gait: Int
) : Serializable


