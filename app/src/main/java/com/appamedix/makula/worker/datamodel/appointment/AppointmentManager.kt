package com.appamedix.makula.worker.datamodel.appointment

import com.appamedix.makula.types.AppointmentType
import com.appamedix.makula.utils.DateUtils
import com.appamedix.makula.worker.datamodel.amslertest.AmslerTestObject
import com.appamedix.makula.worker.datamodel.histogram.NhdObject
import com.appamedix.makula.worker.datamodel.histogram.VisusObject
import com.appamedix.makula.worker.datamodel.note.NoteObject
import com.appamedix.makula.worker.datamodel.readingtest.ReadingTestObject
import io.realm.Realm
import io.realm.RealmResults
import io.realm.Sort
import io.realm.kotlin.where
import java.util.*
import kotlin.collections.ArrayList

/**
 * Retrieves the last appointment objects of the type `treatment`.
 * @param upToDate: The latest date for a valid appointment to find.
 * @return The appointment objects up until the date.
 */
fun Realm.getLastTreatmentObjects(upToDate: Date): ArrayList<AppointmentObject> {
    val dateTruncated = DateUtils.truncateTime(upToDate)
    val endDate = DateUtils.increaseDay(dateTruncated, 1)

    val objects = this.where<AppointmentObject>()
            .equalTo("type", AppointmentType.Treatment.rawValue)
            .and()
            .lessThan("date", endDate)
            .sort("date", Sort.DESCENDING)
            .findAll()

    return ArrayList(objects)
}

/**
 * Retrieves the last appointments in the past.
 * Only appointments of the type `treatment`, `aftercare` and `octCheck` are taken into count.
 * All such appointments on the same day are returned.
 *
 * @param upToDate: The end date up to which to look for appointments (inclusive).
 * @return The appointment on the last day in the past which has appointments.
 */
fun Realm.getLastAppointments(upToDate: Date): ArrayList<AppointmentObject> {
    val lastDate = getDateOfLastAppointment(upToDate) ?: return ArrayList()
    return getAppointments(lastDate)
}

/**
 * Retrieves the date on which the last appointment of the type `treatment`, `aftercare` and `octCheck` was.
 * @param upToDate: The end date up to which to look for appointments (inclusive).
 * @return The last date which has at least one appointment.
 */
private fun Realm.getDateOfLastAppointment(upToDate: Date): Date? {
    val dateTruncated = DateUtils.truncateTime(upToDate)
    val endDate = DateUtils.increaseDay(dateTruncated, 1)

    val objects = this.where<AppointmentObject>()
            .notEqualTo("type", AppointmentType.Other.rawValue)
            .and()
            .lessThan("date", endDate)
            .sort("date", Sort.DESCENDING)
            .findAll()

    return when {
        objects.size > 0 -> objects.first()?.getAppointmentDate()
        else -> null
    }
}

/**
 * Retrieves all appointments of the type `treatement`, `aftercare` and `octCheck` for a given day.
 * @param forDate: The day to look for appointments.
 * @return All appointments matching the type and date.
 */
private fun Realm.getAppointments(forDate: Date): ArrayList<AppointmentObject> {
    val startDate = DateUtils.truncateTime(forDate)
    val endDate = DateUtils.increaseDay(startDate, 1)

    val objects = this.where<AppointmentObject>()
            .notEqualTo("type", AppointmentType.Other.rawValue)
            .and()
            .greaterThanOrEqualTo("date", startDate)
            .and()
            .lessThan("date", endDate)
            .sort("date", Sort.DESCENDING)
            .findAll()

    return ArrayList(objects)
}

/**
 * Retrieves the appointment objects for a specific day.
 *
 * @param forDate: The day for which to retrieve the entries. The hours will be stripped off for search.
 * @return The appointment objects.
 */
fun Realm.getAppointmentObjects(forDate: Date): RealmResults<AppointmentObject> {
    val startDate = DateUtils.truncateTime(forDate)
    val endDate = DateUtils.increaseDay(startDate, 1)

    val objects = this.where<AppointmentObject>()
            .greaterThanOrEqualTo("date", startDate)
            .and()
            .lessThan("date", endDate)

    return objects.findAll()
}

