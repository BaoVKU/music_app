package com.example.musicapp.ui.screens

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.musicapp.ui.components.SongItem
import com.example.musicapp.ui.components.TopBar
import com.example.musicapp.ui.navigation.NavigationDestination
import com.example.musicapp.ui.viewmodels.PlaylistDetailViewModel
import com.example.musicapp.ui.viewmodels.PlaylistUiState
import com.example.musicapp.ui.viewmodels.PlaylistViewModel
import com.example.musicapp.ui.viewmodels.SongListUiState

@Composable
fun PlaylistDetailScreen(
    modifier: Modifier = Modifier,
    onNavigate: (NavigationDestination) -> Unit,
    onNavigateWithArgument: (NavigationDestination, String) -> Unit,
    onNavigateBack: () -> Unit,
    onSongQueryChanged: (String) -> Unit,
    playlistDetailViewModel: PlaylistDetailViewModel = viewModel()
) {
    val context = LocalContext.current
    val playlistUiState by playlistDetailViewModel.playlistUiState.collectAsState()
    val songListUiState by playlistDetailViewModel.songListUiState.collectAsState()

   LaunchedEffect(key1 = playlistDetailViewModel.reloadScreen) {
       playlistDetailViewModel.getPlaylistDetailScreen()
   }

    if(playlistUiState is PlaylistUiState.Success) {
        val playlist = (playlistUiState as PlaylistUiState.Success).playlist
        Scaffold(
            topBar = {
                TopBar(
                    title = playlist.name ?: "Playlist",
                    canNavigateBack = true,
                    hasActions = false,
                    onNavigate = onNavigate,
                    onNavigateBack = onNavigateBack
                )
            },
            modifier = modifier
        ) { contentPadding ->
            if(songListUiState is SongListUiState.Success) {
                val songs = (songListUiState as SongListUiState.Success).songs
                LazyColumn(
                    contentPadding = contentPadding
                ) {
                    items(songs){song->
                        SongItem(
                            song = song,
                            onSongQueryChanged = {songQuery ->
                            onSongQueryChanged(songQuery+"&playlist="+playlist.id)
                        },
                            isInPlaylistScreen = true,
                            onRemoveFromPlaylistClick = {
                                playlistDetailViewModel.removeFromPlaylist(context, playlist.id!!, song.id)
                            })
                    }
                }
            }
        }
    }
}