package com.nxtru.bondscalc.presentation.components

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.nxtru.bondscalc.R

sealed class Screen(val route: String, @StringRes val resourceId: Int) {
    object Calculator : Screen("calculator", R.string.calculator_screen)
    object Search : Screen("search", R.string.search_screen)
}

val screens = listOf(
    Screen.Calculator,
    Screen.Search,
)

@Composable
fun BottomBar(
    navController: NavHostController
) {
    // https://developer.android.com/jetpack/compose/navigation#bottom-nav
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    NavigationBar(
        modifier = Modifier.fillMaxWidth()
    ) {
        screens.forEachIndexed { index, screen ->
            val selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true
            val contentDescription = stringResource(screen.resourceId)
            NavigationBarItem(
                icon = {
                    when (index) {
                        0 -> Icon(
                            painterResource(
                                if (selected) R.drawable.ic_baseline_calculate_24 else R.drawable.ic_outline_calculate_24
                        ),
                            contentDescription
                        )
                        else -> Icon(Icons.Filled.Search, contentDescription)
                    }
                },
                label = { Text(stringResource(screen.resourceId)) },
                selected = selected,
                onClick = {
                    // https://developer.android.com/jetpack/compose/navigation#bottom-nav
                    navController.navigate(screen.route) {
                        // Pop up to the start destination of the graph to
                        // avoid building up a large stack of destinations
                        // on the back stack as users select items
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        // Avoid multiple copies of the same destination when
                        // reselecting the same item
                        launchSingleTop = true
                        // Restore state when reselecting a previously selected item
                        restoreState = true
                    }
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun PreviewBottomBar() {
    val navController = rememberNavController()
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            BottomBar(
                navController
            )
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Calculator.route,
            Modifier.padding(innerPadding)
        ) {
            composable(Screen.Calculator.route) { Text("calculator") }
            composable(Screen.Search.route) { Text("search") }
        }
    }

}
