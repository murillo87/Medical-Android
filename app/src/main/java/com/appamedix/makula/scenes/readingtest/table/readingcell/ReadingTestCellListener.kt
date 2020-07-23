package com.appamedix.makula.scenes.readingtest.table.readingcell

interface ReadingTestCellListener {
    /**
     * Informs that the left button has been selected.
     *
     * @param position: The cell index for referencing.
     */
    fun leftButtonSelected(position: Int)

    /**
     * Informs that the right button has been selected.
     *
     * @param position: The cell index for referencing.
     */
    fun rightButtonSelected(position: Int)
}