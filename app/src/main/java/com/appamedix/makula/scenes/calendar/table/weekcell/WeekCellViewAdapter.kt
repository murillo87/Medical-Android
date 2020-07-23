package com.appamedix.makula.scenes.calendar.table.weekcell

import android.content.Context
import android.os.Build
import android.support.v4.content.ContextCompat
import android.support.v4.content.res.ResourcesCompat
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.BaseAdapter
import android.widget.TextView
import com.appamedix.makula.R
import com.appamedix.makula.constants.Const
import com.appamedix.makula.utils.DeviceUtils

class WeekCellViewAdapter(private val context: Context,
                          private val model: CalendarWeekCellViewModel) : BaseAdapter() {

    override fun getCount(): Int = 7

    override fun getItem(position: Int): Any? = null

    override fun getItemId(position: Int): Long = 0L

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val dayLabel: TextView
        // Calculate the width of the labels included in grid view.
        val expectedLabelWidth = (DeviceUtils.getDeviceDimensions(context).widthPixels
                - Const.Calendar.margin.left * 2 - Const.Calendar.spacingBetweenGrid * 6) / 7

        // Get the text view for the given position.
        if (convertView == null) {
            // Create a text view if it doesn't exist.
            dayLabel = TextView(context)
            dayLabel.layoutParams = ViewGroup.LayoutParams(expectedLabelWidth, WRAP_CONTENT)
            dayLabel.textAlignment = View.TEXT_ALIGNMENT_CENTER
            dayLabel.tag = position
            dayLabel.setPadding(0,
                    Const.Calendar.margin.top,
                    0,
                    Const.Calendar.margin.bottom)
            dayLabel.setTextColor(when (position) {
                in 0..4 -> ContextCompat.getColor(context, R.color.lightMain)
                else -> ContextCompat.getColor(context, R.color.gray)
            })
            if (Build.VERSION.SDK_INT < 26) {
                val typeFace = ResourcesCompat.getFont(context, R.font.the_sans_bold_expert)
                dayLabel.typeface = typeFace
            } else {
                dayLabel.setTextAppearance(R.style.fontNumbersDefault)
            }
            dayLabel.textSize = Const.Font.numbersDefault
        } else {
            dayLabel = convertView as TextView
        }

        // Enlarge the text size in large style mode.
        if (model.largeStyle) {
            dayLabel.textSize = Const.Font.numbersLarge
        }

        // Set text and color to the label.
        when (position) {
            0 -> {
                model.monText?.let { dayLabel.text = it }
                model.monColor?.let { dayLabel.setTextColor(ContextCompat.getColor(context, it)) }
            }
            1 -> {
                model.tueText?.let { dayLabel.text = it }
                model.tueColor?.let { dayLabel.setTextColor(ContextCompat.getColor(context, it)) }
            }
            2 -> {
                model.wedText?.let { dayLabel.text = it }
                model.wedColor?.let { dayLabel.setTextColor(ContextCompat.getColor(context, it)) }
            }
            3 -> {
                model.thuText?.let { dayLabel.text = it }
                model.thuColor?.let { dayLabel.setTextColor(ContextCompat.getColor(context, it)) }
            }
            4 -> {
                model.friText?.let { dayLabel.text = it }
                model.friColor?.let { dayLabel.setTextColor(ContextCompat.getColor(context, it)) }
            }
            5 -> {
                model.satText?.let { dayLabel.text = it }
                model.satColor?.let { dayLabel.setTextColor(ContextCompat.getColor(context, it)) }
            }
            6 -> {
                model.sunText?.let { dayLabel.text = it }
                model.sunColor?.let { dayLabel.setTextColor(ContextCompat.getColor(context, it)) }
            }
        }

        return dayLabel
    }
}