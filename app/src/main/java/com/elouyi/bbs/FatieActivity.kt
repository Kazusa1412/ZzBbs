package com.elouyi.bbs

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import com.elouyi.bbs.data.User
import com.elouyi.bbs.t.*
import com.elouyi.elylib.ElyActivity
import com.elouyi.elylib.md5
import com.elouyi.elylib.showToast
import kotlinx.android.synthetic.main.activity_fatie.*
import kotlinx.android.synthetic.main.toolbar.*
import okhttp3.FormBody
import java.lang.Exception

class FatieActivity : ElyActivity() {

    var bt = ""
    var nr = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fatie)
        setSupportActionBar(toolbar)
        title = "写帖子"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun initv(){

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.fatie,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.fatie_tiem -> {
                bt = editBiaoti.text.toString()
                nr = editContent.text.toString()
                if (bt.length < 2){
                    "标题至少要有2个字符".showToast(this)
                    return true
                }
                if (nr.length < 2){
                    "内容至少要有两个字符".showToast(this)
                    return true
                }
                checkLogin(this,object :LoginCallback{
                    override fun isLogin(user: User) {
                        val url = "http://120.26.174.247/api/fatie/"
                        val t = md5(bt+nr+user.username+"ebbs")
                        val body = FormBody.Builder()
                            .add("title",bt)
                            .add("content",nr)
                            .add("username", user.username)
                            .add("t",t)
                            .build()
                        httpPost(url,body,object : HttpCallback{
                            override fun onFinish(response: String) {
                                Log.e("这里是发帖post的哦那finish：",response)
                                runOnUiThread {
                                    if (response.startsWith("ok")){
                                        val intent = Intent()
                                        intent.putExtra("data_return","ok")
                                        setResult(Activity.RESULT_OK,intent)
                                        AlertDialog.Builder(this@FatieActivity).apply {
                                            setTitle("发布成功")
                                            setMessage("!!!!!")
                                            setCancelable(false)
                                            setPositiveButton("确定"){_,_ ->
                                                finish()
                                            }
                                            show()
                                        }
                                    }
                                }

                            }

                            override fun onError(exception: Exception) {
                                Log.e("这里是发帖post的onerror：", exception.toString())
                            }
                        })
                    }

                    override fun notLogin(msg: String) {
                        runOnUiThread {
                            "还未登录啊: $msg ".showToast(this@FatieActivity)
                            if (msg.startsWith("-2")){
                                "本地账号数据与服务器冲突,请重新登录".showToast(this@FatieActivity)
                                logOut(this@FatieActivity)
                                val intent = Intent()
                                intent.putExtra("data_return","not_login")
                                setResult(Activity.RESULT_OK,intent)
                            }
                        }
                    }
                })
            }
            android.R.id.home -> {
                finish()
            }
        }
        return true
    }
}
