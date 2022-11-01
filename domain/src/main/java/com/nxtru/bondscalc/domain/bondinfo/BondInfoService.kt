package com.nxtru.bondscalc.domain.bondinfo

import com.nxtru.bondscalc.domain.models.Ticker

/**
 * Loads information about bonds.
 */
interface BondInfoService {
    /**
     * Search bonds.
     * "query" must have length >=3
     */
    suspend fun searchBonds(query: String): List<Ticker>
}
