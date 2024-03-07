package com.example.splitz

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.splitz.databinding.ContactChildBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.selects.select
import java.util.ArrayList

class groupAdapter(private var c: Context,
                   val contactL: ArrayList<groupModel>
): RecyclerView.Adapter<groupAdapter.MultiselectViewHolder>(){
    private var isEnable = false
    private val itemSelectedList = mutableListOf<Int>()
    private lateinit var firebaseAuth : FirebaseAuth
    val db = FirebaseFirestore.getInstance()
//    inner class MyViewHolder(val binding: ContactChildBinding) : RecyclerView.ViewHolder(binding.root){
//        val nameTV = binding.nameContact
//        val numberTV = binding.numberContact
//    }

    class MultiselectViewHolder(public val view: View) : RecyclerView.ViewHolder(view) {
             val tv : TextView = view.findViewById(R.id.nameContact)
             val  iv : TextView = view.findViewById(R.id.numberContact)
           //  val submitGroupList : TextView = view.findViewById(R.id.submitGroupList)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MultiselectViewHolder {
        val adapterLayout = LayoutInflater.from(parent.context).inflate(R.layout.contact_child,parent, false)
        return MultiselectViewHolder(adapterLayout)
    }

    override fun onBindViewHolder(holder: MultiselectViewHolder, position: Int) {
        val item = contactL[position]
        holder.tv.text = item.displayName
        holder.iv.text = item.number

        val savedUsername = getUsername()


        holder.view.setOnLongClickListener{
            selectItem(holder,item,position)
            true
        }

        holder.view.setOnClickListener {
            if(itemSelectedList != null){
                Toast.makeText(c, "${item.displayName}", Toast.LENGTH_LONG).show()
            }
            else{
                Toast.makeText(c, "No Selected Items", Toast.LENGTH_LONG).show()
            }
        }

//        holder.binding.root.setOnClickListener {
//
//            val UserFriends = hashMapOf(
//                "Name" to item.displayName,
//                "Number" to item.number,
//                "UserName" to savedUsername
//            )
//
//            db.collection("UserFriends")
//                .add(UserFriends)
//                .addOnSuccessListener {
//                    Toast.makeText(c, "Working", Toast.LENGTH_LONG).show()
//                }
//        }

    }

    private fun selectItem(holder: MultiselectViewHolder, item: groupModel, position: Int) {
        isEnable = true
        itemSelectedList.add(position)
        holder.tv.text ="Yeeeeeahhh"
        holder.iv.text = "Working"
        item.Selected = true
    }

    override fun getItemCount(): Int = contactL.size

    private fun getUsername(): String? {
        // Retrieve the shared preferences
        val prefs = c.getSharedPreferences(login.PREFS_NAME, Context.MODE_PRIVATE)

        // Retrieve the username from shared preferences using the key
        return prefs?.getString(login.KEY_USERNAME, null)
    }

}