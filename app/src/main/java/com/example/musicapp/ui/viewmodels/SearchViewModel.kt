package com.example.musicapp.ui.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.musicapp.model.Artist
import com.example.musicapp.model.Song
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject
import com.google.firebase.firestore.toObjects
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.text.Normalizer
import java.util.regex.Pattern

class SearchViewModel : ViewModel() {
    private val database = Firebase.firestore

    private var _songListUiState = MutableStateFlow<SongListUiState>(SongListUiState.Loading)

    private var _searchResults = MutableStateFlow<List<Song>>(emptyList())
    val searchResults: StateFlow<List<Song>> = _searchResults.asStateFlow()

    init {
        getSongs(database)
    }

    fun searchSongs(keyword: String) {
        if (_songListUiState.value is SongListUiState.Success) {
            if (keyword.isNotEmpty()) {
                val songs = (_songListUiState.value as SongListUiState.Success).songs
                _searchResults.value = songs.filter { song ->
                    normalizeString(song.name!!).contains(
                        normalizeString(keyword),
                        ignoreCase = true
                    )
                }
            } else {
                _searchResults.value = emptyList()
            }
        } else {
            _searchResults.value = emptyList()
        }
    }

    fun normalizeString(input: String): String {
        var normalized = Normalizer.normalize(input, Normalizer.Form.NFD)
        val pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+")
        normalized = pattern.matcher(normalized).replaceAll("")
        return normalized
    }

    private fun getSongs(database: FirebaseFirestore) {
        viewModelScope.launch {
            _songListUiState.value = SongListUiState.Loading
            try {
                val result = database.collection("songs")
                    .get()
                    .await()
                val songs = result.map { document ->
                    val data = document.data
                    val artistIds = data.getValue("artists") as List<*>
                    val artistNames = artistIds.map { artistId ->
                        val id = artistId as String
                        try {
                            val artist = database.collection("artists")
                                .document(id)
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
                        .whereArrayContains("songs", document.id)
                        .get()
                        .await()
                    Song(
                        id = document.id,
                        name = data.getValue("name") as String,
                        artists = artistNames,
                        audio = data.getValue("audio") as String,
                        thumbnail = data.getValue("thumbnail") as String,
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