package com.example.splitz

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.splitz.databinding.ActivityHomeBinding
import com.example.splitz.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationItemView
import com.google.android.material.bottomnavigation.BottomNavigationView

class Home : AppCompatActivity() {
    private lateinit var binding : ActivityHomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        replaceFragments(home_Freg())

        binding.bottomNavBar.setOnItemSelectedListener {
            when(it.itemId){
                R.id.home-> replaceFragments(home_Freg())
                R.id.group-> replaceFragments(freg_Group())
                R.id.friends-> replaceFragments(addFriends())
                R.id.setting-> replaceFragments(setting_Freg())

                else->{

                }

            }
            true
        }




    }
    private fun replaceFragments(fragment : Fragment){
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frameLayout, fragment)
        fragmentTransaction.commit()
    }
}