package com.tokopedia.maps.vo


import com.google.gson.annotations.SerializedName

data class Deu(
    @SerializedName("common")
    val common: String,
    @SerializedName("official")
    val official: String
)