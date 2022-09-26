package com.devwarex.currency.util
import java.util.Calendar
import java.util.Date

object TimeoutUtil {

    private const val symbols_ttl = 86_400_000
    private const val rate_ttl = 600_000
    fun isSymbolsTimeout(currentTime: Long,savedTime: Long): Boolean = (currentTime - savedTime) > symbols_ttl

    fun convertServerTimeToLocal(serverTime: Long): Long {
        val date = Date(serverTime*1000)
        val calendar = Calendar.getInstance()
        calendar.time = date

        return calendar.timeInMillis

    }
    fun rateTime(): Long = Calendar.getInstance().timeInMillis - rate_ttl
}