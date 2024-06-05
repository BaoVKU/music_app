package com.example.musicapp.ui.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.musicapp.ui.components.SongItem
import com.example.musicapp.ui.components.TopBar
import com.example.musicapp.ui.navigation.SongListDestination
import com.example.musicapp.ui.navigation.NavigationDestination
import com.example.musicapp.ui.viewmodels.SongListUiState
import com.example.musicapp.ui.viewmodels.SongListViewModel

@Composable
fun SongListScreen(
    modifier: Modifier = Modifier,
    onNavigate: (NavigationDestination) -> Unit,
    onNavigateBack: () -> Unit,
    onSongQueryChanged: (String) -> Unit,
    songListViewModel: SongListViewModel = viewModel()
) {
    val songListUiState by songListViewModel.songListUiState.collectAsState()
    Scaffold(
        topBar = {
            TopBar(
                title = SongListDestination.title,
                canNavigateBack = true,
                onNavigate = onNavigate,
                onNavigateBack = onNavigateBack
            )
        }, modifier = modifier
    ) { contentPadding ->
        LazyColumn(
            contentPadding = contentPadding,
            modifier = Modifier.padding(8.dp)
        ) {
            if(songListUiState is SongListUiState.Success){
                val songs = (songListUiState as SongListUiState.Success).songs
                items(songs){song->
                    SongItem(
                        song = song,
                        onSongQueryChanged = onSongQueryChanged
                    )
                }
            }
        }
    }
}
