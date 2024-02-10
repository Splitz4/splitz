package com.example.splitz

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.splitz.databinding.ContactChildBinding
import java.util.ArrayList

class RCVAdapter (

    val contactL:ArrayList<ContactModel>
        ):RecyclerView.Adapter<RCVAdapter.MyViewHolder>(){
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
    }

    override fun getItemCount(): Int = contactL.size

}