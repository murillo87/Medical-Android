package com.appamedix.makula.worker.datamodel

import android.content.Context
import android.net.Uri
import android.support.v4.content.ContextCompat
import android.util.Log
import com.appamedix.makula.R
import com.appamedix.makula.constants.Const
import com.appamedix.makula.utils.DateUtils
import com.appamedix.makula.worker.filemanage.UriHelper
import com.appamedix.makula.worker.filemanage.ZipManager
import io.realm.Realm
import io.realm.RealmConfiguration
import java.io.File
import java.util.*

object DataModelManager {

    /// The tag for the logs belonging to the data model.
    private const val logTag = "DATAMODEL"

    /// The path to the database inclusive file, but relative to the files folder.
    private const val databaseFilePath = Const.DataModelManager.databasePathName +
            Const.DataModelManager.databaseFileName

    fun init(context: Context) {
        // Initialize Realm.
        Realm.init(context)

        // Set up default realm configuration.
        val defaultConfiguration = RealmConfiguration.Builder()
                .name(databaseFilePath)
                .schemaVersion(Const.DataModelManager.latestVersionNumber)
                .migration(DataModelMigration(context))
                .build()
        Realm.setDefaultConfiguration(defaultConfiguration)
    }

    /**
     * Tries to establish a connection to the database.
     * Creates the database with default values or migrates it when necessary.
     * This should be the first call to happen before calling any other method.
     *
     * When run in a "devProduction" build then the database will be filled with test data.

     * @param context: The app's context.
     */
    fun touchDatabase(context: Context) {
        // Make sure the folder exists.
        val databasePath = File(context.filesDir, Const.DataModelManager.databasePathName)
        val databaseFile = File(context.filesDir, databaseFilePath)
        Log.d(logTag, "Realm file: $databaseFile")
        databasePath.mkdirs()

        // Is there a database or do we need to prepare a blank one for the first migration?
        if (!databaseFile.exists()) {
            // Database doesn't exist, create one with no schema for the initial version.
            val noSchemaConfiguration = RealmConfiguration.Builder()
                    .name(databaseFilePath)
                    .schemaVersion(Const.DataModelManager.modelVersion0)
                    .build()
            var realm: Realm? = null
            try {
                realm = Realm.getInstance(noSchemaConfiguration)
            } catch (e: Exception) {
                Log.e(logTag, "Realm error: $e")
                throw Error("Database Error $e")
            } finally {
                realm?.close()
            }
        }

        // Open the database and perform a migration if necessary.
        // The default migration also applies some test data in devProduction.
        var realm: Realm? = null
        try {
            realm = Realm.getDefaultInstance()
        } catch (e: Exception) {
            Log.e(logTag, "Realm migration error: $e")
            throw Error("Database Error $e")
        } finally {
            realm?.close()
        }
    }

    /**
     * Exports the database as a backup zip file.
     * Make sure no database connection is open at the time.
     *
     * @param context: The application context.
     * @return The zip file path that was created.
     */
    fun exportData(context: Context): String? {
        val databaseFile = File(context.filesDir, databaseFilePath)
        if (!databaseFile.exists()) return null

        // Make sure the backup folder exists.
        val externalStoragePath = ContextCompat.getExternalFilesDirs(context, null).first()
        val backupPath = File(externalStoragePath, Const.DataModelManager.backupPathName)
        val zipFilePath = Const.DataModelManager.backupPathName +
                Const.DataModelManager.backupFileName
        val zipFile = File(externalStoragePath, zipFilePath)
        backupPath.mkdirs()

        val realm = Realm.getDefaultInstance()
        return try {
            realm.beginTransaction()
            val files = arrayOf(databaseFile.absolutePath)
            ZipManager().zip(files, zipFile.absolutePath)
            realm.cancelTransaction()

            // Rename file.
            val dateFormat = context.getString(R.string.commonDateFormat)
            val dateString = DateUtils.toSimpleString(Date(), dateFormat)
            val backupFilePath = Const.DataModelManager.backupPathName +
                    String.format(context.getString(R.string.backupFileName), dateString)
            val backupFile = File(externalStoragePath, backupFilePath)
            if (zipFile.renameTo(backupFile)) {
                zipFile.delete()
                backupFile.absolutePath
            } else {
                null
            }
        } catch (e: Exception) {
            null
        } finally {
            realm.close()
        }
    }

    /**
     * Imports a zipped backup file by unzipping the database and replacing the current one in the app with
     * the newly extracted one.
     * This happens immediately on the main thread.
     * Make sure the app refreshes afterwards.
     *
     * @param context: The application context.
     * @param uri: The Uri for the zip file.
     * @returns Whether importing succeeded or not.
     */
    fun importData(context: Context, uri: Uri): Boolean {
        // Prepare the backup folder.
        val backupFolder = File(context.filesDir, Const.DataModelManager.backupPathName)
        if (!backupFolder.deleteRecursively()) {
            Log.e(logTag, "Deleting backup folder error.")
            return false
        }
        if (!backupFolder.mkdirs()) {
            Log.e(logTag, "Backup folder couldn't be created.")
            return false
        }

        // Construct backup file name.
        val filePath = UriHelper.getPath(context, uri)
        val backupFile = File(filePath)
        val zipFilePath= Const.DataModelManager.backupPathName +
                Const.DataModelManager.backupFileName
        val zipFile = File(context.filesDir, zipFilePath)

        var realm: Realm? = null
        return try {
            // Extract the provided backup file to the backup folder.
            backupFile.copyTo(zipFile, true)
            ZipManager().unzip(zipFile.absolutePath, backupFolder.absolutePath)
            zipFile.delete()

            // Try touching the extracted backup database.
            val dbFilePath = Const.DataModelManager.backupPathName + Const.DataModelManager.databaseFileName
            val configuration = RealmConfiguration.Builder()
                    .name(dbFilePath)
                    .schemaVersion(Const.DataModelManager.latestVersionNumber)
                    .build()
            realm = Realm.getInstance(configuration)

            // Backup seems valid, replace current database with backup.
            val dbFile = File(context.filesDir, databaseFilePath)
            val backupDBFile = File(context.filesDir, dbFilePath)
            backupDBFile.copyTo(dbFile, true)
            backupFolder.deleteRecursively()

            true
        } catch (e: Exception) {
            false
        } finally {
            realm?.close()
        }
    }
}