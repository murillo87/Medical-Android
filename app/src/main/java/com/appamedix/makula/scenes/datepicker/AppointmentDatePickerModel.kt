package com.appamedix.makula.scenes.datepicker

import android.os.Parcel
import com.appamedix.makula.types.AppointmentType
import com.appamedix.makula.utils.*

data class AppointmentDatePickerModel(val appointmentType: AppointmentType?) : KParcelable {

    private constructor(p: Parcel) : this(
            appointmentType = p.readEnum<AppointmentType>()
    )

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeEnum(appointmentType)
    }

    companion object {
        @JvmField val CREATOR = parcelableCreator(::AppointmentDatePickerModel)
    }
}