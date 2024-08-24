package com.therxmv.dirolreader.ui.profile.view

import androidx.annotation.StringRes
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import com.therxmv.dirolreader.ui.profile.viewmodel.utils.ProfileUiSection
import kotlinx.collections.immutable.PersistentList

@Composable
fun ProfileScreenContent(
    screenPadding: PaddingValues,
    sections: PersistentList<ProfileUiSection>,
    onNavigateToRoute: (String) -> Unit,
    doSignOut: () -> Unit,
) {
    val scrollState = rememberScrollState()
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
            .fillMaxSize()
            .verticalScroll(scrollState),
    ) {
        sections.forEach {
            ProfileSection(
                title = it.title,
                items = it.items,
                onNavigateToRoute = onNavigateToRoute,
            )
        }

        Spacer(modifier = Modifier.weight(1f))
        Footer(
            openSignOutDialog = {
                isDialogOpened = true
            }
        )
    }
}

@Composable
private fun ProfileSection(
    @StringRes title: Int,
    items: PersistentList<ProfileUiSection.Item>,
    onNavigateToRoute: (String) -> Unit,
) {
    val uriHandler = LocalUriHandler.current

    ItemsTitle(
        text = stringResource(id = title),
    )
    Spacer(modifier = Modifier.height(10.dp))

    items.forEach {
        Item(
            text = stringResource(id = it.name),
            icon = painterResource(id = it.icon),
            onClick = {
                when (it.onClick) {
                    is ProfileUiSection.ItemClick.Navigate -> {
                        onNavigateToRoute(it.onClick.route)
                    }

                    is ProfileUiSection.ItemClick.OpenBrowser -> {
                        uriHandler.openUri(it.onClick.link)
                    }
                }
            },
        )
    }
    Spacer(modifier = Modifier.height(16.dp))
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