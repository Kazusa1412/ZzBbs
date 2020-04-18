package com.elouyi.bbs.t

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.elouyi.bbs.data.User
import com.elouyi.elylib.md5
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*
import kotlin.concurrent.thread

fun register(user : User) : User{
    lateinit var us : User;
    val zhuceService = ServiceCeator.create(ZhuceService::class.java)
    zhuceService.zhuce(user, md5(user.username+user.pw+"ebbs")).enqueue(object : Callback<User>{
        override fun onResponse(call: Call<User>, response: Response<User>) {
            val u = response.body()
            if (u != null){
                Log.e("fuck",u.username+u.pw+u.p)
                us = u;
            }
        }

        override fun onFailure(call: Call<User>, t: Throwable) {
            t.printStackTrace()
        }
    })
    return us
}

fun zhuce(user: User){

}

fun getTime(ts : Long) : String{
    println("时间戳是： $ts")
    val formatter = SimpleDateFormat("yyyy年-MM月dd日 HH:mm",Locale.CHINA)
    val date = Date(ts)
    return formatter.format(date)
}

fun httpGet(url: String,callback : HttpCallback){
    thread {
        try {
            val client = OkHttpClient()
            val request = Request.Builder()
                .url(url)
                .build()
            val response = client.newCall(request).execute()
            val data = response.body()?.string()
            if (data != null){
                callback.onFinish(data)
            }else{
                callback.onError(Exception("没有数据"))
            }
        }catch (e:Exception){
            callback.onError(e)
        }
    }
}

fun httpPost(url : String, body : FormBody, callback: HttpCallback){
    thread {
        try {
            val client = OkHttpClient()
            val request = Request.Builder()
                .url(url)
                .post(body)
                .build()
            val response = client.newCall(request).execute()
            val data = response.body()?.string()
            if (data != null){
                callback.onFinish(data)
            }else{
                callback.onError(Exception("没有数据"))
            }
        }catch (e : Exception){
            callback.onError(e)
        }
    }
}

fun checkLogin(context: Context,callback: LoginCallback){
    println("开始checklogin")
    val prefs = context.getSharedPreferences("user",Context.MODE_PRIVATE)
    val username = prefs.getString("username"," ")
    val pw = prefs.getString("pw"," ")
    val t = prefs.getString("token"," ")
    if (t == " " || username == " " || pw == " "){
        callback.notLogin("1 未登录")
        return
    }
    val t2 = md5(username+t+"ebbs")
    val url = "http://120.26.174.247/api/logint/?username=$username&t1=$t&t2=$t2"
    println("开始网络请求login")
    httpGet(url,object : HttpCallback{
        override fun onFinish(response: String) {
            println("checklogin d infinish : $response")
            if (response.length > 30){
                val gson = Gson()
                val typeOf = object : TypeToken<User>(){}.type
                val user = gson.fromJson<User>(response.substring(1,response.lastIndex),typeOf)
                if (user != null){
                    callback.isLogin(user)
                    return
                }
            }
            callback.notLogin(response)
        }

        override fun onError(exception: Exception) {
            println("check login d  excep: $exception")
            callback.notLogin("获取登录信息失败 ${exception.toString()}")
        }
    })
}

fun checkTExist(context: Context) : Boolean{
    val prf = context.getSharedPreferences("user",Context.MODE_PRIVATE)
    val t = prf.getString("token"," ")
    return t != " "
}

fun getUserFromShare(context: Context):User{
    val prf = context.getSharedPreferences("user",Context.MODE_PRIVATE)
    val id = prf.getInt("id",0)
    val username = prf.getString("username"," ")
    val pw = prf.getString("pw"," ")
    val tz = prf.getInt("tz",0)
    val p = prf.getInt("p",0)
    val token = prf.getString("tokem"," ")
    val createTime = prf.getLong("createTime",0)
    return User(id,username!!,pw!!,tz,p,token!!,createTime)
}

fun setUsertoShare(context: Context, user: User){
    context.getSharedPreferences("user", Context.MODE_PRIVATE).open {
        putInt("id",user.id)
        putString("username",user.username)
        putString("pw",user.pw)
        putInt("tz",user.tz)
        putInt("p",user.p)
        putString("token",user.token)
        putLong("ceateTime",user.createTime)
    }
}

fun logOut(context: Context){
    context.getSharedPreferences("user",Context.MODE_PRIVATE).open {
        putString("username"," ")
        putString("pw"," ")
        putString("token"," ")
    }

}

fun SharedPreferences.open(block: SharedPreferences.Editor.() -> Unit){
    val editor = edit()
    editor.block()
    editor.apply()
}

fun main() {
    var str = "易老师最猥琐"
}


