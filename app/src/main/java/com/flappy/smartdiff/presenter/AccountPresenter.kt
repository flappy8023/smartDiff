package com.flappy.smartdiff.presenter

import com.flappy.smartdiff.base.BasePresenter
import com.flappy.smartdiff.bean.User
import com.flappy.smartdiff.contract.AccountContract
import com.flappy.smartdiff.model.AccountModel

/**
 * @FileName: AccountPresenter
 * @Author: luweiming
 * @Date: 2020/12/11 17:05
 * @Description:
 * @Version: 1.0
 */
class AccountPresenter:AccountContract.IAccountPresenter ,BasePresenter<AccountContract.IAccountView,AccountModel>(){
    override fun createModel(): AccountModel {
        return AccountModel()
    }

    override fun getUsers(): List<User> {
        return mModel.getUsers()
    }

    override fun addUser(user: User) {
        if (mModel.userExist(user)){
            mView?.showToast("用户已存在")
        }else{
            mModel.addUser(user)
            mView?.showAccounts(getUsers())
        }
    }

    override fun update(user: User) {
        mModel.update(user)
        mView?.showAccounts(getUsers())
    }

    override fun delete(user: User) {
        mModel.delete(user)
        mView?.showAccounts(getUsers())
    }
}