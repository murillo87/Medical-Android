package com.appamedix.makula.scenes.calendar.table

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.appamedix.makula.R
import com.appamedix.makula.base.BaseCellViewModel
import com.appamedix.makula.scenes.calendar.table.monthcell.CalendarMonthCell
import com.appamedix.makula.scenes.calendar.table.monthcell.CalendarMonthCellViewModel
import com.appamedix.makula.scenes.calendar.table.weekcell.CalendarWeekCell
import com.appamedix.makula.scenes.calendar.table.weekcell.CalendarWeekCellViewModel
import com.appamedix.makula.types.ViewCellType

class CalendarTableAdapter(private val context: Context,
                           private val arrayList: ArrayList<BaseCellViewModel>)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val TYPE_MONTH_CELL = 0
        const val TYPE_WEEK_CELL = 1
    }

    override fun getItemViewType(position: Int): Int {
        return when (arrayList[position].cellType) {
            ViewCellType.CalendarMonthCell -> TYPE_MONTH_CELL
            ViewCellType.CalendarWeekCell -> TYPE_WEEK_CELL
            else -> throw IllegalArgumentException("Invalid cell type")
        }
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = when (viewType) {
            TYPE_MONTH_CELL -> layoutInflater.inflate(R.layout.calendar_month_cell, parent, false)
            else -> layoutInflater.inflate(R.layout.calendar_week_cell, parent, false)
        }

        return when (viewType) {
            TYPE_MONTH_CELL -> CalendarMonthCell(context, view)
            else -> CalendarWeekCell(context, view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val cellViewModel = arrayList[position]

        if (cellViewModel.cellType == ViewCellType.CalendarMonthCell) {
            (holder as CalendarMonthCell).bindItems(cellViewModel as CalendarMonthCellViewModel)
        } else {
            (holder as CalendarWeekCell).bindItems(cellViewModel as CalendarWeekCellViewModel)
        }
    }
}