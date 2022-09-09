package com.tokopedia.filter.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.button.MaterialButton
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.slider.LabelFormatter
import com.google.android.material.slider.RangeSlider
import com.google.android.material.textfield.TextInputEditText
import com.tokopedia.filter.R
import java.text.NumberFormat
import java.util.*

class BottomSheetFilterProductFragment(cityFilterListener: OnCityFilterListener) :
    BottomSheetDialogFragment(),
    View.OnClickListener, RangeSlider.OnChangeListener {

    private val TAG: String = BottomSheetFilterProductFragment::class.java.simpleName

    private lateinit var cgLocation: ChipGroup
    private lateinit var arrListCity: ArrayList<String>
    private lateinit var btnFilter: MaterialButton
    private lateinit var rsPrice: RangeSlider
    private lateinit var etMinPrice: TextInputEditText
    private lateinit var etMaxPrice: TextInputEditText
    private var onCityFilterListener: OnCityFilterListener

    private var minPrice: Int = 0
    private var maxPrice: Int = 0
    private var minSelectedPrice: Int = 0
    private var maxSelectedPrice: Int = 0


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
        minSelectedPrice = arguments?.getInt("minPrice")!!
        maxSelectedPrice = arguments?.getInt("maxPrice")!!

        return inflater.inflate(R.layout.bottomsheet_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(view)
    }

    private fun initView(view: View) {
        etMinPrice = view.findViewById(R.id.et_min_price)
        etMinPrice.setText(Util.formatRupiah(minPrice))
        etMinPrice.isEnabled = false

        etMaxPrice = view.findViewById(R.id.et_max_price)
        etMaxPrice.setText(Util.formatRupiah(maxPrice))
        etMaxPrice.isEnabled = false

        btnFilter = view.findViewById(R.id.btn_filter)
        btnFilter.setOnClickListener(this)

        cgLocation = view.findViewById(R.id.cg_location)
        for (i in arrListCity.indices) {
            if (i < 3) {
                addChip(i, arrListCity[i])
            }
        }
        cgLocation.isSingleSelection = true

        rsPrice = view.findViewById(R.id.rs_price)
        addSlider()
    }

    private fun addSlider() {
        rsPrice.labelBehavior = LabelFormatter.LABEL_GONE
        rsPrice.valueFrom = minPrice.toFloat()
        rsPrice.valueTo = maxPrice.toFloat()
        rsPrice.setValues(minSelectedPrice.toFloat(), maxSelectedPrice.toFloat())
        rsPrice.stepSize = resources.getInteger(R.integer.sliderStepSize).toFloat()
        rsPrice.setLabelFormatter {
            val localeID = Locale("in", "ID")
            val numberFormat: NumberFormat = NumberFormat.getCurrencyInstance(localeID)
            numberFormat.minimumFractionDigits = 0
            numberFormat.format(it)
        }
        rsPrice.addOnChangeListener(this)
    }


    private fun addChip(id: Int, label: String) {
        val chip: Chip = layoutInflater.inflate(R.layout.filter_chip, cgLocation, false) as Chip
        chip.id = id
        chip.tag = "chip_location_$id"
        chip.text = label
        cgLocation.addView(chip)
    }

    private fun getSelectedCityChip(): String {
        var cityName = ""
        val ids: List<Int> = cgLocation.checkedChipIds
        if (ids.isNotEmpty()) {
            for (id in ids) {
                val chip: Chip = cgLocation.findViewById(id)
                cityName = chip.text.toString()
                Log.d(TAG, "getSelectedCityChip: $chip with $id (${chip.text}) is checked")
            }
            return cityName
        } else {
            return ""
        }
    }


    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_filter -> {
                val cityName = getSelectedCityChip()
                onCityFilterListener.onCityFilter(cityName, minSelectedPrice, maxSelectedPrice)
                this.dismiss()
            }
        }
    }

    interface OnCityFilterListener {
        fun onCityFilter(cityName: String, minSelectedPrice: Int, maxSelectedPrice: Int);
    }

    @SuppressLint("RestrictedApi")
    override fun onValueChange(slider: RangeSlider, value: Float, fromUser: Boolean) {
        minSelectedPrice = slider.values[0].toInt()
        maxSelectedPrice = slider.values[1].toInt()

        etMinPrice.setText(Util.formatRupiah(minSelectedPrice))
        etMaxPrice.setText(Util.formatRupiah(maxSelectedPrice))
    }


}