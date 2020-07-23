package com.appamedix.makula.scenes.visusnhd.table

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.appamedix.makula.R
import com.appamedix.makula.base.BaseCellViewModel
import com.appamedix.makula.scenes.visusnhd.VisusNhdInputDisplayModel
import com.appamedix.makula.scenes.visusnhd.table.pickercell.VisusNhdInputPickerCellModel
import com.appamedix.makula.scenes.visusnhd.table.pickercell.VisusNhdInputPickerCellViewModel
import com.appamedix.makula.types.ImageButtonType
import com.appamedix.makula.views.navigation.NavigationCellModel
import com.appamedix.makula.views.navigation.NavigationCellViewModel
import com.appamedix.makula.views.splitcell.SplitCellModel
import com.appamedix.makula.views.splitcell.SplitCellViewModel

class VisusNhdInputTableDataSource : ViewModel() {

    private var mainCellsMutableData = MutableLiveData<ArrayList<BaseCellViewModel>>()

    /**
     * Returns a mutable live data for the table cells.
     *
     * @param displayModel: The display model.
     * @return A mutable live data list for the cells.
     */
    fun getMainCellData(displayModel: VisusNhdInputDisplayModel): MutableLiveData<ArrayList<BaseCellViewModel>> {
        // Initialize data.
        var tableData: List<BaseCellViewModel> = listOf()

        // Add the navigation view as first cell in large style.
        if (displayModel.largeStyle) {
            val navigation = createNavigationCellData(displayModel)
            tableData += navigation
        }

        // Add split cells.
        val splitCells = createSplitCellData(displayModel)
        tableData += splitCells

        // Add the picker cell if the value is set.
        if (displayModel.leftSelected || displayModel.rightSelected) {
            val pickerCell = createPickerCellData(displayModel)
            tableData += pickerCell
        }

        mainCellsMutableData.value = ArrayList(tableData)
        return mainCellsMutableData
    }

    /**
     * Creates a navigation cell data.
     *
     * @param displayModel: The display model.
     * @return The navigation view model.
     */
    private fun createNavigationCellData(displayModel: VisusNhdInputDisplayModel): List<NavigationCellViewModel> {
        val activity = displayModel.activity
        val title = activity.getString(displayModel.sceneType.titleString())
        val navigationCellModel = NavigationCellModel(
                title,
                R.color.white,
                true,
                true,
                true,
                ImageButtonType.Back,
                true,
                ImageButtonType.NavInfo,
                activity
        )
        val navigationCellViewModel = NavigationCellViewModel(navigationCellModel)

        return arrayListOf(navigationCellViewModel)
    }

    /**
     * Creates split cell data.
     *
     * @param displayModel: The display model.
     * @return The split cell view model.
     */
    private fun createSplitCellData(displayModel: VisusNhdInputDisplayModel): List<SplitCellViewModel> {
        val activity = displayModel.activity

        val leftMenuTitle = activity.getString(R.string.visusNhdCellLeft)
        val rightMenuTitle = activity.getString(R.string.visusNhdCellRight)
        val splitMenuCellModel = SplitCellModel(
                leftMenuTitle,
                rightMenuTitle,
                null,
                displayModel.leftSelected,
                displayModel.rightSelected,
                displayModel.largeStyle,
                false,
                R.color.darkMain,
                R.color.lightMain,
                activity
        )
        val splitMenuCellViewModel = SplitCellViewModel(splitMenuCellModel)

        val leftValueTitle = displayModel.sceneType.valueOutput(activity, displayModel.leftValue)
        val rightValueTitle = displayModel.sceneType.valueOutput(activity, displayModel.rightValue)
        val splitValueCellModel = SplitCellModel(
                leftValueTitle,
                rightValueTitle,
                null,
                displayModel.leftSelected,
                displayModel.rightSelected,
                displayModel.largeStyle,
                true,
                R.color.darkMain,
                R.color.lightMain,
                activity
        )
        val splitValueCellViewModel = SplitCellViewModel(splitValueCellModel)

        return arrayListOf(splitMenuCellViewModel, splitValueCellViewModel)
    }

    /**
     * Creates a visus/NHD input picker cell data.
     *
     * @param displayModel: The display model.
     * @return The visus/NHD input picker cell view model.
     */
    private fun createPickerCellData(displayModel: VisusNhdInputDisplayModel): List<VisusNhdInputPickerCellViewModel> {
        val activity = displayModel.activity
        val value = when {
            displayModel.leftSelected -> displayModel.leftValue ?: displayModel.sceneType.middleValue()
            displayModel.rightSelected -> displayModel.rightValue ?: displayModel.sceneType.middleValue()
            else -> throw IllegalArgumentException("Invalid case")
        }
        val cellModel = VisusNhdInputPickerCellModel(
                displayModel.largeStyle,
                displayModel.sceneType,
                value,
                activity
        )
        val cellViewModel = VisusNhdInputPickerCellViewModel(cellModel)

        return arrayListOf(cellViewModel)
    }
}