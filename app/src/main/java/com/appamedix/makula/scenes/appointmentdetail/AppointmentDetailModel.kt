package com.appamedix.makula.scenes.appointmentdetail

import android.os.Parcel
import com.appamedix.makula.utils.KParcelable
import com.appamedix.makula.utils.parcelableCreator
import com.appamedix.makula.utils.readDate
import com.appamedix.makula.utils.writeDate
import java.util.*

data class AppointmentDetailModel(val date: Date?) : KParcelable {

    private constructor(p: Parcel) : this(
            date = p.readDate()
    )

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeDate(date)
    }

    companion object {
        @JvmField val CREATOR = parcelableCreator(::AppointmentDetailModel)
    }
}

data class AppointmentDetailDisplayModel(
        // The activity for this scene.
        val activity: AppointmentDetailSceneActivity,
        // The appointment date.
        val date: Date,
        // Whether the display should use a large style.
        val largeStyle: Boolean,
        // The nav title.
        val title: String,
        // The nav title's color.
        val titleColor: Int
)