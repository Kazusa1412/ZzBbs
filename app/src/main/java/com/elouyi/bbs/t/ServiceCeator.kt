package com.elouyi.bbs.t

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ServiceCeator {

    private const val BASE_URL = "http://120.26.174.247/"

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

     fun <T> create(serviceClass : Class<T>) : T = retrofit.create(serviceClass)
}