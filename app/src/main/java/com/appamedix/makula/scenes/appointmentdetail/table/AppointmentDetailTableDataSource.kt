package com.appamedix.makula.scenes.appointmentdetail.table

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.appamedix.makula.R
import com.appamedix.makula.base.BaseCellViewModel
import com.appamedix.makula.scenes.amslertest.AmslerTestProgressType
import com.appamedix.makula.scenes.appointmentdetail.AppointmentDetailDisplayModel
import com.appamedix.makula.scenes.menu.table.MenuCellIdentifier
import com.appamedix.makula.types.*
import com.appamedix.makula.utils.DateUtils
import com.appamedix.makula.views.navigation.NavigationCellModel
import com.appamedix.makula.views.navigation.NavigationCellViewModel
import com.appamedix.makula.views.radiocell.SplitRadioCellModel
import com.appamedix.makula.views.radiocell.SplitRadioCellViewModel
import com.appamedix.makula.views.splitcell.SplitCellModel
import com.appamedix.makula.views.splitcell.SplitCellViewModel
import com.appamedix.makula.views.textcell.StaticTextCellModel
import com.appamedix.makula.views.textcell.StaticTextCellViewModel
import com.appamedix.makula.worker.datamodel.amslertest.getAmslerTestObjects
import com.appamedix.makula.worker.datamodel.appointment.getAppointmentObjects
import com.appamedix.makula.worker.datamodel.histogram.getNhdObjects
import com.appamedix.makula.worker.datamodel.histogram.getVisusObjects
import com.appamedix.makula.worker.datamodel.readingtest.getReadingTestObjects
import com.appamedix.makula.worker.speech.SpeechSynthesizer
import io.realm.Realm

class AppointmentDetailTableDataSource : ViewModel() {

    private var mainCellsMutableData = MutableLiveData<ArrayList<BaseCellViewModel>>()
    var realm: Realm? = null

    /**
     * Returns a mutable live data for the table cells.
     *
     * @param displayModel: The display model.
     * @return A mutable live data list for the cells.
     */
    fun getMainCellData(displayModel: AppointmentDetailDisplayModel): MutableLiveData<ArrayList<BaseCellViewModel>> {
        // Initialize data.
        var tableData: List<BaseCellViewModel> = listOf()

        // Add the navigation view as first cell in large style.
        if (displayModel.largeStyle) {
            val navigation = createNavigationCellData(displayModel)
            tableData += navigation
        }

        // Add the treatment appointment cells.
        val treatments = createAppointmentCellData(displayModel, AppointmentType.Treatment)
        treatments?.let { tableData += it }

        // Add the visus/NHD introduction cell.
        val visusNhdHeader = createNhdVisusHeaderCellData(displayModel)
        visusNhdHeader?.let { tableData += it }

        // Add the visus cells.
        val visusCells = createVisusCellData(displayModel)
        visusCells?.let { tableData += it }

        // Add the NHD cells.
        val nhdCells = createNhdCellData(displayModel)
        nhdCells?.let { tableData += it }

        // Add the aftercare appointment cells if it exists.
        val aftercares = createAppointmentCellData(displayModel, AppointmentType.Aftercare)
        aftercares?.let { tableData += it }

        // Add the oct-check appointment cells if it exists.
        val octChecks = createAppointmentCellData(displayModel, AppointmentType.OctCheck)
        octChecks?.let { tableData += it }

        // Add the other appointment cells if it exists.
        val others = createAppointmentCellData(displayModel, AppointmentType.Other)
        others?.let { tableData += it }

        // Add the amslertest cells if it exists.
        val amslerTestCells = createAmslertestCellData(displayModel)
        amslerTestCells?.let { tableData += it }

        // Add the reading test cells if it exists.
        val readingTextCells = createReadingTestCellData(displayModel)
        readingTextCells?.let { tableData += it }

        // Add the note cell.
        val noteCell = createNoteCellData(displayModel)
        tableData += noteCell

        // Add the delete cell.
        val deleteCell = createDeleteCellData(displayModel)
        tableData += deleteCell

        mainCellsMutableData.value = ArrayList(tableData)
        return mainCellsMutableData
    }

