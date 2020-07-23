package com.appamedix.makula.scenes.menu.table

import com.appamedix.makula.R
import com.appamedix.makula.types.InfoType
import com.appamedix.makula.types.SceneId

/* The table model of an entry */
data class MenuTableRawEntry(
        val title: Int,                     // The resource id of the cell's title
        val darkStyle: Boolean,             // Whether the cell's dark style is used or not
        val separatorVisible: Boolean,      // Shows or hides the separator
        val sceneId: SceneId                // The corresponding scene ID
)

/* All possible cells in the menu with their accessibility identifier as key */
enum class MenuCellIdentifier(val rawValue: String) {
    /* Root menu */

    // `Arztbesuch`
    DoctorVisit("MenuCellDoctorVisit"),
    // `Termine`
    NewAppointment("MenuCellNewAppointment"),
    // `Kalender`
    Calendar("MenuCellCalendar"),
    // `Kontakte`
    ContactPerson("MenuCellContactPerson"),
    // `Selbsttest`
    SelfTest("MenuCellSelfTest"),
    // `Wissen/Information`
    Knowledge("MenuCellKnowledge"),
    // `Adressverzeichnis`
    Addresses("MenuCellAddresses"),
    // `Aktuelles`
    News("MenuCellNews"),
    // `Suchfunktion`
    Search("MenuCellSearch"),
    // `Einstellungen`
    Settings("MenuCellSettings"),
    // `Impressum`
    Inprint("MenuCellInprint"),
    // `Version`
    Version("MenuCellVersion"),

    /* Doctor visit menu */

    //  The date of the last treatment (`Behandlung`)
    Treatment("MenuCellTreatment"),
    // `Diagnose`
    Diagnosis("MenuCellDiagnosis"),
    // `Medikamente`
    Medicament("MenuCellMedicament"),
    // `Visus-Eingabe`
    VisusInput("MenuCellVisusInput"),
    // `NH-Dicke-Eingabe`
    NhdInput("MenuCellNHDInput"),
    // `OCT/Visus`
    OctVisus("MenuCellOctVisus"),

    /* Self test menu */

    // `Amslertest`
    AmslerTest("MenuCellAmslerTest"),
    // `Lesetest`
    ReadingTest("MenuCellReadingTest"),

    /* Knowledge menu */

    // `Bedienung`
    Manual("MenuCellManual"),
    // `Erkrankung`
    Illness("MenuCellIllness"),
    // `Untersuchung`
    Examination("MenuCellExamination"),
    // `Therapie`
    Therapy("MenuCellTherapy"),
    // `Maßnahmen`
    Activities("MenuCellActivities"),
    // `Hilfsmittel`
    Aid("MenuCellAid"),
    // `Unterstützung`
    Support("MenuCellSupport"),
    // `Diagnose`
    Diagnose("MenuCellDiagnose"),

    /* Settings menu */

    // `Erinnerung`
    Reminder("MenuCellReminder"),
    // `Backup`
    Backup("MenuCellBackup"),

    /* Illness menu */

    IllnessInfo0("MenuCellIllnessInfo0"),
    IllnessInfo1("MenuCellIllnessInfo1"),
    IllnessInfo2("MenuCellIllnessInfo2"),
    IllnessInfo3("MenuCellIllnessInfo3"),
    IllnessInfo4("MenuCellIllnessInfo4"),
    IllnessInfo5("MenuCellIllnessInfo5"),
    IllnessInfo6("MenuCellIllnessInfo6"),
    IllnessInfo7("MenuCellIllnessInfo7"),
    IllnessInfo8("MenuCellIllnessInfo8"),
    IllnessInfo9("MenuCellIllnessInfo9"),

    /* Examination menu */

