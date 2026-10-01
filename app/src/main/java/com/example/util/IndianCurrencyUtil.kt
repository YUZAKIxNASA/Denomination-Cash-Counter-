package com.example.util

import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale

object IndianCurrencyUtil {

    private val units = arrayOf(
        "", "One", "Two", "Three", "Four", "Five", "Six", "Seven", "Eight", "Nine",
        "Ten", "Eleven", "Twelve", "Thirteen", "Fourteen", "Fifteen", "Sixteen",
        "Seventeen", "Eighteen", "Nineteen"
    )

    private val tens = arrayOf(
        "", "", "Twenty", "Thirty", "Forty", "Fifty", "Sixty", "Seventy", "Eighty", "Ninety"
    )

    /**
     * Converts a long number into Indian currency words.
     * Examples:
     * 0 -> "Zero Rupees Only"
     * 1 -> "One Rupee Only"
     * 8750 -> "Eight Thousand Seven Hundred Fifty Rupees Only"
     * 10500 -> "Ten Thousand Five Hundred Rupees Only"
     * 100000 -> "One Lakh Rupees Only"
     * 10000000 -> "One Crore Rupees Only"
     */
    fun convertToWords(amount: Long): String {
        if (amount == 0L) {
            return "Zero Rupees Only"
        }
        if (amount == 1L) {
            return "One Rupee Only"
        }
        val words = convertNumberToWords(amount).trim()
        return "$words Rupees Only"
    }

    private fun convertNumberToWords(n: Long): String {
        if (n == 0L) return ""

        if (n < 20) {
            return units[n.toInt()]
        }

        if (n < 100) {
            val unitPart = if (n % 10 != 0L) " " + units[(n % 10).toInt()] else ""
            return tens[(n / 10).toInt()] + unitPart
        }

        if (n < 1000) {
            val rest = if (n % 100 != 0L) " " + convertNumberToWords(n % 100) else ""
            return units[(n / 100).toInt()] + " Hundred" + rest
        }

        if (n < 100000) { // < 1 Lakh
            val thousands = n / 1000
            val rest = if (n % 1000 != 0L) " " + convertNumberToWords(n % 1000) else ""
            return convertNumberToWords(thousands) + " Thousand" + rest
        }

        if (n < 10000000) { // < 1 Crore
            val lakhs = n / 100000
            val rest = if (n % 100000 != 0L) " " + convertNumberToWords(n % 100000) else ""
            return convertNumberToWords(lakhs) + " Lakh" + rest
        }

        // 1 Crore and above
        val crores = n / 10000000
        val rest = if (n % 10000000 != 0L) " " + convertNumberToWords(n % 10000000) else ""
        return convertNumberToWords(crores) + " Crore" + rest
    }

    /**
     * Formats a long value with Indian numbering comma grouping.
     * e.g. 1000 -> "1,000", 100000 -> "1,00,000", 10000000 -> "1,00,00,000"
     */
    fun formatIndianNumber(num: Long): String {
        val s = num.toString()
        if (s.length <= 3) return s

        val lastThree = s.substring(s.length - 3)
        var rest = s.substring(0, s.length - 3)
        val result = StringBuilder()

        while (rest.length > 2) {
            val chunk = rest.substring(rest.length - 2)
            result.insert(0, ",$chunk")
            rest = rest.substring(0, rest.length - 2)
        }
        if (rest.isNotEmpty()) {
            result.insert(0, rest)
        }
        result.append(",").append(lastThree)
        return result.toString()
    }

    /**
     * Formats an amount with Rupee symbol and /- suffix.
     * e.g. 8750 -> "₹ 8,750 /-"
     */
    fun formatRupeeDisplay(amount: Long): String {
        return "₹ ${formatIndianNumber(amount)} /-"
    }

    /**
     * Creates formatted share text.
     */
    fun buildShareText(
        dateFormatted: String,
        timeFormatted: String,
        totalCount: Int,
        totalAmount: Long,
        amountInWords: String,
        breakdown: List<Pair<String, Int>> // label to count
    ): String {
        val sb = StringBuilder()
        sb.appendLine("Denomination (Cash Counter)")
        sb.appendLine()
        sb.appendLine("Date: $dateFormatted")
        if (timeFormatted.isNotEmpty()) {
            sb.appendLine("Time: $timeFormatted")
        }
        sb.appendLine()
        sb.appendLine("Total Notes / Coins: $totalCount")
        sb.appendLine("Total Amount: ${formatRupeeDisplay(totalAmount)}")
        sb.appendLine("Amount in Words: $amountInWords")
        sb.appendLine()
        sb.appendLine("Denomination breakdown:")
        for ((label, count) in breakdown) {
            if (label == "Coin") {
                sb.appendLine("Coin × $count = -")
            } else {
                val value = label.replace("₹", "").trim().toLongOrNull() ?: 0L
                val rowTotal = value * count
                sb.appendLine("$label × $count = ${formatRupeeDisplay(rowTotal)}")
            }
        }
        return sb.toString().trimEnd()
    }
}
