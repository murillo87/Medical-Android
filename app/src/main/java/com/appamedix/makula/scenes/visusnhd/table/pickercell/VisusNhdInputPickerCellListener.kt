package com.appamedix.makula.scenes.visusnhd.table.pickercell

interface VisusNhdInputPickerCellListener {

    /**
     * Informs that the picker's value has changed.
     *
     * @param newValue: The changed value.
     */
    fun pickerValueChanged(newValue: Int)
}