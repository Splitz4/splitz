package com.example.splitz

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment.findNavController


class create_Group : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_group)

        val cancelButton = findViewById<TextView>(R.id.cancel_button_Profile)

        cancelButton.setOnClickListener {
            cancelButtonWork()
        }
    }
    fun cancelButtonWork() {
        val navController = findNavController(R.id.bottomNavBar)
        navController.navigate(R.id.group)


    }

}