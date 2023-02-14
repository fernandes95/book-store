package com.example.bookstore

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.bookstore.ui.screens.VolumesApp
import com.example.bookstore.ui.screens.theme.VolumesTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            VolumesTheme {
                VolumesApp()
            }
        }
    }
}