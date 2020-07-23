package com.appamedix.makula.scenes.graph.table.graphcell

import com.appamedix.makula.base.BaseCellViewModel
import com.appamedix.makula.scenes.graph.EyeType
import com.appamedix.makula.types.ViewCellType
import com.appamedix.makula.worker.datamodel.histogram.NhdObject
import com.appamedix.makula.worker.datamodel.histogram.VisusObject
import java.util.*
import kotlin.collections.ArrayList

/* The view model for the `GraphCell` */
class GraphCellViewModel(model: GraphCellModel) : BaseCellViewModel(ViewCellType.GraphCell) {
    // When `true` an extra large font size (e.g. for landscape mode) and scaled icons will be used,
    // while `false` the default size (e.g. for portrait) uses.
    var largeStyle: Boolean = false
    // The date of the last treatment appointments.
    var ivomDates: ArrayList<Date> = ArrayList()
    // The type of eye this scene represents.
    var eyeType: EyeType = EyeType.Left
    // The Visus objects to show in the graph.
    var visusObjects: ArrayList<VisusObject> = ArrayList()
    // The NHD objects to show in the graph.
    var nhdObjects: ArrayList<NhdObject> = ArrayList()

    init {
        this.largeStyle = model.largeStyle
        this.ivomDates = model.ivomDates
        this.eyeType = model.eyeType
        this.visusObjects = model.visusObjects
        this.nhdObjects = model.nhdObjects
    }

}