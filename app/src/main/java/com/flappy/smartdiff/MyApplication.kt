package com.flappy.smartdiff

import android.app.Application
import android.content.Context
import com.flappy.smartdiff.bean.User
import com.flappy.smartdiff.constant.Constant
import com.flappy.smartdiff.db.MyDatabase

class MyApplication : Application() {
    companion object {
        lateinit var mContext: Context
    }

    override fun onCreate() {
        super.onCreate()
        mContext = this
        val userDao = MyDatabase.instance.userDao()
        if (userDao.getAll().isEmpty()) {
            val user: User = User(Constant.DEFAULT_USERNAME, Constant.DEFAULT_PWD)
            userDao.insertUser(user)
        }
    }
}