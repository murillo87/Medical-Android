package com.appamedix.makula.scenes.menu

import android.os.Parcel
import com.appamedix.makula.types.SceneId
import com.appamedix.makula.utils.*

data class MenuModel(val sceneId: SceneId?) : KParcelable {

    private constructor(p: Parcel) : this(
            sceneId = p.readEnum<SceneId>()
    )

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeEnum(sceneId)
    }

    companion object {
        @JvmField val CREATOR = parcelableCreator(::MenuModel)
    }
}

data class MenuDisplayModel(
        // The activity for this scene.
        val activity: MenuSceneActivity,
        // Whether the display should use a large style, e.g. for landscape
        // or not in which case the default style is used, e.g. portrait.
        val largeStyle: Boolean,
        // The scene ID of this menu scene to differentiate the content to display.
        val sceneId: SceneId
)