    /**
     * Returns a mutable live data for the speech.
     *
     * @param displayModel: The display model.
     * @return A data list for the speech.
     */
    fun getMainCellSpeechData(displayModel: AppointmentDetailDisplayModel): ArrayList<SpeechSynthesizer.SpeechData> {
        // Initialize data.
        val tableSpeechData: ArrayList<SpeechSynthesizer.SpeechData> = arrayListOf()
        val cellViewModels = mainCellsMutableData.value ?: arrayListOf()

        // Make speech data
        val activity = displayModel.activity
        var index = if (displayModel.largeStyle) 1 else 0
        for (model in cellViewModels) {
            val text = when (model.cellType) {
                ViewCellType.StaticTextCell -> {
                    (model as StaticTextCellViewModel).speechTitle
                }
                ViewCellType.SplitCell -> {
                    (model as SplitCellViewModel).speechText
                }
                ViewCellType.SplitRadioCell -> {
                    val cellModel = model as SplitRadioCellViewModel
                    activity.getString(cellModel.progressType.titleSpeechText())
                }
                else -> ""
            } ?: ""

            val speechData = SpeechSynthesizer.SpeechData(text, index)
            tableSpeechData.add(speechData)

            index += 1
        }

        return tableSpeechData
    }

    /**
     * Creates a navigation cell data.
     *
     * @param displayModel: The display model.
     * @return The navigation view model.
     */
    private fun createNavigationCellData(displayModel: AppointmentDetailDisplayModel): List<NavigationCellViewModel> {
        val navigationCellModel = NavigationCellModel(
                displayModel.title,
                displayModel.titleColor,
                true,
                true,
                true,
                ImageButtonType.Back,
                true,
                ImageButtonType.Speaker,
                displayModel.activity
        )
        val navigationCellViewModel = NavigationCellViewModel(navigationCellModel)

        return arrayListOf(navigationCellViewModel)
    }

    /**
     * Creates appointment cell data with the given appointment type.
     *
     * @param displayModel: The display model.
     * @param type: The appointment type.
     * @return The list of appointment cell view model.
     */
    private fun createAppointmentCellData(displayModel: AppointmentDetailDisplayModel,
                                          type: AppointmentType)
            : List<StaticTextCellViewModel>? {
        // Retrieve model from database.
        val realm = realm ?: return null
        val appointments = realm.getAppointmentObjects(displayModel.date, type)
        if (appointments.size == 0) { return null }

        appointments.first()?.let {
            // The title cell.
            val cellTitle = displayModel.activity.getString(
                    when (type) {
                        AppointmentType.Other -> R.string.detailMedicalAppointmentCellTitle
                        else -> it.getAppointmentType().nameString()
                    }
            )
            val speechText = displayModel.activity.getString(
                    when (type) {
                        AppointmentType.Other -> R.string.detailMedicalAppointmentCellTitle
                        else -> it.getAppointmentType().nameSpeechString()
                    }
            )
            val titleCellModel = StaticTextCellModel(
                    MenuCellIdentifier.NoMenu,
                    SceneId.Other,
                    cellTitle,
                    speechText,
                    displayModel.largeStyle,
                    it.getAppointmentType().defaultColor(),
                    it.getAppointmentType().highlightColor(),
                    R.color.darkMain,
                    true,
                    it.getAppointmentType().defaultColor(),
                    it.getAppointmentType().defaultColor(),
                    true
            )
            val titleCellViewModel = StaticTextCellViewModel(titleCellModel)

            // The appointment's date.
            val timeString = DateUtils.toSimpleString(
                    it.getAppointmentDate(),
                    displayModel.activity.getString(R.string.appointmentTimeFormat)
            )
            val formatString = displayModel.activity.getString(R.string.appointmentTime)
            val dateCellTitle = String.format(formatString, timeString)
            val dateCellModel = StaticTextCellModel(
                    MenuCellIdentifier.NoMenu,
                    SceneId.Other,
                    dateCellTitle,
                    dateCellTitle,
                    displayModel.largeStyle,
                    it.getAppointmentType().defaultColor(),
                    it.getAppointmentType().highlightColor(),
                    R.color.darkMain,
                    true,
                    it.getAppointmentType().defaultColor(),
                    it.getAppointmentType().defaultColor(),
                    true
            )
            val dateCellViewModel = StaticTextCellViewModel(dateCellModel)

            return arrayListOf(titleCellViewModel, dateCellViewModel)
        }

        return null
    }

