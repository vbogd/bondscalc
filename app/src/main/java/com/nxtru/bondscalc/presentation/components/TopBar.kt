package com.nxtru.bondscalc.presentation.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val searchSelected = currentDestination?.hierarchy?.any { it.route == Screen.Search.route } == true
    if (searchSelected) {
        SearchScreenTopBar()
    } else {
        CalculatorScreenTopBar()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CalculatorScreenTopBar() {
    TopAppBar(
        modifier = Modifier.fillMaxWidth(),
        title = { Text(stringResource(R.string.calculator_screen_title)) },
        actions = {
            ClickableIcon(
                imageVector = Icons.Filled.Search,
                onClick = {}
            )
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SearchScreenTopBar() {
    TopAppBar(
        modifier = Modifier.fillMaxWidth(),
        title = {
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth().padding(start = 16.dp),
                placeholder = { Text(stringResource(R.string.search_field_placeholder)) },
                singleLine = true,
                value = "",
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent,
                    errorBorderColor = Color.Transparent
                ),
                onValueChange = {},
            )
        },
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

@Preview
@Composable
fun PreviewTopBar() {
//    CalculatorScreenTopBar()
    SearchScreenTopBar()
}
