package com.appamedix.makula.scenes.amslertest.table

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.appamedix.makula.R
import com.appamedix.makula.base.BaseCellViewModel
import com.appamedix.makula.scenes.amslertest.table.imagecell.AmslerTestImageCell
import com.appamedix.makula.scenes.amslertest.table.imagecell.AmslerTestImageCellViewModel
import com.appamedix.makula.types.ViewCellType
import com.appamedix.makula.views.navigation.NavigationCell
import com.appamedix.makula.views.navigation.NavigationCellViewModel
import com.appamedix.makula.views.radiocell.SplitRadioCell
import com.appamedix.makula.views.radiocell.SplitRadioCellViewModel
import com.appamedix.makula.views.splitcell.SplitCell
import com.appamedix.makula.views.splitcell.SplitCellViewModel
import com.appamedix.makula.views.textcell.StaticTextCellView
import com.appamedix.makula.views.textcell.StaticTextCellViewModel

class AmslerTestTableAdapter(private val context: Context,
                             val arrayList: ArrayList<BaseCellViewModel>)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val TYPE_NAVIGATION = 0
        const val TYPE_IMAGE_CELL = 1
        const val TYPE_TEXT_CELL = 2
        const val TYPE_SPLIT_CELL = 3
        const val TYPE_RADIO_CELL = 4
    }

    override fun getItemViewType(position: Int): Int {
        return when (arrayList[position].cellType) {
            ViewCellType.Navigation -> TYPE_NAVIGATION
            ViewCellType.AmslerTestImageCell -> TYPE_IMAGE_CELL
            ViewCellType.StaticTextCell -> TYPE_TEXT_CELL
            ViewCellType.SplitCell -> TYPE_SPLIT_CELL
            ViewCellType.SplitRadioCell -> TYPE_RADIO_CELL
            else -> throw IllegalArgumentException("Invalid cell type")
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = when (viewType) {
            TYPE_NAVIGATION -> layoutInflater.inflate(R.layout.navigation_view, parent, false)
            TYPE_IMAGE_CELL -> layoutInflater.inflate(R.layout.amsler_test_image_cell, parent, false)
            TYPE_TEXT_CELL -> layoutInflater.inflate(R.layout.static_text_cell, parent, false)
            TYPE_SPLIT_CELL -> layoutInflater.inflate(R.layout.split_cell_view, parent, false)
            TYPE_RADIO_CELL -> layoutInflater.inflate(R.layout.split_radio_cell, parent, false)
            else -> throw IllegalArgumentException("Invalid cell type")
        }

        return when (viewType) {
            TYPE_IMAGE_CELL -> AmslerTestImageCell(view)
            TYPE_TEXT_CELL -> StaticTextCellView(context, view)
            TYPE_SPLIT_CELL -> SplitCell(context, view)
            TYPE_RADIO_CELL -> SplitRadioCell(context, view)
            else -> NavigationCell(context, view)
        }
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val cellViewModel = arrayList[position]

        when (cellViewModel.cellType) {
            ViewCellType.Navigation -> {
                (holder as NavigationCell).bindItems(cellViewModel as NavigationCellViewModel)
            }
            ViewCellType.AmslerTestImageCell -> {
                (holder as AmslerTestImageCell).bindItems(cellViewModel as AmslerTestImageCellViewModel)
            }
            ViewCellType.StaticTextCell -> {
                (holder as StaticTextCellView).bindItems(cellViewModel as StaticTextCellViewModel, null)
            }
            ViewCellType.SplitCell -> {
                (holder as SplitCell).bindItems(cellViewModel as SplitCellViewModel)
            }
            ViewCellType.SplitRadioCell -> {
                (holder as SplitRadioCell).bindItems(cellViewModel as SplitRadioCellViewModel, position)
            }
            else -> throw IllegalArgumentException("Invalid cell type")
        }
    }
}