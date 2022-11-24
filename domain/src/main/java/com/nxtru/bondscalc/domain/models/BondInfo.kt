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
    // MOEX: CURRENCYID
    val currencyId: String,
    // MOEX: COUPONPERCENT
    val coupon: String,
    // MOEX: COUPONPERIOD
    val couponPeriod: String,
    // MOEX: NEXTCOUPON
    val nextCouponDate: String,
    // MOEX: MATDATE
    val maturityDate: String,
    // MOEX: OFFERDATE
    val offerDate: String,
    // MOEX: LAST
    val lastPrice: String,
    // MOEX: BOARDID
    val boardId: String,
)
