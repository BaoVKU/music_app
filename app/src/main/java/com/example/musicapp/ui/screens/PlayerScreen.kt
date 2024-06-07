package com.example.musicapp.ui.screens

import android.annotation.SuppressLint
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.musicapp.R
import com.example.musicapp.model.Song
import com.example.musicapp.ui.components.MinimizedPlayer
import com.example.musicapp.ui.viewmodels.AudioUrisUiState
import com.example.musicapp.ui.viewmodels.FavoriteViewModel
import com.example.musicapp.ui.viewmodels.PlayerViewModel
import com.example.musicapp.ui.viewmodels.SongListUiState
import com.example.musicapp.ui.viewmodels.SongUiState
import com.google.firebase.Firebase
import com.google.firebase.storage.storage
import kotlinx.coroutines.delay

@kotlin.OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlayerScreen(
    modifier: Modifier = Modifier,
    songQuery: String,
    playerViewModel: PlayerViewModel = viewModel(),
    favoriteViewModel: FavoriteViewModel = viewModel()
) {

    LaunchedEffect(songQuery) {
        playerViewModel.initPlayer(songQuery)
    }

    val context = LocalContext.current
    val storageRef = Firebase.storage.reference
    val songUiState by playerViewModel.songUiState.collectAsState()
    val songListUiState by playerViewModel.songListUiState.collectAsState()
    val audioUrisUiState by playerViewModel.audioUrisUiState.collectAsState()

    var isMinimized by remember { mutableStateOf(false) }

    var currentSong by remember {
        mutableStateOf(Song())
    }

    var isFavorite by remember {
        mutableStateOf(currentSong.isFavorite)
    }

    var playlist by remember(songQuery) {
        mutableStateOf(listOf<Song>())
    }

    var thumbnailUri by remember {
        mutableStateOf<Uri>(Uri.EMPTY)
    }

    var initialAudioUri by remember {
        mutableStateOf<Uri>(Uri.EMPTY)
    }

    var audioUris by remember {
        mutableStateOf(listOf<Pair<String, Uri>>())
    }

    if (songListUiState is SongListUiState.Success) {
        playlist = (songListUiState as SongListUiState.Success).songs
        playerViewModel.setSongListUiStateToLoading()
    }

    if (audioUrisUiState is AudioUrisUiState.Success) {
        audioUris = (audioUrisUiState as AudioUrisUiState.Success).uris
    }

    if (songUiState is SongUiState.Success) {
        currentSong = (songUiState as SongUiState.Success).song
        if (currentSong.thumbnail != null) {
            storageRef.child(currentSong.thumbnail!!).downloadUrl.addOnSuccessListener {
                thumbnailUri = it
            }
        }
        if (currentSong.audio != null) {
            storageRef.child(currentSong.audio!!).downloadUrl.addOnSuccessListener {
                initialAudioUri = it
            }
        }
        playerViewModel.setSongUiStateToLoading()
    }

    if (initialAudioUri != Uri.EMPTY) {
        val player = remember(initialAudioUri) {
            ExoPlayer.Builder(context).build().apply {
                repeatMode = Player.REPEAT_MODE_OFF
                shuffleModeEnabled = false
                setMediaItem(MediaItem.fromUri(initialAudioUri))
                prepare()
            }
        }

        if (audioUrisUiState is AudioUrisUiState.Success) {
            val startIndex = audioUris.indexOfFirst { it.first == currentSong.id }
            player.repeatMode = Player.REPEAT_MODE_ALL
            player.setMediaItems(
                audioUris.map { MediaItem.fromUri(it.second) },
                startIndex,
                0L
            )
            player.prepare()
            playerViewModel.setAudioUrisUiStateToLoading()
        }

        var playbackPosition by remember { mutableLongStateOf(0L) }
        var duration by remember { mutableLongStateOf(0L) }

        var isAudioPlaying by remember(initialAudioUri) {
            mutableStateOf(false)
        }

        DisposableEffect(player) {
            val listener = object : Player.Listener {
                override fun onPlaybackStateChanged(state: Int) {
                    if (state == Player.STATE_READY) {
                        duration = player.duration
                    }
                }

                override fun onIsPlayingChanged(isPlaying: Boolean) {
                    super.onIsPlayingChanged(isPlaying)
                    isAudioPlaying = isPlaying
                }

                override fun onMediaItemTransition(
                    mediaItem: MediaItem?,
                    reason: Int
                ) {
                    super.onMediaItemTransition(mediaItem, reason)
                    if (player.mediaItemCount > 1) {
                        currentSong = playlist[player.currentMediaItemIndex]
                        storageRef.child(currentSong.thumbnail!!).downloadUrl.addOnSuccessListener {
                            thumbnailUri = it
                        }
                    }
                }
            }

            player.addListener(listener)

            onDispose {
                player.removeListener(listener)
                player.release()
            }
        }

        LaunchedEffect(player) {
            while (true) {
                if (isAudioPlaying) {
                    playbackPosition = player.currentPosition
                }
                delay(1000L)
            }
        }

        if (isMinimized) {
            if (thumbnailUri != Uri.EMPTY) {
                MinimizedPlayer(
                    context = context,
                    thumbnail = thumbnailUri,
                    songName = currentSong.name ?: "",
                    songArtists = currentSong.artists?.joinToString(", ") ?: "",
                    isAudioPlaying = isAudioPlaying,
                    player = player,
                    position = (playbackPosition.toFloat() / duration.toFloat()),
                    modifier = modifier.clickable { isMinimized = false }
                )
            }
        } else {
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = { },
                        navigationIcon = {
                            IconButton(onClick = { isMinimized = true }) {
                                Icon(
                                    imageVector = Icons.Default.KeyboardArrowDown,
                                    contentDescription = "minimize_icon"
                                )
                            }
                        }
                    )
                }
            ) { contentPadding ->
                Column(
                    modifier = Modifier
                        .padding(contentPadding)
                        .padding(16.dp)
                        .fillMaxHeight(),
                    verticalArrangement = Arrangement.SpaceAround,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    if (thumbnailUri != Uri.EMPTY) {
                        AsyncImage(
                            model = ImageRequest.Builder(context).data(thumbnailUri).crossfade(true)
                                .build(),
                            contentDescription = "thumbnail",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(360.dp)
                                .clip(shape = RoundedCornerShape(24.dp)),
                        )
                    }

                    PlayerHeader(song = currentSong,
                        isFavorite = isFavorite,
                        onFavoriteClick = {
                            isFavorite = !isFavorite
                            favoriteViewModel.toggleFavorite("songs", it)
                        })

                    ExpandedAudioTrack(
                        duration = duration,
                        position = playbackPosition,
                        player = player
                    )

                    ExpandedAudioControl(isAudioPlaying = isAudioPlaying, player = player)
                }
            }
        }

    }

}

