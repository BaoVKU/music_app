package com.example.musicapp.ui.screens

import android.net.Uri
import android.util.Log
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.musicapp.R
import com.example.musicapp.model.Concert
import com.example.musicapp.ui.viewmodels.ConcertUiState
import com.example.musicapp.ui.viewmodels.ConcertViewModel
import com.google.firebase.Firebase
import com.google.firebase.Timestamp
import com.google.firebase.storage.storage
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun ConcertDetailScreen(
    modifier: Modifier = Modifier,
    onNavigateBack: () -> Unit,
    concertViewModel: ConcertViewModel = viewModel()
) {
    val context = LocalContext.current
    val concertUiState by concertViewModel.concertUiState.collectAsState()
    var concert by remember { mutableStateOf(Concert()) }
    var bannerUri by remember {
        mutableStateOf<Uri>(Uri.EMPTY)
    }

    if (concertUiState is ConcertUiState.Success) {
        concert = (concertUiState as ConcertUiState.Success).concert
        if (concert.banner != null) {
            Firebase.storage.reference.child(concert.banner!!).downloadUrl.addOnSuccessListener {
                bannerUri = it
            }
        }
    }

    LazyColumn(
        modifier = modifier
    ) {
        item {
            Box(contentAlignment = Alignment.BottomStart) {
                if (bannerUri != Uri.EMPTY) {

                    AsyncImage(
                        model = ImageRequest.Builder(context)
                            .data(bannerUri)
                            .crossfade(true)
                            .build(),
                        contentDescription = "concert_image",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
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
                if (concertUiState is ConcertUiState.Success) {
                    Text(
                        text = concert.name ?: "",
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
        }
        if (concertUiState is ConcertUiState.Success) {
            val timePair = convertTimestampToTimeAndDate(concert.date)
            item {
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.padding(8.dp)
                ) {
                    Row {
                        Text(text = "Date: ", fontWeight = FontWeight(500))
                        Text(text = timePair?.second?: "Unknown")
                    }
                    Row {
                        Text(text = "Time: ", fontWeight = FontWeight(500))
                        Text(text = timePair?.first?: "Unknown")
                    }
                    Row {
                        Text(text = "Location: ", fontWeight = FontWeight(500))
                        Text(text = concert.location ?: "Unknown")
                    }
                    Row {
                        Text(text = "Price: ", fontWeight = FontWeight(500))
                        Text(text = concert.ticket?.toString() ?: "Unknown")
                    }
                    Row {
                        Text(text = "Seating capacity: ", fontWeight = FontWeight(500))
                        Text(text = concert.capacity?.toString() ?: "Unknown")
                    }
                    val artistText = buildAnnotatedString {
                        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                            append("Artists: ")
                        }
                        append(concert.artists?.joinToString(", ") ?: "Unknown")
                    }
                    Text(text = artistText)
                    val descriptionText = buildAnnotatedString {
                        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                            append("Description: ")
                        }
                        append(concert.description ?: "No description available")
                    }
                    Text(text = descriptionText, textAlign = TextAlign.Justify)
                }
            }
        }
    }
}

fun convertTimestampToTimeAndDate(timestamp: Timestamp?): Pair<String, String>? {
    if (timestamp == null) return null
    val date = timestamp.toDate() // Convert the Timestamp to a Date

    val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
    val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    val time = timeFormat.format(date)
    val dateStr = dateFormat.format(date)

    return Pair(time, dateStr)
}