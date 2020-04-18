package com.elouyi.bbs.t

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.elouyi.bbs.R
import com.elouyi.bbs.data.Tiezi
import kotlinx.android.synthetic.main.tiezi_item.view.*

class TieziAdapter(activity : Activity, val resourceId : Int, data : List<Tiezi>) :
    ArrayAdapter<Tiezi>(activity, resourceId, data){

    inner class ViewHolder(val titleText: TextView, val contentText: TextView)

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view : View
        val viewHolder : ViewHolder
        if (convertView == null){
            view = LayoutInflater.from(context).inflate(resourceId, parent, false)
            val titleText : TextView = view.findViewById(R.id.tiezi_title_tiem)
            val contentText : TextView = view.findViewById(R.id.tiezi_content_tiem)
            viewHolder = ViewHolder(titleText,contentText)
            view.tag = viewHolder
        }else{
            view = convertView
            viewHolder = view.tag as ViewHolder
        }

        val tiezi = getItem(position)
        if (tiezi != null){
            viewHolder.titleText.text = tiezi.title
            viewHolder.contentText.text = tiezi.content
        }
        return view
    }
}