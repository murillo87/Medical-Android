package com.appamedix.makula.scenes.calendar.table.weekcell

import java.util.*

/* The model for the 'CalendarWeekCell' */
data class CalendarWeekCellModel(
        // When true the cell uses a large style for text and buttons (e.g. for landscape mode),
        // while false uses the default style (e.g. for portrait).
        val largeStyle: Boolean,
        // The week's date for passing back when tapping a day.
        val date: Date,
        // The text for the Monday label.
        val monText: String?,
        // The Monday label's text color. Null to use the default.
        val monColor: Int?,
        // The text for the Tuesday label.
        val tueText: String?,
        // The Tuesday label's text color. Null to use the default.
        val tueColor: Int?,
        // The text for the Wednesday label.
        val wedText: String?,
        // The Wednesday label's text color. Null to use the default.
        val wedColor: Int?,
        // The text for the Thursday label.
        val thuText: String?,
        // The Thursday label's text color. Null to use the default.
        val thuColor: Int?,
        // The text for the Friday label.
        val friText: String?,
        // The Friday label's text color. Null to use the default.
        val friColor: Int?,
        // The text for the Saturday label.
        val satText: String?,
        // The Saturday label's text color. Null to use the default.
        val satColor: Int?,
        // The text for the Sunday label.
        val sunText: String?,
        // The Sunday label's text color. Null to use the default.
        val sunColor: Int?,
        // The interface to inform about cell actions.
        val listener: CalendarWeekCellListener?
)