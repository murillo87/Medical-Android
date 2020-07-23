package com.appamedix.makula.worker.datamodel.readingtest

import com.appamedix.makula.scenes.readingtest.ReadingTestMagnitudeType
import io.realm.RealmObject
import io.realm.annotations.Index
import io.realm.annotations.Required
import java.util.*

/* A single entry of the reading test */
open class ReadingTestObject : RealmObject() {
    // The measured value for the left eye.
    private var magnitudeLeftValue: Int? = null

    // The measured value for the right eye.
    private var magnitudeRightValue: Int? = null

    // The date when this entry was measured.
    @Required @Index
    var date: Date = Date()

    /**
     * Save an reading test magnitude type value for the left eye.
     *
     * @param type: The reading test magnitude type.
     */
    fun saveMagnitudeTypeLeft(type: ReadingTestMagnitudeType?) {
        magnitudeLeftValue = type?.rawValue
    }

    /**
     * Returns the reading test magnitude type for the left eye.
     *
     * @return The reading test magnitude type if it was set, otherwise null.
     */
    fun getMagnitudeTypeLeft(): ReadingTestMagnitudeType? {
        val value = magnitudeLeftValue
        if (value != null) {
            return ReadingTestMagnitudeType.from(value)
        }
        return null
    }

    /**
     * Save an reading test magnitude type value for the right eye.
     *
     * @param type: The reading test magnitude type.
     */
    fun saveMagnitudeTypeRight(type: ReadingTestMagnitudeType?) {
        magnitudeRightValue = type?.rawValue
    }

    /**
     * Returns the reading test magnitude type for the right eye.
     *
     * @return The reading test magnitude type if it was set, otherwise null.
     */
    fun getMagnitudeTypeRight(): ReadingTestMagnitudeType? {
        val value = magnitudeRightValue
        if (value != null) {
            return ReadingTestMagnitudeType.from(value)
        }
        return null
    }
}