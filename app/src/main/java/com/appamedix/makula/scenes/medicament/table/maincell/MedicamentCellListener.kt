package com.appamedix.makula.scenes.medicament.table.maincell

interface MedicamentCellListener {
    /**
     * Informs that the delete button has been clicked from the swipe menu.
     * This is called in the case that both delete buttons in the left and right swipe menu.
     *
     * @param position: The cell index to delete.
     */
    fun deleteButtonClicked(position: Int)

    /**
     * Informs that the cell has been pressed.
     *
     * @param position: The cell index to be pressed.
     */
    fun onCellItemClicked(position: Int)
}