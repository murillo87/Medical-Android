package com.appamedix.makula.scenes.visusnhd.table

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.appamedix.makula.R
import com.appamedix.makula.base.BaseCellViewModel
import com.appamedix.makula.scenes.visusnhd.table.pickercell.VisusNhdInputPickerCell
import com.appamedix.makula.scenes.visusnhd.table.pickercell.VisusNhdInputPickerCellViewModel
import com.appamedix.makula.types.ViewCellType
import com.appamedix.makula.views.navigation.NavigationCell
import com.appamedix.makula.views.navigation.NavigationCellViewModel
import com.appamedix.makula.views.splitcell.SplitCell
import com.appamedix.makula.views.splitcell.SplitCellViewModel

class VisusNhdInputTableAdapter(private val context: Context,
                                val arrayList: ArrayList<BaseCellViewModel>)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val TYPE_NAVIGATION = 0
        const val TYPE_SPLIT = 1
        const val TYPE_VISUS_NHD_PICKER = 2
    }

    override fun getItemViewType(position: Int): Int {
        return when (arrayList[position].cellType) {
            ViewCellType.Navigation -> TYPE_NAVIGATION
            ViewCellType.SplitCell -> TYPE_SPLIT
            ViewCellType.VisusNhdPickerCell -> TYPE_VISUS_NHD_PICKER
            else -> throw IllegalArgumentException("Invalid cell type")
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = when (viewType) {
            TYPE_NAVIGATION -> layoutInflater.inflate(R.layout.navigation_view, parent, false)
            TYPE_SPLIT -> layoutInflater.inflate(R.layout.split_cell_view, parent, false)
            TYPE_VISUS_NHD_PICKER -> layoutInflater.inflate(R.layout.visus_nhd_input_cell, parent, false)
            else -> throw IllegalArgumentException("Invalid cell type")
        }

        return when (viewType) {
            TYPE_SPLIT -> SplitCell(context, view)
            TYPE_VISUS_NHD_PICKER -> VisusNhdInputPickerCell(context, view)
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
            ViewCellType.SplitCell -> {
                (holder as SplitCell).bindItems(cellViewModel as SplitCellViewModel)
            }
            ViewCellType.VisusNhdPickerCell -> {
                (holder as VisusNhdInputPickerCell).bindItems(cellViewModel as VisusNhdInputPickerCellViewModel)
            }
            else -> throw IllegalArgumentException("Invalid cell type")
        }
    }
}

