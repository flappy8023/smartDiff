package com.flappy.smartdiff.bean

/**
 * @FileName: DepotBean
 * @Author: luweiming
 * @Date: 2020/12/11 9:28
 * @Description:
 * @Version: 1.0
 */
data class DepotBean(var nameStart:String,var interfacePostion:String,var count:Int) {
    val items = mutableListOf<MaterialBean>()
    init {
        val start:Int = nameStart.subSequence(nameStart.length-2,nameStart.length).toString().toInt()
        val pre:String= nameStart.substring(0,nameStart.length-2)
        for (i in start..count+start){
            val id = if (i<10) "0"+i else i.toString()
            val materialBean = MaterialBean(id,"","")
            items.add(materialBean)
        }
    }

}

fun main() {
    val s = "start01"
    println(s.subSequence(s.length-2,s.length).toString().toInt())
    println(s.subSequence(0,s.length-2))
    for(i in 1..10){
        println(i)
    }
}