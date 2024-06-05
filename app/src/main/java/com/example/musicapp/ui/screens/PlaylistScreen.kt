package com.example.musicapp.ui.screens


import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.musicapp.ui.components.BottomNavigationBar
import com.example.musicapp.ui.components.PlaylistItem
import com.example.musicapp.ui.components.TopBar
import com.example.musicapp.ui.navigation.NavigationDestination
import com.example.musicapp.ui.navigation.PlaylistDestination


@Composable
fun PlayListScreen(
    modifier: Modifier = Modifier,
    onNavigate: (NavigationDestination) -> Unit,
    onNavigateWithArgument: (NavigationDestination, String) -> Unit
) {
    Scaffold(
        topBar = {
            TopBar(title = PlaylistDestination.title, onNavigate = onNavigate)
        },
        bottomBar = {
            BottomNavigationBar(
                currentDestination = PlaylistDestination,
                onNavigate = onNavigate
            )
        },
        modifier = modifier
    ) { contentPadding ->
        LazyColumn(contentPadding = contentPadding) {
            items(10) {
                PlaylistItem(onNavigateWithArgument = onNavigateWithArgument)
            }
        }
    }
}