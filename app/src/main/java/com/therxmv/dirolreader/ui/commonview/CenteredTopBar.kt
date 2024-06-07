package com.therxmv.dirolreader.ui.commonview

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.therxmv.common.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CenteredTopBar(
    title: String,
    navController: NavController,
    actions: @Composable (RowScope.() -> Unit) = {},
) {
    CenterAlignedTopAppBar(
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
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
        },
        actions = actions,
    )
}

@Preview
@Composable
private fun CenteredTopBarPreview() {
    CenteredTopBar(title = "Title", navController = rememberNavController())
}