package com.nxtru.bondscalc.data.repository

import com.nxtru.bondscalc.data.storage.BondParamsStorage
import com.nxtru.bondscalc.domain.models.BondParams
import com.nxtru.bondscalc.domain.repository.BondParamsRepository

private const val TICKER_KEY = "ticker"
private const val COMMISSION_KEY = "commission"
private const val TAX_KEY = "tax"
private const val COUPON_KEY = "coupon"
private const val PAR_VALUE_KEY = "parValue"
private const val BUY_PRICE_KEY = "buyPrice"
private const val BUY_DATE_KEY = "buyDate"
private const val SELL_PRICE_KEY = "sellPrice"
private const val SELL_DATE_KEY = "sellDate"
private const val TILL_MATURITY_KEY = "tillMaturity"

// https://youtu.be/zt07bObIpSk?list=PLeF3l86ZMVkLQbdRL6Ra4cr_cmPROj94y&t=1527
class BondParamsRepositoryImpl(private val storage: BondParamsStorage) : BondParamsRepository {
    override fun saveBondParams(value: BondParams): Boolean {
        saveString(TICKER_KEY, value.ticker)
        saveString(COMMISSION_KEY, value.commission)
        saveString(TAX_KEY, value.tax)
        saveString(COUPON_KEY, value.coupon)
        saveString(PAR_VALUE_KEY, value.parValue)
        saveString(BUY_PRICE_KEY, value.buyPrice)
        saveString(BUY_DATE_KEY, value.buyDate)
        saveString(SELL_PRICE_KEY, value.sellPrice)
        saveString(SELL_DATE_KEY, value.sellDate)
        saveBoolean(TILL_MATURITY_KEY, value.tillMaturity)
        return true
    }

    override fun loadBondParams(): BondParams {
        return BondParams(
            ticker = loadString(TICKER_KEY),
            commission = loadString(COMMISSION_KEY),
            tax = loadString(TAX_KEY),
            coupon = loadString(COUPON_KEY),
            parValue = loadString(PAR_VALUE_KEY),
            buyDate = loadString(BUY_DATE_KEY),
            buyPrice = loadString(BUY_PRICE_KEY),
            sellDate = loadString(SELL_DATE_KEY),
            sellPrice = loadString(SELL_PRICE_KEY),
            tillMaturity = loadBoolean(TILL_MATURITY_KEY)
        )
    }

    private fun saveString(key: String, value: String): Boolean =
        storage.saveString(key, value)

    private fun loadString(key: String, defaultValue: String = ""): String =
        storage.loadString(key, defaultValue)

    private fun saveBoolean(key: String, value: Boolean): Boolean =
        storage.saveBoolean(key, value)

    private fun loadBoolean(key: String, defaultValue: Boolean = false): Boolean =
        storage.loadBoolean(key, defaultValue)
}
