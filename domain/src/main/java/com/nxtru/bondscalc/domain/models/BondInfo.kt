package com.nxtru.bondscalc.domain.models

typealias Ticker = String

data class BriefBondInfo(
    val ticker: Ticker,
    val isin: String,
    val isTraded: Boolean = true,
)
