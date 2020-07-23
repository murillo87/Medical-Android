package com.appamedix.makula.types

import android.content.Context
import com.appamedix.makula.R
import com.appamedix.makula.constants.Const
import com.appamedix.makula.scenes.visusnhd.VisusNhdInputConst

/* All possible scenes in the app */
enum class SceneId {
    // The root of the menu, scene 1.1 "Makula-App"
    HomeMenu,
    // The doctor visit scene, scene 2.1 "Arztbesuch"
    DoctorVisitMenu,
    // The self test scene, scene 2.5 "Selbsttests"
    SelfTestMenu,
    // The knowledge scene, scene 2.6 (Flowchart2) "Wissen"
    KnowledgeMenu,
    // The settings scene, scene 2.7 (Flowchart2) "Einstellungen"
    SettingsMenu,
    // The illness scene, scene 3.1 (Flowchart2) "Erkrankung"
    IllnessMenu,
    // The examination scene, scene 3.2 (Flowchart2) "Untersuchung"
    ExaminationMenu,
    // The therapy scene, scene 3.3 (Flowchart2) "Therapie"
    TherapyMenu,
    // The activities scene, scene 3.4 (Flowchart2) "Maßnahmen"
    ActivitiesMenu,
    // The aid scene, scene 3.5 (Flowchart2) "Hilfsmittel"
    AidMenu,
    // The support scene, scene 3.6 (Flowchart2) "Unterstützung"
    SupportMenu,
    // The reminder scene, scene 3.9 (Flowchart2) "Erinnerung"
    Reminder,
    // The search scene, scene 3.8 (Flowchart2) "Suchfunktion"
    Search,
    // The new appointment scene, scene 2.2 "Neue Termine"
    NewAppointment,
    // The appointment detail scene.
    AppointmentDetail,
    // The contact scene, scene 2.4 "Kontakte"
    Contact,
    // The diagnosis scene, scene 3.2. "Diagnose"
    Diagnosis,
    // The medicament scene, scene 3.3. "Medikamente"
    Medicament,
    // The Visus / NHD Input scene with Visus type 3.4 "Visus-Eingabe"
    VisusInput,
    // The Visus / NHD Input scene with NHD type 3.5 "NHD-Eingabe"
    NhdInput,
    // The calendar scene 2.3 "Kalender"
    Calendar,
    // The Amslertest scene 3.11 "Amslertest"
    AmslerTest,
    // The reading test scene 3.12 "Lesetest"
    ReadingTest,
    // The Graph scene 3.6 / 3.7 "NH-Dicke/Visus"
    Graph,
    // The note scene 4.1 "Notizen"
    Note,
    // The info scene.
    Info,
    // The action dialog to delete something.
    Delete,
    // Any other scene or non-scene not covered.
    Other;

    /**
     * Returns the resource id of the scene's title.
     * @return The resource id
     */
    fun titleString(): Int = when (this) {
        HomeMenu -> R.string.homeMenuTitle
        DoctorVisitMenu -> R.string.doctorVisitMenuTitle
        SelfTestMenu -> R.string.selfTestMenuTitle
        KnowledgeMenu -> R.string.knowledgeMenuTitle
        SettingsMenu -> R.string.settingsMenuTitle
        IllnessMenu -> R.string.illnessMenuTitle
        ExaminationMenu -> R.string.examinationMenuTitle
        TherapyMenu -> R.string.therapyMenuTitle
        ActivitiesMenu -> R.string.activitiesMenuTitle
        AidMenu -> R.string.aidMenuTitle
        SupportMenu -> R.string.supportMenuTitle
        else -> throw IllegalArgumentException("Invalid menu")
    }
}

/* All possible cells in the app */
enum class ViewCellType {
    // The navigation view cell.
    Navigation,
    // The static text cell.
    StaticTextCell,
    // The split cell with left and right buttons.
    SplitCell,
    // The split cell with the left and right radio options.
    SplitRadioCell,
    // The image cell for the amsler test scene.
    AmslerTestImageCell,
    // The month cell for the calendar scene.
    CalendarMonthCell,
    // The week cell for the calendar scene.
    CalendarWeekCell,
    // The main cell for the contact scene.
    ContactCell,
    // The main cell for the contact details scene.
    ContactDetailCell,
    // The main cell for the diagnosis scene.
    DiagnosisCell,
    // The main cell for the graph scene.
    GraphCell,
    // The main cell for the medicament scene.
    MedicamentCell,
    // The input cell for the medicament scene.
    MedicamentInputCell,
    // The main cell for the reading test scene.
    ReadingTestCell,
    // The picker cell for the visus & nhd input scene.
    VisusNhdPickerCell,
    // The input cell for the search scene.
    SearchInputCell,
    // The checkbox cell for the reminder scene.
    ReminderCheckboxCell,
    // The picker cell for the reminder scene.
    ReminderPickerCell
}

