package com.mobikasa.userinfo.db

import androidx.room.*

@Dao
interface HeaderDao {

    @Query("SELECT * FROM header_table WHERE sno =1")
    fun getAll(): HeaderEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg authTable: HeaderEntity)

    @Delete
    fun delete(authTable: HeaderEntity)

}