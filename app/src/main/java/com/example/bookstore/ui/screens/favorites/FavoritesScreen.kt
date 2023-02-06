package com.example.bookstore.ui.screens.favorites

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.bookstore.ui.screens.components.ErrorScreen
import com.example.bookstore.ui.screens.components.LoadingScreen
import com.example.bookstore.ui.screens.home.VolumesListScreen

@Composable
fun FavoritesScreen(
    uiState: FavUiState,
    volumeSelected: (String) -> Unit,
    retryAction: () -> Unit,
    modifier: Modifier = Modifier
) {
    when (uiState) {
        is FavUiState.Loading -> LoadingScreen(modifier)
        is FavUiState.Success -> VolumesListScreen(uiState.favorites, { volumeSelected }, {}, modifier)
        else -> ErrorScreen(retryAction, modifier)
    }
}