    ExaminationInfo0("MenuCellExaminationInfo0"),
    ExaminationInfo1("MenuCellExaminationInfo1"),
    ExaminationInfo2("MenuCellExaminationInfo2"),
    ExaminationInfo3("MenuCellExaminationInfo3"),
    ExaminationInfo4("MenuCellExaminationInfo4"),
    ExaminationInfo5("MenuCellExaminationInfo5"),
    ExaminationInfo6("MenuCellExaminationInfo6"),

    /* Therapy menu */

    TherapyInfo0("MenuCellTherapyInfo0"),
    TherapyInfo1("MenuCellTherapyInfo1"),
    TherapyInfo2("MenuCellTherapyInfo2"),
    TherapyInfo3("MenuCellTherapyInfo3"),
    TherapyInfo4("MenuCellTherapyInfo4"),
    TherapyInfo5("MenuCellTherapyInfo5"),

    /* Activities menu */

    ActivitiesInfo0("MenuCellActivitiesInfo0"),
    ActivitiesInfo1("MenuCellActivitiesInfo1"),
    ActivitiesInfo2("MenuCellActivitiesInfo2"),
    ActivitiesInfo3("MenuCellActivitiesInfo3"),
    ActivitiesInfo4("MenuCellActivitiesInfo4"),
    ActivitiesInfo5("MenuCellActivitiesInfo5"),

    /* Aid menu */

    AidInfo0("MenuCellAidInfo0"),
    AidInfo1("MenuCellAidInfo1"),
    AidInfo2("MenuCellAidInfo2"),
    AidInfo3("MenuCellAidInfo3"),
    AidInfo4("MenuCellAidInfo4"),
    AidInfo5("MenuCellAidInfo5"),
    AidInfo6("MenuCellAidInfo6"),
    AidInfo7("MenuCellAidInfo7"),

    /* Support menu */

    SupportInfo0("MenuCellSupportInfo0"),
    SupportInfo1("MenuCellSupportInfo1"),
    SupportInfo2("MenuCellSupportInfo2"),
    SupportInfo3("MenuCellSupportInfo3"),
    SupportInfo4("MenuCellSupportInfo4"),

    /* No menu type */
    NoMenu("NoMenuCell");

