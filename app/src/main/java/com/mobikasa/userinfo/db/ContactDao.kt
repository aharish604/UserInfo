package com.mobikasa.userinfo.db

import androidx.room.*

@Dao
interface ContactDao {

    @Query("SELECT * FROM contact_table")
    fun getAll(): ContactEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg authTable: ContactEntity)

    @Delete
    fun delete(authTable: ContactEntity)

}