package com.flappy.smartdiff.bean

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * @FileName: MaterialBean
 * @Author: luweiming
 * @Date: 2020/12/11 9:26
 * @Description:
 * @Version: 1.0
 */
@Entity(tableName = "material")
data class MaterialBean(var id:String,var number:String,var name:String) {
    @PrimaryKey(autoGenerate = true) var uid:Int = 0
}