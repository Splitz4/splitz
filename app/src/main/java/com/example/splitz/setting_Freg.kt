package com.example.splitz

import android.app.Activity
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.content.res.Configuration
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SwitchCompat
import androidx.core.app.ActivityCompat.recreate
import com.google.android.material.navigation.NavigationView
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadLocate()

        // Check the current theme and set the switch state accordingly
        val buttonReg = view.findViewById<TextView>(R.id.buttonReg)
        val darkModeSwitch = view.findViewById<SwitchCompat>(R.id.darkModeSwitch)
            darkModeSwitch.isChecked = AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES


        darkModeSwitch.setOnCheckedChangeListener { _, isChecked ->
            // Apply the selected theme based on the switch state
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }
        buttonReg.setOnClickListener {
                 showChangeLang()
        }
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
