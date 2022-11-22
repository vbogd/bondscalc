package com.nxtru.bondscalc.domain.repository

import com.nxtru.bondscalc.domain.models.BondInfo

interface BondInfoRepository {
    fun saveBondInfo(value: BondInfo): Boolean
    fun loadBondInfo(): BondInfo
}
