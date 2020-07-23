package com.appamedix.makula.worker.datamodel

import android.util.Log
import android.content.Context
import com.appamedix.makula.BuildConfig
import com.appamedix.makula.R
import com.appamedix.makula.constants.Const
import com.appamedix.makula.scenes.diagnosis.table.DiagnosisType
import com.appamedix.makula.types.AppointmentType
import com.appamedix.makula.types.ContactType
import com.appamedix.makula.utils.DateUtils
import io.realm.DynamicRealm
import io.realm.RealmMigration
import io.realm.Sort

class DataModelMigration(private val context: Context) : RealmMigration {
    override fun migrate(realm: DynamicRealm, oldVersion: Long, newVersion: Long) {
        Log.i("REALM", "Migrating DB from" + oldVersion + " to " + Const.DataModelManager.latestVersionNumber)

        if (oldVersion < Const.DataModelManager.modelVersion1) {
            // Add default diagnosis objects.
            val diagnosisAmd = realm.createObject("DiagnosisObject", DiagnosisType.AMD.rawValue)
            diagnosisAmd.setInt("sortOrderPosition", DiagnosisType.AMD.rawValue)

            val diagnosisDmo = realm.createObject("DiagnosisObject", DiagnosisType.DMO.rawValue)
            diagnosisDmo.setInt("sortOrderPosition", DiagnosisType.DMO.rawValue)

            val diagnosisRvv = realm.createObject("DiagnosisObject", DiagnosisType.RVV.rawValue)
            diagnosisRvv.setInt("sortOrderPosition", DiagnosisType.RVV.rawValue)

            val diagnosisMcnv = realm.createObject("DiagnosisObject", DiagnosisType.MCNV.rawValue)
            diagnosisMcnv.setInt("sortOrderPosition", DiagnosisType.MCNV.rawValue)

            // Add default medicament objects.
            var objectDate = DateUtils.setDateWithDate(2018, 6, 1, 0, 1, 0)
            val medicament1 = realm.createObject("MedicamentObject")
            medicament1.setString("name", context.getString(R.string.medicamentEntryAvastin))
            medicament1.setBoolean("editable", false)
            medicament1.setDate("creationDate", objectDate)

            objectDate = DateUtils.setDateWithDate(2018, 6, 1, 0, 2, 0)
            val medicament2 = realm.createObject("MedicamentObject")
            medicament2.setString("name", context.getString(R.string.medicamentEntryEylea))
            medicament2.setBoolean("editable", false)
            medicament2.setDate("creationDate", objectDate)

            objectDate = DateUtils.setDateWithDate(2018, 6, 1, 0, 3, 0)
            val medicament3 = realm.createObject("MedicamentObject")
            medicament3.setString("name", context.getString(R.string.medicamentEntryLucentis))
            medicament3.setBoolean("editable", false)
            medicament3.setDate("creationDate", objectDate)

            objectDate = DateUtils.setDateWithDate(2018, 6, 1, 0, 4, 0)
            val medicament4 = realm.createObject("MedicamentObject")
            medicament4.setString("name", context.getString(R.string.medicamentEntryOzurdex))
            medicament4.setBoolean("editable", false)
            medicament4.setDate("creationDate", objectDate)

            objectDate = DateUtils.setDateWithDate(2018, 6, 1, 0, 5, 0)
            val medicament5 = realm.createObject("MedicamentObject")
            medicament5.setString("name", context.getString(R.string.medicamentEntryIluvien))
            medicament5.setBoolean("editable", false)
            medicament5.setDate("creationDate", objectDate)

            // Add default contact objects.
            objectDate = DateUtils.setDateWithDate(2018, 6, 1, 0, 1, 0)
            val contact1 = realm.createObject("ContactObject")
            contact1.setInt("type", ContactType.Treatment.rawValue)
            contact1.setDate("creationDate", objectDate)

            objectDate = DateUtils.setDateWithDate(2018, 6, 1, 0, 2, 0)
            val contact2 = realm.createObject("ContactObject")
            contact2.setInt("type", ContactType.Aftercare.rawValue)
            contact2.setDate("creationDate", objectDate)

            objectDate = DateUtils.setDateWithDate(2018, 6, 1, 0, 3, 0)
            val contact3 = realm.createObject("ContactObject")
            contact3.setInt("type", ContactType.OctCheck.rawValue)
            contact3.setDate("creationDate", objectDate)

            objectDate = DateUtils.setDateWithDate(2018, 6, 1, 0, 4, 0)
            val contact4 = realm.createObject("ContactObject")
            contact4.setInt("type", ContactType.AmdNet.rawValue)
            contact4.setDate("creationDate", objectDate)
            contact4.setString("name","AMD-Netz e.V.")
            contact4.setString("phone", "01805774778")
            contact4.setString("email", "info@amd-netz.de")
            contact4.setString("web", "www.amd-netz.de")
            contact4.setString("street", "Hohenzollernring 56")
            contact4.setString("city", "48145 Münster")

            // Add debug data.
            if (BuildConfig.FLAVOR.equals(Const.BuildConfig.devProduction)) {
                addDebugData(realm)
            }
        }
    }

