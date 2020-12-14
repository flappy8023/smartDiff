package com.flappy.smartdiff.view

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
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : MainContract.IMainView, BaseActivity<MainPreseneter>() {
    val fragments = mutableListOf<Fragment>()
    private lateinit var binding: ActivityMainBinding
    override fun getContentView(): View {
        binding = ActivityMainBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun initView() {
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

    override fun createPreseneter(): MainPreseneter {
        return MainPreseneter()
    }
}