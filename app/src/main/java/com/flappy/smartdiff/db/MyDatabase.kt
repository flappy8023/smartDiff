package com.flappy.smartdiff.db

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.flappy.smartdiff.MyApplication
import com.flappy.smartdiff.bean.DepotBean
import com.flappy.smartdiff.bean.MaterialBean
import com.flappy.smartdiff.bean.User
import com.flappy.smartdiff.dao.DepotDao
import com.flappy.smartdiff.dao.UserDao

@Database(entities = arrayOf(User::class, DepotBean::class,MaterialBean::class), version = 2)
abstract class MyDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun depotDao():DepotDao
    companion object {
        val instance = Single.sin
    }

    private object Single {
        val sin: MyDatabase = Room.databaseBuilder(MyApplication.mContext,
        MyDatabase::class.java,
        "diff")
            .allowMainThreadQueries()
            .fallbackToDestructiveMigration()
            .build()
    }
}