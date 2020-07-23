package com.appamedix.makula.views.radiocell

import com.appamedix.makula.base.BaseCellViewModel
import com.appamedix.makula.scenes.amslertest.AmslerTestProgressType
import com.appamedix.makula.types.ViewCellType

/* The view model for the `SplitRadioCell` */
class SplitRadioCellViewModel(model: SplitRadioCellModel)
    : BaseCellViewModel(ViewCellType.SplitRadioCell) {
    // The amslertest progress type of this cell.
    var progressType: AmslerTestProgressType = AmslerTestProgressType.Equal
    // The title string.
    var title: String = ""
    // The selection state for the left radio button.
    var leftSelected: Boolean = false
    // The selection state for the right radio button.
    var rightSelected: Boolean = false
    // When true the cell uses a large style for text and buttons (e.g. for landscape mode),
    // while false uses the default style (e.g. for portrait).
    var largeStyle: Boolean = false
    // Whether the buttonn is enabled or disabled so no interaction can occur.
    var disabled: Boolean = false
    // The listener to inform about cell actions.
    var listener: SplitRadioCellListener? = null

    init {
        this.progressType = model.progressType
        this.title = model.title
        this.leftSelected = model.leftSelected
        this.rightSelected = model.rightSelected
        this.largeStyle = model.largeStyle
        this.disabled = model.disabled
        this.listener = model.listener
    }
}
