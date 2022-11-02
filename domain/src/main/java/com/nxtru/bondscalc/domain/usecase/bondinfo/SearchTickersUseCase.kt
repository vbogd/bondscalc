package com.nxtru.bondscalc.domain.usecase.bondinfo

import com.nxtru.bondscalc.domain.bondinfo.BondInfoService
import com.nxtru.bondscalc.domain.models.Ticker

class SearchTickersUseCase(
    private val loader: BondInfoService
) {
    suspend operator fun invoke(ticker: Ticker): List<Ticker>? {
        return loader.searchBonds(ticker)
    }
}
