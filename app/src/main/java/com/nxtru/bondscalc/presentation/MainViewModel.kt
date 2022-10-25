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

    fun onBondParamsChange(value: BondParams) {
        bondParams = value
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
        saveBondParamsUseCase.execute(BondParams(
            ticker = ticker.value ?: "",
            commission = commission.value ?: "",
            tax = tax.value ?: "",
            coupon = coupon.value ?: "",
            parValue = parValue.value ?: "",
            buyPrice = buyPrice.value ?: "",
            buyDate = buyDate.value ?: "",
            sellPrice = sellPrice.value ?: "",
            sellDate = sellDate.value ?: "",
            tillMaturity = tillMaturity.value ?: false
        ))
    }

    private fun loadBondParams() {
        val params = loadBondParamsUseCase.execute()
        ticker.value = params.ticker
        commission.value = params.commission
        tax.value = params.tax
        coupon.value = params.coupon
        parValue.value = params.parValue
        buyPrice.value = params.buyPrice
        buyDate.value = params.buyDate
        sellPrice.value = params.sellPrice
        sellDate.value = params.sellDate
        tillMaturity.value = params.tillMaturity
    }

    private fun calculate() {
        val params = loadBondParamsUseCase.execute()
        Log.d(TAG, "params = $params")
        val res = bondCalcUseCase.execute(params)
        Log.d(TAG, "result = $res")

        if (res == null) { noResult(); return }

        resultRubLive.value = String.format("%,.2f₽", res.income)
        resultYTMLive.value = String.format("%,.2f", res.ytm * 100) + "%"
    }

    private fun noResult() {
        resultRubLive.value = ""
        resultYTMLive.value = ""
    }

}
