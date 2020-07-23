package com.appamedix.makula.scenes.note

import android.os.Parcel
import com.appamedix.makula.utils.KParcelable
import com.appamedix.makula.utils.parcelableCreator
import com.appamedix.makula.utils.readDate
import com.appamedix.makula.utils.writeDate
import java.util.*

data class NoteModel(val date: Date?) : KParcelable {

    private constructor(p: Parcel) : this(
            date = p.readDate()
    )

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeDate(date)
    }

    companion object {
        @JvmField val CREATOR = parcelableCreator(::NoteModel)
    }
}