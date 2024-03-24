package com.example.splitz

import android.app.Dialog
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
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.cardview.widget.CardView
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
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
interface progresBars{
    fun showProgressDialog(context:Context, text: String)
    fun hideProgressDialog()

}

class addFriends : Fragment(),  progresBars{
    // TODO: Rename and change types of parameters
    private var mProgressDialog: Dialog? = null
    private var param1: String? = null
    private var param2: String? = null

    private  var recyclerView: RecyclerView? = null
    private lateinit var friendsList: ArrayList<friendsData>
    private lateinit var friendsAdapter: friendsAdapter
    //  private lateinit var friendsAdapter: friendsAdapter
    private lateinit var db: FirebaseFirestore
    private lateinit var autoCompleteTextView: AutoCompleteTextView
    private lateinit var noFriendsText : LottieAnimationView

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
        noFriendsText = view.findViewById(R.id.noFriendsText)
        val backTraceAddFrd = view.findViewById<ImageView>(R.id.backTraceAddFrd)



        recyclerView = view.findViewById(R.id.friendsRecyclerView)
        recyclerView?.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        recyclerView?.setHasFixedSize(true)
        friendsList = arrayListOf<friendsData>()
        eventChangeListner()
//        friendsAdapter = friendsAdapter(friendsList)
//        recyclerView.adapter = friendsAdapter

        buttonContact.setOnClickListener {
            navigateButton()

        }

        backTraceAddFrd.setOnClickListener {
            if (childFragmentManager.backStackEntryCount > 0) {
                childFragmentManager.popBackStack()
            } else {
                activity?.onBackPressed()
            }
        }
        return view

    }

    private fun eventChangeListner() {
        showProgressDialog(requireContext(),resources.getString(R.string.please_click_back_again_to_exit))
        val savedUsername = getUsername()
        db = FirebaseFirestore.getInstance()


        db.collection("UserFriends").get()
            .addOnSuccessListener { collection ->
                hideProgressDialog()
                val documents = collection.documents
                for (document in documents) {
                    if (document.get("UserName") == savedUsername) {
                        val friends = document.toObject(friendsData::class.java)
                        friendsList.add(friends!!)
                    }
                }
                if(friendsList!= emptyList<friendsData>()) {
                    recyclerView?.adapter = friendsAdapter(friendsList, requireContext())
                }
                else{
                    noFriendsText.visibility = View.VISIBLE
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
        val secondFragment = contact_list()
        activity?.supportFragmentManager?.beginTransaction()?.apply {

            replace(R.id.frameLayout, secondFragment)
            addToBackStack(null) // Optional, adds the transaction to the back stack
            commit()
        }
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
                        val secondFragment = freg_Group()
                        activity?.supportFragmentManager?.beginTransaction()?.apply {
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

        // Show the dialog
        alertDialog.show()
    }

    override fun showProgressDialog(context: Context, text: String) {
        mProgressDialog = Dialog(context)
        mProgressDialog?.setContentView(R.layout.dialog_progress)
        // Set progress text or any other configurations
        // mProgressDialog?.tv_progress_text?.text = text
        mProgressDialog?.show()
    }

    override fun hideProgressDialog() {
        mProgressDialog?.dismiss()
    }


}

