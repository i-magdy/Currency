package com.devwarex.currency.util

object TimeoutUtil {

    private const val symbols_ttl = 86_400_000
    fun isSymbolsTimeout(currentTime: Long,savedTime: Long): Boolean = (currentTime - savedTime) > symbols_ttl
}