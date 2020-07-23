package com.appamedix.makula.scenes.calendar

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.appamedix.makula.R
import com.appamedix.makula.base.BaseActivity
import com.appamedix.makula.scenes.appointmentdetail.AppointmentDetailModel
import com.appamedix.makula.scenes.appointmentdetail.AppointmentDetailSceneActivity
import com.appamedix.makula.scenes.calendar.table.CalendarTableAdapter
import com.appamedix.makula.scenes.calendar.table.CalendarTableDataSource
import com.appamedix.makula.scenes.calendar.table.weekcell.CalendarWeekCellListener
import com.appamedix.makula.scenes.menu.MenuSceneActivity
import com.appamedix.makula.scenes.newappointment.NewAppointmentSceneActivity
import com.appamedix.makula.types.ImageButtonType
import com.appamedix.makula.utils.DateUtils
import com.appamedix.makula.views.navigation.NavigationCellModel
import com.appamedix.makula.views.navigation.NavigationCellViewModel
import com.appamedix.makula.views.navigation.NavigationViewListener
import io.realm.Realm
import kotlinx.android.synthetic.main.activity_calendar_scene.*
import java.util.*

class CalendarSceneActivity : BaseActivity(), NavigationViewListener, CalendarWeekCellListener {

    companion object {
        const val CALENDAR_MODEL = "calendar_model"
    }

    private var recyclerView: RecyclerView? = null
    private var tableAdapter: CalendarTableAdapter? = null

    private var isLandscape: Boolean = false
    private var isModifyNavigationStackSet: Boolean = false
    private var focusDate: Date? = null

    private lateinit var realm: Realm

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendar_scene)

        realm = Realm.getDefaultInstance()

        // Check the device orientation.
        val orientation = resources.configuration.orientation
        isLandscape = orientation == Configuration.ORIENTATION_LANDSCAPE

        intent.extras?.let {
            val calendarModel = it.getParcelable<CalendarModel>(CALENDAR_MODEL)
            focusDate = calendarModel?.focusDate
            isModifyNavigationStackSet = calendarModel?.modifyNavigationStack ?: false
        }

        // Setup navigation
        val navigationCellModel = NavigationCellModel(
                getString(R.string.calendarTitle),
                R.color.white,
                false,
                true,
                true,
                ImageButtonType.Back,
                true,
                ImageButtonType.Add,
                this)
        val navigationCellViewModel = NavigationCellViewModel(navigationCellModel)
        navigation.setupView(navigationCellViewModel)

        week_name_view.setupView(isLandscape)

        if (isLandscape) {
            navigation.layoutParams.height = 0
            week_name_view.layoutParams.height = 0
        }

        // Setup table view.
        recyclerView = findViewById(R.id.calendar_table)
        recyclerView!!.itemAnimator = DefaultItemAnimator()
    }

    override fun onStart() {
        super.onStart()

        // Apply data source and adapter to the table view.
        val tableDataSource: CalendarTableDataSource = ViewModelProviders.of(this).get(CalendarTableDataSource::class.java)
        tableDataSource.realm = realm
        tableDataSource.getCellData(this, isLandscape).observe(this, Observer { cellViewModels ->
            tableAdapter = CalendarTableAdapter(this@CalendarSceneActivity, cellViewModels!!)
            recyclerView!!.layoutManager = LinearLayoutManager(this@CalendarSceneActivity)
            recyclerView!!.adapter = tableAdapter
            recyclerView!!.scrollToPosition(tableDataSource.indexForDate(this, focusDate ?: Date()))
        })
    }

    override fun onDestroy() {
        realm.close()
        super.onDestroy()
    }

    override fun onBackPressed() {
        if (isModifyNavigationStackSet) {
            val intent = Intent(this, MenuSceneActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
        } else {
            super.onBackPressed()
        }
        overridePendingTransition(R.anim.right_in, R.anim.right_out)
    }

    /* CalendarWeekCellListener */

    override fun daySelected(atWeekDate: Date, dayIndex: Int) {
        val firstOfWeek = DateUtils.startOfWeek(atWeekDate)
        val selectedDate = DateUtils.increaseDay(firstOfWeek, dayIndex)

        if (!DateUtils.isInSameMonth(atWeekDate, selectedDate)) {
            // Outside of this month, just skip action.
            return
        }

        // Route to appointment detail scene.
        val detailModel = AppointmentDetailModel(selectedDate)
        val intent = Intent(this, AppointmentDetailSceneActivity::class.java)
        intent.putExtra(AppointmentDetailSceneActivity.DETAIL_MODEL, detailModel)

        startActivity(intent)
        overridePendingTransition(R.anim.left_in, R.anim.left_out)
    }

    /* Navigation view listener */

    override fun leftButtonClicked() {
        onBackPressed()
    }

    override fun rightButtonClicked() {
        val intent = Intent(this, NewAppointmentSceneActivity::class.java)
        startActivity(intent)
        overridePendingTransition(R.anim.left_in, R.anim.left_out)
    }
}
