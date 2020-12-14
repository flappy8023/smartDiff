package com.flappy.smartdiff

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.WindowManager
import com.flappy.smartdiff.bean.User
import com.flappy.smartdiff.databinding.DialogLayoutBinding

class MyDialog(context: Context,val type:Int):Dialog(context) {
    var user:User? = null
    lateinit var binding:DialogLayoutBinding
    var iSave:ISave? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=DialogLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val param=window?.attributes
        param?.width = WindowManager.LayoutParams.MATCH_PARENT
        param?.height = WindowManager.LayoutParams.WRAP_CONTENT
        window?.attributes = param
        binding.title.text = if (0==type) "新增用户" else "修改用户"
        if(1 ==type){
            binding.etName.setText(user?.userName)
            binding.etPwd.setText(user?.pwd)
            binding.etName.isEnabled = false
        }
        binding.btSave.setOnClickListener {
            iSave?.onSave(binding.etName.text.toString(),binding.etPwd.text.toString())
        }
    }
    interface ISave{
        fun onSave(name:String,pwd:String)
    }
}