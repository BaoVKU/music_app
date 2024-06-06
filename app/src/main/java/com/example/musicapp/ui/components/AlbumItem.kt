package com.example.musicapp.ui.components

import android.net.Uri
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.musicapp.R
import com.example.musicapp.model.Album
import com.example.musicapp.ui.navigation.AlbumDetailDestination
import com.example.musicapp.ui.navigation.NavigationDestination
import com.example.musicapp.ui.viewmodels.FavoriteViewModel
import com.google.firebase.Firebase
import com.google.firebase.storage.storage

@Composable
fun AlbumItem(
    album: Album,
    onNavigateWithArgument: (NavigationDestination, String) -> Unit,
    favoriteViewModel: FavoriteViewModel = viewModel()
) {
    val context = LocalContext.current
    var thumbnailUri by remember {
        mutableStateOf<Uri>(Uri.EMPTY)
    }

    Firebase.storage.reference.child(album.thumbnail!!).downloadUrl.addOnSuccessListener {
        thumbnailUri = it
    }

    var isFavorite by remember {
        mutableStateOf(album.isFavorite)
    }

    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .height(64.dp)
            .padding(8.dp)
            .fillMaxWidth()
            .animateContentSize(
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessLow
                )
            )
            .clickable { onNavigateWithArgument(AlbumDetailDestination, album.id!!) }
    ) {
        if (thumbnailUri != Uri.EMPTY) {
            AsyncImage(
                model = ImageRequest.Builder(context).data(thumbnailUri).crossfade(true).build(),
                contentDescription = "album",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(48.dp)
                    .clip(shape = RoundedCornerShape(8.dp))
            )
        }
        Text(
            text = album.name ?: "",
            style = MaterialTheme.typography.titleMedium,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .weight(1f)
                .padding(start = 8.dp)
        )
        IconButton(onClick = {
            isFavorite = !isFavorite
            favoriteViewModel.toggleFavorite("albums", album.id!!)
        }) {
            Icon(
                painter = painterResource(id = if (isFavorite) R.drawable.ic_favorite_filled else R.drawable.ic_favorite_outlined),
                contentDescription = "favorite_icon",
                tint = if (isFavorite) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onBackground
            )
        }
    }
}