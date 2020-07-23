package com.appamedix.makula.worker.datamodel.amslertest

import com.appamedix.makula.scenes.amslertest.AmslerTestProgressType
import com.appamedix.makula.utils.DateUtils
import io.realm.Realm
import io.realm.RealmResults
import io.realm.kotlin.where
import java.util.*

/**
 * Retrieves the amslertest objects (should be one at most) for a specific day.
 *
 * @param forDate: The day for which to retrieve the entry. The hours will be stripped off for the search.
 * @return The amslertest objects.
 */
fun Realm.getAmslerTestObjects(forDate: Date): RealmResults<AmslerTestObject>? {
    val startDate = DateUtils.truncateTime(forDate)
    val endDate = DateUtils.increaseDay(startDate, 1)

    val objects = this.where<AmslerTestObject>()
            .greaterThanOrEqualTo("date", startDate)
            .and()
            .lessThan("date", endDate)
            .findAll()

    return if (objects.size > 0) objects else null
}

/**
 * Creates and adds a new amslertest object to the database.
 *
 * @param date: The date for when the object gets saved.
 * @return The created object.
 */
fun Realm.createAmslerTestObject(date: Date): AmslerTestObject? {
    return try {
        this.beginTransaction()
        val amslerTestObject = this.createObject(AmslerTestObject::class.java)
        amslerTestObject.date = date
        this.commitTransaction()
        amslerTestObject
    } catch (e: Exception) {
        null
    }
}

/**
 * Deletes an amslertest object.
 *
 * @param amslerTestObject: The object to delete.
 * @return `true` if deleting is successful, otherwise `false`.
 */
fun Realm.deleteAmslerTestObject(amslerTestObject: AmslerTestObject): Boolean {
    return try {
        this.beginTransaction()
        amslerTestObject.deleteFromRealm()
        this.commitTransaction()
        true
    } catch (e: Exception) {
        false
    }
}

/**
 * Updates an existing amslertest object to set the progress for the left or right eye.
 *
 * @param amslerTestObject: The object to update.
 * @param type: The progress value for the left or right eye.
 * @param isLeft: Whether the left eye or not.
 * @return Whether writing is successful or not.
 */
fun Realm.updateAmslerTestObject(amslerTestObject: AmslerTestObject,
                                 type: AmslerTestProgressType?, isLeft: Boolean): Boolean {
    return try {
        this.beginTransaction()
        if (isLeft) {
            amslerTestObject.saveProgressTypeLeft(type)
        } else {
            amslerTestObject.saveProgressTypeRight(type)
        }
        this.copyToRealm(amslerTestObject)
        this.commitTransaction()
        true
    } catch (e: Exception) {
        false
    }
}
