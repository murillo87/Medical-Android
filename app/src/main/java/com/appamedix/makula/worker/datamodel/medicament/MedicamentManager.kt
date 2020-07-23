package com.appamedix.makula.worker.datamodel.medicament

import io.realm.Realm
import io.realm.RealmResults
import io.realm.Sort
import io.realm.kotlin.where
import java.util.*

/**
 * Fetches the data list of medicament.
 *
 * @return The medicament objects or null if no exists.
 */
fun Realm.getMedicamentObjects(): RealmResults<MedicamentObject>? {
    val objects = this.where<MedicamentObject>()
            .sort("creationDate", Sort.ASCENDING)
            .findAll()

    return if (objects.size > 0) objects else null
}

/**
 * Creates and adds a new medicament object to the database.
 *
 * @param name: The name of the medicament to save.
 * @return The new created object.
 */
fun Realm.createMedicamentObject(name: String): MedicamentObject? {
    return try {
        this.beginTransaction()
        val medicamentObject = this.createObject(MedicamentObject::class.java)
        medicamentObject.name = name
        medicamentObject.editable = true
        medicamentObject.selected = true
        this.commitTransaction()
        medicamentObject
    } catch (e: Exception) {
        null
    }
}

/**
 * Creates and adds a new medicament object to the database.
 *
 * @param name: The name of the medicament to save.
 * @param date: The date this entry was created.
 * @param editable: Whether this entry is editable or not.
 */
fun Realm.createMedicamentObject(name: String, date: Date, editable: Boolean) {
    this.beginTransaction()
    val medicamentObject = this.createObject(MedicamentObject::class.java)
    medicamentObject.name = name
    medicamentObject.editable = editable
    medicamentObject.creationDate = date
    this.commitTransaction()
}

/**
 * Deletes a medicament object.
 *
 * @param medicamentObject: The object to delete.
 * @return `true` if deleting was successful, otherwise `false`.
 */
fun Realm.deleteMedicamentObject(medicamentObject: MedicamentObject): Boolean {
    return try {
        this.beginTransaction()
        medicamentObject.deleteFromRealm()
        this.commitTransaction()
        true
    } catch (e: Exception) {
        false
    }
}

/**
 * Updates the `selected` flag of a medicament object.
 *
 * @param medicamentObject: The object to modify.
 * @param selected: The new state.
 * @return Whether the write operation succeeded or not.
 */
fun Realm.updateMedicamentObject(medicamentObject: MedicamentObject, selected: Boolean): Boolean {
    return try {
        this.beginTransaction()
        medicamentObject.selected = selected
        this.copyToRealm(medicamentObject)
        this.commitTransaction()
        true
    } catch (e: Exception) {
        false
    }
}
