package com.appamedix.makula.scenes.diagnosis.table.maincell

import com.appamedix.makula.scenes.diagnosis.table.DiagnosisType

/* The model for the `DiagnosisCell` */
data class DiagnosisCellModel(
        // The diagnosis type of this cell.
        val diagnosisType: DiagnosisType,
        // The selected state.
        val selected: Boolean,
        // The cell's title.
        val title: String,
        // The cell's subtitle.
        val subtitle: String,
        // When true the title will use an extra large font size (e.g. for landscape mode),
        // while false uses the default size (e.g. for portrait).
        val largeStyle: Boolean,
        // The listener to inform about cell actions.
        val listener: DiagnosisCellListener?
)