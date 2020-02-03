package com.example.roomdemo.repositry

import android.content.Context
import android.os.AsyncTask
import android.service.autofill.UserData
import android.util.Log
import androidx.lifecycle.LiveData
import com.example.roomdemo.dao.DataAccessObject
import com.example.roomdemo.database.UserDataBase
import com.example.roomdemo.entity.UserName

class DataRespositry(context:Context)
{
    var userDao:DataAccessObject
    var myData:LiveData<List<UserName>>
    var context:Context

    init {
        userDao = UserDataBase.invoke(context).getDataAccessObject()
        this.context = context
        myData = getUserData()
    }

    public fun getUserData(): LiveData<List<UserName>>
    {

        return AllDataAsyncTask(userDao).execute().get()
    }


    public fun saveUser(user:UserName)
    {
        InsertUserDetail(userDao).execute(user)
    }

    public fun isUserExist(userName:String):Boolean
    {
        var value1 = GetUserOfNameAsyncTask(userDao).execute("%"+userName+"%").get()

        Log.e("value",value1.toString())

        Log.e("Size",value1.value.toString() + "/"+value1.value?.size)
        var data:List<UserName>? = value1.value
        return data?.size?:0 > 0
    }


data class AllDataAsyncTask(val userDao:DataAccessObject):AsyncTask<Void,Void,LiveData<List<UserName>>>()
{
  
    override fun doInBackground(vararg params: Void?): LiveData<List<UserName>>
    {
        var value = userDao.getAllUser()
        return value
    }
}


    /**
     * insert Record
     */
    data class InsertUserDetail(val userDao:DataAccessObject):AsyncTask<UserName,Void,Void>()
    {
        override fun doInBackground(vararg params: UserName): Void?
        {
          var value = userDao.insertUserData(params[0])
            Log.e("insertValue",params[0].toString() + value)
            return  null
        }
    }


    /***
     * Get Perticular user
     */

    data class GetUserOfNameAsyncTask(val userDao:DataAccessObject):AsyncTask<String,Void,LiveData<List<UserName>>>()
    {

        override fun doInBackground(vararg params: String?): LiveData<List<UserName>>
        {

            return userDao.getUserName(params[0])
        }
    }


}