package com.example.musicapp.ui.viewmodels

import android.net.Uri
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.musicapp.model.Artist
import com.example.musicapp.model.Song
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject
import com.google.firebase.storage.storage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

sealed class SongUiState {
    data object Loading : SongUiState()
    data class Success(val song: Song) : SongUiState()
    data class Error(val message: String) : SongUiState()
}

sealed class AudioUrisUiState {
    data object Loading : AudioUrisUiState()
    data class Success(val uris: List<Pair<String, Uri>>) : AudioUrisUiState()
    data class Error(val message: String) : AudioUrisUiState()
}

class PlayerViewModel : ViewModel() {

    private var _songUiState = MutableStateFlow<SongUiState>(SongUiState.Loading)
    val songUiState: StateFlow<SongUiState> = _songUiState.asStateFlow()

    private var _songListUiState = MutableStateFlow<SongListUiState>(SongListUiState.Loading)
    val songListUiState: StateFlow<SongListUiState> = _songListUiState.asStateFlow()

    private var _audioUrisUiState = MutableStateFlow<AudioUrisUiState>(AudioUrisUiState.Loading)
    val audioUrisUiState: StateFlow<AudioUrisUiState> = _audioUrisUiState.asStateFlow()

    fun initPlayer(songQuery: String) {
        if (songQuery.isNotEmpty()) {
            val database = Firebase.firestore
            val params = parseRoute(songQuery)
            val songId = params["song"]
            val albumId = params["album"]
            val playlistId = params["playlist"]
            if (songId != null) {
                if (albumId != null) {
                    getList(database, "albums", albumId)
                } else if (playlistId != null) {
                    getList(database, "playlists", playlistId)
                }
                if (_songListUiState.value is SongListUiState.Success) {
                    val songs = (_songListUiState.value as SongListUiState.Success).songs
                    val song = songs.find { it.id == songId }
                    if (song != null) {
                        _songUiState.value = SongUiState.Success(song)
                    }
                } else {
                    getSong(database, songId)
                }
            }
        }
    }

    private fun getSong(database: FirebaseFirestore, songId: String) {
        viewModelScope.launch {
            _songUiState.value = SongUiState.Loading
            try {
                val song =
                    database.collection("songs").document(songId).get().await().toObject<Song>()
                if (song != null) {
                    val artistNames = song.artists?.map { artistId ->
                        try {
                            val artist = database.collection("artists")
                                .document(artistId)
                                .get()
                                .await()
                                .toObject<Artist>()
                            artist?.name ?: ""
                        } catch (e: Exception) {
                            ""
                        }
                    }
                    val isFavorite = database.collection("favorites")
                        .whereEqualTo("user_id", Firebase.auth.currentUser?.uid)
                        .whereArrayContains("songs", songId)
                        .get()
                        .await()
                    _songUiState.value = SongUiState.Success(
                        song.copy(
                            id = songId,
                            artists = artistNames,
                            isFavorite = !isFavorite.isEmpty
                        )
                    )
                } else {
                    _songUiState.value = SongUiState.Error("Song not found")
                }
            } catch (e: Exception) {
                _songUiState.value = SongUiState.Error(e.message ?: "An error occurred")
            }
        }

    }

    private fun getList(database: FirebaseFirestore, listType: String, listId: String) {
        viewModelScope.launch {
            _songListUiState.value = SongListUiState.Loading
            try {
                val result = database.collection(listType).document(listId).get().await()
                val songIds = result.data?.get("songs") as List<*>
                val songs = songIds.mapNotNull { id ->
                    val songId = id as String
                    val song =
                        try {
                            database.collection("songs").document(songId).get().await()
                                .toObject<Song>()
                        } catch (e: Exception) {
                            null
                        }

                    val artistNames = song?.artists?.map { artistId ->
                        try {
                            val artist = database.collection("artists")
                                .document(artistId)
                                .get()
                                .await()
                                .toObject<Artist>()
                            artist?.name ?: ""
                        } catch (e: Exception) {
                            ""
                        }
                    }
                    val isFavorite = database.collection("favorites")
                        .whereEqualTo("user_id", Firebase.auth.currentUser?.uid)
                        .whereArrayContains("songs", songId)
                        .get()
                        .await()
                    song?.copy(
                        id = songId,
                        artists = artistNames,
                        isFavorite = !isFavorite.isEmpty
                    )
                }
                _songListUiState.value = SongListUiState.Success(songs)
                val audioUris = songs.map { song ->
                    song.id to Firebase.storage.reference.child(song.audio!!).downloadUrl.await()
                }
                _audioUrisUiState.value = AudioUrisUiState.Success(audioUris)
            } catch (e: Exception) {
                _songListUiState.value = SongListUiState.Error(e.message ?: "An error occurred")
            }
        }
    }

    fun setSongUiStateToLoading() {
        _songUiState.value = SongUiState.Loading
    }

    fun setSongListUiStateToLoading() {
        _songListUiState.value = SongListUiState.Loading
    }

    fun setAudioUrisUiStateToLoading() {
        _audioUrisUiState.value = AudioUrisUiState.Loading
    }

    private fun parseRoute(route: String): Map<String, String> {
        val parts = route.split("&")
        val result = mutableMapOf<String, String>()

        for (part in parts) {
            val keyValue = part.split("=")
            if (keyValue.size == 2) {
                result[keyValue[0]] = keyValue[1]
            }
        }
        return result
    }
}