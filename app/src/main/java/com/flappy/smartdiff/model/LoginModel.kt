package com.flappy.smartdiff.model

import com.flappy.smartdiff.bean.User
import com.flappy.smartdiff.contract.LoginContract
import com.flappy.smartdiff.dao.UserDao
import com.flappy.smartdiff.db.MyDatabase

class LoginModel:LoginContract.ILoginModel {
    val userDao:UserDao = MyDatabase.instance.userDao()

    fun userExist(name: String):Boolean{
      return userDao.getUserByName(name) != null
    }
    fun getUserByName(name: String) = userDao.getUserByName(name)
    fun save(user: User):Long{
        return userDao.insertUser(user)
    }
}