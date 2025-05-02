package com.vidler.vidler.presentation.navgraph

sealed class Route(
    val route: String
) {
    data object AppStartNavigation: Route(route = "appStartNavigation")
    data object HomeScreen: Route("homeScreen")
    data object CollectionScreen: Route("collectionScreen")
    data object ScheduleScreen: Route("scheduleScreen")
}