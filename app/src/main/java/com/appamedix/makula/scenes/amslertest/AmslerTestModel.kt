package com.appamedix.makula.scenes.amslertest

import com.appamedix.makula.worker.datamodel.amslertest.AmslerTestObject

/* The model to pass displaying this scene */
data class AmslerTestDisplayModel(
        // The activity for this scene.
        val activity: AmslerTestSceneActivity,
        // Whether the display should use a large style, e.g. for landscape
        // or not in which case the default style is used, e.g. portrait.
        val largeStyle: Boolean,
        // The amslertest object, null when a database error occurred.
        val amslerTestObject: AmslerTestObject?
)

/* The data to hold the current content state */
class AmslerTestContentData {
    // The persisted model object representing.
    var amslerTestObject: AmslerTestObject? = null
}