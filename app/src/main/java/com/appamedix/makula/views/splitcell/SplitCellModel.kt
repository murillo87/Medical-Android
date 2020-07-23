package com.appamedix.makula.views.splitcell

/* The model for the `SplitCell` */
data class SplitCellModel(
        // The title string for the left button.
        val leftTitle: String,
        // The title string for the right button.
        val rightTitle: String,
        // The cell's title text prepared for the speech synthesizer.
        val speechText: String?,
        // The selection state for the left button.
        val leftSelected: Boolean,
        // The selection state for the right button.
        val rightSelected: Boolean,
        // When true the cell uses a large style for text and buttons (e.g. for landscape mode)
        // while false uses the default style (e.g. for portrait).
        val largeStyle: Boolean,
        // Whether the button is enabled or disabled so no interaction can occur.
        val disabled: Boolean,
        // The background color.
        val backgroundColor: Int,
        // The color of the separator.
        val separatorColor: Int,
        // The listener to inform about cell actions.
        val listener: SplitCellListener?
)