package com.appamedix.makula.scenes.calendar.table.monthcell

import com.appamedix.makula.base.BaseCellViewModel
import com.appamedix.makula.types.ViewCellType

/* The view model for the 'CalendarMonthCell' */
class CalendarMonthCellViewModel(model: CalendarMonthCellModel)
    : BaseCellViewModel(ViewCellType.CalendarMonthCell) {
    // When true the cell uses a large style for text and buttons (e.g. for landscape mode),
    // while false uses the default style (e.g. for portrait).
    var largeStyle: Boolean = false
    // The string to show as the cell's title.
    var cellTitle: String = ""

    init {
        this.largeStyle = model.largeStyle
        this.cellTitle = model.cellTitle
    }
}