package com.appamedix.makula.scenes.search.table.inputcell

/* The model for the `SearchInputCell` */
data class SearchInputCellModel(
        // When true the title will use an extra large font size (e.g. for landscape mode),
        // while false uses the default size (e.g. for portrait).
        val largeStyle: Boolean,
        // The textfield's content text if any should be shown. Leave to `null` to show the placeholder.
        val searchText: String?,
        // The listener to inform about cell actions.
        val listener: SearchInputCellListener?
)