package com.nxtru.bondscalc.presentation.widgets

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import com.nxtru.bondscalc.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TickerField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    tickers: List<String>?,
    onSearchTicker: (String) -> Unit,
    onSelectionDone: (String) -> Unit,
    onSelectionCancel: () -> Unit = {},
) {
    val loadingStr = stringResource(R.string.loading)
    Box(
        modifier = modifier
    ) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            label = { Text(stringResource(R.string.ticker)) },
            singleLine = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(
                onSearch = { onSearchTicker(value) },
            ),
            modifier = Modifier.fillMaxWidth() //.onKeyEvent {
//                if (it.nativeKeyEvent.keyCode == KeyEvent.KEYCODE_ENTER) {
//                    onSearchTicker(value)
//                    showMenu = true
//                    true
//                } else
//                    false
//            }
        )
        DropdownMenu(
            modifier = Modifier.fillMaxWidth(),
            expanded = tickers != null,
            onDismissRequest = { onSelectionCancel() },
        ) {
            tickers?.forEach { label ->
                DropdownMenuItem(
                    text = { Text(text = label) },
                    enabled = label != loadingStr,
                    onClick = { onSelectionDone(label) }
                )
            }
        }
    }
}

@Preview
@Composable
fun PreviewTickerField() {
    var ticker by remember { mutableStateOf("ОФЗ") }
    var tickers by remember { mutableStateOf<List<String>?>(null) }
    val loadingStr = stringResource(R.string.loading)
    val coroutineScope = rememberCoroutineScope()
    TickerField(
        value = ticker,
        onValueChange = { ticker = it },
        modifier = Modifier.fillMaxWidth(),
        tickers = tickers,
        onSearchTicker = {
            println("Searching '$it'...")
            tickers = listOf(loadingStr)
            coroutineScope.launch {
                tickers = loadEmul(it)
            }
        },
        onSelectionDone = {
            ticker = it
            tickers = null
            println("Ticker selected: '$it'")
        }
    )
}

suspend fun loadEmul(query: String): List<String> {
    delay(1000L)
    return listOf("$query-1", "$query-2", "$query-3")
}
