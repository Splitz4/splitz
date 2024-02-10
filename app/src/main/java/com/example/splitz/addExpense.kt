package com.example.splitz

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.anychart.AnyChart
import com.anychart.AnyChartView
import com.anychart.chart.common.dataentry.DataEntry
import com.anychart.chart.common.dataentry.ValueDataEntry
import com.anychart.charts.Pie
import com.google.android.material.floatingactionbutton.FloatingActionButton

class addExpense : AppCompatActivity() {

    private var chart:AnyChartView?=null
    private val salary = listOf(200,300,400,600)
    private val month = listOf("January", "February", "March", "April")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_expense)

        chart = findViewById(R.id.pieGraph)

        configChartView()
    }

    private fun configChartView() {
        val pie : Pie = AnyChart.pie()

        val dataPieChart : MutableList<DataEntry> = mutableListOf()

        for(index in salary.indices){
            dataPieChart.add(ValueDataEntry(month.elementAt(index), salary.elementAt(index)))

        }
        pie.data(dataPieChart)
        chart?.setChart(pie)

    }

}