package com.example.zd5_up_versia3


import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
@SuppressLint("CustomSplashScreen")
class MainActivity : AppCompatActivity() {
    private val splashDelay: Long = 2000
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Handler().postDelayed({
            val intent = Intent(this@MainActivity, VhodActivity::class.java)
            startActivity(intent)
            finish()
        }, splashDelay)
    }
}