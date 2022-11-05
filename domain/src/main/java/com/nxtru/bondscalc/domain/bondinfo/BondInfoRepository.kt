package com.nxtru.bondscalc.domain.bondinfo

import com.nxtru.bondscalc.domain.models.BondInfo
import com.nxtru.bondscalc.domain.models.BriefBondInfo

/**
 * Loads information about bonds.
 */
interface BondInfoRepository {
    /**
     * Search bonds.
     * "query" must have length >=3
     *
     * @return null in case of error
     */
    suspend fun searchBonds(query: String): List<BriefBondInfo>?

    /**
     * Load bond info.
     *
     * @return null in case of error
     */
    suspend fun loadBondInfo(secId: String): BondInfo?
}
