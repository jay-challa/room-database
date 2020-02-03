package com.example.roomdemo.view_model

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.roomdemo.entity.UserName
import com.example.roomdemo.repositry.DataRespositry

class UserViewModel(application: Application): AndroidViewModel(application)
{
    var dataRespositry:DataRespositry?=null
    var userData:LiveData<List<UserName>>?=null

    init {
        dataRespositry = DataRespositry(application)
        userData = dataRespositry?.myData
    }

    public fun getAllData():LiveData<List<UserName>>?
    {
        return userData
    }


    public fun insertUser(userName:UserName)
    {
        dataRespositry?.saveUser(userName)
    }


    public fun isUserExist(name:String): Boolean? {
        return dataRespositry?.isUserExist(name)
    }
}