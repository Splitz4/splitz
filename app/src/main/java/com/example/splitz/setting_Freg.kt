package com.example.splitz

import android.app.Activity

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Configuration
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import android.window.SplashScreen
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SwitchCompat
import androidx.cardview.widget.CardView
import androidx.core.app.ActivityCompat.recreate
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import java.util.Locale

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [setting_Freg.newInstance] factory method to
 * create an instance of this fragment.
 */
class setting_Freg : Fragment(){
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    lateinit var toggle : ActionBarDrawerToggle
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_setting__freg, container, false)
    }

    private lateinit var auth: FirebaseAuth
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadLocate()

        // Check the current theme and set the switch state accordingly
        val buttonReg = view.findViewById<TextView>(R.id.buttonReg)
        val logoutApp = view.findViewById<TextView>(R.id.logoutApp)
        val editProfile = view.findViewById<TextView>(R.id.buttonEditProfile)
        val ourTeam = view.findViewById<TextView>(R.id.ourTeam)
        val RateUs = view.findViewById<TextView>(R.id.RateUs)
        val backTraceSeting = view.findViewById<ImageView>(R.id.backTraceSeting)

        val auth = FirebaseAuth.getInstance()

        editProfile.setOnClickListener {
            showPopupProfile()
        }
        backTraceSeting.setOnClickListener {
            if (childFragmentManager.backStackEntryCount > 0) {
                childFragmentManager.popBackStack()
            } else {
                activity?.onBackPressed()
            }
        }

        RateUs.setOnClickListener {
            val dialogView =
                LayoutInflater.from(requireContext()).inflate(R.layout.rateuspop, null)
            val rateStar = dialogView.findViewById<RatingBar>(R.id.ratingStar)

            val submitRate = dialogView.findViewById<CardView>(R.id.submitRateUs)

            val alertDialogBuilder = AlertDialog.Builder(requireContext())
                .setView(dialogView)
                .setCancelable(false)

            val alertDialog = alertDialogBuilder.create()

            submitRate.setOnClickListener {
                alertDialog.dismiss()
            }


            alertDialog.show()
        }
        ourTeam.setOnClickListener {
            val secondFragment = outTeam()
            activity?.supportFragmentManager?.beginTransaction()?.apply {
                replace(R.id.frameLayout, secondFragment)
                addToBackStack(null) // Optional, adds the transaction to the back stack
                commit()
            }
        }
        logoutApp.setOnClickListener {

            val builder = AlertDialog.Builder(requireContext())
            builder.setMessage("Are you sure you want to log out?")

            builder.setCancelable(false)
            builder.setPositiveButton("OK") { dialog, _ ->
                auth.signOut()

                saveLoggedInStatus(isLoggedIn = false)
                val intent = Intent(activity, splashScreen::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                activity?.finish()
                // You can perform any action here when the user clicks OK
            }
            builder.setNegativeButton("CANCEL"){ dialog, _ ->
                dialog.dismiss()

            }
            val dialog = builder.create()
            dialog.show()


        }


        buttonReg.setOnClickListener {
                 showChangeLang()
        }
        }

    private fun showPopupProfile() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setCancelable(false)
        builder.setMessage("Edit or View your profile")
        builder.setPositiveButton("Edit") { dialog, _ ->
            dialog.dismiss()
        }
        builder.setNegativeButton("View") { dialog, _ ->
            val secondFragment = profile()
            activity?.supportFragmentManager?.beginTransaction()?.apply {
                dialog.dismiss()
                replace(R.id.frameLayout, secondFragment)
                addToBackStack(null) // Optional, adds the transaction to the back stack
                commit()
            }
        }
        val dialog = builder.create()
        dialog.show()
    }

    private fun showChangeLang(){
        val listItems = arrayOf("Gujrati","English","Hindi")

        val mBuilder = AlertDialog.Builder(requireContext())
        mBuilder.setTitle("Choose Language")
        mBuilder.setSingleChoiceItems(listItems, -1) { dialog, which ->
            if (which == 0) {
                setLocate("gu")
                recreate(requireActivity())
            }
            else if (which == 1){
                setLocate("en")
                recreate(requireActivity())
            }
            else if (which == 2){
                setLocate("hi")
                recreate(requireActivity())
            }
              dialog.dismiss()
        }
        val mDialog = mBuilder.create()

        mDialog.show()
    }
    private fun setLocate(Lang:String){
        val locale = Locale(Lang)
        Locale.setDefault(locale)
        val config = Configuration(resources.configuration)
        config.locale = locale
        resources.updateConfiguration(config, resources.displayMetrics)

        val editor = requireActivity().getSharedPreferences("Settings",Context.MODE_PRIVATE).edit()
        editor.putString("My_Lang", Lang)
        editor.apply()


    }
    private fun loadLocate() {
        val sharedPreferences = requireActivity().getSharedPreferences("Settings",Activity.MODE_PRIVATE)
        val language = sharedPreferences.getString("My_Lang", "")?:""
        setLocate(language)
    }

    private fun saveLoggedInStatus(isLoggedIn: Boolean) {
        // Retrieve the shared preferences
        val prefs = requireActivity().getSharedPreferences(login.PREFS_NAME, Context.MODE_PRIVATE)


        // Get the shared preferences editor
        val editor = prefs.edit()

        // Store the value of isLoggedIn in shared preferences
        editor.putBoolean(login.KEY_IS_LOGGED_IN, isLoggedIn)

        // Apply the changes
        editor.apply()
    }




    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment setting_Freg.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            setting_Freg().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    }
