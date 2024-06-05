package com.example.musicapp.ui.screens

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.musicapp.ui.components.SongItem
import com.example.musicapp.ui.components.TopBar
import com.example.musicapp.ui.navigation.NavigationDestination

@Composable
fun PlaylistDetailScreen(
    modifier: Modifier = Modifier,
    onNavigate: (NavigationDestination) -> Unit,
    onNavigateWithArgument: (NavigationDestination, String) -> Unit,
    onNavigateBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopBar(
                title = "Playlist name",
                canNavigateBack = true,
                onNavigate = onNavigate,
                onNavigateBack = onNavigateBack
            )
        },
        modifier = modifier
    ) { contentPadding ->
        LazyColumn(
            contentPadding = contentPadding
        ) {
//            items(5) {
//                SongItem(
//                    songName = "Song name",
//                    songArtist = "Artist name",
//                    onNavigateWithArgument = onNavigateWithArgument
//                )
//            }
        }
    }
}