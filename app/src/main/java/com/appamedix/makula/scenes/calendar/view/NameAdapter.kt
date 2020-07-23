package com.appamedix.makula.scenes.calendar.view

import android.content.Context
import android.os.Build
import android.support.v4.content.ContextCompat
import android.support.v4.content.res.ResourcesCompat
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.*
import android.widget.BaseAdapter
import android.widget.TextView
import com.appamedix.makula.R
import com.appamedix.makula.constants.Const
import com.appamedix.makula.utils.DeviceUtils

class NameAdapter(private val context: Context) : BaseAdapter() {

    // Prepare the grid data.
    private val weekNames = arrayOf(
            R.string.weekNameMonday,
            R.string.weekNameTuesday,
            R.string.weekNameWednesday,
            R.string.weekNameThursday,
            R.string.weekNameFriday,
            R.string.weekNameSaturday,
            R.string.weekNameSunday)

    override fun getCount(): Int = weekNames.size

    override fun getItem(position: Int): Any? = null

    override fun getItemId(position: Int): Long = 0L

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val weekNameLabel: TextView
        // Calculate the width of the labels included in grid view.
        val expectedLabelWidth = (DeviceUtils.getDeviceDimensions(context).widthPixels
                - Const.Calendar.margin.left * 2 - Const.Calendar.spacingBetweenGrid * 6) / 7

        // Get the text view for the given position.
        if (convertView == null) {
            // Create a text view if it doesn't exist.
            weekNameLabel = TextView(context)
            weekNameLabel.layoutParams = ViewGroup.LayoutParams(expectedLabelWidth, WRAP_CONTENT)
            weekNameLabel.textAlignment = View.TEXT_ALIGNMENT_CENTER
            weekNameLabel.tag = position
            weekNameLabel.setPadding(0,
                    Const.Calendar.margin.top,
                    0,
                    Const.Calendar.margin.bottom)
            weekNameLabel.setTextColor(when (position) {
                in 0..4 -> ContextCompat.getColor(context, R.color.lightMain)
                else -> ContextCompat.getColor(context, R.color.gray)
            })
            if (Build.VERSION.SDK_INT < 26) {
                val typeFace = ResourcesCompat.getFont(context, R.font.the_sans_b7_bold)
                weekNameLabel.typeface = typeFace
            } else {
                weekNameLabel.setTextAppearance(R.style.fontContent2Default)
            }
            weekNameLabel.textSize = Const.Font.content2Default
        } else {
            weekNameLabel = convertView as TextView
        }

        // Set text and color to the label.
        weekNameLabel.setText(weekNames[position])

        return weekNameLabel
    }
}