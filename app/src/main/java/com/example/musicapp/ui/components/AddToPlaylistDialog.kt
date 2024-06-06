package com.example.musicapp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.musicapp.model.Playlist
import com.example.musicapp.ui.screens.PlaylistCreateDialog
import com.example.musicapp.ui.theme.MusicAppTheme

@Composable
fun AddToPlaylistDialog(
    list: List<Playlist>,
    onDismissRequest: () -> Unit,
    onChoosePlaylist: (String) -> Unit
) {
    Dialog(onDismissRequest = onDismissRequest) {
        Column(
            modifier = Modifier
                .clip(shape = RoundedCornerShape(24.dp))
                .background(color = MaterialTheme.colorScheme.background)
                .padding(16.dp)
        ) {
            Text(text = "Choose playlist", style = MaterialTheme.typography.headlineSmall)
            Divider(modifier = Modifier.padding(vertical = 8.dp))
            LazyColumn(modifier = Modifier.height(240.dp)) {
                items(list) { playlist ->
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                            .clickable { onChoosePlaylist(playlist.id!!) }) {
                        Text(text = playlist.name!!, style = MaterialTheme.typography.titleMedium)
                        Text(
                            text = "${playlist.songs?.size ?: 0} songs",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
        }
    }
}