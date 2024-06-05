package com.example.musicapp.ui.navigation

interface NavigationDestination {
    val route: String
    val title: String
}

object LoginDestination : NavigationDestination {
    override val route: String = "login"
    override val title: String = "Login"
}

object RegisterDestination : NavigationDestination {
    override val route: String = "register"
    override val title: String = "Register"
}

object HomeDestination : NavigationDestination {
    override val route: String = "home"
    override val title: String = "Home"
}

object FavoriteDestination : NavigationDestination {
    override val route: String = "favorite"
    override val title: String = "Favorite"
}

object PlaylistDestination : NavigationDestination {
    override val route: String = "playlist"
    override val title: String = "Playlist"
}

object DownloadDestination : NavigationDestination {
    override val route: String = "download"
    override val title: String = "Download"
}

object ConcertListDestination : NavigationDestination {
    override val route: String = "concert"
    override val title: String = "Concert"
}

object AlbumListDestination : NavigationDestination {
    override val route: String = "album"
    override val title: String = "Albums"
}

object SongListDestination : NavigationDestination {
    override val route: String = "song"
    override val title: String = "Songs"
}

object SearchDestination : NavigationDestination {
    override val route: String = "search"
    override val title: String = "Search"
}

object AlbumDetailDestination : NavigationDestination {
    override val route: String = "album_detail"
    override val title: String = "Album Detail"
    val routeWithArgument: String = "album_detail/{albumId}"
}

object ConcertDetailDestination : NavigationDestination {
    override val route: String = "concert_detail"
    override val title: String = "Concert Detail"
    val routeWithArgument: String = "concert_detail/{concertId}"
}

object PlaylistDetailDestination : NavigationDestination {
    override val route: String = "playlist_detail"
    override val title: String = "Playlist Detail"
    val routeWithArgument: String = "playlist_detail/{playlistId}"
}