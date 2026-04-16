package com.example.miseenplace.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

//to create instances of the DAO for the app to get data from

@Database(
    entities = [NotesEntity::class, TaskEntity::class], version = 1, exportSchema = false
)

@TypeConverters(TaskItemTypeConverter::class) //to register the converter, and say there's conversion logic
abstract class MiseEnPlaceDatabase: RoomDatabase(){

    abstract fun noteDao(): NoteDao
    abstract fun taskDao(): TaskDao

    companion object{ //for methods to create or get the database. companion obejct so all instances of the databse have the same constants
        @Volatile
        private var Instance: MiseEnPlaceDatabase? = null

        fun getInstance(context: Context): MiseEnPlaceDatabase{
            return Instance?: synchronized(this){
                Room.databaseBuilder(
                    context,
                    MiseEnPlaceDatabase::class.java,
                    "mise_en_place_db")
                    .build()
                    .also { Instance = it }
            }
        }
    }
}
