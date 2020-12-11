package com.flappy.smartdiff.presenter

import com.flappy.smartdiff.base.BasePresenter
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
}