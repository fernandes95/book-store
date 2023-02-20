package com.example.bookstore.ui.screens.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Search
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavType
import androidx.navigation.navArgument

interface VolumesDestination {
    val selectedIcon: ImageVector?
    val unselectedIcon: ImageVector?
    val route: String
}

object Landing : VolumesDestination {
    override val selectedIcon = null
    override val unselectedIcon = null
    override val route = "landing"
}

object Home : VolumesDestination {
    override val selectedIcon = Icons.Filled.Home
    override val unselectedIcon = Icons.Outlined.Home
    override val route = "home"
}

object Search : VolumesDestination {
    override val selectedIcon = Icons.Filled.Search
    override val unselectedIcon = Icons.Outlined.Search
    override val route = "search"
}

object Favorites : VolumesDestination {
    override val selectedIcon = Icons.Filled.Favorite
    override val unselectedIcon = Icons.Outlined.FavoriteBorder
    override val route = "favorites"
}

object Detail : VolumesDestination{
    override val selectedIcon = null
    override val unselectedIcon = null
    override val route = "detail"
    const val volumeIdArg = "volume_id"
    val routeWithArgs = "$route/{$volumeIdArg}"
    val arguments = listOf(navArgument(volumeIdArg) { type = NavType.StringType })
}

// Screens to be displayed in the bottom
val volumesTabRowScreens = listOf(Home, Search, Favorites)
val bottomNavRoutes = listOf(Home.route, Search.route, Favorites.route)