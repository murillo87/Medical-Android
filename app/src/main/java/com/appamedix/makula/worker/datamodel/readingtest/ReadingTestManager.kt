package com.appamedix.makula.worker.datamodel.readingtest

import com.appamedix.makula.scenes.readingtest.ReadingTestMagnitudeType
import com.appamedix.makula.utils.DateUtils
import io.realm.Realm
import io.realm.RealmResults
import io.realm.kotlin.where
import java.util.*

/**
 * Retrieves the reading test objects (should be one at most) for a specific day.
 *
 * @param forDate: The day for which to retrieve the entry. The hours will be stripped off for the search.
 * @return The reading test objects.
 */
fun Realm.getReadingTestObjects(forDate: Date): RealmResults<ReadingTestObject>? {
    val startDate = DateUtils.truncateTime(forDate)
    val endDate = DateUtils.increaseDay(startDate, 1)

    val objects = this.where<ReadingTestObject>()
            .greaterThanOrEqualTo("date", startDate)
            .and()
            .lessThan("date", endDate)
            .findAll()

    return if (objects.size > 0) objects else null
}

/**
 * Creates and adds a new reading test object to the database.
 *
 * @param date: The date for when the object gets saved.
 * @return The created object.
 */
fun Realm.createReadingTestObject(date: Date): ReadingTestObject? {
    return try {
        this.beginTransaction()
        val readingTestObject = this.createObject(ReadingTestObject::class.java)
        readingTestObject.date = date
        this.commitTransaction()
        readingTestObject
    } catch (e: Exception) {
        null
    }
}

/**
 * Deletes a reading test object.
 *
 * @param readingTestObject: The object to delete.
 * @return `true` if deleting was successful, otherwise `false`.
 */
fun Realm.deleteReadingTestObject(readingTestObject: ReadingTestObject): Boolean {
    return try {
        this.beginTransaction()
        readingTestObject.deleteFromRealm()
        this.commitTransaction()
        true
    } catch (e: Exception) {
        false
    }
}

/**
 * Updates an existing reading test object to set the magnitude for the left or right eye.
 *
 * @param readingTestObject: The object to update.
 * @param type: The magnitude value for the left or right eye.
 * @param isLeft: Whether the left eye or not.
 * @return Whether writing is successful or not.
 */
fun Realm.updateReadingTestObject(readingTestObject: ReadingTestObject,
                                  type: ReadingTestMagnitudeType?, isLeft: Boolean): Boolean {
    return try {
        this.beginTransaction()
        if (isLeft) {
            readingTestObject.saveMagnitudeTypeLeft(type)
        } else {
            readingTestObject.saveMagnitudeTypeRight(type)
        }
        this.copyToRealm(readingTestObject)
        this.commitTransaction()
        true
    } catch (e: Exception) {
        false
    }
}