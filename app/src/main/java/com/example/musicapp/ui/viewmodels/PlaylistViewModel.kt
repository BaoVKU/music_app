package com.example.musicapp.ui.viewmodels

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.musicapp.model.Playlist
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject
import com.google.firebase.firestore.toObjects
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow


sealed class PlaylistListUiState {
    data object Loading : PlaylistListUiState()
    data class Success(val playlists: List<Playlist>) : PlaylistListUiState()
    data class Error(val message: String) : PlaylistListUiState()
}

class PlaylistViewModel :ViewModel(){
    private val database = Firebase.firestore
    private val _playlistListUiState = MutableStateFlow<PlaylistListUiState>(PlaylistListUiState.Loading)
    val playlistListUiState:StateFlow<PlaylistListUiState> = _playlistListUiState.asStateFlow()

    var reloadScreen by mutableStateOf(false)
        private set
    init {
        initPlaylistScreen()
    }

    fun initPlaylistScreen(){
        _playlistListUiState.value = PlaylistListUiState.Loading
        database.collection("playlists").whereEqualTo("user_id", Firebase.auth.currentUser?.uid).get()
            .addOnSuccessListener { result ->
                val playlists = result.map { document ->
                    document.toObject<Playlist>().copy(
                        id = document.id
                    )

                }
                _playlistListUiState.value = PlaylistListUiState.Success(playlists)
            }
            .addOnFailureListener { exception ->
                _playlistListUiState.value = PlaylistListUiState.Error(exception.message!!)
            }
    }

    fun createPlaylist(context:Context, name:String){
        if(name.isNotEmpty()){
            val playlist = hashMapOf(
                "name" to name,
                "user_id" to Firebase.auth.currentUser?.uid
            )
            database.collection("playlists").add(playlist).addOnSuccessListener {
                Toast.makeText(context, "Playlist created", Toast.LENGTH_SHORT).show()
                reloadScreen=!reloadScreen
            }.addOnFailureListener {
                Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun deletePlaylist(context:Context, id: String){
        database.collection("playlists").document(id).delete().addOnSuccessListener {
            Toast.makeText(context, "Playlist deleted", Toast.LENGTH_SHORT).show()
            reloadScreen=!reloadScreen
        }.addOnFailureListener {
            Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
        }
    }

    fun addToPlaylist(context:Context, playlistId: String, songId: String){
        database.collection("playlists").document(playlistId).update("songs", FieldValue.arrayUnion(songId)).addOnSuccessListener {
            Toast.makeText(context, "Song added to playlist", Toast.LENGTH_SHORT).show()
        }.addOnFailureListener {
            Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
        }
    }
}