package com.appamedix.makula.scenes.calendar.view

import android.content.Context
import android.support.v4.content.ContextCompat
import android.support.v7.widget.CardView
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.widget.GridView
import com.appamedix.makula.R
import com.appamedix.makula.constants.Const

/* The custom view to show week names (Monday to Sunday) */
class WeekNamesView(context: Context, attrs: AttributeSet) : CardView(context, attrs) {

    private var gridView: GridView
    private var separator: View

    /**
     * Initialize the view.
     */
    init {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.calendar_week_cell, this, true)

        view.setBackgroundColor(ContextCompat.getColor(context, R.color.darkMain))
        gridView = view.findViewById(R.id.weekday_grid)
        separator = view.findViewById(R.id.week_cell_separator)
    }

    /**
     * Sets up the view.
     *
     * @param largeStyle: `true` in landscape mode, `false` in portrait mode.
     */
    fun setupView(largeStyle: Boolean) {
        gridView.adapter = NameAdapter(context)

        if (largeStyle) {
            val height = TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    Const.Size.separatorThicknessLarge,
                    context.resources.displayMetrics).toInt()
            separator.layoutParams.height = height
        }
    }
}