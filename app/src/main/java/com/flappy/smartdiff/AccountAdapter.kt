package com.flappy.smartdiff

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.flappy.smartdiff.bean.User

class AccountAdapter(val context: Context,val data:List<User>) : RecyclerView.Adapter<AccountAdapter.MyHolder>() {
    var listener:ClickListener? = null
    class MyHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvDel: TextView? = null
        var tvEdit: TextView? = null
        var tvAccount: TextView? = null

        init {
            tvDel = itemView.findViewById(R.id.tv_del)
            tvEdit = itemView.findViewById(R.id.tv_edit)
            tvAccount = itemView.findViewById(R.id.tv_account)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        return MyHolder(LayoutInflater.from(context).inflate(R.layout.item_account_layout, parent, false))
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        val bean = data.get(position)
        holder.tvAccount?.setText(bean.userName)
        holder.tvEdit?.setOnClickListener {
            listener?.onEdit(bean)
        }
        holder.tvDel?.setOnClickListener {
            listener?.onDel(bean)
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }
    public interface ClickListener{
        fun onDel(user:User)
        fun onEdit(user:User)
    }
}