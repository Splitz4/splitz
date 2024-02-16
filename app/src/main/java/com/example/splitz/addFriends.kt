package com.example.splitz

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.common.util.Strings
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.toObject
import java.lang.Float.parseFloat
import java.util.jar.Attributes.Name

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [addFriends.newInstance] factory method to
 * create an instance of this fragment.
 */
class addFriends : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var recyclerView: RecyclerView
    private lateinit var friendsList: ArrayList<friendsData>
    private lateinit var adapter: ArrayAdapter<String>

    //  private lateinit var friendsAdapter: friendsAdapter
    private lateinit var db: FirebaseFirestore
    private lateinit var autoCompleteTextView: AutoCompleteTextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_add_friends, container, false)
        val buttonContact = view.findViewById<CardView>(R.id.buttonContact)
        val splitButton = view.findViewById<CardView>(R.id.splitAmount11)



        recyclerView = view.findViewById(R.id.friendsRecyclerView)
        recyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        recyclerView.setHasFixedSize(true)
        friendsList = arrayListOf<friendsData>()
        eventChangeListner()
//        friendsAdapter = friendsAdapter(friendsList)
//        recyclerView.adapter = friendsAdapter

        buttonContact.setOnClickListener {
            navigateButton()

        }

        splitButton.setOnClickListener {

            showPopupForm()
            //Toast.makeText(requireContext(),"workn",Toast.LENGTH_LONG).show()

        }
        return view

    }

    private fun eventChangeListner() {
        val savedUsername = getUsername()
        db = FirebaseFirestore.getInstance()

        db.collection("UserFriends").get()
            .addOnSuccessListener { collection ->
                val documents = collection.documents
                for (document in documents) {
                    if(document.get("UserName")==savedUsername){
                        val friends = document.toObject(friendsData::class.java)
                        friendsList.add(friends!!)
                    }
                    recyclerView.adapter = friendsAdapter(friendsList, requireContext())

                }
            }

    }

    private fun getUsername(): String? {
        // Retrieve the shared preferences
        val prefs = activity?.getSharedPreferences(login.PREFS_NAME, Context.MODE_PRIVATE)

        // Retrieve the username from shared preferences using the key
        return prefs?.getString(login.KEY_USERNAME, null)
    }

    private fun navigateButton() {
        val intent = Intent(activity, contactList::class.java)
        startActivity(intent)
    }

    private fun showPopupForm() {

        val dialogView =
            LayoutInflater.from(requireContext()).inflate(R.layout.createsplit, null)

        var expDesc = dialogView.findViewById<EditText>(R.id.splitDescriptionEdit)
        var expAmou = dialogView.findViewById<EditText>(R.id.splitAmountEdit)

        val alertDialogBuilder = AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .setTitle("Income Form")

        val alertDialog = alertDialogBuilder.create()


        val submitButton = dialogView.findViewById<Button>(R.id.splitExp)

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



}

