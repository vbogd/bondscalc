package com.nxtru.bondscalc.domain.usecase

import com.nxtru.bondscalc.domain.models.BondParams
import com.nxtru.bondscalc.domain.repository.BondParamsRepository

class SaveBondParamsUseCase(private val repo: BondParamsRepository) {
    fun execute(value: BondParams): Boolean = repo.saveBondParams(value)
}
