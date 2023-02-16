package com.example.bookstore.ui.screens.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.bookstore.ui.screens.detail.DetailScreen
import com.example.bookstore.ui.screens.detail.DetailUiState
import com.example.bookstore.ui.screens.detail.VolumeDetailViewModel
import com.example.bookstore.ui.screens.favorites.FavoritesScreen
import com.example.bookstore.ui.screens.favorites.FavoritesViewModel
import com.example.bookstore.ui.screens.home.HomeScreen
import com.example.bookstore.ui.screens.home.VolumesViewModel

@Composable
fun VolumesNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Home.route,
        modifier = modifier
    ) {
        composable(route = Home.route) {
            val vm: VolumesViewModel = viewModel()
            HomeScreen(
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
        ) { navBackStackEntry ->
            val volumeId = navBackStackEntry.arguments?.getString(Detail.volumeIdArg)!!
            val vm: VolumeDetailViewModel = viewModel()
            if(vm.uiState is DetailUiState.Loading)//TODO PRETTY SURE THIS IS THE WRONG WAY TO DO IT
                (vm::getVolume)(volumeId)
            val context = LocalContext.current

            DetailScreen(
                uiState = vm.uiState,
                context = context,
                favAction = vm::setFavorite,
                retryAction = { (vm::getVolume)(volumeId) }//TODO NOT SURE IF THIS WORKS,
                )
        }
    }
}

fun NavHostController.navigateSingleTopTo(route: String) =
    this.navigate(route) {
        // Pop up to the start destination of the graph to
        // avoid building up a large stack of destinations
        // on the back stack as users select items
        popUpTo(
            this@navigateSingleTopTo.graph.findStartDestination().id
        ) {
            saveState = true
        }
        // Avoid multiple copies of the same destination when
        // reselecting the same item
        launchSingleTop = true
        // Restore state when reselecting a previously selected item
        restoreState = true
    }

private fun NavHostController.navigateToDetail(volumeId: String) {
    this.navigate("${Detail.route}/$volumeId")
}