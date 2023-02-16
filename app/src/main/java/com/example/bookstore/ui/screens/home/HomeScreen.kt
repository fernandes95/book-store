package com.example.bookstore.ui.screens.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
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
    uiState: ListUiState,
    volumeSelected: (String) -> Unit,
    onSearchAction: (String) -> Unit,
    retryAction: () -> Unit,
    onLoadMore: () -> Unit,
    modifier: Modifier = Modifier
) {
    when (uiState) {
        is ListUiState.Loading -> LoadingScreen(modifier)
        is ListUiState.Success -> VolumesListScreen(uiState, volumeSelected, onSearchAction, onLoadMore, modifier)
        else -> RetryScreen(stringResource(R.string.failed_loading), retryAction, modifier)
    }
}

@Composable
fun InfiniteListHandler(
    listState: LazyListState,
    isOnLimit: Boolean,
    buffer: Int = 2,
    onLoadMore: () -> Unit
) {
    if(isOnLimit)
        return

    val loadMore = remember {
        derivedStateOf {
            val layoutInfo = listState.layoutInfo
            val totalItemsNumber = layoutInfo.totalItemsCount
            val lastVisibleItemIndex = (layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0).plus(1)

            lastVisibleItemIndex > (totalItemsNumber.minus(buffer))
        }
    }

    LaunchedEffect(loadMore) {
        snapshotFlow { loadMore.value }
            .distinctUntilChanged()
            .filter { it }
            .collect {
                onLoadMore()
            }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SearchView(searchAction: (String) -> Unit) {
    val state = rememberSaveable { mutableStateOf("") }
    val focused = rememberSaveable { mutableStateOf(false) }
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    TextField(
        value = state.value,
        onValueChange = { value ->
            state.value = value
            focused.value = value.isNotEmpty()
        },
        modifier = Modifier.fillMaxWidth(),
        textStyle = TextStyle(color = Color.White, fontSize = 18.sp),
        leadingIcon = {
            Icon(
                Icons.Default.Search,
                contentDescription = "",
                modifier = Modifier
                    .padding(15.dp)
                    .size(24.dp)
            )
        },
        trailingIcon = {
            if (focused.value) {
                IconButton(onClick = { state.value = "" }) {
                    Icon(
                        Icons.Default.Close,
                        contentDescription = "",
                        modifier = Modifier
                            .padding(15.dp)
                            .size(24.dp)
                    )
                }
            }
        },
        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Search),
        keyboardActions = KeyboardActions(onSearch = {
            searchAction(state.value)
            keyboardController?.hide()
            focusManager.clearFocus()
            focused.value = false
        }),
        singleLine = true,
        shape = RectangleShape, // The TextFiled has rounded corners top left and right by default
        colors = TextFieldDefaults.textFieldColors(
            textColor = Color.White,
            cursorColor = Color.White,
            leadingIconColor = Color.White,
            trailingIconColor = Color.White,
            backgroundColor = colorResource(id = R.color.purple_700),
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent
        )
    )
}

@Composable
fun VolumesListScreen(
    uiState: ListUiState.Success,
    volumeSelected: (String) -> Unit,
    onSearchAction: (String) -> Unit,
    onLoadMore: () -> Unit,
    modifier: Modifier = Modifier)
{
    Box(Modifier.fillMaxSize()) {
        val listState = rememberLazyListState()

        Column {
            SearchView(onSearchAction)
            VolumesList(uiState, volumeSelected, listState, modifier)
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
fun SearchViewPreview() {
    SearchView {}
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
        VolumesListScreen(uiStateMock, {}, {}, {})
    }
}