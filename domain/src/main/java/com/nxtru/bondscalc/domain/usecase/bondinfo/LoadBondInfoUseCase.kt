package com.nxtru.bondscalc.domain.usecase.bondinfo

import com.nxtru.bondscalc.domain.bondinfo.BondInfoService
import com.nxtru.bondscalc.domain.models.BondInfo

class LoadBondInfoUseCase(
    private val loader: BondInfoService
) {
    suspend operator fun invoke(secId: String): BondInfo? {
        return loader.loadBondInfo(secId)
    }
}
