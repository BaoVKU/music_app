package com.example.musicapp

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.musicapp.ui.navigation.NavigationGraph
import com.google.accompanist.systemuicontroller.SystemUiController
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@Composable
fun MusicApp(
    navController: NavHostController = rememberNavController()
){
    NavigationGraph(navController = navController)

    //change system bar color
    val systemUiController: SystemUiController = rememberSystemUiController()
    systemUiController.setSystemBarsColor(color = Color.Transparent)
}