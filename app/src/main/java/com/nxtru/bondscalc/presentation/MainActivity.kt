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
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
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
            }
        }
    }
}

@Composable
fun MainScreenOld(viewModel: MainViewModel) {
    val uiState: MainUIState by viewModel.uiStateFlow.collectAsState()

    MainContent(
        uiState = uiState,
        onUIStateChange = viewModel::onUIStateChange,
        onSearchScreenSearch = viewModel::onSearchScreenSearch,
        bondParams = viewModel.bondParams,
        calcResult = viewModel.calcResult,
        bondInfo = viewModel.bondInfo,
        errorMessageCode = viewModel.errorMessageCode,
        onBondParamsChange = viewModel::onBondParamsChange,
        onTickerSelectionDone = viewModel::onTickerSelectionDone,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainContent(
    uiState: MainUIState,
    onUIStateChange: (MainUIState) -> Unit,
    onSearchScreenSearch: (String) -> Unit,
    bondParams: BondParams,
    calcResult: BondCalcUIResult,
    bondInfo: BondInfo?,
    errorMessageCode: Flow<Int>,
    onBondParamsChange: (BondParams) -> Unit,
    onTickerSelectionDone: (String) -> Unit,
) {
    // see https://blog.devgenius.io/snackbars-in-jetpack-compose-d1b553224dca
    // see https://stackoverflow.com/a/73006218
    val snackbarHostState = remember { SnackbarHostState() }
    val navController = rememberNavController()
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
            .fillMaxSize(),
//                .statusBarsPadding()
//            .navigationBarsPadding()
//            .imePadding(),
//                .verticalScroll(rememberScrollState()),
        bottomBar = { BottomBar(navController) },
    ) { contentPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Calculator.route,
            modifier = Modifier.padding(contentPadding),
        ) {
            composable(
                Screen.Calculator.route,
                arguments = listOf(navArgument(NavArgument.secId) { defaultValue = "" })
            ) { backStackEntry ->
                val secId = backStackEntry.arguments?.getString(NavArgument.secId) ?: ""
                if (secId.isNotEmpty()) onTickerSelectionDone(secId)
                CalculatorScreen(
                    modifier = Modifier.fillMaxWidth(),
                    bondParams = bondParams,
                    calcResult = calcResult,
                    bondInfo = bondInfo,
                    onBondParamsChange = onBondParamsChange,
                )
            }
            composable(Screen.Search.route) {
                SearchScreen(
                    uiState = uiState.searchScreenUIState,
                    onUIStateChange = {
                        onUIStateChange(uiState.copy(searchScreenUIState = it))
                    },
                    onSearch = onSearchScreenSearch,
                    onSelected = {
                        navController.navigate(getCalculatorRoute(it.secId))
                    },
                )
            }
        }
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
    var uiState by remember { mutableStateOf(MainUIState()) }
    MainTheme {
        MainContent(
            uiState = uiState,
            onUIStateChange = { uiState = it},
            onSearchScreenSearch = {},
            bondParams = BondParams.EMPTY,
            calcResult = BondCalcUIResult("14.5₽", "9.5%"),
            bondInfo = null,
            errorMessageCode = emptyFlow(),
            onBondParamsChange = {},
            onTickerSelectionDone = {},
        )
    }
}

private fun getErrorMessage(errCode: Int): String {
    // TODO: load resource string
    return if (errCode == R.string.failed_to_load)
        "Ошибка загрузки"
    else "Ошибка $errCode"
}
