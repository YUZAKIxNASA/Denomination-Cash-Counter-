package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "calculation_history")
data class CalculationRecord(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val timestamp: Long,
    val formattedDate: String,
    val formattedTime: String,
    val totalCount: Int,
    val totalAmount: Long,
    val amountInWords: String,
    // Denomination counts
    val count500: Int = 0,
    val count200: Int = 0,
    val count100: Int = 0,
    val count50: Int = 0,
    val count20: Int = 0,
    val count10: Int = 0,
    val count5: Int = 0,
    val count2: Int = 0,
    val count1: Int = 0,
    val countCoin: Int = 0
) {
    fun toCountMap(): Map<String, Int> {
        return mapOf(
            "500" to count500,
            "200" to count200,
            "100" to count100,
            "50" to count50,
            "20" to count20,
            "10" to count10,
            "5" to count5,
            "2" to count2,
            "1" to count1,
            "coin" to countCoin
        )
    }

    fun toBreakdownList(): List<Pair<String, Int>> {
        return listOf(
            "₹ 500" to count500,
            "₹ 200" to count200,
            "₹ 100" to count100,
            "₹ 50" to count50,
            "₹ 20" to count20,
            "₹ 10" to count10,
            "₹ 5" to count5,
            "₹ 2" to count2,
            "₹ 1" to count1,
            "Coin" to countCoin
        )
    }
}
