package com.tokopedia.climbingstairs

import android.os.Bundle
import android.util.Log
import android.webkit.WebView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.tokopedia.core.loadFile

class ClimbingStairsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_problem)
        val webView = findViewById<WebView>(R.id.webView)
        webView.loadFile("climbing_stairs.html")

        // example of how to call the function
        val number = Solution.climbStairs(10)
        Log.d("ANS2", "ANSWER2: $number")
        Toast.makeText(this, "Answer2: $number", Toast.LENGTH_SHORT).show()

    }
}