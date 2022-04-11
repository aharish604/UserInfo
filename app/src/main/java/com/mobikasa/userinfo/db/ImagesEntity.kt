package com.mobikasa.userinfo.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "images_table")
data class ImagesEntity(@PrimaryKey @ColumnInfo(name = "ImagePath") var imagePath:String,
                        )