package com.appamedix.makula.scenes.medicament.table.inputcell

data class MedicamentInputCellModel(
        // When true the title will use an extra large font size (e.g. for landscape mode),
        // while false uses the default size (e.g. for portrait).
        val largeStyle: Boolean,
        // The listener to inform about cell actions.
        val listener: MedicamentInputCellListener?
)