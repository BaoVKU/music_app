package com.example.musicapp.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.musicapp.model.Album
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

class AlbumListViewModel : ViewModel() {
    private var _albumListUiState = MutableStateFlow<AlbumListUiState>(AlbumListUiState.Loading)
    val albumListUiState: StateFlow<AlbumListUiState> = _albumListUiState.asStateFlow()

    init {
        val database = Firebase.firestore
        getAlbums(database)
    }

    private fun getAlbums(database: FirebaseFirestore) {
        _albumListUiState.value = AlbumListUiState.Loading
        viewModelScope.launch {
            try {
                val result = database.collection("albums")
                    .get()
                    .await()
                val albums = result.map { document ->
                    val data = document.data
                    val isFavorite = database.collection("favorites")
                        .whereEqualTo("user_id", Firebase.auth.currentUser?.uid)
                        .whereArrayContains("albums", document.id)
                        .get()
                        .await()
                    Album(
                        id = document.id,
                        name = data.getValue("name") as String,
                        thumbnail = data.getValue("thumbnail") as String?,
                        isFavorite = !isFavorite.isEmpty
                    )
                }
                _albumListUiState.value = AlbumListUiState.Success(albums)
            } catch (exception: Exception) {
                _albumListUiState.value =
                    AlbumListUiState.Error(exception.message ?: "An error occurred")
            }
        }
    }
}