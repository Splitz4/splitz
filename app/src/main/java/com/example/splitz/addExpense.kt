package com.example.splitz

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
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
            Toast.makeText(this,"This is Working2", Toast.LENGTH_LONG).show()
        }
        add_btn2.setOnClickListener{
               Toast.makeText(this,"This is Working2", Toast.LENGTH_LONG).show()
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

}