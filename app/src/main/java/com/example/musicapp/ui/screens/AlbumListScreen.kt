package com.example.musicapp.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.musicapp.ui.components.AlbumItem
import com.example.musicapp.ui.components.TopBar
import com.example.musicapp.ui.navigation.AlbumListDestination
import com.example.musicapp.ui.navigation.NavigationDestination

@Composable
fun AlbumListScreen(
    modifier: Modifier = Modifier,
    onNavigate: (NavigationDestination) -> Unit,
    onNavigateWithArgument: (NavigationDestination, String) -> Unit,
    onNavigateBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopBar(
                title = AlbumListDestination.title,
                canNavigateBack = true,
                onNavigate = onNavigate,
                onNavigateBack = onNavigateBack
            )

        },
        modifier = modifier
    ) { contentPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding)
                .padding(8.dp)
        ) {
            Text(
                text = "Newest",
                style = MaterialTheme.typography.titleLarge
            )
            LazyColumn {
                items(10) {
                    AlbumItem(onNavigateWithArgument = onNavigateWithArgument)
                }
            }
        }
    }
}