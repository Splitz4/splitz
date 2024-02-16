package com.example.splitz

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Recycler
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.lang.Float.parseFloat

class friendsAdapter(private val friendsList : ArrayList<friendsData>, private var c: Context) :
    RecyclerView.Adapter<friendsAdapter.MyViewHolder>() {
    private lateinit var firebaseAuth : FirebaseAuth
    val db = FirebaseFirestore.getInstance()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): friendsAdapter.MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.friends_item,
        parent, false)

        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: friendsAdapter.MyViewHolder, position: Int) {
        val friends = friendsList[position]
        holder.friendsName.text = friends.Name
        holder.friendsNumber.text = friends.Number

        holder.itemView.setOnClickListener {

            val dialogView =
                LayoutInflater.from(c).inflate(R.layout.createsplit, null)

            var expDesc = dialogView.findViewById<EditText>(R.id.splitDescriptionEdit)
            var expAmou = dialogView.findViewById<EditText>(R.id.splitAmountEdit)
            var NameFriends = friends.Name


            val alertDialogBuilder = AlertDialog.Builder(c)
                .setView(dialogView)
                .setTitle("Splitz Form")

            val alertDialog = alertDialogBuilder.create()


            val submitButton = dialogView.findViewById<Button>(R.id.splitExp)

            submitButton.setOnClickListener {
                var expDescVal = expDesc.text.toString()
                var expAmou = expAmou.text.toString()
                var friendsName = NameFriends

                val savedUsername = getUsername()
                if (savedUsername != null) {
                    val Transaction = hashMapOf(
                        "Description" to expDescVal,
                        "ExpAmount" to expAmou,
                        "Name" to friendsName,
                        "IncomeAmount" to "0"
                    )
                    db.collection("Transaction")
                        .add(Transaction)
                        .addOnSuccessListener {
                            Toast.makeText(c, "Working", Toast.LENGTH_LONG).show()
                        }
                }

            }

            alertDialog.show()

        }
    }
    override fun getItemCount(): Int {
       return friendsList.size
    }


    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        var friendsName : TextView = itemView.findViewById(R.id.tvNameFriends)
        var friendsNumber : TextView = itemView.findViewById(R.id.tvNumberFriends)
    }
    private fun getUsername(): String? {
        // Retrieve the shared preferences
        val prefs = c.getSharedPreferences(login.PREFS_NAME, Context.MODE_PRIVATE)

        // Retrieve the username from shared preferences using the key
        return prefs?.getString(login.KEY_USERNAME, null)
    }
}