    /**
     * Make the cell data for the NHD/Visus header.
     *
     * @param displayModel: The display model.
     * @return The split cell data.
     */
    private fun createNhdVisusHeaderCellData(displayModel: AppointmentDetailDisplayModel): List<SplitCellViewModel>? {
        // Retrieve objects from database.
        val realm = realm ?: return null
        val visusResults = realm.getVisusObjects(displayModel.date)
        val nhdResults = realm.getNhdObjects(displayModel.date)
        if (visusResults == null && nhdResults == null) {
            return null
        }

        val leftTitle = displayModel.activity.getString(R.string.detailSplitCellTitleLeft)
        val rightTitle = displayModel.activity.getString(R.string.detailSplitCellTitleRight)
        val speechText = displayModel.activity.getString(R.string.detailSplitCellTitleLeftSpeech) +
                displayModel.activity.getString(R.string.speechSplitConnector) +
                displayModel.activity.getString(R.string.detailSplitCellTitleRightSpeech)
        val titleCellModel = SplitCellModel(
                leftTitle,
                rightTitle,
                speechText,
                false,
                false,
                displayModel.largeStyle,
                true,
                R.color.darkMain,
                R.color.lightMain,
                null
        )
        val titleCellViewModel = SplitCellViewModel(titleCellModel)

        return arrayListOf(titleCellViewModel)
    }

    /**
     * Make the cells for the visus object.
     *
     * @param displayModel: The display model.
     * @return The cell data model.
     */
    private fun createVisusCellData(displayModel: AppointmentDetailDisplayModel): List<BaseCellViewModel>? {
        // Retrieve objects from database.
        val realm = realm ?: return null
        val results = realm.getVisusObjects(displayModel.date) ?: return null
        val visusObject = results.first() ?: return null

        var cellData: List<BaseCellViewModel> = listOf()

        // The title.
        val cellTitle = displayModel.activity.getString(R.string.detailVisusModelCellTitle)
        val titleCellSpeech = displayModel.activity.getString(R.string.detailVisusModelCellTitleSpeech)
        val titleCellModel = StaticTextCellModel(
                MenuCellIdentifier.NoMenu,
                SceneId.Other,
                cellTitle,
                titleCellSpeech,
                displayModel.largeStyle,
                AppointmentType.Treatment.defaultColor(),
                AppointmentType.Treatment.highlightColor(),
                R.color.darkMain,
                true,
                AppointmentType.Treatment.defaultColor(),
                AppointmentType.Treatment.highlightColor(),
                true
        )
        val titleCellViewModel = StaticTextCellViewModel(titleCellModel)
        cellData += titleCellViewModel

        // The value split cell.
        val leftTitle = VisusNhdType.Visus.valueOutput(displayModel.activity, visusObject.valueLeft)
        val rightTitle = VisusNhdType.Visus.valueOutput(displayModel.activity, visusObject.valueRight)
        val valueCellSpeech = leftTitle +
                displayModel.activity.getString(R.string.speechSplitConnector) +
                rightTitle
        val valueCellModel = SplitCellModel(
                leftTitle,
                rightTitle,
                valueCellSpeech,
                false,
                false,
                displayModel.largeStyle,
                true,
                R.color.darkMain,
                R.color.lightMain,
                null
        )
        val valueCellViewModel = SplitCellViewModel(valueCellModel)
        cellData += valueCellViewModel

        return cellData
    }

    /**
     * Make the cells for the NHD object.
     *
     * @param displayModel: The display model.
     * @return The cell data model.
     */
    private fun createNhdCellData(displayModel: AppointmentDetailDisplayModel): List<BaseCellViewModel>? {
        // Retrieve objects from database.
        val realm = realm ?: return null
        val results = realm.getNhdObjects(displayModel.date) ?: return null
        val nhdObject = results.first() ?: return null

        var cellData: List<BaseCellViewModel> = listOf()

        // The title cell.
        val cellTitle = displayModel.activity.getString(R.string.detailNhdModelCellTitle)
        val titleCellSpeech = displayModel.activity.getString(R.string.detailNhdModelCellTitleSpeech)
        val titleCellModel = StaticTextCellModel(
                MenuCellIdentifier.NoMenu,
                SceneId.Other,
                cellTitle,
                titleCellSpeech,
                displayModel.largeStyle,
                AppointmentType.Treatment.defaultColor(),
                AppointmentType.Treatment.highlightColor(),
                R.color.darkMain,
                true,
                AppointmentType.Treatment.defaultColor(),
                AppointmentType.Treatment.highlightColor(),
                true
        )
        val titleCellViewModel = StaticTextCellViewModel(titleCellModel)
        cellData += titleCellViewModel

        // The value split cell.
        val leftTitle = VisusNhdType.Nhd.valueOutput(displayModel.activity, nhdObject.valueLeft.toInt())
        val rightTitle = VisusNhdType.Nhd.valueOutput(displayModel.activity, nhdObject.valueRight.toInt())
        val valueCellSpeech = leftTitle +
                displayModel.activity.getString(R.string.speechSplitConnector) +
                rightTitle
        val valueCellModel = SplitCellModel(
                leftTitle,
                rightTitle,
                valueCellSpeech,
                false,
                false,
                displayModel.largeStyle,
                true,
                R.color.darkMain,
                R.color.lightMain,
                null
        )
        val valueCellViewModel = SplitCellViewModel(valueCellModel)
        cellData += valueCellViewModel

        return cellData
    }

