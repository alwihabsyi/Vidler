package com.vidler.vidler.presentation.navgraph

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.vidler.vidler.presentation.appnavigator.AppNavigatorScreen
import com.vidler.vidler.presentation.home.HomeScreen

@Composable
fun NavGraph() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = Route.AppNavigator.route
    ) {
        navigation(
            route = Route.AppNavigator.route,
            startDestination = Route.AppNavigatorScreen.route
        ) {
            composable(
                route = Route.AppNavigatorScreen.route
            ) {
                AppNavigatorScreen()
            }
        }
    }
}