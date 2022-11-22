package com.nxtru.bondscalc.presentation

import androidx.arch.core.executor.ArchTaskExecutor
import androidx.arch.core.executor.TaskExecutor
import com.nxtru.bondscalc.domain.models.BondParams
import com.nxtru.bondscalc.domain.usecase.*
import com.nxtru.bondscalc.domain.usecase.bondinfo.*
import com.nxtru.bondscalc.presentation.models.MainUIState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.never

@OptIn(ExperimentalCoroutinesApi::class)
internal class MainViewModelTest {
    private val saveBondParamsUseCase = mock<SaveBondParamsUseCase>()
    private val loadBondParamsUseCase = mock<LoadBondParamsUseCase>()
    private val saveBondInfoUseCase = mock<SaveBondInfoUseCase>()
    private val loadBondInfoUseCase = mock<LoadBondInfoUseCase>()
    private val searchTickersUseCase = mock<SearchTickersUseCase>()
    private val loadBondInfoDataUseCase = mock<LoadBondInfoDataUseCase>()

    private val testDispatcher: TestDispatcher = UnconfinedTestDispatcher()

    @BeforeEach
    fun setUp() {
        ArchTaskExecutor.getInstance().setDelegate(object : TaskExecutor() {
            override fun executeOnDiskIO(runnable: Runnable) {
                runnable.run()
            }

            override fun postToMainThread(runnable: Runnable) {
                runnable.run()
            }

            override fun isMainThread(): Boolean {
                return true
            }
        })
        Dispatchers.setMain(testDispatcher)
    }

    @AfterEach
    fun tearDown() {
        Mockito.reset(
            saveBondParamsUseCase,
            loadBondParamsUseCase,
            searchTickersUseCase,
            loadBondInfoDataUseCase
        )
        ArchTaskExecutor.getInstance().setDelegate(null)
        Dispatchers.resetMain()
    }

    @Test
    fun getUiStateFlow() {
        val expected = createUIState(bondParams = testBondParams, calcResult = testCalcResult)
        val actual = createViewModel(testBondParams).uiStateFlow.value

        assertEquals(expected, actual)
    }

    @Test
    @Disabled
    fun getErrorMessageCode() {
        // TODO: implement
    }

    @Test
    @Disabled
    fun onUIStateChange() {
        // TODO: implement
    }

    @Test
    @Disabled
    fun onSearchScreenSearch() {
        // TODO: implement
    }

    @Test
    @Disabled
    fun onTickerSelectionDone() {
        // TODO: implement
    }

    @Test
    fun `onCalculatorScreenRefresh with empty secId`() = runTest {
        createViewModel().onCalculatorScreenRefresh()

        Mockito.verify(loadBondInfoDataUseCase, never()).invoke(any())
    }

    @Test
    @Disabled
    fun `onCalculatorScreenRefresh with non-empty secId`() {
        // TODO: implement
    }

    private fun createViewModel(bondParams: BondParams = BondParams.EMPTY): MainViewModel {
        Mockito.`when`(loadBondParamsUseCase.execute()).thenReturn(bondParams)
        return MainViewModel(
            saveBondParamsUseCase,
            loadBondParamsUseCase,
            saveBondInfoUseCase,
            loadBondInfoUseCase,
            searchTickersUseCase,
            loadBondInfoDataUseCase
        )
    }
}

private val testBondParams = BondParams(
    ticker = "",
    commission = "0.05",
    tax = "13",
    coupon = "5",
    parValue = "1000",
    buyPrice = "97.10",
    buyDate = "26.03.2021",
    sellPrice = "99",
    sellDate = "06.05.2021",
    tillMaturity = false
)

private val testCalcResult = BondCalcUIResult(
    income="20,56 ₽",
    ytm="18,83 %",
    currentYield="4,48 %"
)

private fun createUIState(
    bondParams: BondParams = BondParams.EMPTY,
    calcResult: BondCalcUIResult = BondCalcUIResult.UNDEFINED,
): MainUIState {
    val res = MainUIState()
    return res.copy(
        calculatorScreenUIState = res.calculatorScreenUIState.copy(
            bondParams = bondParams,
            calcResult = calcResult
        )
    )
}
