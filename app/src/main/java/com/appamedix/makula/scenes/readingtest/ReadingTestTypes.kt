package com.appamedix.makula.scenes.readingtest

import com.appamedix.makula.R
import com.appamedix.makula.constants.Const

/* The magnitude type for the reading test */
enum class ReadingTestMagnitudeType(val rawValue: Int) {
    // The tiny size (12).
    Tiny(1),
    // The little size (15).
    Little(2),
    // The small size (18).
    Small(3),
    // The medium size (24).
    Medium(4),
    // The large size (32).
    Large(5),
    // The big size (42).
    Big(6);

    companion object {
        fun from(findValue: Int): ReadingTestMagnitudeType = ReadingTestMagnitudeType.values().first {
            it.rawValue == findValue
        }
    }

    /**
     * Returns the number of text lines for this type.
     *
     * @return The number of lines.
     */
    fun defaultLines(): Int = when (this) {
        Tiny, Little -> 4
        Small -> 3
        else -> 2
    }

    /**
     * Returns the localized text for this type.
     *
     * @return The localized text.
     */
    fun contentText(): Int = when (this) {
        Tiny -> R.string.readingTestContentCellTiny
        Little -> R.string.readingTestContentCellLittle
        Small -> R.string.readingTestContentCellSmall
        Medium -> R.string.readingTestContentCellMedium
        Large -> R.string.readingTestContentCellLarge
        Big -> R.string.readingTestContentCellBig
    }

    /**
     * Returns the font size for this type.
     *
     * @return The font size of text.
     */
    fun textFontSize(): Float = when (this) {
        Tiny -> Const.Font.readingtestTiny
        Little -> Const.Font.readingtestLittle
        Small -> Const.Font.readingtestSmall
        Medium -> Const.Font.readingtestMedium
        Large -> Const.Font.readingtestLarge
        Big -> Const.Font.readingtestBig
    }
}