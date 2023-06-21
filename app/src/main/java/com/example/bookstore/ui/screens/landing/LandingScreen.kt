package com.example.bookstore.ui.screens.landing

import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.bookstore.R
import com.example.bookstore.data.google.GoogleApiContract
import com.example.bookstore.ui.screens.components.LoadingScreen
import com.example.bookstore.ui.screens.components.RetryScreen
import com.example.bookstore.ui.screens.theme.VolumesTheme
import com.google.android.gms.common.api.ApiException

@Composable
fun LandingScreen(
    uiState: LandingUiState,
    loginAction: () -> Unit,
    offlineAction: () -> Unit,
    modifier: Modifier = Modifier
){
    val signInRequestCode = 1
    val authResultLauncher =
        rememberLauncherForActivityResult(contract =  GoogleApiContract()) { task ->
            try {
                val gsa = task?.getResult(ApiException::class.java)
                if (gsa != null) { loginAction.invoke() }
            } catch (e: ApiException) {
                Log.d("Error in AuthScreen%s", e.toString())
            }
        }

    when(uiState){
        is LandingUiState.Loading -> LoadingScreen(modifier)
        is LandingUiState.NoAccount -> LandingPage(
            { authResultLauncher.launch(signInRequestCode) },
            { offlineAction.invoke() },
            modifier
        )
        is LandingUiState.Success -> {
            LaunchedEffect(key1 = "homeNavigationKey") { loginAction.invoke() }
        }
        else -> RetryScreen(stringResource(R.string.failed_loading), {}, modifier)
    }
}

@Composable
fun LandingPage(
    signInAction: () -> Unit,
    offlineAction: () -> Unit,
    modifier: Modifier = Modifier
    )
{
    Column(
      modifier
        .padding(20.dp)
        .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
    )
    {
        Button(
            onClick = signInAction,
            colors = ButtonDefaults.buttonColors(backgroundColor = Color.White)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                  .padding(
                    start = 12.dp,
                    end = 16.dp,
                    top = 12.dp,
                    bottom = 12.dp
                  )
                  .fillMaxWidth()
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_google_logo),
                    contentDescription = "Google Login",
                    tint = Color.Unspecified
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Sign in With Google"
                )

                Spacer(modifier = Modifier.width(16.dp))
            }
        }

        TextButton(
            onClick = offlineAction,
            colors = ButtonDefaults.buttonColors(backgroundColor = Color.Transparent),
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text(
                text = "Enter without account",
                color = MaterialTheme.colors.primary
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LandingPreview(){
    VolumesTheme {
        LandingPage({}, {})
    }
}