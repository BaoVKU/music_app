package com.example.musicapp.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.musicapp.ui.components.AlbumItem
import com.example.musicapp.ui.components.BottomNavigationBar
import com.example.musicapp.ui.components.SongItem
import com.example.musicapp.ui.components.TopBar
import com.example.musicapp.ui.navigation.FavoriteDestination
import com.example.musicapp.ui.navigation.NavigationDestination

@Composable
fun FavoriteScreen(
    modifier: Modifier = Modifier,
    onNavigate: (NavigationDestination) -> Unit,
    onNavigateWithArgument: (NavigationDestination, String) -> Unit
) {
    Scaffold(
        topBar = {
            TopBar(title = FavoriteDestination.title, onNavigate = onNavigate)
        },
        bottomBar = {
            BottomNavigationBar(currentDestination = FavoriteDestination, onNavigate = onNavigate)
        },
        modifier = modifier
    ) { contentPadding ->
        LazyColumn(
            contentPadding = contentPadding,
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
        ) {
            item {
                Column {
                    Text(
                        text = "Album",
                        style = MaterialTheme.typography.titleLarge
                    )

                    (1..5).forEach {
                        AlbumItem(onNavigateWithArgument = onNavigateWithArgument)
                    }

                }
            }
            item {
                Column {
                    Text(
                        text = "Song",
                        style = MaterialTheme.typography.titleLarge
                    )
//                    (1..5).forEach {
//                        SongItem(
//                            songName = "Song name",
//                            songArtist = "Artist name",
//                            onNavigateWithArgument = onNavigateWithArgument
//                        )
//                    }
                }
            }

        }
    }
}