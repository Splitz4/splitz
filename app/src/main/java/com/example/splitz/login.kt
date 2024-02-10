package com.example.splitz

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast

class login : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)


        val btnlogin = findViewById<Button>(R.id.btn_login)
        val logingoogle = findViewById<Button>(R.id.login_google)

        btnlogin.setOnClickListener {
            //Toast.makeText(this,"Hellow", Toast.LENGTH_LONG).show()
            val intent = Intent(this, Home::class.java)
            startActivity(intent)
        }
        logingoogle.setOnClickListener {
            //Toast.makeText(this,"Hellow", Toast.LENGTH_LONG).show()
            val intent = Intent(this, Home::class.java)
            startActivity(intent)
        }
    }
}