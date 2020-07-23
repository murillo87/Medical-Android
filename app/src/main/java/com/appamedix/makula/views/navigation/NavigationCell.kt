package com.appamedix.makula.views.navigation

import android.content.Context
import android.content.res.Resources
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.util.TypedValue
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import com.appamedix.makula.R
import com.appamedix.makula.constants.Const
import com.appamedix.makula.types.ImageButtonType

/**
 * The custom class for the navigation cell view.
 * This cell is available in landscape mode only.
 */
class NavigationCell(private val context: Context, view: View) : RecyclerView.ViewHolder(view) {

    // The UI elements for this cell.
    private val title: TextView = view.findViewById(R.id.nav_title)
    private val separator: View = view.findViewById(R.id.nav_cell_separator)
    private val leftButton: ImageButton = view.findViewById(R.id.nav_left_button)
    private val rightButton: ImageButton = view.findViewById(R.id.nav_right_button)

    // The listener for the left and right buttons.
    private var listener: NavigationViewListener? = null

    /**
     * Sets up the cell view.
     *
     * @param model: The cell's view model.
     */
    fun bindItems(model: NavigationCellViewModel) {
        // Apply view model.
        val textColor = ContextCompat.getColor(context, model.color)
        title.text = model.title
        title.setTextColor(textColor)

        separator.visibility = if (model.separatorVisible) View.VISIBLE else View.GONE
        leftButton.visibility = if (model.leftButtonVisible) View.VISIBLE else View.GONE
        rightButton.visibility = if (model.rightButtonVisible) View.VISIBLE else View.GONE

        val leftButtonType = model.leftButtonType ?: ImageButtonType.Back
        val rightButtonType = model.rightButtonType ?: ImageButtonType.Speaker

        leftButton.setImageResource(leftButtonType.imageResource())
        rightButton.setImageResource(rightButtonType.imageResource())
        leftButton.scaleType = ImageView.ScaleType.FIT_START
        rightButton.scaleType = ImageView.ScaleType.FIT_END

        leftButton.contentDescription = context.getString(leftButtonType.contentDescription())
        rightButton.contentDescription = context.getString(rightButtonType.contentDescription())

        if (rightButtonType == ImageButtonType.NavInfo) {
            val padding = TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    Const.Size.navButtonPaddingNormal * 0.5f,
                    context.resources.displayMetrics).toInt()
            rightButton.setPadding(rightButton.paddingLeft, padding, rightButton.paddingRight, padding)
        }

        listener = model.listener

        // Register for callback listeners.
        leftButton.setOnClickListener { listener?.leftButtonClicked() }
        rightButton.setOnClickListener { listener?.rightButtonClicked() }

        // Update the width of left & right buttons.
        val screenWidth = Resources.getSystem().displayMetrics.widthPixels
        leftButton.layoutParams.width = screenWidth / 2
        rightButton.layoutParams.width = screenWidth / 2

        // Adjust the cell view elements in the case of large style, e.g. landscape mode.
        if (model.largeStyle) {
            // Enlarge the title size.
            title.textSize = Const.Font.titleLarge

            // Enlarge the size of nav buttons.
            var height = TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    Const.Size.navButtonHeightNormal * Const.Size.landscapeScaleFactor,
                    context.resources.displayMetrics).toInt()
            leftButton.layoutParams.height = height
            rightButton.layoutParams.height = height

            // Adjust the top and bottom padding of nav buttons.
            val padding = TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    Const.Size.navButtonPaddingNormal * Const.Size.landscapeScaleFactor,
                    context.resources.displayMetrics).toInt()
            leftButton.setPadding(leftButton.paddingLeft, padding, leftButton.paddingRight, padding)
            if (rightButtonType == ImageButtonType.NavInfo) {
                rightButton.setPadding(rightButton.paddingLeft, padding / 2, rightButton.paddingRight, padding / 2)
            } else {
                rightButton.setPadding(rightButton.paddingLeft, padding, rightButton.paddingRight, padding)
            }

            // Enlarge the height of separator.
            height = TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    Const.Size.separatorThicknessLarge,
                    context.resources.displayMetrics).toInt()
            separator.layoutParams.height = height
        }
    }
}