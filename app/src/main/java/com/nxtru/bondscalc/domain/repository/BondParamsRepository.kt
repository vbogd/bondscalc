package com.nxtru.bondscalc.domain.repository

import com.nxtru.bondscalc.domain.models.BondParams

interface BondParamsRepository {
    fun saveBondParams(value: BondParams): Boolean
    fun loadBondParams(): BondParams
}
