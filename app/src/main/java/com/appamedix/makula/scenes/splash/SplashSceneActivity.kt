package com.appamedix.makula.scenes.splash

import android.Manifest
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.StrictMode
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.Toast
import com.appamedix.makula.R
import com.appamedix.makula.base.BaseActivity
import com.appamedix.makula.constants.Const
import com.appamedix.makula.extensions.batchRequestPermissions
import com.appamedix.makula.extensions.isPermissionGranted
import com.appamedix.makula.scenes.disclaimer.DisclaimerSceneActivity
import com.appamedix.makula.scenes.menu.MenuSceneActivity
import com.appamedix.makula.utils.Run
import com.appamedix.makula.worker.datamodel.DataModelManager
import com.appamedix.makula.worker.notification.NotificationScheduler
import com.appamedix.makula.worker.preference.InternalSettings


class SplashSceneActivity : BaseActivity(), ActivityCompat.OnRequestPermissionsResultCallback {

    companion object {
        // ID to identify the external storage permission request.
        const val REQUEST_STORAGE = 100
        // Permissions required to read and write external storage.
        val PERMISSIONS_STORAGE = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_scene)

        // Set background color.
        val backgroundView = findViewById<View>(R.id.splashContentView)
        backgroundView.setBackgroundColor(ContextCompat.getColor(this, R.color.pompadour))

        // Ignore URI exposure.
        val builder = StrictMode.VmPolicy.Builder()
        StrictMode.setVmPolicy(builder.build())

        // Set animation to the logo.
        AnimationUtils.loadAnimation(this, R.anim.fade_in).also { fadeInAnim ->
            findViewById<ImageView>(R.id.logo).startAnimation(fadeInAnim)
        }

        // Check if the external storage permission is available.
        if (!isPermissionGranted(Manifest.permission.READ_EXTERNAL_STORAGE)) {
            // The permission has not been granted.
            batchRequestPermissions(PERMISSIONS_STORAGE, REQUEST_STORAGE)
        } else {
            // Perform database import.
            processingImport()
            // Initialize preference, database and notification,
            // and route to next scene.
            performInitAndRoute()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == REQUEST_STORAGE) {
            // Check if the permission has been granted.
            if (grantResults.contains(PackageManager.PERMISSION_GRANTED)) {
                // Storage permission has been granted, perform database import.
                processingImport()
            } else {
                // The permission denied.
                // Disable the functionality that depends on this permission.
                showError(R.string.externalStoragePermissionMessage)
            }
            // Initialize preference and database,
            // and route to next scene.
            performInitAndRoute()
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }

    private fun processingImport() {
        if (intent != null) {
            val data = intent.data
            if (data != null && data.scheme == "content") {
                // Import database.
                if (!DataModelManager.importData(applicationContext, data)) {
                    showError(R.string.backupErrorMessage)
                } else {
                    // DB imported, restart the app.
                    val mStartActivity = Intent(this, SplashSceneActivity::class.java)
                    val mPendingIntentId = 123456
                    val mPendingIntent = PendingIntent.getActivity(this, mPendingIntentId, mStartActivity, PendingIntent.FLAG_CANCEL_CURRENT)
                    val mgr = getSystemService(ALARM_SERVICE) as AlarmManager
                    mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 100, mPendingIntent)
                    android.os.Process.killProcess(android.os.Process.myPid())
                }
            }
        }
    }

    private fun performInitAndRoute() {
        // Update the app's internal settings if needed.
        InternalSettings.updateSettings()
        // Update the app's database.
        DataModelManager.touchDatabase(this)

        // Update local notifications.
        NotificationScheduler.setReminder(applicationContext, null)

        // Route to next scene after 2 seconds.
        Run.after(Const.Time.transitionDelay) {
            if (InternalSettings.disclaimerAccepted)
                routeToMenu()
            else
                routeToDisclaimer()
        }
    }

    private fun routeToDisclaimer() {
        val intent = Intent(this, DisclaimerSceneActivity::class.java)
        startActivity(intent)
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
    }

    private fun routeToMenu() {
        val intent = Intent(this, MenuSceneActivity::class.java)
        startActivity(intent)
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
    }

    private fun showError(message: Int) {
        Toast.makeText(this@SplashSceneActivity, message, Toast.LENGTH_SHORT).show()
    }
}
