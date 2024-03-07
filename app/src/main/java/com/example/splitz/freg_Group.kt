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
import android.widget.TextView
import androidx.cardview.widget.CardView
import com.airbnb.lottie.LottieAnimationView
import com.anychart.AnyChart
import com.anychart.AnyChartView
import com.anychart.chart.common.dataentry.DataEntry
import com.anychart.chart.common.dataentry.ValueDataEntry
import com.anychart.charts.Pie
import com.google.firebase.firestore.FirebaseFirestore

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [freg_Group.newInstance] factory method to
 * create an instance of this fragment.
 */
class freg_Group : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var chart: AnyChartView? = null
    private var incomeAmount: Double = 0.0 // Initialize to 0
    private var expenseAmount: Double = 0.0 // Initialize to 0
    private var salary: List<Double>? = null // Initially null
    private val month = listOf("Income", "Expense")
    private lateinit var noGraphText : LottieAnimationView

    private val db = FirebaseFirestore.getInstance()


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
        val view = inflater.inflate(R.layout.fragment_freg__group, container, false)
        chart = view?.findViewById(R.id.pieGraph)
        noGraphText = view.findViewById<LottieAnimationView>(R.id.noGraphText)

        // Call getTransaction to fetch data from Firestore
        getTransaction()

        return view
    }

    private fun configChartView() {
        val pie: Pie = AnyChart.pie()
        val dataPieChart: MutableList<DataEntry> = mutableListOf()

        // Check if salary is not null and contains two elements
        if (salary != null && salary!!.size == 2) {
            for (index in month.indices) {
                dataPieChart.add(ValueDataEntry(month[index], salary!![index]))
            }
            pie.data(dataPieChart)
            chart?.setChart(pie)
        }
    }
    private fun getTransaction() {
        val savedUsername = getUsername()
        if (savedUsername != null) {
            db.collection("Transaction").whereEqualTo("Name", savedUsername)
                .get()
                .addOnSuccessListener { collection ->
                    val documents = collection.documents
                    var totalIncome = 0.0
                    var totalExpenses = 0.0

                    for (document in documents) {
                        val incomeAmount =
                            document.getString("IncomeAmount")?.toDoubleOrNull() ?: 0.0
                        val expenseAmount = document.getDouble("ExpAmount") ?: 0.0

                        totalIncome += incomeAmount
                        totalExpenses += expenseAmount
                    }
                    incomeAmount = totalIncome
                    expenseAmount = totalExpenses

                    // Calculate salary list after retrieving data
                    if(incomeAmount > 0.0 || expenseAmount > 0.0) {

                        salary = listOf(incomeAmount, expenseAmount)
                        chart?.visibility = View.VISIBLE

                        configChartView()
                    } else {
                        noGraphText.visibility = View.VISIBLE
                    }


                }
                .addOnFailureListener { exception ->
                    Log.w(ContentValues.TAG, "Error getting documents: ", exception)
                }
        } else {
            // Handle the case where savedUsername is null
        }
    }

    private fun getUsername(): String? {
        // Retrieve the shared preferences
        val prefs = activity?.getSharedPreferences(login.PREFS_NAME, Context.MODE_PRIVATE)

        // Retrieve the username from shared preferences using the key
        return prefs?.getString(login.KEY_USERNAME, null)
    }
}