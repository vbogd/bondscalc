package com.nxtru.bondscalc.presentation

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nxtru.bondscalc.domain.models.BondParams
import com.nxtru.bondscalc.domain.models.BondResults
import com.nxtru.bondscalc.domain.usecase.*
import com.nxtru.bondscalc.domain.usecase.bondinfo.*
import kotlinx.coroutines.launch
import com.nxtru.bondscalc.R
import com.nxtru.bondscalc.domain.models.BondInfo
import com.nxtru.bondscalc.domain.models.BriefBondInfo
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

    var bondParams by mutableStateOf(BondParams.EMPTY)
        private set
    var calcResult by mutableStateOf(BondCalcUIResult.UNDEFINED)
        private set
    var bondInfo by mutableStateOf<BondInfo?>(null)
        private set
    var tickerSelectionState by mutableStateOf(TickerSelectionUIState(""))
        private set
    // see https://blog.devgenius.io/snackbars-in-jetpack-compose-d1b553224dca
    private val _errorMessageCode = MutableSharedFlow<Int>()
    val errorMessageCode = _errorMessageCode.asSharedFlow()
    private var foundTickers: List<BriefBondInfo> = emptyList()

    private val bondCalcUseCase = BondCalcUseCase()

    init {
        loadBondParams()
        calculate()
    }

    fun onUIStateChange(value: MainUIState) {
        viewModelScope.launch {
            _uiStateFlow.emit(value)
        }
    }

    fun onSearchScreenSearch(pattern: String) {
        viewModelScope.launch {
            updateSearchScreenUIState {
                copy(isSearching = true)
            }
            val foundTickers = searchTickersUseCase(pattern)
            if (foundTickers == null) {
                showError(R.string.failed_to_load)
            }
            updateSearchScreenUIState {
                copy(
                    tickers = foundTickers ?: emptyList(),
                    isSearching = false
                )
            }
        }
    }

    fun onBondParamsChange(value: BondParams) {
        bondParams = value
        updateTickerSelectionTicker()
        saveBondParams()
        calculate()
    }

    /*
     * Ticker selection callbacks.
     */
    fun onSearchTicker(ticker: String) {
        viewModelScope.launch {
            tickerSelectionState = tickerSelectionState.copy(searching = true)
            val foundTickers = searchTickersUseCase(ticker)
            tickerSelectionState = tickerSelectionState.copy(
                foundTickers = foundTickers?.map(BriefBondInfo::ticker),
                searching = false,
            )
            this@MainViewModel.foundTickers = foundTickers ?: emptyList()
            if (foundTickers == null) {
                showError(R.string.failed_to_load)
            }
        }
    }

    fun onTickerSelectionDone(ticker: String) {
        onBondParamsChange(
            bondParams.copy(ticker = ticker)
        )
        tickerSelectionState = tickerSelectionState.copy(
            foundTickers = null,
        )
        val secId = foundTickers.find { it.ticker == ticker }?.secId
        if (secId == null)
            onUpdateBondInfo(null)
        else {
            viewModelScope.launch {
                val bondInfo = loadBondInfoUseCase(secId)
                if (bondInfo == null) showError(R.string.failed_to_load)
                onUpdateBondInfo(bondInfo)
            }
        }
    }

    fun onTickerSelectionCancel() {
        tickerSelectionState = tickerSelectionState.copy(
            foundTickers = null,
            searching = false,
        )
    }

    /*
     * Aux functions.
     */
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

    private fun onUpdateBondInfo(value: BondInfo?) {
        bondInfo = value
        var newBondParams = bondParams.copy(
            coupon = value?.coupon ?: "",
            parValue = value?.parValue ?: "",
        )
        if (bondParams.tillMaturity) {
            newBondParams = newBondParams.copy(
                sellDate = value?.maturityDate ?: "",
                sellPrice = "100",
            )
        }
        onBondParamsChange(newBondParams)
    }

    private suspend fun showError(msgId: Int) {
        _errorMessageCode.emit(msgId)
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
    val foundTickers: List<String>? = null,
)