    /**
     * Make the cells for the amslertest object.
     *
     * @param displayModel: The display model.
     * @return The list of cell data for the amslertest progress.
     */
    private fun createAmslertestCellData(displayModel: AppointmentDetailDisplayModel): List<BaseCellViewModel>? {
        // Retrieve object form database.
        val realm = realm ?: return null
        val results = realm.getAmslerTestObjects(displayModel.date) ?: return null
        val amslerTestObject = results.first() ?: return null

        var cellData: List<BaseCellViewModel> = listOf()

        // The title cell.
        val cellTitle = displayModel.activity.getString(R.string.detailAmslertestModelCellTitle)
        val titleCellSpeech = displayModel.activity.getString(R.string.detailAmslertestModelCellTitleSpeech)
        val titleCellModel = StaticTextCellModel(
                MenuCellIdentifier.NoMenu,
                SceneId.Other, cellTitle,
                titleCellSpeech,
                displayModel.largeStyle,
                R.color.lightMain,
                R.color.white,
                R.color.darkMain,
                true,
                R.color.lightMain,
                R.color.lightMain,
                true
        )
        val titleCellViewModel = StaticTextCellViewModel(titleCellModel)
        cellData += titleCellViewModel

        // The left & right title.
        val leftTitle = displayModel.activity.getString(R.string.detailSplitCellTitleLeft)
        val rightTitle = displayModel.activity.getString(R.string.detailSplitCellTitleRight)
        val splitCellSpeech = displayModel.activity.getString(R.string.detailSplitCellTitleLeftSpeech) +
                displayModel.activity.getString(R.string.speechSplitConnector) +
                displayModel.activity.getString(R.string.detailSplitCellTitleRightSpeech)
        val splitCellModel = SplitCellModel(
                leftTitle,
                rightTitle,
                splitCellSpeech,
                false,
                false,
                displayModel.largeStyle,
                true,
                R.color.darkMain,
                R.color.lightMain,
                null
        )
        val splitCellViewModel = SplitCellViewModel(splitCellModel)
        cellData += splitCellViewModel

        // The progress cells.
        val progressTypeOrder = arrayListOf(
                AmslerTestProgressType.Equal,
                AmslerTestProgressType.Better,
                AmslerTestProgressType.Worse)
        val progressCellModels = progressTypeOrder.map {
            SplitRadioCellModel(
                    it,
                    displayModel.activity.getString(it.titleText()),
                    amslerTestObject.getProgressTypeLeft() == it,
                    amslerTestObject.getProgressTypeRight() == it,
                    displayModel.largeStyle,
                    true,
                    null
            )
        }
        val progressCellViewModels = progressCellModels.map {
            SplitRadioCellViewModel(it)
        }
        cellData += progressCellViewModels

        return cellData
    }

