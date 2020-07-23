package com.appamedix.makula.scenes.contact

import com.appamedix.makula.worker.datamodel.contact.ContactObject
import io.realm.RealmResults

data class ContactDisplayModel(
        // The activity for this scene.
        val activity: ContactSceneActivity,
        // Whether the display should use a large style.
        val largeStyle: Boolean,
        // The contact list.
        val contactObjects: RealmResults<ContactObject>
)