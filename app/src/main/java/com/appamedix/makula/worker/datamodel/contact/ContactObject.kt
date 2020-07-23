package com.appamedix.makula.worker.datamodel.contact

import com.appamedix.makula.types.ContactType
import io.realm.RealmObject
import io.realm.annotations.Index
import io.realm.annotations.Required
import java.util.*

/* A single entry of the contact */
open class ContactObject : RealmObject() {
    // The type of this contact.
    private var type: Int = ContactType.Custom.rawValue

    // The name of the contact.
    var name: String? = null

    // The mobile number for sending an SMS.
    var mobile: String? = null

    // The phone number for starting a phone call.
    var phone: String? = null

    // The email address to send an email.
    var email: String? = null

    // The internet web address.
    var web: String? = null

    // The street address of the contact.
    var street: String? = null

    // The city.
    var city: String? = null

    // The date when this entry was created.
    @Required @Index
    var creationDate: Date = Date()

    /**
     * Save an ContactType value as string.
     *
     * @param type: The contact type value.
     */
    fun saveContactType(type: ContactType) {
        this.type = type.rawValue
    }

    /**
     * Returns the contact type of this object.
     *
     * @return The contact type.
     */
    fun getContactType(): ContactType = ContactType.from(type)
}