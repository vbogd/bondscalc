package com.nxtru.bondscalc.presentation

import android.util.Log
import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nxtru.bondscalc.domain.models.BondParams
import com.nxtru.bondscalc.domain.models.BondResults
import com.nxtru.bondscalc.domain.usecase.*
import com.nxtru.bondscalc.domain.usecase.bondinfo.*
import kotlinx.coroutines.launch
import com.nxtru.bondscalc.R
import com.nxtru.bondscalc.domain.models.BondInfo
import com.nxtru.bondscalc.presentation.models.*
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow

private const val TAG = "MainViewModel"

class MainViewModel(
    private val saveBondParamsUseCase: SaveBondParamsUseCase,
    private val loadBondParamsUseCase: LoadBondParamsUseCase,
    private val searchTickersUseCase: SearchTickersUseCase,
    private val loadBondInfoUseCase: LoadBondInfoUseCase,
) : ViewModel() {

    /*
     * State.
     */
    private val _uiStateFlow = MutableStateFlow(MainUIState())
    val uiStateFlow: StateFlow<MainUIState> = _uiStateFlow

    // see https://blog.devgenius.io/snackbars-in-jetpack-compose-d1b553224dca
    private val _errorMessageCode = MutableSharedFlow<Int>()
    val errorMessageCode = _errorMessageCode.asSharedFlow()

    private val bondCalcUseCase = BondCalcUseCase()

    init {
        viewModelScope.launch {
            loadBondParams()
            calculate()
        }
    }

    fun onUIStateChange(value: MainUIState) {
        viewModelScope.launch {
            _uiStateFlow.emit(value)
        }
    }

    fun onSearchScreenSearch(pattern: String) {
        viewModelScope.launch {
            if (pattern.length < 3) {
                updateSearchScreenUIState {
                    copy(
                        isSearching = false,
                        messageId = R.string.search_field_too_short,
                        tickers = emptyList(),
                    )
                }
                return@launch
            }
            updateSearchScreenUIState {
                copy(isSearching = true)
            }
            val foundTickers = searchTickersUseCase(pattern)
            if (foundTickers == null) {
                showError(R.string.failed_to_load)
            }
            val tickers = foundTickers ?: emptyList()
            updateSearchScreenUIState {
                copy(
                    tickers = tickers,
                    messageId = if (tickers.isEmpty()) R.string.not_found else R.string.empty,
                    isSearching = false
                )
            }
        }
    }

    fun onBondParamsChange(value: BondParams) {
        viewModelScope.launch {
            updateCalculatorScreenUIState {
                copy(bondParams = value)
            }
            saveBondParams()
            calculate()
        }
    }

    fun onTickerSelectionDone(secId: String) {
        if (uiState().calculatorScreenUIState.bondInfo?.secId == secId) return
        viewModelScope.launch {
            // TODO: show loading indicator
            val bondInfo = loadBondInfoUseCase(secId)
            if (bondInfo == null) showError(R.string.failed_to_load)
            onUpdateBondInfo(bondInfo)
        }
    }

    /*
     * Aux functions.
     */
    private fun uiState() = uiStateFlow.value
    private fun calculatorScreenUIState() = uiState().calculatorScreenUIState

    private suspend fun updateUIState(value: MainUIState) {
        _uiStateFlow.emit(value)
    }

    private suspend fun updateUIState(modifier: MainUIState.() -> MainUIState) {
        updateUIState(modifier.invoke(uiStateFlow.value))
    }

    private suspend fun updateSearchScreenUIState(
        modifier: SearchScreenUIState.() -> SearchScreenUIState
    ) {
        updateUIState {
            val newState = modifier.invoke(searchScreenUIState)
            copy(searchScreenUIState = newState)
        }
    }

    private suspend fun updateCalculatorScreenUIState(
        modifier: CalculatorScreenUIState.() -> CalculatorScreenUIState
    ) {
        updateUIState {
            val newState = modifier.invoke(calculatorScreenUIState)
            copy(calculatorScreenUIState = newState)
        }
    }

    // TODO: simplify
    private fun onUpdateBondInfo(value: BondInfo?) {
        viewModelScope.launch {
            updateCalculatorScreenUIState {
                var newBondParams = bondParams.copy(
                    ticker = value?.ticker ?: "",
                    coupon = value?.coupon ?: "",
                    parValue = value?.parValue ?: "",
                )
//        if (bondParams.tillMaturity) {
                newBondParams = newBondParams.copy(
                    sellDate = value?.maturityDate ?: "",
                    sellPrice = "100",
                )
//        }
                copy(
                    bondInfo = value,
                    bondParams = newBondParams
                )
            }
            saveBondParams()
            calculate()
        }
    }

    private suspend fun showError(@StringRes msgId: Int) {
        _errorMessageCode.emit(msgId)
    }

    private fun saveBondParams() {
        saveBondParamsUseCase.execute(calculatorScreenUIState().bondParams)
    }

    private suspend fun loadBondParams() {
        updateCalculatorScreenUIState {
            copy(bondParams = loadBondParamsUseCase.execute())
        }
    }

    private suspend fun calculate() {
        val bondParams = calculatorScreenUIState().bondParams
        Log.d(TAG, "params = $bondParams")
        val res = bondCalcUseCase.execute(bondParams)
        Log.d(TAG, "result = $res")

        updateCalculatorScreenUIState {
            copy(calcResult = toUIResults(res))
        }
    }
}

// TODO: should be use case?
private fun toUIResults(value: BondResults?): BondCalcUIResult =
    if (value == null) BondCalcUIResult.UNDEFINED
    else BondCalcUIResult(
        String.format("%,.2f ₽", value.income),
        String.format("%,.2f", value.ytm * 100) + " %"
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
