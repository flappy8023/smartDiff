package com.flappy.smartdiff.view.fragment

import android.view.LayoutInflater
import android.view.View
import com.flappy.smartdiff.base.BaseFragment
import com.flappy.smartdiff.contract.SettingContract
import com.flappy.smartdiff.databinding.FragmentSettingBinding
import com.flappy.smartdiff.presenter.SettingPresenter

/**
 * @FileName: SystemSettingFragment
 * @Author: luweiming
 * @Date: 2020/12/10 17:49
 * @Description:
 * @Version: 1.0
 */
class SystemSettingFragment:SettingContract.ISettingView,BaseFragment<SettingPresenter>() {
    private lateinit var binding:FragmentSettingBinding
    override fun getRootView(inflater: LayoutInflater): View {
        binding = FragmentSettingBinding.inflate(inflater)
        return binding.root
    }

    override fun initView() {
    }

    override fun createPresenter(): SettingPresenter {
        return SettingPresenter()
    }
}