    /**
     * Returns the raw cell data of entries for the table cells.
     * @return The raw data entry.
     */
    fun rawData(): MenuTableRawEntry = when (this) {
        DoctorVisit -> MenuTableRawEntry(R.string.homeMenuCell0, true, true, SceneId.DoctorVisitMenu)
        NewAppointment -> MenuTableRawEntry(R.string.homeMenuCell1, true, true, SceneId.NewAppointment)
        Calendar -> MenuTableRawEntry(R.string.homeMenuCell2, true, true, SceneId.Calendar)
        ContactPerson -> MenuTableRawEntry(R.string.homeMenuCell3, true, true, SceneId.Contact)
        SelfTest -> MenuTableRawEntry(R.string.homeMenuCell4, true, false, SceneId.SelfTestMenu)
        Knowledge -> MenuTableRawEntry(R.string.homeMenuCell5, false, true, SceneId.KnowledgeMenu)
        Addresses -> MenuTableRawEntry(R.string.homeMenuCell6, false, true, SceneId.Info)
        News -> MenuTableRawEntry(R.string.homeMenuCell7, false, true, SceneId.Info)
        Search -> MenuTableRawEntry(R.string.homeMenuCell8, false, true, SceneId.Search)
        Settings -> MenuTableRawEntry(R.string.homeMenuCell9, false, true, SceneId.SettingsMenu)
        Inprint -> MenuTableRawEntry(R.string.homeMenuCell10, false, true, SceneId.Info)
        Version -> MenuTableRawEntry(R.string.homeMenuCell11, false, false, SceneId.Info)
        Manual -> MenuTableRawEntry(R.string.homeMenuCell12, false, true, SceneId.Info)

        Treatment -> MenuTableRawEntry(R.string.doctorVisitMenuCell0, true, true, SceneId.AppointmentDetail)
        Diagnosis -> MenuTableRawEntry(R.string.doctorVisitMenuCell1, true, true, SceneId.Diagnosis)
        Medicament -> MenuTableRawEntry(R.string.doctorVisitMenuCell2, true, true, SceneId.Medicament)
        VisusInput -> MenuTableRawEntry(R.string.doctorVisitMenuCell3, true, true, SceneId.VisusInput)
        NhdInput -> MenuTableRawEntry(R.string.doctorVisitMenuCell4, true, true, SceneId.NhdInput)
        OctVisus -> MenuTableRawEntry(R.string.doctorVisitMenuCell5, true, true, SceneId.Graph)

        AmslerTest -> MenuTableRawEntry(R.string.selfTestMenuCell0, true, true, SceneId.AmslerTest)
        ReadingTest -> MenuTableRawEntry(R.string.selfTestMenuCell1, true, true, SceneId.ReadingTest)

        Illness -> MenuTableRawEntry(R.string.knowledgeMenuCell0, false, true, SceneId.IllnessMenu)
        Examination -> MenuTableRawEntry(R.string.knowledgeMenuCell1, false, true, SceneId.ExaminationMenu)
        Therapy -> MenuTableRawEntry(R.string.knowledgeMenuCell2, false, true, SceneId.TherapyMenu)
        Activities -> MenuTableRawEntry(R.string.knowledgeMenuCell3, false, true, SceneId.ActivitiesMenu)
        Aid -> MenuTableRawEntry(R.string.knowledgeMenuCell4, false, true, SceneId.AidMenu)
        Support -> MenuTableRawEntry(R.string.knowledgeMenuCell5, false, true, SceneId.SupportMenu)
        Diagnose -> MenuTableRawEntry(R.string.knowledgeMenuCell6, false, true, SceneId.Info)

        Reminder -> MenuTableRawEntry(R.string.settingsMenuCell0, false, true, SceneId.Reminder)
        Backup -> MenuTableRawEntry(R.string.settingsMenuCell1, false, true, SceneId.Info)

        IllnessInfo0 -> MenuTableRawEntry(R.string.illnessMenuCell0, false, true, SceneId.Info)
        IllnessInfo1 -> MenuTableRawEntry(R.string.illnessMenuCell1, false, true, SceneId.Info)
        IllnessInfo2 -> MenuTableRawEntry(R.string.illnessMenuCell2, false, true, SceneId.Info)
        IllnessInfo3 -> MenuTableRawEntry(R.string.illnessMenuCell3, false, true, SceneId.Info)
        IllnessInfo4 -> MenuTableRawEntry(R.string.illnessMenuCell4, false, true, SceneId.Info)
        IllnessInfo5 -> MenuTableRawEntry(R.string.illnessMenuCell5, false, true, SceneId.Info)
        IllnessInfo6 -> MenuTableRawEntry(R.string.illnessMenuCell6, false, true, SceneId.Info)
        IllnessInfo7 -> MenuTableRawEntry(R.string.illnessMenuCell7, false, true, SceneId.Info)
        IllnessInfo8 -> MenuTableRawEntry(R.string.illnessMenuCell8, false, true, SceneId.Info)
        IllnessInfo9 -> MenuTableRawEntry(R.string.illnessMenuCell9, false, true, SceneId.Info)

        ExaminationInfo0 -> MenuTableRawEntry(R.string.examinationMenuCell0, false, true, SceneId.Info)
        ExaminationInfo1 -> MenuTableRawEntry(R.string.examinationMenuCell1, false, true, SceneId.Info)
        ExaminationInfo2 -> MenuTableRawEntry(R.string.examinationMenuCell2, false, true, SceneId.Info)
        ExaminationInfo3 -> MenuTableRawEntry(R.string.examinationMenuCell3, false, true, SceneId.Info)
        ExaminationInfo4 -> MenuTableRawEntry(R.string.examinationMenuCell4, false, true, SceneId.Info)
        ExaminationInfo5 -> MenuTableRawEntry(R.string.examinationMenuCell5, false, true, SceneId.Info)
        ExaminationInfo6 -> MenuTableRawEntry(R.string.examinationMenuCell6, false, true, SceneId.Info)

        TherapyInfo0 -> MenuTableRawEntry(R.string.therapyMenuCell0, false, true, SceneId.Info)
        TherapyInfo1 -> MenuTableRawEntry(R.string.therapyMenuCell1, false, true, SceneId.Info)
        TherapyInfo2 -> MenuTableRawEntry(R.string.therapyMenuCell2, false, true, SceneId.Info)
        TherapyInfo3 -> MenuTableRawEntry(R.string.therapyMenuCell3, false, true, SceneId.Info)
        TherapyInfo4 -> MenuTableRawEntry(R.string.therapyMenuCell4, false, true, SceneId.Info)
        TherapyInfo5 -> MenuTableRawEntry(R.string.therapyMenuCell5, false, true, SceneId.Info)

        ActivitiesInfo0 -> MenuTableRawEntry(R.string.activitiesMenuCell0, false, true, SceneId.Info)
        ActivitiesInfo1 -> MenuTableRawEntry(R.string.activitiesMenuCell1, false, true, SceneId.Info)
        ActivitiesInfo2 -> MenuTableRawEntry(R.string.activitiesMenuCell2, false, true, SceneId.Info)
        ActivitiesInfo3 -> MenuTableRawEntry(R.string.activitiesMenuCell3, false, true, SceneId.Info)
        ActivitiesInfo4 -> MenuTableRawEntry(R.string.activitiesMenuCell4, false, true, SceneId.Info)
        ActivitiesInfo5 -> MenuTableRawEntry(R.string.activitiesMenuCell5, false, true, SceneId.Info)

        AidInfo0 -> MenuTableRawEntry(R.string.aidMenuCell0, false, true, SceneId.Info)
        AidInfo1 -> MenuTableRawEntry(R.string.aidMenuCell1, false, true, SceneId.Info)
        AidInfo2 -> MenuTableRawEntry(R.string.aidMenuCell2, false, true, SceneId.Info)
        AidInfo3 -> MenuTableRawEntry(R.string.aidMenuCell3, false, true, SceneId.Info)
        AidInfo4 -> MenuTableRawEntry(R.string.aidMenuCell4, false, true, SceneId.Info)
        AidInfo5 -> MenuTableRawEntry(R.string.aidMenuCell5, false, true, SceneId.Info)
        AidInfo6 -> MenuTableRawEntry(R.string.aidMenuCell6, false, true, SceneId.Info)
        AidInfo7 -> MenuTableRawEntry(R.string.aidMenuCell7, false, true, SceneId.Info)

        SupportInfo0 -> MenuTableRawEntry(R.string.supportMenuCell0, false, true, SceneId.Info)
        SupportInfo1 -> MenuTableRawEntry(R.string.supportMenuCell1, false, true, SceneId.Info)
        SupportInfo2 -> MenuTableRawEntry(R.string.supportMenuCell2, false, true, SceneId.Info)
        SupportInfo3 -> MenuTableRawEntry(R.string.supportMenuCell3, false, true, SceneId.Info)
        SupportInfo4 -> MenuTableRawEntry(R.string.supportMenuCell4, false, true, SceneId.Info)

        else -> throw IllegalArgumentException("Invalid menu")
    }

