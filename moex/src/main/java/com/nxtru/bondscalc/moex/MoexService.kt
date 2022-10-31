package com.nxtru.bondscalc.moex

typealias Ticker = String

interface MoexService {
    /**
     * Search bonds.
     * "query" must have length >=3
     */
    suspend fun searchBonds(query: String): List<Ticker>

    companion object {
        fun create(): MoexService = MoexServiceImpl()
    }
}
