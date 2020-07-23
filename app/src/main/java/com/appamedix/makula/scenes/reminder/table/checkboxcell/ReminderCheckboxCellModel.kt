package com.appamedix.makula.scenes.reminder.table.checkboxcell

/* The model for the `ReminderCheckboxCell` */
data class ReminderCheckboxCellModel(
        // When true the title will use an extra large font size (e.g. for landscape mode),
        // while false uses the default size (e.g. for portrait).
        val largeStyle: Boolean,
        // The cell's checked state.
        val checked: Boolean,
        // The listener to inform about cell actions.
        val listener: ReminderCheckboxCellListener?
)