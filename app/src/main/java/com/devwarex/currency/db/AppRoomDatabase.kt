package com.devwarex.currency.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = arrayOf(Currency::class,CurrencyRate::class), version = 1, exportSchema = false)
abstract class AppRoomDatabase : RoomDatabase() {

    abstract fun appDao(): AppDao

    companion object{

        @Volatile
        private var INSTANCE: AppRoomDatabase? = null

        fun getAppDatabase(context: Context): AppRoomDatabase{
            return INSTANCE ?: synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppRoomDatabase::class.java,
                    "currency_app_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}