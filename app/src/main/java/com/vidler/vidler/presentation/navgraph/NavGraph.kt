package com.vidler.vidler.presentation.navgraph

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.vidler.vidler.presentation.appnavigator.AppNavigatorScreen

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