/* All possible type for the image button */
enum class ImageButtonType {
    // A default back button showing the arrow to the left.
    Back,
    // The add button showing the '+' icon.
    Add,
    // The info button showing the 'i' icon.
    Info,
    // The info button showing the 'i' icon in navigation view.
    NavInfo,
    // The speaker button for starting the speech synthesizer.
    Speaker,
    // The 3-dot drag indicator button.
    DragIndicator;

    /**
     * Returns the resource id of the button image.
     * @return The resource id
     */
    fun imageResource(): Int = when (this) {
        Back -> R.drawable.back_arrow
        Add -> R.drawable.add
        NavInfo -> R.drawable.infobutton_selected
        Speaker -> R.drawable.speaker
        DragIndicator -> R.drawable.threedots
        else -> throw IllegalArgumentException("Invalid value")
    }

    /**
     * Returns the description of the button.
     *
     * @return The description string.
     */
    fun contentDescription(): Int = when (this) {
        Back -> R.string.nav_back_button
        Add -> R.string.nav_add_button
        NavInfo -> R.string.nav_info_button
        Speaker -> R.string.nav_speak_button
        else -> throw IllegalArgumentException("Invalid value")
    }
}

/* All type of appointments */
enum class AppointmentType(val rawValue: Int) {
    // The treatment ("Behandlung") appointment.
    Treatment(1),
    // The aftercare ("Nachsorge") appointment.
    Aftercare(2),
    // The OCT-check ("OCT-Kontrolle") appointment.
    OctCheck(3),
    // The other ("Sonstige") appointment.
    Other(4);

    companion object {
        fun from(findValue: Int): AppointmentType = AppointmentType.values().first {
            it.rawValue == findValue
        }
    }

    /**
     * Returns the printable string for the appointment.
     * @return The appointment's string
     */
    fun nameString(): Int = when (this) {
        Treatment -> R.string.appointmentTreatment
        Aftercare -> R.string.appointmentAftercare
        OctCheck -> R.string.appointmentOctCheck
        Other -> R.string.appointmentOther
    }

    /**
     * Returns the speakable string for the appointment.
     * @return The appointment's speech synthesizer string
     */
    fun nameSpeechString(): Int = when (this) {
        Treatment -> R.string.appointmentTreatmentSpeech
        Aftercare -> R.string.appointmentAftercareSpeech
        OctCheck -> R.string.appointmentOctCheckSpeech
        Other -> R.string.appointmentOtherSpeech
    }

    /**
     * Returns the foreground color for the appointment's cell in default state.
     * @return The default color
     */
    fun defaultColor(): Int = when (this) {
        Treatment -> R.color.green
        Aftercare -> R.color.magenta
        OctCheck -> R.color.yellow
        Other -> R.color.white
    }

    /**
     * Returns the foreground color for the appointment's cell in highlight state.
     * @return The highlight color
     */
    fun highlightColor(): Int = when (this) {
        Other -> R.color.lightMain
        else -> R.color.white
    }
}

/* All type of contacts */
enum class ContactType(val rawValue: Int) {
    // The treatment ("Behandlung") contact.
    Treatment(1),
    // The aftercare ("Nachsorge") contact.
    Aftercare(2),
    // The OCT-check ("OCT-Kontrolle") contact.
    OctCheck(3),
    // The AMD-network ("AMD-Netz) contact.
    AmdNet(4),
    // A user created custom contact.
    Custom(5);

    companion object {
        fun from(findValue: Int): ContactType = ContactType.values().first {
            it.rawValue == findValue
        }
    }

    /**
     * Returns the printable string for the contact's type.
     * Only valid for a non-custom entry (treatment, aftercare, octCheck, amdNet).
     *
     * @return The contact type's string.
     */
    fun displayString(): Int = when (this) {
        Treatment -> R.string.contactTreatment
        Aftercare -> R.string.contactAftercare
        OctCheck -> R.string.contactOctCheck
        AmdNet -> R.string.contactAmdNetwork
        else -> throw IllegalArgumentException("Invalid type")
    }

