package com.tokopedia.maps

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.gson.Gson
import com.tokopedia.maps.data.api.RestCountriesInterface
import com.tokopedia.maps.vo.CountryItem
import okhttp3.ResponseBody
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.*
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

open class MapsActivity : AppCompatActivity(), OnMapReadyCallback {
    private val TAG = MapsActivity::class.java.simpleName
    private var mapFragment: SupportMapFragment? = null
    private var googleMap: GoogleMap? = null

    private lateinit var textCountryName: TextView
    private lateinit var textCountryCapital: TextView
    private lateinit var textCountryPopulation: TextView
    private lateinit var textCountryCallCode: TextView

    private var editText: EditText? = null
    private var buttonSubmit: View? = null

    private val BASE_URL = "https://restcountries.com/"
//    private var myCompositeDisposable: CompositeDisposable? = null

    private val gson: Gson = Gson()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
//        myCompositeDisposable = CompositeDisposable()
        bindViews()
        loadMap()
        initListeners()
    }

    private fun bindViews() {
        mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        editText = findViewById(R.id.editText)
        buttonSubmit = findViewById(R.id.buttonSubmit)
        textCountryName = findViewById(R.id.txtCountryName)
        textCountryCapital = findViewById(R.id.txtCountryCapital)
        textCountryPopulation = findViewById(R.id.txtCountryPopulation)
        textCountryCallCode = findViewById(R.id.txtCountryCallCode)
    }

    private fun initListeners() {
        buttonSubmit!!.setOnClickListener {
            // TODO
            // search by the given country name, and
            // 1. pin point to the map
            // 2. set the country information to the textViews.

            // Loading
            textCountryName.text = ("Nama Negara: Loading...")
            textCountryCapital.text = ("Ibukota: Loading...")
            textCountryPopulation.text = ("Jumlah Penduduk: Loading...")
            textCountryCallCode.text = ("Kode Telepon: Loading...")


            val searchQuery = editText?.text.toString()
            if (searchQuery.isNotEmpty()) {
                loadData(searchQuery)
            } else {
                Toast.makeText(this, "Search box is empty", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun loadData(countryName: String) {
        val requestInterface = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()

        val api: RestCountriesInterface =
            requestInterface.create(RestCountriesInterface::class.java)

        val call: Call<ResponseBody> = api.getString(countryName)

        call.enqueue(object : Callback<ResponseBody?> {
            override fun onResponse(call: Call<ResponseBody?>, response: Response<ResponseBody?>) {
                if (response.isSuccessful) {
                    if (response.body() != null) {
                        val jsonResponseString = response.body()?.string()
                        Log.d(TAG, "onResponse: $jsonResponseString")
                        setupTextViews(jsonResponseString.toString())
                    }
                } else {
                    errToast(response.code())
                }
            }

            // 200
            override fun onFailure(call: Call<ResponseBody?>, t: Throwable) {
            }
        })
    }

    private fun errToast(code: Int) {
        var msg: String
        if (code == 404) {
            msg = "Data not found"
            googleMap?.clear()
            googleMap?.animateCamera(CameraUpdateFactory.zoomOut(), 500, null)
            textCountryName.text = ("Nama Negara:")
            textCountryCapital.text = ("Ibukota:")
            textCountryPopulation.text = ("Jumlah Penduduk:")
            textCountryCallCode.text = ("Kode Telepon:")

        } else {
            msg = "Something went wrong"
        }
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

    private fun addMapMarkers(jsonArray: JSONArray) {
        googleMap?.clear()
        var lastCountryLatLng = LatLng(0.00, 0.00)
        for (i in 0 until jsonArray.length()) {
            val json = jsonArray.getJSONObject(i).toString()
            val countryItem: CountryItem = gson.fromJson(json, CountryItem::class.java)
            val latLng = LatLng(countryItem.latlng[0], countryItem.latlng[1])
            val countryName = countryItem.name.common
            Log.d("LATLNG", "LATLNG: $latLng")

            if (googleMap != null) {
                googleMap?.addMarker(
                    MarkerOptions()
                        .position(latLng)
                        .title(countryName)
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                )
            }

            if (i == jsonArray.length() - 1) {
                lastCountryLatLng = latLng
            }
        }

        if (googleMap != null) {
            googleMap?.animateCamera(
                CameraUpdateFactory.newLatLngZoom(lastCountryLatLng, 5f),
                500,
                null
            )
        }
    }


    private fun setupTextViews(responses: String) {
        try {
            Log.d(TAG, "RESPONSE: $responses")
            val jsonArray = JSONArray(responses)
            val arrayLength = jsonArray.length()
            if (arrayLength > 0) {
                addMapMarkers(jsonArray)
                val lastObj = jsonArray.getJSONObject(arrayLength - 1)
                val json = lastObj.toString()
                val countryItem: CountryItem = gson.fromJson(json, CountryItem::class.java)

                val capital = countryItem.capital[0]
                val population = countryItem.population.toString()
                val callCode = "${countryItem.idd.root}${countryItem.idd.suffixes.first()}"

                textCountryCapital.text = ("Ibukota: $capital")
                textCountryPopulation.text = ("Jumlah penduduk: $population")
                textCountryCallCode.text = ("Kode Telepon: $callCode")

                val nameObj = JSONObject(lastObj.get("name").toString())
                val nativeNameObj = JSONObject(nameObj.get("nativeName").toString())

                val keys: Iterator<String> = nativeNameObj.keys()

                var dynamicKeys = arrayListOf<String>()
                while (keys.hasNext()) {
                    val key: String = keys.next()
                    dynamicKeys.add(key)
                }

                if (dynamicKeys.isNotEmpty()) {
                    val firstNativeNameObj =
                        JSONObject(nativeNameObj.get(dynamicKeys[0]).toString())
                    val commonName = firstNativeNameObj.get("common")
                    textCountryName.text = ("Nama Negara: $commonName")
                }
            }
        } catch (e: HttpException) {
            Log.e(TAG, "setupTextViews: ${e.message}")
            val msg = if (e.code() == 404) "Data not found" else "Something went wrong"
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
        }
    }


    private fun loadMap() {
        mapFragment?.getMapAsync(this)

    }


    override fun onMapReady(googleMap: GoogleMap) {
        this.googleMap = googleMap
//        // Set Marker
//        val latLng = LatLng(-5.00, 120.00)
//        googleMap.addMarker(
//            MarkerOptions()
//                .position(latLng)
//                .title("Marker in Sydney")
//                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
//        )
//        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 2.0f))

    }
}


/* UNUSED CODE
*
*
* //        myCompositeDisposable?.add(
//            api.getCountryDetails(countryName)
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribeOn(Schedulers.io())
//                .subscribe(this::handleResponse)
//        )
*
*
*
*
* //    // Try using GsonConverterFactory
//    private fun handleResponse(countryDetails: Country) {
//        try {
//            val lastCountryDetails = countryDetails.last()
//            val capital = lastCountryDetails.capital.first()
//            val population = lastCountryDetails.population.toString()
//            val callCode =
//                "${lastCountryDetails.idd.root}${lastCountryDetails.idd.suffixes.last()}"
//
//            textCountryCapital.text = ("Ibukota: $capital")
//            textCountryPopulation.text = ("Jumlah penduduk: $population")
//            textCountryCallCode.text = ("Kode Telepon: $callCode")
//        } catch (e: Exception) {
//            Log.e(TAG, "handleResponse: $e")
//            Toast.makeText(this, "${e.message}", Toast.LENGTH_SHORT).show()
//        }
//    }
*
*
* */
