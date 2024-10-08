package com.therxmv.dirolreader.ui.commonview

import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.therxmv.common.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CenteredTopBar(
    title: @Composable () -> Unit,
    navController: NavController,
    actions: @Composable (RowScope.() -> Unit) = {},
) {
    CenterAlignedTopAppBar(
        modifier = Modifier
            .padding(
                end = 8.dp,
                bottom = 10.dp,
                top = 10.dp,
            ),
        navigationIcon = {
            if (navController.previousBackStackEntry != null) {
                IconButton(onClick = navController::navigateUp) {
                    Icon(
                        painter = painterResource(id = R.drawable.back_arrow_icon),
                        contentDescription = "Back"
                    )
                }
            }
        },
        title = title,
        actions = actions,
    )
}

@Composable
fun DefaultTitle(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.headlineSmall,
        fontWeight = FontWeight.Bold,
    )
}

@Preview
@Composable
private fun CenteredTopBarPreview() {
    CenteredTopBar(
        title = {
            DefaultTitle("Title")
        },
        navController = rememberNavController(),
    )
}