package com.tokopedia.oilreservoir

import android.os.Bundle
import android.util.Log
import android.webkit.WebView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.tokopedia.core.loadFile

/**
 * Created by fwidjaja on 2019-09-24.
 */
class OilReservoirActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_problem)

        val webView = findViewById<WebView>(R.id.webView)
        webView.loadFile("oil_reservoir.html")

        // example of how to call the function
        var number = Solution.collectOil(intArrayOf(0, 1, 0, 2, 1, 0, 1, 3, 2, 1, 2, 1))
        Log.d("ANS3", "ANSWER3: $number")
        Toast.makeText(this, "Answer3: $number", Toast.LENGTH_SHORT).show()

    }

}