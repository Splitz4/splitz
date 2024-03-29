package com.example.splitz

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.cardview.widget.CardView
import androidx.constraintlayout.motion.widget.TransitionBuilder.validate
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.auth.User
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class login : BaseActivity() {


    companion object {
        const val PREFS_NAME = "MyPrefsFile"
        const val KEY_IS_LOGGED_IN = "isLoggedIn"
        const val KEY_USERNAME = "username"
    }

    private var email: EditText? = null
    private var pass: EditText? = null
    private val db = Firebase.firestore
    private lateinit var auth: FirebaseAuth
    val dab = FirebaseFirestore.getInstance()
    private lateinit var googleSignInClient : GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        auth = FirebaseAuth.getInstance()
        email = findViewById(R.id.etl_email)
        pass = findViewById(R.id.etl_password)
        var registerBtn = findViewById<TextView>(R.id.loginRegister)
        var forgotBtn = findViewById<TextView>(R.id.forgotPassword)

        registerBtn.setOnClickListener {
            val intent = Intent(this@login, registeration::class.java)
            startActivity(intent)
        }
        forgotBtn.setOnClickListener {
            forgotPasswordPopUp()
        }

        val btnlogin = findViewById<CardView>(R.id.btn_login)
        val logingoogle = findViewById<CardView>(R.id.login_google)

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
            googleSignInClient = GoogleSignIn.getClient(this, gso)



            btnlogin.setOnClickListener {
                signIn()
            }

        if (!isConnected()) {
            buildDialog().show()
        }

        logingoogle.setOnClickListener {
            signInGoogle()
        }


    }

    private fun forgotPasswordPopUp() {
        val dialogView =
            LayoutInflater.from(this).inflate(R.layout.forgot_password, null)
        var forgotPasswordEmail = dialogView.findViewById<EditText>(R.id.forgotPasswordEmailEdit)

        val alertDialogBuilder = AlertDialog.Builder(this)
            .setView(dialogView)
            .setTitle("Reset Password")
            .setCancelable(false)

        val alertDialog = alertDialogBuilder.create()


        val submitButton = dialogView.findViewById<CardView>(R.id.btn_forgotPassword)
        val cancelButton = dialogView.findViewById<CardView>(R.id.btn_Cancel)

        submitButton.setOnClickListener {
            showProgressDialog(resources.getString(R.string.please_click_back_again_to_exit))
            var emailAddress = forgotPasswordEmail.text.toString()
            auth.sendPasswordResetEmail(emailAddress)
                .addOnSuccessListener {

                   // Toast.makeText(this, "Please check you email", Toast.LENGTH_LONG).show()
                    hideProgressDialog()
                    alertDialog.dismiss()
                    showErrorSnackBar("Please check you email")


                }.addOnFailureListener{
                    hideProgressDialog()
                    alertDialog.dismiss()
                    showErrorSnackBar(it.toString())
                }

        }
        cancelButton.setOnClickListener {
            alertDialog.dismiss()
        }

        // Show the dialog
        alertDialog.show()
    }



    private fun signIn() {
        val etEmail: String = email?.text.toString().trim { it <= ' ' }
        val etPass: String = pass?.text.toString().trim { it <= ' ' }



        if (validate(etEmail, etPass)) {
            showProgressDialog(resources.getString(R.string.please_click_back_again_to_exit))
            //Toast.makeText(this, "Working01", Toast.LENGTH_LONG).show()
            //showProgressDialog(resources.getString(R.string.please_click_back_again_to_exit))
            auth.signInWithEmailAndPassword(etEmail, etPass)

                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {

                        //Toast.makeText(this, "Working01", Toast.LENGTH_LONG).show()
                        // Sign in success, update UI with the signed-in user's information
                        Log.d("Sign in", "signInWithEmail:success")
                        val user = auth.currentUser
                        if (!user!!.isEmailVerified) {
                            hideProgressDialog()

                            showErrorSnackBar("Please verify your account first")


                        } else {

                            //Toast.makeText(this,"Working1", Toast.LENGTH_LONG).show()
                            db.collection("User").get()
                                .addOnSuccessListener { collection ->
                                    val documents = collection.documents

                                    //Toast.makeText(this, "$documents", Toast.LENGTH_LONG).show()
                                    for (document in documents) {
                                        if (document.get("Email") == etEmail) {
                                            saveLoggedInStatus(true)
                                            hideProgressDialog()
                                            val username = document.getString("Name")
                                            if (username != null) {
                                                saveUsername(username)
                                            }
                                            val intent = Intent(this, Home::class.java)
                                            //val Name = intent.putExtra("username", "$username")

                                            startActivity(intent)

                                            finish()
                                        }
                                    }
                                }.addOnFailureListener { exception ->
                                    // Handle any errors
                                    showErrorSnackBar("$exception")
                                }
                        }

                        // updateUI(user)
                        // readData()
                    } else {
                        hideProgressDialog()
                        showErrorSnackBar("Please enter the details properly or check Internet connection")
                        // If sign in fails, display a message to the user.
//                        hideProgressDialog()
//                        Log.w("Sign in", "signInWithEmail:failure", task.exception)
//                        showErrorSnackBar("Please enter the details properly or check Internet connection")
                        // updateUI(null)
                    }
                }
        }
    }

    private fun signInGoogle(){
        val signInIntent = googleSignInClient.signInIntent
        launcher.launch(signInIntent)
    }
    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->

        if(result.resultCode == Activity.RESULT_OK){
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            handleResults(task)
        }
    }

    private fun handleResults(task: Task<GoogleSignInAccount>) {
        if(task.isSuccessful){

            val account : GoogleSignInAccount?= task.result
            val user = account?.displayName
            val username = user.toString()
            if(account!=null){
                saveUsername(username)
                updateUI(account)
            }
        }else{

            showErrorSnackBar(task.exception.toString())
        }
    }

    private fun updateUI(account: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(account.idToken,null)
        auth.signInWithCredential(credential).addOnCompleteListener{
            if(it.isSuccessful){
                saveLoggedInStatus(true)
                      val intent : Intent = Intent(this, Home::class.java)
                startActivity(intent)
                finish()
            }else{
                showErrorSnackBar(it.exception.toString())
            }
        }

    }

    private fun saveUsername(username: String) {
        // Retrieve the shared preferences
        val prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

        // Get the shared preferences editor
        val editor = prefs.edit()

        // Store the username in shared preferences
        editor.putString(KEY_USERNAME, username)

        // Apply the changes
        editor.apply()
    }
    private fun saveLoggedInStatus(isLoggedIn: Boolean) {
        // Retrieve the shared preferences
        val prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)


        // Get the shared preferences editor
        val editor = prefs.edit()

        // Store the value of isLoggedIn in shared preferences
        editor.putBoolean(KEY_IS_LOGGED_IN, isLoggedIn)

        // Apply the changes
        editor.apply()
    }
    private fun validate(etEmail: String, etPass: String): Boolean {
        return when {
            TextUtils.isEmpty(etEmail) -> {
                showErrorSnackBar("Please enter proper username")
                //showErrorSnackBar("Please enter proper username")
                false
            }
            TextUtils.isEmpty(etPass) -> {
                showErrorSnackBar("Please enter proper password")
                //showErrorSnackBar("Please enter proper password")
                false
            }
            else -> {
                true
            }
        }

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