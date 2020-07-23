package com.appamedix.makula.scenes.readingtest.table.readingcell

import com.appamedix.makula.base.BaseCellViewModel
import com.appamedix.makula.scenes.readingtest.ReadingTestMagnitudeType
import com.appamedix.makula.types.ViewCellType

/* The view model for the `ReadingTestCell` */
class ReadingTestCellViewModel(model: ReadingTestCellModel)
    : BaseCellViewModel(ViewCellType.ReadingTestCell) {
    // The magnitude type of this cell.
    var magnitudeType: ReadingTestMagnitudeType = ReadingTestMagnitudeType.Medium
    // The content string.
    var content: String = ""
    // The selection state for the left radio button.
    var leftSelected: Boolean = false
    // The selection state for the right radio button.
    var rightSelected: Boolean = false
    // When true the cell uses a large style for text and buttons (e.g. for landscape mode),
    // while false uses the default style (e.g. for portrait).
    var largeStyle: Boolean = false
    // The listener to inform about cell actions.
    var listener: ReadingTestCellListener? = null

    init {
        this.magnitudeType = model.magnitudeType
        this.content = model.content
        this.leftSelected = model.leftSelected
        this.rightSelected = model.rightSelected
        this.largeStyle = model.largeStyle
        this.listener = model.listener
    }
}