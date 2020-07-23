package com.appamedix.makula.scenes.newappointment.table

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.appamedix.makula.R
import com.appamedix.makula.base.BaseCellViewModel
import com.appamedix.makula.scenes.menu.table.MenuCellIdentifier
import com.appamedix.makula.scenes.newappointment.NewAppointmentDisplayModel
import com.appamedix.makula.types.AppointmentType
import com.appamedix.makula.types.ImageButtonType
import com.appamedix.makula.types.SceneId
import com.appamedix.makula.views.navigation.NavigationCellModel
import com.appamedix.makula.views.navigation.NavigationCellViewModel
import com.appamedix.makula.views.textcell.StaticTextCellModel
import com.appamedix.makula.views.textcell.StaticTextCellViewModel
import com.appamedix.makula.worker.speech.SpeechSynthesizer

class NewAppointmentTableDataSource : ViewModel() {

    private var mainCellsMutableData = MutableLiveData<ArrayList<BaseCellViewModel>>()
    private var menuCellsSpeechData = MutableLiveData<ArrayList<SpeechSynthesizer.SpeechData>>()

    val tableData = arrayListOf(
            AppointmentType.Treatment,
            AppointmentType.Aftercare,
            AppointmentType.OctCheck,
            AppointmentType.Other)

    /**
     * Returns a mutable live data for the table cells.
     *
     * @param displayModel: The display model.
     * @return A mutable live data list for the cells.
     */
    fun getMainCellData(displayModel: NewAppointmentDisplayModel): MutableLiveData<ArrayList<BaseCellViewModel>> {
        // Initialize data.
        var tableCellData: List<BaseCellViewModel> = listOf()

        // Add the navigation view as first cell in large style.
        if (displayModel.largeStyle) {
            val navigation = createNavigationCellData(displayModel)
            tableCellData += navigation
        }

        // Add the main cells.
        val mainCells = createMainCellData(displayModel)
        tableCellData += mainCells

        mainCellsMutableData.value = ArrayList(tableCellData)
        return mainCellsMutableData
    }

    /**
     * Returns a mutable live data for the speech.
     *
     * @param displayModel: The display model.
     * @return A mutable live data list for the speech.
     */
    fun getMenuCellSpeechData(displayModel: NewAppointmentDisplayModel): MutableLiveData<ArrayList<SpeechSynthesizer.SpeechData>> {
        val activity = displayModel.activity

        // Make speech data.
        var index = if (displayModel.largeStyle) 0 else -1
        val speechData = tableData.map {
            index += 1
            val text = activity.getString(it.nameSpeechString())
            SpeechSynthesizer.SpeechData(text, index)
        }

        menuCellsSpeechData.value = ArrayList(speechData)
        return menuCellsSpeechData
    }

    /**
     * Creates a navigation cell data.
     *
     * @param displayModel: The display model.
     * @return The navigation view model.
     */
    private fun createNavigationCellData(displayModel: NewAppointmentDisplayModel): List<NavigationCellViewModel> {
        val activity = displayModel.activity

        val navigationCellModel = NavigationCellModel(
                activity.getString(R.string.newAppointmentTitle),
                R.color.white,
                true,
                true,
                true,
                ImageButtonType.Back,
                true,
                ImageButtonType.Speaker,
                activity)
        val navigationCellViewModel = NavigationCellViewModel(navigationCellModel)

        return arrayListOf(navigationCellViewModel)
    }

    /**
     * Creates main cell data.
     *
     * @param displayModel: The display model.
     * @return The list of main cell data.
     */
    private fun createMainCellData(displayModel: NewAppointmentDisplayModel): List<StaticTextCellViewModel> {
        val activity = displayModel.activity

        val mainCellModels = tableData.map {
            StaticTextCellModel(
                    MenuCellIdentifier.NoMenu,
                    SceneId.Other,
                    activity.getString(it.nameString()),
                    null,
                    displayModel.largeStyle,
                    it.defaultColor(),
                    it.highlightColor(),
                    R.color.darkMain,
                    true,
                    it.defaultColor(),
                    null,
                    false
            )
        }

        return mainCellModels.map {
            StaticTextCellViewModel(it)
        }
    }
}