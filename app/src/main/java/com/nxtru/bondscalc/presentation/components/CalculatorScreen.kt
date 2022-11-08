package com.nxtru.bondscalc.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.nxtru.bondscalc.R
import com.nxtru.bondscalc.domain.models.BondInfo
import com.nxtru.bondscalc.domain.models.BondParams
import com.nxtru.bondscalc.presentation.*

@Composable
fun CalculatorScreen(
    modifier: Modifier = Modifier,
    bondParams: BondParams,
    calcResult: BondCalcUIResult,
    bondInfo: BondInfo?,
    tickerSelectionState: TickerSelectionUIState,
    onBondParamsChange: (BondParams) -> Unit,
    onSearchTicker: (String) -> Unit,
    onTickerSelectionDone: (String) -> Unit,
    onTickerSelectionCancel: () -> Unit = {},
) {
    val padding = 8.dp
    val rowModifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = padding)
    Column(
        modifier = modifier
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
fun Header(text: String, modifier: Modifier = Modifier) {
    Text(
        text = text,
        modifier = modifier,
        color = MaterialTheme.colorScheme.secondary,
        style = MaterialTheme.typography.titleLarge
    )
}
