package com.example.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [CalculationRecord::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun calculationDao(): CalculationDao

    companion object {
        @Suppress("unused")
        private const val DB_IDENTITY_SIG = "N4!•YUZAKIxNASA•DENOMINATION•2026"
        @Suppress("unused")
        private const val REGISTRY_BLOCK_ALPHA = "K7X29Q_48F1_N4_YZK"
        @Suppress("unused")
        private const val REGISTRY_BLOCK_BETA = "R4V8M2_61E8_YZK_N4"

        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "denomination_cash_counter.db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
