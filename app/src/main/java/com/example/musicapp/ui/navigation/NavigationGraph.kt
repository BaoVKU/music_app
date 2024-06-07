package com.example.musicapp.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.musicapp.ui.screens.AlbumDetailScreen
import com.example.musicapp.ui.screens.AlbumListScreen
import com.example.musicapp.ui.screens.ConcertDetailScreen
import com.example.musicapp.ui.screens.ConcertListScreen
import com.example.musicapp.ui.screens.FavoriteScreen
import com.example.musicapp.ui.screens.HomeScreen
import com.example.musicapp.ui.screens.LoginScreen
import com.example.musicapp.ui.screens.SongListScreen
import com.example.musicapp.ui.screens.PlaylistScreen
import com.example.musicapp.ui.screens.PlayerScreen
import com.example.musicapp.ui.screens.PlaylistDetailScreen
import com.example.musicapp.ui.screens.RegisterScreen
import com.example.musicapp.ui.screens.SearchScreen

@Composable
fun NavigationGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    Box {
        var isShowPlayer by remember {
            mutableStateOf(false)
        }
        var isNavBarHavingScreen by remember {
            mutableStateOf(false)
        }
        var songQuery by remember {
            mutableStateOf("")
        }
        NavHost(
            navController = navController,
            startDestination = LoginDestination.route,
            modifier = modifier
        ) {
            composable(route = LoginDestination.route) {
                LoginScreen(
                    onNavigate = { navController.navigate(it.route) }
                )
                isShowPlayer = false
                isNavBarHavingScreen = false
            }

            composable(route = RegisterDestination.route) {
                RegisterScreen(
                    onNavigate = { navController.navigate(it.route) }
                )
                isNavBarHavingScreen = false
            }
            composable(route = HomeDestination.route) {
                HomeScreen(
                    onNavigate = { navController.navigate(it.route) },
                    onNavigateWithArgument = { destination, id ->
                        navController.navigate("${destination.route}/$id")
                    },
                    onSongQueryChanged = {
                        songQuery = it
                    }
                )
                isShowPlayer = true
                isNavBarHavingScreen = true
            }

            composable(route = FavoriteDestination.route) {
                FavoriteScreen(
                    onNavigate = { navController.navigate(it.route) },
                    onNavigateWithArgument = { destination, id ->
                        navController.navigate("${destination.route}/$id")
                    },
                    onSongQueryChanged = {
                        songQuery = it
                    }
                )
                isNavBarHavingScreen = true
            }

            composable(route = PlaylistDestination.route) {
                PlaylistScreen(
                    onNavigate = { navController.navigate(it.route) },
                    onNavigateWithArgument = { destination, id ->
                        navController.navigate("${destination.route}/$id")
                    }
                )
                isNavBarHavingScreen = true
            }

            composable(route = ConcertListDestination.route) {
                ConcertListScreen(
                    onNavigate = { navController.navigate(it.route) },
                    onNavigateWithArgument = { destination, id ->
                        navController.navigate("${destination.route}/$id")
                    },
                    onNavigateBack = { navController.navigateUp() }
                )
                isNavBarHavingScreen = false
            }

            composable(route = AlbumListDestination.route) {
                AlbumListScreen(
                    onNavigate = { navController.navigate(it.route) },
                    onNavigateWithArgument = { destination, id ->
                        navController.navigate("${destination.route}/$id")
                    },
                    onNavigateBack = { navController.navigateUp() }
                )
                isNavBarHavingScreen = false
            }

            composable(route = SongListDestination.route) {
                SongListScreen(
                    onNavigate = { navController.navigate(it.route) },
                    onSongQueryChanged = {
                        songQuery = it
                    },
                    onNavigateBack = { navController.navigateUp() }
                )
                isNavBarHavingScreen = false
            }

            composable(route = SearchDestination.route) {
                SearchScreen(
                    onNavigate = { navController.navigate(it.route) },
                    onNavigateWithArgument = { destination, id ->
                        navController.navigate("${destination.route}/$id")
                    },
                    onNavigateBack = { navController.navigateUp() },
                    onSongQueryChanged = {
                        songQuery = it
                    }
                )
                isNavBarHavingScreen = false
            }

            composable(
                route = AlbumDetailDestination.routeWithArgument,
                arguments = listOf(navArgument("albumId") {
                    type = NavType.StringType
                })
            ) {
                AlbumDetailScreen(
                    onNavigate = { navController.navigate(it.route) },
                    onNavigateWithArgument = { destination, id ->
                        navController.navigate("${destination.route}/$id")
                    },
                    onNavigateBack = { navController.navigateUp() },
                    onSongQueryChanged = {
                        songQuery = it
                    }
                )
                isNavBarHavingScreen = false
            }

            composable(
                route = ConcertDetailDestination.routeWithArgument,
                arguments = listOf(navArgument("concertId") {
                    type = NavType.StringType
                })
            ) {
                ConcertDetailScreen(
                    onNavigateBack = { navController.navigateUp() }
                )
                isNavBarHavingScreen = false
            }

            composable(
                route = PlaylistDetailDestination.routeWithArgument,
                arguments = listOf(navArgument("playlistId") {
                    type = NavType.StringType
                })
            ) {
                PlaylistDetailScreen(
                    onNavigate = { navController.navigate(it.route) },
                    onNavigateWithArgument = { destination, id ->
                        navController.navigate("${destination.route}/$id")
                    },
                    onNavigateBack = { navController.navigateUp() },
                    onSongQueryChanged = {
                        songQuery = it
                    }
                )
                isNavBarHavingScreen = false
            }
        }
        if (isShowPlayer) {
            val padding = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()
            PlayerScreen(
                songQuery = songQuery,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = if (isNavBarHavingScreen) padding + 82.dp else padding)
            )
        }
    }
}