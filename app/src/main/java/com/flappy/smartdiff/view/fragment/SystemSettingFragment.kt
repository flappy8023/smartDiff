package com.flappy.smartdiff.view.fragment

import android.content.Intent
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import com.flappy.smartdiff.base.BaseFragment
import com.flappy.smartdiff.bean.DepotBean
import com.flappy.smartdiff.constant.Constant
import com.flappy.smartdiff.contract.SettingContract
import com.flappy.smartdiff.databinding.FragmentSettingBinding
import com.flappy.smartdiff.presenter.SettingPresenter
import com.flappy.smartdiff.view.AccountActivity

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
        binding.top.title.text = "系统设定"
        if(Constant.isAdmin){
            binding.tvAccount.visibility = View.VISIBLE
        }else{
            binding.tvAccount.visibility = View.GONE
        }
        val depot = mPresenter.depot()
        if(null == depot){
            binding.etCount.setText("")
            binding.etName.setText("")
        }else{
            binding.etCount.setText(depot.count.toString())
            binding.etName.setText(depot.nameStart)
            binding.etPosition.setText(depot.interfacePostion)
        }
        binding.btSave.setOnClickListener {
            val dep:DepotBean = Constant.curUser?.userId?.let { it1 -> DepotBean(it1,binding.etName.text.toString(),binding.etPosition.text.toString(),binding.etCount.text.toString().toInt()) }!!
            if(mPresenter.saveDepot(dep)){
                showToast("保存成功")
            }else{
                showToast("保存失败")
            }
        }
        binding.tvAccount.setOnClickListener{
            val intent = Intent(context,AccountActivity::class.java)
            context?.startActivity(intent)
        }
    }

    override fun createPresenter(): SettingPresenter {
        return SettingPresenter()
    }
}