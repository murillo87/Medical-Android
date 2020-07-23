package com.appamedix.makula.scenes.search

data class SearchDisplayModel(
        // The activity for this scene.
        val activity: SearchSceneActivity,
        // The search string to apply for the search.
        val searchString: String,
        // Whether the display should use a large style, e.g. for landscape
        // or not in which case the default style is used, e.g. portrait.
        val largeStyle:Boolean
)