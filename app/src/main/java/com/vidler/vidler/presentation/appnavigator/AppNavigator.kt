package com.vidler.vidler.presentation.appnavigator

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.vidler.vidler.presentation.appnavigator.components.AppBottomNavigation
import com.vidler.vidler.presentation.appnavigator.components.BottomNavigationItem
import com.vidler.vidler.presentation.collection.CollectionScreen
import com.vidler.vidler.presentation.collection.CollectionViewModel
import com.vidler.vidler.presentation.common.GlobalSnackbarHost
import com.vidler.vidler.presentation.home.HomeScreen
import com.vidler.vidler.presentation.home.HomeViewModel
import com.vidler.vidler.presentation.navgraph.Route
import com.vidler.vidler.presentation.schedule.ScheduleScreen
import com.vidler.vidler.presentation.schedule.ScheduleViewModel
import com.vidler.vidler.presentation.schedule.CreateScheduleScreen
import org.koin.androidx.compose.koinViewModel

@Composable
fun AppNavigatorScreen() {
    val bottomNavigationItems = remember {
        listOf(
            BottomNavigationItem(icon = Icons.Default.Home, text = "Home"),
            BottomNavigationItem(icon = Icons.AutoMirrored.Default.List, text = "Collection"),
            BottomNavigationItem(icon = Icons.Default.DateRange, text = "Schedule")
        )
    }
    val navController = rememberNavController()
    val backStackState = navController.currentBackStackEntryAsState().value
    var selectedItem by rememberSaveable { mutableIntStateOf(0) }
    val currentRoute = backStackState?.destination?.route
    selectedItem = remember(key1 = backStackState) {
        when (backStackState?.destination?.route) {
            Route.HomeScreen.route -> 0
            Route.CollectionScreen.route -> 1
            Route.ScheduleScreen.route -> 2
            else -> 0
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(), bottomBar = {
            if (currentRoute != Route.CreateScheduleScreen.route)
                AppBottomNavigation(
                    items = bottomNavigationItems,
                    selected = selectedItem,
                    onItemClicked = { index ->
                        when (index) {
                            0 -> navigateToTab(
                                navController = navController, route = Route.HomeScreen.route
                            )

                            1 -> navigateToTab(
                                navController = navController, route = Route.CollectionScreen.route
                            )

                            2 -> navigateToTab(
                                navController = navController, route = Route.ScheduleScreen.route
                            )
                        }
                    })
        }) {
        val bottomPadding = it.calculateBottomPadding()
        NavHost(
            navController = navController,
            startDestination = Route.HomeScreen.route,
            modifier = Modifier.padding(bottom = bottomPadding, top = 40.dp)
        ) {
            composable(Route.HomeScreen.route) {
                val viewModel: HomeViewModel = koinViewModel()
                HomeScreen(state = viewModel.state.value)
            }
            composable(Route.CollectionScreen.route) {
                val viewModel: CollectionViewModel = koinViewModel()
                CollectionScreen(state = viewModel.state.value, onEvent = viewModel::onEvent)
            }
            composable(Route.ScheduleScreen.route) {
                val viewModel: ScheduleViewModel = koinViewModel()
                ScheduleScreen(
                    state = viewModel.state.value,
                    onEvent = viewModel::onEvent,
                    navigateToCreateSchedule = {
                        navController.navigate(Route.CreateScheduleScreen.route)
                    }
                )
            }
            composable(Route.CreateScheduleScreen.route) {
                val viewModel: ScheduleViewModel = koinViewModel()
                val videos = koinViewModel<CollectionViewModel>().state.value.videos
                CreateScheduleScreen(
                    videos = videos,
                    onEvent = viewModel::onEvent,
                    onBack = { navController.popBackStack() }
                )
            }
        }

        GlobalSnackbarHost(Modifier.padding(bottom = bottomPadding + 10.dp))
    }
}

private fun navigateToTab(navController: NavController, route: String) {
    navController.navigate(route) {
        navController.graph.startDestinationRoute?.let { homeScreen ->
            popUpTo(homeScreen) {
                saveState = true
            }
            restoreState = true
            launchSingleTop = true
        }
    }
}