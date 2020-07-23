package com.appamedix.makula.scenes.calendar.table.weekcell

import com.appamedix.makula.base.BaseCellViewModel
import com.appamedix.makula.types.ViewCellType
import java.util.*

/* The view model for the 'CalendarWeekCell' */
class CalendarWeekCellViewModel(model: CalendarWeekCellModel)
    : BaseCellViewModel(ViewCellType.CalendarWeekCell) {
    // When true the cell uses a large style for text and buttons (e.g. for landscape mode),
    // while false uses the default style (e.g. for portrait).
    var largeStyle: Boolean = false
    // The week's date for passing back when tapping a day.
    var date: Date = Date()
    // The text for the Monday label.
    var monText: String? = null
    // The Monday label's text color. Null to use the default.
    var monColor: Int? = null
    // The text for the Tuesday label.
    var tueText: String? = null
    // The Tuesday label's text color. Null to use the default.
    var tueColor: Int? = null
    // The text for the Wednesday label.
    var wedText: String? = null
    // The Wednesday label's text color. Null to use the default.
    var wedColor: Int? = null
    // The text for the Thursday label.
    var thuText: String? = null
    // The Thursday label's text color. Null to use the default.
    var thuColor: Int? = null
    // The text for the Friday label.
    var friText: String? = null
    // The Friday label's text color. Null to use the default.
    var friColor: Int? = null
    // The text for the Saturday label.
    var satText: String? = null
    // The Saturday label's text color. Null to use the default.
    var satColor: Int? = null
    // The text for the Sunday label.
    var sunText: String? = null
    // The Sunday label's text color. Null to use the default.
    var sunColor: Int? = null
    // The interface to inform about cell actions.
    var listener: CalendarWeekCellListener? = null

    init {
        this.largeStyle = model.largeStyle
        this.date = model.date
        this.monText = model.monText
        this.monColor = model.monColor
        this.tueText = model.tueText
        this.tueColor = model.tueColor
        this.wedText = model.wedText
        this.wedColor = model.wedColor
        this.thuText = model.thuText
        this.thuColor = model.thuColor
        this.friText = model.friText
        this.friColor = model.friColor
        this.satText = model.satText
        this.satColor = model.satColor
        this.sunText = model.sunText
        this.sunColor = model.sunColor
        this.listener = model.listener
    }
}