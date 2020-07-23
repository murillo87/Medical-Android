package com.appamedix.makula.scenes.readingtest

import com.appamedix.makula.worker.datamodel.readingtest.ReadingTestObject

/* The model to pass displaying this scene */
data class ReadingTestDisplayModel(
        // The activity for this scene.
        val activity: ReadingTestSceneActivity,
        // Whether the display should use a large style, e.g. for landscape
        // or not in which case the default style is used, e.g. portrait.
        val largeStyle: Boolean,
        // The reading test object, nil when a database error occurred.
        val readingTestObject: ReadingTestObject?
)

/* The data to hold the current content state */
class ReadingTestContentData {
    // The persisted model object representing.
    var readingTestObject: ReadingTestObject? = null
}