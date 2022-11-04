package com.nxtru.bondscalc.domain.models

typealias Ticker = String

data class BriefBondInfo(
    val ticker: Ticker,
    val isin: String,
    val isTraded: Boolean = true,
)

data class BondInfo(
    // MOEX: SHORTNAME
    val ticker: Ticker,
    // MOEX: ISIN
    val isin: String,
    // MOEX: FACEVALUE
    val parValue: String,
    // MOEX: COUPONPERCENT
    val coupon: String,
    // MOEX: MATDATE
    val maturityDate: String,
)
