package com.tokopedia.oilreservoir

import kotlin.math.max

/**
 * Created by fwidjaja on 2019-09-24.
 */
object Solution {
    fun collectOil(height: IntArray): Int {
        // TODO, return the amount of oil blocks that could be collected
        // below is stub

        // Two Pointers approach
        if (height.isEmpty()) return 0

        var l = 0
        var r = height.size - 1

        var lMax = height[l]
        var rMax = height[r]
        var res = 0

        while (l < r) {
            if (lMax < rMax) {
                // Shift left pointer
                l++
                lMax = max(lMax, height[l])
                res += lMax - height[l]
            } else {
                // Shift right pointer
                r--
                rMax = max(rMax, height[r])
                res += rMax - height[r]
            }
        }
        return res
    }
}
