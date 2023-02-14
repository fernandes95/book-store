package com.example.bookstore.ui.screens.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
fun LoadingScreen(modifier: Modifier = Modifier) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.fillMaxSize()
    ) {
        Image(
            modifier = Modifier.size(200.dp),
            painter = painterResource(R.drawable.ic_loading),
            contentDescription = null
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

@Preview(showBackground = true, )
@Composable
fun VolumeItemPreview() {
    VolumesTheme {
//        VolumeCard(VolumeDto.Volume("50","Lorem Ipsum", ""), {})
    }
}