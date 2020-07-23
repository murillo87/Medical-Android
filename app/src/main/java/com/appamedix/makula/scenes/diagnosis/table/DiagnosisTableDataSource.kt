package com.appamedix.makula.scenes.diagnosis.table

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.appamedix.makula.R
import com.appamedix.makula.base.BaseCellViewModel
import com.appamedix.makula.scenes.diagnosis.DiagnosisDisplayModel
import com.appamedix.makula.scenes.diagnosis.table.maincell.DiagnosisCellModel
import com.appamedix.makula.scenes.diagnosis.table.maincell.DiagnosisCellViewModel
import com.appamedix.makula.types.ImageButtonType
import com.appamedix.makula.views.navigation.NavigationCellModel
import com.appamedix.makula.views.navigation.NavigationCellViewModel
import com.appamedix.makula.worker.datamodel.diagnosis.DiagnosisObject
import com.appamedix.makula.worker.speech.SpeechSynthesizer
import io.realm.RealmResults

class DiagnosisTableDataSource : ViewModel() {

    private var mainCellsMutableData = MutableLiveData<ArrayList<BaseCellViewModel>>()
    private var menuCellsSpeechData = MutableLiveData<ArrayList<SpeechSynthesizer.SpeechData>>()
    var diagnosisObjects: RealmResults<DiagnosisObject>? = null

    /**
     * Returns a mutable live data for the table cells.
     *
     * @param displayModel: The display model.
     * @return A mutable live data list for the cells.
     */
    fun getMainCellData(displayModel: DiagnosisDisplayModel): MutableLiveData<ArrayList<BaseCellViewModel>> {
        // Initialize data.
        var tableData: List<BaseCellViewModel> = listOf()

        // Add the navigation view as first cell in large style.
        if (displayModel.largeStyle) {
            val navigation = createNavigationCellData(displayModel)
            tableData += navigation
        }

        // Add the diagnosis cells if exists.
        diagnosisObjects?.let {
            val diagnosisCells = createDiagnosisCellData(displayModel, it)
            tableData += diagnosisCells
        }

        mainCellsMutableData.value = ArrayList(tableData)
        return mainCellsMutableData
    }

    /**
     * Returns a mutable live data for the speech.
     *
     * @param displayModel: The display model.
     * @return A mutable live data list for the speech.
     */
    fun getMenuCellSpeechData(displayModel: DiagnosisDisplayModel)
            : MutableLiveData<ArrayList<SpeechSynthesizer.SpeechData>> {
        val objects = diagnosisObjects ?: return MutableLiveData()
        val activity = displayModel.activity

        // Make speech data.
        var index = if (displayModel.largeStyle) 0 else -1
        val speechData = objects.map {
            index += 1
            val text = activity.getString(it.getDiagnosisType().titleText())
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
    private fun createNavigationCellData(displayModel: DiagnosisDisplayModel): List<NavigationCellViewModel> {
        val title = displayModel.activity.getString(R.string.diagnosisTitle)
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
     * Creates the list of diagnosis cell data.
     *
     * @param displayModel: The display model.
     * @param results: The diagnosis objects to show.
     * @return The list of diagnosis cell view model.
     */
    private fun createDiagnosisCellData(displayModel: DiagnosisDisplayModel,
                                        results: RealmResults<DiagnosisObject>): List<DiagnosisCellViewModel> {
        val activity = displayModel.activity
        val diagnosisCellModels = results.map {
            val title = activity.getString(it.getDiagnosisType().titleText())
            val subtitle = activity.getString(it.getDiagnosisType().subTitleText())
            DiagnosisCellModel(
                    it.getDiagnosisType(),
                    it.selected,
                    title,
                    subtitle,
                    displayModel.largeStyle,
                    activity
            )
        }

        return diagnosisCellModels.map {
            DiagnosisCellViewModel(it)
        }
    }
}
