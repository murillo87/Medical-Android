package com.appamedix.makula.scenes.readingtest.table.readingcell

import com.appamedix.makula.scenes.readingtest.ReadingTestMagnitudeType

/* The model for the `ReadingTestCell` */
data class ReadingTestCellModel(
        // The magnitude type of this cell.
        val magnitudeType: ReadingTestMagnitudeType,
        // The content string.
        val content: String,
        // The selection state for the left radio button.
        val leftSelected: Boolean,
        // The selection state for the right radio button.
        val rightSelected: Boolean,
        // When true the cell uses a large style for text and buttons (e.g. for landscape mode),
        // while false uses the default style (e.g. for portrait).
        val largeStyle: Boolean,
        // The listener to inform about cell actions.
        val listener: ReadingTestCellListener?
)