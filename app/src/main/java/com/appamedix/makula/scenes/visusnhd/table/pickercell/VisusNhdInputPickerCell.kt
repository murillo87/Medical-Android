package com.appamedix.makula.scenes.visusnhd.table.pickercell

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.NumberPicker
import com.appamedix.makula.R
import com.appamedix.makula.scenes.visusnhd.VisusNhdInputConst
import com.appamedix.makula.types.VisusNhdType
import kotlin.math.max
import kotlin.math.min

/* The custom class for the visus/nhd input picker cell view */
class VisusNhdInputPickerCell(private val context: Context, view: View)
    : RecyclerView.ViewHolder(view) {

    // The UI elements to consist this cell view.
    private val pickerLeft: NumberPicker = view.findViewById(R.id.number_picker_left)
    private val pickerMiddle: NumberPicker = view.findViewById(R.id.number_picker_middle)
    private val pickerRight: NumberPicker = view.findViewById(R.id.number_picker_right)

    private lateinit var cellModel: VisusNhdInputPickerCellViewModel

    // The currently selected value for the picker type shown in the picker (or at least close to it).
    private var selectedValue: Int
        get() = when (cellModel.type) {
            VisusNhdType.Visus -> pickerMiddle.value
            VisusNhdType.Nhd -> {
                pickerLeft.value * 100 + pickerMiddle.value * 10 + pickerRight.value
            }
        }
        set(value) {
            setPickerValue(value)
        }

    fun bindItems(model: VisusNhdInputPickerCellViewModel) {
        this.cellModel = model

        // Setup views
        pickerLeft.minValue = 2
        pickerLeft.maxValue = 4
        pickerRight.minValue = 0
        pickerRight.maxValue = 9

        if (cellModel.type == VisusNhdType.Visus) {
            pickerLeft.visibility = View.GONE
            pickerRight.visibility = View.GONE

            pickerMiddle.minValue = 0
            pickerMiddle.maxValue = 12
            pickerMiddle.displayedValues = VisusNhdInputConst.visusValues
        } else {
            pickerLeft.visibility = View.VISIBLE
            pickerRight.visibility = View.VISIBLE

            pickerMiddle.minValue = 0
            pickerMiddle.maxValue = 9
        }

        pickerLeft.wrapSelectorWheel = false
        pickerMiddle.wrapSelectorWheel = false
        pickerRight.wrapSelectorWheel = false

        selectedValue = cellModel.value

        // Register for callback listeners.
        pickerLeft.setOnValueChangedListener { _, _, _ ->
            pickerValueChanged()
        }
        pickerMiddle.setOnValueChangedListener { _, _, _ ->
            pickerValueChanged()
        }
        pickerRight.setOnValueChangedListener { _, _, _ ->
            pickerValueChanged()
        }
    }

    /**
     * Sets a value to show it in the pickers.
     *
     * @param newValue: The value to set.
     */
    private fun setPickerValue(newValue: Int) {
        val value = min(max(newValue, cellModel.type.minValue()), cellModel.type.maxValue())
        when (cellModel.type) {
            VisusNhdType.Visus -> {
                pickerMiddle.value = value
            }
            VisusNhdType.Nhd -> {
                pickerLeft.value = value / 100
                pickerMiddle.value = (value % 100) / 10
                pickerRight.value = value % 10
            }
        }
    }

    /**
     * Checks if the value is in range and informs to listener.
     * If the value is out of range, adjusts the picker value.
     */
    private fun pickerValueChanged() {
        // Crop the value to fit into bounds.
        val value = selectedValue
        val valueInRange = min(max(value, cellModel.type.minValue()), cellModel.type.maxValue())
        if (valueInRange != value) {
            setPickerValue(valueInRange)
        }

        // Inform to listener about value change.
        cellModel.listener?.pickerValueChanged(valueInRange)
    }
}