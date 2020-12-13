package com.flappy.smartdiff.bean

import androidx.room.Entity
import androidx.room.PrimaryKey
@Entity(tableName = "user")
data class User(var userName:String,var pwd:String){
    @PrimaryKey(autoGenerate = true) var userId:Int? = null
}