    /**
     * Returns the speakable string for the contact's type.
     * Only valid for a non-custom entry (treatment, aftercare, octCheck, amdNet).
     *
     * @return The contact type's string for a speech synthesizer.
     */
    fun speechString(): Int = when (this) {
        Treatment -> R.string.contactTreatmentSpeech
        Aftercare -> R.string.contactAftercareSpeech
        OctCheck -> R.string.contactOctCheckSpeech
        AmdNet -> R.string.contactAmdNetworkSpeech
        else -> throw IllegalArgumentException("Invalid type")
    }

    /**
     * Returns the foreground color for the contact's cell in default state.
     *
     * @return The default color.
     */
    fun defaultColor(): Int = when (this) {
        Treatment -> R.color.green
        Aftercare -> R.color.magenta
        OctCheck -> R.color.yellow
        AmdNet -> R.color.lightMain
        Custom -> R.color.white
    }

    /**
     * Returns the foreground color for the contact's cell in selected state.
     *
     * @return The highlight color.
     */
    fun highlightColor(): Int = when (this) {
        Custom -> R.color.lightMain
        else -> R.color.white
    }
}

/* All type of information */
enum class InfoType {
    // The information for the amslertest.
    AmslerTest,
    // The information for the readingtest.
    ReadingTest,
    // The information for the visus input scene.
    Visus,
    // The information for the NHD input scene.
    NHD,
    // The information for the AMD entry in the diagnosis scene.
    AMD,
    // The information for the DMO entry in the diagnosis scene.
    DMO,
    // The information for the RVV entry in the diagnosis scene.
    RVV,
    // The information for the mCNV entry in the diagnosis scene.
    MCNV,

    // `Adressverzeichnis`.
    Addresses,
    // `Aktuelles`.
    News,
    // `Impressum`.
    Inprint,
    // `Version`.
    Version,

    /* settings */

    Backup,

    /* knowledge */

    // `Bedienung`.
    Manual,
    // `Diagnose`.
    Diagnose,

    /* illness */

    IllnessInfo0,
    IllnessInfo1,
    IllnessInfo2,
    IllnessInfo3,
    IllnessInfo4,
    IllnessInfo5,
    IllnessInfo6,
    IllnessInfo7,
    IllnessInfo8,
    IllnessInfo9,

    /* examination */

    ExaminationInfo0,
    ExaminationInfo1,
    ExaminationInfo2,
    ExaminationInfo3,
    ExaminationInfo4,
    ExaminationInfo5,
    ExaminationInfo6,

    /* therapy */

    TherapyInfo0,
    TherapyInfo1,
    TherapyInfo2,
    TherapyInfo3,
    TherapyInfo4,
    TherapyInfo5,

    /* activities */

    ActivitiesInfo0,
    ActivitiesInfo1,
    ActivitiesInfo2,
    ActivitiesInfo3,
    ActivitiesInfo4,
    ActivitiesInfo5,

    /* aid */
    AidInfo0,
    AidInfo1,
    AidInfo2,
    AidInfo3,
    AidInfo4,
    AidInfo5,
    AidInfo6,
    AidInfo7,

    /* support */
    SupportInfo0,
    SupportInfo1,
    SupportInfo2,
    SupportInfo3,
    SupportInfo4;

