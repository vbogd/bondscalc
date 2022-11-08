package com.nxtru.bondscalc.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.nxtru.bondscalc.R
import com.nxtru.bondscalc.domain.models.BondInfo
import com.nxtru.bondscalc.domain.models.BondParams
import com.nxtru.bondscalc.presentation.ui.theme.MainTheme
import com.nxtru.bondscalc.presentation.components.*
import com.nxtru.bondscalc.presentation.models.MainUIState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.emptyFlow

class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels {
        MainViewModelFactory(applicationContext)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        // see https://developer.android.com/reference/kotlin/androidx/compose/foundation/layout/package-summary#(androidx.compose.ui.Modifier).imePadding()
//        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)
        setContent {
            MainTheme {
                MainScreenOld(viewModel)
//                MyAppNavHost(modifier = Modifier.fillMaxSize())
            }
        }
    }
}

@Composable
fun MainScreen(viewModel: MainViewModel) {
    val uiState: MainUIState by viewModel.uiStateFlow.collectAsState()
    SearchScreen(
        uiState = uiState.searchScreenUIState,
        onUIStateChange = {
            viewModel.onUIStateChange(uiState.copy(searchScreenUIState = it))
        },
        onSearch = viewModel::onSearchScreenSearch,
        onSelected = {},
    )
}

@Composable
fun MainScreenOld(viewModel: MainViewModel) {
    MainContentOld(
        bondParams = viewModel.bondParams,
        calcResult = viewModel.calcResult,
        bondInfo = viewModel.bondInfo,
        tickerSelectionState = viewModel.tickerSelectionState,
        errorMessageCode = viewModel.errorMessageCode,
        onBondParamsChange = viewModel::onBondParamsChange,
        onSearchTicker = viewModel::onSearchTicker,
        onTickerSelectionDone = viewModel::onTickerSelectionDone,
        onTickerSelectionCancel = viewModel::onTickerSelectionCancel,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainContentOld(
    bondParams: BondParams,
    calcResult: BondCalcUIResult,
    bondInfo: BondInfo?,
    tickerSelectionState: TickerSelectionUIState,
    errorMessageCode: Flow<Int>,
    onBondParamsChange: (BondParams) -> Unit,
    onSearchTicker: (String) -> Unit,
    onTickerSelectionDone: (String) -> Unit,
    onTickerSelectionCancel: () -> Unit = {},
) {
    // see https://blog.devgenius.io/snackbars-in-jetpack-compose-d1b553224dca
    // see https://stackoverflow.com/a/73006218
    val snackbarHostState = remember { SnackbarHostState() }
    LaunchedEffect(Unit) {
        errorMessageCode.collectLatest { errCode ->
            snackbarHostState.showSnackbar(
                message = getErrorMessage(errCode),
                duration = SnackbarDuration.Short
            )
        }
    }
    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        // https://stackoverflow.com/a/72608560
        modifier = Modifier
//                .fillMaxSize()
//                .statusBarsPadding()
//                .navigationBarsPadding()
            .imePadding()
//                .verticalScroll(rememberScrollState()),
    ) { contentPadding ->
        CalculatorScreen(
            modifier = Modifier.padding(contentPadding),
            bondParams = bondParams,
            calcResult = calcResult,
            bondInfo = bondInfo,
            tickerSelectionState = tickerSelectionState,
            onBondParamsChange = onBondParamsChange,
            onSearchTicker = onSearchTicker,
            onTickerSelectionDone = onTickerSelectionDone,
            onTickerSelectionCancel = onTickerSelectionCancel,
        )
    }
}

@Preview(
    name = "Light Mode"
)
//@Preview(
//    uiMode = Configuration.UI_MODE_NIGHT_YES,
//    showBackground = true,
//    name = "Dark Mode"
//)
@Composable
fun PreviewMessageCard() {
    MainTheme {
//        MyAppNavHost(
//            modifier = Modifier.fillMaxSize()
//        )
        MainContentOld(
            BondParams.EMPTY,
            BondCalcUIResult("14.5₽", "9.5%"),
            bondInfo = null,
            errorMessageCode = emptyFlow(),
            onBondParamsChange = {},
            tickerSelectionState = TickerSelectionUIState("ОФЗ"),
            onSearchTicker = {},
            onTickerSelectionDone = {},
            onTickerSelectionCancel = {}
        )
    }
}

private fun getErrorMessage(errCode: Int): String {
    // TODO: load resource string
    return if (errCode == R.string.failed_to_load)
        "Ошибка загрузки"
    else "Ошибка $errCode"
}
