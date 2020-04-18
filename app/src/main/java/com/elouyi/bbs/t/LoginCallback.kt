package com.elouyi.bbs.t

import com.elouyi.bbs.data.User

interface LoginCallback {
    fun isLogin(user:User)
    fun notLogin(msg:String)
}