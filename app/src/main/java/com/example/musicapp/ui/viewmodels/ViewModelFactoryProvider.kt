package com.example.musicapp.ui.viewmodels

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory

object ViewModelFactoryProvider {
    val Factory: ViewModelProvider.Factory = viewModelFactory {
        initializer {
            AlbumDetailViewModel(
                this.createSavedStateHandle()
            )
        }
        initializer {
            PlaylistDetailViewModel(
                this.createSavedStateHandle()
            )
        }
    }
}