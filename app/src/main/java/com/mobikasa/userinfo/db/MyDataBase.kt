package com.mobikasa.userinfo.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import java.util.concurrent.Executors


@Database(
    entities = [HeaderEntity::class, ContactEntity::class, ImagesEntity::class],
    version = 1,
    exportSchema = true )
abstract  class MyDataBase :RoomDatabase()
{

abstract fun HeaderDao():HeaderDao
abstract fun ContactDao():ContactDao
abstract fun ImagesDao():ImagesDao






companion object {
        private val NUMBER_OF_THREADS = 4
        val databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS)

        private const val dataBaseName = "User_INFO"
        fun getInstance(context: Context): MyDataBase = Room.databaseBuilder(
            context,
            MyDataBase::class.java,
            dataBaseName
        ).allowMainThreadQueries().addCallback(sRoomDatabaseCallback)
            .enableMultiInstanceInvalidation().build()


        private val sRoomDatabaseCallback: Callback = object : Callback() {
            override fun onOpen(db: SupportSQLiteDatabase) {
                super.onOpen(db)

                // If you want to keep data through app restarts,
                // comment out the following block
                databaseWriteExecutor.execute(Runnable { })
            }
        }

    }


    }