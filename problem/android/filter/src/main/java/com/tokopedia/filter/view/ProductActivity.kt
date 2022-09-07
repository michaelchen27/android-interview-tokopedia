package com.tokopedia.filter.view

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.gson.Gson
import com.tokopedia.filter.R
import com.tokopedia.filter.view.adapter.ProductAdapter
import com.tokopedia.filter.view.data.vo.Product
import com.tokopedia.filter.view.data.vo.ProductX
import java.io.InputStream

class ProductActivity : AppCompatActivity(), View.OnClickListener,
    BottomSheetFilterProductFragment.OnCityFilterListener {
    private val TAG: String = ProductActivity::class.java.simpleName

    private lateinit var rvProductList: RecyclerView
    private lateinit var productList: List<ProductX>
    private lateinit var fabFilter: FloatingActionButton
    private lateinit var bottomSheetFilterProductFragment: BottomSheetFilterProductFragment
    private lateinit var productAdapter: ProductAdapter
    private lateinit var bundleToFilterFragment: Bundle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product)

        initView()
        readJSON()
        initRV()
    }

    private fun initView() {
        rvProductList = findViewById(R.id.rv_product_list)
        fabFilter = findViewById(R.id.fab_filter)
        fabFilter.setOnClickListener(this)
        bottomSheetFilterProductFragment = BottomSheetFilterProductFragment(this)
        bundleToFilterFragment = Bundle()
    }

    private fun readJSON() {
        val json: String?

        try {
            val inputStream: InputStream =
                applicationContext!!.resources.openRawResource(R.raw.products)
            json = inputStream.bufferedReader().use { it.readText() }

            Log.d("json", "readJSON: $json")
            val gson = Gson()
            val productObj = gson.fromJson(json, Product::class.java)
            Log.d("pf", "readJSON: $productObj")
            productList = productObj.data.products

        } catch (e: Exception) {
            Log.e("e", "readJSON: $e")
        }
    }

    private fun initRV() {
        rvProductList.layoutManager =
            StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        productAdapter = ProductAdapter(productList, this)
        rvProductList.adapter = productAdapter
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.fab_filter -> {
                setupChip()
                setupSlider()
                bottomSheetFilterProductFragment.show(
                    supportFragmentManager,
                    "BottomSheetFilterProductFragment"
                )
            }
        }
    }

    private fun setupChip() {
        val mapOfLocation: Map<String, Int> =
            productList.groupingBy { it.shop.city }.eachCount().filter { it.value > 0 }
                .toList().sortedByDescending { (_, value) -> value }.toMap()

        val arrListCity: ArrayList<String> = arrayListOf()
        mapOfLocation.keys.forEach { k ->
            arrListCity.add(k)
        }

        bundleToFilterFragment.putStringArrayList("arrListCity", arrListCity)
        bottomSheetFilterProductFragment.arguments = bundleToFilterFragment
    }

    private fun setupSlider() {
        if (productList.isNotEmpty()) {
            val minPrice = productList.minBy { it.priceInt }?.priceInt
            val maxPrice = productList.maxBy { it.priceInt }?.priceInt

            bundleToFilterFragment.putInt("minPrice", minPrice!!)
            bundleToFilterFragment.putInt("maxPrice", maxPrice!!)
            bottomSheetFilterProductFragment.arguments = bundleToFilterFragment
        }
    }

    override fun onCityFilter(cityName: String) {
        val productFilterByCity = productList.filter { it.shop.city == cityName }
        if (productFilterByCity.isNotEmpty()) {
            productAdapter.updateData(productFilterByCity)
        }

    }
}