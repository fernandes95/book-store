package com.example.bookstore.ui.screens.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.bookstore.data.models.dto.VolumeDto
import com.example.bookstore.ui.screens.components.ErrorScreen
import com.example.bookstore.ui.screens.components.LoadingScreen
import com.example.bookstore.ui.screens.components.VolumeCard
import com.example.bookstore.ui.screens.theme.VolumesTheme

@Composable
fun HomeScreen(
    uiState: HomeUiState,
    volumeSelected: (String) -> Unit,
    retryAction: () -> Unit,
    modifier: Modifier = Modifier
) {
    when (uiState) {
        is HomeUiState.Loading -> LoadingScreen(modifier)
        is HomeUiState.Success -> uiState.volumes.items?.let { VolumesListScreen(it.toList(), volumeSelected  , modifier) }
        else -> ErrorScreen(retryAction, modifier)
    }
}

@Composable
fun VolumesListScreen(
    volumes: List<VolumeDto.Volume>,
    volumeSelected: (String) -> Unit,
    modifier: Modifier = Modifier) {
    LazyColumn(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(16.dp)
    ) {
        items(
            items = volumes,
            key = { volume -> volume.id}
        ) { volume ->
            VolumeCard(volume = volume, onClicked = volumeSelected )
        }
    }
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