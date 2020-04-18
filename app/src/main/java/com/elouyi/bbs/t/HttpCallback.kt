package com.elouyi.bbs.t

import java.lang.Exception

interface HttpCallback {
    fun onFinish(response: String)
    fun onError(exception: Exception)
}
