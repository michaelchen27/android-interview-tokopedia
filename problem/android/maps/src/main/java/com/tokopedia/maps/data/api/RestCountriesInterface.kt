package com.tokopedia.maps.data.api

import com.tokopedia.maps.vo.Country
import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface RestCountriesInterface {

    // https://restcountries.com/v3.1/name/{name}

    @GET("name/{name}")
    fun getString(@Path("name") name: String): Call<ResponseBody>

}