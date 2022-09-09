package com.tokopedia.maps

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.gson.Gson
import com.tokopedia.maps.data.api.RestCountriesInterface
import com.tokopedia.maps.vo.CountryItem
import io.reactivex.disposables.CompositeDisposable
import okhttp3.ResponseBody
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.*
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

open class MapsActivity : AppCompatActivity() {
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
    private var myCompositeDisposable: CompositeDisposable? = null

    private val gson: Gson = Gson()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        myCompositeDisposable = CompositeDisposable()
        bindViews()
        initListeners()
//        loadMap()
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
        val msg = if (code == 404) "Data not found" else "Something went wrong"
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()

        if (code == 404) {
            textCountryName.text = ("Nama Negara:")
            textCountryCapital.text = ("Ibukota:")
            textCountryPopulation.text = ("Jumlah Penduduk:")
            textCountryCallCode.text = ("Kode Telepon:")
        }
    }


    private fun setupTextViews(responses: String) {
        try {
            Log.d(TAG, "RESPONSE: $responses")
            val jsonArray = JSONArray(responses)
            val arrayLength = jsonArray.length()
            if (arrayLength > 0) {
                val lastObj = jsonArray.getJSONObject(arrayLength - 1)
                val json = lastObj.toString()
                val countryItem: CountryItem = gson.fromJson(json, CountryItem::class.java)

                val capital = countryItem.capital
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


    fun loadMap() {
        mapFragment!!.getMapAsync { googleMap -> this@MapsActivity.googleMap = googleMap }
    }

    override fun onDestroy() {
        super.onDestroy()
        myCompositeDisposable?.clear()
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
