package com.example.splitz

import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.Manifest
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.splitz.databinding.ActivityHomeBinding
import com.google.android.material.bottomnavigation.BottomNavigationItemView
import com.google.android.material.bottomnavigation.BottomNavigationView
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions

class Home : AppCompatActivity() {
    private lateinit var binding : ActivityHomeBinding
    private val CONTACTS_PERMISSION_REQUEST_CODE = 101
    private val NOTIFICATION_PERMISSION_REQUEST_CODE = 102
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        replaceFragment(home_Freg())

        binding.bottomNavBar.setOnItemSelectedListener { id ->
            if (!isFinishing && !isDestroyed) { // Check if activity is not finishing or destroyed
                when (id) {
                    R.id.home -> replaceFragment(home_Freg())
                    R.id.group -> replaceFragment(freg_Group())
                    R.id.friends -> replaceFragment(addFriends())
                    R.id.setting -> replaceFragment(setting_Freg())
                    else -> {
                        // Do nothing
                    }
                }
            }
            true
        }

        if (checkContactsPermission()) {
            if (checkNotifyPermission()) {

            } else {
                // Permission not granted, request it
                requestNotifyPermission()
            }

        } else {
            // Permission not granted, request it
            requestContactsPermission()

        }


    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CONTACTS_PERMISSION_REQUEST_CODE ) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Contacts permission granted
                // Proceed to the home screen
            } else {
                // Contacts permission denied
                // Handle accordingly (e.g., show a message or disable functionality)
                Toast.makeText(
                    this,
                    "Contacts permission denied",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }else {
            // Handle other permissions if needed
        }
        if (requestCode == NOTIFICATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Notification permission granted
                // Proceed with notification-related functionality
            } else {
                // Notification permission denied
                // Handle accordingly (e.g., show a message or disable functionality)
                Toast.makeText(
                    this,
                    "Notification permission denied",
                    Toast.LENGTH_SHORT
                ).show()
            }
        } else {
            // Handle other permissions if needed
        }
    }

    private fun checkContactsPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.READ_CONTACTS
        ) == PackageManager.PERMISSION_GRANTED
    }
    private fun requestContactsPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.READ_CONTACTS),
            CONTACTS_PERMISSION_REQUEST_CODE

        )
    }
    private fun checkNotifyPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.POST_NOTIFICATIONS
        ) == PackageManager.PERMISSION_GRANTED
    }
    private fun requestNotifyPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.POST_NOTIFICATIONS),
            NOTIFICATION_PERMISSION_REQUEST_CODE

        )
    }


    private fun replaceFragment(fragment : Fragment){
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frameLayout,fragment)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }
    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 0) {
            supportFragmentManager.popBackStack()
        } else {
            super.onBackPressed()
        }
    }


}
