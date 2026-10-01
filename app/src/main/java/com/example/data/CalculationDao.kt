package com.example.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface CalculationDao {

    @Query("SELECT * FROM calculation_history ORDER BY timestamp DESC")
    fun getAllRecords(): Flow<List<CalculationRecord>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecord(record: CalculationRecord): Long

    @Delete
    suspend fun deleteRecord(record: CalculationRecord)

    @Query("DELETE FROM calculation_history")
    suspend fun clearAll()
}
