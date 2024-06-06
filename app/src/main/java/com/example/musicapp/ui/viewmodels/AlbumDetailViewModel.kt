package com.example.musicapp.ui.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.musicapp.model.Album
import com.example.musicapp.model.Artist
import com.example.musicapp.model.Song
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

sealed class AlbumUiState {
    data object Loading : AlbumUiState()
    data class Success(val album: Album) : AlbumUiState()
    data class Error(val message: String) : AlbumUiState()
}

class AlbumDetailViewModel(savedStateHandle: SavedStateHandle) : ViewModel() {
    private val albumId: String = checkNotNull(savedStateHandle["albumId"])

    private var _albumUiState = MutableStateFlow<AlbumUiState>(AlbumUiState.Loading)
    val albumUiState: StateFlow<AlbumUiState> = _albumUiState.asStateFlow()

    private var _songListUiState = MutableStateFlow<SongListUiState>(SongListUiState.Loading)
    val songListUiState: StateFlow<SongListUiState> = _songListUiState.asStateFlow()

    init {
        val database = Firebase.firestore
        getAlbum(database)
    }

    private fun getAlbum(database: FirebaseFirestore) {
        viewModelScope.launch {
            _albumUiState.value = AlbumUiState.Loading
            try {
                val album = database.collection("albums").document(albumId).get().await().toObject<Album>()?.copy(
                    id = albumId
                )
                val isFavorite = database.collection("favorites")
                    .whereEqualTo("user_id", Firebase.auth.currentUser?.uid)
                    .whereArrayContains("albums", albumId)
                    .get()
                    .await()
                if (album!=null) {
                    _albumUiState.value = AlbumUiState.Success(album.copy(isFavorite = !isFavorite.isEmpty))
                    getSongs(database, album)
                } else {
                    _albumUiState.value = AlbumUiState.Error("Album not found")
                }
            } catch (e: Exception) {
                _albumUiState.value = AlbumUiState.Error(e.message ?: "An unknown error occurred")
            }
        }
    }

    private fun getSongs(database: FirebaseFirestore, album: Album) {
        viewModelScope.launch {
            _songListUiState.value = SongListUiState.Loading
            try {
                val songs = album.songs!!.map { songId ->
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
}