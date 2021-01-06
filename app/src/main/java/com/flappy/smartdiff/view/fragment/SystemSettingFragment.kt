package com.flappy.smartdiff.view.fragment

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import com.flappy.smartdiff.Preference
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
import java.util.regex.Matcher
import java.util.regex.Pattern

/**
 * @FileName: SystemSettingFragment
 * @Author: luweiming
 * @Date: 2020/12/10 17:49
 * @Description:
 * @Version: 1.0
 */
class SystemSettingFragment : SettingContract.ISettingView, BaseFragment<SettingPresenter>() {
    private lateinit var binding: FragmentSettingBinding
    private var tcp_IP by Preference(Constant.KEY_TCP_IP, "192.168.10.1")
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

            if(!binding.etPosition.text.isEmpty()){
                tcp_IP = binding.etPosition.text.toString()
            }
            val nameStart = binding.etName.text.toString()
            if(nameStart.length<2||!isNumeric(nameStart.substring(nameStart.length-2,nameStart.length))){
                activity?.toast("命名不合法，请重新输入")
                binding.etName.requestFocus()
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

    /**
     * 利用正则表达式判断字符串是否是数字
     * @param str
     * @return
     */
    fun isNumeric(str: String?): Boolean {
        val pattern: Pattern = Pattern.compile("[0-9]*")
        val isNum: Matcher = pattern.matcher(str)
        return if (!isNum.matches()) {
            false
        } else true
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