package com.example.musicapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.musicapp.ui.theme.MusicAppTheme
import com.google.firebase.Firebase
import com.google.firebase.appcheck.AppCheckProvider
import com.google.firebase.appcheck.AppCheckProviderFactory
import com.google.firebase.appcheck.FirebaseAppCheck
import com.google.firebase.appcheck.appCheck
import com.google.firebase.appcheck.debug.DebugAppCheckProviderFactory
import com.google.firebase.appcheck.playintegrity.PlayIntegrityAppCheckProviderFactory
import com.google.firebase.appcheck.playintegrity.internal.PlayIntegrityAppCheckProvider
import com.google.firebase.initialize

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Firebase.initialize(context = this)
        setContent {
            MusicAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MusicApp()
                }
            }
        }
    }
}

