package com.nxtru.bondscalc.data.repository

import com.nxtru.bondscalc.data.storage.BondParamsStorage
import com.nxtru.bondscalc.domain.models.BondInfo
import com.nxtru.bondscalc.domain.repository.BondInfoRepository

private const val SEC_ID_KEY = "bond_info_secId"
private const val ISIN_KEY = "bond_info_isin"
private const val CURRENCY_ID_KEY = "bond_info_currency_id"
private const val NEXT_COUPON_DATE_KEY = "bond_info_nex_coupon_date"

class BondInfoRepositoryImpl(
    private val storage: BondParamsStorage
) : BondInfoRepository {
    override fun saveBondInfo(value: BondInfo): Boolean {
        storage.saveString(SEC_ID_KEY, value.secId)
        storage.saveString(ISIN_KEY, value.isin)
        storage.saveString(CURRENCY_ID_KEY, value.currencyId)
        storage.saveString(NEXT_COUPON_DATE_KEY, value.nextCouponDate)
        return true
    }

    override fun loadBondInfo(): BondInfo {
        return BondInfo(
            secId = storage.loadString(SEC_ID_KEY),
            ticker = "",
            isin = storage.loadString(ISIN_KEY),
            parValue = "",
            coupon = "",
            maturityDate = "",
            currencyId = storage.loadString(CURRENCY_ID_KEY),
            lastPrice = "",
            nextCouponDate = storage.loadString(NEXT_COUPON_DATE_KEY),
            boardId = "",
        )
    }
}
