package com.example.roomdemo.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "username")
data class  UserName(@PrimaryKey (autoGenerate = true)var userId:Int, var userName:String,var userImage:String,var isUserView:Boolean)
