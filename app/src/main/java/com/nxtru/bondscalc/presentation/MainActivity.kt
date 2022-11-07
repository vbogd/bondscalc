@file:OptIn(ExperimentalMaterial3Api::class)

package com.nxtru.bondscalc.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.nxtru.bondscalc.R
import com.nxtru.bondscalc.domain.models.BondInfo
import com.nxtru.bondscalc.domain.models.BondParams
import com.nxtru.bondscalc.presentation.ui.theme.MainTheme
import com.nxtru.bondscalc.presentation.components.*
import com.nxtru.bondscalc.presentation.models.MainUIState
import com.nxtru.bondscalc.presentation.models.SearchScreenUIState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.emptyFlow

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        // see https://developer.android.com/reference/kotlin/androidx/compose/foundation/layout/package-summary#(androidx.compose.ui.Modifier).imePadding()
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)
        setContent {
            MainScreen(viewModel(factory = MainViewModelFactory(applicationContext)))
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
        onSearch = {
//            viewModel.onUIStateChange(uiState.copy(searchScreenUIState = uiState.searchScreenUIState.copy(isSearching = true)))
        },
        onSelected = {

        },
    )
}

@Composable
fun MainScreenOld(viewModel: MainViewModel) {
    MainContent(
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

@Composable
fun MainContent(
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
    return MainTheme {
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
            val padding = 8.dp
            val rowModifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = padding)
            Column(
                modifier = Modifier.padding(contentPadding)
            ) {
                TickerField(
                    value = tickerSelectionState.ticker,
                    onValueChange = { onBondParamsChange(bondParams.copy(ticker = it)) },
                    modifier = rowModifier,
                    tickers = tickerSelectionState.foundTickers,
                    searching = tickerSelectionState.searching,
                    onSearchTicker = onSearchTicker,
                    onSelectionDone = onTickerSelectionDone,
                    onSelectionCancel = onTickerSelectionCancel
                )
                Row(modifier = rowModifier) {
                    Column(Modifier.weight(1f)) {
                        NumericField(stringResource(R.string.commission), bondParams.commission) {
                            onBondParamsChange(bondParams.copy(commission = it))
                        }
                    }
                    Spacer(modifier = Modifier.width(padding))
                    Column(Modifier.weight(1f)) {
                        NumericField(stringResource(R.string.tax), bondParams.tax) {
                            onBondParamsChange(bondParams.copy(tax = it))
                        }
                    }
                }
                Row(modifier = rowModifier) {
                    Column(Modifier.weight(1f)) {
                        MoneyField(stringResource(R.string.par_value), bondParams.parValue) {
                            onBondParamsChange(bondParams.copy(parValue = it))
                        }
                    }
                    Spacer(modifier = Modifier.width(padding))
                    Column(Modifier.weight(1f)) {
                        NumericField(stringResource(R.string.coupon), bondParams.coupon) {
                            onBondParamsChange(bondParams.copy(coupon = it))
                        }
                    }
                }
                Header(
                    text = stringResource(R.string.buy),
                    modifier = rowModifier,
                )
                Row(modifier = rowModifier) {
                    Column(Modifier.weight(1f)) {
                        DateField(stringResource(R.string.date), bondParams.buyDate) {
                            onBondParamsChange(bondParams.copy(buyDate = it))
                        }
                    }
                    Spacer(modifier = Modifier.width(padding))
                    Column(Modifier.weight(1f)) {
                        NumericField(stringResource(R.string.price), bondParams.buyPrice) {
                            onBondParamsChange(bondParams.copy(buyPrice = it))
                        }
                    }
                }
                Header(
                    text = stringResource(R.string.sell),
                    modifier = rowModifier
                )
                Row(
                    modifier = rowModifier,
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        color = MaterialTheme.colorScheme.secondary,
                        text = stringResource(R.string.till_maturity)
                    )
                    Switch(checked = bondParams.tillMaturity, onCheckedChange = {
                        onBondParamsChange(bondParams.copy(tillMaturity = it))
                    })
                }
                Row(modifier = rowModifier) {
                    Column(Modifier.weight(1f)) {
                        DateField(stringResource(R.string.date), bondParams.sellDate) {
                            onBondParamsChange(bondParams.copy(sellDate = it))
                        }
                    }
                    Spacer(modifier = Modifier.width(padding))
                    Column(Modifier.weight(1f)) {
                        NumericField(stringResource(R.string.price), bondParams.sellPrice) {
                            onBondParamsChange(bondParams.copy(sellPrice = it))
                        }
                    }
                }
                Card(
                    modifier = rowModifier
                        .padding(vertical = padding),

//                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary)
                ) {
                    val paddingModifier = Modifier.padding(padding)
                    Column(
                        modifier = paddingModifier,
                        verticalArrangement = Arrangement.spacedBy(padding),
                    ) {
                        Header(stringResource(R.string.result))
                        ResultRow(stringResource(R.string.result_rub), calcResult.income)
                        ResultRow(stringResource(R.string.result_percent), calcResult.ytm)
                    }
                }
                Text(text = bondInfo?.toString() ?: "null")
            }
        }
    }
}

@Composable
fun ResultRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            color = MaterialTheme.colorScheme.secondary,
        )
        Text(
            text = value,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun NumericField(label: String, value: String, onValueChange: (String) -> Unit) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier.fillMaxWidth(),
        label = { Text(label) },
        singleLine = true,
        trailingIcon = { Text("%") },
        textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.End),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
    )
}

@Composable
fun MoneyField(label: String, value: String, onValueChange: (String) -> Unit) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier.fillMaxWidth(),
        label = { Text(label) },
        singleLine = true,
        trailingIcon = { Text("₽") },
        textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.End),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
    )
}

@Composable
fun DateField(label: String, value: String, onValueChange: (String) -> Unit) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier.fillMaxWidth(),
        label = { Text(label) },
        singleLine = true,
        placeholder = { Text("DD.MM.YYYY") },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
    )
}

@Composable
fun Header(text: String, modifier: Modifier = Modifier) {
    Text(
        text = text,
        modifier = modifier,
        color = MaterialTheme.colorScheme.secondary,
        style = MaterialTheme.typography.titleLarge
    )
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
    MainContent(
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

private fun getErrorMessage(errCode: Int): String {
    // TODO: load resource string
    return if (errCode == R.string.failed_to_load)
        "Ошибка загрузки"
    else "Ошибка $errCode"
}
