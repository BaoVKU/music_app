package com.example.musicapp.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.musicapp.ui.components.AlbumItem
import com.example.musicapp.ui.components.BottomNavigationBar
import com.example.musicapp.ui.components.SongItem
import com.example.musicapp.ui.components.TopBar
import com.example.musicapp.ui.navigation.FavoriteDestination
import com.example.musicapp.ui.navigation.NavigationDestination
import com.example.musicapp.ui.viewmodels.AlbumListUiState
import com.example.musicapp.ui.viewmodels.FavoriteViewModel
import com.example.musicapp.ui.viewmodels.SongListUiState

@Composable
fun FavoriteScreen(
    modifier: Modifier = Modifier,
    onNavigate: (NavigationDestination) -> Unit,
    onNavigateWithArgument: (NavigationDestination, String) -> Unit,
    onSongQueryChanged: (String) -> Unit,
    favoriteViewModel: FavoriteViewModel = viewModel()
) {
    val songListUiState by favoriteViewModel.songListUiState.collectAsState()
    val albumListUiState by favoriteViewModel.albumListUiState.collectAsState()

    LaunchedEffect(key1 = favoriteViewModel.reloadScreen) {
        favoriteViewModel.initFavoriteScreen()
    }

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
                    if (albumListUiState is AlbumListUiState.Success) {
                        val albums = (albumListUiState as AlbumListUiState.Success).albums
                        if (albums.isNotEmpty()) {
                            albums.forEach { album ->
                                AlbumItem(
                                    album = album,
                                    onNavigateWithArgument = onNavigateWithArgument
                                )
                            }
                        } else {
                            Text(
                                text = "No album found",
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }
            }
            item {
                Column {
                    Text(
                        text = "Song",
                        style = MaterialTheme.typography.titleLarge
                    )
                    if (songListUiState is SongListUiState.Success) {
                        val songs = (songListUiState as SongListUiState.Success).songs
                        if (songs.isNotEmpty()) {
                            songs.forEach { song ->
                                SongItem(
                                    song = song,
                                    onSongQueryChanged = onSongQueryChanged
                                )
                            }
                        } else {
                            Text(
                                text = "No song found",
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }

            }
        }
    }
}