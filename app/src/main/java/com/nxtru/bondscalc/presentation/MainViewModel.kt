package com.nxtru.bondscalc.presentation

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.MutableLiveData
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

    // input fields
    val ticker = MutableLiveData<String>()
    val commission = MutableLiveData<String>()
    val tillMaturity = MutableLiveData<Boolean>()
    val tax = MutableLiveData<String>()
    val coupon = MutableLiveData<String>()
    val parValue = MutableLiveData<String>()
    val buyPrice = MutableLiveData<String>()
    val buyDate = MutableLiveData<String>()
    val sellPrice = MutableLiveData<String>()
    val sellDate = MutableLiveData<String>()

    // output fields
    val resultRubLive = MutableLiveData<String>()
    val resultYTMLive = MutableLiveData<String>()

    private val bondCalcUseCase = BondCalcUseCase()

    init {
        loadBondParams()
        calculate()
    }

    fun setTicker(value: String) = setAndUpdate(ticker, value)
    fun setCommission(value: String) = setAndUpdate(commission, value)
    fun setTillMaturity(value: Boolean) = setAndUpdate(tillMaturity, value)
    fun setTax(value: String) = setAndUpdate(tax, value)
    fun setCoupon(value: String) = setAndUpdate(coupon, value)
    fun setParValue(value: String) = setAndUpdate(parValue, value)
    fun setBuyPrice(value: String) = setAndUpdate(buyPrice, value)
    fun setBuyDate(value: String) = setAndUpdate(buyDate, value)
    fun setSellPrice(value: String) = setAndUpdate(sellPrice, value)
    fun setSellDate(value: String) = setAndUpdate(sellDate, value)

    private fun <T> setAndUpdate(liveData: MutableLiveData<T>, value: T) {
        liveData.value = value
        saveBondParams()
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

        resultRubLive.value = String.format("%,.2f₽", res.income)
        resultYTMLive.value = String.format("%,.2f", res.ytm * 100) + "%"

        calcResult = "${resultRubLive.value}; ${resultYTMLive.value}"
    }

    private fun noResult() {
        resultRubLive.value = ""
        resultYTMLive.value = ""
    }

}
