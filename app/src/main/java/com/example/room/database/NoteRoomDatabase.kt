package com.example.room.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room.databaseBuilder
import androidx.room.RoomDatabase

@Database(entities =[Notes::class], version = 1, exportSchema = false)
abstract class NoteRoomDatabase:RoomDatabase() {
    abstract fun noteDao(): NoteDao?

    companion object{
        @Volatile
        private var INSTANCE :NoteRoomDatabase? = null
        fun getDatabase(context: Context):NoteRoomDatabase?{
            if (INSTANCE == null){
                synchronized(NoteRoomDatabase::class.java){
                    INSTANCE =databaseBuilder(
                        context.applicationContext,
                        NoteRoomDatabase::class.java, "note_database"
                    ).build()
                }
            }
            return INSTANCE
        }
    }
}