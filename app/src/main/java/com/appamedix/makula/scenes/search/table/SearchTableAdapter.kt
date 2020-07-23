package com.appamedix.makula.scenes.search.table

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.appamedix.makula.R
import com.appamedix.makula.base.BaseCellViewModel
import com.appamedix.makula.global.listener.CellItemListener
import com.appamedix.makula.scenes.search.table.inputcell.SearchInputCell
import com.appamedix.makula.scenes.search.table.inputcell.SearchInputCellViewModel
import com.appamedix.makula.types.ViewCellType
import com.appamedix.makula.views.navigation.NavigationCell
import com.appamedix.makula.views.navigation.NavigationCellViewModel
import com.appamedix.makula.views.textcell.StaticTextCellView
import com.appamedix.makula.views.textcell.StaticTextCellViewModel

class SearchTableAdapter(private val context: Context,
                         private val arrayList: ArrayList<BaseCellViewModel>,
                         private val clickListener: CellItemListener)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val TYPE_NAVIGATION = 0
        const val TYPE_INPUT_CELL = 1
        const val TYPE_TEXT_CELL = 2
    }

    override fun getItemViewType(position: Int): Int {
        return when (arrayList[position].cellType) {
            ViewCellType.Navigation -> TYPE_NAVIGATION
            ViewCellType.SearchInputCell -> TYPE_INPUT_CELL
            ViewCellType.StaticTextCell -> TYPE_TEXT_CELL
            else -> throw IllegalArgumentException("Invalid cell type")
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = when (viewType) {
            TYPE_NAVIGATION -> layoutInflater.inflate(R.layout.navigation_view, parent, false)
            TYPE_INPUT_CELL -> layoutInflater.inflate(R.layout.text_input_cell, parent, false)
            TYPE_TEXT_CELL -> layoutInflater.inflate(R.layout.static_text_cell, parent, false)
            else -> throw IllegalArgumentException("Invalid cell type")
        }

        return when (viewType) {
            TYPE_INPUT_CELL -> SearchInputCell(context, view)
            TYPE_TEXT_CELL -> StaticTextCellView(context, view)
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
            ViewCellType.SearchInputCell -> {
                (holder as SearchInputCell).bindItems(cellViewModel as SearchInputCellViewModel)
            }
            ViewCellType.StaticTextCell -> {
                (holder as StaticTextCellView).bindItems(cellViewModel as StaticTextCellViewModel, clickListener)
            }
            else -> throw IllegalArgumentException("Invalid cell type")
        }
    }
}