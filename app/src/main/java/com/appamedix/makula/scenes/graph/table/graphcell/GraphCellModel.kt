package com.appamedix.makula.scenes.graph.table.graphcell

import com.appamedix.makula.scenes.graph.EyeType
import com.appamedix.makula.worker.datamodel.histogram.NhdObject
import com.appamedix.makula.worker.datamodel.histogram.VisusObject
import java.util.*
import kotlin.collections.ArrayList

/* The model for the `GraphCell` */
data class GraphCellModel(
        // When `true` an extra large font size (e.g. for landscape mode) and scaled icons will be used,
        // while `false` the default size (e.g. for portrait) uses.
        val largeStyle: Boolean,
        // The date of the last treatment appointments.
        val ivomDates: ArrayList<Date>,
        // The type of eye this scene represents.
        val eyeType: EyeType,
        // The Visus objects to show in the graph.
        val visusObjects: ArrayList<VisusObject>,
        // The NHD objects to show in the graph.
        val nhdObjects: ArrayList<NhdObject>
)