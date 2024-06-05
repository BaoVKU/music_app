package com.example.musicapp.ui.components


import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.media3.exoplayer.ExoPlayer
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.musicapp.MusicApp
import com.example.musicapp.R
import com.example.musicapp.ui.navigation.HomeDestination
import com.example.musicapp.ui.theme.MusicAppTheme

@Composable
fun MinimizedPlayer(
    context: Context,
    thumbnail: Uri,
    songName: String,
    songArtists: String,
    isAudioPlaying: Boolean,
    position: Float,
    player: ExoPlayer,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .padding(2.dp)
            .fillMaxWidth()
            .clip(shape = RoundedCornerShape(8.dp))
            .background(color = MaterialTheme.colorScheme.primary)
    ) {
        Row(
            modifier = Modifier.padding(8.dp)
        ) {
            AsyncImage(
                model = ImageRequest.Builder(context).data(thumbnail).crossfade(true).build(),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(8.dp))
            )
//            Image(
//                painter = painterResource(id = R.drawable.squirrel), contentDescription = null,
//                contentScale = ContentScale.Crop,
//                modifier = Modifier
//                    .size(48.dp)
//                    .clip(RoundedCornerShape(8.dp))
//            )
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 8.dp)
            ) {
                Text(
                    text = songName,
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onPrimary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = songArtists,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onPrimary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            IconButton(
                onClick = {
                    if (player.isPlaying) player.pause()
                    else player.play()
                }
            ) {
                Icon(
                    painter = painterResource(id = if (isAudioPlaying) R.drawable.ic_pause else R.drawable.ic_play),
                    contentDescription = stringResource(R.string.next),
                    tint = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.size(48.dp)
                )
            }
        }
        Box(
            modifier = Modifier
                .fillMaxWidth(position)
                .height(2.dp)
                .background(color = MaterialTheme.colorScheme.onPrimary)
        )
    }
}

//@Preview
//@Composable
//fun MinimizedPlayerPreview() {
//    MusicAppTheme {
//        Surface(
//            modifier = Modifier.fillMaxSize(),
//            color = MaterialTheme.colorScheme.background
//        ) {
//            Box {
//                Scaffold(
//                    bottomBar = {
//                        BottomNavigationBar(currentDestination = HomeDestination) {
//
//                        }
//                    }
//                ) {
//                    it
//                }
//                MinimizedPlayer(
//                    context = LocalContext.current,
//                    thumbnail = Uri.EMPTY,
//                    songName = "Song Name",
//                    songArtists = "Song Artists",
//                    player = ExoPlayer.Builder(LocalContext.current).build(),
//                    isAudioPlaying = false,
//                    modifier = Modifier.align(Alignment.BottomCenter)
//                )
//            }
//        }
//    }
//}