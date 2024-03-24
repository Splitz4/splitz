package com.example.splitz

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class transactioListAdapter(private val allTransactionList : ArrayList<transactionListData>)
    : RecyclerView.Adapter<transactioListAdapter.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.transaction_list_view,
            parent,false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: transactioListAdapter.MyViewHolder, position: Int) {
        val currentItem = allTransactionList[position]

        if(currentItem.IncomeAmount == "0"){
          //  holder.transactionType.text = "Expense"
            holder.transactionAmount.text = currentItem.ExpAmount.toString()
        }
        else if (currentItem.ExpAmount == 0.0){
            holder.transactionAmount.text = "N"
           // holder.transactionType.text = "Income"
            holder.transactionAmount.text = currentItem.IncomeAmount
        }
        holder.transactionDescrip.text = currentItem.Description
        holder.timeTransaction.text = currentItem.Time
        holder.dateTransaction.text = currentItem.Date

    }

    override fun getItemCount(): Int {
        return allTransactionList.size
    }

    class MyViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){

        val transactionAmount : TextView = itemView.findViewById(R.id.amountTextTrans)
       // val incomeAmount : TextView = itemView.findViewById(R.id.incomesAmount)
       // val transactionType : TextView = itemView.findViewById(R.id.typeTransaction)
        val transactionDescrip : TextView = itemView.findViewById(R.id.descriptionText)
        val timeTransaction : TextView = itemView.findViewById(R.id.timeText)
        val dateTransaction : TextView = itemView.findViewById(R.id.dateText)

    }

}