package com.flappy.smartdiff.view.fragment

import android.R
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.flappy.smartdiff.Preference
import com.flappy.smartdiff.base.BaseFragment
import com.flappy.smartdiff.bean.MaterialBean
import com.flappy.smartdiff.constant.Constant
import com.flappy.smartdiff.contract.DiffSetContract
import com.flappy.smartdiff.databinding.FragmentMaterSetLayoutBinding
import com.flappy.smartdiff.presenter.DiffSetPresenter
import com.flappy.smartdiff.toast
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

/**
 * @FileName: MaterialsSetFragment
 * @Author: luweiming
 * @Date: 2020/12/10 17:49
 * @Description:
 * @Version: 1.0
 */
class MaterialsSetFragment : DiffSetContract.IDiffSetView, BaseFragment<DiffSetPresenter>() {
    val data = mutableListOf<MaterialBean>()
    val strings = mutableListOf<String>()
    var curPosition = 0
    var adapter: ArrayAdapter<String>? = null
    private var numberStart by Preference(Constant.KEY_NUMBER_START, 0)
    private var numberEnd by Preference(Constant.KEY_NUMBER_END, 0)
    private var pihaoStart by Preference(Constant.KEY_PIHAO_START, 0)
    private var pihaoEnd by Preference(Constant.KEY_PIHAO_END, 0)
    private lateinit var binding: FragmentMaterSetLayoutBinding
    override fun getRootView(inflater: LayoutInflater): View {
        binding = FragmentMaterSetLayoutBinding.inflate(inflater)
        return binding.root
    }

    override fun initView() {
        EventBus.getDefault().register(this)
        binding.top.title.text = "物料设定"
        val materials = mPresenter.getMaterials();
        if (materials.isEmpty()) {
            strings.add("暂未设置")
        } else {
            data.addAll(materials)
            data.forEach {
                strings.add(it.id)
            }
            binding.tvOldName.setText(data.get(0).name)
            binding.tvOldNumber.setText(data.get(0).number)
            binding.tvOldPihao.setText(data.get(0).pihao)
        }
        adapter =
            this.context?.let { ArrayAdapter<String>(it, R.layout.simple_spinner_item, strings) }
        adapter?.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
        binding.spinner.adapter = adapter
        binding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                if (!data.isEmpty()) {
                    curPosition = p2
                    binding.tvOldName.setText(data.get(p2).name)
                    binding.tvOldNumber.setText(data.get(p2).number)
                    binding.tvOldPihao.setText(data.get(p2).pihao)
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }

        }
        binding.btBind.setOnClickListener {
            if (!data.isEmpty()) {
                var number = binding.etNewNumber.text.toString()
//                if (numberStart <= number.length && numberEnd <= number.length) {
//                    if (0 != numberEnd && 0 != numberStart) {
//                        number = number.substring(numberStart-1, numberEnd)
//                    }
//                }
                var pihao = number
                if (pihaoStart <= number.length && pihaoEnd <= number.length) {
                    if (0 != pihaoEnd && 0 != pihaoStart) {
                        pihao = number.substring(pihaoStart-1, pihaoEnd)
                    }
                }
                data.get(curPosition).pihao = pihao
                data.get(curPosition).number = number
                data.get(curPosition).name = binding.etNewName.text.toString()

                mPresenter.updateMaterial(data.get(curPosition))
                binding.tvOldName.text = data.get(curPosition).name
                binding.tvOldNumber.text = data.get(curPosition).number
                binding.etNewName.setText("")
                binding.etNewNumber.setText("")
                EventBus.getDefault().post("1111")
            } else {
                context?.toast("请先设置料仓")
            }
        }
    }

    override fun createPresenter(): DiffSetPresenter {
        return DiffSetPresenter()
    }

    @Subscribe
    fun refresh(msg: String) {
        if (msg.equals("1111")) {
            data.clear()
            strings.clear()
            data.addAll(mPresenter.getMaterials())
            data.forEach {
                strings.add(it.id)
            }
            adapter?.notifyDataSetChanged()
        }
        if (msg.equals("2222")) {
            binding.etNewNumber.setText("")
            binding.etNewName.setText("")
            binding.tvOldNumber.setText("")
            binding.tvOldName.setText("")
            data.clear()
            strings.clear()
            data.addAll(mPresenter.getMaterials())
            data.forEach {
                strings.add(it.id)
            }
            adapter?.notifyDataSetChanged()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }
}