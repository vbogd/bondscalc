package com.nxtru.bondscalc.presentation.models

data class MainUIState(
    val searchScreenUIState: SearchScreenUIState = SearchScreenUIState(),
    val calculatorScreenUIState: CalculatorScreenUIState = CalculatorScreenUIState(),
)
