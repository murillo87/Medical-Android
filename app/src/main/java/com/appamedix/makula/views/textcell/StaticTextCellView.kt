package com.appamedix.makula.views.textcell

import android.content.Context
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.util.TypedValue
import android.view.View
import android.widget.TextView
import com.appamedix.makula.R
import com.appamedix.makula.constants.Const
import com.appamedix.makula.global.listener.CellItemListener
import com.appamedix.makula.types.ViewCellType
import com.appamedix.makula.utils.Run

/* The custom class for the static text cell view. */
class StaticTextCellView(private var context: Context, private val view: View)
    : RecyclerView.ViewHolder(view), View.OnClickListener {

    // The UI elements to consist this cell view.
    private val contentTitle: TextView = view.findViewById(R.id.text_cell_content_title)
    private val separator: View = view.findViewById(R.id.text_cell_separator)
    private lateinit var cellModel: StaticTextCellViewModel
    private var clickListener: CellItemListener? = null

    /**
     * Sets up the cell view.
     *
     * @param model: The view model for this cell.
     * @param clickListener: The click listener for this cell view.
     */
    fun bindItems(model: StaticTextCellViewModel, clickListener: CellItemListener?) {
        // Apply model and listener.
        cellModel = model
        this.clickListener = clickListener

        val backgroundColor = ContextCompat.getColor(context, model.backgroundColor)
        view.setBackgroundColor(backgroundColor)
        view.setOnClickListener(this)

        val defaultColor = ContextCompat.getColor(context, model.defaultColor)
        contentTitle.text = model.cellTitle
        contentTitle.setTextColor(defaultColor)

        val separatorColor = ContextCompat.getColor(context, model.separatorDefaultColor ?: model.defaultColor)
        separator.visibility = if (model.separatorVisible) View.VISIBLE else View.GONE
        separator.setBackgroundColor(separatorColor)

        // Adjust UI elements in the case of large style, e.g. landscape.
        if (model.largeFont) {
            // Enlarge the text size of the title view.
            contentTitle.textSize = Const.Font.headlineLarge

            // Enlarge the height of separator.
            val height = TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    Const.Size.separatorThicknessLarge,
                    context.resources.displayMetrics).toInt()
            separator.layoutParams.height = height
        }
    }

    override fun onClick(v: View?) {
        if (cellModel.disable) { return }

        clickListener?.let {
            applyHighlightColor()
            Run.after(Const.Time.defaultAnimationDuration) {
                applyDefaultColor()
            }
            it.onItemClicked(cellModel, ViewCellType.StaticTextCell)
        }
    }

    /**
     * Sets the color of the views depending on the current style state.
     */
    fun applyDefaultColor() {
        val textColor = ContextCompat.getColor(context, cellModel.defaultColor)
        contentTitle.setTextColor(textColor)

        val separatorColor = ContextCompat.getColor(context, cellModel.separatorDefaultColor ?: cellModel.defaultColor)
        separator.setBackgroundColor(separatorColor)
    }

    /**
     * Sets the color of the views for the selected state.
     */
    fun applyHighlightColor() {
        val textColor = ContextCompat.getColor(context, cellModel.highlightColor ?: cellModel.defaultColor)
        contentTitle.setTextColor(textColor)

        val separatorColor = ContextCompat.getColor(context, cellModel.separatorHighlightColor ?: cellModel.defaultColor)
        separator.setBackgroundColor(separatorColor)
    }
}