package com.example.musicapp.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.musicapp.R
import com.example.musicapp.ui.components.SongItem
import com.example.musicapp.ui.components.TopBar
import com.example.musicapp.ui.navigation.AlbumDetailDestination
import com.example.musicapp.ui.navigation.NavigationDestination


@Composable
fun AlbumDetailScreen(
    modifier: Modifier = Modifier,
    onNavigate: (NavigationDestination) -> Unit,
    onNavigateWithArgument: (NavigationDestination, String) -> Unit,
    onNavigateBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopBar(
                title = AlbumDetailDestination.title,
                canNavigateBack = true,
                onNavigate = onNavigate,
                onNavigateBack = onNavigateBack
            )
        },
        modifier = modifier
    ) { contentPadding ->
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(contentPadding)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = R.drawable.squirrel),
                    contentDescription = "album_thumbnail",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .padding(16.dp)
                        .size(250.dp)
                        .clip(shape = RoundedCornerShape(16.dp))
                )
                Text(
                    text = "Album name",
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Song list",
                        style = MaterialTheme.typography.titleLarge
                    )
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_favorite_outlined),
                            contentDescription = ""
                        )
                    }
                }
                LazyColumn {
//                    items(10) {
//                        SongItem(
//                            songName = "Song name",
//                            songArtist = "Artist name",
//                            onNavigateWithArgument = onNavigateWithArgument
//                        )
//                    }
                }
            }
        }
    }
}
