package com.appamedix.makula.scenes.search.table.inputcell

interface SearchInputCellListener {
    /**
     * Informs that the return key ('\n') has been pressed in the edit text.
     *
     * @param content: The content of the entry cell.
     */
    fun editTextDidEndEditing(content: String)
}