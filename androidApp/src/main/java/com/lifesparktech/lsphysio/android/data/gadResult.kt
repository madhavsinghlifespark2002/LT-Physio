package com.lifesparktech.lsphysio.android.data
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
    val dynamic_gait: Int,
    val trial1Time1: Int? = 0,
    val trial1Time2: Int? = 0,
    val trial2Time1: Int? = 0,
    val trial2Time2: Int? = 0,
    var question7sec: Int? =0,
    var question8sec: Int? =0,
    var question9sec : Int? =0,
    var count3 : Int? =0,
    var tug1 : Int? =0,
    var tug2 : Int? =0,
) : Serializable


