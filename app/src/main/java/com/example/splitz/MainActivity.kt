package com.example.splitz

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {

    private lateinit var btn1 : Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn1 = findViewById(R.id.button)

        btn1.setOnClickListener {
            val intent = Intent(this, Home::class.java)
            startActivity(intent)
        }
    }
}