package com.tokopedia.climbingstairs

object Solution {
    fun climbStairs(n: Int): Long {
        // TODO, return in how many distinct ways can you climb to the top. Each time you can either climb 1 or 2 steps.
        // 1 <= n < 90

        var i = 1
        var j = 1

        var res = 0
        for (a in 1 until n) {
            res = i + j
            j = i
            i = res
        }
        
        return i.toLong()
    }
}