    /**
     * Adds some debug test data to the database.
     * Does nothing when not building for the devProduction environment.
     */
    private fun addDebugData(realm: DynamicRealm) {
        if (!BuildConfig.FLAVOR.equals(Const.BuildConfig.devProduction)) {
            return
        }

        // Add some custom medicaments.
        var objectDate = DateUtils.setDateWithDate(2018, 7, 1, 0, 1, 0)
        val medicament1 = realm.createObject("MedicamentObject")
        medicament1.setString("name", "My custom med 1")
        medicament1.setBoolean("editable", true)
        medicament1.setDate("creationDate", objectDate)

        objectDate = DateUtils.setDateWithDate(2018, 7, 1, 0, 2, 0)
        val medicament2 = realm.createObject("MedicamentObject")
        medicament2.setString("name", "Another custom medicament with a longer title")
        medicament2.setBoolean("editable", true)
        medicament2.setDate("creationDate", objectDate)

        // Add custom contacts.
        objectDate = DateUtils.setDateWithDate(2018, 7, 1, 0, 1, 0)
        val contact1 = realm.createObject("ContactObject")
        contact1.setInt("type", ContactType.Custom.rawValue)
        contact1.setDate("creationDate", objectDate)
        contact1.setString("name","My buddy")
        contact1.setString("mobile", "0179 555 55 55 55")
        contact1.setString("phone", "555 55 55 55")
        contact1.setString("email", "mail@trash.me")
        contact1.setString("web", "google.de")
        contact1.setString("street", "Street Name")
        contact1.setString("city", "City Name")

        // Add some appointments.
        objectDate = DateUtils.setDateWithDate(2018, 6, 1, 18, 20, 0)
        val appointment1 = realm.createObject("AppointmentObject")
        appointment1.setInt("type", AppointmentType.Treatment.rawValue)
        appointment1.setDate("date", objectDate)

        objectDate = DateUtils.setDateWithDate(2018, 6, 2, 0, 0, 0)
        val appointment2 = realm.createObject("AppointmentObject")
        appointment2.setInt("type", AppointmentType.Treatment.rawValue)
        appointment2.setDate("date", objectDate)

        objectDate = DateUtils.setDateWithDate(2018, 3, 15, 0, 0, 0)
        val appointment3 = realm.createObject("AppointmentObject")
        appointment3.setInt("type", AppointmentType.Treatment.rawValue)
        appointment3.setDate("date", objectDate)

        objectDate = DateUtils.setDateWithDate(2018, 6, 1, 8, 30, 0)
        val appointment4 = realm.createObject("AppointmentObject")
        appointment4.setInt("type", AppointmentType.Aftercare.rawValue)
        appointment4.setDate("date", objectDate)

        objectDate = DateUtils.setDateWithDate(2018, 6, 3, 0, 0, 0)
        val appointment5 = realm.createObject("AppointmentObject")
        appointment5.setInt("type", AppointmentType.Aftercare.rawValue)
        appointment5.setDate("date", objectDate)

        objectDate = DateUtils.setDateWithDate(2018, 6, 1, 12, 50, 0)
        val appointment6 = realm.createObject("AppointmentObject")
        appointment6.setInt("type", AppointmentType.OctCheck.rawValue)
        appointment6.setDate("date", objectDate)

        objectDate = DateUtils.setDateWithDate(2018, 6, 4, 0, 0, 0)
        val appointment7 = realm.createObject("AppointmentObject")
        appointment7.setInt("type", AppointmentType.OctCheck.rawValue)
        appointment7.setDate("date", objectDate)

        objectDate = DateUtils.setDateWithDate(2018, 6, 1, 15, 11, 0)
        val appointment8 = realm.createObject("AppointmentObject")
        appointment8.setInt("type", AppointmentType.Other.rawValue)
        appointment8.setDate("date", objectDate)

        objectDate = DateUtils.setDateWithDate(2018, 6, 5, 0, 0, 0)
        val appointment9 = realm.createObject("AppointmentObject")
        appointment9.setInt("type", AppointmentType.Other.rawValue)
        appointment9.setDate("date", objectDate)

        objectDate = DateUtils.setDateWithDate(2018, 9, 21, 0, 0, 0)
        val appointment10 = realm.createObject("AppointmentObject")
        appointment10.setInt("type", AppointmentType.Aftercare.rawValue)
        appointment10.setDate("date", objectDate)

        // Add some NHD values.
        objectDate = DateUtils.setDateWithDate(2018, 2, 1, 0, 0, 0)
        val nhd1 = realm.createObject("NhdObject")
        nhd1.setDate("date", objectDate)
        nhd1.setFloat("valueLeft", 300.0f)
        nhd1.setFloat("valueRight", 360.0f)

        objectDate = DateUtils.setDateWithDate(2018, 2, 15, 0, 0, 0)
        val nhd2 = realm.createObject("NhdObject")
        nhd2.setDate("date", objectDate)
        nhd2.setFloat("valueLeft", 320.0f)
        nhd2.setFloat("valueRight", 260.0f)

        objectDate = DateUtils.setDateWithDate(2018, 5, 15, 0, 0, 0)
        val nhd3 = realm.createObject("NhdObject")
        nhd3.setDate("date", objectDate)
        nhd3.setFloat("valueLeft", 340.0f)
        nhd3.setFloat("valueRight", 260.0f)

        objectDate = DateUtils.setDateWithDate(2018, 6, 1, 0, 0, 0)
        val nhd4 = realm.createObject("NhdObject")
        nhd4.setDate("date", objectDate)
        nhd4.setFloat("valueLeft", 280.0f)
        nhd4.setFloat("valueRight", 310.0f)

        objectDate = DateUtils.setDateWithDate(2018, 7, 1, 0, 0, 0)
        val nhd5 = realm.createObject("NhdObject")
        nhd5.setDate("date", objectDate)
        nhd5.setFloat("valueLeft", 300.0f)
        nhd5.setFloat("valueRight", 280.0f)

        // Add some Visus values
        objectDate = DateUtils.setDateWithDate(2017, 8, 1, 0, 0, 0)
        val visus1 = realm.createObject("VisusObject")
        visus1.setDate("date", objectDate)
        visus1.setInt("valueLeft", 11)
        visus1.setInt("valueRight", 0)

        objectDate = DateUtils.setDateWithDate(2018, 6, 1, 0, 0, 0)
        val visus2 = realm.createObject("VisusObject")
        visus2.setDate("date", objectDate)
        visus2.setInt("valueLeft", 5)
        visus2.setInt("valueRight", 4)

        objectDate = DateUtils.setDateWithDate(2018, 8, 1, 0, 0, 0)
        val visus3 = realm.createObject("VisusObject")
        visus3.setDate("date", objectDate)
        visus3.setInt("valueLeft", 3)
        visus3.setInt("valueRight", 12)

        // Activate diagnose
        val diagnosis= realm.where("DiagnosisObject")
                .sort("sortOrderPosition", Sort.ASCENDING)
                .equalTo("type", DiagnosisType.DMO.rawValue)
                .findFirst()
        if (diagnosis != null) {
            diagnosis.setBoolean("selected", true)
        }
    }
}