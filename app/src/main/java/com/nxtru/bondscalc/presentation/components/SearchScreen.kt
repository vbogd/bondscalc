package com.nxtru.bondscalc.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nxtru.bondscalc.R
import com.nxtru.bondscalc.domain.models.BriefBondInfo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

data class SearchScreenUIState(
    val pattern: String = "",
    val isSearching: Boolean = false,
    val tickers: List<BriefBondInfo> = emptyList(),
)

private val padding = 8.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    uiState: SearchScreenUIState,
    modifier: Modifier = Modifier,
    onUIStateChange: (SearchScreenUIState) -> Unit,
    onSearch: (String) -> Unit,
    onSelected: (BriefBondInfo) -> Unit,
) {
    Column(
        modifier = modifier
    ) {
        TickerField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = padding),
            value = uiState.pattern,
            onSearch = onSearch,
            onValueChange = { onUIStateChange(uiState.copy(pattern = it)) },
            onClear = {
                onUIStateChange(uiState.copy(pattern = "", tickers = emptyList()))
            }
        )
        if (uiState.isSearching) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                CircularProgressIndicator()
            }
        } else if (uiState.tickers.isNotEmpty()) {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                uiState.tickers.map {
                    BriefBondInfoCard(info = it, onClick = { onSelected(it) })
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TickerField(
    value: String,
    modifier: Modifier = Modifier,
    onValueChange: (String) -> Unit,
    onSearch: (String) -> Unit,
    onClear: () -> Unit,
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(stringResource(R.string.ticker)) },
        singleLine = true,
        trailingIcon = {
            Icon(
                imageVector = Icons.Filled.Close,
                contentDescription = null,
                modifier = Modifier.clickable(onClick = onClear)
            )
        },
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        keyboardActions = KeyboardActions(
            onSearch = { onSearch(value) },
        ),
        modifier = modifier
    )
}

@Composable
private fun BriefBondInfoCard(
    info: BriefBondInfo,
    onClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        val modifier = Modifier.padding(start = padding)
        Text(
            text = info.ticker,
            modifier = modifier.padding(top = padding / 2)
        )
        Text(
            text = info.isin,
            modifier = modifier.padding(bottom = padding / 2),
            style = MaterialTheme.typography.bodySmall,
            color = LocalContentColor.current.copy(alpha = 0.5f)
        )
        Divider(
            modifier = Modifier.padding(horizontal = padding)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewSearchScreen() {
    val initial = SearchScreenUIState(
        pattern = "26009",
        isSearching = false,
        tickers = listOf(
            BriefBondInfo("SU26229RMFS3", "ОФЗ 26229", "RU000A100EG3"),
            BriefBondInfo("SU29006RMFS2", "ОФЗ 29006", "RU000A0JV4L2"),
            BriefBondInfo("SU29014RMFS6", "ОФЗ 29014", "RU000A101N52"),
        )
    )
//    val uiStateFlow = MutableStateFlow(initial)
//    val uiState: SearchScreenUIState by uiStateFlow.collectAsState()
    var uiState by remember { mutableStateOf(initial) }
    val coroutineScope = rememberCoroutineScope()
    SearchScreen(
        uiState = uiState,
        modifier = Modifier.fillMaxSize(),
        onUIStateChange = {
//            coroutineScope.launch {
//                uiStateFlow.emit(it)
//            }
              uiState = it
        },
        onSearch = { uiState = uiState.copy(tickers = initial.tickers) },
        onSelected = { uiState = uiState.copy(pattern = it.secId, tickers = emptyList()) }
    )
}
