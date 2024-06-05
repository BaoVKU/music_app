package com.example.musicapp.ui.screens

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.musicapp.ui.components.BottomNavigationBar
import com.example.musicapp.ui.components.SongItem
import com.example.musicapp.ui.components.TopBar
import com.example.musicapp.ui.navigation.DownloadDestination
import com.example.musicapp.ui.navigation.NavigationDestination

@Composable
fun DownloadScreen(
    modifier: Modifier = Modifier,
    onNavigate: (NavigationDestination) -> Unit,
    onNavigateWithArgument: (NavigationDestination, String) -> Unit
) {
    Scaffold(
        topBar = {
            TopBar(title = DownloadDestination.title, onNavigate = onNavigate)
        },
        bottomBar = {
            BottomNavigationBar(currentDestination = DownloadDestination, onNavigate = onNavigate)
        },
        modifier = modifier

    ) { contentPadding ->
        LazyColumn(contentPadding = contentPadding) {
//            items(10) {
//                SongItem(
//                    songName = "Song name",
//                    songArtist = "Song artist",
//                    onNavigateWithArgument = onNavigateWithArgument
//                )
//            }
        }
    }
}