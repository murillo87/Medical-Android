package com.appamedix.makula.scenes.contactdetail

import android.os.Parcel
import com.appamedix.makula.utils.KParcelable
import com.appamedix.makula.utils.parcelableCreator
import com.appamedix.makula.utils.readDate
import com.appamedix.makula.utils.writeDate
import com.appamedix.makula.worker.datamodel.contact.ContactObject
import java.util.*

data class ContactDetailModel(val createDate: Date?) : KParcelable {

    private constructor(p: Parcel) : this(
            createDate = p.readDate()
    )

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeDate(createDate)
    }

    companion object {
        @JvmField val CREATOR = parcelableCreator(::ContactDetailModel)
    }
}

data class ContactDetailDisplayModel(
        // The activity for this scene.
        val activity: ContactDetailSceneActivity,
        // Whether the display should use a large style or not.
        val largeStyle: Boolean,
        // The contact object to show.
        val contactObject: ContactObject
)