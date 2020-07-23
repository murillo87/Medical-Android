package com.appamedix.makula.views.radiocell

import com.appamedix.makula.scenes.amslertest.AmslerTestProgressType

/* The model for the `SplitRadioCell` */
data class SplitRadioCellModel(
        // The amslertest progress type of this cell.
        val progressType: AmslerTestProgressType,
        // The title string.
        val title: String,
        // The selection state for the left radio button.
        val leftSelected: Boolean,
        // The selection state for the right radio button.
        val rightSelected: Boolean,
        // When true the cell uses a large style for text and buttons (e.g. for landscape mode),
        // while false uses the default style (e.g. for portrait).
        val largeStyle: Boolean,
        // Whether the buttonn is enabled or disabled so no interaction can occur.
        val disabled: Boolean,
        // The listener to inform about cell actions.
        val listener: SplitRadioCellListener?
)