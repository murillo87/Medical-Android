package com.appamedix.makula.scenes.amslertest.table.imagecell

import android.support.v7.widget.RecyclerView
import android.view.View

class AmslerTestImageCell(view: View) : RecyclerView.ViewHolder(view) {

    private lateinit var cellModel: AmslerTestImageCellViewModel

    /**
     * Sets up the cell view.
     *
     * @param model: The cell's view model.
     */
    fun bindItems(model: AmslerTestImageCellViewModel) {
        cellModel = model
    }
}