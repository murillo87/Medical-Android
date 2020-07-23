package com.appamedix.makula.worker.datamodel.amslertest

import com.appamedix.makula.scenes.amslertest.AmslerTestProgressType
import io.realm.RealmObject
import io.realm.annotations.Index
import io.realm.annotations.Required
import java.util.*

/* A single entry of the amslertest */
open class AmslerTestObject : RealmObject() {
    // The measured value for the left eye.
    private var progressLeftValue: Int? = null

    // The measured value for the right eye.
    private var progressRightValue: Int? = null

    // The date when this entry was measured.
    @Required @Index
    var date: Date = Date()

    /**
     * Save an amslertest progress type value for the left eye.
     *
     * @param type: The amslertest progress type.
     */
    fun saveProgressTypeLeft(type: AmslerTestProgressType?) {
        progressLeftValue = type?.rawValue
    }

    /**
     * Returns the amslertest progress type for the left eye.
     *
     * @return The amslertest progress type if it was set, otherwise null.
     */
    fun getProgressTypeLeft(): AmslerTestProgressType? {
        val value = progressLeftValue
        if (value != null) {
            return AmslerTestProgressType.from(value)
        }
        return null
    }

    /**
     * Save an amslertest progress type value for the right eye.
     *
     * @param type: The amslertest progress type.
     */
    fun saveProgressTypeRight(type: AmslerTestProgressType?) {
        progressRightValue = type?.rawValue
    }

    /**
     * Returns the amslertest progress type for the right eye.
     *
     * @return The amslertest progress type if it was set, otherwise null.
     */
    fun getProgressTypeRight(): AmslerTestProgressType? {
        val value = progressRightValue
        if (value != null) {
            return AmslerTestProgressType.from(value)
        }
        return null
    }
}