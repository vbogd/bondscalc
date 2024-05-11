package com.nxtru.bondscalc.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nxtru.bondscalc.R
import com.nxtru.bondscalc.domain.models.BondParams
import com.nxtru.bondscalc.presentation.*
import com.nxtru.bondscalc.presentation.models.CalculatorScreenUIState

private val padding = 8.dp

@Composable
fun CalculatorScreen(
    uiState: CalculatorScreenUIState,
    onUIStateChange: (CalculatorScreenUIState) -> Unit,
    modifier: Modifier = Modifier,
) {
    val onBondParamsChange = { it: BondParams ->
        onUIStateChange(uiState.copy(bondParams = it))
    }
    val rowModifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = padding)
    val focusManager = LocalFocusManager.current
    Column(
        modifier = modifier
            .clickable(
                // disable ripple effect
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) {
                focusManager.clearFocus()
            }
            .verticalScroll(rememberScrollState())
    ) {
        val bondParams = uiState.bondParams
        TickerField(
            value = bondParams.ticker,
            onValueChange = { onBondParamsChange(bondParams.copy(ticker = it)) },
            modifier = rowModifier,
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
                DateField(
                    label = stringResource(R.string.date),
                    value = bondParams.buyDate,
                    onValueChange = {
                        onBondParamsChange(bondParams.copy(buyDate = it))
                    },
                    onToday = {
                        onBondParamsChange(bondParams.copy(buyDate = getTodayDate()))
                    }
                )
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
            if (uiState.hasOfferDate) {
                Column(Modifier.weight(1f)) {
                    LabeledCheckbox(
                        label = stringResource(R.string.offer),
                        checked = uiState.tillOffer
                    ) {
                        onUIStateChange(uiState.setTillOffer())
                    }
                }
            }
            Column(Modifier.weight(1f)) {
                LabeledCheckbox(
                    label = stringResource(R.string.maturity),
                    checked = uiState.tillMaturity
                ) {
                    onUIStateChange(uiState.setTillMaturity(!uiState.tillMaturity))
                }
            }
        }

        Row(modifier = rowModifier) {
            Column(Modifier.weight(1f)) {
                DateField(
                    label = stringResource(R.string.date),
                    value = bondParams.sellDate,
                    onValueChange = {
                        onUIStateChange(uiState.setSellDate(it))
                    },
                    onToday = {
                        onUIStateChange(uiState.setSellDate(getTomorrowDate()))
                    }
                )
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
            val calcResult = uiState.calcResult
            val paddingModifier = Modifier.padding(padding)
            Column(
                modifier = paddingModifier,
                verticalArrangement = Arrangement.spacedBy(padding),
            ) {
                Header(stringResource(R.string.result))
                ResultRow(stringResource(R.string.result_percent), calcResult.ytm)
                ResultRow(
                    label = stringResource(R.string.result_current_yield),
                    value = calcResult.currentYield,
                    helpTitle = stringResource(R.string.help_dialog_current_yield_title),
                    helpText = stringArrayResource(R.array.help_dialog_current_yield_body),
                )
                ResultRow(stringResource(R.string.result_rub), calcResult.income)
                ResultRow(stringResource(R.string.time_interval), calcResult.timeInterval)
            }
        }
//        Text(text = uiState.bondInfo?.toString() ?: "null")
    }
}

@Composable
fun ResultRow(
    label: String,
    value: String,
    helpTitle: String? = null,
    helpText: Array<String> = emptyArray()
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row {
            Text(
                text = label,
                color = MaterialTheme.colorScheme.secondary,
            )
            if (helpTitle != null) {
                val openDialog = remember { mutableStateOf(false) }
                ClickableIcon(
                    modifier = Modifier
                        .padding(start = padding / 2)
                        .requiredSize(16.dp),
                    painter = painterResource(R.drawable.ic_baseline_info_24),
                    onClick = { openDialog.value = true }
                )
                if (openDialog.value) {
                    HelpDialog(
                        title = helpTitle,
                        text = helpText,
                        onDismissRequest = { openDialog.value = false }
                    )
                }
            }
        }
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
    onToday: () -> Unit,
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier.fillMaxWidth(),
        label = { Text(label) },
        trailingIcon = {
            ClickableIcon(
                painter = painterResource(R.drawable.ic_outline_today_24),
                onClick = onToday
            )
        },
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
        uiState = CalculatorScreenUIState(
            calcResult = BondCalcUIResult("14.5 ₽", "9.5 %", "8 %", "11")
        ),
        onUIStateChange = {}
    )
}

@Composable
private fun LabeledCheckbox(
    label: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                // This is for removing ripple when Row is clicked
                indication = null,
                role = Role.Switch,
                onClick = {
                    onCheckedChange(!checked)
                }
            )
    ) {
        Checkbox(checked = checked, onCheckedChange = onCheckedChange)
        Text(
            color = MaterialTheme.colorScheme.secondary,
            text = label
        )
    }
}
