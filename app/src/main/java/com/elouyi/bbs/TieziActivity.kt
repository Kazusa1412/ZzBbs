package com.elouyi.bbs

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.elouyi.bbs.data.Comment
import com.elouyi.bbs.data.Tiezi
import com.elouyi.bbs.t.*
import com.elouyi.elylib.ElyActivity
import com.elouyi.elylib.md5
import com.elouyi.elylib.showToast
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_tiezi.*
import okhttp3.FormBody
import java.lang.Exception

class TieziActivity : ElyActivity() {

    private lateinit var tiezi : Tiezi
    private lateinit var adapter : CommentAdapter
    private val comments = ArrayList<Comment>()

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tiezi)
        tiezi = intent.getSerializableExtra("tiezi") as Tiezi
        Log.e(tag,tiezi.title+tiezi.author)
        val title = tiezi.title
        val content =  tiezi.content
        textviewFf.text = "by ${tiezi.author} at ${getTime(tiezi.createTime)}"
        setSupportActionBar(titleToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        collapsingToolbar.title = title
        tieziContentText.text = content
        initv()
        getComment()
    }

    private fun getComment(){
        val from = tiezi.id
        val t = md5(from.toString() + "ebbs")
        val url = "http://120.26.174.247/api/getcomment/"
        val body = FormBody.Builder()
            .add("from",from.toString())
            .add("t",t)
            .build()
        httpPost(url,body,object : HttpCallback{
            override fun onFinish(response: String) {
                Log.e("这里是获取评论的finish回调: ",response)
                runOnUiThread {
                    if (response.startsWith("[") && response.length > 2){
                        val typeOf = object : TypeToken<List<Comment>>(){}.type
                        val gson = Gson()
                        val coms = gson.fromJson<List<Comment>>(response,typeOf)
                        comments.clear()
                        for (c in coms){
                            comments.add(c)
                        }
                        textNoComment.visibility = View.GONE
                        adapter.notifyDataSetChanged()
                    }else if (response == "[]"){
                        textNoComment.visibility = View.VISIBLE
                    }else{
                        "错误 ：$response".showToast(this@TieziActivity)
                    }
                }
            }

            override fun onError(exception: Exception) {
                Log.e("这里是获取评论的err回调: ",exception.toString())
                runOnUiThread {
                    "发送错误： ${exception.toString()}".showToast(this@TieziActivity,Toast.LENGTH_LONG)
                }
            }
        })
    }

    private fun initv(){
        adapter = CommentAdapter(this,R.layout.comment_item,comments)
        listComment.adapter = adapter

        btm_comment.setOnClickListener {
            if(checkTExist(this)){
                val content = edit_comment.text.toString()
                if (content.length<2){
                    "评论至少两个字哦>>".showToast(this)
                    return@setOnClickListener
                }
                val user = getUserFromShare(this)
                val t = md5(tiezi.id.toString() + content + user.username + "ebbs")
                val url = "http://120.26.174.247/api/sendcomment/"
                val body = FormBody.Builder()
                    .add("from",tiezi.id.toString())
                    .add("content",content)
                    .add("author",user.username)
                    .add("t",t)
                    .build()
                httpPost(url,body,object : HttpCallback{
                    override fun onFinish(response: String) {
                        Log.e("这里是发评论的onfinish: ",response)
                        runOnUiThread{
                            if (response.startsWith("ok")){
                                edit_comment.setText("")
                                AlertDialog.Builder(this@TieziActivity).apply {
                                    setTitle("发送成功")
                                    setMessage("<<>>><")
                                    setCancelable(true)
                                    setPositiveButton("确定"){_,_ ->}
                                    show()
                                }
                                getComment()
                            }
                        }
                    }

                    override fun onError(exception: Exception) {
                        Log.e("这里是发评论的onerr: ",exception.toString())
                    }
                })
            }else{
                "登录才能评论哦".showToast(this)
            }

        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home -> {
                finish()
                return  true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
