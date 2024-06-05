package com.example.musicapp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.musicapp.ui.navigation.ConcertDetailDestination
import com.example.musicapp.ui.navigation.NavigationDestination

@Composable
fun ConcertItem(
    modifier: Modifier = Modifier,
    onNavigateWithArgument: (NavigationDestination, String) -> Unit
) {
    Row(
        modifier = modifier
            .padding(8.dp)
            .clickable { onNavigateWithArgument(ConcertDetailDestination, "id") }) {
        Column(modifier = Modifier.width(52.dp)) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = Color.DarkGray)
            ) {
                Text(text = "AUG", fontSize = 14.sp, color = Color.White)
            }
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = Color.LightGray)
                    .height(32.dp)
            ) {
                Text(text = "12", fontSize = 18.sp, fontWeight = FontWeight(700))
            }
        }
        Column(modifier = Modifier.padding(start = 8.dp)) {
            Text(
                text = "Concert name",
                fontSize = 18.sp,
                fontWeight = FontWeight(500)
            )
            Text(text = "Location", fontSize = 14.sp)
        }
    }
}