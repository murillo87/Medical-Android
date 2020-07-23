package com.appamedix.makula.worker.datamodel.note

import io.realm.RealmObject
import io.realm.annotations.Index
import io.realm.annotations.Required
import java.util.*

/* A single entry of the note */
open class NoteObject : RealmObject() {
    // The content of the note.
    var content: String? = null

    // The date when this entry was created.
    @Required @Index
    var date: Date = Date()
}