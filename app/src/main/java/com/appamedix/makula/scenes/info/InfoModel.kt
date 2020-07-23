package com.appamedix.makula.scenes.info

import android.os.Parcel
import com.appamedix.makula.types.InfoType
import com.appamedix.makula.utils.KParcelable
import com.appamedix.makula.utils.parcelableCreator
import com.appamedix.makula.utils.readEnum
import com.appamedix.makula.utils.writeEnum

data class InfoModel(val sceneType: InfoType?) : KParcelable {

    private constructor(p: Parcel) : this(
            sceneType = p.readEnum<InfoType>()
    )

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeEnum(sceneType)
    }

    companion object {
        @JvmField val CREATOR = parcelableCreator(::InfoModel)
    }
}

/* The model to pass displaying this scene */
data class InfoDisplayModel(
        // The activity for this scene.
        val activity: InfoSceneActivity,
        // Whether the display should use a large style.
        val largeStyle: Boolean,
        // The scene type.
        val sceneType: InfoType
)

/* The data to hold the current content state */
class InfoContentData {
    // The scene type.
    var sceneType: InfoType? = null
    // A running state for processing the bottom button, e.g. during backup.
    var processing: Boolean = false
}