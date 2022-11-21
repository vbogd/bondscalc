package com.nxtru.bondscalc.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.nxtru.bondscalc.R

@Composable
fun TopAppBar(
    navController: NavHostController,
    searchTickerField: @Composable () -> Unit,
    calculatorScreenRefreshAvailable: Boolean,
    onCalculatorScreenRefresh: () -> Unit,
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val searchSelected = currentDestination?.hierarchy?.any { it.route == Screen.Search.route } == true
    if (searchSelected) {
        SearchScreenTopBar(
            searchTickerField = searchTickerField,
            onBack = {
                navController.popBackStack()
            }
        )
    } else {
        CalculatorScreenTopBar(
            refreshAvailable = calculatorScreenRefreshAvailable,
            onSearch = {
                navController.navigate(Screen.Search.route) {
                    popUpTo(Screen.Search.route) {
                        inclusive = true
                    }
                }
            },
            onRefresh = onCalculatorScreenRefresh
        )
    }
}

private fun Modifier.iconPadding() = this.then(Modifier.padding(horizontal = 12.dp))

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CalculatorScreenTopBar(
    refreshAvailable: Boolean,
    onSearch: () -> Unit,
    onRefresh: () -> Unit,
) {
    TopAppBar(
        title = { Text(stringResource(R.string.calculator_screen_title)) },
        actions = {
            if (refreshAvailable) {
                ClickableIcon(
                    modifier = Modifier.iconPadding(),
                    imageVector = Icons.Filled.Refresh,
                    onClick = onRefresh
                )
            }
            ClickableIcon(
                modifier = Modifier.iconPadding(),
                imageVector = Icons.Filled.Search,
                onClick = onSearch
            )
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SearchScreenTopBar(
    searchTickerField: @Composable () -> Unit,
    onBack: () -> Unit,
) {
    TopAppBar(
        modifier = Modifier.fillMaxWidth(),
        title = searchTickerField,
        navigationIcon = {
            ClickableIcon(
                modifier = Modifier.iconPadding(),
                imageVector = Icons.Filled.ArrowBack,
                onClick = onBack
            )
        },
//        actions = {
//            ClickableIcon(
//                imageVector = Icons.Filled.Search,
//                onClick = {}
//            )
//        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun PreviewTopBar() {
    Column {
        CalculatorScreenTopBar(
            refreshAvailable = true,
            onSearch = {},
            onRefresh = {},
        )
        SearchScreenTopBar(
            searchTickerField = {
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = "123",
                    onValueChange = {})
            },
            onBack = {}
        )
    }
}
