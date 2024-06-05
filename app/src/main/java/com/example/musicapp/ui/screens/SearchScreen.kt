package com.example.musicapp.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.musicapp.ui.components.SongItem
import com.example.musicapp.ui.navigation.NavigationDestination

@Composable
fun SearchScreen(
    modifier: Modifier = Modifier,
    onNavigateWithArgument: (NavigationDestination, String) -> Unit,
    onNavigateBack: () -> Unit
) {
    var searchValue by remember { mutableStateOf("") }
    Column(
        verticalArrangement = Arrangement.Top,
        modifier = modifier
            .padding(8.dp)
            .fillMaxSize()
    ) {
        TextField(
            value = searchValue, onValueChange = {
                searchValue = it
            },
            placeholder = { Text(text = "Search...") },
            leadingIcon = {
                IconButton(onClick = onNavigateBack) {
                    Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "back_icon")
                }
            },
            trailingIcon = {
                IconButton(onClick = {
                    searchValue = ""
                }) {
                    Icon(imageVector = Icons.Default.Clear, contentDescription = "clear_icon")
                }
            }, modifier = Modifier.fillMaxWidth()
        )
        LazyColumn(modifier = Modifier.weight(1f)) {
//            items(20) {
//                SongItem(
//                    songName = "Song name",
//                    songArtist = "Artist name",
//                    onNavigateWithArgument = onNavigateWithArgument
//                )
//            }
        }
    }
}