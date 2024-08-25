package com.lifesparktech.lsphysio

object FogTest: Test {
    override val labels = listOf(
        "Sit to Stand Transition",
        "4m straight walk",
        "360 degree turn clockwise",
        "540 degree turn anticlockwise",
        "2 rounds in cluttered maze",
        "Passing through door",
        "(Dual task) Passing through door",
        "(Dual task) 2 rounds in cluttered maze",
        "(Dual task) 540 degree turn clockwise",
        "(Dual task) 360 degree turn anti clockwise",
        "(Dual task) 4m straight walk",
        "(Dual task) Stand to Sit Transition"
    )
    val hnyScore  = mapOf(1.0 to "Unilateral involvement only",
        1.5 to "Unilateral and axial involvement",
        2.0 to "Bilateral involvement without impairment of balance",
        2.5 to "Mild bilateral involvement with recovery on retropulsion (pull) test",
        3.0 to "Mild to moderate bilateral involvement, some postural instability but physically independent",
        4.0 to "Severe disability, still able to walk and to stand unassisted",
        5.0 to "Wheelchair bound or bedridden unless aided")
}
