package com.appamedix.makula.scenes.medicament.table

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.appamedix.makula.R
import com.appamedix.makula.base.BaseCellViewModel
import com.appamedix.makula.scenes.medicament.MedicamentDiaplayModel
import com.appamedix.makula.scenes.medicament.table.inputcell.MedicamentInputCellModel
import com.appamedix.makula.scenes.medicament.table.inputcell.MedicamentInputCellViewModel
import com.appamedix.makula.scenes.medicament.table.maincell.MedicamentCellModel
import com.appamedix.makula.scenes.medicament.table.maincell.MedicamentCellViewModel
import com.appamedix.makula.types.ImageButtonType
import com.appamedix.makula.views.navigation.NavigationCellModel
import com.appamedix.makula.views.navigation.NavigationCellViewModel
import com.appamedix.makula.worker.datamodel.medicament.MedicamentObject
import com.appamedix.makula.worker.speech.SpeechSynthesizer
import io.realm.RealmResults

class MedicamentTableDataSource : ViewModel() {

    private var mainCellsMutableData = MutableLiveData<ArrayList<BaseCellViewModel>>()
    private var menuCellsSpeechData = MutableLiveData<ArrayList<SpeechSynthesizer.SpeechData>>()
    var medicamentObjects: RealmResults<MedicamentObject>? = null

    /**
     * Returns a mutable live data for the table cells.
     *
     * @param displayModel: The display model.
     * @return A mutable live data list for the cells.
     */
    fun getMainCellData(displayModel: MedicamentDiaplayModel): MutableLiveData<ArrayList<BaseCellViewModel>> {
        // Initialize data.
        var tableData: List<BaseCellViewModel> = listOf()

        // Add the navigation view as first cell in large style.
        if (displayModel.largeStyle) {
            val navigation = createNavigationCellData(displayModel)
            tableData += navigation
        }

        // Add the medicament cells if exists.
        medicamentObjects?.let {
            val medicamentCells = createMedicamentCellData(displayModel, it)
            tableData += medicamentCells
        }

        // Add the input cell data.
        val inputCell = createInputCellData(displayModel)
        tableData += inputCell

        mainCellsMutableData.value = ArrayList(tableData)
        return mainCellsMutableData
    }

    /**
     * Returns a mutable live data for the speech.
     *
     * @param displayModel: The display model.
     * @return A mutable live data list for the speech.
     */
    fun getMenuCellSpeechData(displayModel: MedicamentDiaplayModel)
            : MutableLiveData<ArrayList<SpeechSynthesizer.SpeechData>> {
        val objects = medicamentObjects ?: return MutableLiveData()
        val activity = displayModel.activity

        // Initialize data.
        var tableSpeechData: List<SpeechSynthesizer.SpeechData> = listOf()

        // Make speech data.
        var index = if (displayModel.largeStyle) 0 else -1
        val mainCellSpeeches = objects.map {
            index += 1
            SpeechSynthesizer.SpeechData(it.name, index)
        }
        tableSpeechData += mainCellSpeeches

        val inputText = activity.getString(R.string.medicamentCellMoreSpeech)
        val inputCellSpeech = SpeechSynthesizer.SpeechData(inputText, index + 1)
        tableSpeechData += arrayListOf(inputCellSpeech)

        menuCellsSpeechData.value = ArrayList(tableSpeechData)
        return menuCellsSpeechData
    }

    /**
     * Creates a navigation cell data.
     *
     * @param displayModel: The display model.
     * @return The navigation view model.
     */
    private fun createNavigationCellData(displayModel: MedicamentDiaplayModel): List<NavigationCellViewModel> {
        val title = displayModel.activity.getString(R.string.medicamentTitle)
        val navigationCellModel = NavigationCellModel(
                title,
                R.color.white,
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
     * Creates an input cell data.
     *
     * @param displayModel: The display model.
     * @return The input cell view model.
     */
    private fun createInputCellData(displayModel: MedicamentDiaplayModel): List<MedicamentInputCellViewModel> {
        val inputCellModel = MedicamentInputCellModel(displayModel.largeStyle, displayModel.activity)
        val inputCellViewModel = MedicamentInputCellViewModel(inputCellModel)

        return arrayListOf(inputCellViewModel)
    }

    /**
     * Creates the list of medicament cell data.
     *
     * @param displayModel: The display model.
     * @param results: The medicament objects to show.
     * @return The list of medicament cell view model.
     */
    private fun createMedicamentCellData(displayModel: MedicamentDiaplayModel,
                                         results: RealmResults<MedicamentObject>): List<MedicamentCellViewModel> {
        val medicamentModels = results.map {
            MedicamentCellModel(
                    it.name,
                    it.editable,
                    it.selected,
                    displayModel.largeStyle,
                    displayModel.activity
            )
        }

        return medicamentModels.map {
            MedicamentCellViewModel(it)
        }
    }
}