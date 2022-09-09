package com.tokopedia.maps.vo


import com.google.gson.annotations.SerializedName

data class PEN(
    @SerializedName("name")
    val name: String,
    @SerializedName("symbol")
    val symbol: String
)