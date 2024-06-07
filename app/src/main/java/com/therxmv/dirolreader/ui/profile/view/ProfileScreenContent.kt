package com.therxmv.dirolreader.ui.profile.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.therxmv.common.R
import com.therxmv.dirolreader.BuildConfig

@Composable
fun ProfileScreenContent(
    screenPadding: PaddingValues,
    onNavigateToTheming: () -> Unit,
    onNavigateToStorage: () -> Unit,
    doSignOut: () -> Unit,
) {
    val uriHandler = LocalUriHandler.current
    var isDialogOpened by remember { mutableStateOf(false) }

    if (isDialogOpened) {
        SignOutDialog(
            onClick = {
                doSignOut()
            },
            onDismiss = {
                isDialogOpened = false
            }
        )
    }

    Column(
        modifier = Modifier
            .padding(screenPadding)
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween,
    ) {
        // TODO provide items(sealed/data) from VM
        ItemsTitle(
            text = stringResource(id = R.string.profile_settings),
        )
        Spacer(modifier = Modifier.height(10.dp))

        Item(
            text = stringResource(id = R.string.profile_theme),
            icon = painterResource(id = R.drawable.theme_icon),
            onClick = onNavigateToTheming,
        )
        Item(
            text = stringResource(id = R.string.profile_storage),
            icon = painterResource(id = R.drawable.storage_icon),
            onClick = onNavigateToStorage,
        )
        Spacer(modifier = Modifier.height(16.dp))

        ItemsTitle(
            text = stringResource(id = R.string.profile_about),
        )
        Spacer(modifier = Modifier.height(10.dp))

        Item(
            text = stringResource(id = R.string.profile_telegram),
            icon = painterResource(id = R.drawable.telegram_icon),
            onClick = {
                uriHandler.openUri("https://t.me/therxmv_channel")
            },
        )
        Item(
            text = stringResource(id = R.string.profile_github),
            icon = painterResource(id = R.drawable.github_icon),
            onClick = {
                uriHandler.openUri("https://github.com/therxmv/Dirol-Reader")
            },
        )
        Spacer(modifier = Modifier.weight(1f))


        Footer(
            openSignOutDialog = {
                isDialogOpened = true
            }
        )
    }
}

@Composable
private fun ItemsTitle(
    text: String,
) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleLarge,
        fontWeight = FontWeight.Bold
    )
}

@Composable
private fun Item(
    text: String,
    icon: Painter,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
            .padding(vertical = 4.dp)
            .clip(MaterialTheme.shapes.medium)
            .clickable {
                onClick()
            },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start,
    ) {
        Icon(
            modifier = Modifier.size(24.dp),
            painter = icon,
            contentDescription = "theme",
        )
        Spacer(modifier = Modifier.width(20.dp))

        Text(
            modifier = Modifier
                .fillMaxHeight()
                .padding(vertical = 8.dp),
            text = text,
            style = MaterialTheme.typography.bodyLarge,
            fontSize = 20.sp,
        )
    }
}

@Composable
private fun Footer(
    openSignOutDialog: () -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        ClickableText(
            text = stringResource(id = R.string.profile_sign_out),
            onClick = openSignOutDialog,
        )
        Text(
            text = "${stringResource(id = R.string.app_name)} ${BuildConfig.VERSION_NAME}",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.outline,
        )
    }
}

@Composable
private fun ClickableText(
    text: String,
    onClick: () -> Unit,
) {
    Text(
        modifier = Modifier
            .padding(bottom = 6.dp)
            .clickable {
                onClick()
            },
        text = text,
        style = MaterialTheme.typography.bodyLarge,
        color = MaterialTheme.colorScheme.error,
        textDecoration = TextDecoration.Underline,
    )
}

@Preview(showBackground = true)
@Composable
private fun ClickableTextPreview() {
    ClickableText(
        text = "Clickable text",
        onClick = {}
    )
}

@Preview(showBackground = true)
@Composable
private fun ItemsTitlePreview() {
    ItemsTitle(text = "Title")
}

@Preview(showBackground = true)
@Composable
private fun ItemPreview() {
    Item(
        text = "Item",
        icon = painterResource(id = R.drawable.theme_icon),
        onClick = {},
    )
}