package com.example.musicapp.ui.components

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Environment
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.musicapp.R
import com.example.musicapp.model.Song
import com.example.musicapp.ui.navigation.NavigationDestination
import com.example.musicapp.ui.viewmodels.FavoriteViewModel
import com.google.firebase.Firebase
import com.google.firebase.storage.storage

@Composable
fun SongItem(
    modifier: Modifier = Modifier,
    song: Song,
    isInPlaylistScreen: Boolean = false,
    onSongQueryChanged: (String) -> Unit,
    onFavoriteClick: () -> Unit = {},
    onAddToPlaylistClick: () -> Unit = {},
    onRemoveFromPlaylistClick: () -> Unit = {},
    favoriteViewModel: FavoriteViewModel = viewModel()
) {
    val context = LocalContext.current

    var isMenuExpanded by remember { mutableStateOf(false) }

    var thumbnailUri by remember {
        mutableStateOf<Uri>(Uri.EMPTY)
    }

    var isFavorite by remember {
        mutableStateOf(song.isFavorite)
    }

    Firebase.storage.reference.child(song.thumbnail!!).downloadUrl.addOnSuccessListener {
        thumbnailUri = it
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .height(64.dp)
            .padding(8.dp)
            .clickable { onSongQueryChanged("song=" + song.id) }
    ) {
        if (thumbnailUri != Uri.EMPTY) {
            Box(modifier = Modifier.size(48.dp)) {
                AsyncImage(
                    model = ImageRequest.Builder(context).data(thumbnailUri).crossfade(true)
                        .build(),
                    contentDescription = "song_thumbnail",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(shape = RoundedCornerShape(8.dp))
                )
            }
        }
        Column(
            modifier = Modifier
                .padding(start = 8.dp)
                .weight(1f)
        ) {
            Text(
                text = song.name ?: "",
                style = MaterialTheme.typography.titleMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = song.artists?.joinToString(", ") ?: "",
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

        }
        IconButton(onClick = {
            favoriteViewModel.toggleFavorite("songs",song.id)
            isFavorite = !isFavorite
        }) {
            Icon(
                painter = painterResource(id = if (isFavorite) R.drawable.ic_favorite_filled else R.drawable.ic_favorite_outlined),
                contentDescription = "favorite_icon",
                tint = if (isFavorite) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onBackground
            )
        }
        Column {
            IconButton(onClick = { isMenuExpanded = true }) {
                Icon(imageVector = Icons.Default.MoreVert, contentDescription = "more_icon")
            }
            DropdownMenu(expanded = isMenuExpanded, onDismissRequest = { isMenuExpanded = false }) {
                if (!isInPlaylistScreen) {
                    DropdownMenuItem(text = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_playlist_add),
                                contentDescription = "add_to_playlist_icon"
                            )
                            Text(
                                text = "Add to playlist",
                                modifier = Modifier.padding(start = 8.dp)
                            )
                        }
                    }, onClick = {
                        onAddToPlaylistClick()
                        isMenuExpanded = false
                    })
                }
                if (isInPlaylistScreen) {
                    DropdownMenuItem(text = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_playlist_remove),
                                contentDescription = "remove_from_playlist_icon"
                            )
                            Text(
                                text = "Remove from playlist",
                                modifier = Modifier.padding(start = 8.dp)
                            )
                        }
                    }, onClick = {
                        onRemoveFromPlaylistClick()
                        isMenuExpanded = false
                    })
                }
                DropdownMenuItem(text = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_download),
                            contentDescription = "download_icon"
                        )
                        Text(text = "Download", modifier = Modifier.padding(start = 8.dp))
                    }
                }, onClick = {
                    isMenuExpanded = false
                    downloadSong(context, song)
                })
            }
        }
    }
}

private fun downloadSong(context: Context, song: Song){
    Firebase.storage.reference.child(song.audio!!).downloadUrl.addOnSuccessListener { uri ->
        context.getSystemService(DownloadManager::class.java).enqueue(
            DownloadManager.Request(uri)
                .setMimeType("audio/mpeg")
                .setTitle(song.name + ".mp3")
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                .setDestinationInExternalPublicDir(
                    Environment.DIRECTORY_MUSIC,
                    song.name + ".mp3"
                )
        )
    }
}