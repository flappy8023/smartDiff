package com.flappy.smartdiff.constant

import android.widget.Toast
import com.flappy.smartdiff.bean.User

object Constant {
    val SHARED_NAME: String = "diff"
    var showToast: Toast? = null
    val DEFAULT_USERNAME = "admin"
    val DEFAULT_PWD = "888888"
    var curUser:User? = null
    var isAdmin = false
    const val KEY_REMERMBER = "remember"
    const val KEY_USERNAME = "username"
    const val KEY_PWD = "pwd"
    const val KEY_TCP_IP = "TCP_IP"
    const val KEY_PIHAO_IP = "pihao_IP"
    const val KEY_NUMBER_START = "number_start"
    const val KEY_NUMBER_END = "number_end"
    const val KEY_PIHAO_START = "pihao_start"
    const val KEY_PIHAO_END = "pihao_end"
}