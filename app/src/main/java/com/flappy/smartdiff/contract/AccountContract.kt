package com.flappy.smartdiff.contract

import com.flappy.smartdiff.base.IBaseModel
import com.flappy.smartdiff.base.IBaseView
import com.flappy.smartdiff.bean.User

/**
 * @FileName: AccountContract
 * @Author: luweiming
 * @Date: 2020/12/11 17:02
 * @Description:
 * @Version: 1.0
 */
interface AccountContract {
    interface IAccountView:IBaseView{
        fun showAccounts(users:List<User>)
    }
    interface IAccountModel:IBaseModel{
        fun getUsers():List<User>
        fun addUser(user: User)
        fun update(user: User)
        fun delete(user: User)
    }
    interface IAccountPresenter{
        fun getUsers():List<User>
        fun addUser(user: User)
        fun update(user: User)
        fun delete(user: User)
    }
}