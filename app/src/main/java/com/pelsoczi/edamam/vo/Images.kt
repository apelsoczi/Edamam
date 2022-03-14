package com.pelsoczi.edamam.vo

import com.google.gson.annotations.SerializedName


data class Images (
    @SerializedName("THUMBNAIL") var thumbnail: Thumbnail? = Thumbnail(),
    @SerializedName("SMALL") var small: Small? = Small(),
    @SerializedName("REGULAR") var regular: Regular? = Regular(),
    @SerializedName("LARGE") var large: Large? = Large()
)

data class Thumbnail(
    @SerializedName("url") var url: String? = null,
    @SerializedName("width") var width: Int? = null,
    @SerializedName("height") var height: Int? = null
)

data class Small(
    @SerializedName("url") var url: String? = null,
    @SerializedName("width") var width: Int? = null,
    @SerializedName("height") var height: Int? = null
)

data class Large(
    @SerializedName("url") var url: String? = null,
    @SerializedName("width") var width: Int? = null,
    @SerializedName("height") var height: Int? = null
)

data class Regular(
    @SerializedName("url") var url: String? = null,
    @SerializedName("width") var width: Int? = null,
    @SerializedName("height") var height: Int? = null
)