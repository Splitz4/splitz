package com.example.splitz

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.cardview.widget.CardView

class start_act : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)
        val loginButtonCard : CardView = findViewById(R.id.loginButtonCard)
        val registerButtonCard : CardView = findViewById(R.id.registerButtonCard)

        showProgressDialog(resources.getString(R.string.please_click_back_again_to_exit))


        if (isLoggedIn()) {
            hideProgressDialog()
            val intent = Intent(this, Home::class.java)
            startActivity(intent)
        }
        else{
            hideProgressDialog()
        }

            loginButtonCard.setOnClickListener {
                val intent = Intent(this, login::class.java)
                startActivity(intent)
            }
        registerButtonCard.setOnClickListener {
            val intent = Intent(this, registeration::class.java)
            startActivity(intent)
        }
        }

    private fun isLoggedIn(): Boolean {
        // Retrieve the shared preferences
        val prefs = getSharedPreferences(login.PREFS_NAME, Context.MODE_PRIVATE)

        // Retrieve the value of isLoggedIn from shared preferences
        return prefs.getBoolean(login.KEY_IS_LOGGED_IN, false)
    }

}