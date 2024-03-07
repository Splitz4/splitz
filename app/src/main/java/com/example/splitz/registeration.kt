package com.example.splitz

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import com.example.splitz.databinding.ActivityRegisterationBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.auth.User

class registeration : BaseActivity() {

    private lateinit var binding: ActivityRegisterationBinding
    private lateinit var firebaseAuth : FirebaseAuth
    val db = FirebaseFirestore.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()


//        var et_password =
        var et_email = binding.etEmail
        var btn_register = binding.btnRegister
        var et_password = binding.etoPassword
        var et_repassword = binding.etRepassword
        var et_name = binding.etName

        et_email.addTextChangedListener(object:TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if(android.util.Patterns.EMAIL_ADDRESS.matcher(et_email.text.toString()).matches()){
                    btn_register.isEnabled = true
                }
                else{
                    btn_register.isEnabled = false
                    et_email.error = "Invalid Email"
                }
            }

            override fun afterTextChanged(s: Editable?) {


            }

        })

        btn_register.setOnClickListener {
            showProgressDialog(resources.getString(R.string.please_click_back_again_to_exit))
            val email = et_email.text.toString()
            val name = et_name.text.toString()
            val pass =  et_password.text.toString()
            val et_repassword = et_repassword.text.toString()

            if(name.isNotEmpty() && email.isNotEmpty() && pass.isNotEmpty() && et_repassword.isNotEmpty()){
                if (pass == et_repassword){

                    firebaseAuth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener {
                        if (it.isSuccessful){
                            firebaseAuth.currentUser?.sendEmailVerification()
                                ?.addOnSuccessListener {
                                    hideProgressDialog()
                                    showSuccessSnackBar("Please check you email for verification")

                                    val User = hashMapOf(
                                        "Name" to name,
                                        "Email" to email,
                                        "Password" to pass
                                    )

                                    db.collection("User")
                                        .add(User)
                                        .addOnSuccessListener {
                                            val intent =  Intent(this@registeration, login::class.java)
                                            startActivity(intent)
                                        }
                        }
                        }

                        else{
                               // Toast.makeText(this, it.exception.toString(),Toast.LENGTH_LONG).show()
                            showErrorSnackBar(it.exception.toString())
                        }
                    }

                }else{
                    //Toast.makeText(this,"", Toast.LENGTH_LONG).show()
                    showErrorSnackBar("Both Password is not matching")
                }
            }else{
                showErrorSnackBar("Empty fields can not be submitted")

            }
        }





    }

}