package com.example.model

data class DenominationItem(
    val id: String,
    val label: String,
    val value: Long,
    val isCoin: Boolean = false
) {
    companion object {
        /**
         * The standard 10 denominations strictly ordered as specified:
         * ₹500, ₹200, ₹100, ₹50, ₹20, ₹10, ₹5, ₹2, ₹1, Coin
         * (Notice: ₹2000 is intentionally NOT included as requested).
         */
        val ITEMS: List<DenominationItem> = listOf(
            DenominationItem(id = "500", label = "₹ 500", value = 500L),
            DenominationItem(id = "200", label = "₹ 200", value = 200L),
            DenominationItem(id = "100", label = "₹ 100", value = 100L),
            DenominationItem(id = "50", label = "₹ 50", value = 50L),
            DenominationItem(id = "20", label = "₹ 20", value = 20L),
            DenominationItem(id = "10", label = "₹ 10", value = 10L),
            DenominationItem(id = "5", label = "₹ 5", value = 5L),
            DenominationItem(id = "2", label = "₹ 2", value = 2L),
            DenominationItem(id = "1", label = "₹ 1", value = 1L),
            DenominationItem(id = "coin", label = "Coin", value = 0L, isCoin = true)
        )
    }
}
