package com.devwarex.currency.util

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.Calendar

data class ApiDateTimeModel(var start: String = "",var end: String = "",var days:ArrayList<String> = arrayListOf())
object ApiTimeUtil {

    @SuppressLint("SimpleDateFormat")
    fun getTimeSeries(days:Int): ApiDateTimeModel{
        val calender = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("yyyy-MM-dd")
        val data = ApiDateTimeModel()
        (1..days).forEach {
            calender.add(Calendar.DAY_OF_YEAR,-1)
            if (it == 1){
                data.end = dateFormat.format(calender.timeInMillis)
            }
            if (it == days){
                data.start = dateFormat.format(calender.timeInMillis)
            }
            data.days.add(dateFormat.format(calender.timeInMillis))
        }
        return data
    }
}