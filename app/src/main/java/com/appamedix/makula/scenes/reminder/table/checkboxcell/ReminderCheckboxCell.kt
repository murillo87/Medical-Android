package com.appamedix.makula.scenes.reminder.table.checkboxcell

import android.content.Context
import android.graphics.PorterDuff
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.util.TypedValue
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.appamedix.makula.R
import com.appamedix.makula.constants.Const

class ReminderCheckboxCell(private val context: Context, private val view: View)
    : RecyclerView.ViewHolder(view) {

    // The UI elements to consist this cell view.
    private val titleView: TextView = view.findViewById(R.id.reminder_checkbox_title)
    private val checkboxIcon: ImageView = view.findViewById(R.id.reminder_radio)
    private val separator: View = view.findViewById(R.id.reminder_checkbox_separator)

    private var listener: ReminderCheckboxCellListener? = null

    fun bindItems(model: ReminderCheckboxCellViewModel, position: Int) {
        this.listener = model.listener

        // Setup views.
        checkboxIcon.scaleType = ImageView.ScaleType.FIT_END
        if (model.checked) applyHighlightColor()
        else applyDefaultColor()

        // Register for callback listeners.
        view.setOnClickListener {
            listener?.onCellItemClicked(position)
        }

        // Adjust UI elements in the case of large style, e.g. landscape.
        if (model.largeStyle) {
            // Enlarge the text size of the title view.
            titleView.textSize = Const.Font.headlineLarge

            // Enlarge the radio icon size.
            var sizeValue = TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    (Const.Size.radioButtonHeightNormal - 20) * Const.Size.landscapeScaleFactor,
                    context.resources.displayMetrics).toInt()
            checkboxIcon.layoutParams.width = sizeValue
            checkboxIcon.layoutParams.height = sizeValue

            // Enlarge the height of separator.
            sizeValue = TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    Const.Size.separatorThicknessLarge,
                    context.resources.displayMetrics).toInt()
            separator.layoutParams.height = sizeValue
        }
    }

    /**
     * Sets the color of the views depending on the current style state.
     */
    fun applyDefaultColor() {
        titleView.setTextColor(ContextCompat.getColor(context, R.color.lightMain))
        checkboxIcon.isActivated = false
        checkboxIcon.setColorFilter(ContextCompat.getColor(context, R.color.lightMain), PorterDuff.Mode.SRC_IN)
    }

    /**
     * Sets the color of the views for the selected state.
     */
    fun applyHighlightColor() {
        titleView.setTextColor(ContextCompat.getColor(context, R.color.magenta))
        checkboxIcon.isActivated = true
        checkboxIcon.setColorFilter(ContextCompat.getColor(context, R.color.magenta), PorterDuff.Mode.SRC_IN)
    }
}