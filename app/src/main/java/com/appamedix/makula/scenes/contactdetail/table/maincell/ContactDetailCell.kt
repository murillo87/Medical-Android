package com.appamedix.makula.scenes.contactdetail.table.maincell

import android.content.Context
import android.graphics.PorterDuff
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.text.InputType
import android.util.TypedValue
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.*
import com.appamedix.makula.R
import com.appamedix.makula.constants.Const
import com.appamedix.makula.scenes.contactdetail.ContactInfoType
import com.appamedix.makula.utils.Run
import com.daimajia.swipe.SwipeLayout

/* The custom class for the contact detail cell view */
class ContactDetailCell(private val context: Context, view: View)
    : RecyclerView.ViewHolder(view), View.OnClickListener {

    // The UI elements to consist this cell view.
    val swipeLayout: SwipeLayout = view.findViewById(R.id.swipe_contact_detail)
    val leftDelete: TextView = view.findViewById(R.id.left_delete)
    val rightDelete: TextView = view.findViewById(R.id.right_delete)
    val contentEntry: EditText = view.findViewById(R.id.contact_detail_cell_entry)

    private val leftMenu: LinearLayout = view.findViewById(R.id.left_menu)
    private val rightMenu: LinearLayout = view.findViewById(R.id.right_menu)
    private val contentTitle: TextView = view.findViewById(R.id.contact_detail_cell_title)
    private val arrowIcon: ImageView = view.findViewById(R.id.arrow_icon)
    private val separator: View = view.findViewById(R.id.contact_detail_cell_separator)
    private val dragButton: ImageButton = view.findViewById(R.id.contact_detail_drag)

    private lateinit var cellModel: ContactDetailCellViewModel
    private var listener: ContactDetailCellListener? = null

    fun bindItems(model: ContactDetailCellViewModel, listener: ContactDetailCellListener?) {
        this.cellModel = model
        this.listener = listener

        // Setup views
        val defaultColor = ContextCompat.getColor(context, model.defaultColor)
        contentTitle.setTextColor(defaultColor)
        contentEntry.setTextColor(defaultColor)
        contentEntry.setHintTextColor(defaultColor)

        when (model.type) {
            ContactInfoType.Mobile, ContactInfoType.Phone -> {
                contentEntry.inputType = InputType.TYPE_CLASS_PHONE
            }
            ContactInfoType.Email -> {
                contentEntry.inputType = InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
            }
            else -> {
                contentEntry.inputType = InputType.TYPE_CLASS_TEXT
            }
        }

        if (model.title.isNullOrEmpty()) {
            contentTitle.visibility = View.GONE
            contentEntry.visibility = View.VISIBLE
            contentEntry.setHint(model.type.defaultString())
        } else {
            contentTitle.visibility = View.VISIBLE
            contentEntry.visibility = View.GONE
            contentTitle.text = model.title
        }

        separator.setBackgroundColor(defaultColor)
        dragButton.visibility = if (model.editable) View.VISIBLE else View.GONE
        dragButton.setColorFilter(defaultColor, PorterDuff.Mode.SRC_IN)

        arrowIcon.visibility = if (model.actable) View.VISIBLE else View.GONE
        arrowIcon.setColorFilter(defaultColor, PorterDuff.Mode.SRC_IN)
        arrowIcon.scaleType = ImageView.ScaleType.FIT_CENTER

        // Register for callback listeners.
        swipeLayout.surfaceView.setOnClickListener(this)
        dragButton.setOnClickListener {
            if (swipeLayout.openStatus == SwipeLayout.Status.Open)
                swipeLayout.close()
            else
                swipeLayout.open(SwipeLayout.DragEdge.Left)
        }

        contentEntry.setOnFocusChangeListener { view, hasFocus ->
            if (hasFocus) {
                // Clear the hint text.
                contentEntry.hint = ""
            } else {
                // Restore hint text and save the content.
                contentEntry.setHint(model.type.defaultString())
                cellModel.title = contentEntry.text.toString().trim()
                listener?.editTextDidEndEditing(cellModel)
                hideKeyboard(view)
            }
        }
        contentEntry.setOnEditorActionListener { view, actionId, event ->
            if ((actionId == EditorInfo.IME_ACTION_DONE) || (event.keyCode == KeyEvent.KEYCODE_ENTER)) {
                cellModel.title = contentEntry.text.toString().trim()
                listener?.editTextDidEndEditing(cellModel)
                hideKeyboard(view)
            }
            false
        }

        // Adjust UI elements in the case of large style, e.g. landscape.
        if (model.largeStyle) {
            // Enlarge the text size of the title view.
            contentTitle.textSize = Const.Font.headlineLarge
            contentEntry.textSize = Const.Font.headlineLarge
            leftDelete.textSize = Const.Font.content1Large
            rightDelete.textSize = Const.Font.content1Large

            // Enlarge the drag button size, width is not changed.
            var sizeValue = TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    Const.Size.dragButtonHeightNormal * Const.Size.landscapeScaleFactor,
                    context.resources.displayMetrics).toInt()
            dragButton.layoutParams.height = sizeValue

            // Enlarge the arrow icon size.
            sizeValue = TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    Const.Size.arrowIconHeightNormal * Const.Size.landscapeScaleFactor,
                    context.resources.displayMetrics).toInt()
            arrowIcon.layoutParams.width = sizeValue
            arrowIcon.layoutParams.height = sizeValue

            // Enlarge the size of swipe menu buttons.
            sizeValue = TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    Const.Size.swipeMenuWidthNormal * Const.Size.landscapeScaleFactor,
                    context.resources.displayMetrics).toInt()
            leftMenu.layoutParams.width = sizeValue
            rightMenu.layoutParams.width = sizeValue

            // Enlarge the height of separator.
            sizeValue = TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    Const.Size.separatorThicknessLarge,
                    context.resources.displayMetrics).toInt()
            separator.layoutParams.height = sizeValue
        }
    }

    override fun onClick(v: View?) {
        swipeLayout.close()
        if (!cellModel.actable) return

        listener?.let {
            applyHighlightColor()
            Run.after(Const.Time.defaultAnimationDuration) {
                applyDefaultColor()
            }
            it.onCellItemClicked(cellModel)
        }
    }

    /**
     * Sets the color of the views depending on the current style state.
     */
    private fun applyDefaultColor() {
        val defaultColor = ContextCompat.getColor(context, cellModel.defaultColor)

        contentTitle.setTextColor(defaultColor)
        arrowIcon.setColorFilter(defaultColor, PorterDuff.Mode.SRC_IN)
        separator.setBackgroundColor(defaultColor)
    }

    /**
     * Sets the color of the views for the selected state.
     */
    private fun applyHighlightColor() {
        val highlightColor = ContextCompat.getColor(context, cellModel.highlightColor)

        contentTitle.setTextColor(highlightColor)
        arrowIcon.setColorFilter(highlightColor, PorterDuff.Mode.SRC_IN)
        separator.setBackgroundColor(highlightColor)
    }

    /**
     * Sets the text hint color as original.
     */
    fun applySpeechDefaultColor() {
        val defaultColor = ContextCompat.getColor(context, cellModel.defaultColor)
        contentTitle.setTextColor(defaultColor)
        contentEntry.setHintTextColor(defaultColor)
    }

    /**
     * Highlights the text hint while speaking.
     */
    fun applySpeechHighlightColor() {
        val highlightColor = ContextCompat.getColor(context, cellModel.highlightColor)
        contentTitle.setTextColor(highlightColor)
        contentEntry.setHintTextColor(highlightColor)
    }

    /**
     * Hides the keyboard.
     *
     * @param view: The focused view.
     */
    private fun hideKeyboard(view: View) {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }
}