    /**
     * Returns the title for the scene.
     *
     * @return The scene's title string.
     */
    fun titleString(): Int = when (this) {
        AmslerTest -> R.string.amslerTestInfoTitle
        ReadingTest -> R.string.readingTestInfoTitle
        Visus -> R.string.visusInfoTitle
        NHD -> R.string.nhdInfoTitle
        AMD -> R.string.amdInfoTitle
        DMO -> R.string.dmoInfoTitle
        RVV -> R.string.rvvInfoTitle
        MCNV -> R.string.mcnvInfoTitle
        Addresses -> R.string.addressesInfoTitle
        News -> R.string.newsInfoTitle
        Inprint -> R.string.inprintInfoTitle
        Version -> R.string.versionInfoTitle
        Backup -> R.string.backupInfoTitle
        Manual -> R.string.manualInfoTitle
        Diagnose -> R.string.diagnoseInfoTitle
        IllnessInfo0 -> R.string.illnessInfo0Title
        IllnessInfo1 -> R.string.illnessInfo1Title
        IllnessInfo2 -> R.string.illnessInfo2Title
        IllnessInfo3 -> R.string.illnessInfo3Title
        IllnessInfo4 -> R.string.illnessInfo4Title
        IllnessInfo5 -> R.string.illnessInfo5Title
        IllnessInfo6 -> R.string.illnessInfo6Title
        IllnessInfo7 -> R.string.illnessInfo7Title
        IllnessInfo8 -> R.string.illnessInfo8Title
        IllnessInfo9 -> R.string.illnessInfo9Title
        ExaminationInfo0 -> R.string.examinationInfo0Title
        ExaminationInfo1 -> R.string.examinationInfo1Title
        ExaminationInfo2 -> R.string.examinationInfo2Title
        ExaminationInfo3 -> R.string.examinationInfo3Title
        ExaminationInfo4 -> R.string.examinationInfo4Title
        ExaminationInfo5 -> R.string.examinationInfo5Title
        ExaminationInfo6 -> R.string.examinationInfo6Title
        TherapyInfo0 -> R.string.therapyInfo0Title
        TherapyInfo1 -> R.string.therapyInfo1Title
        TherapyInfo2 -> R.string.therapyInfo2Title
        TherapyInfo3 -> R.string.therapyInfo3Title
        TherapyInfo4 -> R.string.therapyInfo4Title
        TherapyInfo5 -> R.string.therapyInfo5Title
        ActivitiesInfo0 -> R.string.activitiesInfo0Title
        ActivitiesInfo1 -> R.string.activitiesInfo1Title
        ActivitiesInfo2 -> R.string.activitiesInfo2Title
        ActivitiesInfo3 -> R.string.activitiesInfo3Title
        ActivitiesInfo4 -> R.string.activitiesInfo4Title
        ActivitiesInfo5 -> R.string.activitiesInfo5Title
        AidInfo0 -> R.string.aidInfo0Title
        AidInfo1 -> R.string.aidInfo1Title
        AidInfo2 -> R.string.aidInfo2Title
        AidInfo3 -> R.string.aidInfo3Title
        AidInfo4 -> R.string.aidInfo4Title
        AidInfo5 -> R.string.aidInfo5Title
        AidInfo6 -> R.string.aidInfo6Title
        AidInfo7 -> R.string.aidInfo7Title
        SupportInfo0 -> R.string.supportInfo0Title
        SupportInfo1 -> R.string.supportInfo1Title
        SupportInfo2 -> R.string.supportInfo2Title
        SupportInfo3 -> R.string.supportInfo3Title
        SupportInfo4 -> R.string.supportInfo4Title
    }

