package com.appamedix.makula.scenes.calendar.table.weekcell

import android.content.Context
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.util.TypedValue
import android.view.View
import android.widget.AdapterView
import android.widget.GridView
import com.appamedix.makula.R
import com.appamedix.makula.constants.Const

class CalendarWeekCell(private var context: Context, private val view: View) : RecyclerView.ViewHolder(view) {

    // The UI elements for this cell view.
    private val gridView: GridView = view.findViewById(R.id.weekday_grid)
    private val separator: View = view.findViewById(R.id.week_cell_separator)

    // The cell's model.
    private var model: CalendarWeekCellViewModel? = null

    /**
     * Sets up the cell.
     *
     * @param model: The cell's model
     */
    fun bindItems(model: CalendarWeekCellViewModel) {
        // Apply model.
        this.model = model
        view.setBackgroundColor(ContextCompat.getColor(context, R.color.darkMain))
        gridView.adapter = WeekCellViewAdapter(context, model)

        // Register callback listener for the child items of the grid view.
        gridView.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            model.listener?.daySelected(model.date, position)
        }

        // Adjust views in large style mode.
        if (model.largeStyle) {
            // Enlarge the height of separator.
            val height = TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    Const.Size.separatorThicknessLarge,
                    context.resources.displayMetrics).toInt()
            separator.layoutParams.height = height
        }
    }
}