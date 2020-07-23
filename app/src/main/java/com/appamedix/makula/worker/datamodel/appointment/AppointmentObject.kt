package com.appamedix.makula.worker.datamodel.appointment

import com.appamedix.makula.types.AppointmentType
import io.realm.RealmObject
import io.realm.annotations.Index
import io.realm.annotations.Required
import java.util.*

/* A single entry of the appointment */
open class AppointmentObject : RealmObject() {
    // The type of this appointment.
    @Index
    private var type: Int = AppointmentType.Other.rawValue

    // The date of the appointment.
    @Required @Index
    private var date: Date = Date()

    /**
     * Save an appointment enum value as a string.
     *
     * @param type: The appointment type
     */
    fun saveAppointmentType(type: AppointmentType) {
        this.type = type.rawValue
    }

    /**
     * Returns the appointment type of this object.
     *
     * @return The appointment type
     */
    fun getAppointmentType(): AppointmentType = AppointmentType.from(type)

    /**
     * Save an appointment date.
     *
     * @param date: The appointment date
     */
    fun saveAppointmentDate(date: Date) {
        this.date = date
    }

    /**
     * Returns the appointment date of this object.
     *
     * @return The appointment date
     */
    fun getAppointmentDate() = date
}