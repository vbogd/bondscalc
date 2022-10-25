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
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.nxtru.bondscalc.domain.models.BondParams
import com.nxtru.bondscalc.presentation.ui.theme.MainTheme

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
        onBondParamsChange = { viewModel.onBondParamsChange(it) },
    )
}

@Composable
fun MainContent(bondParams: BondParams, onBondParamsChange: (BondParams) -> Unit) {
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
            Column(
                modifier = Modifier.padding(all = padding) //.verticalScroll(rememberScrollState())
            ) {
                TextField(label = "тикер", value = bondParams.ticker) {
                    onBondParamsChange(bondParams.copy(ticker = it))
                }
                Row(modifier = Modifier.fillMaxWidth()) {
                    Column(Modifier.weight(1f)) {
                        NumericField("комиссия", bondParams.commission) {
                            onBondParamsChange(bondParams.copy(commission = it))
                        }
                    }
                    Spacer(modifier = Modifier.width(padding))
                    Column(Modifier.weight(1f)) {
                        NumericField("налог", bondParams.tax) {
                            onBondParamsChange(bondParams.copy(tax = it))
                        }
                    }
                }
                Row(modifier = Modifier.fillMaxWidth()) {
                    Column(Modifier.weight(1f)) {
                        MoneyField("номинал", bondParams.parValue) {
                            onBondParamsChange(bondParams.copy(parValue = it))
                        }
                    }
                    Spacer(modifier = Modifier.width(padding))
                    Column(Modifier.weight(1f)) {
                        NumericField("купон", bondParams.coupon) {
                            onBondParamsChange(bondParams.copy(coupon = it))
                        }
                    }
                }
                Header("покупка")
                Row(modifier = Modifier.fillMaxWidth()) {
                    Column(Modifier.weight(1f)) {
                        DateField("дата", bondParams.buyDate) {
                            onBondParamsChange(bondParams.copy(buyDate = it))
                        }
                    }
                    Spacer(modifier = Modifier.width(padding))
                    Column(Modifier.weight(1f)) {
                        NumericField("цена", bondParams.buyPrice) {
                            onBondParamsChange(bondParams.copy(buyPrice = it))
                        }
                    }
                }
                Header("продажа")
                Switch(checked = bondParams.tillMaturity, onCheckedChange = {
                    onBondParamsChange(bondParams.copy(tillMaturity = it))
                })
                Row(modifier = Modifier.fillMaxWidth()) {
                    Column(Modifier.weight(1f)) {
                        DateField("дата", bondParams.sellDate) {
                            onBondParamsChange(bondParams.copy(sellDate = it))
                        }
                    }
                    Spacer(modifier = Modifier.width(padding))
                    Column(Modifier.weight(1f)) {
                        NumericField("цена", bondParams.sellPrice) {
                            onBondParamsChange(bondParams.copy(sellPrice = it))
                        }
                    }
                }
                Header("результаты")
                Text(text = bondParams.toString())
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TextField(label: String, value: String, onValueChange: (String) -> Unit) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier.fillMaxWidth(),
        label = { Text(label) },
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
        trailingIcon = { Text("P") },
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
    MainContent(BondParams.EMPTY) {}
}
