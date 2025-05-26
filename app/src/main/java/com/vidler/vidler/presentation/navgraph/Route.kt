package com.vidler.vidler.presentation.navgraph

sealed class Route(
    val route: String
) {
    data object AppNavigator: Route(route = "appNavigator")
    data object AppNavigatorScreen: Route(route = "appNavigatorScreen")
    data object HomeScreen: Route("homeScreen")
    data object CollectionScreen: Route("collectionScreen")
    data object ScheduleScreen: Route("scheduleScreen")
    data object CreateScheduleScreen: Route("createScheduleScreen")
}