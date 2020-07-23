package com.appamedix.makula.scenes.contactdetail

import com.appamedix.makula.R

/* The contact information type */
enum class ContactInfoType(val rawValue: Int) {
    // The contact name.
    Name(1),
    // The contact mobile number.
    Mobile(2),
    // The contact phone number.
    Phone(3),
    // The contact email.
    Email(4),
    // The web address.
    Web(5),
    // The street of the contact person.
    Street(6),
    // The city of the contact person.
    City(7);

    /**
     * Returns the placeholder text for this type.
     *
     * @return The placeholder text.
     */
    fun defaultString(): Int = when (this) {
        Name -> R.string.contactDetailNameCell
        Mobile -> R.string.contactDetailMobileCell
        Phone -> R.string.contactDetailPhoneCell
        Email -> R.string.contactDetailEmailCell
        Web -> R.string.contactDetailWebCell
        Street -> R.string.contactDetailStreetCell
        City -> R.string.contactDetailCityCell
    }

    /**
     * Returns the text for this type to speak via speech synthesizer.
     *
     * @return The speech text
     */
    fun speechString(): Int = when (this) {
        Name -> R.string.contactDetailNameCellSpeech
        Mobile -> R.string.contactDetailMobileCellSpeech
        Phone -> R.string.contactDetailPhoneCellSpeech
        Email -> R.string.contactDetailEmailCellSpeech
        Web -> R.string.contactDetailWebCellSpeech
        Street -> R.string.contactDetailStreetCellSpeech
        City -> R.string.contactDetailCityCellSpeech
    }
}