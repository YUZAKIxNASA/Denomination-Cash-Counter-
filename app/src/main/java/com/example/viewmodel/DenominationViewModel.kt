package com.example.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.AppDatabase
import com.example.data.CalculationRecord
import com.example.model.DenominationItem
import com.example.util.IndianCurrencyUtil
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class DenominationViewModel(application: Application) : AndroidViewModel(application) {

    private val dao = AppDatabase.getDatabase(application).calculationDao()

    val historyRecords: StateFlow<List<CalculationRecord>> = dao.getAllRecords()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    // Current counts: denomination id -> count string
    private val _counts = MutableStateFlow<Map<String, String>>(emptyMap())
    val counts: StateFlow<Map<String, String>> = _counts.asStateFlow()

    // Live formatted date & time string: "Thu, 1 Oct 26, 01:40 pm"
    private val _currentDateTimeDisplay = MutableStateFlow(getCurrentDateTimeDisplay())
    val currentDateTimeDisplay: StateFlow<String> = _currentDateTimeDisplay.asStateFlow()

    // Transient UI events for feedback (e.g. snackbar messages)
    private val _toastEvents = MutableSharedFlow<String>()
    val toastEvents: SharedFlow<String> = _toastEvents.asSharedFlow()

    init {
        // Coroutine to update live time every second
        viewModelScope.launch {
            while (true) {
                _currentDateTimeDisplay.value = getCurrentDateTimeDisplay()
                delay(1000)
            }
        }
    }

    private fun getCurrentDateTimeDisplay(): String {
        // Matches the screenshot style: "Thu, 1 Oct 26, 01:40 pm"
        val sdf = SimpleDateFormat("EEE, d MMM yy, hh:mm a", Locale.ENGLISH)
        return sdf.format(Date())
    }

    private fun getCurrentDateFormatted(): String {
        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH)
        return sdf.format(Date())
    }

    private fun getCurrentTimeFormatted(): String {
        val sdf = SimpleDateFormat("hh:mm a", Locale.ENGLISH)
        return sdf.format(Date())
    }

    val totalCount: StateFlow<Int> = _counts.combine(_counts) { countsMap, _ ->
        calculateTotalCount(countsMap)
    }.stateIn(viewModelScope, SharingStarted.Eagerly, 0)

    val totalAmount: StateFlow<Long> = _counts.combine(_counts) { countsMap, _ ->
        calculateTotalAmount(countsMap)
    }.stateIn(viewModelScope, SharingStarted.Eagerly, 0L)

    val amountInWords: StateFlow<String> = totalAmount.combine(totalAmount) { amount, _ ->
        IndianCurrencyUtil.convertToWords(amount)
    }.stateIn(viewModelScope, SharingStarted.Eagerly, "Zero Rupees Only")

    fun onCountChanged(id: String, newText: String) {
        // Allow only digits
        val filtered = newText.filter { it.isDigit() }
        // Limit to 7 digits to prevent overflow
        val trimmed = if (filtered.length > 7) filtered.take(7) else filtered
        val cleanValue = if (trimmed.startsWith("0") && trimmed.length > 1) {
            trimmed.trimStart('0')
        } else {
            trimmed
        }

        val updated = _counts.value.toMutableMap()
        if (cleanValue.isEmpty()) {
            updated.remove(id)
        } else {
            updated[id] = cleanValue
        }
        _counts.value = updated
    }

    fun getCountForId(id: String): String {
        return _counts.value[id] ?: ""
    }

    fun getCountIntForId(id: String): Int {
        return _counts.value[id]?.toIntOrNull() ?: 0
    }

    fun getRowAmount(item: DenominationItem): Long {
        if (item.isCoin) return 0L
        val count = getCountIntForId(item.id)
        return item.value * count
    }

    private fun calculateTotalCount(countsMap: Map<String, String>): Int {
        var sum = 0
        DenominationItem.ITEMS.forEach { item ->
            val count = countsMap[item.id]?.toIntOrNull() ?: 0
            sum += count
        }
        return sum
    }

    private fun calculateTotalAmount(countsMap: Map<String, String>): Long {
        var sum = 0L
        DenominationItem.ITEMS.forEach { item ->
            if (!item.isCoin) {
                val count = countsMap[item.id]?.toIntOrNull() ?: 0
                sum += item.value * count
            }
        }
        return sum
    }

    fun clearCurrent() {
        _counts.value = emptyMap()
        viewModelScope.launch {
            _toastEvents.emit("Current calculation cleared")
        }
    }

    fun saveCalculation() {
        val currentCounts = _counts.value
        val countTotal = calculateTotalCount(currentCounts)
        val amountTotal = calculateTotalAmount(currentCounts)
        val words = IndianCurrencyUtil.convertToWords(amountTotal)

        val record = CalculationRecord(
            timestamp = System.currentTimeMillis(),
            formattedDate = getCurrentDateFormatted(),
            formattedTime = getCurrentTimeFormatted(),
            totalCount = countTotal,
            totalAmount = amountTotal,
            amountInWords = words,
            count500 = currentCounts["500"]?.toIntOrNull() ?: 0,
            count200 = currentCounts["200"]?.toIntOrNull() ?: 0,
            count100 = currentCounts["100"]?.toIntOrNull() ?: 0,
            count50 = currentCounts["50"]?.toIntOrNull() ?: 0,
            count20 = currentCounts["20"]?.toIntOrNull() ?: 0,
            count10 = currentCounts["10"]?.toIntOrNull() ?: 0,
            count5 = currentCounts["5"]?.toIntOrNull() ?: 0,
            count2 = currentCounts["2"]?.toIntOrNull() ?: 0,
            count1 = currentCounts["1"]?.toIntOrNull() ?: 0,
            countCoin = currentCounts["coin"]?.toIntOrNull() ?: 0
        )

        viewModelScope.launch {
            dao.insertRecord(record)
            _toastEvents.emit("Calculation saved to history")
        }
    }

    fun loadCalculation(record: CalculationRecord) {
        val map = mutableMapOf<String, String>()
        if (record.count500 > 0) map["500"] = record.count500.toString()
        if (record.count200 > 0) map["200"] = record.count200.toString()
        if (record.count100 > 0) map["100"] = record.count100.toString()
        if (record.count50 > 0) map["50"] = record.count50.toString()
        if (record.count20 > 0) map["20"] = record.count20.toString()
        if (record.count10 > 0) map["10"] = record.count10.toString()
        if (record.count5 > 0) map["5"] = record.count5.toString()
        if (record.count2 > 0) map["2"] = record.count2.toString()
        if (record.count1 > 0) map["1"] = record.count1.toString()
        if (record.countCoin > 0) map["coin"] = record.countCoin.toString()

        _counts.value = map
        viewModelScope.launch {
            _toastEvents.emit("Loaded saved calculation")
        }
    }

    fun deleteRecord(record: CalculationRecord) {
        viewModelScope.launch {
            dao.deleteRecord(record)
            _toastEvents.emit("Record deleted")
        }
    }

    fun clearAllHistory() {
        viewModelScope.launch {
            dao.clearAll()
            _toastEvents.emit("All history cleared")
        }
    }

    fun getShareText(): String {
        val breakdown = DenominationItem.ITEMS.map { item ->
            item.label to getCountIntForId(item.id)
        }
        return IndianCurrencyUtil.buildShareText(
            dateFormatted = getCurrentDateFormatted(),
            timeFormatted = getCurrentTimeFormatted(),
            totalCount = totalCount.value,
            totalAmount = totalAmount.value,
            amountInWords = amountInWords.value,
            breakdown = breakdown
        )
    }

    fun getShareTextForRecord(record: CalculationRecord): String {
        return IndianCurrencyUtil.buildShareText(
            dateFormatted = record.formattedDate,
            timeFormatted = record.formattedTime,
            totalCount = record.totalCount,
            totalAmount = record.totalAmount,
            amountInWords = record.amountInWords,
            breakdown = record.toBreakdownList()
        )
    }
}
