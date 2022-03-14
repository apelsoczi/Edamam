package com.pelsoczi.edamam.api

import com.google.gson.annotations.SerializedName

import com.pelsoczi.edamam.vo.Hits
import com.pelsoczi.edamam.vo.Links

/** A serialized class representation of data returned from the server */
data class RecipeSearchResponse(
    @SerializedName("from") var from: Int? = null,
    @SerializedName("to") var to: Int? = null,
//    @SerializedName("count") var count: Int? = null,
    @SerializedName("_links") var Links: Links? = Links(),
    @SerializedName("hits") var hits: ArrayList<Hits> = arrayListOf()
)
