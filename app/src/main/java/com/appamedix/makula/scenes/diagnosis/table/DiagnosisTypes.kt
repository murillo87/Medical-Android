package com.appamedix.makula.scenes.diagnosis.table

import com.appamedix.makula.R

/**
 * The diagnosis type representing the identifier of a `DiagnosisObject`.
 * The raw value represents its position and index in the realm database so keep it in sync when adding new entries.
 */
enum class DiagnosisType(val rawValue: Int) {
    // `Altersabhängige Makulardegeneration (AMD)`.
    AMD(1),
    // `Diabetisches Makulaodem (DMO)`.
    DMO(2),
    // `Retinaler Venenverschluss (RVV)`.
    RVV(3),
    // `Myope choroidale Neovaskularisation (mCNV)`.
    MCNV(4);

    companion object {
        fun from(findValue: Int): DiagnosisType = DiagnosisType.values().first {
            it.rawValue == findValue
        }
    }

    /**
     * Returns the cell's title text for this type.
     *
     * @return The localized text.
     */
    fun titleText(): Int = when (this) {
        AMD -> R.string.diagnosisCellTitleAmd
        DMO -> R.string.diagnosisCellTitleDmo
        RVV -> R.string.diagnosisCellTitleRvv
        MCNV -> R.string.diagnosisCellTitleMcnv
    }

    /**
     * Returns the cell's subtitle text for this type.
     *
     * @return The localized text.
     */
    fun subTitleText(): Int = when (this) {
        AMD -> R.string.diagnosisSubTitleAmd
        DMO -> R.string.diagnosisSubTitleDmo
        RVV -> R.string.diagnosisSubTitleRvv
        MCNV -> R.string.diagnosisSubTitleMcnv
    }
}