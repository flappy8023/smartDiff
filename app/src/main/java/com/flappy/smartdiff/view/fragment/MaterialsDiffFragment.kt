package com.flappy.smartdiff.view.fragment

import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.flappy.smartdiff.base.BaseFragment
import com.flappy.smartdiff.contract.DiffContract
import com.flappy.smartdiff.databinding.FragmentDiffBinding
import com.flappy.smartdiff.presenter.DiffPresenter
import kotlinx.android.synthetic.main.fragment_diff.*

/**
 * @FileName: DiffFragment
 * @Author: luweiming
 * @Date: 2020/12/10 17:46
 * @Description:
 * @Version: 1.0
 */
class MaterialsDiffFragment:BaseFragment<DiffPresenter>(),DiffContract.IDiffView {
    private lateinit var binding: FragmentDiffBinding
    val data = mutableListOf<String>()
    override fun initView() {
        binding.top.title.text = "物料比对"
        val materials = mPresenter.getMaterials()
        if(materials.isEmpty()){
            data.add("暂未设置")
        }else{
            data.addAll(materials)
        }
        val adapter = this.context?.let { ArrayAdapter<String>(it,android.R.layout.simple_spinner_item,data) }
        adapter?.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinner.adapter = adapter
        binding.spinner.onItemSelectedListener = object:AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }

        }
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