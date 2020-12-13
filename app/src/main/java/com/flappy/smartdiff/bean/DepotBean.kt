package com.flappy.smartdiff.bean

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

/**
 * @FileName: DepotBean
 * @Author: luweiming
 * @Date: 2020/12/11 9:28
 * @Description:
 * @Version: 1.0
 */
@Entity(tableName = "depot")
data class DepotBean(@PrimaryKey var userId:Int,var nameStart:String, var interfacePostion:String, var count:Int) {

}

fun main() {
    val s = "start01"
    println(s.subSequence(s.length-2,s.length).toString().toInt())
    println(s.subSequence(0,s.length-2))
    for(i in 1..10){
        println(i)
    }
}