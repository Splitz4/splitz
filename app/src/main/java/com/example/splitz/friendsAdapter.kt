package com.example.splitz

import android.Manifest
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemServiceName
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Recycler
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.lang.Float.parseFloat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

const val CHANNEL_ID = "channelId"
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
            val cancelButton = dialogView.findViewById<Button>(R.id.cancelSplit)

            submitButton.setOnClickListener {
                var expDescVal = expDesc.text.toString()
                var expAmou = expAmou.text.toString().toFloatOrNull() ?: 0.0f
                var friendsName = NameFriends
                val currentTime = Calendar.getInstance().time
                val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
                val timestamp = dateFormat.format(currentTime)
                val parsedDateTime = dateFormat.parse(timestamp)
                val date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(parsedDateTime)
                val time = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(parsedDateTime)

                val savedUsername = getUsername()
                if (savedUsername != null) {
                    val Transaction = hashMapOf(
                        "Description" to expDescVal,
                        "ExpAmount" to expAmou / 2,
                        "Name" to friendsName,
                        "IncomeAmount" to "0",
                        "Date" to date,
                        "Time" to time
                    )
                    db.collection("Transaction")
                        .add(Transaction)
                        .addOnSuccessListener {
                            val Transaction = hashMapOf(
                                "Description" to expDescVal,
                                "ExpAmount" to expAmou / 2,
                                "Name" to savedUsername,
                                "IncomeAmount" to "0",
                                "Date" to date,
                                "Time" to time
                            )
                            db.collection("Transaction")
                                .add(Transaction)
                                .addOnSuccessListener {
                                    alertDialog.dismiss()
                                    val newFragment = freg_Group()
                                    val transaction =
                                        (holder.itemView.context as FragmentActivity).supportFragmentManager.beginTransaction()
                                    transaction.replace(R.id.frameLayout, newFragment)
                                    transaction.addToBackStack(null)
                                    transaction.commit()

                                }
                        }
                }

            }
            cancelButton.setOnClickListener {
                alertDialog.dismiss()
            }

            alertDialog.show()

        }
    }

    private fun createNotificationChannel() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val channel = NotificationChannel(CHANNEL_ID,"First Channel",
            NotificationManager.IMPORTANCE_DEFAULT
                )
            channel.description = "Testing for my channel"

            val notificationManager = c.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
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

//createNotificationChannel()
//                            val intent = Intent(c, Home::class.java).apply {
//                                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//                            }
//                            val pendingIntent: PendingIntent = PendingIntent.getActivity(c, 0, intent, PendingIntent.FLAG_IMMUTABLE)
//                            var builder = NotificationCompat.Builder(c, CHANNEL_ID)
//                            builder.setSmallIcon(R.drawable.logo2)
//                                .setGroup(friendsName)
//                                .setShowWhen(savedUsername == friendsName)
//                                .setContentIntent(pendingIntent)
//                                .setContentTitle("Testing title")
//                                .setContentText("$expAmou")
//                                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
//
//
//
//
//
//                            with(NotificationManagerCompat.from(c)){
//                                if (ActivityCompat.checkSelfPermission(
//                                        c,
//                                        Manifest.permission.POST_NOTIFICATIONS
//                                    ) != PackageManager.PERMISSION_GRANTED
//                                ) {
//
//                                }
//                                notify(1,builder.build())
//                            }
