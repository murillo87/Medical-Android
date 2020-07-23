package com.appamedix.makula.worker.datamodel.histogram

import com.appamedix.makula.constants.Const
import com.appamedix.makula.utils.DateUtils
import io.realm.Realm
import io.realm.RealmResults
import io.realm.Sort
import io.realm.kotlin.where
import java.util.*

/**
 * Retrieves the visus objects for a specific day (should be one at most).
 *
 * @param forDate: The day for which to retrieve the entries. The hours will be stripped off for the search.
 * @return The visus objects.
 */
fun Realm.getVisusObjects(forDate: Date): RealmResults<VisusObject>? {
    val startDate = DateUtils.truncateTime(forDate)
    val endDate = DateUtils.increaseDay(startDate, 1)

    return getVisusObjects(startDate, endDate)
}

/**
 * Retrieves all visus objects within a time period.
 *
 * @param fromDate: The earliest date to find objects for (inclusive).
 * @param toDate: The latest date to find objects for (exclusive).
 * @return The visus objects.
 */
fun Realm.getVisusObjects(fromDate: Date, toDate: Date): RealmResults<VisusObject>? {
    val objects = this.where<VisusObject>()
            .greaterThanOrEqualTo("date", fromDate)
            .and()
            .lessThan("date", toDate)
            .sort("date", Sort.ASCENDING)
            .findAll()

    return if (objects.size > 0) objects else null
}

/**
 * Retrieves the NHD objects for a specific day.
 *
 * @param forDate: The day for which to retrieve the entries. The hours will be stripped off for the search.
 * @return The NHD objects.
 */
fun Realm.getNhdObjects(forDate: Date): RealmResults<NhdObject>? {
    val startDate = DateUtils.truncateTime(forDate)
    val endDate = DateUtils.increaseDay(startDate, 1)

    return getNhdObjects(startDate, endDate)
}

/**
 * Retrieves all NHD objects within a time period.
 *
 * @param fromDate: The earliest date to find objects for (inclusive).
 * @param toDate: The latest date to find objects for (exclusive).
 * @return The NHD objects.
 */
fun Realm.getNhdObjects(fromDate: Date, toDate: Date): RealmResults<NhdObject>? {
    val objects = this.where<NhdObject>()
            .greaterThanOrEqualTo("date", fromDate)
            .and()
            .lessThan("date", toDate)
            .sort("date", Sort.ASCENDING)
            .findAll()

    return if (objects.size > 0) objects else null
}

/**
 * Creates and adds a new visus object to the database.
 *
 * @param date: The date for when the object gets saved.
 * @param valueLeft: The index value for the left eye.
 * @param valueRight: The index value for the right eye.
 */
fun Realm.createVisusObject(date: Date, valueLeft: Int, valueRight: Int) {
    assert(valueLeft >= Const.Data.visusMinValue && valueLeft <= Const.Data.visusMaxValue)
    assert(valueRight >= Const.Data.visusMinValue && valueRight <= Const.Data.visusMaxValue)

    // Create an object.
    this.beginTransaction()
    val visusObject = this.createObject(VisusObject::class.java)
    visusObject.date = date
    visusObject.valueLeft = valueLeft
    visusObject.valueRight = valueRight
    this.commitTransaction()
}

/**
 * Creates and adds a new NHD object to the database.
 *
 * @param date: The date for when the object gets saved.
 * @param valueLeft: The value for the left eye.
 * @param valueRight: The value for the right eye.
 */
fun Realm.createNhdObject(date: Date, valueLeft: Int, valueRight: Int) {
    assert(valueLeft >= Const.Data.nhdMinValue && valueLeft <= Const.Data.nhdMaxValue)
    assert(valueRight >= Const.Data.nhdMinValue && valueRight <= Const.Data.nhdMaxValue)

    // Create an object.
    this.beginTransaction()
    val nhdObject = this.createObject(NhdObject::class.java)
    nhdObject.date = date
    nhdObject.valueLeft = valueLeft.toFloat()
    nhdObject.valueRight = valueRight.toFloat()
    this.commitTransaction()
}

/**
 * Updates an existing visus object.
 *
 * @param visusObject: The object to update.
 * @param date: The new date to save.
 * @param valueLeft: The index value for the left eye.
 * @param valueRight: The index value for the right eye.
 * @return Whether writing was successful or not.
 */
fun Realm.updateVisusObject(visusObject: VisusObject, date: Date, valueLeft: Int, valueRight: Int): Boolean {
    assert(valueLeft >= Const.Data.visusMinValue && valueLeft <= Const.Data.visusMaxValue)
    assert(valueRight >= Const.Data.visusMinValue && valueRight <= Const.Data.visusMaxValue)

    return try {
        this.beginTransaction()
        visusObject.date = date
        visusObject.valueLeft = valueLeft
        visusObject.valueRight = valueRight
        this.copyToRealm(visusObject)
        this.commitTransaction()
        true
    } catch (e: Exception) {
        e.printStackTrace()
        false
    }
}

/**
 * Updates an existing NHD object.
 *
 * @param nhdObject: The object to update.
 * @param date: The new date to save.
 * @param valueLeft: The index value for the left eye.
 * @param valueRight: The index value for the right eye.
 * @return Whether writing was successful or not.
 */
fun Realm.updateNhdObject(nhdObject: NhdObject, date: Date, valueLeft: Int, valueRight: Int): Boolean {
    assert(valueLeft >= Const.Data.nhdMinValue && valueLeft <= Const.Data.nhdMaxValue)
    assert(valueRight >= Const.Data.nhdMinValue && valueRight <= Const.Data.nhdMaxValue)

    return try {
        this.beginTransaction()
        nhdObject.date = date
        nhdObject.valueLeft = valueLeft.toFloat()
        nhdObject.valueRight = valueRight.toFloat()
        this.copyToRealm(nhdObject)
        this.commitTransaction()
        true
    } catch (e: Exception) {
        e.printStackTrace()
        false
    }
}