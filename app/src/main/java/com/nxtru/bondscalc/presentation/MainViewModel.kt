package com.nxtru.bondscalc.presentation

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.nxtru.bondscalc.domain.models.BondParams
import com.nxtru.bondscalc.domain.usecase.BondCalcUseCase
import com.nxtru.bondscalc.domain.usecase.SaveBondParamsUseCase
import com.nxtru.bondscalc.domain.usecase.LoadBondParamsUseCase

private const val TAG = "MainViewModel"

class MainViewModel(
    private val saveBondParamsUseCase: SaveBondParamsUseCase,
    private val loadBondParamsUseCase: LoadBondParamsUseCase
) : ViewModel() {

    var bondParams by mutableStateOf(BondParams.EMPTY)
        private set

    var calcResult by mutableStateOf("")
        private set

    fun onBondParamsChange(value: BondParams) {
        bondParams = value
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
    }

    private fun calculate() {
        Log.d(TAG, "params = $bondParams")
        val res = bondCalcUseCase.execute(bondParams)
        Log.d(TAG, "result = $res")

        if (res == null) { noResult(); return }

        val resultRub = String.format("%,.2f₽", res.income)
        val resultYTM = String.format("%,.2f", res.ytm * 100) + "%"

        calcResult = "$resultRub; $resultYTM"
    }

    private fun noResult() {
        calcResult = ""
    }
}
