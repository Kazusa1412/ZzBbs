package com.elouyi.bbs.data

import java.io.Serializable

class Tiezi(val title : String, val content : String, val author : String, val createTime : Long,
            var zan : Int, var conment : Int, var id : Int) : Serializable {
}