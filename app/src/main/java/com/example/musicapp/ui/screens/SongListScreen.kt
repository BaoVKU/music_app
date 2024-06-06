package com.example.musicapp.ui.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.musicapp.ui.components.AddToPlaylistDialog
import com.example.musicapp.ui.components.SongItem
import com.example.musicapp.ui.components.TopBar
import com.example.musicapp.ui.navigation.SongListDestination
import com.example.musicapp.ui.navigation.NavigationDestination
import com.example.musicapp.ui.viewmodels.PlaylistListUiState
import com.example.musicapp.ui.viewmodels.PlaylistViewModel
import com.example.musicapp.ui.viewmodels.SongListUiState
import com.example.musicapp.ui.viewmodels.SongListViewModel

@Composable
fun SongListScreen(
    modifier: Modifier = Modifier,
    onNavigate: (NavigationDestination) -> Unit,
    onNavigateBack: () -> Unit,
    onSongQueryChanged: (String) -> Unit,
    songListViewModel: SongListViewModel = viewModel(),
    playlistViewModel: PlaylistViewModel = viewModel()
) {
    val context = LocalContext.current
    val songListUiState by songListViewModel.songListUiState.collectAsState()
    val playlistListUiState by playlistViewModel.playlistListUiState.collectAsState()
    var isOpenAddToPlaylistDialog by remember { mutableStateOf(false) }
    var addToPlaylistSongId by remember { mutableStateOf("") }
    Scaffold(
        topBar = {
            TopBar(
                title = SongListDestination.title,
                canNavigateBack = true,
                hasActions = false,
                onNavigate = onNavigate,
                onNavigateBack = onNavigateBack
            )
        }, modifier = modifier
    ) { contentPadding ->
        LazyColumn(
            contentPadding = contentPadding,
            modifier = Modifier.padding(8.dp)
        ) {
            if (songListUiState is SongListUiState.Success) {
                val songs = (songListUiState as SongListUiState.Success).songs
                items(songs) { song ->
                    SongItem(
                        song = song,
                        onSongQueryChanged = onSongQueryChanged,
                        onAddToPlaylistClick = {
                            addToPlaylistSongId = song.id
                            isOpenAddToPlaylistDialog = true
                        }
                    )
                }
            }
        }
    }
    if (isOpenAddToPlaylistDialog) {
        if (playlistListUiState is PlaylistListUiState.Success) {
            val playlists = (playlistListUiState as PlaylistListUiState.Success).playlists
            AddToPlaylistDialog(
                list = playlists,
                onDismissRequest = { isOpenAddToPlaylistDialog = false },
                onChoosePlaylist = { playlistId ->
                    if (addToPlaylistSongId.isNotEmpty()) {
                        playlistViewModel.addToPlaylist(context, playlistId, addToPlaylistSongId)
                    }
                        isOpenAddToPlaylistDialog = false
                }
            )
        }
    }
}
