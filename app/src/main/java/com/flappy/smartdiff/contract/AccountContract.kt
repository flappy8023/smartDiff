package com.flappy.smartdiff.contract

import com.flappy.smartdiff.base.IBaseModel
import com.flappy.smartdiff.base.IBaseView

/**
 * @FileName: AccountContract
 * @Author: luweiming
 * @Date: 2020/12/11 17:02
 * @Description:
 * @Version: 1.0
 */
interface AccountContract {
    interface IAccountView:IBaseView{}
    interface IAccountModel:IBaseModel{}
    interface IAccountPresenter{}
}