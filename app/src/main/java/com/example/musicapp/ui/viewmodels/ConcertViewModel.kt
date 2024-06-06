package com.example.musicapp.ui.viewmodels

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.musicapp.model.Artist
import com.example.musicapp.model.Concert
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

sealed class ConcertUiState {
    data object Loading : ConcertUiState()
    data class Success(val concert: Concert) : ConcertUiState()
    data class Error(val message: String) : ConcertUiState()
}

class ConcertViewModel(savedStateHandle: SavedStateHandle) : ViewModel() {
    private val concertId: String = checkNotNull(savedStateHandle["concertId"])

    private var _concertUiState = MutableStateFlow<ConcertUiState>(ConcertUiState.Loading)
    val concertUiState: StateFlow<ConcertUiState> = _concertUiState.asStateFlow()

    init {
        initConcertDetailScreen()
    }

   private fun initConcertDetailScreen() {
        _concertUiState.value = ConcertUiState.Loading
        Firebase.firestore.collection("concerts").document(concertId)
            .get()
            .addOnSuccessListener { result ->
                val concert = result.toObject<Concert>()?.copy(
                    id = concertId
                )
                if (concert != null) {
                    viewModelScope.launch {
                        val artistNames = concert.artists?.map { artistId ->
                            try {
                                val artist = Firebase.firestore.collection("artists")
                                    .document(artistId)
                                    .get()
                                    .await()
                                    .toObject<Artist>()
                                artist?.name ?: ""
                            } catch (e: Exception) {
                                ""
                            }
                        }
                    _concertUiState.value = ConcertUiState.Success(concert.copy(artists = artistNames))
                    }
                } else {
                    _concertUiState.value = ConcertUiState.Error("Concert not found")
                }
            }
            .addOnFailureListener { exception ->
                _concertUiState.value =
                    ConcertUiState.Error(exception.message ?: "An error occurred")
                Log.e("HomeViewModel", exception.message ?: "An error occurred")
            }
    }
}