package com.example.musicapp.ui.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.musicapp.model.Album
import com.example.musicapp.model.Artist
import com.example.musicapp.model.Favorite
import com.example.musicapp.model.Song
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject
import com.google.firebase.firestore.toObjects
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class FavoriteViewModel : ViewModel() {
    private val database = Firebase.firestore

    private var _songListUiState = MutableStateFlow<SongListUiState>(SongListUiState.Loading)
    val songListUiState: StateFlow<SongListUiState> = _songListUiState.asStateFlow()

    private var _albumListUiState = MutableStateFlow<AlbumListUiState>(AlbumListUiState.Loading)
    val albumListUiState: StateFlow<AlbumListUiState> = _albumListUiState.asStateFlow()

    var reloadScreen by mutableStateOf(false)
        private set

    fun initFavoriteScreen() {
        viewModelScope.launch {
            val result = database.collection("favorites")
                .whereEqualTo("user_id", Firebase.auth.currentUser?.uid)
                .get()
                .await()
                .toObjects<Favorite>()
            if (result.isNotEmpty()) {
                val favorite = result[0]
                _songListUiState.value = SongListUiState.Loading
                val songs = favorite.songs?.map { songId ->
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
                _albumListUiState.value = AlbumListUiState.Loading
                val albums = favorite.albums?.map { albumId ->
                    val album = database.collection("albums").document(albumId).get().await()
                        .toObject<Album>()
                    val isFavorite = database.collection("favorites")
                        .whereEqualTo("user_id", Firebase.auth.currentUser?.uid)
                        .whereArrayContains("albums", albumId)
                        .get()
                        .await()
                    Album(
                        id = albumId,
                        name = album?.name ?: "",
                        thumbnail = album?.thumbnail ?: "",
                        isFavorite = !isFavorite.isEmpty
                    )
                }
                _songListUiState.value = SongListUiState.Success(songs ?: emptyList())
                _albumListUiState.value = AlbumListUiState.Success(albums ?: emptyList())
            } else {
                _songListUiState.value = SongListUiState.Success(emptyList())
                _albumListUiState.value = AlbumListUiState.Success(emptyList())
            }
        }
    }

    fun toggleFavorite(field: String, id: String) {
        viewModelScope.launch {
            val result = database.collection("favorites")
                .whereEqualTo("user_id", Firebase.auth.currentUser?.uid)
                .get()
                .await()
            if (result.documents.isNotEmpty()) {
                val favorite = result.map { document ->
                    document.toObject<Favorite>().copy(
                        id = document.id
                    )
                }[0]
                when (field) {
                    "albums" -> {
                        if (favorite.albums?.contains(id) == false) {
                            database.collection("favorites").document(favorite.id!!)
                                .update("albums", FieldValue.arrayUnion(id)).await()
                        } else {
                            database.collection("favorites").document(favorite.id!!)
                                .update("albums", FieldValue.arrayRemove(id)).await()
                        }
                    }

                    "songs" -> {
                        if (favorite.songs?.contains(id) == false) {
                            database.collection("favorites").document(favorite.id!!)
                                .update("songs", FieldValue.arrayUnion(id)).await()
                        } else {
                            database.collection("favorites").document(favorite.id!!)
                                .update("songs", FieldValue.arrayRemove(id)).await()
                        }
                    }
                }
            }
        }
    }
}