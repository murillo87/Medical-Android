package com.appamedix.makula.scenes.reminder.table.pickercell

import com.appamedix.makula.base.BaseCellViewModel
import com.appamedix.makula.types.ViewCellType

/* The view model for the `ReminderPickerCell` */
class ReminderPickerCellViewModel(model: ReminderPickerCellModel)
    : BaseCellViewModel(ViewCellType.ReminderPickerCell) {
    // When true the title will use an extra large font size (e.g. for landscape mode),
    // while false uses the default size (e.g. for portrait).
    var largeStyle: Boolean = false
    // The value in minutes to show in the picker.
    var value: Int = 0
    // The listener to inform about cell actions.
    var listener: ReminderPickerCellListener? = null

    init {
        this.largeStyle = model.largeStyle
        this.value = model.value
        this.listener = model.listener
    }
}