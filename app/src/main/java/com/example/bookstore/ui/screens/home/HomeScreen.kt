package com.example.bookstore.ui.screens.home

import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.bookstore.R
import com.example.bookstore.ui.screens.components.LoadingScreen
import com.example.bookstore.ui.screens.components.RetryScreen

@Composable
fun HomeScreen(
  uiState: HomeUiState,
  logoutAction: () -> Unit,
  loggedOutAction: () -> Unit,
  modifier: Modifier = Modifier
) {
    when (uiState) {
        is HomeUiState.Loading -> LoadingScreen(modifier)
        is HomeUiState.Success -> Homepage(uiState, logoutAction, modifier)
        is HomeUiState.LoggedOut -> {
            LaunchedEffect(key1 = "loggedOutKey") { loggedOutAction.invoke() }
        }
        else -> RetryScreen(stringResource(R.string.failed_loading), {}, modifier)
    }
}

@Composable
fun Homepage(
    uiState: HomeUiState,
    logoutAction: () -> Unit,
    modifier: Modifier = Modifier
){
    Column(modifier =
    modifier
      .fillMaxSize()
      .padding(20.dp)
    ) {
        if((uiState as HomeUiState.Success).username != null) {
            Row(
                modifier = modifier
                    .fillMaxWidth(),
                verticalAlignment = CenterVertically,
            ) {
                Text(
                    text = uiState.username!!,
                    style = MaterialTheme.typography.h4,
                    modifier = Modifier.weight(1.0f)
                )
                IconButton(onClick = logoutAction) {
                    Icon(
                        imageVector = Icons.Filled.Logout,
                        contentDescription = null,
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SearchViewPreview() {
    val uiState = HomeUiState.Success("username")
    MaterialTheme{
        Homepage(uiState, {})
    }
}

