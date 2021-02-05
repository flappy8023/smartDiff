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
    private var pihao_IP by Preference(Constant.KEY_PIHAO_IP,"192.168.1.10")
    private var numberStart by Preference(Constant.KEY_NUMBER_START, 1)
    private var numberEnd by Preference(Constant.KEY_NUMBER_END, 14)
    private var pihaoStart by Preference(Constant.KEY_PIHAO_START, 21)
    private var pihaoEnd by Preference(Constant.KEY_PIHAO_END, 26)
    override fun getRootView(inflater: LayoutInflater): View {
        binding = FragmentSettingBinding.inflate(inflater)
        return binding.root
    }

    override fun initView() {
        EventBus.getDefault().register(this)
        binding.etPosition.setText(tcp_IP)
        binding.top.title.text = "系统设定"
        binding.etNumberStart.setText(numberStart.toString())
        binding.etNumberEnd.setText(numberEnd.toString())
        binding.etPihaoEnd.setText(pihaoEnd.toString())
        binding.etPihaoStart.setText(pihaoStart.toString())
        binding.etPihaoPosition.setText(pihao_IP)
        if (Constant.isAdmin) {
            binding.btSave.visibility = View.VISIBLE
            binding.etName.isEnabled = true
            binding.etCount.isEnabled = true
            binding.etPosition.isEnabled = true
            binding.llAccount.visibility = View.VISIBLE
            binding.etPihaoPosition.isEnabled = true
            binding.etNumberStart.isEnabled = true
            binding.etNumberEnd.isEnabled = true
            binding.etPihaoStart.isEnabled = true
            binding.etPihaoEnd.isEnabled = true
        } else {
            binding.btSave.visibility = View.GONE
            binding.etName.isEnabled = false
            binding.etCount.isEnabled = false
            binding.etPosition.isEnabled = false
            binding.llAccount.visibility = View.GONE
            binding.etPihaoPosition.isEnabled = false
            binding.etNumberStart.isEnabled = false
            binding.etNumberEnd.isEnabled = false
            binding.etPihaoStart.isEnabled = false
            binding.etPihaoEnd.isEnabled = false
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
            if (binding.etName.text.isEmpty()) {
                activity?.toast("请输入命名规则")
                return@setOnClickListener
            }
            if (binding.etCount.text.isEmpty()) {
                activity?.toast("请输入数量")
                return@setOnClickListener
            }

            if (!binding.etPosition.text.isEmpty()) {
                tcp_IP = binding.etPosition.text.toString()
            }
            if(!binding.etPihaoPosition.text.isEmpty()){
                pihao_IP = binding.etPihaoPosition.text.toString()
            }
            val nameStart = binding.etName.text.toString()
            if (nameStart.length < 2 || !isNumeric(
                    nameStart.substring(
                        nameStart.length - 2,
                        nameStart.length
                    )
                )
            ) {
                activity?.toast("命名不合法，请重新输入")
                binding.etName.requestFocus()
                return@setOnClickListener
            }
            if (!binding.etNumberStart.text.isEmpty() && !binding.etNumberEnd.text.isEmpty() && binding.etNumberStart.text.toString()
                    .toInt() > binding.etNumberEnd.text.toString().toInt()
            ) {
                activity?.toast("条码起止位置设置错误")
                return@setOnClickListener
            }
            if (!binding.etPihaoStart.text.isEmpty() && !binding.etPihaoEnd.text.isEmpty() && binding.etPihaoStart.text.toString()
                    .toInt() > binding.etPihaoEnd.text.toString().toInt()
            ) {
                activity?.toast("条码起止位置设置错误")
                return@setOnClickListener
            }
            numberStart =
                if (binding.etNumberStart.text.isEmpty()) 0 else binding.etNumberStart.text.toString()
                    .toInt()
            numberEnd =
                if (binding.etNumberEnd.text.isEmpty()) 0 else binding.etNumberEnd.text.toString()
                    .toInt()
            pihaoStart =
                if (binding.etPihaoStart.text.isEmpty()) 0 else binding.etPihaoStart.text.toString()
                    .toInt()
            pihaoEnd =
                if (binding.etPihaoEnd.text.isEmpty()) 0 else binding.etPihaoEnd.text.toString()
                    .toInt()
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
        binding.llAccount.setOnClickListener {
            val intent = Intent(context, AccountActivity::class.java)
            context?.startActivity(intent)
        }
        binding.llQuit.setOnClickListener {
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