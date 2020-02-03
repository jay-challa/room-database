package com.example.roomdemo.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.roomdemo.entity.UserName

@Dao
interface DataAccessObject
{

    @Insert
    public fun insertUserData(user:UserName?)

    @Query("select * from USERNAME where userName LIKE :name")
    public fun getUserName(name:String?):LiveData<List<UserName>>

    @Query("select * from USERNAME")
    public fun getAllUser():LiveData<List<UserName>>

}