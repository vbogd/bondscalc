package com.nxtru.bondscalc.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nxtru.bondscalc.R
import com.nxtru.bondscalc.domain.models.BriefBondInfo
import com.nxtru.bondscalc.presentation.models.SearchScreenUIState

private val padding = 8.dp

@Composable
fun SearchScreen(
    uiState: SearchScreenUIState,
    modifier: Modifier = Modifier,
    onSelected: (BriefBondInfo) -> Unit,
) {
    Column(
        modifier = modifier
    ) {
        if (uiState.isSearching) {
            AlignCenterBox {
                CircularProgressIndicator()
            }
        } else if (uiState.tickers.isNotEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
            ) {
                uiState.tickers.map {
                    BriefBondInfoCard(info = it, onClick = { onSelected(it) })
                }
            }
        } else {
            AlignCenterBox {
                Text(
                    text = stringResource(uiState.messageId),
                    color = LocalContentColor.current.copy(alpha = 0.5f)
                )
            }
        }
    }
}

@Composable
private fun AlignCenterBox(
    content: @Composable BoxScope.() -> Unit
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize(),
        content = content,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchTickerField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    shouldFocus: Boolean,
    onSearch: (String) -> Unit,
    onClear: () -> Unit,
) {
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current
    LaunchedEffect(shouldFocus) {
        if (shouldFocus) {
            focusRequester.requestFocus()
        } else {
            focusManager.clearFocus()
        }
    }

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = { Text(stringResource(R.string.search_field_placeholder)) },
        singleLine = true,
        trailingIcon = {
            if (value.isNotEmpty()) {
                ClickableIcon(
                    imageVector = Icons.Filled.Close,
                    onClick = onClear,
                )
            }
        },
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = Color.Transparent,
            unfocusedBorderColor = Color.Transparent,
            errorBorderColor = Color.Transparent
        ),
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        keyboardActions = KeyboardActions(
            onSearch = { onSearch(value) },
        ),
        modifier = modifier
            .fillMaxWidth()
            .focusRequester(focusRequester)
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
            // to remove ripple effect: https://stackoverflow.com/a/66703449
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

private val initial = SearchScreenUIState(
    pattern = "ОФЗ",
    isSearching = false,
//    messageId = R.string.search_field_too_short,
//    tickers = emptyList(),
    tickers = listOf(
        BriefBondInfo("SU26229RMFS3", "ОФЗ 26229", "RU000A100EG3"),
        BriefBondInfo("SU29006RMFS2", "ОФЗ 29006", "RU000A0JV4L2"),
        BriefBondInfo("SU29014RMFS6", "ОФЗ 29014", "RU000A101N52"),
    )
)

@Preview(showBackground = true)
@Composable
fun PreviewSearchScreen() {
    var uiState by remember { mutableStateOf(initial) }
    SearchScreen(
        uiState = uiState,
        modifier = Modifier.fillMaxSize(),
        onSelected = { uiState = uiState.copy(pattern = it.secId, tickers = emptyList()) },
    )
}
