package com.mobikasa.userinfo.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "header_table")
data class HeaderEntity(@ColumnInfo(name = "ipAddress") var ipAddress: String,
                        @ColumnInfo(name = "device_model") var device_model: String,
                        @ColumnInfo(name = "device_token") var device_token: String,
                        @PrimaryKey @ColumnInfo(name = "sno") var no: Int = 1)