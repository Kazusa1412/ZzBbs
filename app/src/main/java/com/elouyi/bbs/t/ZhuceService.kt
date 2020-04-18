package com.elouyi.bbs.t

import com.elouyi.bbs.data.User
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query

interface ZhuceService {

    @POST("api/register/")
    fun zhuce(@Body user : User,@Query("t") t: String) : Call<User>
}