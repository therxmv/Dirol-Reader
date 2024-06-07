package com.therxmv.dirolreader.ui.profile.view

import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.therxmv.dirolreader.ui.commonview.CenteredTopBar
import com.therxmv.dirolreader.ui.profile.viewmodel.ProfileViewModel
import com.therxmv.dirolreader.ui.profile.viewmodel.utils.ProfileUiEvent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    navController: NavController,
    onNavigateToTheming: () -> Unit,
    onNavigateToStorage: () -> Unit,
    onNavigateToAuth: () -> Unit,
    viewModel: ProfileViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsState()

    Scaffold(
        topBar = {
            if (state.appBarState.avatarPath.isNotBlank()) with(state.appBarState) {
                CenteredTopBar(
                    title = userName,
                    navController = navController,
                    actions = {
                        AppBarAvatar(avatarPath = avatarPath)
                    }
                )
            }
        }
    ) { padding ->
        ProfileScreenContent(
            screenPadding = padding,
            onNavigateToTheming = onNavigateToTheming,
            onNavigateToStorage = onNavigateToStorage,
            doSignOut = {
                viewModel.onEvent(ProfileUiEvent.LogOut(onNavigateToAuth))
            },
        )
    }
}

@Composable
private fun AppBarAvatar(
    avatarPath: String,
) {
    Image(
        bitmap = BitmapFactory.decodeFile(avatarPath).asImageBitmap(),
        contentDescription = "Avatar",
        modifier = Modifier
            .padding(8.dp)
            .width(48.dp)
            .height(48.dp)
            .clip(
                MaterialTheme.shapes.small
            )
    )
}