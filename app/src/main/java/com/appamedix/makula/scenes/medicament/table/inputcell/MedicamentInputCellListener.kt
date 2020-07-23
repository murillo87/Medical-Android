package com.appamedix.makula.scenes.medicament.table.inputcell

interface MedicamentInputCellListener {
    /**
     * Informs that the return key ('\n') has been pressed in the edit text.
     *
     * @param content: The content of the entry cell.
     * @param position: The index of focused cell.
     */
    fun editTextDidEndEditing(content: String, position: Int)
}