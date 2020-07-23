package com.appamedix.makula.worker.datamodel.contact

import com.appamedix.makula.scenes.contactdetail.ContactInfoType
import com.appamedix.makula.types.ContactType
import io.realm.Realm
import io.realm.RealmResults
import io.realm.Sort
import io.realm.kotlin.where
import java.util.*

/**
 * Fetches the data list for the contact scene.
 *
 * @return The contact objects.
 */
fun Realm.getContactObjects(): RealmResults<ContactObject> {
   val objects = this.where<ContactObject>()
            .sort("creationDate", Sort.ASCENDING)

    return objects.findAll()
}

/**
 * Fetches the contact object with given id.
 *
 * @param atDate: The contact date to fetch.
 * @return A contact object.
 */
fun Realm.getContactObject(atDate: Date): ContactObject? {
    val objects = this.where<ContactObject>()
            .equalTo("creationDate", atDate)
            .findAll()

    return if (objects.size > 0) objects.first() else null
}

/**
 * Creates and adds a new empty contact object to the database.
 *
 * @return The newly created entry or null if an error occurred.
 */
fun Realm.createEmptyContactObject(): ContactObject? {
    return try {
        this.beginTransaction()
        val contactObject = this.createObject(ContactObject::class.java)
        contactObject.saveContactType(ContactType.Custom)
        this.commitTransaction()
        contactObject
    } catch (e: Exception) {
        null
    }
}

/**
 * Deletes a contact object.
 *
 * @param contactObject: The contact object to delete.
 * @return `true` if deleting was successful, otherwise `false`.
 */
fun Realm.deleteContactObject(contactObject: ContactObject): Boolean {
    return try {
        this.beginTransaction()
        contactObject.deleteFromRealm()
        this.commitTransaction()
        true
    } catch (e: Exception) {
        false
    }
}

/**
 * Updates an existing contact object.
 *
 * @param contactObject: The contact object to update.
 * @param infoType: The type of the contact object property to update its value.
 * @param content: The contact object property's content to set.
 * @return Whether writing was successful or not.
 */
fun Realm.updateContactObject(contactObject: ContactObject,
                              infoType: ContactInfoType, content: String?): Boolean {
    return try {
        this.beginTransaction()
        when (infoType) {
            ContactInfoType.Name -> contactObject.name = content
            ContactInfoType.Mobile -> contactObject.mobile = content
            ContactInfoType.Phone -> contactObject.phone = content
            ContactInfoType.Email -> contactObject.email = content
            ContactInfoType.Web -> contactObject.web = content
            ContactInfoType.Street -> contactObject.street = content
            ContactInfoType.City -> contactObject.city = content
        }
        this.copyToRealm(contactObject)
        this.commitTransaction()
        true
    } catch (e: Exception) {
        false
    }
}