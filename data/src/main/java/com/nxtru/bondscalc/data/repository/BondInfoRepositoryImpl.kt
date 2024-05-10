package com.nxtru.bondscalc.data.repository

import com.nxtru.bondscalc.data.storage.BondParamsStorage
import com.nxtru.bondscalc.domain.models.BondInfo
import com.nxtru.bondscalc.domain.repository.BondInfoRepository

private const val SEC_ID_KEY = "bond_info_secId"
private const val ISIN_KEY = "bond_info_isin"
private const val CURRENCY_ID_KEY = "bond_info_currency_id"
private const val NEXT_COUPON_DATE_KEY = "bond_info_next_coupon_date"
private const val MATURITY_DATE = "bond_info_maturity_date"
private const val OFFER_DATE = "bond_info_offer_date"

class BondInfoRepositoryImpl(
    private val storage: BondParamsStorage
) : BondInfoRepository {
    override fun saveBondInfo(value: BondInfo): Boolean {
        storage.saveString(SEC_ID_KEY, value.secId)
        storage.saveString(ISIN_KEY, value.isin)
        storage.saveString(CURRENCY_ID_KEY, value.currencyId)
        storage.saveString(NEXT_COUPON_DATE_KEY, value.nextCouponDate)
        storage.saveString(MATURITY_DATE, value.maturityDate)
        storage.saveString(OFFER_DATE, value.offerDate)
        return true
    }

    override fun loadBondInfo(): BondInfo {
        return BondInfo(
            secId = storage.loadString(SEC_ID_KEY),
            ticker = "",
            isin = storage.loadString(ISIN_KEY),
            parValue = "",
            currencyId = storage.loadString(CURRENCY_ID_KEY),
            coupon = "",
            couponPeriod = "",
            nextCouponDate = storage.loadString(NEXT_COUPON_DATE_KEY),
            maturityDate = storage.loadString(MATURITY_DATE),
            offerDate = storage.loadString(OFFER_DATE),
            lastPrice = "",
            boardId = "",
        )
    }
}