    /**
     * Make the cells for the reading test object.
     *
     * @param displayModel: The display model.
     * @return The cell data model.
     */
    private fun createReadingTestCellData(displayModel: AppointmentDetailDisplayModel): List<BaseCellViewModel>? {
        // Retrieve object from database.
        val realm = realm ?: return null
        val results = realm.getReadingTestObjects(displayModel.date) ?: return null
        val readingTestObject = results.first() ?: return null

        var cellData: List<BaseCellViewModel> = listOf()

        // The title cell.
        val cellTitle = displayModel.activity.getString(R.string.detailReadingTestModelCellTitle)
        val titleCellSpeech = displayModel.activity.getString(R.string.detailReadingTestModelCellTitleSpeech)
        val titleCellModel = StaticTextCellModel(
                MenuCellIdentifier.NoMenu,
                SceneId.Other,
                cellTitle,
                titleCellSpeech,
                displayModel.largeStyle,
                R.color.lightMain,
                R.color.white,
                R.color.darkMain,
                true,
                R.color.lightMain,
                R.color.lightMain,
                true
        )
        val titleCellViewModel = StaticTextCellViewModel(titleCellModel)
        cellData += titleCellViewModel

        // The left & right title.
        val leftTitle = displayModel.activity.getString(R.string.detailSplitCellTitleLeft)
        val rightTitle = displayModel.activity.getString(R.string.detailSplitCellTitleRight)
        val splitCellSpeech = displayModel.activity.getString(R.string.detailSplitCellTitleLeftSpeech) +
                displayModel.activity.getString(R.string.speechSplitConnector) +
                displayModel.activity.getString(R.string.detailSplitCellTitleRightSpeech)
        val splitCellModel = SplitCellModel(
                leftTitle,
                rightTitle,
                splitCellSpeech,
                false,
                false,
                displayModel.largeStyle,
                true,
                R.color.darkMain,
                R.color.lightMain,
                null
        )
        val splitCellViewModel = SplitCellViewModel(splitCellModel)
        cellData += splitCellViewModel

        // The reading test value.
        val leftValue = readingTestObject.getMagnitudeTypeLeft()?.rawValue ?: 0
        val rightValue = readingTestObject.getMagnitudeTypeRight()?.rawValue ?: 0
        val valueCellSpeech = leftTitle +
                displayModel.activity.getString(R.string.speechSplitConnector) +
                rightTitle
        val valueCellModel = SplitCellModel(
                leftValue.toString(),
                rightValue.toString(),
                valueCellSpeech,
                false,
                false,
                displayModel.largeStyle,
                true,
                R.color.darkMain,
                R.color.lightMain,
                null
        )
        val valueCellViewModel = SplitCellViewModel(valueCellModel)
        cellData += valueCellViewModel

        return cellData
    }

    /**
     * Creates a note cell data.
     *
     * @param displayModel: The display model.
     * @return A note cell view model.
     */
    private fun createNoteCellData(displayModel: AppointmentDetailDisplayModel): List<StaticTextCellViewModel> {
        var appointmentType = AppointmentType.Other

        // Retrieve objects from database.
        realm?.let { realm ->
            val appointments = realm.getAppointmentObjects(displayModel.date)
            if (appointments.size == 1) {
                appointments.first()?.let {
                    appointmentType = it.getAppointmentType()
                }
            }
        }

        val cellTitle = displayModel.activity.getString(R.string.detailNoteCellTitle)
        val speechText = displayModel.activity.getString(R.string.detailNoteCellTitleSpeech)
        val noteCellModel = StaticTextCellModel(
                MenuCellIdentifier.NoMenu,
                SceneId.Note,
                cellTitle,
                speechText,
                displayModel.largeStyle,
                displayModel.titleColor,
                appointmentType.highlightColor(),
                R.color.darkMain,
                true,
                displayModel.titleColor,
                displayModel.titleColor,
                false
        )
        val noteCellViewModel = StaticTextCellViewModel(noteCellModel)

        return arrayListOf(noteCellViewModel)
    }

    /**
     * Creates a delete cell data.
     *
     * @param displayModel: The display model.
     * @return A delete cell view model.
     */
    private fun createDeleteCellData(displayModel: AppointmentDetailDisplayModel): List<StaticTextCellViewModel> {
        val cellTitle = displayModel.activity.getString(R.string.detailDeleteCellTitle)
        val speechText = displayModel.activity.getString(R.string.detailDeleteCellTitleSpeech)
        val deleteCellModel = StaticTextCellModel(
                MenuCellIdentifier.NoMenu,
                SceneId.Delete,
                cellTitle,
                speechText,
                displayModel.largeStyle,
                R.color.magenta,
                R.color.white,
                R.color.darkMain,
                true,
                R.color.white,
                null,
                false
        )
        val deleteCellViewModel = StaticTextCellViewModel(deleteCellModel)

        return arrayListOf(deleteCellViewModel)
    }
}