package com.tokopedia.maps.vo


import com.google.gson.annotations.SerializedName

data class Translations(
    @SerializedName("ara")
    val ara: Ara,
    @SerializedName("bre")
    val bre: Bre,
    @SerializedName("ces")
    val ces: Ces,
    @SerializedName("cym")
    val cym: Cym,
    @SerializedName("deu")
    val deu: Deu,
    @SerializedName("est")
    val est: Est,
    @SerializedName("fin")
    val fin: Fin,
    @SerializedName("fra")
    val fra: FraX,
    @SerializedName("hrv")
    val hrv: Hrv,
    @SerializedName("hun")
    val hun: Hun,
    @SerializedName("ita")
    val ita: Ita,
    @SerializedName("jpn")
    val jpn: Jpn,
    @SerializedName("kor")
    val kor: Kor,
    @SerializedName("nld")
    val nld: Nld,
    @SerializedName("per")
    val per: Per,
    @SerializedName("pol")
    val pol: Pol,
    @SerializedName("por")
    val por: Por,
    @SerializedName("rus")
    val rus: Rus,
    @SerializedName("slk")
    val slk: Slk,
    @SerializedName("spa")
    val spa: Spa,
    @SerializedName("swe")
    val swe: Swe,
    @SerializedName("urd")
    val urd: Urd,
    @SerializedName("zho")
    val zho: Zho
)