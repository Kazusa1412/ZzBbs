package com.elouyi.bbs.t

import com.elouyi.bbs.data.Tiezi
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface TieziService {

    @GET("api/gettiezi/")
    fun getTiezi(@Query("a") a: String,@Query("t") t : String): Call<List<Tiezi>>
}