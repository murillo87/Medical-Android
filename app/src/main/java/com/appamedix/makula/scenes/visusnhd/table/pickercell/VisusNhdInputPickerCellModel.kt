package com.appamedix.makula.scenes.visusnhd.table.pickercell

import com.appamedix.makula.types.VisusNhdType

/* The model for the `VisusNhdInputPickerCell` */
data class VisusNhdInputPickerCellModel(
        // When true the title will use an extra large font size (e.g. for landscape mode),
        // while false uses the default size (e.g. for portrait).
        val largeStyle: Boolean,
        // The type of value the picker represents.
        val type: VisusNhdType,
        // The value to show in the picker.
        val value: Int,
        // The listener to inform about cell actions.
        val listener: VisusNhdInputPickerCellListener?
)