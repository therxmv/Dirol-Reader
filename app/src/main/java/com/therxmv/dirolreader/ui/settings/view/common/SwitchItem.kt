package com.therxmv.dirolreader.ui.settings.view.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SwitchItem(
    text: String,
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(
            modifier = Modifier
                .padding(vertical = 8.dp),
            text = text,
            style = MaterialTheme.typography.bodyLarge,
            fontSize = 20.sp
        )
        Switch(
            checked = isChecked,
            onCheckedChange = onCheckedChange,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun UncheckedSwitchItemPreview() {
    SwitchItem(
        text = "Switch1",
        isChecked = false,
        onCheckedChange = {}
    )
}

@Preview(showBackground = true)
@Composable
private fun CheckedSwitchItemPreview() {
    SwitchItem(
        text = "Switch2",
        isChecked = true,
        onCheckedChange = {}
    )
}