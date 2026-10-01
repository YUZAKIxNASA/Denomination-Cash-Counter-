package com.example

import com.example.model.DenominationItem
import com.example.util.IndianCurrencyUtil
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ExampleUnitTest {

    @Test
    fun testAmountInWords_examplesFromSpec() {
        assertEquals("Zero Rupees Only", IndianCurrencyUtil.convertToWords(0L))
        assertEquals("One Rupee Only", IndianCurrencyUtil.convertToWords(1L))
        assertEquals("Ten Rupees Only", IndianCurrencyUtil.convertToWords(10L))
        assertEquals("One Hundred Rupees Only", IndianCurrencyUtil.convertToWords(100L))
        assertEquals("One Thousand Rupees Only", IndianCurrencyUtil.convertToWords(1000L))
        assertEquals("Eight Thousand Seven Hundred Fifty Rupees Only", IndianCurrencyUtil.convertToWords(8750L))
        assertEquals("Ten Thousand Five Hundred Rupees Only", IndianCurrencyUtil.convertToWords(10500L))
        assertEquals("One Lakh Rupees Only", IndianCurrencyUtil.convertToWords(100000L))
        assertEquals("One Crore Rupees Only", IndianCurrencyUtil.convertToWords(10000000L))
    }

    @Test
    fun testIndianNumberFormatting() {
        assertEquals("0", IndianCurrencyUtil.formatIndianNumber(0L))
        assertEquals("100", IndianCurrencyUtil.formatIndianNumber(100L))
        assertEquals("1,000", IndianCurrencyUtil.formatIndianNumber(1000L))
        assertEquals("8,750", IndianCurrencyUtil.formatIndianNumber(8750L))
        assertEquals("1,00,000", IndianCurrencyUtil.formatIndianNumber(100000L))
        assertEquals("1,00,00,000", IndianCurrencyUtil.formatIndianNumber(10000000L))
    }

    @Test
    fun testRupeeDisplayFormatting() {
        assertEquals("₹ 0 /-", IndianCurrencyUtil.formatRupeeDisplay(0L))
        assertEquals("₹ 8,750 /-", IndianCurrencyUtil.formatRupeeDisplay(8750L))
        assertEquals("₹ 2,000 /-", IndianCurrencyUtil.formatRupeeDisplay(2000L))
    }

    @Test
    fun testDenominationsStrictlyCompliant() {
        val items = DenominationItem.ITEMS
        assertEquals("Must have exactly 10 denominations", 10, items.size)

        // Verify ₹2000 is NOT present
        assertFalse("Must NOT include ₹2000", items.any { it.value == 2000L || it.label.contains("2000") || it.label.contains("2,000") })

        // Verify exact order: ₹500, ₹200, ₹100, ₹50, ₹20, ₹10, ₹5, ₹2, ₹1, Coin
        val expectedLabels = listOf("₹ 500", "₹ 200", "₹ 100", "₹ 50", "₹ 20", "₹ 10", "₹ 5", "₹ 2", "₹ 1", "Coin")
        assertEquals(expectedLabels, items.map { it.label })

        // Verify Coin value is 0 and isCoin is true
        val coin = items.first { it.id == "coin" }
        assertTrue(coin.isCoin)
        assertEquals(0L, coin.value)
    }
}
