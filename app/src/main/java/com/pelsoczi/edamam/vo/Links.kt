package com.pelsoczi.edamam.vo

import com.google.gson.annotations.SerializedName


data class Links(
  @SerializedName("self") var self: Self? = Self(),
  @SerializedName("next") var next: Next? = Next()
)

data class Self(
  @SerializedName("href") var href: String? = null,
  @SerializedName("title") var title: String? = null
)

data class Next(
  @SerializedName("href") var href: String? = null,
  @SerializedName("title") var title: String? = null
)