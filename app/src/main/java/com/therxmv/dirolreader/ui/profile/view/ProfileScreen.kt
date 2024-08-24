package com.therxmv.dirolreader.ui.profile.view

import android.graphics.BitmapFactory
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.therxmv.dirolreader.ui.commonview.CenteredBoxLoader
import com.therxmv.dirolreader.ui.commonview.CenteredTopBar
import com.therxmv.dirolreader.ui.commonview.DefaultTitle
import com.therxmv.dirolreader.ui.news.view.post.EmptyAvatar
import com.therxmv.dirolreader.ui.profile.viewmodel.ProfileViewModel
import com.therxmv.dirolreader.ui.profile.viewmodel.utils.AppBarState
import com.therxmv.dirolreader.ui.profile.viewmodel.utils.ProfileUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    navController: NavController,
    onNavigateToRoute: (String) -> Unit,
    eraseApplication: () -> Unit,
    viewModel: ProfileViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()
    val event = viewModel.eventFlow.collectAsState().value

    Scaffold(
        topBar = {
            if (uiState is ProfileUiState.Ready) {
                val appBar = (uiState as ProfileUiState.Ready).appBarState
                CenteredTopBar(
                    title = {
                        DefaultTitle(title = appBar.userName)
                    },
                    navController = navController,
                    actions = {
                        AppBarAvatar(state = appBar)
                    }
                )
            }
        }
    ) { padding ->
        Crossfade(
            targetState = uiState,
            label = "content",
        ) {
            when (it) {
                is ProfileUiState.Ready -> ProfileScreenContent(
                    screenPadding = padding,
                    sections = it.sections,
                    onNavigateToRoute = onNavigateToRoute,
                    doSignOut = viewModel::signOut,
                )

                is ProfileUiState.Loading -> CenteredBoxLoader()
            }
        }
    }

    LaunchedEffect(event) {
        if (event == null) return@LaunchedEffect

        when (event) {
            is ProfileViewModel.Event.SignedOut -> {
                eraseApplication()
            }
        }
    }
}

@Composable
private fun AppBarAvatar(
    state: AppBarState,
) {
    if (state.avatarPath.isNotBlank()) {
        Image(
            bitmap = BitmapFactory.decodeFile(state.avatarPath).asImageBitmap(),
            contentDescription = "Avatar",
            modifier = Modifier
                .width(48.dp)
                .height(48.dp)
                .clip(
                    MaterialTheme.shapes.small,
                )
        )
    } else {
        EmptyAvatar(name = state.userName)
    }
}