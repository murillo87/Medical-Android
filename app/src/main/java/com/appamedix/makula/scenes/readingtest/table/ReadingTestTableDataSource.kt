package com.appamedix.makula.scenes.readingtest.table

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.appamedix.makula.R
import com.appamedix.makula.base.BaseCellViewModel
import com.appamedix.makula.scenes.readingtest.ReadingTestDisplayModel
import com.appamedix.makula.scenes.readingtest.ReadingTestMagnitudeType
import com.appamedix.makula.scenes.readingtest.table.readingcell.ReadingTestCellModel
import com.appamedix.makula.scenes.readingtest.table.readingcell.ReadingTestCellViewModel
import com.appamedix.makula.types.ImageButtonType
import com.appamedix.makula.views.navigation.NavigationCellModel
import com.appamedix.makula.views.navigation.NavigationCellViewModel
import com.appamedix.makula.views.splitcell.SplitCellModel
import com.appamedix.makula.views.splitcell.SplitCellViewModel

class ReadingTestTableDataSource : ViewModel() {

    private var mainCellsMutableData = MutableLiveData<ArrayList<BaseCellViewModel>>()

    /**
     * Returns a mutable live data for the table cells.
     *
     * @param displayModel: The display model.
     * @return A mutable live data list for the cells.
     */
    fun getMainCellData(displayModel: ReadingTestDisplayModel): MutableLiveData<ArrayList<BaseCellViewModel>> {
        // Initialize data.
        var tableData: List<BaseCellViewModel> = listOf()

        // Add the navigation view as first cell in large style.
        if (displayModel.largeStyle) {
            val navigation = createNavigationCellData(displayModel)
            tableData += navigation
        }

        // Add a split cell for the titles.
        val splitCell = createSplitCellData(displayModel)
        tableData += splitCell

        // Add the reading test cells for the magnitude types.
        val magnitudeCells = createReadingTestCellData(displayModel)
        tableData += magnitudeCells

        mainCellsMutableData.value = ArrayList(tableData)
        return mainCellsMutableData
    }

    /**
     * Creates a navigation cell data.
     *
     * @param displayModel: The display model.
     * @return The navigation view model.
     */
    private fun createNavigationCellData(displayModel: ReadingTestDisplayModel): List<NavigationCellViewModel> {
        val activity = displayModel.activity
        val title = activity.getString(R.string.readingTestTitle)
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
     * Creates a split cell data.
     *
     * @param displayModel: The display model.
     * @return A split cell view model.
     */
    private fun createSplitCellData(displayModel: ReadingTestDisplayModel): List<SplitCellViewModel> {
        val leftTitle = displayModel.activity.getString(R.string.readingTestSplitCellLeft)
        val rightTitle = displayModel.activity.getString(R.string.readingTestSplitCellRight)
        val splitCell = SplitCellModel(
                leftTitle,
                rightTitle,
                null,
                false,
                false,
                displayModel.largeStyle,
                true,
                R.color.white,
                R.color.darkMain,
                null
        )
        val splitCellViewModel = SplitCellViewModel(splitCell)

        return arrayListOf(splitCellViewModel)
    }

    /**
     * Creates the magnitude cells for the reading test.
     *
     * @param displayModel: The display model.
     * @return The list of reading test cell view model.
     */
    private fun createReadingTestCellData(displayModel: ReadingTestDisplayModel): List<ReadingTestCellViewModel> {
        val cellData = arrayListOf(
                ReadingTestMagnitudeType.Big,
                ReadingTestMagnitudeType.Large,
                ReadingTestMagnitudeType.Medium,
                ReadingTestMagnitudeType.Small,
                ReadingTestMagnitudeType.Little,
                ReadingTestMagnitudeType.Tiny
        )

        val readingTestCellModels = cellData.map {
            ReadingTestCellModel(
                    it,
                    displayModel.activity.getString(it.contentText()),
                    displayModel.readingTestObject?.getMagnitudeTypeLeft() == it,
                    displayModel.readingTestObject?.getMagnitudeTypeRight() == it,
                    displayModel.largeStyle,
                    displayModel.activity
            )
        }

        return readingTestCellModels.map {
            ReadingTestCellViewModel(it)
        }
    }
}