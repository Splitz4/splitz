package com.example.splitz

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class transactionAdapter(private val transactionList: ArrayList<transactionsData>) :
    RecyclerView.Adapter<transactionAdapter.MyViewHolder>(){



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): transactionAdapter.MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.transactions_view,
        parent,false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: transactionAdapter.MyViewHolder, position: Int) {
        val currentItem = transactionList[position]

        if(currentItem.IncomeAmount == "0"){
            holder.transactionType.text = "Expense"
            holder.transactionAmount.text = currentItem.ExpAmount.toString()
        }
        else if (currentItem.ExpAmount == 0.0){
            holder.transactionAmount.text = "N"
            holder.transactionType.text = "Income"
            holder.transactionAmount.text = currentItem.IncomeAmount
        }
        holder.transactionDescrip.text = currentItem.Description
        holder.timeTransaction.text = currentItem.Time

    }

    override fun getItemCount(): Int {
        return transactionList.size
    }
    class MyViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){

        val transactionAmount : TextView = itemView.findViewById(R.id.transactionAmount)
        val incomeAmount : TextView = itemView.findViewById(R.id.incomesAmount)
        val transactionType : TextView = itemView.findViewById(R.id.typeTransaction)
        val transactionDescrip : TextView = itemView.findViewById(R.id.descriptionTransaction)
        val timeTransaction : TextView = itemView.findViewById(R.id.timeTransaction)

    }
}