package com.appamedix.makula.scenes.visusnhd

import android.os.Parcel
import com.appamedix.makula.types.VisusNhdType
import com.appamedix.makula.utils.KParcelable
import com.appamedix.makula.utils.parcelableCreator
import com.appamedix.makula.utils.readEnum
import com.appamedix.makula.utils.writeEnum

data class VisusNhdInputModel(val sceneType: VisusNhdType?) : KParcelable {
    private constructor(p: Parcel) : this(
            sceneType = p.readEnum<VisusNhdType>()
    )

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeEnum(sceneType)
    }

    companion object {
        @JvmField val CREATOR = parcelableCreator(::VisusNhdInputModel)
    }
}

/* The model to pass displaying this scene */
data class VisusNhdInputDisplayModel(
        // The activity for this scene.
        var activity: VisusNhdInputSceneActivity,
        // Whether the display should use a large style, e.g. for landscape or
        // not in which case the default style is used, e.g. portrait.
        var largeStyle: Boolean,
        // The scene type.
        var sceneType: VisusNhdType,
        // Whether the left side is selected.
        var leftSelected: Boolean,
        // Whether the right side is selected.
        var rightSelected: Boolean,
        // The value for the left side.
        var leftValue: Int?,
        // The value for the right side.
        var rightValue: Int?
)

/* The data to hold the current content state */
class VisusNhdInputContentData {
    // The scene type.
    var sceneType: VisusNhdType? = null
    // Whether the left side is selected.
    var leftSelected: Boolean = false
    // Whether the right side is selected.
    var rightSelected: Boolean = false
    // The value for the left eye.
    var leftValue: Int? = null
    // The value for the right eye.
    var rightValue: Int? = null
}