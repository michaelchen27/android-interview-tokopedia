package com.tokopedia.filter.view

import java.text.NumberFormat
import java.util.*

class Util {
    companion object {
        fun formatRupiah(num: Int): String? {
            val localeID: Locale = Locale("in", "ID")
            val numberFormat: NumberFormat = NumberFormat.getCurrencyInstance(localeID)
            numberFormat.minimumFractionDigits = 0
            return numberFormat.format(num)
        }

    }
}