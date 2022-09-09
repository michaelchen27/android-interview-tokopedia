package com.tokopedia.maps.vo


import com.google.gson.annotations.SerializedName

data class NativeName(
    @SerializedName("aym")
    val aym: Aym,
    @SerializedName("que")
    val que: Que,
    @SerializedName("spa")
    val spa: Spa
)