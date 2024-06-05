package com.example.musicapp.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.musicapp.R
import com.example.musicapp.ui.navigation.LoginDestination
import com.example.musicapp.ui.navigation.NavigationDestination
import com.example.musicapp.ui.navigation.SearchDestination
import com.example.musicapp.ui.viewmodels.TopBarViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    modifier: Modifier = Modifier,
    title: String,
    canNavigateBack: Boolean = false,
    hasActions: Boolean = true,
    onNavigate: (NavigationDestination) -> Unit,
    onNavigateBack: () -> Unit = {},
    topBarViewModel: TopBarViewModel = viewModel()
) {
    val context = LocalContext.current
    var isAccountMenuExpanded by remember { mutableStateOf(false) }
    TopAppBar(
        title = {
            Text(text = title, style = MaterialTheme.typography.titleLarge)
        },
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = onNavigateBack) {
                    Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "back_icon")
                }
            }
        },
        actions = {
            if(hasActions){
                Row {
                    IconButton(onClick = { onNavigate(SearchDestination) }) {
                        Icon(imageVector = Icons.Filled.Search, contentDescription = "search_icon")
                    }
                    Column {
                        IconButton(onClick = {
                            isAccountMenuExpanded = !isAccountMenuExpanded
                        }) {
                            Icon(
                                imageVector = Icons.Filled.AccountCircle,
                                contentDescription = "account_icon"
                            )
                        }
                        DropdownMenu(
                            expanded = isAccountMenuExpanded,
                            onDismissRequest = { isAccountMenuExpanded = false }) {
                            DropdownMenuItem(text = {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.ic_logout),
                                        contentDescription = "logout_icon",
                                        tint = MaterialTheme.colorScheme.error
                                    )
                                    Text(
                                        text = "Logout",
                                        color = MaterialTheme.colorScheme.error,

                                        modifier = Modifier.padding(start = 8.dp)
                                    )
                                }
                            }, onClick = {
                                topBarViewModel.logout(context = context)
                                isAccountMenuExpanded = false
                                onNavigate(LoginDestination)
                            })
                        }

                    }
                }
            }
        }, modifier = modifier
    )
}