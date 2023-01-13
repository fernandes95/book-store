package com.example.bookstore.ui.screens.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavType
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink


interface VolumesDestination {
    val icon: ImageVector?
    val route: String
}

/**
 * Rally app navigation destinations
 */
object Home : VolumesDestination {
    override val icon = Icons.Filled.Home
    override val route = "home"
}

object Favorites : VolumesDestination {
    override val icon = Icons.Filled.Favorite
    override val route = "favorites"
}

object Detail : VolumesDestination {
    // Added for simplicity, this icon will not in fact be used, as SingleAccount isn't
    // part of the RallyTabRow selection
    override val icon = null
    override val route = "detail"
    const val volumeIdArg = "volume_id"
    val routeWithArgs = "$route/{$volumeIdArg}"
    val arguments = listOf(
        navArgument(volumeIdArg) { type = NavType.StringType }
    )
    val deepLinks = listOf(
        navDeepLink { uriPattern = "rally://$route/{$volumeIdArg}" }
    )
}

// Screens to be displayed in the top RallyTabRow
val volumesTabRowScreens = listOf(Home, Favorites)