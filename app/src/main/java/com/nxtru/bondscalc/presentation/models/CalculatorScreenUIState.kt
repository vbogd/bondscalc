package com.nxtru.bondscalc.presentation.models

import com.nxtru.bondscalc.domain.models.BondInfo
import com.nxtru.bondscalc.domain.models.BondParams
import com.nxtru.bondscalc.presentation.BondCalcUIResult

data class CalculatorScreenUIState(
    val bondParams: BondParams = BondParams.EMPTY,
    val calcResult: BondCalcUIResult = BondCalcUIResult.UNDEFINED,
    val bondInfo: BondInfo? = null,
)

fun CalculatorScreenUIState.bondParams(value: BondParams): CalculatorScreenUIState =
    this.copy(bondParams = value)
