package com.appamedix.makula.scenes.calendar.table.weekcell

import java.util.*

/* The interface to inform about calendar cell actions */
interface CalendarWeekCellListener {
    /**
     * Informs that the picker's value has changed.
     *
     * @param atWeekDate: The cell's week day date.
     * @param dayIndex: The day's index selected (0 = Monday, ..., 6 = Sunday).
     */
    fun daySelected(atWeekDate: Date, dayIndex: Int)
}