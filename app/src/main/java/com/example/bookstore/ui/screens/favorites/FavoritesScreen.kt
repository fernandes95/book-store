package com.example.bookstore.ui.screens.favorites

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.bookstore.R
import com.example.bookstore.ui.screens.components.LoadingScreen
import com.example.bookstore.ui.screens.components.RetryScreen
import com.example.bookstore.ui.screens.home.VolumesListScreen
import kotlinx.coroutines.flow.StateFlow

@Composable
fun FavoritesScreen(
    uiState: StateFlow<FavUiState>,
    volumeSelected: (String) -> Unit,
    retryAction: () -> Unit,
    modifier: Modifier = Modifier
) {
    val uiStateAsState by uiState.collectAsState()

    when (uiStateAsState) {
        is FavUiState.Loading -> LoadingScreen(modifier)
        is FavUiState.Success -> VolumesListScreen((uiStateAsState as FavUiState.Success).favorites, false, volumeSelected, {}, false, modifier)
        else -> RetryScreen(stringResource(R.string.empty), retryAction, modifier)
    }
}