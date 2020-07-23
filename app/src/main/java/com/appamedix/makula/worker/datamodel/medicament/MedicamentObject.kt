package com.appamedix.makula.worker.datamodel.medicament

import io.realm.RealmObject
import io.realm.annotations.Index
import io.realm.annotations.Required
import java.util.*

/* A single entry of the medicament */
open class MedicamentObject : RealmObject() {
    // The name of the medicament.
    @Required
    var name: String = ""

    // Whether this entry is editable or not.
    var editable: Boolean = false

    // Whether the user has selected this entry or not.
    var selected: Boolean = false

    // The date when this entry was created.
    @Required @Index
    var creationDate: Date = Date()
}