package com.nxtru.bondscalc.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.nxtru.bondscalc.R
import com.nxtru.bondscalc.domain.models.BondParams
import com.nxtru.bondscalc.presentation.ui.theme.MainTheme
import com.nxtru.bondscalc.presentation.widgets.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
//        window.setDecorFitsSystemWindows(false)
        setContent { MainScreen(viewModel(factory = MainViewModelFactory(applicationContext))) }
    }
}

@Composable
fun MainScreen(viewModel: MainViewModel) {
    MainContent(
        bondParams = viewModel.bondParams,
        calcResult = viewModel.calcResult,
        tickerSelectionState = viewModel.tickerSelectionState,
        onBondParamsChange = { viewModel.onBondParamsChange(it) },
        onSearchTicker = {},
        onTickerSelectionDone = {},
        onTickerSelectionCancel = {},
    )
}

@Composable
fun MainContent(
    bondParams: BondParams,
    calcResult: BondCalcUIResult,
    tickerSelectionState: TickerSelectionUIState,
    onBondParamsChange: (BondParams) -> Unit,
    onSearchTicker: (String) -> Unit,
    onTickerSelectionDone: (String) -> Unit,
    onTickerSelectionCancel: () -> Unit = {},
) {
    return MainTheme {
        // A surface container using the 'background' color from the theme
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            // https://stackoverflow.com/a/72608560
//            modifier = Modifier
//                .fillMaxSize()
//                .statusBarsPadding()
//                .navigationBarsPadding()
//                .imePadding()
//                .verticalScroll(rememberScrollState()),
            color = MaterialTheme.colorScheme.background
        ) {
            val padding = 8.dp
            val paddingModifier = Modifier.padding(padding)
            Column(
                modifier = paddingModifier //.verticalScroll(rememberScrollState())
            ) {
                TickerField(
                    value = tickerSelectionState.ticker,
                    onValueChange = { onBondParamsChange(bondParams.copy(ticker = it)) },
                    modifier = Modifier.fillMaxWidth(),
                    tickers = tickerSelectionState.foundTickers,
                    searching = tickerSelectionState.searching,
                    onSearchTicker = onSearchTicker,
                    onSelectionDone = onTickerSelectionDone,
                    onSelectionCancel = onTickerSelectionCancel
                )
                Row(modifier = Modifier.fillMaxWidth()) {
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
                Row(modifier = Modifier.fillMaxWidth()) {
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
                Header(stringResource(R.string.buy))
                Row(modifier = Modifier.fillMaxWidth()) {
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
                Header(stringResource(R.string.sell))
                Row(
                    modifier = Modifier.fillMaxWidth(),
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
                Row(modifier = Modifier.fillMaxWidth()) {
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
                    modifier = Modifier
                        .padding(vertical = padding)
                        .fillMaxWidth(),

//                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary)
                ) {
                    Column(
                        modifier = paddingModifier,
                        verticalArrangement = Arrangement.spacedBy(padding),
                    ) {
                        Header(stringResource(R.string.result))
                        ResultRow(stringResource(R.string.result_rub), calcResult.income)
                        ResultRow(stringResource(R.string.result_percent), calcResult.ytm)
                    }
                }
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

@OptIn(ExperimentalMaterial3Api::class)
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

@OptIn(ExperimentalMaterial3Api::class)
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

@OptIn(ExperimentalMaterial3Api::class)
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
fun Header(text: String) {
    Text(
        text = text,
        color = MaterialTheme.colorScheme.secondary,
        style = MaterialTheme.typography.titleLarge
    )
}

@Preview(name = "Light Mode")
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
        onBondParamsChange = {},
        tickerSelectionState = TickerSelectionUIState("ОФЗ"),
        onSearchTicker = {},
        onTickerSelectionDone = {},
        onTickerSelectionCancel = {}
    )
}
