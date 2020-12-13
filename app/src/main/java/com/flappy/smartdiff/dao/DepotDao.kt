package com.flappy.smartdiff.dao

import androidx.room.*
import com.flappy.smartdiff.bean.DepotBean
import com.flappy.smartdiff.bean.MaterialBean

@Dao
interface DepotDao {
    @Insert
    fun insert(depot:DepotBean):Long
    @Query("DELETE from depot")
    fun deleteAll()
    @Delete
    fun delete(depot: DepotBean)
    @Update
    fun update(depot: DepotBean)
    @Query("SELECT * from depot")
    fun getDepots():DepotBean?
    @Query("SELECT * from material")
    fun queryMaterials():List<MaterialBean>
    @Update
    fun updateMaterial(curMaterial: MaterialBean)
    @Query("Delete from material")
    fun deleteAllMaterials()
    @Insert
    fun insertAllMaterials(beans:List<MaterialBean>)
}