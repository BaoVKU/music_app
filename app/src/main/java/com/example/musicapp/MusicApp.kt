package com.example.musicapp

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.musicapp.ui.navigation.NavigationGraph

@Composable
fun MusicApp(
    navController: NavHostController = rememberNavController()
){
    NavigationGraph(navController = navController)
}