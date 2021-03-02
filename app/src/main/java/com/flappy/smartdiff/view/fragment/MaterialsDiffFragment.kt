package com.flappy.smartdiff.view.fragment

import android.app.Activity
import android.app.AlertDialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.flappy.smartdiff.Preference
import com.flappy.smartdiff.R
import com.flappy.smartdiff.base.BaseFragment
import com.flappy.smartdiff.bean.MaterialBean
import com.flappy.smartdiff.constant.Constant
import com.flappy.smartdiff.contract.DiffContract
import com.flappy.smartdiff.databinding.FragmentDiffBinding
import com.flappy.smartdiff.presenter.DiffPresenter
import com.flappy.smartdiff.toast
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
    private var numberStart by Preference(Constant.KEY_NUMBER_START, 1)
    private var numberEnd by Preference(Constant.KEY_NUMBER_END, 14)
    private var pihaoStart by Preference(Constant.KEY_PIHAO_START, 21)
    private var pihaoEnd by Preference(Constant.KEY_PIHAO_END, 26)
    var curPosition = 0
    override fun initView() {
        EventBus.getDefault().register(this)
        binding.top.title.text = "物料比对"
        materials.addAll(mPresenter.getMaterials())
        if (!materials.isEmpty() && materials.size > curPosition) {
            binding.tvName.setText(materials.get(curPosition).name)
        }
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
                R.layout.simple_spinner_item,
                data
            )
        }
        adapter?.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinner.adapter = adapter
        binding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                if (!materials.isEmpty())
                    if (curPosition != p2) {
                        binding.etNumber.setText("")
                    }
                if (!materials.isEmpty() && materials.size > p2) {
                    binding.tvName.setText(materials.get(p2).name)
                }
                curPosition = p2
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }

        }
        binding.etNumber.setOnClickListener {
            binding.etNumber.selectAll()
        }
        binding.btCompare.setOnClickListener {
            if (binding.etNumber.text.isEmpty()) {
                activity?.toast("请输入号码")
                return@setOnClickListener
            }
            var number = binding.etNumber.text.toString()
            var realNumber = number
            if (numberStart <= number.length && numberEnd <= number.length) {
                if (0 != numberEnd && 0 != numberEnd) {
                    realNumber = number.substring(numberStart - 1, numberEnd)
                }
            }

            binding.etNumber.selectAll()
            if (materials.size > curPosition) {
                if (materials.get(curPosition).number.startsWith(realNumber)) {
                    mPresenter.openLock(curPosition)

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

    override fun openSucc() {

        activity?.runOnUiThread {
            var number = binding.etNumber.text.toString()
            var pihao = number
            if(pihaoStart<=number.length&&numberEnd<=number.length){
                if(pihaoStart!=0&&pihaoEnd!=0){
                    pihao.substring(pihaoStart-1,pihaoEnd)
                }
            }
            mPresenter.sendPihao(curPosition, pihao)
            if (dialog == null)
                dialog = AlertDialog.Builder(activity).setTitle("提示").setMessage("开启成功")
                    .setIcon(R.drawable.done).create()
            dialog?.show()
            binding.root.postDelayed({
                dialog?.dismiss()
            }, 1000)
        }
    }

    override fun getMyContext(): Context? {
        return context
    }

    private var dialog: AlertDialog? = null
    override fun getRootView(inflater: LayoutInflater): View {
        binding = FragmentDiffBinding.inflate(inflater)
        return binding.root
    }

    @Subscribe
    fun refresh(s: String) {
        if (s.equals("1111") || s.equals("2222")) {
            binding.etNumber.setText("")
            data.clear()
            materials.clear()
            materials.addAll(mPresenter.getMaterials())
            if (!materials.isEmpty() && materials.size > curPosition) {
                binding.tvName.setText(materials.get(curPosition).name)
            }
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