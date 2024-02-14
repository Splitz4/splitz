package com.example.splitz

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.splitz.databinding.ContactChildBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import java.util.ArrayList

class RCVAdapter (
    private var c:Context,
    val contactL:ArrayList<ContactModel>
):RecyclerView.Adapter<RCVAdapter.MyViewHolder>(){
    private lateinit var firebaseAuth : FirebaseAuth
    val db = FirebaseFirestore.getInstance()
    inner class MyViewHolder(val binding: ContactChildBinding) : RecyclerView.ViewHolder(binding.root){
        val nameTV = binding.nameContact
        val numberTV = binding.numberContact
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RCVAdapter.MyViewHolder {
        return MyViewHolder(ContactChildBinding.inflate(LayoutInflater.from(parent.context),parent, false))
    }

    override fun onBindViewHolder(holder: RCVAdapter.MyViewHolder, position: Int) {
        val item = contactL[position]
        holder.binding.nameContact.text = item.displayName
        holder.binding.numberContact.text = item.number

        val savedUsername = getUsername()

        holder.binding.root.setOnClickListener {

            val UserFriends = hashMapOf(
                "Name" to item.displayName,
                "Number" to item.number,
                "UserName" to savedUsername
            )

            db.collection("UserFriends")
                .add(UserFriends)
                .addOnSuccessListener {
                    Toast.makeText(c, "Working", Toast.LENGTH_LONG).show()
                }
        }

    }
    override fun getItemCount(): Int = contactL.size

    private fun getUsername(): String? {
        // Retrieve the shared preferences
        val prefs = c.getSharedPreferences(login.PREFS_NAME, Context.MODE_PRIVATE)

        // Retrieve the username from shared preferences using the key
        return prefs?.getString(login.KEY_USERNAME, null)
    }

}