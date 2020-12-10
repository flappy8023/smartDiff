package com.flappy.smartdiff.base

abstract class BasePresenter<V : IBaseView, M : IBaseModel> {
    var mView: V? = null
    val mModel: M by lazy { createModel() }
    fun attachView(view: IBaseView) {
        mView = view as V
    }

    fun detachView() {
        mView = null
    }

    abstract fun createModel(): M
    fun isViewAttached(): Boolean = mView != null
}