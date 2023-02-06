package com.example.bookstore.ui.screens.home

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.asLiveData
import com.example.bookstore.data.models.dto.VolumeDto
import com.example.bookstore.ui.screens.components.ErrorScreen
import com.example.bookstore.ui.screens.components.LoadingScreen
import com.example.bookstore.ui.screens.components.VolumeCard
import com.example.bookstore.ui.screens.theme.VolumesTheme
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.toList
import okhttp3.internal.toImmutableList

@Composable
fun HomeScreen(
    uiState: HomeUiState,
    volumeSelected: (String) -> Unit,
    retryAction: () -> Unit,
    onLoadMore: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    when (uiState) {
        is HomeUiState.Loading -> LoadingScreen(modifier)
        is HomeUiState.Success -> VolumesListScreen(uiState.volumes, volumeSelected, onLoadMore, modifier)
        else -> ErrorScreen(retryAction, modifier)
    }
}

@Composable
fun InfiniteListHandler(
    listState: LazyListState,
    buffer: Int = 2,
    onLoadMore: (Int) -> Unit
) {
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
            .filter{ it }
            .collect {
                onLoadMore(listState.layoutInfo.totalItemsCount)
            }
    }
}

@Composable
fun VolumesListScreen(
    volumes: List<VolumeDto.Volume>,
    volumeSelected: (String) -> Unit,
    onLoadMore: (Int) -> Unit,
    modifier: Modifier = Modifier)
{
    val listState = rememberLazyListState()
    val scrollState = rememberScrollState()

    LazyColumn(
        state = listState,
        modifier = modifier
            .fillMaxWidth()
            .scrollable(rememberScrollState(), Orientation.Vertical),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(16.dp),
    ) {
        items(
            items = volumes,
            key = { volume -> volume.id}
        ) { volume ->
            VolumeCard(volume = volume, onClicked = volumeSelected )
        }
    }

    InfiniteListHandler(listState = listState, onLoadMore = onLoadMore)
}

@Preview(showBackground = true)
@Composable
fun ListScreenPreview() {
    VolumesTheme {
//        val mockData = List(10) {
//            Volume("$it","Lorem Ipsum - $it", "")
//        }
//        VolumesListScreen(mockData)
    }
}