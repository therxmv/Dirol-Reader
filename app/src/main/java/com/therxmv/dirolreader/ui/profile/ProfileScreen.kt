package com.therxmv.dirolreader.ui.profile

import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.therxmv.dirolreader.BuildConfig
import com.therxmv.dirolreader.R
import com.therxmv.dirolreader.ui.profile.utils.ProfileUiEvent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    navController: NavController,
    onNavigateToTheming: () -> Unit,
    onNavigateToStorage: () -> Unit,
    onNavigateToAuth: () -> Unit,
    viewModel: ProfileViewModel = hiltViewModel(),
) {
    val state = viewModel.state.collectAsState().value
    val uriHandler = LocalUriHandler.current
    var isDialogOpened by remember { mutableStateOf(false)  }

    Scaffold(
        topBar = {
            if (state.appBarState.avatarPath.isNotBlank()) with(state.appBarState) {
                CenterAlignedTopAppBar(
                    navigationIcon = {
                        if (navController.previousBackStackEntry != null) {
                            IconButton(onClick = { navController.navigateUp() }) {
                                Icon(
                                    painter = painterResource(id = R.drawable.back_arrow_icon),
                                    contentDescription = "Back"
                                )
                            }
                        }
                    },
                    title = {
                        Text(
                            text = userName,
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold
                        )
                    },
                    actions = {
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
                )
            }
        }
    ) { padding ->
        if(isDialogOpened) {
            AlertDialog(
                shape = MaterialTheme.shapes.medium,
                onDismissRequest = { isDialogOpened = false },
                confirmButton = {
                    TextButton(
                        onClick = {
                            isDialogOpened = false
                            viewModel.onEvent(ProfileUiEvent.LogOut(onNavigateToAuth))
                        }
                    ) {
                        Text(text = stringResource(id = R.string.auth_dialog_confirm))
                    }
                },
                title = {
                    Text(text = stringResource(id = R.string.sign_out_dialog_title))
                },
                text = {
                    Text(text = stringResource(id = R.string.sign_out_dialog_text))
                }
            )
        }

        Column(
            modifier = Modifier
                .padding(padding)
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween,
        ) {
            Column {
                Text(
                    modifier = Modifier.padding(bottom = 10.dp),
                    text = stringResource(id = R.string.profile_settings),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(IntrinsicSize.Min)
                        .padding(vertical = 4.dp)
                        .clip(MaterialTheme.shapes.medium)
                        .clickable {
                            onNavigateToTheming()
                        },
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        modifier = Modifier.size(24.dp),
                        painter = painterResource(id = R.drawable.theme_icon),
                        contentDescription = "theme"
                    )
                    Text(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .padding(horizontal = 20.dp, vertical = 8.dp),
                        text = stringResource(id = R.string.profile_theme),
                        style = MaterialTheme.typography.bodyLarge,
                        fontSize = 20.sp
                    )
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(IntrinsicSize.Min)
                        .padding(vertical = 4.dp)
                        .clip(MaterialTheme.shapes.medium)
                        .clickable {
                            onNavigateToStorage()
                        },
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        modifier = Modifier.size(24.dp),
                        painter = painterResource(id = R.drawable.storage_icon),
                        contentDescription = "theme"
                    )
                    Text(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .padding(horizontal = 20.dp, vertical = 8.dp),
                        text = stringResource(id = R.string.profile_storage),
                        style = MaterialTheme.typography.bodyLarge,
                        fontSize = 20.sp
                    )
                }

                Text(
                    modifier = Modifier.padding(top = 16.dp, bottom = 10.dp),
                    text = stringResource(id = R.string.profile_about),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(IntrinsicSize.Min)
                        .padding(vertical = 4.dp)
                        .clip(MaterialTheme.shapes.medium)
                        .clickable {
                            uriHandler.openUri("https://t.me/therxmv_channel")
                        },
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        modifier = Modifier.size(24.dp),
                        painter = painterResource(id = R.drawable.telegram_icon),
                        contentDescription = "telegram"
                    )
                    Text(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .padding(horizontal = 20.dp, vertical = 8.dp),
                        text = stringResource(id = R.string.profile_telegram),
                        style = MaterialTheme.typography.bodyLarge,
                        fontSize = 20.sp
                    )
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(IntrinsicSize.Min)
                        .padding(vertical = 4.dp)
                        .clip(MaterialTheme.shapes.medium)
                        .clickable {
                            uriHandler.openUri("https://github.com/therxmv/Dirol-Reader")
                        },
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        modifier = Modifier.size(24.dp),
                        painter = painterResource(id = R.drawable.github_icon),
                        contentDescription = "github"
                    )
                    Text(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .padding(horizontal = 20.dp, vertical = 8.dp),
                        text = stringResource(id = R.string.profile_github),
                        style = MaterialTheme.typography.bodyLarge,
                        fontSize = 20.sp
                    )
                }
            }

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    modifier = Modifier
                        .padding(bottom = 6.dp)
                        .clickable {
                            isDialogOpened = true
                        },
                    text = stringResource(id = R.string.profile_sign_out),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.error,
                    textDecoration = TextDecoration.Underline
                )
                Text(
                    text = "${stringResource(id = R.string.app_name)} ${BuildConfig.VERSION_NAME}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.outline,
                )
            }
        }
    }
}