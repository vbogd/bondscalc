package com.nxtru.bondscalc.domain.models

/**
 * Results of calculation.
 */
data class BondCalcResult(
    // income in roubles
    val income: Double,
    // yield to maturity
    val ytm: Double,
    val currentYield: Double,
    val days: Long,
)
