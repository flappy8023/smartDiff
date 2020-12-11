package com.flappy.smartdiff.contract

import com.flappy.smartdiff.base.IBaseModel
import com.flappy.smartdiff.base.IBaseView

/**
 * @FileName: DiffSetContract
 * @Author: luweiming
 * @Date: 2020/12/11 14:10
 * @Description:
 * @Version: 1.0
 */
interface DiffSetContract {
    interface IDiffSetView:IBaseView{}
    interface IDiffSetMoel:IBaseModel{}
    interface IDiffSetPreseneter{}
}