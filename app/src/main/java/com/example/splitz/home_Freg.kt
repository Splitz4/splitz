package com.example.splitz

import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View

import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.splitz.login.Companion.KEY_USERNAME
import com.example.splitz.login.Companion.PREFS_NAME


import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.auth.User



private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class home_Freg : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    val db = FirebaseFirestore.getInstance()

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







        userRecyclerView = view.findViewById(R.id.recycler_viewTransactions)
        //userRecyclerView.LayoutManager = LinearLayoutManager(this)
        userRecyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        userRecyclerView.setHasFixedSize(true)

        transactionList = arrayListOf<transactionsData>()

        getTransactionData()


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
                        val expenseAmount = document.getString("ExpAmount")?.toDoubleOrNull() ?: 0.0

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
        } else {

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
            val secondFragment = addFriends()
            activity?.supportFragmentManager?.beginTransaction()?.apply {
                replace(R.id.frameLayout, secondFragment)
                addToBackStack(null) // Optional, adds the transaction to the back stack
                commit()
            }
        }
        return view
    }


    //    fun replaceFragment(fragment: Fragment) {
//        val fragmentManager = supportFragmentManager
//        val fragmentTransaction = fragmentManager.beginTransaction()
//        fragmentTransaction.replace(R.id.frameLayout, fragment)
//        fragmentTransaction.commit()
//    }
    private fun getTransactionData() {
        val savedUsername = getUsername()
        db.collection("Transaction").get()
            .addOnSuccessListener { collection ->
                val documents = collection.documents
                for (document in documents) {
                    if(document.get("Name")==savedUsername){
                        val Transactions = document.toObject(transactionsData::class.java)
                        transactionList.add(Transactions!!)
                    }
                    userRecyclerView.adapter = transactionAdapter(transactionList)

                }
            }
    }

    private fun showPopupForm() {
        val dialogView =
            LayoutInflater.from(requireContext()).inflate(R.layout.create_expense, null)
        var expDesc = dialogView.findViewById<EditText>(R.id.expenseDescriptionEdit)
        var expAmou = dialogView.findViewById<EditText>(R.id.expAmountEdit)

        val alertDialogBuilder = AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .setTitle("Income Form")

        val alertDialog = alertDialogBuilder.create()


        val submitButton = dialogView.findViewById<Button>(R.id.saveExp)
        submitButton.setOnClickListener {
            var expDescVal = expDesc.text.toString()
            var expAmou = expAmou.text.toString()
            val savedUsername = getUsername()
            if (savedUsername != null) {
                val Transaction = hashMapOf(
                    "Description" to expDescVal,
                    "ExpAmount" to expAmou,
                    "Name" to savedUsername,
                    "IncomeAmount" to "0"
                )
                db.collection("Transaction")
                    .add(Transaction)
                    .addOnSuccessListener {
                        Toast.makeText(activity, "Working", Toast.LENGTH_LONG).show()
                    }
            } else {
                activity?.recreate()
            }

            //            val intent = Intent(activity, addExpense::class.java)
//            startActivity(intent)
//
//            alertDialog.dismiss()
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
        submitButton.setOnClickListener {
            var expDescVal = incomeDesc.text.toString()
            var expAmou = incomeAmou.text.toString()
            val savedUsername = getUsername()
            if (savedUsername != null) {
                val Transaction = hashMapOf(
                    "Description" to expDescVal,
                    "IncomeAmount" to expAmou,
                    "Name" to savedUsername,
                    "ExpAmount" to "0"
                )
                db.collection("Transaction")
                    .add(Transaction)
                    .addOnSuccessListener {
                        Toast.makeText(activity, "Working", Toast.LENGTH_LONG).show()
                    }
            } else {
                activity?.recreate()
            }

            //            val intent = Intent(activity, addExpense::class.java)
//            startActivity(intent)
//
//            alertDialog.dismiss()
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




}