@Composable
fun PlayerHeader(
    song: Song,
    isFavorite: Boolean,
    onFavoriteClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(
                text = song.name ?: "",
                style = MaterialTheme.typography.headlineSmall,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )
            Text(
                song.artists?.joinToString(", ") ?: "",
                style = MaterialTheme.typography.bodyLarge,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
        IconButton(onClick = {
            onFavoriteClick(song.id)
        }) {
            Icon(
                painter = painterResource(id = if (isFavorite) R.drawable.ic_favorite_filled else R.drawable.ic_favorite_outlined),
                contentDescription = "favorite_con",
                tint = if (isFavorite) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.size(48.dp),
            )
        }
    }
}

@Composable
fun ExpandedAudioTrack(
    duration: Long,
    position: Long,
    player: ExoPlayer,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Slider(
            value = position.toFloat(),
            valueRange = 0f..duration.toFloat(),
            onValueChange = { player.seekTo(it.toLong()) },
            modifier = Modifier.fillMaxWidth()
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = formatTime(position),
                modifier = Modifier.padding(start = 8.dp)
            )
            Text(
                text = formatTime(duration),
                modifier = Modifier.padding(end = 8.dp)
            )
        }
    }
}

@Composable
fun ExpandedAudioControl(
    isAudioPlaying: Boolean,
    player: ExoPlayer,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier.fillMaxWidth()
    ) {
        IconButton(onClick = {
            if (player.repeatMode == Player.REPEAT_MODE_ONE) {
                if (player.mediaItemCount > 1)
                    player.repeatMode = Player.REPEAT_MODE_ALL
                else
                    player.repeatMode = Player.REPEAT_MODE_OFF
            } else
                player.repeatMode = Player.REPEAT_MODE_ONE
        }) {
            Icon(
                painter = painterResource(id = R.drawable.ic_repeat),
                contentDescription = stringResource(id = R.string.repeat),
                tint = if (player.repeatMode == Player.REPEAT_MODE_ONE) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.size(32.dp)
            )
        }
        IconButton(
            enabled = player.hasPreviousMediaItem(),
            onClick = { player.seekToPreviousMediaItem() }
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_skip_previous),
                contentDescription = stringResource(R.string.previous),
                modifier = Modifier.size(40.dp)
            )
        }
        IconButton(
            colors = IconButtonDefaults.iconButtonColors(
                contentColor = MaterialTheme.colorScheme.onPrimary,
                containerColor = MaterialTheme.colorScheme.primary
            ),
            modifier = Modifier
                .size(64.dp),
            onClick = {
                if (player.isPlaying) player.pause()
                else player.play()
            }) {
            Icon(
                painter = painterResource(id = if (isAudioPlaying) R.drawable.ic_pause else R.drawable.ic_play),
                contentDescription = stringResource(R.string.play),
                modifier = Modifier.size(48.dp)
            )

        }
        IconButton(
            enabled = player.hasNextMediaItem(),
            onClick = { player.seekToNextMediaItem() }
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_skip_next),
                contentDescription = stringResource(R.string.next),
                modifier = Modifier.size(40.dp)
            )
        }
        IconButton(
            enabled = player.mediaItemCount > 1,
            onClick = { player.shuffleModeEnabled = !player.shuffleModeEnabled }
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_shuffle),
                contentDescription = stringResource(id = R.string.shuffle),
                tint = if (player.shuffleModeEnabled) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.size(32.dp)
            )
        }
    }
}

@SuppressLint("DefaultLocale")
fun formatTime(ms: Long): String {
    val totalSeconds = ms / 1000
    val minutes = totalSeconds / 60
    val seconds = totalSeconds % 60
    return String.format("%02d:%02d", minutes, seconds)
}