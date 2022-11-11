package com.nxtru.bondscalc.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nxtru.bondscalc.R
import com.nxtru.bondscalc.presentation.*
import com.nxtru.bondscalc.presentation.models.CalculatorScreenUIState
import com.nxtru.bondscalc.presentation.models.bondParams

@Composable
fun CalculatorScreen(
    uiState: CalculatorScreenUIState,
    onUIStateChange: (CalculatorScreenUIState) -> Unit,
    modifier: Modifier = Modifier,
) {
    val padding = 8.dp
    val rowModifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = padding)
    Column(
        modifier = modifier
    ) {
        val bondParams = uiState.bondParams
        TickerField(
            value = bondParams.ticker,
            onValueChange = { onUIStateChange(uiState.bondParams(bondParams.copy(ticker = it))) },
            modifier = rowModifier,
        )
        Row(modifier = rowModifier) {
            Column(Modifier.weight(1f)) {
                NumericField(stringResource(R.string.commission), bondParams.commission) {
                    onUIStateChange(uiState.bondParams(bondParams.copy(commission = it)))
                }
            }
            Spacer(modifier = Modifier.width(padding))
            Column(Modifier.weight(1f)) {
                NumericField(stringResource(R.string.tax), bondParams.tax) {
                    onUIStateChange(uiState.bondParams(bondParams.copy(tax = it)))
                }
            }
        }
        Row(modifier = rowModifier) {
            Column(Modifier.weight(1f)) {
                MoneyField(stringResource(R.string.par_value), bondParams.parValue) {
                    onUIStateChange(uiState.bondParams(bondParams.copy(parValue = it)))
                }
            }
            Spacer(modifier = Modifier.width(padding))
            Column(Modifier.weight(1f)) {
                NumericField(stringResource(R.string.coupon), bondParams.coupon) {
                    onUIStateChange(uiState.bondParams(bondParams.copy(coupon = it)))
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
                    onUIStateChange(uiState.bondParams(bondParams.copy(buyDate = it)))
                }
            }
            Spacer(modifier = Modifier.width(padding))
            Column(Modifier.weight(1f)) {
                NumericField(stringResource(R.string.price), bondParams.buyPrice) {
                    onUIStateChange(uiState.bondParams(bondParams.copy(buyPrice = it)))
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
                onUIStateChange(uiState.bondParams(bondParams.copy(tillMaturity = it)))
            })
        }
        Row(modifier = rowModifier) {
            Column(Modifier.weight(1f)) {
                DateField(stringResource(R.string.date), bondParams.sellDate) {
                    onUIStateChange(uiState.bondParams(bondParams.copy(sellDate = it)))
                }
            }
            Spacer(modifier = Modifier.width(padding))
            Column(Modifier.weight(1f)) {
                NumericField(stringResource(R.string.price), bondParams.sellPrice) {
                    onUIStateChange(uiState.bondParams(bondParams.copy(sellPrice = it)))
                }
            }
        }
        Card(
            modifier = rowModifier
                .padding(vertical = padding),

//                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary)
        ) {
            val calcResult = uiState.calcResult
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
        Text(text = uiState.bondInfo?.toString() ?: "null")
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
fun TickerField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        label = { Text(stringResource(R.string.ticker)) },
        singleLine = true,
    )
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
fun DateField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
//    onToday: () -> Unit,
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier.fillMaxWidth(),
        label = { Text(label) },
//        trailingIcon = {
//            Icon(
//                painterResource(R.drawable.ic_outline_today_24),
//                null,
//                modifier = Modifier.clickable(onClick = onToday)
//            )
//        },
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

@Preview(showBackground = true)
@Composable
fun CalculatorScreenPreview() {
    CalculatorScreen(
        uiState = CalculatorScreenUIState(calcResult = BondCalcUIResult("14.5₽", "9.5%")),
        onUIStateChange = {}
    )
}
