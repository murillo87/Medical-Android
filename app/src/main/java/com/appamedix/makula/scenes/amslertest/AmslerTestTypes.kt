package com.appamedix.makula.scenes.amslertest

import com.appamedix.makula.R

/* The progress type for the amslertest. */
enum class AmslerTestProgressType(val rawValue: Int) {
    // Same progress, nothing has changed (`gleich`).
    Equal(1),
    // The situation improved, it got better (`besser`).
    Better(2),
    // The situation got worse (`schlechter`).
    Worse(3);

    companion object {
        fun from(findValue: Int): AmslerTestProgressType = AmslerTestProgressType.values().first {
            it.rawValue == findValue
        }
    }

    /**
     * Returns the localized text for this type.
     *
     * @return The localized text.
     */
    fun titleText(): Int = when (this) {
        Equal -> R.string.amslerTestProgressEqual
        Better -> R.string.amslerTestProgressBetter
        Worse -> R.string.amslerTestProgressWorse
    }

    /**
     * Returns the speech synthesizer text for this type.
     *
     * @return The text for the speech synthesizer.
     */
    fun titleSpeechText(): Int = when (this) {
        Equal -> R.string.amslerTestProgressEqualSpeech
        Better -> R.string.amslerTestProgressBetterSpeech
        Worse -> R.string.amslerTestProgressWorseSpeech
    }
}