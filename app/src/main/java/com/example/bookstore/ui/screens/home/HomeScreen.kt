package com.example.bookstore.ui.screens.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bookstore.R
import com.example.bookstore.data.models.toVolume
import com.example.bookstore.data.room.FavoriteEntity
import com.example.bookstore.ui.screens.components.*
import com.example.bookstore.ui.screens.theme.VolumesTheme
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter

@Composable
fun HomeScreen(
    uiState: HomeUiState,
    modifier: Modifier = Modifier
) {
    when (uiState) {
        is HomeUiState.Loading -> LoadingScreen(modifier)
        is HomeUiState.Success -> Homepage(uiState, modifier)
        else -> RetryScreen(stringResource(R.string.failed_loading), {}, modifier)
    }
}

@Composable
fun Homepage(
    uiState: HomeUiState,
    modifier: Modifier = Modifier
){
    Column(modifier) {
        Text(text = (uiState as HomeUiState.Success).username)
    }
}


@Preview(showBackground = true)
@Composable
fun SearchViewPreview() {
//    Homepage(Hom)
}

