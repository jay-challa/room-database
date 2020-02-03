package com.example.roomdemo.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.roomdemo.dao.DataAccessObject
import com.example.roomdemo.entity.UserName

@Database(entities = arrayOf(UserName::class),version = 1)
abstract class UserDataBase: RoomDatabase()
{
    abstract fun  getDataAccessObject(): DataAccessObject

    companion object
    {
        @Volatile private var instance:UserDataBase?=null
        private val lock = Any()

        operator fun invoke(context: Context)= instance ?: synchronized(lock){
            instance ?: buildDatabase(context).also { instance = it}
        }

        private fun buildDatabase(context: Context) = Room.databaseBuilder(context,UserDataBase::class.java,"UserDataBase.db").
            allowMainThreadQueries().build()
    }
}