package com.flappy.smartdiff.view.fragment

import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.flappy.smartdiff.base.BaseFragment
import com.flappy.smartdiff.bean.MaterialBean
import com.flappy.smartdiff.contract.DiffContract
import com.flappy.smartdiff.databinding.FragmentDiffBinding
import com.flappy.smartdiff.presenter.DiffPresenter
import com.flappy.smartdiff.toast
import kotlinx.android.synthetic.main.fragment_diff.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

/**
 * @FileName: DiffFragment
 * @Author: luweiming
 * @Date: 2020/12/10 17:46
 * @Description:
 * @Version: 1.0
 */
class MaterialsDiffFragment : BaseFragment<DiffPresenter>(), DiffContract.IDiffView {
    private lateinit var binding: FragmentDiffBinding
    val data = mutableListOf<String>()
    var materials = mutableListOf<MaterialBean>()
    var adapter: ArrayAdapter<String>? = null
    var curPosition = 0
    override fun initView() {
        EventBus.getDefault().register(this)

        binding.top.title.text = "物料比对"
        materials.addAll(mPresenter.getMaterials())
        if (materials.isEmpty()) {
            data.add("暂未设置")
        } else {
            materials.forEach {
                data.add(it.id)
            }
        }
        adapter = this.context?.let {
            ArrayAdapter<String>(
                it,
                android.R.layout.simple_spinner_item,
                data
            )
        }
        adapter?.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinner.adapter = adapter
        binding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                if (!materials.isEmpty())
                    if(curPosition!=p2){
                        binding.etNumber.setText("")
                    }
                    curPosition = p2
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }

        }
        binding.btCompare.setOnClickListener {
            if(binding.etNumber.text.isEmpty()){
                activity?.toast("请输入号码")
                return@setOnClickListener
            }
            if (materials.size > curPosition) {
                if (materials.get(curPosition).number.equals(binding.etNumber.text.toString())) {
                    activity?.toast("比对成功")
                } else {
                    activity?.toast("比对失败")
                }
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

    @Subscribe
    fun refresh(s: String) {
        if (s.equals("1111")||s.equals("2222")) {
            binding.etNumber.setText("")
            data.clear()
            materials.clear()
            materials.addAll(mPresenter.getMaterials())
            materials.forEach {
                data.add(it.id)
            }
            adapter?.notifyDataSetChanged()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }

}