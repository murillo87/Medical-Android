package com.appamedix.makula.scenes.disclaimer

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import com.appamedix.makula.R
import com.appamedix.makula.base.BaseActivity
import com.appamedix.makula.constants.Const
import com.appamedix.makula.scenes.menu.MenuSceneActivity
import com.appamedix.makula.worker.preference.InternalSettings

class DisclaimerSceneActivity : BaseActivity() {

    private var isLandscape: Boolean = false

    private lateinit var titleLabel: TextView
    private lateinit var contentLabel: TextView
    private lateinit var checkBoxLabel: TextView
    private lateinit var checkBox: CheckBox
    private lateinit var confirmButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_disclaimer_scene)

        // Check the device orientation.
        val orientation = resources.configuration.orientation
        isLandscape = orientation == Configuration.ORIENTATION_LANDSCAPE

        // Get instances of UI elements.
        titleLabel = findViewById(R.id.title)
        contentLabel = findViewById(R.id.content)
        checkBoxLabel = findViewById(R.id.checkBox_title)
        checkBox = findViewById(R.id.checkBox)
        confirmButton = findViewById(R.id.confirm_button)

        // Set click listener to check mark button.
        checkBox.setOnCheckedChangeListener { _, isChecked ->
            confirmButton.isEnabled = isChecked

            val deactiveColor = ContextCompat.getColor(this, R.color.disabled)
            val activeColor = ContextCompat.getColor(this, R.color.lightMain)
            confirmButton.setBackgroundColor(if (isChecked) activeColor else deactiveColor)
        }

        // Set click listener to confirm button.
        confirmButton.setOnClickListener {
            InternalSettings.disclaimerAccepted = true
            val intent = Intent(this, MenuSceneActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
        }

        // Apply large style to UI elements in landscape mode.
        if (isLandscape) {
            titleLabel.textSize = Const.Font.content1Large
            contentLabel.textSize = Const.Font.content1Large
            checkBoxLabel.textSize = Const.Font.content1Large
            confirmButton.textSize = Const.Font.content1Large

            val params = checkBox.layoutParams
            params.height = (params.height * Const.Size.landscapeScaleFactor).toInt()
            params.width = (params.width * Const.Size.landscapeScaleFactor).toInt()
            checkBox.layoutParams = params
        }
    }

    override fun onBackPressed() {
        // Pressing the hardware back button should terminate the app.
        finishAffinity()
        System.exit(0)
    }
}
