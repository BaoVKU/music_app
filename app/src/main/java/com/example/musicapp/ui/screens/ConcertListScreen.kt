package com.example.musicapp.ui.screens

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.musicapp.ui.components.ConcertItem
import com.example.musicapp.ui.components.TopBar
import com.example.musicapp.ui.navigation.ConcertListDestination
import com.example.musicapp.ui.navigation.NavigationDestination

@Composable
fun ConcertListScreen(
    modifier: Modifier = Modifier,
    onNavigate: (NavigationDestination) -> Unit,
    onNavigateWithArgument: (NavigationDestination, String) -> Unit,
    onNavigateBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopBar(
                title = ConcertListDestination.title,
                canNavigateBack = true,
                hasActions = false,
                onNavigateBack = onNavigateBack,
                onNavigate = onNavigate
            )
        }, modifier = modifier
    ) { contentPadding ->
        LazyColumn(
            contentPadding = contentPadding
        ) {
            items(3) {
                ConcertItem(onNavigateWithArgument = onNavigateWithArgument)
            }
        }
    }
}