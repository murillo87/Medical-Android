package com.appamedix.makula.views.splitcell

import com.appamedix.makula.R
import com.appamedix.makula.base.BaseCellViewModel
import com.appamedix.makula.types.ViewCellType

/* The view model for the `SplitCell` */
class SplitCellViewModel(model: SplitCellModel) : BaseCellViewModel(ViewCellType.SplitCell) {
    // The title string for the left button.
    var leftTitle: String = ""
    // The title string for the right button.
    var rightTitle: String = ""
    // The cell's title text prepared for the speech synthesizer.
    var speechText: String? = null
    // The selection state for the left button.
    var leftSelected: Boolean = false
    // The selection state for the right button.
    var rightSelected: Boolean = false
    // When true the cell uses a large style for text and buttons (e.g. for landscape mode)
    // while false uses the default style (e.g. for portrait).
    var largeStyle: Boolean = false
    // Whether the button is enabled or disabled so no interaction can occur.
    var disabled: Boolean = false
    // The background color.
    var backgroundColor: Int = R.color.darkMain
    // The color of the separator.
    var separatorColor: Int = R.color.lightMain
    // The listener to inform about cell actions.
    var listener: SplitCellListener? = null

    init {
        this.leftTitle = model.leftTitle
        this.rightTitle = model.rightTitle
        this.speechText = model.speechText
        this.leftSelected = model.leftSelected
        this.rightSelected = model.rightSelected
        this.largeStyle = model.largeStyle
        this.disabled = model.disabled
        this.backgroundColor = model.backgroundColor
        this.separatorColor = model.separatorColor
        this.listener = model.listener
    }
}