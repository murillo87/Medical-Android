package com.appamedix.makula.worker.datamodel.diagnosis

import com.appamedix.makula.scenes.diagnosis.table.DiagnosisType
import io.realm.RealmObject
import io.realm.annotations.Index
import io.realm.annotations.PrimaryKey
import io.realm.annotations.Required

/* A single entry of the diagnosis */
open class DiagnosisObject : RealmObject() {
    // The diagnosis type.
    @PrimaryKey
    private var type: Int = DiagnosisType.AMD.rawValue

    // The position of this object in a sorted list.
    @Index
    var sortOrderPosition: Int = 0

    // Whether the use has selected this entry or not.
    var selected: Boolean = false

    /**
     * Returns the type of this entry.
     *
     * @return The diagnosis type.
     */
    fun getDiagnosisType(): DiagnosisType = DiagnosisType.from(type)

    /**
     * Saves the type of this entry.
     *
     * @param type: The diagnosis type to save.
     */
    fun saveDiagnosisType(type: DiagnosisType) {
        this.type = type.rawValue
    }
}