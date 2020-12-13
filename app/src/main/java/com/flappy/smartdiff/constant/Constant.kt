package com.flappy.smartdiff.constant

import android.widget.Toast
import com.flappy.smartdiff.bean.User

object Constant {
    var showToast: Toast? = null
    val DEFAULT_USERNAME = "admin"
    val DEFAULT_PWD = "888888"
    var curUser:User? = null
    var isAdmin = false
}