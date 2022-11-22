package com.nxtru.bondscalc.domain.usecase

import com.nxtru.bondscalc.domain.models.BondInfo
import com.nxtru.bondscalc.domain.repository.BondInfoRepository

class SaveBondInfoUseCase(
    private val repo: BondInfoRepository
) {
    operator fun invoke(value: BondInfo): Boolean = repo.saveBondInfo(value)
}
