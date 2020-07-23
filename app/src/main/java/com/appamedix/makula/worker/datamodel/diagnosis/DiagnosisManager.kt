package com.appamedix.makula.worker.datamodel.diagnosis

import com.appamedix.makula.scenes.diagnosis.table.DiagnosisType
import io.realm.Realm
import io.realm.RealmResults
import io.realm.Sort
import io.realm.kotlin.where

/**
 * Fetches the data list of diagnosis.
 *
 * @return The diagnosis objects or null if no exists.
 */
fun Realm.getDiagnosisObjects(): RealmResults<DiagnosisObject>? {
    val objects = this.where<DiagnosisObject>()
            .sort("sortOrderPosition", Sort.ASCENDING)
            .findAll()

    return if (objects.size > 0) objects else null
}

/**
 * Creates and adds a new diagnosis object to the database.
 *
 * @param type: The diagnosis type for the new created object.
 */
fun Realm.createDiagnosisObject(type: DiagnosisType) {
    this.beginTransaction()
    val diagnosisObject = this.createObject(DiagnosisObject::class.java, type.rawValue)
    diagnosisObject.sortOrderPosition = type.rawValue
    this.commitTransaction()
}

/**
 * Updates the `selected` flag of a diagnosis object.
 *
 * @param diagnosisObject: The object to modify.
 * @param selected: The new state.
 * @return Whether the write operation succeeded or not.
 */
fun Realm.updateDiagnosisObject(diagnosisObject: DiagnosisObject, selected: Boolean): Boolean {
    return try {
        this.beginTransaction()
        diagnosisObject.selected = selected
        this.copyToRealm(diagnosisObject)
        this.commitTransaction()
        true
    } catch (e: Exception) {
        false
    }
}