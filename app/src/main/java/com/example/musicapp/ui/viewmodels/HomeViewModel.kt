package com.example.musicapp.ui.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.musicapp.model.Album
import com.example.musicapp.model.Concert
import com.example.musicapp.model.Song
import com.google.firebase.Firebase
import com.google.firebase.Timestamp
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject
import com.google.firebase.firestore.toObjects
import com.google.firebase.storage.storage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.text.SimpleDateFormat
import java.util.Locale

sealed class AlbumListUiState {
    data object Loading : AlbumListUiState()
    data class Success(val albums: List<Album>) : AlbumListUiState()
    data class Error(val message: String) : AlbumListUiState()
}

sealed class SongListUiState {
    data object Loading : SongListUiState()
    data class Success(val songs: List<Song>) : SongListUiState()
    data class Error(val message: String) : SongListUiState()
}

sealed class ConcertListUiState {
    data object Loading : ConcertListUiState()
    data class Success(val concerts: List<Concert>) : ConcertListUiState()
    data class Error(val message: String) : ConcertListUiState()
}

class HomeViewModel : ViewModel() {
    private var _songListUiState = MutableStateFlow<SongListUiState>(SongListUiState.Loading)
    val songListUiState: StateFlow<SongListUiState> = _songListUiState.asStateFlow()

    private var _albumListUiState = MutableStateFlow<AlbumListUiState>(AlbumListUiState.Loading)
    val albumListUiState: StateFlow<AlbumListUiState> = _albumListUiState.asStateFlow()

    private var _concertListUiState = MutableStateFlow<ConcertListUiState>(ConcertListUiState.Loading)
    val concertListUiState: StateFlow<ConcertListUiState> = _concertListUiState.asStateFlow()

    init {
        val database = Firebase.firestore
        getSongs(database)
        getAlbums(database)
        getConcerts(database)
    }

    private fun getSongs(database: FirebaseFirestore){
        _songListUiState.value = SongListUiState.Loading
        database.collection("songs")
            .limit(3)
            .get()
            .addOnSuccessListener { result ->
                val songs = result.map { document ->
                    document.toObject<Song>().copy(
                        id = document.id
                    )
                }
                _songListUiState.value = SongListUiState.Success(songs)
            }
            .addOnFailureListener { exception ->
                _songListUiState.value = SongListUiState.Error(exception.message ?: "An error occurred")
            }
    }

    private fun getAlbums(database: FirebaseFirestore){
        _albumListUiState.value = AlbumListUiState.Loading
        database.collection("albums")
            .get()
            .addOnSuccessListener { result ->
                val albums = result.map { document ->
                    document.toObject<Album>().copy(
                        id = document.id
                    )
                }
                _albumListUiState.value = AlbumListUiState.Success(albums)
            }
            .addOnFailureListener { exception ->
                _albumListUiState.value = AlbumListUiState.Error(exception.message ?: "An error occurred")
            }
    }

    private fun getConcerts(database: FirebaseFirestore){
        _concertListUiState.value = ConcertListUiState.Loading
        database.collection("concerts")
            .get()
            .addOnSuccessListener { result ->
                val concerts = result.map { document ->
                    document.toObject<Concert>().copy(
                        id = document.id
                    )
                }
                _concertListUiState.value = ConcertListUiState.Success(concerts)
            }
            .addOnFailureListener { exception ->
                _concertListUiState.value = ConcertListUiState.Error(exception.message ?: "An error occurred")
                Log.e("HomeViewModel", exception.message ?: "An error occurred")
            }
    }
}