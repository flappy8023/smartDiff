package com.flappy.smartdiff.view.fragment

import android.view.LayoutInflater
import android.view.View
import com.flappy.smartdiff.base.BaseFragment
import com.flappy.smartdiff.contract.DiffSetContract
import com.flappy.smartdiff.databinding.FragmentMaterSetLayoutBinding
import com.flappy.smartdiff.presenter.DiffSetPresenter

/**
 * @FileName: MaterialsSetFragment
 * @Author: luweiming
 * @Date: 2020/12/10 17:49
 * @Description:
 * @Version: 1.0
 */
class MaterialsSetFragment:DiffSetContract.IDiffSetView,BaseFragment<DiffSetPresenter>() {
    private lateinit var binding:FragmentMaterSetLayoutBinding
    override fun getRootView(inflater: LayoutInflater): View {
        binding = FragmentMaterSetLayoutBinding.inflate(inflater)
        return binding.root
    }

    override fun initView() {
    }

    override fun createPresenter(): DiffSetPresenter {
        return DiffSetPresenter()
    }
}