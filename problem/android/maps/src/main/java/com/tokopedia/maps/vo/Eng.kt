package com.tokopedia.maps.vo


import com.google.gson.annotations.SerializedName

data class Eng(
    @SerializedName("f")
    val f: String,
    @SerializedName("m")
    val m: String
)