    /**
     * Returns the resource id of the speech text for the table cells.
     * @return The resource id
     */
    fun speechText(): Int = when (this) {
        DoctorVisit -> R.string.homeMenuCell0Speech
        NewAppointment -> R.string.homeMenuCell1Speech
        Calendar -> R.string.homeMenuCell2Speech
        ContactPerson -> R.string.homeMenuCell3Speech
        SelfTest -> R.string.homeMenuCell4Speech
        Knowledge -> R.string.homeMenuCell5Speech
        Addresses -> R.string.homeMenuCell6Speech
        News -> R.string.homeMenuCell7Speech
        Search -> R.string.homeMenuCell8Speech
        Settings -> R.string.homeMenuCell9Speech
        Inprint -> R.string.homeMenuCell10Speech
        Version -> R.string.homeMenuCell11Speech
        Manual -> R.string.homeMenuCell12Speech

        Treatment -> R.string.doctorVisitMenuCell0Speech
        Diagnosis -> R.string.doctorVisitMenuCell1Speech
        Medicament -> R.string.doctorVisitMenuCell2Speech
        VisusInput -> R.string.doctorVisitMenuCell3Speech
        NhdInput -> R.string.doctorVisitMenuCell4Speech
        OctVisus -> R.string.doctorVisitMenuCell5Speech

        AmslerTest -> R.string.selfTestMenuCell0Speech
        ReadingTest -> R.string.selfTestMenuCell1Speech

        Illness -> R.string.knowledgeMenuCell0Speech
        Examination -> R.string.knowledgeMenuCell1Speech
        Therapy -> R.string.knowledgeMenuCell2Speech
        Activities -> R.string.knowledgeMenuCell3Speech
        Aid -> R.string.knowledgeMenuCell4Speech
        Support -> R.string.knowledgeMenuCell5Speech
        Diagnose -> R.string.knowledgeMenuCell6Speech

        Reminder -> R.string.settingsMenuCell0Speech
        Backup -> R.string.settingsMenuCell1Speech

        IllnessInfo0 -> R.string.illnessMenuCell0Speech
        IllnessInfo1 -> R.string.illnessMenuCell1Speech
        IllnessInfo2 -> R.string.illnessMenuCell2Speech
        IllnessInfo3 -> R.string.illnessMenuCell3Speech
        IllnessInfo4 -> R.string.illnessMenuCell4Speech
        IllnessInfo5 -> R.string.illnessMenuCell5Speech
        IllnessInfo6 -> R.string.illnessMenuCell6Speech
        IllnessInfo7 -> R.string.illnessMenuCell7Speech
        IllnessInfo8 -> R.string.illnessMenuCell8Speech
        IllnessInfo9 -> R.string.illnessMenuCell9Speech

        ExaminationInfo0 -> R.string.examinationMenuCell0Speech
        ExaminationInfo1 -> R.string.examinationMenuCell1Speech
        ExaminationInfo2 -> R.string.examinationMenuCell2Speech
        ExaminationInfo3 -> R.string.examinationMenuCell3Speech
        ExaminationInfo4 -> R.string.examinationMenuCell4Speech
        ExaminationInfo5 -> R.string.examinationMenuCell5Speech
        ExaminationInfo6 -> R.string.examinationMenuCell6Speech

        TherapyInfo0 -> R.string.therapyMenuCell0Speech
        TherapyInfo1 -> R.string.therapyMenuCell1Speech
        TherapyInfo2 -> R.string.therapyMenuCell2Speech
        TherapyInfo3 -> R.string.therapyMenuCell3Speech
        TherapyInfo4 -> R.string.therapyMenuCell4Speech
        TherapyInfo5 -> R.string.therapyMenuCell5Speech

        ActivitiesInfo0 -> R.string.activitiesMenuCell0Speech
        ActivitiesInfo1 -> R.string.activitiesMenuCell1Speech
        ActivitiesInfo2 -> R.string.activitiesMenuCell2Speech
        ActivitiesInfo3 -> R.string.activitiesMenuCell3Speech
        ActivitiesInfo4 -> R.string.activitiesMenuCell4Speech
        ActivitiesInfo5 -> R.string.activitiesMenuCell5Speech

        AidInfo0 -> R.string.aidMenuCell0Speech
        AidInfo1 -> R.string.aidMenuCell1Speech
        AidInfo2 -> R.string.aidMenuCell2Speech
        AidInfo3 -> R.string.aidMenuCell3Speech
        AidInfo4 -> R.string.aidMenuCell4Speech
        AidInfo5 -> R.string.aidMenuCell5Speech
        AidInfo6 -> R.string.aidMenuCell6Speech
        AidInfo7 -> R.string.aidMenuCell7Speech

        SupportInfo0 -> R.string.supportMenuCell0Speech
        SupportInfo1 -> R.string.supportMenuCell1Speech
        SupportInfo2 -> R.string.supportMenuCell2Speech
        SupportInfo3 -> R.string.supportMenuCell3Speech
        SupportInfo4 -> R.string.supportMenuCell4Speech

        else -> throw IllegalArgumentException("Invalid menu")
    }

