package com.flappy.smartdiff.view.fragment

import android.view.LayoutInflater
import android.view.View
import com.flappy.smartdiff.base.BaseFragment
import com.flappy.smartdiff.contract.DiffContract
import com.flappy.smartdiff.databinding.FragmentDiffBinding
import com.flappy.smartdiff.presenter.DiffPresenter

/**
 * @FileName: DiffFragment
 * @Author: luweiming
 * @Date: 2020/12/10 17:46
 * @Description:
 * @Version: 1.0
 */
class MaterialsDiffFragment:BaseFragment<DiffPresenter>(),DiffContract.IDiffView {
    private lateinit var binding: FragmentDiffBinding
    override fun initView() {
    }


    override fun createPresenter(): DiffPresenter {
        return DiffPresenter()
    }

    override fun showMaterials() {
    }

    override fun getRootView(inflater: LayoutInflater): View {
        binding = FragmentDiffBinding.inflate(inflater)
        return binding.root
    }

}