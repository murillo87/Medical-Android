package com.appamedix.makula.scenes.amslertest.table

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.appamedix.makula.R
import com.appamedix.makula.base.BaseCellViewModel
import com.appamedix.makula.scenes.amslertest.AmslerTestDisplayModel
import com.appamedix.makula.scenes.amslertest.AmslerTestProgressType
import com.appamedix.makula.scenes.amslertest.table.imagecell.AmslerTestImageCellViewModel
import com.appamedix.makula.scenes.menu.table.MenuCellIdentifier
import com.appamedix.makula.types.ImageButtonType
import com.appamedix.makula.types.SceneId
import com.appamedix.makula.views.navigation.NavigationCellModel
import com.appamedix.makula.views.navigation.NavigationCellViewModel
import com.appamedix.makula.views.radiocell.SplitRadioCellModel
import com.appamedix.makula.views.radiocell.SplitRadioCellViewModel
import com.appamedix.makula.views.splitcell.SplitCellModel
import com.appamedix.makula.views.splitcell.SplitCellViewModel
import com.appamedix.makula.views.textcell.StaticTextCellModel
import com.appamedix.makula.views.textcell.StaticTextCellViewModel

class AmslerTestTableDataSource : ViewModel() {

    private var mainCellsMutableData = MutableLiveData<ArrayList<BaseCellViewModel>>()

    /**
     * Returns a mutable live data for the table cells.
     *
     * @param displayModel: The display model.
     * @return A mutable live data list for the cells.
     */
    fun getMainCellData(displayModel: AmslerTestDisplayModel): MutableLiveData<ArrayList<BaseCellViewModel>> {
        // Initialize data.
        var tableData: List<BaseCellViewModel> = listOf()

        // Add the navigation view as first cell in large style.
        if (displayModel.largeStyle) {
            val navigation = createNavigationCellData(displayModel)
            tableData += navigation
        }

        // Add an image cell showing a grid image.
        val imageCell = createGridImageCellData()
        tableData += imageCell

        // Add a text cell for the title.
        val textCell = createTextCellData(displayModel)
        tableData += textCell

        // Add a split cell for the titles.
        val splitCell = createSplitCellData(displayModel)
        tableData += splitCell

        // Add the split radio button cells for the progress types.
        val progressCells = createProgressCellData(displayModel)
        tableData += progressCells

        mainCellsMutableData.value = ArrayList(tableData)
        return mainCellsMutableData
    }

    /**
     * Creates a navigation cell data.
     *
     * @param displayModel: The display model.
     * @return The navigation view model.
     */
    private fun createNavigationCellData(displayModel: AmslerTestDisplayModel): List<NavigationCellViewModel> {
        val activity = displayModel.activity
        val title = activity.getString(R.string.amslerTestTitle)
        val navigationCellModel = NavigationCellModel(
                title,
                R.color.white,
                true,
                false,
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
     * Creates a grid image cell data.
     *
     * @return The grid cell view model.
     */
    private fun createGridImageCellData(): List<AmslerTestImageCellViewModel> {
        return arrayListOf(AmslerTestImageCellViewModel())
    }

    /**
     * Creates a text cell data.
     *
     * @param displayModel: The display model.
     * @return A text cell view model.
     */
    private fun createTextCellData(displayModel: AmslerTestDisplayModel): List<StaticTextCellViewModel> {
        val title = displayModel.activity.getString(R.string.amslerTestTextCell)
        val textCellModel = StaticTextCellModel(
                MenuCellIdentifier.NoMenu,
                SceneId.Other,
                title,
                null,
                displayModel.largeStyle,
                R.color.lightMain,
                null,
                R.color.darkMain,
                true,
                null,
                null,
                true
        )
        val textCellViewModel = StaticTextCellViewModel(textCellModel)

        return arrayListOf(textCellViewModel)
    }

    /**
     * Creates a split cell data.
     *
     * @param displayModel: The display model.
     * @return A split cell view model.
     */
    private fun createSplitCellData(displayModel: AmslerTestDisplayModel): List<SplitCellViewModel> {
        val leftTitle = displayModel.activity.getString(R.string.amslerTestSplitCellLeft)
        val rightTitle = displayModel.activity.getString(R.string.amslerTestSplitCellRight)
        val splitCell = SplitCellModel(
                leftTitle,
                rightTitle,
                null,
                false,
                false,
                displayModel.largeStyle,
                true,
                R.color.darkMain,
                R.color.lightMain,
                null
        )
        val splitCellViewModel = SplitCellViewModel(splitCell)

        return arrayListOf(splitCellViewModel)
    }

    /**
     * Creates the split radio cells for the amslertest progress.
     *
     * @param displayModel: The display model.
     * @return The list of split radio cell view model.
     */
    private fun createProgressCellData(displayModel: AmslerTestDisplayModel): List<SplitRadioCellViewModel> {
        val cellData = arrayListOf(
                AmslerTestProgressType.Equal,
                AmslerTestProgressType.Better,
                AmslerTestProgressType.Worse)

        val splitRadioCellModels = cellData.map {
            SplitRadioCellModel(
                    it,
                    displayModel.activity.getString(it.titleText()),
                    displayModel.amslerTestObject?.getProgressTypeLeft() == it,
                    displayModel.amslerTestObject?.getProgressTypeRight() == it,
                    displayModel.largeStyle,
                    false,
                    displayModel.activity
            )
        }

        return splitRadioCellModels.map {
            SplitRadioCellViewModel(it)
        }
    }
}