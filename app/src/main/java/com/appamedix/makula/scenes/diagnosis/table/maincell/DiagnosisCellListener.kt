package com.appamedix.makula.scenes.diagnosis.table.maincell

import com.appamedix.makula.scenes.diagnosis.table.DiagnosisType

/* The listener to inform about cell actions */
interface DiagnosisCellListener {
    /**
     * Informs that the info button has been pressed.
     *
     * @param type: The diagnosis type of this cell.
     */
    fun infoButtonPressed(type: DiagnosisType)

    /**
     * Informs that the cell view has been clicked.
     *
     * @param position: The cell index.
     */
    fun cellItemClicked(position: Int)
}