package com.appamedix.makula.scenes.calendar.table.monthcell

/* The model for the 'CalendarMonthCell' */
data class CalendarMonthCellModel(
        // When true the cell uses a large style for text and buttons (e.g. for landscape mode),
        // while false uses the default style (e.g. for portrait).
        val largeStyle: Boolean,
        // The string to show as the cell's title.
        val cellTitle: String
)