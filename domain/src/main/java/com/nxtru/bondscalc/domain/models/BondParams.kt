package com.nxtru.bondscalc.domain.models

/**
 * Input parameters for calculator.
 * Can be entered manually.
 */
data class BondParams(
    val ticker: String,
    val commission: String,
    val tax: String,
    val coupon: String,
    val parValue: String,
    val buyPrice: String,
    val buyDate: String,
    val sellPrice: String,
    val sellDate: String,
    val tillMaturity: Boolean,
) {
    companion object {
        val EMPTY = BondParams(
            "", "", "", "", "",
            "", "",
            "", "",
            false
        )
    }
}
