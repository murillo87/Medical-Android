package com.appamedix.makula.views.textcell

import com.appamedix.makula.scenes.menu.table.MenuCellIdentifier
import com.appamedix.makula.types.SceneId

/* The model for the `StaticTextCell` */
class StaticTextCellModel(
        // The identifier for this cell
        val cellIdentifier: MenuCellIdentifier,
        // The scene id navigating to
        val toSceneId: SceneId,
        // The cell's title
        val cellTitle: String,
        // The cell's title for the speech
        val speechTitle: String?,
        // When true the title will use an extra large font size (e.g for landscape mode),
        // while false uses the default size (e.g. for portrait).
        val largeFont: Boolean,
        // The color for the normal mode (not selected).
        val defaultColor: Int,
        // The color for the selected state.
        val highlightColor: Int?,
        // The background color.
        val backgroundColor: Int,
        // Shows or hides the separator.
        val separatorVisible: Boolean,
        // The color of the separator for the normal mode (not selected).
        val separatorDefaultColor: Int?,
        // The color of the separator for the selected state.
        val separatorHighlightColor: Int?,
        // Whether the cell is enabled or disabled so no highlighting can occur.
        val disable: Boolean
)