package com.tokopedia.minimumpathsum

import android.util.Log

object Solution {
    fun minimumPathSum(matrix: Array<IntArray>): Int {
        // TODO, find a path from top left to bottom right which minimizes the sum of all numbers along its path, and return the sum
        // below is stub

        // Initialize Row and Col size
        val rows = matrix.size
        val cols = matrix[0].size

        // Return 0 if empty
        if (rows == 0) {
            return 0
        }

        var table: Array<IntArray> = Array(rows) { IntArray(cols) }
        table[0][0] = matrix[0][0]

        // Calculate 1st row sum
        for (j in 1 until cols) {
            table[0][j] = table[0][j - 1] + matrix[0][j]
        }

        // Calculate 1st col sum
        for (i in 1 until rows) {
            table[i][0] = table[i - 1][0] + matrix[i][0]
        }

        // Approachable from two directions.
        for (i in 1 until rows) {
            for (j in 1 until cols) {
                table[i][j] = matrix[i][j] + minOf(table[i - 1][j], table[i][j - 1])
            }
        }

        return table[rows - 1][cols - 1]
    }

}
