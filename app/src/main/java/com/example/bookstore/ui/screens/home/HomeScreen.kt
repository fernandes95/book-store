package com.example.bookstore.ui.screens.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.bookstore.R
import com.example.bookstore.data.models.dto.VolumeDto
import com.example.bookstore.data.models.toVolume
import com.example.bookstore.data.room.FavoriteEntity
import com.example.bookstore.ui.screens.components.LoadingOverlay
import com.example.bookstore.ui.screens.components.LoadingScreen
import com.example.bookstore.ui.screens.components.RetryScreen
import com.example.bookstore.ui.screens.components.VolumeCard
import com.example.bookstore.ui.screens.theme.VolumesTheme
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter

@Composable
fun HomeScreen(
    uiState: ListUiState,
    volumeSelected: (String) -> Unit,
    retryAction: () -> Unit,
    onLoadMore: () -> Unit,
    modifier: Modifier = Modifier
) {
    when (uiState) {
        is ListUiState.Loading -> LoadingScreen(modifier)
        is ListUiState.Success -> VolumesListScreen(uiState, volumeSelected, onLoadMore, modifier)
        else -> RetryScreen(stringResource(R.string.failed_loading),  retryAction, modifier)
    }
}

@Composable
fun InfiniteListHandler(
    listState: LazyListState,
    isOnLimit: Boolean,
    buffer: Int = 2,
    onLoadMore: () -> Unit
) {
    if(isOnLimit) return

    val loadMore = remember {
        derivedStateOf {
            val layoutInfo = listState.layoutInfo
            val totalItemsNumber = layoutInfo.totalItemsCount
            val lastVisibleItemIndex = (layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0) + 1

            lastVisibleItemIndex > (totalItemsNumber - buffer)
        }
    }

    LaunchedEffect(loadMore) {
        snapshotFlow { loadMore.value }
            .distinctUntilChanged()
            .filter{ it }
            .collect {
                onLoadMore()
            }
    }
}

@Composable
fun VolumesListScreen(
    uiState: ListUiState.Success,
    volumeSelected: (String) -> Unit,
    onLoadMore: () -> Unit,
    modifier: Modifier = Modifier)
{
    Box(Modifier.fillMaxSize()) {
        val listState = rememberLazyListState()

        LazyColumn(
            state = listState,
            modifier = modifier
                .fillMaxWidth()
                .scrollable(rememberScrollState(), Orientation.Vertical),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(16.dp),
        ) {
            items(
                items = uiState.volumes,
                key = { volume -> volume.id}
            ) { volume ->
                VolumeCard(volume = volume, onClicked = volumeSelected)
            }
        }

        if(uiState.isLoading) {
            LoadingOverlay()
        }

        InfiniteListHandler(listState = listState, isOnLimit = uiState.isOnLimit) {
            onLoadMore()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ListScreenPreview() {
    VolumesTheme {
        val mockData = List(10) {
            FavoriteEntity(it, "$it", "Lorem Ipsum - $it", "").toVolume()
        }
        val uiStateMock : ListUiState.Success = ListUiState.Success(
            volumes = mockData,
            isLoading = true,
            isOnLimit = false
        )
        VolumesListScreen(uiStateMock, {}, {})
    }
}