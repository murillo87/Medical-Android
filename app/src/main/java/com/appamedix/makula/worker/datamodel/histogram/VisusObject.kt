package com.appamedix.makula.worker.datamodel.histogram

import io.realm.RealmObject
import io.realm.annotations.Index
import io.realm.annotations.Required
import java.util.*

/* A model for a visus entry in the histogram */
open class VisusObject : RealmObject() {
    // The date when this entry was measured.
    // Make sure it's unique based on the day.
    @Required @Index
    var date: Date = Date()

    // The index of the measured value for the left eye (0 to 12).
    var valueLeft: Int = 0

    // The index of the measured value for the right eye (0 to 12).
    var valueRight: Int = 0
}