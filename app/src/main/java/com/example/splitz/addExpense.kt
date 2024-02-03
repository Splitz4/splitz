package com.example.splitz

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.android.material.floatingactionbutton.FloatingActionButton

class addExpense : AppCompatActivity() {

    private var clicked = false
    private val rotateOpen: Animation by lazy {AnimationUtils.loadAnimation(this, R.anim.rotate_open)  }
    private val rotateClose: Animation by lazy {AnimationUtils.loadAnimation(this, R.anim.rotate_close)  }
    private val toRotate: Animation by lazy {AnimationUtils.loadAnimation(this, R.anim.to_bottom_anim)  }
    private val fromRotate: Animation by lazy {AnimationUtils.loadAnimation(this, R.anim.from_bottom_anim)  }
    private lateinit var fab : FloatingActionButton
    private lateinit var add_btn1 : FloatingActionButton
    private lateinit var add_btn2 : FloatingActionButton
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_expense)

        fab  = findViewById(R.id.add_btn)
        add_btn1 = findViewById(R.id.add_btn1)
        add_btn2 = findViewById(R.id.add_btn2)

        fab.setOnClickListener{
            onFabButtonClicked()
        }
        add_btn1.setOnClickListener{
            showPopupFormExp(this)
        }
        add_btn2.setOnClickListener{
               showPopupFormIncome(this)
        }

    }

    private fun onFabButtonClicked(){
        setVisibility(clicked)
        setAnimation(clicked)
        setClickable(clicked)
        clicked = !clicked

    }
    private fun setVisibility(clicked:Boolean){
        if(!clicked){
            add_btn1.visibility = View.VISIBLE
            add_btn2.visibility = View.VISIBLE
        }else{
            add_btn1.visibility = View.INVISIBLE
            add_btn2.visibility = View.INVISIBLE
        }
    }
    private fun setAnimation(clicked:Boolean) {

        if(!clicked){
            add_btn1.startAnimation(fromRotate)
            add_btn2.startAnimation(fromRotate)
            fab.startAnimation(rotateOpen)
        }else{
            add_btn1.startAnimation(toRotate)
            add_btn2.startAnimation(toRotate)
            fab.startAnimation(rotateClose)
        }
    }
    private fun setClickable(clicked: Boolean){
        if(!clicked){
            add_btn1.isClickable = true
            add_btn2.isClickable = true
        }else{
            add_btn1.isClickable = false
            add_btn2.isClickable = false
        }
    }
    private fun showPopupFormExp(context: Context) {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.create_expense, null)

        val alertDialogBuilder = AlertDialog.Builder(context)
            .setView(dialogView)
            .setTitle("Expense Form")

        val alertDialog = alertDialogBuilder.create()

       //  Handle form elements and buttons
        val submitButton = dialogView.findViewById<Button>(R.id.saveExp)
        submitButton.setOnClickListener {

          alertDialog.dismiss()
        }


        alertDialog.show()
    }
    private fun showPopupFormIncome(context: Context) {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.create_income, null)

        val alertDialogBuilder = AlertDialog.Builder(context)
            .setView(dialogView)
            .setTitle("Income Form")

        val alertDialog = alertDialogBuilder.create()

        // Handle form elements and buttons
        //val submitButton = dialogView.findViewById<Button>(R.id.submitButton)
        //submitButton.setOnClickListener {
        // Handle form submission
        // ...

        //  alertDialog.dismiss()
        //}

        // Show the dialog
        alertDialog.show()
    }
}