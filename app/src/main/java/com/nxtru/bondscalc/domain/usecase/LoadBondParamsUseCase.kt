package com.nxtru.bondscalc.domain.usecase

import com.nxtru.bondscalc.domain.models.BondParams
import com.nxtru.bondscalc.domain.repository.BondParamsRepository

/**
 * Load bond params from repository.
 */
class LoadBondParamsUseCase(private val repo: BondParamsRepository) {
    fun execute(): BondParams = repo.loadBondParams()
}
