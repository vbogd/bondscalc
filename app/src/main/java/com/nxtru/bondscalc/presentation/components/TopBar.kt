package com.nxtru.bondscalc.presentation.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.nxtru.bondscalc.R

@Composable
fun TopAppBar(
    navController: NavHostController,
    searchTickerField: @Composable () -> Unit,
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val searchSelected = currentDestination?.hierarchy?.any { it.route == Screen.Search.route } == true
    if (searchSelected) {
        SearchScreenTopBar(
            searchTickerField = searchTickerField
        )
    } else {
        CalculatorScreenTopBar(
            onSearch = {
                navController.navigate(Screen.Search.route) {
                    popUpTo(Screen.Search.route) {
                        inclusive = true
                    }
                }
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CalculatorScreenTopBar(
    onSearch: () -> Unit,
) {
    TopAppBar(
        modifier = Modifier.fillMaxWidth(),
        title = { Text(stringResource(R.string.calculator_screen_title)) },
        actions = {
            ClickableIcon(
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
) {
    TopAppBar(
        modifier = Modifier.fillMaxWidth(),
        title = searchTickerField,
        navigationIcon = {
            ClickableIcon(
                imageVector = Icons.Filled.ArrowBack,
                onClick = {}
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
//    CalculatorScreenTopBar()
    SearchScreenTopBar(
        searchTickerField = {
            OutlinedTextField(value = "", onValueChange = {})
        }
    )
}
