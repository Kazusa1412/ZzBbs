package com.elouyi.bbs

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import com.elouyi.bbs.data.User
import com.elouyi.bbs.t.HttpCallback
import com.elouyi.bbs.t.httpPost
import com.elouyi.bbs.t.setUsertoShare
import com.elouyi.elylib.*
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_zhuce.*
import kotlinx.android.synthetic.main.toolbar.*
import okhttp3.FormBody
import java.lang.Exception

class ZhuceActivity : ElyActivity() {

    var zh = ""
    var mm = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_zhuce)
        setSupportActionBar(toolbar)
        supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(true)
        }
        title = "注册"
        initv()
    }

    fun initv(){
        btn_zhuce.setOnClickListener {
            zh = editeZcZh.text.toString()
            if (zh.length < 3){
                "账号必须大于三个字符！".showToast(this)
                return@setOnClickListener
            }
            mm = editZcMm.text.toString()
            if (mm.length < 6){
                "密码必须大于6个字符！".showToast(this)
                return@setOnClickListener
            }
            val t = md5(zh+mm+"ebbs")
            val url = "http://120.26.174.247/api/register/"
            val body = FormBody.Builder()
                .add("username",zh)
                .add("pw",mm)
                .add("t",t)
                .build()
            httpPost(url,body,object : HttpCallback{
                override fun onFinish(response: String) {
                    Log.e("注册的onfinish回调: ", response)
                    runOnUiThread {
                        if (response.length > 30){
                            val gson = Gson()
                            var re = response.substring(1,response.lastIndex)
                            val us = gson.fromJson(re,User::class.java)
                            setUsertoShare(this@ZhuceActivity,us)
                            ActivityCollector.finishAll()
                            startActivity<MainActivity>(this@ZhuceActivity){}
                        }else{
                            "错误：：$response".showToast(this@ZhuceActivity)
                        }
                    }
                }

                override fun onError(exception: Exception) {
                    Log.e("注册的onerror回调： ",exception.toString())
                    runOnUiThread {
                        "错误：${exception.toString()}".showToast(this@ZhuceActivity)
                    }
                }
            })
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
