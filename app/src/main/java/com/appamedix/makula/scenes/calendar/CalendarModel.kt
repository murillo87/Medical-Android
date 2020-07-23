package com.appamedix.makula.scenes.calendar

import android.os.Parcel
import com.appamedix.makula.utils.*
import java.util.*

data class CalendarModel(val focusDate: Date?, val modifyNavigationStack: Boolean) : KParcelable {

    private constructor(p: Parcel) : this(
            focusDate = p.readDate(),
            modifyNavigationStack = p.readBoolean()
    )

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeDate(focusDate)
        writeBoolean(modifyNavigationStack)
    }

    companion object {
        @JvmField val CREATOR = parcelableCreator(::CalendarModel)
    }
}