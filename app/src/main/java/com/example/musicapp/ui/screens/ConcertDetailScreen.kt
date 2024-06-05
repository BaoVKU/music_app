package com.example.musicapp.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.musicapp.R

@Composable
fun ConcertDetailScreen(
    modifier: Modifier = Modifier,
    onNavigateBack: () -> Unit
) {
    LazyColumn(
        modifier = modifier
    ) {
        item {
            Box(contentAlignment = Alignment.BottomStart) {
                Image(
                    painter = painterResource(id = R.drawable.squirrel),
                    contentDescription = "concert_image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxWidth()
                )
                IconButton(
                    onClick = onNavigateBack, modifier = Modifier.align(
                        Alignment.TopStart
                    )
                ) {
                    Icon(
                        imageVector = Icons.Filled.KeyboardArrowLeft,
                        contentDescription = "back_icon",
                        tint = Color.White
                    )
                }
                Text(
                    text = "Concert name",
                    fontSize = 28.sp,
                    fontWeight = FontWeight(500),
                    color = Color.White,
                    style = TextStyle(
                        shadow = Shadow(
                            color = Color.Black,
                            offset = Offset(x = 4f, y = 4f),
                            blurRadius = 4f
                        )
                    ),
                    modifier = Modifier.padding(8.dp)
                )
            }
        }
        item {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(8.dp)
            ) {
                Row {
                    Text(text = "Date: ", fontWeight = FontWeight(500))
                    Text(text = "21/02/2024")
                }
                Row {
                    Text(text = "Time: ", fontWeight = FontWeight(500))
                    Text(text = "21:00")
                }
                Row {
                    Text(text = "Location: ", fontWeight = FontWeight(500))
                    Text(text = "Vietnam")
                }
                Row {
                    Text(text = "Price: ", fontWeight = FontWeight(500))
                    Text(text = "$100")
                }
                Row {
                    Text(text = "Seating capacity: ", fontWeight = FontWeight(500))
                    Text(text = "1000")
                }
                val artistText = buildAnnotatedString {
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                        append("Artists: ")
                    }
                    append("Artist 1, Artist 2, Artist 3")
                }
                Text(text = artistText)
                val descriptionText = buildAnnotatedString {
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                        append("Description: ")
                    }
                    append("Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.")
                }
                Text(text = descriptionText, textAlign = TextAlign.Justify)
            }
        }
    }
}