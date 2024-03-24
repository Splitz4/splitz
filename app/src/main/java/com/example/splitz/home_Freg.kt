package com.example.splitz

import android.Manifest
import android.app.Dialog
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.ContentValues.TAG
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View

import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.cardview.widget.CardView
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.example.splitz.login.Companion.KEY_USERNAME
import com.example.splitz.login.Companion.PREFS_NAME


import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
private lateinit var mProgressDialog: Dialog


interface progresBar {
    fun showProgressDialog(context: Context, text: String)
    fun hideProgressDialog()

}


class home_Freg : Fragment(), progresBar {
    // TODO: Rename and change types of parameters
    private var mProgressDialog: Dialog? = null
    private var param1: String? = null
    private var param2: String? = null
    val db = FirebaseFirestore.getInstance()
    private lateinit var emptyText : LottieAnimationView

    val CHANNEL_ID : String = "channelId"

    private lateinit var userRecyclerView: RecyclerView
    private lateinit var transactionList: ArrayList<transactionsData>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home__freg, container, false)
        val addExpbtn = view.findViewById<CardView>(R.id.cardSendIcon)
        val incomeBtn = view.findViewById<CardView>(R.id.cardReqIcon)
        val splitzBtn = view.findViewById<CardView>(R.id.splitzIcon)
        val totalBalance = view.findViewById<TextView>(R.id.addExpText)
        val seeAllText = view.findViewById<TextView>(R.id.seeAllText)
        emptyText = view.findViewById(R.id.noExpText)

        userRecyclerView = view.findViewById(R.id.recycler_viewTransactions)
        //userRecyclerView.LayoutManager = LinearLayoutManager(this)
        userRecyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        userRecyclerView.setHasFixedSize(true)

        transactionList = arrayListOf<transactionsData>()
        getTransactionData()

            seeAllText.setOnClickListener {
                val secondFragment = transactionList()
                activity?.supportFragmentManager?.beginTransaction()?.apply {
                    //alertDialog.dismiss()
                    replace(R.id.frameLayout, secondFragment)
                    addToBackStack(null) // Optional, adds the transaction to the back stack
                    commit()
                }
            }


        val savedUsername = getUsername()
        if (savedUsername != null) {


            // Query transactions where the name is "Chirag Kumar"
            db.collection("Transaction").whereEqualTo("Name", savedUsername)
                .get()
                .addOnSuccessListener { collection ->
                    val documents = collection.documents
                    var totalIncome = 0.0
                    var totalExpenses = 0.0

                    for (document in documents) {
                        val incomeAmount =
                            document.getString("IncomeAmount")?.toDoubleOrNull() ?: 0.0
                        val expenseAmount = document.getDouble("ExpAmount") ?: 0.0

                        totalIncome += incomeAmount
                        totalExpenses += expenseAmount
                    }

                    // Subtract total expenses from total income
                    val result = totalIncome - totalExpenses
                    totalBalance.text = result.toString()
                }
                .addOnFailureListener { exception ->
                    Log.w(TAG, "Error getting documents: ", exception)
                }
        }
        addExpbtn.setOnClickListener {
            showPopupForm()
        }
        incomeBtn.setOnClickListener {
            showPopupForm1()
        }
        splitzBtn.setOnClickListener {
//            val intent = Intent(activity, freg_Group::class.java)
//            startActivity(intent)
            //val secondFragment = addFriends() // Create an instance of SecondFragment
            //onBackPressed()
            val secondFragment = addFriends()
            activity?.supportFragmentManager?.beginTransaction()?.apply {
                replace(R.id.frameLayout, secondFragment)
                addToBackStack(null) // Optional, adds the transaction to the back stack
                commit()
            }
        }
        return view
    }

    private fun getTransactionData() {
        showProgressDialog(
            requireContext(),
            resources.getString(R.string.please_click_back_again_to_exit)
        )
        val savedUsername = getUsername()
        db.collection("Transaction").get()
            .addOnSuccessListener { collection ->
                hideProgressDialog()
                val documents = collection.documents
                for (document in documents) {
                    if (document.get("Name") == savedUsername) {
                        val Transactions = document.toObject(transactionsData::class.java)
                        //transactionList.add(Transactions!!)
                        Transactions?.let {
                            transactionList.add(it)
                        }
                    }

                }
                if (transactionList != emptyList<transactionsData>()) {

                    userRecyclerView.adapter = transactionAdapter(transactionList)

                }
                else{
                    emptyText.visibility = View.VISIBLE
                }
            }
    }

    override fun showProgressDialog(context: Context, text: String) {
        mProgressDialog = Dialog(context)
        mProgressDialog?.setContentView(R.layout.dialog_progress)
        // Set progress text or any other configurations
        // mProgressDialog.tv_progress_text.text = text
        mProgressDialog?.show()
    }

    override fun hideProgressDialog() {
        mProgressDialog?.dismiss()
    }

    private fun showPopupForm() {
        val dialogView =
            LayoutInflater.from(requireContext()).inflate(R.layout.create_expense, null)
        var expDesc = dialogView.findViewById<EditText>(R.id.expenseDescriptionEdit)
        var expAmou = dialogView.findViewById<EditText>(R.id.expAmountEdit)

        val alertDialogBuilder = AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .setTitle("Expense Form")

        val alertDialog = alertDialogBuilder.create()


        val submitButton = dialogView.findViewById<Button>(R.id.saveExp)
        val cancelButton = dialogView.findViewById<Button>(R.id.cancelExp)

        submitButton.setOnClickListener {
            var expDescVal = expDesc.text.toString()
            var expAmou = expAmou.text.toString().toDouble()
            val savedUsername = getUsername()
            val currentTime = Calendar.getInstance().time
            val dateFormat = SimpleDateFormat("dd-MM-yy HH:mma", Locale.getDefault())
            val timestamp = dateFormat.format(currentTime)
            val parsedDateTime = dateFormat.parse(timestamp)
            val date = SimpleDateFormat("dd-MM-yy", Locale.getDefault()).format(parsedDateTime)
            val time = SimpleDateFormat("HH:mma", Locale.getDefault()).format(parsedDateTime)

           // Toast.makeText(requireContext(), time, Toast.LENGTH_LONG).show()
            if (savedUsername != null) {
                val Transaction = hashMapOf(
                    "Description" to expDescVal,
                    "ExpAmount" to expAmou,
                    "Name" to savedUsername,
                    "IncomeAmount" to "0",
                    "Date" to date,
                    "Time" to time
                )
                db.collection("Transaction")
                    .add(Transaction)
                    .addOnSuccessListener {
                        createNotificationChannel()
                        sendExpNotify()
                        getTransactionData()
                        val secondFragment = freg_Group()
                        activity?.supportFragmentManager?.beginTransaction()?.apply {
                            alertDialog.dismiss()
                            replace(R.id.frameLayout, secondFragment)
                            addToBackStack(null) // Optional, adds the transaction to the back stack
                            commit()
                        }
                    }
            } else {
                activity?.recreate()
            }


//
//            alertDialog.dismiss()
        }
        cancelButton.setOnClickListener {
            alertDialog.dismiss()
        }

        // Show the dialog
        alertDialog.show()
    }

    private fun showPopupForm1() {
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.create_income, null)
        var incomeDesc = dialogView.findViewById<EditText>(R.id.incomeDescEdit)
        var incomeAmou = dialogView.findViewById<EditText>(R.id.incomeAmoutEdit)

        val alertDialogBuilder = AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .setTitle("Income Form")

        val alertDialog = alertDialogBuilder.create()


        val submitButton = dialogView.findViewById<Button>(R.id.saveIncome)
        val cancelButton = dialogView.findViewById<Button>(R.id.cancelIncome)
        submitButton.setOnClickListener {
            var expDescVal = incomeDesc.text.toString()
            var expAmou = incomeAmou.text.toString()
            val savedUsername = getUsername()
            val currentTime = Calendar.getInstance().time
            val dateFormat = SimpleDateFormat("dd-MM-yy HH:mma", Locale.getDefault())
            val timestamp = dateFormat.format(currentTime)
            val parsedDateTime = dateFormat.parse(timestamp)
            val date = SimpleDateFormat("dd-MM-yy", Locale.getDefault()).format(parsedDateTime)
            val time = SimpleDateFormat("HH:mma", Locale.getDefault()).format(parsedDateTime)
            if (savedUsername != null) {
                val Transaction = hashMapOf(
                    "Description" to expDescVal,
                    "IncomeAmount" to expAmou,
                    "Name" to savedUsername,
                    "ExpAmount" to 0,
                    "Date" to date,
                    "Time" to time
                )
                db.collection("Transaction")
                    .add(Transaction)
                    .addOnSuccessListener {
                        getTransactionData()
                        createNotificationChannel()
                        sendIncNotify()
                        val secondFragment = freg_Group()
                        activity?.supportFragmentManager?.beginTransaction()?.apply {
                            alertDialog.dismiss()
                            replace(R.id.frameLayout, secondFragment)
                            addToBackStack(null) // Optional, adds the transaction to the back stack
                            commit()
                        }
                    }
            } else {
                activity?.recreate()
            }

            //            val intent = Intent(activity, addExpense::class.java)
//            startActivity(intent)
//
//            alertDialog.dismiss()
        }
        cancelButton.setOnClickListener {
            alertDialog.dismiss()
        }

        // Show the dialog
        alertDialog.show()
    }

    private fun getUsername(): String? {
        // Retrieve the shared preferences
        val prefs = activity?.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

        // Retrieve the username from shared preferences using the key
        return prefs?.getString(KEY_USERNAME, null)
    }

    private fun sendExpNotify(){
        var builder = NotificationCompat.Builder(requireContext(), CHANNEL_ID)
        builder.setSmallIcon(R.drawable.logo2)
            .setContentTitle("Splitz Notification")
            .setContentText("Expense amount added to your account")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        with(NotificationManagerCompat.from(requireContext())){
            if (ActivityCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {

            }
            notify(1,builder.build())
        }
    }

    private fun sendIncNotify(){
        var builder = NotificationCompat.Builder(requireContext(), CHANNEL_ID)
        builder.setSmallIcon(R.drawable.logo2)
            .setContentTitle("Splitz Notification")
            .setContentText("Income amount added to your account")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        with(NotificationManagerCompat.from(requireContext())){
            if (ActivityCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {

            }
            notify(1,builder.build())
        }
    }

    private fun createNotificationChannel(){
           if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
               val channel = NotificationChannel(CHANNEL_ID, "First Channel",
               NotificationManager.IMPORTANCE_DEFAULT
                   )
               channel.description = "test description for my channel"

               val notificationManager = activity?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
               notificationManager.createNotificationChannel(channel)
           }
    }



}