    /**
     * Returns the type of information from this cell identifier.
     *
     * @return The info type.
     */
    fun infoType(): InfoType = when (this) {
        Addresses -> InfoType.Addresses
        News -> InfoType.News
        Inprint -> InfoType.Inprint
        Version -> InfoType.Version
        Manual -> InfoType.Manual
        Diagnose -> InfoType.Diagnose
        Backup -> InfoType.Backup
        IllnessInfo0 -> InfoType.IllnessInfo0
        IllnessInfo1 -> InfoType.IllnessInfo1
        IllnessInfo2 -> InfoType.IllnessInfo2
        IllnessInfo3 -> InfoType.IllnessInfo3
        IllnessInfo4 -> InfoType.IllnessInfo4
        IllnessInfo5 -> InfoType.IllnessInfo5
        IllnessInfo6 -> InfoType.IllnessInfo6
        IllnessInfo7 -> InfoType.IllnessInfo7
        IllnessInfo8 -> InfoType.IllnessInfo8
        IllnessInfo9 -> InfoType.IllnessInfo9
        ExaminationInfo0 -> InfoType.ExaminationInfo0
        ExaminationInfo1 -> InfoType.ExaminationInfo1
        ExaminationInfo2 -> InfoType.ExaminationInfo2
        ExaminationInfo3 -> InfoType.ExaminationInfo3
        ExaminationInfo4 -> InfoType.ExaminationInfo4
        ExaminationInfo5 -> InfoType.ExaminationInfo5
        ExaminationInfo6 -> InfoType.ExaminationInfo6
        TherapyInfo0 -> InfoType.TherapyInfo0
        TherapyInfo1 -> InfoType.TherapyInfo1
        TherapyInfo2 -> InfoType.TherapyInfo2
        TherapyInfo3 -> InfoType.TherapyInfo3
        TherapyInfo4 -> InfoType.TherapyInfo4
        TherapyInfo5 -> InfoType.TherapyInfo5
        ActivitiesInfo0 -> InfoType.ActivitiesInfo0
        ActivitiesInfo1 -> InfoType.ActivitiesInfo1
        ActivitiesInfo2 -> InfoType.ActivitiesInfo2
        ActivitiesInfo3 -> InfoType.ActivitiesInfo3
        ActivitiesInfo4 -> InfoType.ActivitiesInfo4
        ActivitiesInfo5 -> InfoType.ActivitiesInfo5
        AidInfo0 -> InfoType.AidInfo0
        AidInfo1 -> InfoType.AidInfo1
        AidInfo2 -> InfoType.AidInfo2
        AidInfo3 -> InfoType.AidInfo3
        AidInfo4 -> InfoType.AidInfo4
        AidInfo5 -> InfoType.AidInfo5
        AidInfo6 -> InfoType.AidInfo6
        AidInfo7 -> InfoType.AidInfo7
        SupportInfo0 -> InfoType.SupportInfo0
        SupportInfo1 -> InfoType.SupportInfo1
        SupportInfo2 -> InfoType.SupportInfo2
        SupportInfo3 -> InfoType.SupportInfo3
        SupportInfo4 -> InfoType.SupportInfo4

        else -> throw IllegalArgumentException("Invalid type")
    }
}