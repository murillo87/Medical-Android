package com.appamedix.makula.views.textcell

import com.appamedix.makula.R
import com.appamedix.makula.base.BaseCellViewModel
import com.appamedix.makula.scenes.menu.table.MenuCellIdentifier
import com.appamedix.makula.types.SceneId
import com.appamedix.makula.types.ViewCellType

/* The view model for the `StaticTextCell` */
class StaticTextCellViewModel(model: StaticTextCellModel)
    : BaseCellViewModel(ViewCellType.StaticTextCell) {
    // The identifier for this cell
    var cellIdentifier: MenuCellIdentifier = MenuCellIdentifier.NoMenu
    // The scene id navigating to
    var toSceneId: SceneId = SceneId.Other
    // The cell's title
    var cellTitle: String = ""
    // The cell's title for the speech
    var speechTitle: String? = null
    // When true the title will use an extra large font size (e.g for landscape mode),
    // while false uses the default size (e.g. for portrait).
    var largeFont: Boolean = false
    // The color for the normal mode (not selected).
    var defaultColor: Int = R.color.lightMain
    // The color for the selected state.
    var highlightColor: Int? = null
    // The background color.
    var backgroundColor: Int = R.color.darkMain
    // Shows or hides the separator.
    var separatorVisible: Boolean = true
    // The color of the separator for the normal mode (not selected).
    var separatorDefaultColor: Int? = null
    // The color of the separator for the selected state.
    var separatorHighlightColor: Int? = null
    // Whether the cell is enabled or disabled so no highlighting can occur.
    var disable: Boolean = false

    init {
        this.cellIdentifier = model.cellIdentifier
        this.toSceneId = model.toSceneId
        this.cellTitle = model.cellTitle
        this.speechTitle = model.speechTitle
        this.largeFont = model.largeFont
        this.defaultColor = model.defaultColor
        this.highlightColor = model.highlightColor
        this.backgroundColor = model.backgroundColor
        this.separatorVisible = model.separatorVisible
        this.separatorDefaultColor = model.separatorDefaultColor
        this.separatorHighlightColor = model.separatorHighlightColor
        this.disable = model.disable
    }
}