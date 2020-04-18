package com.elouyi.bbs.t

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.elouyi.bbs.R
import com.elouyi.bbs.data.Comment


class CommentAdapter(activity: Activity,val resourceId :Int,data : List<Comment>) :
        ArrayAdapter<Comment>(activity,resourceId,data){

    inner class ViewHolder(val t1:TextView,val t2:TextView,val t3:TextView)

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view : View
        val viewHolder: ViewHolder
        if (convertView == null){
            view = LayoutInflater.from(context).inflate(resourceId,parent,false)
            val t1 : TextView = view.findViewById(R.id.textCommentName)
            val t2 : TextView = view.findViewById(R.id.textCommentTime)
            val t3 : TextView = view.findViewById(R.id.textCommentContent)
            viewHolder = ViewHolder(t1,t2, t3)
            view.tag = viewHolder
        }else{
            view = convertView
            viewHolder = view.tag as ViewHolder
        }
        val comment = getItem(position)
        if (comment != null){
            viewHolder.t1.text = comment.author
            viewHolder.t2.text = getTime(comment.createTime)
            viewHolder.t3.text = comment.content
        }
        return view
    }

}