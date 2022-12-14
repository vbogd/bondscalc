package com.nxtru.bondscalc.domain.models

typealias Ticker = String

data class BriefBondInfo(
    // MOEX: SECID
    val secId: String,
    // MOEX: SHORTNAME
    val ticker: Ticker,
    // MOEX: ISIN
    val isin: String,
    val isTraded: Boolean = true,
)

data class BondInfo(
    // MOEX: SECID
    val secId: String,
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
    // MOEX: CURRENCYID
    val currencyId: String,
    // MOEX: LAST
    val lastPrice: String,
    // MOEX: NEXTCOUPON
    val nextCouponDate: String,
    // MOEX: BOARDID
    val boardId: String,
)
