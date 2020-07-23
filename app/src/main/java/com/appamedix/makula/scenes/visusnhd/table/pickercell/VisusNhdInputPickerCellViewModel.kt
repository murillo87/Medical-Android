package com.appamedix.makula.scenes.visusnhd.table.pickercell

import com.appamedix.makula.base.BaseCellViewModel
import com.appamedix.makula.types.ViewCellType
import com.appamedix.makula.types.VisusNhdType

/* The view model for the `VisusNhdInputPickerCell` */
class VisusNhdInputPickerCellViewModel(model: VisusNhdInputPickerCellModel)
    : BaseCellViewModel(ViewCellType.VisusNhdPickerCell) {
    // When true the title will use an extra large font size (e.g. for landscape mode),
    // while false uses the default size (e.g. for portrait).
    var largeStyle: Boolean = false
    // The type of value the picker represents.
    var type: VisusNhdType = VisusNhdType.Visus
    // The value to show in the picker.
    var value: Int = -1
    // The listener to inform about cell actions.
    var listener: VisusNhdInputPickerCellListener? = null

    init {
        this.largeStyle = model.largeStyle
        this.type = model.type
        this.value = model.value
        this.listener = model.listener
    }
}