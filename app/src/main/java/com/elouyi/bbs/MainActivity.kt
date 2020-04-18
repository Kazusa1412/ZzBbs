package com.elouyi.bbs

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import com.elouyi.bbs.data.Tiezi
import com.elouyi.bbs.data.User
import com.elouyi.bbs.t.*
import com.elouyi.elylib.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.toolbar.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.Exception
import kotlin.concurrent.thread

class MainActivity : ElyActivity() {

    private val tieziList = ArrayList<Tiezi>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        //toolbar.setNavigationIcon(R.drawable.wdl)
        supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(true)
            if (checkTExist(this)){
                it.setHomeAsUpIndicator(R.drawable.ye)
            }else{
                it.setHomeAsUpIndicator(R.drawable.wdl)
            }
        }
        //gettiezi()
        gettiezi("http://120.26.174.247/api/gettiezi/")
        initv()

    }

    private fun freshtiezi(){
        val adapter = TieziAdapter(this,R.layout.tiezi_item,tieziList)
        mainLisv.adapter = adapter
    }

    private fun initv(){
        mainLisv.setOnItemClickListener { _, _, position, _ ->
            val tz = tieziList[position]
            startActivity<TieziActivity>(this){
                putExtra("tiezi",tz)
            }
        }

        fab.setOnClickListener {
            if (checkTExist(this)){
                val intent = Intent(this,FatieActivity::class.java)
                startActivityForResult(intent,1)
            }else{
                AlertDialog.Builder(this@MainActivity).apply {
                    setTitle("您还未登录")
                    setMessage("登录后才能发帖和评论 。")
                    setCancelable(true)
                    setPositiveButton("登录"){ _, _ ->
                        startActivity<LoginActivity>(this@MainActivity){}
                    }
                    setNegativeButton("取消"){_,_ ->

                    }
                    show()
                }
            }

        }

        swipeMain.setOnRefreshListener {
            thread {
                gettiezi("http://120.26.174.247/api/gettiezi/")
                swipeMain.isRefreshing = false
            }
        }
    }

    fun gettiezi(url: String){
        val t = "pppp"
        val tt = md5(t+"ebbs")
        Log.e(tag,"tt is $tt")
        val d = httpGet("$url?a=$t&t=$tt",object : HttpCallback{
            override fun onError(exception: Exception) {
                Log.e(tag,exception.toString())
                runOnUiThread {
                    "错误： ${exception.toString()}".showToast(this@MainActivity)
                }
            }

            override fun onFinish(response: String) {
                Log.e(tag, "结果是 : $response")
                val gson = Gson()
                val typeOf = object : TypeToken<List<Tiezi>>(){}.type
                val list = gson.fromJson<List<Tiezi>>(response,typeOf)
                if (list != null){
                    tieziList.clear()
                    for (t in list){
                        tieziList.add(t)
                    }
                    runOnUiThread {
                        freshtiezi()
                    }
                }
            }
        })
    }

    private fun gettiezi(){
        val retrofit = Retrofit.Builder()
            .baseUrl("http://120.26.174.247/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val tieziService = retrofit.create(TieziService::class.java)
        var a = "fsdadasd"
        var t = md5(a)
        tieziService.getTiezi(a, t).enqueue(object : Callback<List<Tiezi>>{
            override fun onResponse(call: Call<List<Tiezi>>, response: Response<List<Tiezi>>) {
                val list = response.body()
                if (list != null){
                    for (tiezi in list){
                        tieziList.add(tiezi)
                    }
                }
                runOnUiThread {
                    freshtiezi()
                }

            }

            override fun onFailure(call: Call<List<Tiezi>>, t: Throwable) {
                runOnUiThread {
                    "错误：${t.toString()}"
                }
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode){
            1 -> if (resultCode == Activity.RESULT_OK){
                val rt = data?.getStringExtra("data_return")
                when(rt){
                    "ok" -> {
                        thread {
                            gettiezi("http://120.26.174.247/api/gettiezi/")
                        }
                    }
                    "not_login" ->{
                        supportActionBar?.setHomeAsUpIndicator(R.drawable.wdl)
                    }
                }
            }

        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home -> {
                val prf = getSharedPreferences("user",Context.MODE_PRIVATE)
                val t = prf.getString("token"," ")
                if (t == " "){
                    checkLogin(this,object : LoginCallback{
                        override fun isLogin(user: User) {
                            TODO("Not yet implemented")
                        }

                        override fun notLogin(msg: String) {
                            runOnUiThread {
                                AlertDialog.Builder(this@MainActivity).apply {
                                    setTitle("您还未登录")
                                    setMessage("登录后才能发帖和评论 。")
                                    setCancelable(true)
                                    setPositiveButton("登录"){ _, _ ->
                                        startActivity<LoginActivity>(this@MainActivity){}
                                    }
                                    setNegativeButton("取消"){_,_ ->

                                    }
                                    show()
                                }
                            }
                        }
                    })
                }

            }
            R.id.settings_item -> {
                startActivity<SettingsActivity>(this){}
            }
        }
        return true
    }
}
