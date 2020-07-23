package com.appamedix.makula.scenes.medicament.table.maincell

data class MedicamentCellModel(
        // The cell's title.
        val title: String,
        // The cell's editable state.
        val editable: Boolean,
        // The cell's selected state.
        val selected: Boolean,
        // When true the title will use an extra large font size (e.g. landscape mode),
        // while false uses the default size (e.g. for portrait).
        val largeStyle: Boolean,
        // The listener to inform about cell actions.
        val listener: MedicamentCellListener?
)