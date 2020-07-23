package com.appamedix.makula.scenes.search.table.inputcell

import android.content.Context
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.util.TypedValue
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import com.appamedix.makula.R
import com.appamedix.makula.constants.Const

class SearchInputCell(private val context: Context, view: View) :
        RecyclerView.ViewHolder(view) {

    // The UI elements to consist this cell view.
    private val titleEntry: EditText = view.findViewById(R.id.input_entry)
    private val separator: View = view.findViewById(R.id.input_cell_separator)

    private var listener: SearchInputCellListener? = null

    fun bindItems(model: SearchInputCellViewModel) {
        this.listener = model.listener
        titleEntry.hint = context.getText(R.string.searchInputCellPlaceholder)
        titleEntry.setText(model.searchText)

        val colorValue = ContextCompat.getColor(context, R.color.white)
        titleEntry.setTextColor(colorValue)
        titleEntry.setHintTextColor(colorValue)
        separator.setBackgroundColor(colorValue)

        // Register for callback listeners.
        titleEntry.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                // Clear the hint text.
                titleEntry.hint = ""
            }
        }
        titleEntry.setOnEditorActionListener { view, actionId, event ->
            if ((actionId == EditorInfo.IME_ACTION_DONE) || (event.keyCode == KeyEvent.KEYCODE_ENTER)) {
                titleEntry.hint = context.getText(R.string.searchInputCellPlaceholder)
                titleEntry.clearFocus()

                val content = titleEntry.text.toString().trim()
                listener?.editTextDidEndEditing(content)

                titleEntry.text = null
                hideKeyboard(view)
            }
            false
        }

        if (model.largeStyle) {
            // Enlarge the text size.
            titleEntry.textSize = Const.Font.headlineLarge

            // Enlarge the height of separator.
            val height = TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    Const.Size.separatorThicknessLarge,
                    context.resources.displayMetrics).toInt()
            separator.layoutParams.height = height
        }
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