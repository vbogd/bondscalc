package com.nxtru.bondscalc.domain.usecase.bondinfo

import com.nxtru.bondscalc.domain.bondinfo.BondInfoRepository
import com.nxtru.bondscalc.domain.models.BriefBondInfo
import com.nxtru.bondscalc.domain.models.Ticker

class SearchTickersUseCase(
    private val loader: BondInfoRepository
) {
    suspend operator fun invoke(ticker: Ticker): List<BriefBondInfo>? {
        return loader.searchBonds(ticker)
    }
}
