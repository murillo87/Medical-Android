package com.appamedix.makula.scenes.reminder.table

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.appamedix.makula.R
import com.appamedix.makula.base.BaseCellViewModel
import com.appamedix.makula.scenes.reminder.ReminderDisplayModel
import com.appamedix.makula.scenes.reminder.table.checkboxcell.ReminderCheckboxCellModel
import com.appamedix.makula.scenes.reminder.table.checkboxcell.ReminderCheckboxCellViewModel
import com.appamedix.makula.scenes.reminder.table.pickercell.ReminderPickerCellModel
import com.appamedix.makula.scenes.reminder.table.pickercell.ReminderPickerCellViewModel
import com.appamedix.makula.types.ImageButtonType
import com.appamedix.makula.views.navigation.NavigationCellModel
import com.appamedix.makula.views.navigation.NavigationCellViewModel

class ReminderTableDataSource : ViewModel() {

    private var mainCellsMutableData = MutableLiveData<ArrayList<BaseCellViewModel>>()

    /**
     * Returns a mutable live data for the table cells.
     *
     * @param displayModel: The display model.
     * @return A mutable live data list for the cells.
     */
    fun getMainCellData(displayModel: ReminderDisplayModel): MutableLiveData<ArrayList<BaseCellViewModel>> {
        // Initialize data.
        var tableData: List<BaseCellViewModel> = listOf()

        // Add the navigation view as first cell in large style.
        if (displayModel.largeStyle) {
            val navigation = createNavigationCellData(displayModel)
            tableData += navigation
        }

        // Add the checkbox cell.
        val checkboxCell = createCheckboxCellData(displayModel)
        tableData += checkboxCell

        // Add the picker cell.
        val pickerCell = createPickerCellData(displayModel)
        tableData += pickerCell

        mainCellsMutableData.value = ArrayList(tableData)
        return mainCellsMutableData
    }

    /**
     * Creates a navigation cell data.
     *
     * @param displayModel: The display model.
     * @return The navigation view model.
     */
    private fun createNavigationCellData(displayModel: ReminderDisplayModel): List<NavigationCellViewModel> {
        val activity = displayModel.activity
        val title = activity.getString(R.string.reminderTitle)
        val navigationCellModel = NavigationCellModel(
                title,
                R.color.white,
                true,
                true,
                true,
                ImageButtonType.Back,
                false,
                ImageButtonType.Speaker,
                activity
        )
        val navigationCellViewModel = NavigationCellViewModel(navigationCellModel)

        return arrayListOf(navigationCellViewModel)
    }

    /**
     * Creates a reminder checkbox cell data.
     *
     * @param displayModel: The display model.
     * @return The reminder checkbox cell view model.
     */
    private fun createCheckboxCellData(displayModel: ReminderDisplayModel): List<ReminderCheckboxCellViewModel> {
        val activity = displayModel.activity
        val checkboxCellModel = ReminderCheckboxCellModel(
                displayModel.largeStyle,
                displayModel.checkboxChecked,
                activity
        )
        val checkboxCellViewModel = ReminderCheckboxCellViewModel(checkboxCellModel)

        return arrayListOf(checkboxCellViewModel)
    }

    /**
     * Creates a reminder picker cell data.
     *
     * @param displayModel: The display model.
     * @return The reminder picker cell view model.
     */
    private fun createPickerCellData(displayModel: ReminderDisplayModel): List<ReminderPickerCellViewModel> {
        val activity = displayModel.activity
        val pickerCellModel = ReminderPickerCellModel(
                displayModel.largeStyle,
                displayModel.pickerValue,
                activity
        )
        val pickerCellViewModel = ReminderPickerCellViewModel(pickerCellModel)

        return arrayListOf(pickerCellViewModel)
    }
}