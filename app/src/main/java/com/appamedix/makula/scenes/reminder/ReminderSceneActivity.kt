package com.appamedix.makula.scenes.reminder

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.res.Configuration
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SimpleItemAnimator
import com.appamedix.makula.R
import com.appamedix.makula.base.BaseActivity
import com.appamedix.makula.scenes.reminder.table.ReminderTableAdapter
import com.appamedix.makula.scenes.reminder.table.ReminderTableDataSource
import com.appamedix.makula.scenes.reminder.table.checkboxcell.ReminderCheckboxCellListener
import com.appamedix.makula.scenes.reminder.table.checkboxcell.ReminderCheckboxCellViewModel
import com.appamedix.makula.scenes.reminder.table.pickercell.ReminderPickerCellListener
import com.appamedix.makula.types.ImageButtonType
import com.appamedix.makula.views.navigation.NavigationCellModel
import com.appamedix.makula.views.navigation.NavigationCellViewModel
import com.appamedix.makula.views.navigation.NavigationViewListener
import com.appamedix.makula.worker.notification.NotificationScheduler
import com.appamedix.makula.worker.preference.InternalSettings
import kotlinx.android.synthetic.main.activity_reminder_scene.*

class ReminderSceneActivity : BaseActivity(), NavigationViewListener,
        ReminderCheckboxCellListener, ReminderPickerCellListener {

    private var recyclerView: RecyclerView? = null
    private var tableAdapter: ReminderTableAdapter? = null

    private var isLandscape: Boolean = false
    private lateinit var displayModel: ReminderDisplayModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reminder_scene)

        // Check the device orientation.
        val orientation = resources.configuration.orientation
        isLandscape = orientation == Configuration.ORIENTATION_LANDSCAPE

        requestDisplayData()

        // Setup navigation
        val sceneTitle = getString(R.string.reminderTitle)
        val navigationCellModel = NavigationCellModel(
                sceneTitle,
                R.color.white,
                false,
                true,
                true,
                ImageButtonType.Back,
                false,
                ImageButtonType.Speaker,
                this)
        val navigationCellViewModel = NavigationCellViewModel(navigationCellModel)
        navigation.setupView(navigationCellViewModel)

        if (isLandscape) {
            navigation.layoutParams.height = 0
        }

        // Setup table view.
        recyclerView = findViewById(R.id.reminder_table)
        (recyclerView!!.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false

        presentTable()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.right_in, R.anim.right_out)
    }

    private fun requestDisplayData() {
        val reminderChecked = InternalSettings.reminderOn
        val reminderTime = InternalSettings.reminderTime

        displayModel = ReminderDisplayModel(this, isLandscape, reminderChecked, reminderTime)
    }

    /**
     * Apply data source and adapter to the table view.
     */
    private fun presentTable() {
        val dataSource = ViewModelProviders.of(this).get(ReminderTableDataSource::class.java)
        dataSource.getMainCellData(displayModel).observe(this, Observer { cellViewModels ->
            tableAdapter = ReminderTableAdapter(this@ReminderSceneActivity, cellViewModels!!)
            recyclerView!!.layoutManager = LinearLayoutManager(this@ReminderSceneActivity)
            recyclerView!!.adapter = tableAdapter
        })
    }

    /* Navigation view listener */

    override fun leftButtonClicked() {
        onBackPressed()
    }

    override fun rightButtonClicked() {
    }

    /* Reminder checkbox cell listener */

    override fun onCellItemClicked(position: Int) {
        tableAdapter?.let {
            val cellModel = it.arrayList[position] as ReminderCheckboxCellViewModel
            cellModel.checked = !cellModel.checked
            InternalSettings.reminderOn = cellModel.checked

            requestDisplayData()
            it.notifyItemChanged(position)

            // Setup local notification.
            NotificationScheduler.setReminder(applicationContext, null)
        }
    }

    /* Reminder picker cell listener */

    override fun pickerValueChanged(newValue: Int) {
        InternalSettings.reminderTime = newValue
        requestDisplayData()

        // Setup local notification.
        NotificationScheduler.setReminder(applicationContext, null)
    }
}
