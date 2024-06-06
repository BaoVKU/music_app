package com.example.musicapp.ui.screens

import android.content.Context
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.musicapp.model.Album
import com.example.musicapp.model.Concert
import com.example.musicapp.model.Song
import com.example.musicapp.ui.components.BottomNavigationBar
import com.example.musicapp.ui.components.TopBar
import com.example.musicapp.ui.navigation.AlbumDetailDestination
import com.example.musicapp.ui.navigation.AlbumListDestination
import com.example.musicapp.ui.navigation.ConcertDetailDestination
import com.example.musicapp.ui.navigation.ConcertListDestination
import com.example.musicapp.ui.navigation.HomeDestination
import com.example.musicapp.ui.navigation.SongListDestination
import com.example.musicapp.ui.navigation.NavigationDestination
import com.example.musicapp.ui.viewmodels.AlbumListUiState
import com.example.musicapp.ui.viewmodels.ConcertListUiState
import com.example.musicapp.ui.viewmodels.HomeViewModel
import com.example.musicapp.ui.viewmodels.SongListUiState
import com.google.firebase.Firebase
import com.google.firebase.Timestamp
import com.google.firebase.storage.storage
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    onNavigate: (NavigationDestination) -> Unit,
    onNavigateWithArgument: (NavigationDestination, String) -> Unit,
    onSongQueryChanged: (String) -> Unit,
    homeViewModel: HomeViewModel = viewModel()
) {
    val context = LocalContext.current
    val albumListUiState by homeViewModel.albumListUiState.collectAsState()
    val songListUiState by homeViewModel.songListUiState.collectAsState()
    val concertListUiState by homeViewModel.concertListUiState.collectAsState()
    Scaffold(
        topBar = {
            TopBar(title = HomeDestination.title, onNavigate = onNavigate)
        },
        bottomBar = {
            BottomNavigationBar(currentDestination = HomeDestination, onNavigate = onNavigate)
        }, modifier = modifier
    ) { contentPadding ->
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(32.dp),
            contentPadding = contentPadding,
            modifier = Modifier.padding(16.dp)
        ) {
            item {
                if (songListUiState is SongListUiState.Success) {
                    val songs = (songListUiState as SongListUiState.Success).songs
                    HomeCategory(
                        headerTitle = SongListDestination.title,
                        onShowAll = { onNavigate(SongListDestination) })
                    {
                        LazyRow(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                            items(songs) { song ->
                                HomeCategoryImageItem(
                                    item = song,
                                    context = context,
                                    modifier = Modifier.clickable {
                                        onSongQueryChanged("song="+song.id)
                                    })
                            }
                        }
                    }
                }
            }
            item {
                if (albumListUiState is AlbumListUiState.Success) {
                    val albums = (albumListUiState as AlbumListUiState.Success).albums
                    HomeCategory(
                        headerTitle = AlbumListDestination.title,
                        onShowAll = { onNavigate(AlbumListDestination) })
                    {
                        LazyRow(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                            items(albums) { album ->
                                HomeCategoryImageItem(
                                    item = album,
                                    context = context,
                                    modifier = Modifier.clickable {
                                        onNavigateWithArgument(
                                            AlbumDetailDestination,
                                            album.id!!
                                        )
                                    })
                            }
                        }
                    }
                }
            }
            item {
                if (concertListUiState is ConcertListUiState.Success) {
                    val concerts = (concertListUiState as ConcertListUiState.Success).concerts
                    HomeCategory(
                        headerTitle = ConcertListDestination.title,
                        onShowAll = { onNavigate(ConcertListDestination) })
                    {
                        LazyRow(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                            items(concerts) { concert ->
                                HomeCategoryConcertItem(
                                    item = concert,
                                    modifier = Modifier.clickable {
                                        onNavigateWithArgument(
                                            ConcertDetailDestination,
                                            concert.id!!
                                        )
                                    })
                            }
                        }
                    }
                }
            }
            item {
                Spacer(modifier = Modifier.height(48.dp)) //space for minimized player
            }
        }
    }
}

@Composable
fun HomeCategory(
    headerTitle: String,
    onShowAll: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Column(modifier = modifier) {
        HomeCategoryHeader(title = headerTitle, onClick = onShowAll)
        content()
    }
}

@Composable
fun HomeCategoryHeader(title: String, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier.fillMaxWidth()
    ) {
        Text(text = title, style = MaterialTheme.typography.titleLarge)
        TextButton(onClick = onClick) {
            Text(text = "Show all")
        }
    }
}

@Composable
fun HomeCategoryItem(name: String, modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    Column(modifier = modifier) {
        Box(
            modifier = Modifier
                .size(124.dp)
                .clip(RoundedCornerShape(16.dp))
        ) {
            content()
        }
        Surface(modifier = Modifier.width(124.dp)) {
            Text(
                text = name,
                style = MaterialTheme.typography.titleMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(start = 8.dp)
            )
        }
    }
}

@Composable
fun HomeCategoryImageItem(
    item: Any,
    context: Context,
    modifier: Modifier = Modifier
) {
    var imageUri by remember {
        mutableStateOf<Uri>(Uri.EMPTY)
    }
    val thumbnail = when (item) {
        is Song -> item.thumbnail
        is Album -> item.thumbnail
        else -> null
    }
    val name = when (item) {
        is Song -> item.name
        is Album -> item.name
        else -> ""
    }
    if (thumbnail != null) {
        Firebase.storage.reference.child(thumbnail).downloadUrl.addOnSuccessListener {
            imageUri = it
        }
    }
    HomeCategoryItem(name = name ?: "", modifier = modifier) {
        if (imageUri != Uri.EMPTY) {
            AsyncImage(
                model = ImageRequest.Builder(context = context).data(imageUri).crossfade(true)
                    .build(),
                contentDescription = thumbnail ?: "",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

@Composable
fun HomeCategoryConcertItem(item: Concert, modifier: Modifier = Modifier) {
    if (item.date != null) {
        val dateArray = convertTimestampToArray(item.date)
        HomeCategoryItem(name = item.name ?: "", modifier = modifier) {
            Column {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(color = MaterialTheme.colorScheme.secondary)
                        .padding(vertical = 8.dp)
                ) {
                    Text(
                        text = dateArray[0],
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onSecondary
                    )
                }
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxSize()
                        .background(color = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Text(
                        text = dateArray[1],
                        style = MaterialTheme.typography.displayMedium,
                        fontWeight = FontWeight(700),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }
    }
}

fun convertTimestampToArray(timestamp: Timestamp): Array<String> {
    val date = timestamp.toDate() // Convert the Timestamp to a Date
    val monthFormat = SimpleDateFormat("MMMM", Locale.getDefault())
    val dayFormat = SimpleDateFormat("dd", Locale.getDefault())

    val month = monthFormat.format(date).toUpperCase(Locale.getDefault())
    val day = dayFormat.format(date)

    return arrayOf(month, day)
}