package com.example.musicapp.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.musicapp.ui.components.AlbumItem
import com.example.musicapp.ui.components.TopBar
import com.example.musicapp.ui.navigation.AlbumListDestination
import com.example.musicapp.ui.navigation.NavigationDestination
import com.example.musicapp.ui.viewmodels.AlbumListUiState
import com.example.musicapp.ui.viewmodels.AlbumListViewModel

@Composable
fun AlbumListScreen(
    modifier: Modifier = Modifier,
    onNavigate: (NavigationDestination) -> Unit,
    onNavigateWithArgument: (NavigationDestination, String) -> Unit,
    onNavigateBack: () -> Unit,
    albumListViewModel: AlbumListViewModel = viewModel()
) {
    val albumListUiState by albumListViewModel.albumListUiState.collectAsState()
    Scaffold(
        topBar = {
            TopBar(
                title = AlbumListDestination.title,
                canNavigateBack = true,
                hasActions = false,
                onNavigate = onNavigate,
                onNavigateBack = onNavigateBack
            )

        },
        modifier = modifier
    ) { contentPadding ->
        if (albumListUiState is AlbumListUiState.Success) {
            val albums = (albumListUiState as AlbumListUiState.Success).albums
            LazyColumn(contentPadding = contentPadding) {
                items(albums) { album ->
                    AlbumItem(album = album, onNavigateWithArgument = onNavigateWithArgument)
                }
            }
        }
    }
}