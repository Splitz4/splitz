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

class start_act : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)

        val loginButtonCard : CardView = findViewById(R.id.loginButtonCard)
        val registerButtonCard : CardView = findViewById(R.id.registerButtonCard)

        if (!isConnected()) {
            buildDialog().show()
        }
        if (isLoggedIn()) {

            val intent = Intent(this, Home::class.java)
            startActivity(intent)
            finish()
        }

            loginButtonCard.setOnClickListener {
                val intent = Intent(this, login::class.java)
                startActivity(intent)
                finish()
            }
        registerButtonCard.setOnClickListener {
            val intent = Intent(this, registeration::class.java)
            startActivity(intent)
            finish()
        }
        }

    private fun isLoggedIn(): Boolean {
        // Retrieve the shared preferences
        val prefs = getSharedPreferences(login.PREFS_NAME, Context.MODE_PRIVATE)

        // Retrieve the value of isLoggedIn from shared preferences
        return prefs.getBoolean(login.KEY_IS_LOGGED_IN, false)
    }
    private fun isConnected(): Boolean {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = connectivityManager.activeNetworkInfo
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting
    }
    private fun buildDialog(): AlertDialog {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("No Internet Connection")
            .setMessage("Dear user, you are not connected to the internet.")
            .setCancelable(false)
            .setPositiveButton("OK") { dialog, which ->
                finish()
            }
        return builder.create()
    }
}