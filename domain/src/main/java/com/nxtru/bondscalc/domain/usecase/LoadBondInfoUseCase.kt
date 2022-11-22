package com.nxtru.bondscalc.domain.usecase

import com.nxtru.bondscalc.domain.models.BondInfo
import com.nxtru.bondscalc.domain.repository.BondInfoRepository

class LoadBondInfoUseCase(
    private val repo: BondInfoRepository
) {
    operator fun invoke(): BondInfo = repo.loadBondInfo()
}
