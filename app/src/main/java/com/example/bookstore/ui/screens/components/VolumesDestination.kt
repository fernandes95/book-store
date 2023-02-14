package com.example.bookstore.ui.screens.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavType
import androidx.navigation.navArgument

interface VolumesDestination {
    val icon: ImageVector?
    val route: String
}

object Home : VolumesDestination {
    override val icon = Icons.Filled.Home
    override val route = "home"
}

object Favorites : VolumesDestination {
    override val icon = Icons.Filled.Favorite
    override val route = "favorites"
}

object Detail : VolumesDestination{
    override val icon = null
    override val route = "detail"
    const val volumeIdArg = "volume_id"
    val routeWithArgs = "$route/{$volumeIdArg}"
    val arguments = listOf(navArgument(volumeIdArg) { type = NavType.StringType })
}

// Screens to be displayed in the bottom
val volumesTabRowScreens = listOf(Home, Favorites)