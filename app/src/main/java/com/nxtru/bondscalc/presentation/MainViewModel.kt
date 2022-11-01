package com.nxtru.bondscalc.presentation

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.nxtru.bondscalc.domain.models.BondParams
import com.nxtru.bondscalc.domain.models.BondResults
import com.nxtru.bondscalc.domain.usecase.BondCalcUseCase
import com.nxtru.bondscalc.domain.usecase.SaveBondParamsUseCase
import com.nxtru.bondscalc.domain.usecase.LoadBondParamsUseCase

// https://developer.android.com/kotlin/coroutines/coroutines-best-practices

private const val TAG = "MainViewModel"

class MainViewModel(
    private val saveBondParamsUseCase: SaveBondParamsUseCase,
    private val loadBondParamsUseCase: LoadBondParamsUseCase
) : ViewModel() {

    var bondParams by mutableStateOf(BondParams.EMPTY)
        private set

    var calcResult by mutableStateOf(BondCalcUIResult.UNDEFINED)
        private set

    var tickerSelectionState by mutableStateOf(TickerSelectionUIState(""))
        private set

    fun onBondParamsChange(value: BondParams) {
        bondParams = value
        updateTickerSelectionTicker()
        saveBondParams()
        calculate()
    }

    private val bondCalcUseCase = BondCalcUseCase()

    init {
        loadBondParams()
        calculate()
    }

    private fun saveBondParams() {
        saveBondParamsUseCase.execute(bondParams)
    }

    private fun loadBondParams() {
        bondParams = loadBondParamsUseCase.execute()
        updateTickerSelectionTicker()
    }

    private fun updateTickerSelectionTicker() {
        if (tickerSelectionState.ticker != bondParams.ticker)
            tickerSelectionState = tickerSelectionState.copy(ticker = bondParams.ticker)
    }

    private fun calculate() {
        Log.d(TAG, "params = $bondParams")
        val res = bondCalcUseCase.execute(bondParams)
        Log.d(TAG, "result = $res")

        calcResult = toUIResults(res)
    }
}

// TODO: should be use case?
private fun toUIResults(value: BondResults?): BondCalcUIResult =
    if (value == null) BondCalcUIResult.UNDEFINED
    else BondCalcUIResult(
        String.format("%,.2f₽", value.income),
        String.format("%,.2f", value.ytm * 100) + "%"
    )

data class BondCalcUIResult(
    // income in roubles
    val income: String,
    val ytm: String,
) {
    companion object {
        val UNDEFINED = BondCalcUIResult("", "")
    }
}

data class TickerSelectionUIState(
    val ticker: String,
    val searching: Boolean = false,
    val foundTickers: List<String>? = null
)
