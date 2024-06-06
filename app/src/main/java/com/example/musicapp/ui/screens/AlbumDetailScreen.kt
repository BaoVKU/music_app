package com.example.musicapp.ui.screens

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.musicapp.R
import com.example.musicapp.ui.components.AddToPlaylistDialog
import com.example.musicapp.ui.components.SongItem
import com.example.musicapp.ui.components.TopBar
import com.example.musicapp.ui.navigation.AlbumDetailDestination
import com.example.musicapp.ui.navigation.NavigationDestination
import com.example.musicapp.ui.viewmodels.AlbumDetailViewModel
import com.example.musicapp.ui.viewmodels.AlbumUiState
import com.example.musicapp.ui.viewmodels.FavoriteViewModel
import com.example.musicapp.ui.viewmodels.PlaylistListUiState
import com.example.musicapp.ui.viewmodels.PlaylistViewModel
import com.example.musicapp.ui.viewmodels.SongListUiState
import com.example.musicapp.ui.viewmodels.ViewModelFactoryProvider
import com.google.firebase.Firebase
import com.google.firebase.storage.storage


@Composable
fun AlbumDetailScreen(
    modifier: Modifier = Modifier,
    onNavigate: (NavigationDestination) -> Unit,
    onNavigateWithArgument: (NavigationDestination, String) -> Unit,
    onNavigateBack: () -> Unit,
    onSongQueryChanged: (String) -> Unit,
    albumDetailViewModel: AlbumDetailViewModel = viewModel(factory = ViewModelFactoryProvider.Factory),
    playlistViewModel: PlaylistViewModel = viewModel(),
    favoriteViewModel: FavoriteViewModel = viewModel()
) {
    val context = LocalContext.current
    val storageRef = Firebase.storage.reference
    val albumUiState by albumDetailViewModel.albumUiState.collectAsState()
    val songListUiState by albumDetailViewModel.songListUiState.collectAsState()
    var thumbnailUri by remember {
        mutableStateOf<Uri>(Uri.EMPTY)
    }

    val playlistListUiState by playlistViewModel.playlistListUiState.collectAsState()
    var isOpenAddToPlaylistDialog by remember { mutableStateOf(false) }
    var addToPlaylistSongId by remember { mutableStateOf("") }
    Scaffold(
        topBar = {
            TopBar(
                title = AlbumDetailDestination.title,
                canNavigateBack = true,
                hasActions = false,
                onNavigate = onNavigate,
                onNavigateBack = onNavigateBack
            )
        },
        modifier = modifier
    ) { contentPadding ->
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(contentPadding)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (albumUiState is AlbumUiState.Success) {
                    val album = (albumUiState as AlbumUiState.Success).album

                    var isFavorite by remember {
                        mutableStateOf(album.isFavorite)
                    }

                    storageRef.child(album.thumbnail!!).downloadUrl.addOnSuccessListener {
                        thumbnailUri = it
                    }

                    if (thumbnailUri != Uri.EMPTY) {
                        AsyncImage(
                            model = ImageRequest.Builder(context)
                                .data(thumbnailUri)
                                .crossfade(true)
                                .build(),
                            contentDescription = "album_thumbnail",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .padding(16.dp)
                                .size(250.dp)
                                .clip(shape = RoundedCornerShape(16.dp))
                        )
                    }
                    Text(
                        text = album.name ?: "",
                        style = MaterialTheme.typography.headlineSmall,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Song list",
                            style = MaterialTheme.typography.titleLarge
                        )
                        IconButton(onClick = {
                            isFavorite = !isFavorite
                            favoriteViewModel.toggleFavorite("albums", album.id!!)
                        }) {
                            Icon(
                                painter = painterResource(id = if (isFavorite) R.drawable.ic_favorite_filled else R.drawable.ic_favorite_outlined),
                                contentDescription = "favorite_icon",
                                tint = if (isFavorite) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onBackground,
                            )
                        }
                    }
                    if (songListUiState is SongListUiState.Success) {
                        val songs = (songListUiState as SongListUiState.Success).songs
                        LazyColumn {
                            items(songs) { song ->
                                SongItem(
                                    song = song,
                                    onSongQueryChanged = { songQuery ->
                                        onSongQueryChanged(songQuery + "&album=" + album.id)
                                    },
                                    onAddToPlaylistClick = {
                                        addToPlaylistSongId = song.id
                                        isOpenAddToPlaylistDialog = true
                                    }
                                )
                            }
                        }
                    }
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
