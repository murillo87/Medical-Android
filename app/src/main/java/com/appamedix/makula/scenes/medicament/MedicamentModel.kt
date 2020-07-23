package com.appamedix.makula.scenes.medicament

data class MedicamentDiaplayModel(
        // The activity for this scene.
        val activity: MedicamentSceneActivity,
        // Whether the display should use a large style, e.g. for landscape
        // or not in which case the default style is used, e.g. portrait.
        val largeStyle: Boolean
)