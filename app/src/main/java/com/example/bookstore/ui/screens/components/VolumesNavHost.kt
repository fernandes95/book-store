package com.example.bookstore.ui.screens.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.bookstore.ui.screens.detail.DetailScreen
import com.example.bookstore.ui.screens.detail.VolumeDetailViewModel
import com.example.bookstore.ui.screens.favorites.FavoritesScreen
import com.example.bookstore.ui.screens.favorites.FavoritesViewModel
import com.example.bookstore.ui.screens.home.HomeScreen
import com.example.bookstore.ui.screens.home.HomeViewModel
import com.example.bookstore.ui.screens.landing.LandingScreen
import com.example.bookstore.ui.screens.landing.LandingViewModel
import com.example.bookstore.ui.screens.search.SearchScreen
import com.example.bookstore.ui.screens.search.SearchViewModel

@Composable
fun VolumesNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Landing.route,
        modifier = modifier
    ) {
        composable(route = Landing.route) {
            val vm: LandingViewModel = viewModel()
            LandingScreen(
                uiState = vm.uiState,
                loginAction = { navController.navigateSingleTopInclusiveTo(Home.route) },
                offlineAction = { navController.navigateSingleTopTo(Home.route) },
            )
        }
        composable(route = Home.route) {
            val vm: HomeViewModel = viewModel()
            HomeScreen(
              uiState = vm.uiState,
              logoutAction = vm::logout,
              loggedOutAction = { navController.navigateSingleTopInclusiveTo(Landing.route) }
            )
        }
        composable(route = Search.route) {
            val vm: SearchViewModel = viewModel()
            SearchScreen(
                uiState = vm.uiState,
                volumeSelected = { volumeId -> navController.navigateToDetail(volumeId) },
                onSearchAction = vm::searchVolumes,
                retryAction = vm::getVolumes,
                onLoadMore = vm::getMoreVolumes
            )
        }
        composable(route = Favorites.route) {
            val vm: FavoritesViewModel = viewModel()
            FavoritesScreen(
                uiState = vm.uiState,
                volumeSelected = { volumeId -> navController.navigateToDetail(volumeId) },
                retryAction = vm::getFavorites
            )
        }
        composable(
            route = Detail.routeWithArgs,
            arguments = Detail.arguments,
        ) {
            val vm: VolumeDetailViewModel = viewModel()
            DetailScreen(
                uiState = vm.uiState,
                favAction = vm::setFavorite,
                retryAction = vm::getVolume
                )
        }
    }
}

fun NavHostController.navigateSingleTopInclusiveTo(route: String) =
    this.navigate(route) {
        // Pop up to the start destination of the graph to
        // avoid building up a large stack of destinations
        // on the back stack as users select items
        popUpTo(
            this@navigateSingleTopInclusiveTo.graph.findStartDestination().id
        ) {
//            saveState = true
            inclusive = true
        }
        // Avoid multiple copies of the same destination when
        // reselecting the same item
        launchSingleTop = true
        // Restore state when reselecting a previously selected item
        restoreState = true
    }

fun NavHostController.navigateSingleTopTo(route: String) =
    this.navigate(route) {
        // Pop up to the start destination of the graph to
        // avoid building up a large stack of destinations
        // on the back stack as users select items
        popUpTo(
            this@navigateSingleTopTo.graph.findStartDestination().id
        )
        // Avoid multiple copies of the same destination when
        // reselecting the same item
        launchSingleTop = true
        // Restore state when reselecting a previously selected item
        restoreState = true
    }

private fun NavHostController.navigateToDetail(volumeId: String) {
    this.navigate("${Detail.route}/$volumeId")
}