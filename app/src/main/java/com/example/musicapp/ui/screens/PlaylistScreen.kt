package com.example.musicapp.ui.screens


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.musicapp.R
import com.example.musicapp.ui.components.BottomNavigationBar
import com.example.musicapp.ui.components.PlaylistItem
import com.example.musicapp.ui.components.TopBar
import com.example.musicapp.ui.navigation.NavigationDestination
import com.example.musicapp.ui.navigation.PlaylistDestination
import com.example.musicapp.ui.theme.MusicAppTheme
import com.example.musicapp.ui.viewmodels.PlaylistListUiState
import com.example.musicapp.ui.viewmodels.PlaylistViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlaylistScreen(
    modifier: Modifier = Modifier,
    onNavigate: (NavigationDestination) -> Unit,
    onNavigateWithArgument: (NavigationDestination, String) -> Unit,
    playlistViewModel: PlaylistViewModel = viewModel()
) {
    val context = LocalContext.current
    val playlistListUiState by playlistViewModel.playlistListUiState.collectAsState()
    var isOpenPlaylistCreateDialog by remember {
        mutableStateOf(false)
    }
    var playlistName by remember {
        mutableStateOf("")
    }
    LaunchedEffect(key1 = playlistViewModel.reloadScreen) {
        playlistViewModel.initPlaylistScreen()
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = PlaylistDestination.title) },
                actions = {
                    IconButton(onClick = {
                        isOpenPlaylistCreateDialog = true
                    }) {
                        Icon(imageVector = Icons.Default.Add, contentDescription = "add_icon")
                    }
                }
            )
        },
        bottomBar = {
            BottomNavigationBar(
                currentDestination = PlaylistDestination,
                onNavigate = onNavigate
            )
        },
        modifier = modifier
    ) { contentPadding ->
        if (playlistListUiState is PlaylistListUiState.Success) {
            val playlists = (playlistListUiState as PlaylistListUiState.Success).playlists
            LazyColumn(contentPadding = contentPadding) {
                items(playlists) { playlist ->
                    PlaylistItem(
                        playlist = playlist,
                        onNavigateWithArgument = onNavigateWithArgument,
                        onDeleteClick = {
                            playlistViewModel.deletePlaylist(context,playlist.id!!)
                        }
                    )
                }
            }
        }
    }
    if (isOpenPlaylistCreateDialog) {
        PlaylistCreateDialog(
            value = playlistName,
            onValueChange = { playlistName = it },
            onDismissRequest = { isOpenPlaylistCreateDialog = false },
            onSubmit = {
                playlistViewModel.createPlaylist(context,playlistName)
                isOpenPlaylistCreateDialog = false
            })
    }

}

@Composable
fun PlaylistCreateDialog(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    onDismissRequest: () -> Unit,
    onSubmit: () -> Unit
) {
    Dialog(onDismissRequest = onDismissRequest) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier
                .clip(shape = RoundedCornerShape(24.dp))
                .background(color = MaterialTheme.colorScheme.background)
                .padding(16.dp)
        ) {
            Text(text = "Create Playlist", style = MaterialTheme.typography.headlineSmall)
            Divider(modifier = Modifier.padding(vertical = 8.dp))
            OutlinedTextField(
                value = value,
                label = {
                    Text(text = "Name")
                },
                onValueChange = onValueChange
            )
            TextButton(onClick = onSubmit) {
                Text(text = "Create")
            }
        }
    }
}