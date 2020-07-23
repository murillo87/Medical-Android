package com.appamedix.makula.worker.datamodel.histogram

import io.realm.RealmObject
import io.realm.annotations.Index
import io.realm.annotations.Required
import java.util.*

/* A model for a NHD entry in the histogram */
open class NhdObject : RealmObject() {
    // The date when this entry was measured.
    // Make sure it's unique based on the day.
    @Required @Index
    var date: Date = Date()

    // The measured value for the left eye.
    var valueLeft: Float = 0.0f

    // The measured value for the right eye.
    var valueRight: Float = 0.0f
}