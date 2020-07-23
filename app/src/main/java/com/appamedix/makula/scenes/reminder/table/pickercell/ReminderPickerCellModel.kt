package com.appamedix.makula.scenes.reminder.table.pickercell

/* The model for the `ReminderPickerCell` */
data class ReminderPickerCellModel(
        // When true the title will use an extra large font size (e.g. for landscape mode),
        // while false uses the default size (e.g. for portrait).
        val largeStyle: Boolean,
        // The value in minutes to show in the picker.
        val value: Int,
        // The listener to inform about cell actions.
        val listener: ReminderPickerCellListener?
)