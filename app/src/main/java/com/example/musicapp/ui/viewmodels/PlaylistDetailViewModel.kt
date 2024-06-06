package com.example.musicapp.ui.viewmodels

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.musicapp.model.Album
import com.example.musicapp.model.Artist
import com.example.musicapp.model.Playlist
import com.example.musicapp.model.Song
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

sealed class PlaylistUiState {
    data object Loading : PlaylistUiState()
    data class Success(val playlist: Playlist) : PlaylistUiState()
    data class Error(val message: String) : PlaylistUiState()
}

class PlaylistDetailViewModel(savedStateHandle: SavedStateHandle) :ViewModel() {
    private val database = Firebase.firestore
    private val playlistId: String = checkNotNull(savedStateHandle["playlistId"])

    private var _playlistUiState = MutableStateFlow<PlaylistUiState>(PlaylistUiState.Loading)
    val playlistUiState: StateFlow<PlaylistUiState> = _playlistUiState.asStateFlow()

    private var _songListUiState = MutableStateFlow<SongListUiState>(SongListUiState.Loading)
    val songListUiState: StateFlow<SongListUiState> = _songListUiState.asStateFlow()

    var reloadScreen by mutableStateOf(false)
        private set

    fun getPlaylistDetailScreen() {
        viewModelScope.launch {
            _playlistUiState.value = PlaylistUiState.Loading
            try {
                val playlist = database.collection("playlists").document(playlistId).get().await().toObject<Playlist>()?.copy(
                    id = playlistId
                )
                if (playlist!=null) {
                    _playlistUiState.value = PlaylistUiState.Success(playlist)
                    getSongs(playlist)
                } else {
                    _playlistUiState.value = PlaylistUiState.Error("Album not found")
                }
            } catch (e: Exception) {
                _playlistUiState.value = PlaylistUiState.Error(e.message ?: "An unknown error occurred")
            }
        }
    }

    private fun getSongs(playlist: Playlist) {
        viewModelScope.launch {
            _songListUiState.value = SongListUiState.Loading
            try {
                val songs = playlist.songs!!.map { songId ->
                    val song = database.collection("songs").document(songId).get().await()
                        .toObject<Song>()
                    val artistNames = song?.artists?.map { artistId ->
                        try {
                            val artist =
                                database.collection("artists").document(artistId).get()
                                    .await().toObject<Artist>()
                            artist?.name ?: ""
                        } catch (e: Exception) {
                            ""
                        }
                    }
                    val isFavorite = database.collection("favorites")
                        .whereEqualTo("user_id", Firebase.auth.currentUser?.uid)
                        .whereArrayContains("songs", songId).get().await()
                    Song(
                        id = songId,
                        name = song?.name ?: "",
                        artists = artistNames ?: emptyList(),
                        audio = song?.audio ?: "",
                        thumbnail = song?.thumbnail ?: "",
                        isFavorite = !isFavorite.isEmpty
                    )
                }
                _songListUiState.value = SongListUiState.Success(songs)
            } catch (e: Exception) {
                _songListUiState.value = SongListUiState.Error(e.message ?: "An error occurred")
            }
        }
    }

    fun removeFromPlaylist(context: Context, playlistId: String, songId: String){
        database.collection("playlists").document(playlistId).update("songs", FieldValue.arrayRemove(songId)).addOnSuccessListener {
            Toast.makeText(context, "Song removed from playlist", Toast.LENGTH_SHORT).show()
            reloadScreen = !reloadScreen
        }.addOnFailureListener {
            Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
        }
    }
}
