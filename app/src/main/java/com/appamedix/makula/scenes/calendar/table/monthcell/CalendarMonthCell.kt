package com.appamedix.makula.scenes.calendar.table.monthcell

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.TypedValue
import android.view.View
import android.widget.TextView
import com.appamedix.makula.R
import com.appamedix.makula.constants.Const

class CalendarMonthCell(private var context: Context, view: View) : RecyclerView.ViewHolder(view) {

    // The UI elements for this cell view.
    private val title: TextView = view.findViewById(R.id.month_cell_title)
    private val separator: View = view.findViewById(R.id.month_cell_separator)

    /**
     * Sets up the cell.
     *
     * @param model: The cell's model
     */
    fun bindItems(model: CalendarMonthCellViewModel) {
        // Apply model.
        title.text = model.cellTitle

        // Adjust views in large style mode.
        if (model.largeStyle) {
            // Enlarge the text size of the title view.
            title.textSize = Const.Font.headlineLarge
            // Enlarge the height of separator.
            val height = TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    Const.Size.separatorThicknessLarge,
                    context.resources.displayMetrics).toInt()
            separator.layoutParams.height = height
        }
    }
}