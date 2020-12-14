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
import com.flappy.smartdiff.toast
import com.flappy.smartdiff.view.AccountActivity
import com.flappy.smartdiff.view.LoginActivity
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

/**
 * @FileName: SystemSettingFragment
 * @Author: luweiming
 * @Date: 2020/12/10 17:49
 * @Description:
 * @Version: 1.0
 */
class SystemSettingFragment : SettingContract.ISettingView, BaseFragment<SettingPresenter>() {
    private lateinit var binding: FragmentSettingBinding
    override fun getRootView(inflater: LayoutInflater): View {
        binding = FragmentSettingBinding.inflate(inflater)
        return binding.root
    }

    override fun initView() {
        EventBus.getDefault().register(this)

        binding.top.title.text = "系统设定"
        if (Constant.isAdmin) {
            binding.tvAccount.visibility = View.VISIBLE
        } else {
            binding.tvAccount.visibility = View.GONE
        }
        val depot = mPresenter.depot()
        if (null == depot) {
            binding.etCount.setText("")
            binding.etName.setText("")
        } else {
            binding.etCount.setText(depot.count.toString())
            binding.etName.setText(depot.nameStart)
            binding.etPosition.setText(depot.interfacePostion)
        }
        binding.btSave.setOnClickListener {
            if(binding.etName.text.isEmpty()){
                activity?.toast("请输入命名规则")
                return@setOnClickListener
            }
            if (binding.etCount.text.isEmpty()){
                activity?.toast("请输入数量")
                return@setOnClickListener
            }
            val dep: DepotBean = Constant.curUser?.userId?.let { it1 ->
                DepotBean(
                    it1,
                    binding.etName.text.toString(),
                    binding.etPosition.text.toString(),
                    binding.etCount.text.toString().toInt()
                )
            }!!
            if (mPresenter.saveDepot(dep)) {
                showToast("保存成功")
                EventBus.getDefault().post("2222")
            } else {
                showToast("保存失败")
            }
        }
        binding.tvAccount.setOnClickListener {
            val intent = Intent(context, AccountActivity::class.java)
            context?.startActivity(intent)
        }
        binding.tvQuit.setOnClickListener {
            val intent = Intent(activity, LoginActivity::class.java)
            activity?.startActivity(intent)
            activity?.finish()
        }
    }

    @Subscribe
    fun fresh(msg: String) {
    }

    override fun createPresenter(): SettingPresenter {
        return SettingPresenter()
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }
}