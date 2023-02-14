package com.example.bookstore.ui.screens.detail

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.TextView
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat.startActivity
import androidx.core.text.HtmlCompat
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.bookstore.R
import com.example.bookstore.data.models.dto.VolumeDto
import com.example.bookstore.ui.screens.components.LoadingScreen
import com.example.bookstore.ui.screens.components.RetryScreen

@Composable
fun DetailScreen(
    uiState: DetailUiState,
    context: Context,
    favAction: () -> Unit,
    retryAction: () -> Unit,
    modifier: Modifier = Modifier
) {
    when (uiState) {
        is DetailUiState.Loading -> LoadingScreen(modifier)
        is DetailUiState.Success -> DetailPage(uiState.volume, uiState.isFav, favAction, context, modifier)
        else -> RetryScreen(stringResource(R.string.failed_loading), retryAction, modifier)
    }
}

@Composable
fun DetailPage(
    volume: VolumeDto.Volume,
    isFav: Boolean,
    favAction: () -> Unit,
    context: Context,
    modifier: Modifier
){
    Column(
        modifier
            .padding(start = 20.dp, end = 20.dp)
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
    ){
        IconButton(
            onClick = favAction,
            modifier = modifier
                        .align(Alignment.End)) {
            Icon(
                imageVector = if(isFav) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                contentDescription = null,
                modifier = modifier
                            .height(35.dp)
                            .width(35.dp)
            )
        }
        AsyncImage(
            modifier = Modifier
                .height(150.dp)
                .width(150.dp)
                .padding(top = 20.dp, bottom = 10.dp)
                .align(Alignment.CenterHorizontally),
            model = ImageRequest.Builder(context = LocalContext.current)
                .data(volume.volumeInfo?.imageLinks?.thumbnail)
                .crossfade(true)
                .build(),
            contentDescription = null,
            contentScale = ContentScale.Fit,
            error = painterResource(id = R.drawable.ic_broken_image),
            placeholder = painterResource(id = R.drawable.no_photo)
        )

        if(volume.volumeInfo?.title != null) {
            Title(stringResource(R.string.volume_detail_title), modifier)
            Description(volume.volumeInfo.title, modifier)
        }

        if(volume.volumeInfo?.authors != null) {
            val authorsSize = volume.volumeInfo.authors.size > 1
            val title = if(authorsSize)
                stringResource(R.string.volume_detail_author_plural)
            else stringResource(R.string.volume_detail_author_singular)
            val authorContent = if (authorsSize)
                volume.volumeInfo.authors.joinToString(separator = ", ")
            else
                volume.volumeInfo.authors.joinToString()

            Title(title, modifier)
            Description(authorContent, modifier)
        }
        if(volume.volumeInfo?.description != null) {
            Title(stringResource(R.string.volume_detail_description), modifier)
            AndroidView(
                modifier = Modifier.padding(top = 8.dp, bottom = 25.dp),
                factory = { context ->
                    TextView(context).apply {
                        text = HtmlCompat.fromHtml(volume.volumeInfo.description, HtmlCompat.FROM_HTML_MODE_LEGACY)
                    }
            })
        }

        Button(
            onClick = { buyLink(context, volume.saleInfo?.buyLink) },
            modifier = Modifier
                .padding(bottom = 20.dp)
                .align(alignment = Alignment.CenterHorizontally),
            enabled = volume.saleInfo?.saleability == "FOR_SALE"
        ) {
            Text(stringResource(R.string.volume_detail_buy))
        }
    }
}

private fun buyLink(context: Context, link: String?){
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
    startActivity(context, intent, null)
}


@Composable
private fun Title(
    text: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = text,
        textAlign = TextAlign.Center,
        style = MaterialTheme.typography.h5,
        fontWeight = FontWeight.Bold,
        modifier = modifier
    )
}

@Composable
private fun Description(
    text: String,
    modifier: Modifier
) {
    Text(
        text = text,
        style = MaterialTheme.typography.body1,
        modifier = modifier.padding(top = 8.dp, bottom = 25.dp)
    )
}

@Preview
@Composable
private fun TextCombinationPreview(){
    MaterialTheme{
        Column {
            Title(text = "Title", modifier = Modifier)
            Description(
                "Austen’s most celebrated novel tells the story of Elizabeth Bennet, a bright, lively young woman with four sisters, and a mother determined to marry them to wealthy men. At a party near the Bennets’ home in the English countryside, Elizabeth meets the wealthy, proud Fitzwilliam Darcy. Elizabeth initially finds Darcy haughty and intolerable, but circumstances continue to unite the pair. Mr. Darcy finds himself captivated by Elizabeth’s wit and candor, while her reservations about his character slowly vanish. The story is as much a social critique as it is a love story, and the prose crackles with Austen’s wry wit.",
                    Modifier)
        }
    }
}
@Preview
@Composable
private fun DetailScreenPreview(){
    MaterialTheme{
        val authors = ArrayList<String>()
        authors.add("author1")
        authors.add("author2")

        val volInfo =  VolumeDto.VolumeInfo(
            title = "Book #1",
            description = "Austen’s most celebrated novel tells the story of Elizabeth Bennet, a bright, lively young woman with four sisters, and a mother determined to marry them to wealthy men. At a party near the Bennets’ home in the English countryside, Elizabeth meets the wealthy, proud Fitzwilliam Darcy. Elizabeth initially finds Darcy haughty and intolerable, but circumstances continue to unite the pair. Mr. Darcy finds himself captivated by Elizabeth’s wit and candor, while her reservations about his character slowly vanish. The story is as much a social critique as it is a love story, and the prose crackles with Austen’s wry wit.",
            authors = authors,
            allowAnonLogging = false,
            canonicalVolumeLink = "",
            subtitle = null,
            publisher = null,
            publishedDate = null,
            industryIdentifiers = null,
            readingModes = null,
            pageCount = null,
            printType = null,
            categories = null,
            averageRating = null,
            ratingsCount = null,
            maturityRating = null,
            contentVersion = null,
            panelizationSummary = null,
            imageLinks = null,
            language = null,
            previewLink = null,
            infoLink = null,
        )

        val vol = VolumeDto.Volume(
            kind = "",
            id = "0",
            etag = "",
            volumeInfo = volInfo,
            selfLink = null,
            saleInfo = null,
            accessInfo = null,
            searchInfo = null
        )
        DetailPage(volume = vol, false, {}, LocalContext.current, modifier = Modifier)
    }
}