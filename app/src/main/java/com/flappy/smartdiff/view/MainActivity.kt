package com.flappy.smartdiff.view

import android.content.Intent
import android.view.View
import androidx.fragment.app.Fragment
import com.flappy.smartdiff.MainAdapter
import com.flappy.smartdiff.R
import com.flappy.smartdiff.base.BaseActivity
import com.flappy.smartdiff.contract.MainContract
import com.flappy.smartdiff.databinding.ActivityMainBinding
import com.flappy.smartdiff.presenter.MainPreseneter
import com.flappy.smartdiff.view.fragment.MaterialsDiffFragment
import com.flappy.smartdiff.view.fragment.MaterialsSetFragment
import com.flappy.smartdiff.view.fragment.SystemSettingFragment

class MainActivity : MainContract.IMainView, BaseActivity<MainPreseneter>() {
    val fragments = mutableListOf<Fragment>()
    private lateinit var binding: ActivityMainBinding
    override fun getContentView(): View {
        binding = ActivityMainBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun initView() {
        initScanerMode()
        fragments.run {
            add(MaterialsDiffFragment());
            add(MaterialsSetFragment())
            add(SystemSettingFragment())
        }
        binding.viewpager.offscreenPageLimit =3
        binding.viewpager.adapter = MainAdapter(supportFragmentManager, fragments)
        binding.bottomView.setOnNavigationItemSelectedListener {
            return@setOnNavigationItemSelectedListener when (it.itemId) {
                R.id.item_tab1 -> {
                    binding.viewpager.currentItem = 0
                    true
                }
                R.id.item_tab2 -> {
                    binding.viewpager.currentItem = 1
                    true
                }
                R.id.item_tab3 -> {
                    binding.viewpager.currentItem = 2
                    true
                }
                else -> {
                    true
                }

            }
        }
    }
    private fun initScanerMode() {
        //启动广播输出模式
        val intent = Intent("ACTION_BAR_SCANCFG")
        intent.putExtra("EXTRA_SCAN_MODE", 3)
        intent.putExtra("EXTRA_SCAN_AUTOENT", 0)
        sendBroadcast(intent)
    }

    override fun createPreseneter(): MainPreseneter {
        return MainPreseneter()
    }
}