    /**
     * Returns the speech text for the scene.
     *
     * @return The string for the speech synthesizer.
     */
    fun speechText(): IntArray = when (this) {
        AmslerTest -> intArrayOf(R.string.amslerTestSpeechText)
        ReadingTest -> intArrayOf(R.string.readingTestSpeechText)
        Visus -> intArrayOf(R.string.visusSpeechText)
        NHD -> intArrayOf(R.string.nhdSpeechText)
        AMD -> intArrayOf(R.string.amdSpeechText)
        DMO -> intArrayOf(R.string.dmoSpeechText)
        RVV -> intArrayOf(R.string.rvvSpeechText)
        MCNV -> intArrayOf(R.string.mcnvSpeechText)
        Addresses -> intArrayOf(R.string.addressesSpeechText)
        News -> intArrayOf(R.string.newsSpeechText)
        Inprint -> intArrayOf(R.string.inprintSpeechText, R.string.inprintSpeechText2)
        Version -> intArrayOf(R.string.versionSpeechText)
        Backup -> intArrayOf(R.string.backupSpeechText)
        Manual -> intArrayOf(R.string.manualSpeechText, R.string.manualSpeechText2)
        Diagnose -> intArrayOf(R.string.diagnoseSpeechText, R.string.diagnoseSpeechText2)
        IllnessInfo0 -> intArrayOf(R.string.illnessInfo0SpeechText)
        IllnessInfo1 -> intArrayOf(R.string.illnessInfo1SpeechText)
        IllnessInfo2 -> intArrayOf(R.string.illnessInfo2SpeechText)
        IllnessInfo3 -> intArrayOf(R.string.illnessInfo3SpeechText)
        IllnessInfo4 -> intArrayOf(R.string.illnessInfo4SpeechText)
        IllnessInfo5 -> intArrayOf(R.string.illnessInfo5SpeechText)
        IllnessInfo6 -> intArrayOf(R.string.illnessInfo6SpeechText)
        IllnessInfo7 -> intArrayOf(R.string.illnessInfo7SpeechText)
        IllnessInfo8 -> intArrayOf(R.string.illnessInfo8SpeechText)
        IllnessInfo9 -> intArrayOf(R.string.illnessInfo9SpeechText)
        ExaminationInfo0 -> intArrayOf(R.string.examinationInfo0SpeechText)
        ExaminationInfo1 -> intArrayOf(R.string.examinationInfo1SpeechText)
        ExaminationInfo2 -> intArrayOf(R.string.examinationInfo2SpeechText)
        ExaminationInfo3 -> intArrayOf(R.string.examinationInfo3SpeechText)
        ExaminationInfo4 -> intArrayOf(R.string.examinationInfo4SpeechText)
        ExaminationInfo5 -> intArrayOf(R.string.examinationInfo5SpeechText)
        ExaminationInfo6 -> intArrayOf(R.string.examinationInfo6SpeechText)
        TherapyInfo0 -> intArrayOf(R.string.therapyInfo0SpeechText)
        TherapyInfo1 -> intArrayOf(R.string.therapyInfo1SpeechText)
        TherapyInfo2 -> intArrayOf(R.string.therapyInfo2SpeechText)
        TherapyInfo3 -> intArrayOf(R.string.therapyInfo3SpeechText)
        TherapyInfo4 -> intArrayOf(R.string.therapyInfo4SpeechText)
        TherapyInfo5 -> intArrayOf(R.string.therapyInfo5SpeechText)
        ActivitiesInfo0 -> intArrayOf(R.string.activitiesInfo0SpeechText)
        ActivitiesInfo1 -> intArrayOf(R.string.activitiesInfo1SpeechText)
        ActivitiesInfo2 -> intArrayOf(R.string.activitiesInfo2SpeechText)
        ActivitiesInfo3 -> intArrayOf(R.string.activitiesInfo3SpeechText)
        ActivitiesInfo4 -> intArrayOf(R.string.activitiesInfo4SpeechText)
        ActivitiesInfo5 -> intArrayOf(R.string.activitiesInfo5SpeechText)
        AidInfo0 -> intArrayOf(R.string.aidInfo0SpeechText)
        AidInfo1 -> intArrayOf(R.string.aidInfo1SpeechText)
        AidInfo2 -> intArrayOf(R.string.aidInfo2SpeechText)
        AidInfo3 -> intArrayOf(R.string.aidInfo3SpeechText)
        AidInfo4 -> intArrayOf(R.string.aidInfo4SpeechText)
        AidInfo5 -> intArrayOf(R.string.aidInfo5SpeechText)
        AidInfo6 -> intArrayOf(R.string.aidInfo6SpeechText)
        AidInfo7 -> intArrayOf(R.string.aidInfo7SpeechText)
        SupportInfo0 -> intArrayOf(R.string.supportInfo0SpeechText)
        SupportInfo1 -> intArrayOf(R.string.supportInfo1SpeechText)
        SupportInfo2 -> intArrayOf(R.string.supportInfo2SpeechText)
        SupportInfo3 -> intArrayOf(R.string.supportInfo3SpeechText)
        SupportInfo4 -> intArrayOf(R.string.supportInfo4SpeechText)
    }

