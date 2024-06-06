package com.example.musicapp.ui.screens

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.musicapp.ui.components.AddToPlaylistDialog
import com.example.musicapp.ui.components.BottomNavigationBar
import com.example.musicapp.ui.components.SongItem
import com.example.musicapp.ui.navigation.NavigationDestination
import com.example.musicapp.ui.navigation.SearchDestination
import com.example.musicapp.ui.viewmodels.PlaylistListUiState
import com.example.musicapp.ui.viewmodels.PlaylistViewModel
import com.example.musicapp.ui.viewmodels.SearchViewModel
import com.example.musicapp.ui.viewmodels.SongListUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    modifier: Modifier = Modifier,
    onNavigate: (NavigationDestination) -> Unit,
    onNavigateWithArgument: (NavigationDestination, String) -> Unit,
    onNavigateBack: () -> Unit,
    onSongQueryChanged: (String) -> Unit,
    searchViewModel: SearchViewModel = viewModel(),
    playlistViewModel: PlaylistViewModel = viewModel()
) {
    val context = LocalContext.current
    val searchResults by searchViewModel.searchResults.collectAsState()
    var searchValue by remember { mutableStateOf("") }
    val playlistListUiState by playlistViewModel.playlistListUiState.collectAsState()
    var isOpenAddToPlaylistDialog by remember { mutableStateOf(false) }
    var addToPlaylistSongId by remember { mutableStateOf("") }
    Scaffold (
        topBar = {
            TopAppBar(title = {
                OutlinedTextField(
                    value = searchValue,
                    onValueChange = {
                        searchValue = it
                        searchViewModel.searchSongs(it)
                    },
                    placeholder = { Text(text = "Search...") },
                    maxLines = 1,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color.Transparent,
                        unfocusedBorderColor = Color.Transparent,
                    ),
                    modifier = modifier
                        .fillMaxWidth()
                )
            },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "back_icon")
                    }
                }, actions = {
                    IconButton(onClick = {
                        searchValue = ""
                        searchViewModel.searchSongs("")
                    }) {
                        Icon(imageVector = Icons.Default.Clear, contentDescription = "clear_icon")
                    }
                })
        }
    ){
        contentPadding ->
        LazyColumn(contentPadding=contentPadding) {
            items(searchResults) { song ->
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