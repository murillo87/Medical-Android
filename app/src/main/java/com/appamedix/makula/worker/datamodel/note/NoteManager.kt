package com.appamedix.makula.worker.datamodel.note

import com.appamedix.makula.utils.DateUtils
import io.realm.Realm
import io.realm.RealmResults
import io.realm.kotlin.where
import java.util.*

/**
 * Retrieves the note objects (should be one at most) for a specific day.
 *
 * @param forDate: The day for which to retrieve the entry. The hours will be stripped off for the search.
 * @return The note objects.
 */
fun Realm.getNoteObjects(forDate: Date): RealmResults<NoteObject>? {
    val startDate = DateUtils.truncateTime(forDate)
    val endDate = DateUtils.increaseDay(startDate, 1)

    val objects = this.where<NoteObject>()
            .greaterThanOrEqualTo("date", startDate)
            .and()
            .lessThan("date", endDate)
            .findAll()

    return if (objects.size > 0) objects else null
}

/**
 * Creates and adds a new empty note object to the database.
 *
 * @param date: The date for when the object gets saved.
 * @return The created object.
 */
fun Realm.createNoteObject(date: Date): NoteObject? {
    return try {
        this.beginTransaction()
        val noteObject = this.createObject(NoteObject::class.java)
        noteObject.date = date
        this.commitTransaction()
        noteObject
    } catch (e: Exception) {
        null
    }
}

/**
 * Creates and adds a new note object to the database.
 *
 * @param content: The content of this object.
 * @param date: The date for when the object gets saved.
 */
fun Realm.createNoteObject(content: String, date: Date) {
    this.beginTransaction()
    val noteObject = this.createObject(NoteObject::class.java)
    noteObject.content = content
    noteObject.date = date
    this.commitTransaction()
}

/**
 * Deletes a note object.
 *
 * @param noteObject: The object to delete.
 * @return `true` if deleting was successful, otherwise `false`.
 */
fun Realm.deleteNoteObject(noteObject: NoteObject): Boolean {
    return try {
        this.beginTransaction()
        noteObject.deleteFromRealm()
        this.commitTransaction()
        true
    } catch (e: Exception) {
        false
    }
}

/**
 * Updates an existing note object to set the content.
 *
 * @param noteObject: The object to update.
 * @param content: The content string.
 * @return Whether writing was successful or not.
 */
fun Realm.updateNoteObject(noteObject: NoteObject, content: String): Boolean {
    return try {
        this.beginTransaction()
        noteObject.content = content
        this.copyToRealm(noteObject)
        this.commitTransaction()
        true
    } catch (e: Exception) {
        false
    }
}