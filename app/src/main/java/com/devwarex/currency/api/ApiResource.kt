package com.devwarex.currency.api

import retrofit2.Response

sealed class ApiResource<R> {

    companion object{

        fun <R>create(error: Throwable): ApiResource<R> {
            return OnError(isLoading = false,error = error.message ?: "unknown")
        }


        fun <R> create(response: Response<R>): ApiResource<R>{
            return if (response.isSuccessful){
                val body = response.body()
                if (body == null){
                    OnLoading(isLoading = false)
                }else{
                    when(response.code()){
                        200 -> OnSuccess(isLoading = false,body = body)
                        else -> OnLoading(isLoading = false)
                    }
                }
            }else{
                val msg = response.errorBody()?.string()
                val errorMsg = if (msg.isNullOrEmpty()) {
                    response.message()
                } else {
                    msg
                }
                OnError(isLoading = false, error = errorMsg ?: "unknown")
            }
        }

    }

    data class OnLoading<R>(val isLoading:Boolean): ApiResource<R>()
    data class OnSuccess<R>(val isLoading:Boolean,val body: R): ApiResource<R>()
    data class OnError<R>(val isLoading:Boolean,val error: String): ApiResource<R>()

}