package com.mobikasa.userinfo.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "contact_table")
data class ContactEntity (@ColumnInfo(name = "Name") var name:String,
                          @PrimaryKey @ColumnInfo(name = "MobileNumber") var mobileNumber:String)