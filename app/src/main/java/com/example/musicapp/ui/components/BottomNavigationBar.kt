package com.example.musicapp.ui.components

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.example.musicapp.R
import com.example.musicapp.ui.navigation.DownloadDestination
import com.example.musicapp.ui.navigation.FavoriteDestination
import com.example.musicapp.ui.navigation.HomeDestination
import com.example.musicapp.ui.navigation.NavigationDestination
import com.example.musicapp.ui.navigation.PlaylistDestination

@Composable
fun BottomNavigationBar(
    modifier: Modifier = Modifier,
    currentDestination: NavigationDestination,
    onNavigate: (NavigationDestination) -> Unit
) {
    val homeIconId = if (currentDestination is HomeDestination) {
        R.drawable.ic_home_filled
    } else {
        R.drawable.ic_home_outlined
    }

    val favoriteIconId = if (currentDestination is FavoriteDestination) {
        R.drawable.ic_favorite_filled
    } else {
        R.drawable.ic_favorite_outlined
    }

    NavigationBar(
        modifier = modifier
    ) {
        NavigationBarItem(selected = (currentDestination is HomeDestination),
            onClick = { onNavigate(HomeDestination) },
            icon = {
                Icon(
                    painter = painterResource(id = homeIconId),
                    contentDescription = "home_icon"
                )
            },
            label = {
                Text(text = "Home")
            })
        NavigationBarItem(
            selected = (currentDestination is FavoriteDestination),
            onClick = { onNavigate(FavoriteDestination) },
            icon = {
                Icon(
                    painter = painterResource(id = favoriteIconId),
                    contentDescription = "favorite_icon"
                )
            },
            label = {
                Text(text = "Favorite")
            })
        NavigationBarItem(
            selected = (currentDestination is PlaylistDestination),
            onClick = { onNavigate(PlaylistDestination) },
            icon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_playlist),
                    contentDescription = "playlist_icon"
                )
            },
            label = {
                Text(text = "Playlist")
            }
        )
        NavigationBarItem(
            selected = (currentDestination is DownloadDestination),
            onClick = { onNavigate(DownloadDestination) },
            icon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_download),
                    contentDescription = "download_icon"
                )
            },
            label = {
                Text(text = "Download")
            })
    }
}