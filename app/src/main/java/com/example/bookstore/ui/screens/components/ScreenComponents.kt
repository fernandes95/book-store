package com.example.bookstore.ui.screens.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.bookstore.R
import com.example.bookstore.data.models.dto.VolumeDto
import com.example.bookstore.ui.screens.home.ListUiState
import com.example.bookstore.ui.screens.theme.VolumesTheme

@Composable
fun VolumeCard(
    volume: VolumeDto.Volume,
    onClicked: (String) -> Unit,
    modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClicked(volume.id) },
        elevation = 8.dp,
        shape = RoundedCornerShape(8.dp),
    ) {

        Row(verticalAlignment = Alignment.CenterVertically) {
            AsyncImage(
                modifier = Modifier
                    .height(120.dp)
                    .width(120.dp)
                    .padding(15.dp),
                model = ImageRequest.Builder(context = LocalContext.current)
                    .data(volume.volumeInfo?.imageLinks?.thumbnail)
                    .crossfade(true)
                    .build(),
                contentDescription = null,
                contentScale = ContentScale.Fit,
                error = painterResource(id = R.drawable.ic_broken_image),
                placeholder = painterResource(id = R.drawable.no_photo)
            )
            Text(
                text = volume.volumeInfo?.title ?: "No Title Found",
                style = MaterialTheme.typography.subtitle1,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(15.dp)
            )
        }
    }
}

@Composable
fun LoadingOverlay(modifier: Modifier = Modifier){
    LoadingScreen(modifier = modifier
        .background(Color.White.copy(alpha = 0.5f)))
}

@Composable
fun LoadingScreen(modifier: Modifier = Modifier) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.fillMaxSize()
    ) {
        CircularProgressIndicator(
            color = MaterialTheme.colors.secondary,
            strokeWidth = 4.dp
        )
    }
}

@Composable
fun RetryScreen(title: String, retryAction: () -> Unit, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(title)
        Button(onClick = retryAction) {
            Text(stringResource(R.string.retry))
        }
    }
}
@Composable
fun VolumesList(
    uiState: ListUiState.Success,
    volumeSelected: (String) -> Unit,
    listState: LazyListState,
    modifier: Modifier = Modifier
){
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
}

@Preview(showBackground = true, )
@Composable
fun VolumeItemPreview() {
    VolumesTheme {
//        VolumeCard(VolumeDto.Volume("50","Lorem Ipsum", ""), {})
    }
}