/**
 * Retrieves the appointment objects of a specific type for a given day.
 *
 * @param forDate: The day for which to retrieve the entries. The hours will be stripped off for the search.
 * @param type: The type of appointment to find.
 * @return The appointment objects.
 */
fun Realm.getAppointmentObjects(forDate: Date, type: AppointmentType): RealmResults<AppointmentObject> {
    val startDate = DateUtils.truncateTime(forDate)
    val endDate = DateUtils.increaseDay(startDate, 1)

    val objects = this.where<AppointmentObject>()
            .equalTo("type", type.rawValue)
            .and()
            .greaterThanOrEqualTo("date", startDate)
            .and()
            .lessThan("date", endDate)

    return objects.findAll()
}

/**
 * Retrieves all appointments in the future after a given date.
 *
 * @param date: The current date from which on to find all appointments.
 * @return The appointment objects.
 */
fun Realm.getAppointmentsAfter(date: Date): RealmResults<AppointmentObject>? {
    val objects = this.where<AppointmentObject>()
            .greaterThan("date", date)
            .sort("date", Sort.DESCENDING)
            .findAll()

    return if (objects.size > 0) objects else null
}

/**
 * Creates and adds a new appointment object to the database.
 *
 * @param type: The type of appointment to save.
 * @param date: The date of the appointment.
 */
fun Realm.createAppointmentObject(type: AppointmentType, date: Date) {
    // Save model to database.
    this.beginTransaction()
    val appointmentObject = this.createObject(AppointmentObject::class.java)
    appointmentObject.saveAppointmentType(type)
    appointmentObject.saveAppointmentDate(date)
    this.commitTransaction()
}

/**
 * Updates the `appointmentDate` property of an appointment object.
 *
 * @param appointmentObject: The appointment object to modify.
 * @param date: The new date.
 * @return Whether the write operation succeeded or not.
 */
fun Realm.setAppointmentModel(appointmentObject: AppointmentObject, date: Date): Boolean {
    return try {
        this.beginTransaction()
        appointmentObject.saveAppointmentDate(date)
        this.copyToRealm(appointmentObject)
        this.commitTransaction()
        true
    } catch (e: Exception) {
        false
    }
}

/**
 * Deletes all data related to the specific day,
 * which are all appointments and all associated data inclusive the note.
 *
 * @param date: The date on which to delete all data.
 * @return Whether the delete operation succeeded or not.
 */
fun Realm.deleteData(date: Date): Boolean {
    val startDate = DateUtils.truncateTime(date)
    val endDate = DateUtils.increaseDay(startDate, 1)

    val appointments = this.where<AppointmentObject>()
            .greaterThanOrEqualTo("date", startDate)
            .and()
            .lessThan("date", endDate)
            .findAll()
    val amslerTests = this.where<AmslerTestObject>()
            .greaterThanOrEqualTo("date", startDate)
            .and()
            .lessThan("date", endDate)
            .findAll()
    val readingTests = this.where<ReadingTestObject>()
            .greaterThanOrEqualTo("date", startDate)
            .and()
            .lessThan("date", endDate)
            .findAll()
    val visus = this.where<VisusObject>()
            .greaterThanOrEqualTo("date", startDate)
            .and()
            .lessThan("date", endDate)
            .findAll()
    val nhd = this.where<NhdObject>()
            .greaterThanOrEqualTo("date", startDate)
            .and()
            .lessThan("date", endDate)
            .findAll()
    val notes = this.where<NoteObject>()
            .greaterThanOrEqualTo("date", startDate)
            .and()
            .lessThan("date", endDate)
            .findAll()

    return try {
        this.beginTransaction()
        appointments.deleteAllFromRealm()
        amslerTests.deleteAllFromRealm()
        readingTests.deleteAllFromRealm()
        visus.deleteAllFromRealm()
        nhd.deleteAllFromRealm()
        notes.deleteAllFromRealm()
        this.commitTransaction()
        true
    } catch (e: Exception) {
        false
    }
}