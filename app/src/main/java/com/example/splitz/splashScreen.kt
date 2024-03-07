package com.example.splitz

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.RelativeLayout

class splashScreen : AppCompatActivity() {
    //private lateinit var fadeInAnimation: Animation
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

//        val backImage : ImageView = findViewById(R.id.splashLayout)
//        val sideAnim = AnimationUtils.loadAnimation(this,R.anim.splashscreen_anim)
//        backImage.startAnimation(sideAnim)

       Handler().postDelayed({
             startActivity(Intent(this, start_act::class.java))
             finish()
       }, 3000)
    }
}