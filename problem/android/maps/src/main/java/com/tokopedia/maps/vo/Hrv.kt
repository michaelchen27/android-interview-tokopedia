package com.tokopedia.maps.vo


import com.google.gson.annotations.SerializedName

data class Hrv(
    @SerializedName("common")
    val common: String,
    @SerializedName("official")
    val official: String
)