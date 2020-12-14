package com.flappy.smartdiff.view

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.flappy.smartdiff.AccountAdapter
import com.flappy.smartdiff.MyDialog
import com.flappy.smartdiff.base.BaseActivity
import com.flappy.smartdiff.bean.User
import com.flappy.smartdiff.contract.AccountContract
import com.flappy.smartdiff.databinding.ActivityAccountBinding
import com.flappy.smartdiff.presenter.AccountPresenter

class AccountActivity : BaseActivity<AccountPresenter>(), AccountContract.IAccountView{
    private lateinit var binding: ActivityAccountBinding
    val users = mutableListOf<User>()
    var adapter:AccountAdapter?= null
    override fun getContentView(): View {
        binding = ActivityAccountBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun initView() {
        binding.top.title.text = "账号管理"
        users.addAll(mPresenter.getUsers())
        binding.recyclerview.layoutManager = LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)
        adapter = AccountAdapter(this,users)
        binding.recyclerview.adapter = adapter
        adapter?.listener = object : AccountAdapter.ClickListener {
            override fun onDel(user: User) {
                mPresenter.delete(user)
            }

            override fun onEdit(user: User) {
                val dialog = MyDialog(this@AccountActivity,1)
                dialog.user = user
                dialog.iSave = object :MyDialog.ISave{
                    override fun onSave(name: String, pwd: String) {
                        user.userName =name
                        user.pwd =pwd
                        mPresenter.update(user)
                        dialog.dismiss()
                    }

                }
                dialog.show()
            }

        }
        binding.tvAdd.setOnClickListener {
            val dialog = MyDialog(this,0)

            dialog.iSave = object :MyDialog.ISave{
                override fun onSave(name: String, pwd: String) {
                    val user = User(name,pwd)
                    mPresenter.addUser(user)
                    dialog.dismiss()
                }
            }
            dialog.show()
        }
    }

    override fun createPreseneter(): AccountPresenter {
        return AccountPresenter()
    }

    override fun showAccounts(users: List<User>) {
       this.users.clear()
        this.users.addAll(users)
        adapter?.notifyDataSetChanged()
    }
}