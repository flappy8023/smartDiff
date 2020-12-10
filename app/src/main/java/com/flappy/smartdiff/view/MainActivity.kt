package com.flappy.smartdiff.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.flappy.smartdiff.R
import com.flappy.smartdiff.base.BaseFragment
import com.flappy.smartdiff.base.BasePresenter

class MainActivity : AppCompatActivity() {
    val fragments = mutableListOf<BaseFragment<*>>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}