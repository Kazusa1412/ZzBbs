package com.elouyi.bbs

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import com.elouyi.bbs.data.User
import com.elouyi.bbs.t.HttpCallback
import com.elouyi.bbs.t.httpGet
import com.elouyi.bbs.t.setUsertoShare
import com.elouyi.elylib.*
import com.elouyi.elylib.ElyActivity
import com.elouyi.elylib.md5
import com.elouyi.elylib.showToast
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.toolbar.*
import java.lang.Exception
import java.lang.NullPointerException
import kotlin.math.log

class LoginActivity : ElyActivity() {

    var zh = ""
    var mm = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        setSupportActionBar(toolbar)
        supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(true)
        }
        title = "登录"
        initv()
    }

    private fun initv(){
        btmLogin.setOnClickListener {
            zh = editZhanghao.text.toString()
            mm = editMima.text.toString()
            if (zh.length < 3){
                "账号不能小于3个字符".showToast(this)
                return@setOnClickListener
            }
            if (mm.length < 6){
                "密码不能小于6个字符".showToast(this)
                return@setOnClickListener
            }
            val t = md5(zh + mm + "ebbs")
            httpGet("http://120.26.174.247/api/loginp/?username=$zh&pw=$mm&t=$t",object : HttpCallback{
                override fun onFinish(response: String) {
                    Log.e("数据时: ",response)
                    if (response.length > 30){
                        runOnUiThread {
                            val ug = response.substring(1,response.lastIndex)
                            Log.e("数据时: ",ug)
                            val gson = Gson()
                            val user = gson.fromJson(ug, User::class.java)
                            setUsertoShare(this@LoginActivity,user)
                            ActivityCollector.finishAll()
                            com.elouyi.elylib.startActivity<MainActivity>(this@LoginActivity){}
                        }
                    }else{
                        runOnUiThread {
                            "密码错误".showToast(this@LoginActivity)
                        }
                    }

                }

                override fun onError(exception: Exception) {
                    Log.e("登录出错",exception.toString())
                    runOnUiThread {
                        if (exception is NullPointerException) {
                            "账号或密码错误".showToast(this@LoginActivity)
                        }
                        else{
                            "错误：${exception.toString()}".showToast(this@LoginActivity)
                        }
                    }

                }
            })
        }

        textZhuce.setOnClickListener {
            startActivity<ZhuceActivity>(this){}
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home -> {
                finish()
            }
        }
        return true
    }
}
