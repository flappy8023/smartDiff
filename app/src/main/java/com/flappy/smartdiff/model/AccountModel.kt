package com.flappy.smartdiff.model

import com.flappy.smartdiff.bean.User
import com.flappy.smartdiff.contract.AccountContract
import com.flappy.smartdiff.dao.UserDao
import com.flappy.smartdiff.db.MyDatabase

/**
 * @FileName: AccountModel
 * @Author: luweiming
 * @Date: 2020/12/11 17:05
 * @Description:
 * @Version: 1.0
 */
class AccountModel:AccountContract.IAccountModel {
    val dao:UserDao = MyDatabase.instance.userDao()
    override fun getUsers(): List<User> {
        return dao.getAll()
    }

    override fun addUser(user: User) {
        dao.insertUser(user)
    }

    fun userExist(user: User):Boolean{
        return dao.getUserByName(user.userName)!=null
    }
    override fun update(user: User) {
        dao.upate(user)
    }

    override fun delete(user: User) {
        dao.delete(user)
    }
}