package com.example.bookstore.ui.screens.favorites

import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.bookstore.R
import com.example.bookstore.ui.screens.components.LoadingScreen
import com.example.bookstore.ui.screens.components.RetryScreen
import com.example.bookstore.ui.screens.components.VolumesList
import com.example.bookstore.ui.screens.home.ListUiState
import kotlinx.coroutines.flow.StateFlow

@Composable
fun FavoritesScreen(
    uiState: StateFlow<ListUiState>,
    volumeSelected: (String) -> Unit,
    retryAction: () -> Unit,
    modifier: Modifier = Modifier
) {
    val uiStateAsState by uiState.collectAsState()

    when (uiStateAsState) {
        is ListUiState.Loading -> LoadingScreen(modifier)
        is ListUiState.Success -> FavoritesListScreen((uiStateAsState as ListUiState.Success), volumeSelected, modifier)
        else -> RetryScreen(stringResource(R.string.empty), retryAction, modifier)
    }
}

@Composable
fun FavoritesListScreen(
    uiState: ListUiState.Success,
    volumeSelected: (String) -> Unit,
    modifier: Modifier = Modifier)
{
    val listState = rememberLazyListState()
    VolumesList(uiState, volumeSelected, listState, modifier)
}