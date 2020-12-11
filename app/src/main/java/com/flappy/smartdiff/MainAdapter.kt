package com.flappy.smartdiff

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

/**
 * @FileName: MainAdapter
 * @Author: luweiming
 * @Date: 2020/12/11 14:38
 * @Description:
 * @Version: 1.0
 */
class MainAdapter(manager: FragmentManager,val data: MutableList<Fragment>) :
    FragmentPagerAdapter(manager) {
    override fun getCount(): Int {
        return data.size
    }

    override fun getItem(position: Int): Fragment {
        return data.get(position)
    }
}