    /**
     * Returns the content to show.
     *
     * @return The content string.
     */
    fun contentString(): Int = when (this) {
        AmslerTest -> R.string.amslerTestInstruction
        ReadingTest -> R.string.readingTestInstruction
        Visus -> R.string.visusInstruction
        NHD -> R.string.nhdInstruction
        AMD -> R.string.amdInstruction
        DMO -> R.string.dmoInstruction
        RVV -> R.string.rvvInstruction
        MCNV -> R.string.mcnvInstruction
        Addresses -> R.string.addressesInstruction
        News -> R.string.newsInstruction
        Inprint -> R.string.inprintInstruction
        Version -> R.string.versionInstruction
        Backup -> R.string.backupInstruction
        Manual -> R.string.manualInstruction
        Diagnose -> R.string.diagnoseInstruction
        IllnessInfo0 -> R.string.illnessInfo0Instruction
        IllnessInfo1 -> R.string.illnessInfo1Instruction
        IllnessInfo2 -> R.string.illnessInfo2Instruction
        IllnessInfo3 -> R.string.illnessInfo3Instruction
        IllnessInfo4 -> R.string.illnessInfo4Instruction
        IllnessInfo5 -> R.string.illnessInfo5Instruction
        IllnessInfo6 -> R.string.illnessInfo6Instruction
        IllnessInfo7 -> R.string.illnessInfo7Instruction
        IllnessInfo8 -> R.string.illnessInfo8Instruction
        IllnessInfo9 -> R.string.illnessInfo9Instruction
        ExaminationInfo0 -> R.string.examinationInfo0Instruction
        ExaminationInfo1 -> R.string.examinationInfo1Instruction
        ExaminationInfo2 -> R.string.examinationInfo2Instruction
        ExaminationInfo3 -> R.string.examinationInfo3Instruction
        ExaminationInfo4 -> R.string.examinationInfo4Instruction
        ExaminationInfo5 -> R.string.examinationInfo5Instruction
        ExaminationInfo6 -> R.string.examinationInfo6Instruction
        TherapyInfo0 -> R.string.therapyInfo0Instruction
        TherapyInfo1 -> R.string.therapyInfo1Instruction
        TherapyInfo2 -> R.string.therapyInfo2Instruction
        TherapyInfo3 -> R.string.therapyInfo3Instruction
        TherapyInfo4 -> R.string.therapyInfo4Instruction
        TherapyInfo5 -> R.string.therapyInfo5Instruction
        ActivitiesInfo0 -> R.string.activitiesInfo0Instruction
        ActivitiesInfo1 -> R.string.activitiesInfo1Instruction
        ActivitiesInfo2 -> R.string.activitiesInfo2Instruction
        ActivitiesInfo3 -> R.string.activitiesInfo3Instruction
        ActivitiesInfo4 -> R.string.activitiesInfo4Instruction
        ActivitiesInfo5 -> R.string.activitiesInfo5Instruction
        AidInfo0 -> R.string.aidInfo0Instruction
        AidInfo1 -> R.string.aidInfo1Instruction
        AidInfo2 -> R.string.aidInfo2Instruction
        AidInfo3 -> R.string.aidInfo3Instruction
        AidInfo4 -> R.string.aidInfo4Instruction
        AidInfo5 -> R.string.aidInfo5Instruction
        AidInfo6 -> R.string.aidInfo6Instruction
        AidInfo7 -> R.string.aidInfo7Instruction
        SupportInfo0 -> R.string.supportInfo0Instruction
        SupportInfo1 -> R.string.supportInfo1Instruction
        SupportInfo2 -> R.string.supportInfo2Instruction
        SupportInfo3 -> R.string.supportInfo3Instruction
        SupportInfo4 -> R.string.supportInfo4Instruction
    }
}

/* The visus or NHD type */
enum class VisusNhdType {
    // A Visus value
    Visus,
    // A NHD value
    Nhd;

    /**
     * Returns the value as a formatted string inclusive unit of measurement for its type.
     *
     * @param context: The context of the current scene activity.
     * @param value: The value to format.
     * @return The formatted string, e.g. "480 µm".
     */
    fun valueOutput(context: Context, value: Int?): String {
        if (value == null) {
            return when (this) {
                Visus -> context.getString(R.string.visusOutputNoValue)
                Nhd -> context.getString(R.string.nhdOutputNoValue)
            }
        } else {
            return when (this) {
                Visus -> {
                    val formatString = context.getString(R.string.visusOutput)
                    val valueString = VisusNhdInputConst.visusValues[value]
                    String.format(formatString, valueString)
                }
                Nhd -> {
                    val formatString = context.getString(R.string.nhdOutput)
                    String.format(formatString, value)
                }
            }
        }
    }

    /**
     * Returns the title for the scene.
     *
     * @return The scene's title string.
     */
    fun titleString(): Int = when (this) {
        Visus -> R.string.visusTitle
        Nhd -> R.string.nhdTitle
    }

    /**
     * Returns the min value for this type.
     *
     * @return The integer value.
     */
    fun minValue(): Int = when (this) {
        Visus -> Const.Data.visusMinValue
        Nhd -> Const.Data.nhdMinValue
    }

    /**
     * Returns the max value for this type.
     *
     * @return The integer value.
     */
    fun maxValue(): Int = when (this) {
        Visus -> Const.Data.visusMaxValue
        Nhd -> Const.Data.nhdMaxValue
    }

    /**
     * Returns the value to use for representing the default value, e.g. the middle of all available.
     *
     * @return The integer value.
     */
    fun middleValue(): Int = when (this) {
        Visus -> maxValue() / 2
        Nhd -> minValue() + (maxValue() - minValue()) / 2
    }
}