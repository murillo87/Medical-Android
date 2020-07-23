package com.appamedix.makula.views.splitcell

import android.content.Context
import android.content.res.Resources
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.util.TypedValue
import android.view.View
import android.widget.Button
import com.appamedix.makula.R
import com.appamedix.makula.constants.Const

/* The custom class for the split cell view. */
class SplitCell(private val context: Context, private val view: View) : RecyclerView.ViewHolder(view) {

    // The UI elements for this cell.
    private val leftButton: Button = view.findViewById(R.id.split_left_button)
    private val rightButton: Button = view.findViewById(R.id.split_right_button)
    private val separator: View = view.findViewById(R.id.split_cell_separator)

    // The listener for the left and right buttons.
    private var listener: SplitCellListener? = null

    /**
     * Sets up the cell view.
     *
     * @param model: The cell's view model.
     */
    fun bindItems(model: SplitCellViewModel) {
        // Apply view model.
        val backgroundColor = ContextCompat.getColor(context, model.backgroundColor)
        view.setBackgroundColor(backgroundColor)
        val separatorColor = ContextCompat.getColor(context, model.separatorColor)
        separator.setBackgroundColor(separatorColor)

        leftButton.text = model.leftTitle
        rightButton.text = model.rightTitle
        leftButton.setTextColor(ContextCompat.getColor(context, if (model.leftSelected) R.color.white else R.color.magenta))
        rightButton.setTextColor(ContextCompat.getColor(context, if (model.rightSelected) R.color.white else R.color.lightMain))
        listener = model.listener

        // Update the width of left & right buttons.
        val screenWidth = Resources.getSystem().displayMetrics.widthPixels
        leftButton.layoutParams.width = screenWidth / 2
        rightButton.layoutParams.width = screenWidth / 2

        // Register for callback listeners.
        if (!model.disabled) {
            leftButton.setOnClickListener { listener?.leftButtonPressed() }
            rightButton.setOnClickListener { listener?.rightButtonPressed() }
        }

        // Adjust the cell view elements in the case of large style, e.g. landscape mode.
        if (model.largeStyle) {
            // Enlarge the title size.
            leftButton.textSize = Const.Font.headlineLarge
            rightButton.textSize = Const.Font.headlineLarge

            // Enlarge the height of separator.
            val height = TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    Const.Size.separatorThicknessLarge,
                    context.resources.displayMetrics).toInt()
            separator.layoutParams.height = height
        }
    }

    /**
     * Sets the text color as original.
     */
    fun applySpeechDefaultColor() {
        leftButton.setTextColor(ContextCompat.getColor(context, R.color.magenta))
        rightButton.setTextColor(ContextCompat.getColor(context, R.color.lightMain))
    }

    /**
     * Highlights the text while speaking.
     */
    fun applySpeechHighlightColor() {
        leftButton.setTextColor(ContextCompat.getColor(context, R.color.white))
        rightButton.setTextColor(ContextCompat.getColor(context, R.color.white))
    }
}