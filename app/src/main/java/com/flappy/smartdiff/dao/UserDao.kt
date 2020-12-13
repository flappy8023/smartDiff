package com.flappy.smartdiff.dao

import androidx.room.*
import com.flappy.smartdiff.bean.User

@Dao
interface UserDao {
    @Insert
    fun insertUser(user:User):Long
    @Delete
    fun delete(user:User)
    @Update
    fun upate(user: User)
    @Query("SELECT * from user where userName != 'admin'")
    fun getAll():List<User>
    @Query("SELECT * from user where userName == :name")
    fun getUserByName(name:String):User?
}