package com.example.roomdemo.holder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.roomdemo.databinding.UserLayoutBinding
import com.example.roomdemo.entity.UserName

class UserViewHolder(bind: UserLayoutBinding) : RecyclerView.ViewHolder(bind.root)
{
    var bind: UserLayoutBinding
    init {
       this.bind = bind
    }


    fun setUserDetail(userName: UserName)
    {
       bind.user = userName
    }
}