package com.example.roomdemo.adapter


import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.roomdemo.OnValueMatch
import com.example.roomdemo.R
import com.example.roomdemo.databinding.EmptyLayoutBinding
import com.example.roomdemo.databinding.UserLayoutBinding
import com.example.roomdemo.entity.UserName
import com.example.roomdemo.holder.EmptyViewHolder
import com.example.roomdemo.holder.UserViewHolder

class UserAdapter(context:Context,userList:ArrayList<UserName>,onMatch: OnValueMatch):RecyclerView.Adapter <RecyclerView.ViewHolder>()
{
    var context:Context
    var userList:ArrayList<UserName>
    var userListFilter:ArrayList<UserName>
    var inflater:LayoutInflater
    var onMatch: OnValueMatch

    init {
       this.context =context
        this.userList = userList
        this.userListFilter = userList
        inflater = LayoutInflater.from(context)
        this.onMatch = onMatch
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder
    {
        when(viewType)
        {
         0 ->
         {
           var emptyLayoutBinding = DataBindingUtil.inflate<EmptyLayoutBinding>(inflater, R.layout.empty_layout,parent,false)
             return EmptyViewHolder(emptyLayoutBinding.root)
         }else ->{

            var userLayoutBinding = DataBindingUtil.inflate<UserLayoutBinding>(inflater,R.layout.user_layout,parent,false)
            return UserViewHolder(userLayoutBinding)
        }
        }
    }

    override fun getItemCount(): Int
    {
        return userList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int)
    {
        var value = userList.get(position).isUserView
       if(value)
       {
           (holder as UserViewHolder).setUserDetail(userList.get(position))

       }
    }

    override fun getItemViewType(position: Int): Int
    {
        return if(userList.get(position).isUserView) 1 else 0
    }


    /**
     * Search User
     */
    fun searchUser(userName:String)
    {
        var match = true
        userList = ArrayList()
      if(userName.isNullOrEmpty())
      {
        userList = userListFilter

      }else
      {

         for(user in userListFilter)
         {
         if(user.userName.contains(userName,ignoreCase = true))
         {
          userList.add(user)
         }
         }

          /**
           * In case empty list
           */
          if(userList.size==0)
          {
              userList.add(UserName(0,"","",false))
              match = false
          }
      }

        notifyDataSetChanged()
        onMatch.isValueMatch(match)
    }
}