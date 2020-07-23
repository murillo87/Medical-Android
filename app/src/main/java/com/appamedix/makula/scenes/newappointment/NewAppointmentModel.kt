package com.appamedix.makula.scenes.newappointment

data class NewAppointmentDisplayModel(
        // The activity for this scene.
        val activity: NewAppointmentSceneActivity,
        // Whether the display should use a large style, e.g. for landscape
        // or not in which case the default style is used, e.g. portrait.
        val largeStyle: Boolean
)