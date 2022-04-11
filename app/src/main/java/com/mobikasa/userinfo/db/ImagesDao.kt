package com.mobikasa.userinfo.db

import androidx.room.*

@Dao
interface ImagesDao {

    @Query("SELECT * FROM images_table" )
    fun getAll(): List<ImagesEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg authTable: ImagesEntity)

    @Delete
    fun delete(authTable: ImagesEntity)

}