package com.appamedix.makula.scenes.graph

import android.os.Parcel
import com.appamedix.makula.utils.*
import com.appamedix.makula.worker.datamodel.histogram.NhdObject
import com.appamedix.makula.worker.datamodel.histogram.VisusObject
import java.util.*
import kotlin.collections.ArrayList

data class GraphModel(val eyeType: EyeType?,
                      val modifyNavigationStack: Boolean)
    : KParcelable {

    private constructor(p: Parcel) : this(
            eyeType = p.readEnum<EyeType>(),
            modifyNavigationStack = p.readBoolean()
    )

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeEnum(eyeType)
        writeBoolean(modifyNavigationStack)
    }

    companion object {
        @JvmField val CREATOR = parcelableCreator(::GraphModel)
    }
}

data class GraphDisplayModel(
        // The activity for this scene.
        val activity: GraphSceneActivity,
        // Whether the display should use a large style, e.g. for landscape
        // or not in which case the default style is used, e.g. portrait.
        val largeStyle: Boolean,
        // The type of eye this scene represents.
        val eyeType: EyeType,
        // The date of the last treatment appointments.
        val ivomDates: ArrayList<Date>,
        // The Visus objects to show in the graph.
        val visusObjects: ArrayList<VisusObject>,
        // The NHD objects to show in the graph.
        val nhdObjects: ArrayList<NhdObject>
)