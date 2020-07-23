package com.appamedix.makula.scenes.diagnosis

data class DiagnosisDisplayModel(
        // The activity for this scene.
        val activity: DiagnosisSceneActivity,
        // Whether the display should use a large style, e.g. for landscape
        // or not in which case the default style is used, e.g. portrait.
        val largeStyle: Boolean
)