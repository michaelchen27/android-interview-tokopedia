package com.tokopedia.filter.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.button.MaterialButton
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.tokopedia.filter.R

class BottomSheetFilterProductFragment(cityFilterListener: OnCityFilterListener) :
    BottomSheetDialogFragment(),
    View.OnClickListener {

    private val TAG: String = BottomSheetFilterProductFragment::class.java.simpleName

    private lateinit var cgLocation: ChipGroup
    private lateinit var arrListCity: ArrayList<String>
    private lateinit var btnFilter: MaterialButton
    private var onCityFilterListener: OnCityFilterListener

    private var minPrice: Int = 0
    private var maxPrice: Int = 0

    init {
        this.onCityFilterListener = cityFilterListener
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        arrListCity = arguments?.getStringArrayList("arrListCity")!!
        minPrice = arguments?.getInt("minPrice")!!
        maxPrice = arguments?.getInt("maxPrice")!!
        Log.d(TAG, "onCreateView: MinPrice $minPrice MaxPrice: $maxPrice")
        return inflater.inflate(R.layout.bottomsheet_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(view)
    }

    private fun initView(view: View) {
        btnFilter = view.findViewById(R.id.btn_filter)
        btnFilter.setOnClickListener(this)

        cgLocation = view.findViewById(R.id.cg_location)
        for (i in arrListCity.indices) {
            if (i < 3) {
                addChip(i, arrListCity[i])
            }
        }
        cgLocation.isSingleSelection = true
    }

    private fun addChip(id: Int, label: String) {
        val chip: Chip = layoutInflater.inflate(R.layout.filter_chip, cgLocation, false) as Chip
        chip.id = id
        chip.text = label
        cgLocation.addView(chip)
    }

    private fun getSelectedCityChip(): String {
        var cityName = ""
        val ids: List<Int> = cgLocation.checkedChipIds
        for (id in ids) {
            val chip: Chip = cgLocation.findViewById(id)
            cityName = chip.text.toString()
            Log.d(TAG, "getSelectedCityChip: $chip with $id (${chip.text}) is checked")
        }
        return cityName
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_filter -> {
                val cityName = getSelectedCityChip()
                onCityFilterListener.onCityFilter(cityName)
                this.dismiss()
            }
        }
    }

    interface OnCityFilterListener {
        fun onCityFilter(cityName: String);
    }

}