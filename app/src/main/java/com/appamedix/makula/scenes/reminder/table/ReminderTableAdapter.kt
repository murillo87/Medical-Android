package com.appamedix.makula.scenes.reminder.table

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.appamedix.makula.R
import com.appamedix.makula.base.BaseCellViewModel
import com.appamedix.makula.scenes.reminder.table.checkboxcell.ReminderCheckboxCell
import com.appamedix.makula.scenes.reminder.table.checkboxcell.ReminderCheckboxCellViewModel
import com.appamedix.makula.scenes.reminder.table.pickercell.ReminderPickerCell
import com.appamedix.makula.scenes.reminder.table.pickercell.ReminderPickerCellViewModel
import com.appamedix.makula.types.ViewCellType
import com.appamedix.makula.views.navigation.NavigationCell
import com.appamedix.makula.views.navigation.NavigationCellViewModel

class ReminderTableAdapter(private val context: Context,
                           val arrayList: ArrayList<BaseCellViewModel>)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val TYPE_NAVIGATION = 0
        const val TYPE_CHECK_CELL = 1
        const val TYPE_PICKER_CELL = 2
    }

    override fun getItemViewType(position: Int): Int {
        return when (arrayList[position].cellType) {
            ViewCellType.Navigation -> TYPE_NAVIGATION
            ViewCellType.ReminderCheckboxCell -> TYPE_CHECK_CELL
            ViewCellType.ReminderPickerCell -> TYPE_PICKER_CELL
            else -> throw IllegalArgumentException("Invalid cell type")
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = when (viewType) {
            TYPE_NAVIGATION -> layoutInflater.inflate(R.layout.navigation_view, parent, false)
            TYPE_CHECK_CELL -> layoutInflater.inflate(R.layout.reminder_checkbox_cell, parent, false)
            TYPE_PICKER_CELL -> layoutInflater.inflate(R.layout.reminder_picker_cell, parent, false)
            else -> throw IllegalArgumentException("Invalid cell type")
        }

        return when (viewType) {
            TYPE_CHECK_CELL -> ReminderCheckboxCell(context, view)
            TYPE_PICKER_CELL -> ReminderPickerCell(context, view)
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
            ViewCellType.ReminderCheckboxCell -> {
                (holder as ReminderCheckboxCell).bindItems(cellViewModel as ReminderCheckboxCellViewModel, position)
            }
            ViewCellType.ReminderPickerCell -> {
                (holder as ReminderPickerCell).bindItems(cellViewModel as ReminderPickerCellViewModel)
            }
            else -> throw IllegalArgumentException("Invalid cell type")
        }
    }
}